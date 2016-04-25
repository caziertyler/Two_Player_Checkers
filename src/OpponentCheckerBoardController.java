package CheckersClient;

import java.io.ObjectOutputStream;

/**
 * An OpponentCheckerBoardController controls moved received from an opponent.
 *
 * Created by Tyler on 4/22/2016.
 */
public class OpponentCheckerBoardController extends GameBoardController {

    /**
     * Constructs a new CheckerBoardController used to move GameBoard pieces and control which GameBoard tiles are
     * active.
     *
     * @param gameBoard the game board for that this controller manipulates.
     */
    public OpponentCheckerBoardController (
            GameBoard gameBoard, ObjectOutputStream outputStream,
            GamePiece.GamePieceColor playerColor, String username
        ) {
        super(gameBoard, outputStream, playerColor, username);
    }

    /**
     * Move this selectedTile Checker to the targetTile.
     */
    public void moveGamePiece () {
        checkForAndPerformCaptures();

        // Update CheckerPiece coordinates
        selectedTile.getGamePiece().setCoordinates(targetTile.getCoordinates());

        // Empty the CheckerTile that contains the moving CheckerPiece.
        selectedTile.removeActionListener(selectedTile.getGamePieceListener());
        selectedTile.addActionListener(selectedTile.getGameTargetTileListener());
        selectedTile.setIsOccupied(false);
        selectedTile.setEmptyTileImages();

        // Populate the CheckerTile that is receiving the moving CheckerPiece.
        targetTile.setGamePiece(selectedPiece);
        targetPiece = selectedPiece;
        selectedTile.setGamePiece(null);
        targetTile.removeActionListener(this.targetTile.getGameTargetTileListener());
        targetTile.addActionListener(this.targetTile.getGamePieceListener());
        targetTile.setIsOccupied(true);
        targetTile.setSelected(false);
        if  (
                (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.RED &&
                        this.targetTile.getCoordinates().endsWith("1")) ||
                        (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.BLACK &&
                                this.targetTile.getCoordinates().endsWith("8"))
                ) {
            targetPiece.promotePiece();
            hasCaptureMove = false;
        }
        targetTile.setPieceTileImages();
        CheckerTile temp = (CheckerTile) targetTile;
        setSelectedTile(null);
        setTargetTile(null);
        //CheckerBoardController gameController = (CheckerBoardController) gameBoard.getGameBoardController();

        // If a jump was performed, check to make sure additional jumps are not required.
        if(hasCaptureMove) {
            hasCaptureMove = false;

            checkForOpponentJumpMove(temp.getGameBoardRow(), temp.getGameBoardCol());

            // If there are no additional captures to be made, reset the board.
            if(!hasCaptureMove) {
                gameBoard.setIsTurn(true);
                //gameController.enableSelectablePieces();
            }
        } else {
            gameBoard.setIsTurn(true);
            //gameController.enableSelectablePieces();
        }
    }

    /**
     * Checks to see if the opponent has any available jump moves.  If so, the hasEnemyJumpMove boolean is set.
     * @param row - The row of the Tile we are checking.
     * @param col - The column of the Tile we are checking.
     */
    private void checkForOpponentJumpMove(int row, int col) {
        CheckerPiece checkerPiece = (CheckerPiece) gameTiles[row][col].getGamePiece();
        if (
                checkerPiece.isKing() &&
                    ((row >= 2 && col >= 2 &&
                        gameTiles[row-1][col-1].isOccupied() &&
                        !gameTiles[row-2][col-2].isOccupied() &&
                        gameTiles[row-1][col-1].getGamePiece().getColor() == playerColor) ||
                    (row >= 2 && col <= 5 &&
                            gameTiles[row-1][col+1].isOccupied() &&
                            !gameTiles[row-2][col+2].isOccupied() &&
                            gameTiles[row-1][col+1].getGamePiece().getColor() == playerColor))
            ) {
            hasCaptureMove = true;
        }
    }

    /**
     * Checks to see if any pieces have been jumped.  If so, these pieces are captured.
     */
    protected void checkForAndPerformCaptures () {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        CheckerBoard checkerBoard = (CheckerBoard) gameBoard;
        if (Math.abs(selectedTileRow - targetTileRow) == 2) {
            if (selectedTileRow < targetTileRow && selectedTileCol > targetTileCol) {
                checkerBoard.capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol - 1]);
                hasCaptureMove = true;
            } else if (selectedTileRow < targetTileRow && selectedTileCol < targetTileCol) {
                checkerBoard.capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol + 1]);
                hasCaptureMove = true;
            } else if (selectedPiece.isKing()) {
                if (selectedTileRow > targetTileRow && selectedTileCol > targetTileCol) {
                    checkerBoard.capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol - 1]);
                    hasCaptureMove = true;
                } else if (selectedTileRow > targetTileRow && selectedTileCol < targetTileCol) {
                    checkerBoard.capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol + 1]);
                    hasCaptureMove = true;
                }
            }
        }
    }
}
