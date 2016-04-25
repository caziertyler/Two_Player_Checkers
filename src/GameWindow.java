package CheckersClient;

import GameServer.game.GameType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Creates a window and a server connection for an online Checkers game.
 *
 * @author Tyler Cazier
 * @version 8/13/15
 */
public class GameWindow extends JFrame {
    private Image imageBackground;
    private Image imageGameBoard;
    private ImageIcon iconGameBoard;

    private SpringLayout layoutMain;
    private SpringLayout layoutContent;
    private SpringLayout layoutCenter;
    private SpringLayout layoutSendChat;

    private BackgroundImagePanel panelMain;
    private JPanel panelContent;
    private JPanel panelCenter; // Horizontal-Center
    private GameBoard gameBoard; // Center-North
    private JPanel panelSendChat; // Center-South

    private JMenuBar menubarGame;
    private JMenu menuGame;
    private JMenuItem menuItemConnectDisconnectServer;
    private JMenuItem menuItemNewGame;
    private JMenuItem menuItemUpdateUsername;
    private JMenuItem menuItemUpdateServer;
    private JMenuItem menuItemQuitGame;
    private JMenuItem menuItemExit;

    private JTextArea textServerMessage; //West
    private JTextArea textChatMessage; //East
    private JTextArea textSendChat;
    boolean shouldClearText;

    private JScrollPane scrollServerMessage;
    private JScrollPane scrollChatMessage;
    private JScrollPane scrollSendChat;

    private JButton buttonSendMessage;

    // Server Information
    private ClientConnection connectionGame;
    private int portNum;
    private GameType gameType;
    private String ipAddress;
    private String username;

