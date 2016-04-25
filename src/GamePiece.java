package CheckersClient;

import javax.swing.*;

/**
 * An abstract class used to represent game pieces for common board games.  Can represent Checker, Chess, Battleship, etc.
 *
 * Created by Tyler on 3/26/2016.
 */
public abstract class GamePiece {
    private int id;
    protected GamePieceColor color;
    protected ImageIcon imagePiece;
    protected ImageIcon imagePieceSelected;
    private String coordinates;

    enum GamePieceColor {
        RED, BLACK
    }

    /**
     * Construct a new GamePiece object.
     *
     * @param id            The unique identifier for the GamePiece.
     * @param coordinates   The location of the GamePiece on the board.
     */
    protected GamePiece(GamePieceColor color, int id, String coordinates) {
        this.id = id;
        this.color = color;
        this.coordinates = coordinates;
    }

    /**
     * Get this GamePiece's color.
     *
     * @return  this GamePiece's color.
     */
    GamePieceColor getColor() {
        return color;
    }

    /**
     * Get this GamePiece's coordinates.
     *
     * @return  The GamePiece's location on the board.
     */
    String getCoordinates() {
        return coordinates;
    }

    /**
     * Get this GamePiece's unique identifier.
     *
     * @return id   Unique identifier for this GamePiece.
     */
    int getId() {
        return id;
    }

    /**
     * Get this GamePiece's graphic image.
     *
     * @return  the graphic image representation for this game piece.
     */
    ImageIcon getImage() {
        return imagePiece;
    }

    /**
     * Get this GamePiece's graphic image when the piece is selected.
     *
     * @return  the graphic image representation for this GamePiece when the piece is selected.
     */
    ImageIcon getSelectedImage() {
        return imagePieceSelected;
    }

    /**
     * Promote(upgrade) this game piece.
     */
    abstract void promotePiece();

    /**
     * Set this GamePiece's coordinates.
     *
     * @param coordinates   the new GameBoard position for this GamePiece.
     */
    void setCoordinates (String coordinates) {
        this.coordinates = coordinates;
    }
}
