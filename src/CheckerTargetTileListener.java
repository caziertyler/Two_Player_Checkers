package CheckersClient;

import java.awt.event.ActionEvent;

/**
 * This becomes an action listener for empty CheckerTile's that represent a valid move for the checkerBoard's
 * selected CheckerPiece. Upon click of the targetTile, the tile will receive the selected CheckerPiece.
 *
 * Created by Tyler on 4/2/2016.
 */
public class CheckerTargetTileListener extends GameTargetTileListener {
    CheckerTile targetTile;
    CheckerBoard checkerBoard;

    /**
     * Constructs a Listener that is used to define the 'On Mouse-click' behavior of a CheckerTile that
     * can receive the selected piece per the rules of Checkers.
     *
     * @param targetTile - The tile with a CheckerPiece to be moved.
     */
    public CheckerTargetTileListener(CheckerTile targetTile) {
        this.targetTile = targetTile;
        this.checkerBoard = (CheckerBoard) targetTile.getGameBoard();
    }

    /**
     * Upon mouse click, move this checkerBoard's selectedPiece to the targetTile and disable the checkerBoard until
     * the player's next turn.
     *
     * @param e ActionEvent (or mouse click) that causes the checkerBoard's selectedPiece to move to the targetTile
     *          and causes the checkerBoard's CheckerTile's to be disabled.
     */
    public void actionPerformed(ActionEvent e) {
        checkerBoard.moveGamePiece(targetTile);
    }
}
