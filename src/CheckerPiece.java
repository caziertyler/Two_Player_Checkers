package CheckersClient;

import javax.swing.*;

/**
 * This CheckerPiece class is a container for images, color, position, id, and state for creating a checker
 * game.  Appropriate getters and setters have been assigned as needed.
 *
 * Position and king values are changeable.
 *s
 * @author Justin Walker, Tyler Cazier
 * @version 3/25/16
 */
public class CheckerPiece extends GamePiece  {
    private Boolean isKing;

    /**
     * Constructs a new CheckerPiece object.
     *
     * @param color         the color of this CheckerPiece represented by a GameBoard enum.
     * @param id            the unique identifier for this CheckerPiece.
     * @param coordinates   the CheckerBoard location of this CheckerPiece.
     */
	CheckerPiece(GamePieceColor color, int id, String coordinates) {
        super(color, id, coordinates);
        isKing = false;
        updateImage();
	}

    @Override
    public String toString() {
        return getId() + getCoordinates() + (isKing() != null && isKing() ? " (king)" : "");
    }

    // Getters

    /**
     * Get whether or not this CheckerPiece is a Kin.
     *
     * @return  is this Checker a king?
     */
    Boolean isKing() {
        return isKing;
    }

    // Methods

    /**
     * Promote Checker to King state and update Checker image.
     */
    void promotePiece() {
        isKing = true;
        updateImage();
    }

    /**
     * Sets the Checker's image based on it's color and whether it is a king.
     */
    void updateImage() {
        if(color.equals(GamePieceColor.RED) && !isKing) {
            imagePiece = new ImageIcon("res\\RedChecker.png");
            imagePieceSelected = new ImageIcon("res\\RedChecker_Selected.png");
        } else if (color.equals(GamePieceColor.BLACK) && !isKing){
            imagePiece = new ImageIcon("res\\BlackChecker.png");
            imagePieceSelected = new ImageIcon("res\\BlackChecker_Selected.png");
        } else if (color.equals(GamePieceColor.RED) && isKing) {
            imagePiece = new ImageIcon("res\\RedKing.png");
            imagePieceSelected = new ImageIcon("res\\RedKing_Selected.png");
        } else {
            imagePiece = new ImageIcon("res\\BlackKing.png");
            imagePieceSelected = new ImageIcon("res\\BlackKing_Selected.png");
        }
    }
}
