package CheckersClient;

import GameServer.game.*;
import GameServer.game.checkers.CheckersMessage;
import GameServer.game.message.StatusMessage;
import GameServer.message.ChatMessage;
import GameServer.message.Message;
import GameServer.message.ResponseMessage;
import GameServer.message.WhoisMessage;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Connects to a server at the supplied (or default) IP Address and Port Number, creates/joins/observes a new game of
 * the supplied or default type, and handles messages received from and sent to this server.
 *
 * @author Tyler Cazier
 * @version 8/4/15
 */
public class ClientConnection implements Runnable {
    // Connection Fields
    private int portNum;
    private String ipAddress;
    private Socket clientSocket; // Used to bind to game server.
    private ObjectOutputStream os;
    private ObjectInputStream is;

    // Game Fields
    private CheckerBoard checkerBoard;
    private List<String> connectedUsers;
    private Map<GameType, GameInfo> games;
    private GameWindow gameWindow;
    private GameType gameType;
    private String username;
    private String opponentName;
    private boolean isConnectedToServer;
    private boolean isConnectedToGame;
    private boolean choseToDisconnect;
    private boolean sendConnectedToServerMessage;
    private JTextArea textServerMessage;
    private JTextArea textChatMessage;

    enum EndGameType {
        DISCONNECT, GAME_FINISH, NO_OPPONENT, QUIT
    }

    /**
     * Constructor Game and Name - Specify game type and username but use default port/ip settings.
     *
     * @param gameWindow - The GameWindow object for writing messages to and for receiving Client messages.
     */
    public ClientConnection(GameWindow gameWindow) {
        checkerBoard = null;
        ipAddress = gameWindow.getIpAddress();
        choseToDisconnect = false;
        portNum = gameWindow.getPortNum();
        this.gameWindow = gameWindow;
        gameType = gameWindow.getGameType();
        username = gameWindow.getUsername();
        opponentName = null;
        isConnectedToGame = false;
        isConnectedToServer = false;
        textChatMessage = gameWindow.getTextChatMessage();
        textServerMessage = gameWindow.getTextServerMessage();
        sendConnectedToServerMessage = true;
    }

    /**
     * Constructor Game and Name - Specify game type and username but use default port/ip settings.
     *
     * @param gameWindow - The GameWindow object for writing messages to and for receiving Client messages.
     */
    public ClientConnection(GameWindow gameWindow, boolean sendConnectedToServerMessage) {
        checkerBoard = null;
        ipAddress = gameWindow.getIpAddress();
        choseToDisconnect = false;
        portNum = gameWindow.getPortNum();
        this.gameWindow = gameWindow;
        gameType = gameWindow.getGameType();
        username = gameWindow.getUsername();
        opponentName = null;
        isConnectedToGame = false;
        isConnectedToServer = false;
        textChatMessage = gameWindow.getTextChatMessage();
        textServerMessage = gameWindow.getTextServerMessage();
        this.sendConnectedToServerMessage = sendConnectedToServerMessage;
    }

