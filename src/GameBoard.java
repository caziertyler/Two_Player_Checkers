package CheckersClient;

import java.awt.*;
import java.io.ObjectOutputStream;
import javax.swing.*;

/**
 * A GameBoard holds and controls the tiles and pieces of a board game.
 *
 * @author Tyler Cazier
 * @version 8/13/15
 */
public abstract class GameBoard extends JLabel {
    protected boolean isTurn;
    protected GamePiece selectedPiece;
    protected GamePiece targetPiece;
    protected GamePieceCollection gamePieces;
    protected GameTile selectedTile;
    protected GameTile targetTile;
    protected GameTile[][] gameTiles;
    protected GamePiece.GamePieceColor playerColor;
    protected int selectedTileRow;
    protected int selectedTileCol;
    protected int targetTileRow;
    protected int targetTileCol;
    protected ObjectOutputStream outputStream;
    protected String username;

    /**
     * Constructs a new GameBoard object for holding GameTiles and GamePieces.
     */
    public GameBoard() {
        setLayout(new GridLayout(8, 8, 0, 0));
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 0));
     }

    // Getters

    /**
     * Get this GameBoard's gamePieces.
     *
     * @return  this GameBoard's game pieces.
     */
    GamePieceCollection getGamePieces() {
        return gamePieces;
    }

    /**
     * Get this GameBoard's GameTiles.
     *
     * @return  this GameBoard's GameTiles. Includes occupied GameTiles with GamePieces or unoccupied GameTiles.
     */
    GameTile[][] getGameTiles() { return gameTiles; }

    /**
     * Get this CheckerBoard player's isTurn to true.
     * @return boolean - Is it this player's turn.
     */
    boolean isTurn() {
        return isTurn;
    }

    /**
     * Get this player's piece color.
     *
     * @return  the player's (not opponent's) color.
     */
    GamePiece.GamePieceColor getPlayerColor() {
        return playerColor;
    }

    /**
     * Get this GameBoard's currently selected GameTile.
     *
     * @return  this GameBoard's currently selected GameTile that contains a GamePiece.
     */
    GameTile getSelectedTile() {
        return selectedTile;
    }

    // Setters

    /**
     * Set this player's turn to true or false.
     *
     * @param isTurn    set this player's turn.
     */
    void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    /**
     * Select a tile's game piece for movement
     *
     * @param selectedTile  the tile with a game piece to move.
     */
    void setSelectedTile(GameTile selectedTile) {
        if (selectedTile != null) {
            this.selectedTile = selectedTile;
            this.selectedTileCol = selectedTile.getGameBoardCol();
            this.selectedTileRow = selectedTile.getGameBoardRow();
            this.selectedPiece = selectedTile.getGamePiece();
        } else {
            this.selectedTile = null;
            selectedTileCol = -1;
            selectedTileRow = -1;
            selectedPiece = null;
        }
    }

    /**
     * The tile to move this GameBoard's selectedPiece.
     *
     * @param targetTile the receiving game tile for this selected piece.
     */
    void setTargetTile(GameTile targetTile) {
        if (targetTile != null) {
            this.targetTile = targetTile;
            targetPiece = targetTile.getGamePiece();
            targetTileCol = targetTile.getGameBoardCol();
            targetTileRow = targetTile.getGameBoardRow();
        } else {
            this.targetTile = null;
            targetPiece = null;
            targetTileCol = -1;
            targetTileRow = -1;
        }
    }

    // Methods

    /**
     * Create and place game tiles and pieces for the game board.
     *
     * @param playerColor   this player's game piece color.
     * @param username      this player's username.
     * @param outputStream  an output stream for sending moves over a server.
     */
    abstract void initializeGameBoard(GamePiece.GamePieceColor playerColor, String username, ObjectOutputStream outputStream);

    /**
     * Add a game tile to add to this game board.
     *
     * @param gameTile  the tile to add to this game board.
     */
    protected abstract void addGameTile(GameTile gameTile);

    /**
     * Disable all game board tile actions.
     */
    public abstract void freezeBoard();
}
