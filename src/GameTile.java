package CheckersClient;

import javax.swing.*;

/**
 * A GameTile represents a tile on a GameBoard and holds tile information.  It can contain GamePieces and allows
 * for GamePiece movement.
 *
 * Created by Tyler on 3/26/2016.
 */
public abstract class GameTile extends JToggleButton {
    private boolean isOccupied;
    private GameBoard gameBoard;
    protected GamePiece gamePiece;
    protected GamePieceListener gamePieceListener;
    protected GameTargetTileListener gameTargetTileListener;
    private int gameBoardRow;
    private int gameBoardCol;
    private String coordinates;

    /**
     * Constructs a new GameTile object that holds a game piece and allows for game piece movement.
     *
     * @param isOccupied
     * @param gameBoard
     * @param gamePiece
     * @param gameBoardRow
     * @param gameBoardCol
     * @param coordinates
     */
    protected GameTile (
                boolean isOccupied, GameBoard gameBoard, GamePiece gamePiece,
                int gameBoardRow, int gameBoardCol, String coordinates
            ) {
        this.coordinates = coordinates;
        this.gameBoardRow = gameBoardRow;
        this.gameBoardCol = gameBoardCol;
        this.gamePiece = gamePiece;
        this.gameBoard = gameBoard;
        this.isOccupied = isOccupied;
    }

    // Getters

    /**
     * Get this GameTile's coordinates.
     *
     * @return  the position of this GameTile on the GameBoard.
     */
    public String getCoordinates() {
        return coordinates;
    }

    /**
     * Get this GameTile's GamePieceListener.
     *
     * @return the listener that defines the behavior of a tile with a movable Game Piece.
     */
    GamePieceListener getGamePieceListener() {
        return gamePieceListener;
    }

    /**
     * Get this GameTile's GameBoard.
     *
     * @return the GameBoard to which the GameTile belongs.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Get this GameTile's GameBoardRow
     *
     * @return  the row to which this GameTile belongs.
     */
    public int getGameBoardRow() {
        return gameBoardRow;
    }

    /**
     * Get this GameTile's gameBoardCol.
     *
     * @return the column to which this GameTile belongs.
     */
    public int getGameBoardCol() {
        return gameBoardCol;
    }

    /**
     * Get this GameTile's GamePiece.
     *
     * @return the GamePiece that occupies this GameTile.
     */
    public GamePiece getGamePiece() {
        return gamePiece;
    }

    /**
     * Get this GameTile's GameTargetTileListener
     *
     * @return GameTargetTileListener - Defines the behavior of a GameTile that can accept a GamePiece.
     */
    public GameTargetTileListener getGameTargetTileListener() {
        return gameTargetTileListener;
    }

    /**
     * Get this GameTile's occupied status.
     *
     * @return Is this GameTile occupied by a GamePiece?
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Set this GameTile's occupied status.
     *
     * @param isOccupied    the occupied status of this GameTile.
     */
    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    /**
     * Set this GameTile's GamePiece
     * @param gamePiece the GamePiece that occupies this GameTile.
     */
    public void setGamePiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    /**
     * Set the graphic image representation of an empty tile.
     */
    public abstract void setEmptyTileImages();

    /**
     * Set the graphic image representation of an occupied (contains CheckerPiece) tile.
     */
    public abstract void setPieceTileImages();
}