    /**
     * Establishes connection to server and starts or joins a new game of the desired type. While connected to the
     * server, messages from the server are continually read and handled.
     */
    @Override
    public void run() {
        try {
            // Bind to server IP and port.
            clientSocket = new Socket(this.ipAddress, this.portNum);
            isConnectedToServer = true;
            // Guarantee that any streams and sockets connections close on exit.
            Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run(){closeConnection();}
                }
            );
            // Connect to server.
            joinServer();
            // Read messages from the server as long as it is connected.
            while(isConnectedToServer) {
                try {
                    Object obj = is.readObject();
                    if (obj instanceof Message) {
                        readMessage((Message) obj);
                    }
                } catch (ClassNotFoundException e) {
                    // Cannot handle. If received, server code has changed and client code must be updated.
                    writeServerMessage("Unexpected class received from server.");
                }
            }


        } catch (IOException e) {
            if (!choseToDisconnect) {
                writeServerMessage("No connection to server.");
            }
            gameWindow.endGame(EndGameType.DISCONNECT);
            isConnectedToServer = false;
            isConnectedToGame = false;
            choseToDisconnect = false;
        }
    }

    // Getters

    /**
     * Is the client connected to the server? Returns isConnectedToServer.
     * @return boolean - Is the client connected to the server?
     */
    public boolean isConnectedToServer() {
        return isConnectedToServer;
    }

    /**
     * Get this ClientConnection's game connection status.
     * @return boolean - Is this client connected to a game.
     */
    public boolean isConnectedToGame() {
        return isConnectedToGame;
    }

    // Setters

    /**
     * Indicate whether the user choose to disconnect.
     * @param choseToDisconnect - Did the user choose to disconnect?
     */
    public void setChoseToDisconnect(boolean choseToDisconnect) {
        this.choseToDisconnect = choseToDisconnect;
    }

    // Connect to Server and Create/Join/Observe Game

    /**
     * Binds an open connection on this ClientConnection's portNum and ipAddress and provides username to server.
     */
    private void joinServer() {
        try {
            os = new ObjectOutputStream((new BufferedOutputStream(clientSocket.getOutputStream())));
            os.writeUTF(username);
            os.flush();
            is = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));

            // Check to see if connection to server succeeds.
            Message m = (Message)is.readObject();
            if (m.getMessageType().equals(Message.Type.DENY)) {
                writeServerMessage("Unable to join game server. Your username may already be in use. Try a different username.");
            } else {
                if(sendConnectedToServerMessage) {
                    writeServerMessage(username + " successfully added to game server.");
                }
                if(gameType != null) {
                    createOrJoinGame();
                }
            }
        } catch (IOException e) {
            writeServerMessage("Error establishing/writing/reading from inputstream/outputstream.");
        } catch (ClassNotFoundException e) {
            writeServerMessage("Unexpected class received from server.");
        }
    }

    /**
     *  Looks at all current games and joins any existing ,available games of the desired game type.  Otherwise,
     *  creates a new game of the desired game type.
     */
    public void createOrJoinGame() {
        obtainGameServerDetails();
        try {
            // See if there is an existing game of the desired type to join.
            if (games.containsKey(gameType)) {
                // If the game is checkers and there is room for one more player, join.
                if (gameType.equals(GameType.CHECKERS) && games.get(gameType).getPlayers().size() < 2) {
                    os.writeObject(StatusMessage.getJoinMessage(gameType, username));
                    os.flush();
                    displayWaitMessage("Joiner");
                    checkerBoard = gameWindow.initializeNewCheckersGame(GamePiece.GamePieceColor.BLACK, os);

                } else { // Otherwise, Observe the game.
                    os.writeObject(StatusMessage.getObserveMessage(gameType));
                    os.flush();
                    displayWaitMessage("Observer");
                }
                isConnectedToGame = true;
            }
            // If desired game is checkers, start a new game.  Otherwise, advise the user and exit.
            else {
                if (gameType.equals(GameType.CHECKERS)) {
                    os.writeObject(StatusMessage.getStartMessage(gameType, username));
                    os.flush();
                    displayWaitMessage("Creator");
                    checkerBoard = gameWindow.initializeNewCheckersGame(GamePiece.GamePieceColor.RED, os);
                    isConnectedToGame = true;

                } else {
                    writeServerMessage("No existing games of type " + gameType.toString() + " are currently " +
                            "available to observe.  Please try again later.");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing on output stream.");
        }
    }

    /**
     * Retrieve connected users and names.
     */
    private void obtainGameServerDetails() {
        try {
            // Request Whois information.
            os.writeObject(new WhoisMessage());
            os.flush();
            WhoisMessage m = (WhoisMessage)is.readObject();

            // Update users and games.
            this.connectedUsers = m.getConnectedUsers();
            this.games = m.getGames();

        } catch (IOException e){
            // Cannot handle. Connection and stream successfully established. Issue is server-side or server code has changed.
            System.err.println("WhoisMessage could not be delivered. IOException.");
        } catch (ClassNotFoundException e) {
            // Cannot handle. If received, server code has changed and client code must be updated.
            System.err.println("Unexpected class received.");
        }
    }

    /**
     * Receives a timer object and prints the appropriate wait message.
     *
     * @param playerType - Used to distinguish Creator, Joiner, or observer.
     */
    private void displayWaitMessage (String playerType) {
        try {
            Message m = (Message)is.readObject();
            if (!m.getMessageType().equals(ResponseMessage.Type.DENY)) {
                is.readObject();
                if (playerType.equals("Creator"))
                    writeServerMessage("Please wait for other users to join the game.");
                else if (playerType.equals("Joiner"))
                    writeServerMessage("Please wait while " + username + " is added to the game.");
                else
                    writeServerMessage("Connecting to game. Observing started.");
            } else {
                // Cannot handle. Issue on server end.
                if (playerType.equals("Creator"))
                    writeServerMessage("Server denied create game action. Please try again.");
                else if (playerType.equals("Joiner"))
                    writeServerMessage("Server denied join game action. Please try again.");
                else
                    writeServerMessage("Server denied observe game action. Please try again.");
                //System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("Error writing to server.");
        } catch (ClassNotFoundException e) {
            // Cannot handle. If received, server code has changed and client code must be updated.
            System.err.println("Unexpected class received.");
        }
    }

    // Methods

    /**
     * Send quit message and disconnect from game.
     */
    public void leaveGame(ClientConnection.EndGameType endGameType) {
        try {
            if (endGameType == EndGameType.QUIT) {
                os.writeObject(StatusMessage.getEndQuitMessage(gameType, username));
            } else if (endGameType == EndGameType.DISCONNECT) {
                os.writeObject(StatusMessage.getEndDisconnectMessage(gameType, username));
            } else {
                return;
            }
            os.flush();
            isConnectedToGame = false;
            choseToDisconnect = true;
        } catch (IOException e) {
            System.out.println("Unable to write to server.");
        }
    }

    /**
     * Processes any message received based on the messsage type.
     *
     * @param m readMessage
     */
    private void readMessage(Message m){
        String user = m.getUsername();
        if (user == null) {
            user = "Unknown";
        }

        if (m.getMessageType().equals(Message.Type.GAME)) {
            GameMessage gm = (GameMessage) m;
            if (gm.getGameType().equals(GameType.CHECKERS)) {
                CheckersMessage cm = (CheckersMessage)gm;

                if(checkerBoard.receiveOpponentMove(cm.getChecker())) {
                    writeServerMessage(username + "'s turn");
                }
            }
        } else if (m.getMessageType().equals(Message.Type.CHAT)) {
            if (user.equals("Unknown")) {
                user = username;
            } else {
                SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHAT_RECEIVE);
            }
            ChatMessage cm = (ChatMessage)m;
            writeChatMessage(user + ": " + cm.getText());
        } else if (m.getMessageType().equals(Message.Type.START_GAME) && gameType != null) {
            StatusMessage sm = (StatusMessage) m;
            writeServerMessage("New " + sm.getGameType().toString().toLowerCase() + " game started.");
             if (gameWindow.getGameBoard() instanceof CheckerBoard) {
                 if (opponentName == null && games != null) {
                     for (GameType gameType : games.keySet()) {
                         if(gameType.equals(GameType.CHECKERS)) {
                             for (Player name : games.get(gameType).getPlayers()) {
                                 if(!name.getUsername().equals(username)) {
                                     opponentName = name.getUsername();
                                 }
                             }
                         }
                     }
                 }
                 if (
                         sm.getGameType().equals(GameType.CHECKERS) &&
                         gameType.equals(GameType.CHECKERS) &&
                         checkerBoard.getPlayerColor() == GamePiece.GamePieceColor.RED
                     ) {
                     checkerBoard.setIsTurn(true);
                     checkerBoard.enableSelectablePieces();
                     writeServerMessage(username + "'s Turn");
                 } else if (sm.getGameType().equals(GameType.CHECKERS)) {
                     writeServerMessage(opponentName + "'s Turn");
                 }
             }
            isConnectedToGame = true;
        } else if (m.getMessageType().equals(Message.Type.JOIN_GAME)) {
            writeServerMessage(user + " joined a game.");
            StatusMessage sm = (StatusMessage) m;
            if (
                    sm.getGameType().equals(GameType.CHECKERS) &&
                            gameType.equals(GameType.CHECKERS)
                    ) {
                    opponentName = sm.getUsername();
                }
        } else if (m.getMessageType().equals(Message.Type.OBSERVE_GAME)) {
            writeServerMessage(user + " is observing a game.");
        } else if (m.getMessageType().equals(GameMessage.Type.WHOIS)){
            WhoisMessage wm = (WhoisMessage) m;
            writeServerMessage("Gettings server details.");
            connectedUsers = wm.getConnectedUsers();
            games = wm.getGames();
        } else if (m.getMessageType().equals(Message.Type.ACKNOWLEDGE)) {
            if (gameType.equals(GameType.CHECKERS)) {
                if(!checkerBoard.isTurn()) {
                    writeServerMessage(opponentName +"'s turn");
                }
            }
        } else if (m.getMessageType().equals(Message.Type.DENY)) {
            writeServerMessage("Server denied " + username + "'s request.");
        } else if (m.getMessageType().equals(Message.Type.END_GAME) &&
                isConnectedToGame) {
            if (!user.equals("Unknown")) {
                try {
                    os.writeObject(StatusMessage.getEndMessage(GameType.CHECKERS, null));
                    os.flush();
                    writeServerMessage("Game ended. " + username + " won.");
                } catch (IOException e) {
                    System.out.println("Error.");
                }
            } else {
                writeServerMessage("Game ended. " + username + " lost.");
            }
            isConnectedToGame = false;
            checkerBoard.freezeBoard();
            gameWindow.endGame(EndGameType.GAME_FINISH);
        } else if (m.getMessageType().equals(Message.Type.END_GAME_DISCONNECT) &&
                isConnectedToGame) {
            writeServerMessage("Game ended. " + user + " was disconnected.");
            isConnectedToGame = false;
            checkerBoard.freezeBoard();
            gameWindow.endGame(EndGameType.NO_OPPONENT);
        } else if (m.getMessageType().equals(Message.Type.END_GAME_QUIT) &&
                isConnectedToGame) {
            writeServerMessage("Game ended. " + user + " left the game.");
            isConnectedToGame = false;
            checkerBoard.freezeBoard();
            gameWindow.endGame(EndGameType.NO_OPPONENT);
        }
    }

    /**
     * Send chat message to textChatMessage if defined.
     * @param messageToWrite - The chat message to send to the server.
     */
    public void sendChatMessage (String messageToWrite) {
        try {
            SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHAT_SEND);
            os.writeObject(new ChatMessage(messageToWrite, this.username));
            os.flush();
        } catch(IOException e) {
            // If unable to write message, there is nothing that can be done on the client's side.
        }
    }

    /**
     * Write server message to textServerMessage if defined.  Otherwise, write to console.
     * @param messageToWrite - The message to write to this GameWindow's server text area.
     */
    private void writeServerMessage (String messageToWrite) {
        if (textServerMessage != null) {
            textServerMessage.append("\n" + messageToWrite + "\n");
            textServerMessage.repaint();
            textChatMessage.repaint();
        }
    }

    /**
     * Write chat message to textChatMessage if defined.
     * @param messageToWrite - The chat message to write to this gameWindow's chat area.
     */
    private void writeChatMessage (String messageToWrite) {
        if (textChatMessage != null) {
            textChatMessage.append("\n" + messageToWrite + "\n");
        } else {
            System.out.println( "\n" + messageToWrite + "\n");
        }
        textChatMessage.repaint();
        textServerMessage.repaint();
    }

    /**
     * Closes all input streams, output streams, and sockets.
     */
    public void closeConnection() {
        try {
            is.close();
        }catch(IOException e)
        {
            // If triggered, there is no stream to close.
        }
        try {
            os.close();
        }catch(IOException e)
        {
            // If triggered, there is no stream to close.
        }
        try {
            clientSocket.close();
        }catch(IOException e)
        {
            // If triggered, there is no connection to close
        }
        isConnectedToServer = false;
    }
}
