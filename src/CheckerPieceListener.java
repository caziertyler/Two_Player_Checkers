package CheckersClient;

import java.awt.event.ActionEvent;

/**
 * This becomes an action listener for CheckerTile's that contain a CheckerPiece.  Upon click of this tile, the
 * CheckerPiece on this tile becomes the object that will move to an empty target.
 *
 * Created by Tyler on 4/2/2016.
 */
public class CheckerPieceListener extends GamePieceListener {
    CheckerTile selectedTile;
    CheckerBoard checkerBoard;

    /**
     * Constructs a Listener that is used to define the 'On Mouse-click' behavior of a CheckerTile that
     * contains a CheckerPiece.
     *
     * @param selectedTile - The tile with a CheckerPiece to be moved.
     */
    public CheckerPieceListener (CheckerTile selectedTile) {
        this.selectedTile = selectedTile;
        checkerBoard = (CheckerBoard) selectedTile.getGameBoard();
    }

    /**
     * Upon mouse click, disable all other CheckerTile's that contain CheckerPieces and make available
     * all valid, empty CheckerTile's that can house the selectedTile's CheckerPiece.
     *
     * @param e ActionEvent (or mouse click) that triggers empty CheckerTile's that represent valid moves for
     *          the selectedTile.  Also, enable selectedTile in case of move cancellation.
     */
    public void actionPerformed(ActionEvent e) {
        checkerBoard.selectGamePiece(selectedTile);
    }
}
