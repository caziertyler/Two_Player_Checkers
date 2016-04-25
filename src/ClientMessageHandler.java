package CheckersClient;

import GameServer.game.GameInfo;
import GameServer.game.GameMessage;
import GameServer.game.GameType;
import GameServer.game.Player;
import GameServer.game.checkers.CheckersMessage;
import GameServer.game.message.StatusMessage;
import GameServer.message.ChatMessage;
import GameServer.message.Message;
import GameServer.message.WhoisMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 4/24/2016.
 */
public class ClientMessageHandler {

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
//
//    public class ClientMessageHandler(GameBoard) {
//
//    }

    /**
     * Send quit message and disconnect from game.
     */
    public void leaveGame() {
        try {
            os.writeObject(StatusMessage.getEndQuitMessage(gameType, username));
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
        } else if (m.getMessageType().equals(Message.Type.START_GAME)) {
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
            writeServerMessage("Game ended. " + user + " won.");
            isConnectedToGame = false;
            gameWindow.initializeGameBoardArea();
        } else if (m.getMessageType().equals(Message.Type.END_GAME_DISCONNECT) &&
                isConnectedToGame) {
            writeServerMessage("Game ended. " + user + " was disconnected.");
            isConnectedToGame = false;
            gameWindow.endGame(ClientConnection.EndGameType.NO_OPPONENT);
        } else if (m.getMessageType().equals(Message.Type.END_GAME_QUIT) &&
                isConnectedToGame) {
            writeServerMessage("Game ended. " + user + " left the game.");
            isConnectedToGame = false;
            gameWindow.endGame(ClientConnection.EndGameType.NO_OPPONENT);
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
}