    /**
     * Constructor - Default and Only
     */
    private GameWindow() {

        /* Initialize Images */
        initializeImages();

        /* TOP-LEVEL WINDOW */
        setTitle("Checkers!!!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1126, 697));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2 - 20);
        ((JComponent) getContentPane()).setOpaque(false);

            /* CONTENT CONTAINER */
            initializeContent();

        this.pack();
        this.setVisible(true);
        this.setResizable(false);

        // Default Game Settings
        ipAddress = "localhost";
        portNum = 8989;
        gameType = null;
        username = null;
    }

    // Constructor Helpers

    /**
     * Initialize background and gameboard images.
     */
    private void initializeImages() {
        try {
            imageBackground = ImageIO.read(new File("res\\BackgroundImage3.png"));
        } catch (IOException e) {
            System.out.println("Background image not found.");
        }

        try {
            if (gameType == null || gameType.equals(GameType.CHECKERS)) {
                imageGameBoard = ImageIO.read(new File("res\\CheckerBoard4.png"));
                iconGameBoard = new ImageIcon(imageGameBoard);
            }
        } catch (IOException e) {
            System.out.println("Background image not found.");
        }
    }

    /**
     * Initializes all content in this JFrame.
     */
    private void initializeContent() {
        panelMain = new BackgroundImagePanel(imageBackground);
        layoutMain = new SpringLayout();
        panelMain.setLayout(layoutMain);
        panelMain.setOpaque(false);
        this.add(panelMain);

                /* MENU */
        initializeDropdownMenu();

                /* CONTENT PANEL */
        initializeMainContentArea();

        layoutMain.putConstraint(SpringLayout.NORTH, panelContent, 20, SpringLayout.NORTH, panelMain);
    }

    /**
     * Initialize main window dropdown menu.
     */
    private void initializeDropdownMenu() {
        menubarGame = new JMenuBar();
        menubarGame.setPreferredSize(new Dimension(1120, 20));
        panelMain.add(menubarGame, SpringLayout.NORTH);

        menuGame = new JMenu("Menu");
        menubarGame.add(menuGame);

        menuItemNewGame = new JMenuItem("New Game");
        menuItemNewGame.setPreferredSize(new Dimension(200, 22));
        menuItemNewGame.addActionListener(new NewGameActionListener());
        menuGame.add(menuItemNewGame);

        menuItemUpdateUsername = new JMenuItem("Update Username");
        menuItemUpdateUsername.setPreferredSize(new Dimension(200, 22));
        menuItemUpdateUsername.addActionListener(new UpdateUsernameActionListener());
        menuItemUpdateUsername.setEnabled(false);
        menuGame.add(menuItemUpdateUsername);

        menuItemConnectDisconnectServer = new JMenuItem("Server Connect/Disconnect");
        menuItemConnectDisconnectServer.setPreferredSize(new Dimension(200, 22));
        menuItemConnectDisconnectServer.addActionListener(new ConnectDisconnectServerActionListener());
        menuGame.add(menuItemConnectDisconnectServer);

        menuItemUpdateServer = new JMenuItem("Update Server Information");
        menuItemUpdateServer.setPreferredSize(new Dimension(200, 22));
        menuItemUpdateServer.addActionListener(new UpdateServerActionListener());
        menuGame.add(menuItemUpdateServer);

        menuItemQuitGame = new JMenuItem("Quit Game");
        menuItemQuitGame.setPreferredSize(new Dimension(200, 22));
        menuItemQuitGame.addActionListener(new QuitGameActionListener());
        menuItemQuitGame.setEnabled(false);
        menuGame.add(menuItemQuitGame);

        menuItemExit = new JMenuItem("Exit");
        menuItemExit.setPreferredSize(new Dimension(200, 22));
        menuItemExit.addActionListener(new ExitGameActionListener());
        menuGame.add(menuItemExit);
    }

    /**
     * Initialize game board area.
     */
    public void initializeGameBoardArea() {
        if (gameType == null || gameType.equals(GameType.CHECKERS)){
            gameBoard = new CheckerBoard();
            gameBoard.setIcon(iconGameBoard);
            gameBoard.setSize(new Dimension(560, 560));
            gameBoard.setOpaque(false);
            panelCenter.add(gameBoard, SpringLayout.NORTH);
        }
    }

    /**
     * Initialize server message area, game board, receive chat area, and send chat area.
     */
    private void initializeMainContentArea() {
        panelContent = new JPanel();
        panelContent.setPreferredSize(new Dimension(1120, 687));
        layoutContent = new SpringLayout();
        panelContent.setLayout(layoutContent);
        panelContent.setOpaque(false);
        panelMain.add(panelContent, SpringLayout.SOUTH);

                    /* MAIN WEST */
        initializeMainWestArea();

                    /* MAIN HORIZONTAL-CENTER */
        initializeMainCenterArea();

                    /* MAIN EAST */
        initializeMainEastArea();

        layoutContent.putConstraint(SpringLayout.WEST, scrollServerMessage, 0, SpringLayout.WEST, panelContent);
        layoutContent.putConstraint(SpringLayout.HORIZONTAL_CENTER, panelCenter, 0, SpringLayout.HORIZONTAL_CENTER, panelContent);
        layoutContent.putConstraint(SpringLayout.EAST, scrollChatMessage, 0, SpringLayout.EAST, panelContent);
    }

    /**
     * Initialize main center area.
     */
    private void initializeMainCenterArea() {
        panelCenter = new JPanel();
        layoutCenter = new SpringLayout();
        panelCenter.setLayout(layoutCenter);
        panelCenter.setPreferredSize(new Dimension(560, 649));
        panelCenter.setOpaque(false);
        panelContent.add(panelCenter, SpringLayout.HORIZONTAL_CENTER);

            /* NORTH-CENTER */
            initializeGameBoardArea();

            /* SOUTH-CENTER */
            initializeSendChatArea();

        layoutSendChat.putConstraint(SpringLayout.EAST, scrollSendChat, 0, SpringLayout.EAST, panelSendChat);
        layoutCenter.putConstraint(SpringLayout.SOUTH, panelSendChat, 0, SpringLayout.SOUTH, panelCenter);
    }

    /**
     * Initialize receive chat message area.
     */
    private void initializeMainEastArea() {
        textChatMessage = new JTextArea("Chat Messages:\n_________________________________\n");
        textChatMessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        textChatMessage.setDisabledTextColor(Color.BLACK);
        textChatMessage.setMargin(new Insets(10, 10, 10, 10));
        textChatMessage.setWrapStyleWord(true);
        textChatMessage.setLineWrap(true);
        textChatMessage.setEnabled(false);
        textChatMessage.setOpaque(false);
        DefaultCaret caretChatMessage = (DefaultCaret) textSendChat.getCaret();
        caretChatMessage.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panelContent.add(textChatMessage, SpringLayout.EAST);
        scrollChatMessage = new JScrollPane(this.textChatMessage);
        scrollChatMessage.setBackground(new Color(240, 220, 250));
        scrollChatMessage.setPreferredSize(new Dimension(257, 627));
        scrollChatMessage.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChatMessage.setAutoscrolls(true);
        scrollChatMessage.getViewport().setOpaque(false);
        //scrollChatMessage.setVisible(false);
        panelContent.add(scrollChatMessage, SpringLayout.EAST);
    }

    /**
     * Initialize the receive server message area.
     */
    private void initializeMainWestArea() {
        textServerMessage = new JTextArea("Game Status Information:\n_________________________________\n");
        textServerMessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        textServerMessage.setDisabledTextColor(Color.BLACK);
        textServerMessage.setMargin(new Insets(10, 10, 10, 10));
        textServerMessage.setWrapStyleWord(true);
        textServerMessage.setLineWrap(true);
        textServerMessage.setEnabled(false);
        textServerMessage.setOpaque(false);
        DefaultCaret caretServerMessage = (DefaultCaret) textServerMessage.getCaret();
        caretServerMessage.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panelContent.add(textServerMessage, SpringLayout.WEST);

        scrollServerMessage = new JScrollPane(this.textServerMessage);
        scrollServerMessage.setBackground(new Color(240, 220, 250));
        scrollServerMessage.setPreferredSize(new Dimension(257, 627));
        scrollServerMessage.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollServerMessage.setAutoscrolls(true);
        scrollServerMessage.getViewport().setOpaque(false);
        //scrollServerMessage.setVisible(false);
        panelContent.add(scrollServerMessage);
    }

    /**
     * Initialize CheckerBoard with player color.
     * @param color - The color of this player's game pieces
     * @return CheckerBoard - This window's Checkerboard
     */
    public CheckerBoard initializeNewCheckersGame (GamePiece.GamePieceColor color, ObjectOutputStream os) {
        panelCenter.remove(gameBoard);

        gameBoard = new CheckerBoard();
        gameBoard.setIcon(iconGameBoard);
        gameBoard.setSize(new Dimension(560, 560));
        gameBoard.setOpaque(false);
        CheckerBoard checkerBoard = (CheckerBoard) gameBoard;
        checkerBoard.initializeGameBoard(color, username, os);
        panelCenter.add(gameBoard, SpringLayout.NORTH);

        menuItemQuitGame.setEnabled(true);
        SoundPlayer.playSound(SoundPlayer.SOUND_PATH_GAME_START);
        revalidate();
        repaint();
        return checkerBoard;
    }

    /**
     * Initialize send chat area panel and components.
     */
    private void initializeSendChatArea() {
        panelSendChat = new JPanel();
        layoutSendChat = new SpringLayout();
        panelSendChat.setLayout(layoutSendChat);
        panelSendChat.setPreferredSize(new Dimension(560, 90));
        panelSendChat.setOpaque(false);
        panelCenter.add(panelSendChat, SpringLayout.SOUTH);

        /* Initialize chat button, textbox, and scrollbar. */
        initializeSendChatAreaComponents();
    }

    /**
     * Initialize button, chat window, and chat scroll bar for sending messages.
     */
    private void initializeSendChatAreaComponents() {
        buttonSendMessage = new JButton("Send");
        buttonSendMessage.setBackground(new Color(180, 200, 180));
        buttonSendMessage.setPreferredSize(new Dimension(100, 90));
        buttonSendMessage.addActionListener(new SendChatActionListener());
        panelSendChat.add(buttonSendMessage, SpringLayout.WEST);

        textSendChat = new JTextArea("Enter Text Here");
        textSendChat.setBackground(new Color(240, 220, 250));
        textSendChat.setMargin(new Insets(10, 10, 10, 10));
        textSendChat.setLineWrap(true);
        textSendChat.setWrapStyleWord(true);
        textSendChat.addKeyListener(new SendChatKeyListener());
        textSendChat.addMouseListener(new ClearTextMouseListener());
        textSendChat.setOpaque(true);
        DefaultCaret caretSendChat = (DefaultCaret) textSendChat.getCaret();
        caretSendChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panelSendChat.add(textSendChat, SpringLayout.EAST);
        shouldClearText = true;

        scrollSendChat = new JScrollPane(this.textSendChat);
        scrollSendChat.setBackground(new Color(240, 220, 220));
        scrollSendChat.setPreferredSize(new Dimension(460, 90));
        scrollSendChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollSendChat.setAutoscrolls(true);
        scrollSendChat.setOpaque(false);
        panelSendChat.add(scrollSendChat);
    }

    // Getters

    /**
     * Get this GameWindow's ipAddress.
     * @return ipAddress - IP address of the server to connect.
     */
    String getIpAddress() {
        return ipAddress;
    }

    /**
     * Get this GameWindow's checkerBoard.
     * @return checkerBoard - This GameWindow's GameBoard that contains functionality and pieces for a game.
     */
    GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Get this GameWindow's gameType.
     * @return GameType - The game type that the user would like to create.
     */
    GameType getGameType() {
        return gameType;
    }

    /**
     * Get this GameWindow's panelCenter
     * @return JPanel - This GameWindow's center panel.
     */
    JPanel getPanelCenter() {
        return panelCenter;
    }

    /**
     * Get this GameWindow's portNum.
     * @return portNum - Port Number of the server to connect.
     */
    int getPortNum() {
        return portNum;
    }

    /**
     * Get this GameWindow's textChatMessage.
     * @return textChatMessage - The JTextArea for receiving and writing chat messages.
     */
    JTextArea getTextChatMessage() {
        return textChatMessage;
    }

    /**
     * Get this GameWindow's textChatMessage.
     * @return textServerMessage - The JTextArea for receiving and writing server messages.
     */
    JTextArea getTextServerMessage() {
        return textServerMessage;
    }

    /**
     * Get this GameWindow's username.
     * @return username - The player's username.
     */
    String getUsername() {
        return username;
    }

    // Methods

    /**
     * Creates a new connection to the given ipAddress and portNum.
     * @param shouldStartGame - Indicates whether a new game should be started.
     */
    private void connectToServer(boolean shouldStartGame) {
        boolean shouldSendServerMessage = true;
        if(connectionGame != null && connectionGame.isConnectedToServer()) {
            if (connectionGame.isConnectedToGame()) {
                endGame(ClientConnection.EndGameType.QUIT);
            }
            connectionGame.closeConnection();
            shouldSendServerMessage = false;
        } else if(username == null) {
            if(!updateUsername()) {
                return;
            }
        }
        if (shouldStartGame) {
            gameType = GameType.CHECKERS;
        } else {
            gameType = null;
            shouldSendServerMessage = true;
        }
        connectionGame = new ClientConnection(this, shouldSendServerMessage);
        Thread th = new Thread(connectionGame);
        th.start();
    }

    /**
     * Prompts the user for a username and creates a new connection to the server.  Client then joins or starts a game
     * of the established type.
     */
    private void createNewGame() {
        gameType = GameType.CHECKERS;
        if (connectionGame != null) {
            connectionGame.setChoseToDisconnect(true);
        }
        connectToServer(true);
        menuItemUpdateServer.setEnabled(false);
        menuItemUpdateUsername.setEnabled(false);
    }

    /**
     * Disconnect from the game server.
     */
    private void disconnectFromServer() {
        if(connectionGame != null && connectionGame.isConnectedToServer()) {
            if (connectionGame.isConnectedToGame()) {
                endGame(ClientConnection.EndGameType.DISCONNECT);
            }
            connectionGame.closeConnection();
            return;
        }
    }

    /**
     * End connection to server and reset game board.
     */
    public void endGame(ClientConnection.EndGameType endGameType) {
        if (
                connectionGame != null &&
                connectionGame.isConnectedToServer() &&
                connectionGame.isConnectedToGame()
            ) {
            if (endGameType.equals(ClientConnection.EndGameType.DISCONNECT) ||
                    endGameType.equals(ClientConnection.EndGameType.QUIT)) {
                connectionGame.leaveGame(endGameType);
            }
            textServerMessage.append("\nGame Ended.\n");
        }
        getPanelCenter().remove(gameBoard);
        if (endGameType.equals(ClientConnection.EndGameType.DISCONNECT) ||
                endGameType.equals(ClientConnection.EndGameType.QUIT)) {
            initializeGameBoardArea();
            revalidate();
            repaint();
        }
        menuItemQuitGame.setEnabled(false);
        menuItemUpdateServer.setEnabled(true);
        menuItemUpdateUsername.setEnabled(true);
    }

    /**
     * Prompts user for a new server ip address and port number and updates accordingly.
     */
    private void updateServerInfo() {
        gameType = null;
        this.ipAddress = new JOptionPane().showInputDialog("Enter the IP address for server connection: ");
        try {
            this.portNum = Integer.parseInt(new JOptionPane().showInputDialog("Enter the Port number for the server connection: "));
        } catch (Exception e) {
            this.portNum = Integer.parseInt(new JOptionPane().showInputDialog(" Invalid port number. Enter a valid Port number for the server connection: "));
        }
        if(username != null) {
            if (connectionGame != null) {
                connectionGame.setChoseToDisconnect(true);
            }
            connectToServer(false);
        }
    }

    /**
     * Update this GameWindow's username and re-establish connection.
     * @return boolean - Indicates whether name was changed.
     */
    private boolean updateUsername() {
        String userChoice = new JOptionPane().showInputDialog("Enter your username: ");
        // If user did not cancel username prompt, set the username and enable change name option.
        if (userChoice != null) {
            username = userChoice;
            menuItemUpdateUsername.setEnabled(true);
            return true;
        } else {
            return false; // If user cancelled, return.
        }
    }

    // Inner Class Listeners

        // Dropdown Menu Listeners

        /**
         * On action event, establish a new connection to the game server and create/join server.
         */
        private class ConnectDisconnectServerActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                if (connectionGame != null && connectionGame.isConnectedToServer()) {
                    disconnectFromServer();
                    return;
                }
                connectToServer(false);
            }
        }

        /**
         * On action event, close the window.
         */
        private class ExitGameActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                Runtime.getRuntime().exit(0);
            }
        }

        /**
         * On action event, establish a new connection to the game server and create/join server.
         */
        private class NewGameActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                createNewGame();
            }
        }

        /**
         * On action event, disconnect from current game.
         */
        private class QuitGameActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                connectToServer(false);
            }
        }

        /**
         * On action event, prompt for and set username.
         */
        private class UpdateUsernameActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                updateUsername();
                if (connectionGame != null) {
                    connectionGame.setChoseToDisconnect(true);
                }
                connectToServer(false);
            }
        }

        /**
         * On action event, create new chat message with given string and send to server.
         */
        private class UpdateServerActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                updateServerInfo();
            }
        }

        // Chat Listeners

        /**
         * On mouse event, clear beginning text.
         */
        private class ClearTextMouseListener implements MouseListener {
            public void mouseClicked(MouseEvent e){ if(shouldClearText) textSendChat.setText(""); shouldClearText = false; }
            public void mousePressed(MouseEvent e){ if(shouldClearText) textSendChat.setText(""); shouldClearText = false; }
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
        }

        /**
         * On action event, send Chat Message.
         */
        private class SendChatActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                if (connectionGame != null && connectionGame.isConnectedToServer() == true) {
                    connectionGame.sendChatMessage(textSendChat.getText());
                    textSendChat.setText("");
                }else {
                    textChatMessage.append("\nUnable to send message.  Not connected to server.\n");
                    textSendChat.setText("");
                }
            }
        }

        /**
         * On key event, send Chat Message.
         */
        private class SendChatKeyListener implements KeyListener {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    if (connectionGame != null && connectionGame.isConnectedToServer() == true) {
                        connectionGame.sendChatMessage(textSendChat.getText());
                        textSendChat.setText("");
                    } else {
                        textChatMessage.append("\nUnable to send message.  Not connected to server.\n");
                        textSendChat.setText("");
                    }
                }
            }
            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
        }

    /**
     * Create a new GameWindow for playing checkers.
     * @param args
     */
    public static void main(String[] args) {
        GameWindow gw = new GameWindow();
    }
}
