package CheckersClient;

import GameServer.game.GameType;
import GameServer.game.checkers.CheckersMessage;
import GameServer.game.message.StatusMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * CheckerBoardController works with a game board to move pieces and controls which tiles are selectable.
 *
 * Created by Tyler on 4/22/2016.
 */
public class CheckerBoardController extends GameBoardController {

    /**
     * Constructs a new CheckerBoardController used to move GameBoard pieces and control which GameBoard tiles are
     * active.
     *
     * @param gameBoard the game board for that this controller manipulates.
     */
    public CheckerBoardController (
                GameBoard gameBoard, ObjectOutputStream outputStream,
                GamePiece.GamePieceColor playerColor, String username
        ) {
        super(gameBoard, outputStream, playerColor, username);
    }

    /**
     * Enable the selected tile and all empty CheckerTile.
     */
    public void enableSelectablePieces () {
        if(!gameBoard.isTurn()) {
            gameBoard.freezeBoard();
            return;
        }

        hasCaptureMove = false;

        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameBoard.getGameTiles()[row][col];
                CheckerPiece piece = (CheckerPiece) tile.getGamePiece();
                if (!(tile.isOccupied())) {
                    tile.setEnabled(false);
                } else if (piece.getColor() == gameBoard.getPlayerColor()) {
                    checkForAndEnableJumpMoveGamePieces(row, col);
                }
            }
        }

        if (!hasCaptureMove) {
            for (int row = 0; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    GameTile tile = gameBoard.getGameTiles()[row][col];
                    CheckerPiece piece = (CheckerPiece) tile.getGamePiece();
                    if ((tile.isOccupied()) &&
                            piece.getColor() == gameBoard.getPlayerColor()) {
                        enableValidNonJumpMoveGamePieces(row, col);
                    }
                }
            }
        }
    }

    /**
     * Check for available jump moves.  If available, enable (make selectable for move) the tiles that contains a
     * checker that can make a valid jump move.
     * @param row - The row of the Tile we are checking.
     * @param col - The column of the Tile we are checking.
     */
    protected void checkForAndEnableJumpMoveGamePieces (int row, int col) {
        CheckerPiece checkerPiece = (CheckerPiece) gameBoard.getGameTiles()[row][col].getGamePiece();
        if (
                !checkerPiece.isKing() &&
                        ((row >= 2 && col >= 2 &&
                                gameBoard.getGameTiles()[row-1][col-1].isOccupied() &&
                                !gameBoard.getGameTiles()[row-2][col-2].isOccupied() &&
                                gameBoard.getGameTiles()[row-1][col-1].getGamePiece().getColor() != gameBoard.getPlayerColor()) ||
                                (row >= 2 && col <= 5 &&
                                        gameBoard.getGameTiles()[row-1][col+1].isOccupied() &&
                                        !gameBoard.getGameTiles()[row-2][col+2].isOccupied() &&
                                        gameBoard.getGameTiles()[row-1][col+1].getGamePiece().getColor() != gameBoard.getPlayerColor()))
                ) {
            gameBoard.getGameTiles()[row][col].setEnabled(true);
            hasCaptureMove = true;
        } else if (
                checkerPiece.isKing() &&
                        ((row >= 2 && col >= 2 &&
                                gameBoard.getGameTiles()[row-1][col-1].isOccupied() &&
                                !gameBoard.getGameTiles()[row-2][col-2].isOccupied() &&
                                gameBoard.getGameTiles()[row-1][col-1].getGamePiece().getColor() != gameBoard.getPlayerColor()) ||
                                (row >= 2 && col <= 5 &&
                                        gameBoard.getGameTiles()[row-1][col+1].isOccupied() &&
                                        !gameBoard.getGameTiles()[row-2][col+2].isOccupied() &&
                                        gameBoard.getGameTiles()[row-1][col+1].getGamePiece().getColor() != gameBoard.getPlayerColor()) ||
                                (row <= 5 && col >= 2 &&
                                        gameBoard.getGameTiles()[row+1][col-1].isOccupied() &&
                                        !gameBoard.getGameTiles()[row+2][col-2].isOccupied() &&
                                        gameBoard.getGameTiles()[row+1][col-1].getGamePiece().getColor() != gameBoard.getPlayerColor()) ||
                                (row <= 5 && col <= 5 &&
                                        gameBoard.getGameTiles()[row+1][col+1].isOccupied() &&
                                        !gameBoard.getGameTiles()[row+2][col+2].isOccupied() &&
                                        gameBoard.getGameTiles()[row+1][col+1].getGamePiece().getColor() != gameBoard.getPlayerColor()))
                ) {
            gameBoard.getGameTiles()[row][col].setEnabled(true);
            hasCaptureMove = true;
        } else {
            gameBoard.getGameTiles()[row][col].setEnabled(false);
        }
    }

    /**
     * Check for valid non-jump moves.  If available, enable the tiles with a checker and a valid non-jump move as a
     * selectable CheckerTile.
     */
    protected void enableValidNonJumpMoveGamePieces (int row, int col) {
        CheckerPiece checkerPiece = (CheckerPiece) gameBoard.getGameTiles()[row][col].getGamePiece();
        if (
                !checkerPiece.isKing() &&
                        ((row >= 1 && col >= 1 &&
                                !gameBoard.getGameTiles()[row-1][col-1].isOccupied()) ||
                                (row >= 1 && col <= 6 &&
                                        !gameBoard.getGameTiles()[row-1][col+1].isOccupied()))
                ) {
            gameBoard.getGameTiles()[row][col].setEnabled(true);
        } else if (
                checkerPiece.isKing() &&
                        ((row >= 1 && col >= 1 &&
                                !gameBoard.getGameTiles()[row-1][col-1].isOccupied()) ||
                                (row >= 1 && col <= 6 &&
                                        !gameBoard.getGameTiles()[row-1][col+1].isOccupied()) ||
                                (row <= 6 && col >= 1 &&
                                        !gameBoard.getGameTiles()[row+1][col-1].isOccupied()) ||
                                (row <= 6 && col <= 6 &&
                                        !gameBoard.getGameTiles()[row+1][col+1].isOccupied()))
                ) {
            gameBoard.getGameTiles()[row][col].setEnabled(true);
        } else {
            gameBoard.getGameTiles()[row][col].setEnabled(false);
        }
    }

    /**
     * Set this CheckerTile as its GameBoard's selectedTile. This disables all invalid tiles for GamePiece move and
     * enables all valid tiles for GamePiece move.
     */
    public void selectGamePiece (GameTile selectedTile) {
        // Check to see if this tile already selected.  If so, deselect and enable selectable pieces.
        if (gameBoard.getSelectedTile() != null && gameBoard.getSelectedTile().getCoordinates().equals(selectedTile.getCoordinates())) {
            gameBoard.setSelectedTile(null);
            enableSelectablePieces();
            SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
            return;
        }

        SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
        gameBoard.setSelectedTile(selectedTile);
        enableTargetTiles();
    }

    /**
     * Enable all CheckerTiles that contain CheckerPieces
     */
    protected void enableTargetTiles () {

        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameBoard.getGameTiles()[row][col];

                if (tile.getCoordinates().equals(selectedPiece.getCoordinates())) {
                    tile.setEnabled(true);
                } else {
                    tile.setEnabled(false);
                }
            }
        }

        if (hasCaptureMove) {
            enableCaptureMoves();
        } else {
            enableValidNonCaptureMoves();
        }
    }

    /**
     * Enables all possible target CheckerTiles that would allow the current selectedPiece to capture an enemy piece.
     */
    public void enableCaptureMoves () {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        if (
                selectedTileRow >= 2 && selectedTileCol >= 2 &&
                        gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol -1].isOccupied() &&
                        !gameBoard.getGameTiles()[selectedTileRow -2][selectedTileCol -2].isOccupied() &&
                        gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol -1].getGamePiece().getColor() != gameBoard.getPlayerColor()
                ) {
            gameBoard.getGameTiles()[selectedTileRow -2][selectedTileCol -2].setEnabled(true);
        }
        if
                (
                selectedTileRow >= 2 && selectedTileCol <= 5 &&
                        gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol +1].isOccupied() &&
                        !gameBoard.getGameTiles()[selectedTileRow -2][selectedTileCol +2].isOccupied() &&
                        gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol +1].getGamePiece().getColor() != gameBoard.getPlayerColor()
                ) {
            gameBoard.getGameTiles()[selectedTileRow -2][selectedTileCol +2].setEnabled(true);
        }
        if (selectedPiece.isKing()) {
            if
                    (
                    selectedTileRow <= 5 && selectedTileCol >= 2 &&
                            gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol -1].isOccupied() &&
                            !gameBoard.getGameTiles()[selectedTileRow +2][selectedTileCol -2].isOccupied() &&
                            gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol -1].getGamePiece().getColor() != gameBoard.getPlayerColor()
                    ) {
                gameBoard.getGameTiles()[selectedTileRow +2][selectedTileCol -2].setEnabled(true);
            }
            if
                    (
                    selectedTileRow <= 5 && selectedTileCol <= 5 &&
                            gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol +1].isOccupied() &&
                            !gameBoard.getGameTiles()[selectedTileRow +2][selectedTileCol +2].isOccupied() &&
                            gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol +1].getGamePiece().getColor() != gameBoard.getPlayerColor()
                    ) {
                gameBoard.getGameTiles()[selectedTileRow +2][selectedTileCol +2].setEnabled(true);
            }
        }
    }

    /**
     * Enables all possible target CheckerTiles that would represent a valid non-Capture move for the selectedPiece.
     */
    public void enableValidNonCaptureMoves () {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        if (
                selectedTileRow >= 1 && selectedTileCol >= 1 &&
                        !gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol -1].isOccupied()
                ) {
            gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol -1].setEnabled(true);
        }
        if
                (
                selectedTileRow >= 1 && selectedTileCol <= 6 &&
                        !gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol +1].isOccupied()
                ) {
            gameBoard.getGameTiles()[selectedTileRow -1][selectedTileCol +1].setEnabled(true);
        }

        if (selectedPiece.isKing()) {
            if (
                    selectedTileRow <= 6 && selectedTileCol >= 1 &&
                            !gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol -1].isOccupied()
                    ) {
                gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol - 1].setEnabled(true);
            }
            if
                    (
                    selectedTileRow <= 6 && selectedTileCol <= 6 &&
                            !gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol +1].isOccupied()
                    ) {
                gameBoard.getGameTiles()[selectedTileRow +1][selectedTileCol +1].setEnabled(true);
            }
        }
    }

    /**
     * Move this selectedTile Checker to the targetTile.
     */
    public void moveGamePiece (GameTile targetTile) {
        CheckerBoard checkerBoard = (CheckerBoard) gameBoard;
        setTargetTile(targetTile);
        checkForAndPerformCaptures();

        // Update CheckerPiece coordinates
        selectedTile.getGamePiece().setCoordinates(this.targetTile.getCoordinates());

        // Empty the CheckerTile that contains the moving CheckerPiece.
        selectedTile.removeActionListener(selectedTile.getGamePieceListener());
        selectedTile.addActionListener(selectedTile.getGameTargetTileListener());
        selectedTile.setIsOccupied(false);
        selectedTile.setSelected(false);
        selectedTile.setEmptyTileImages();
        selectedTile.setEnabled(false);

        // Move the CheckerPiece
        this.targetTile.setGamePiece(selectedPiece);
        targetPiece = selectedPiece;
        selectedTile.setGamePiece(null);

        // Send CheckerPiece to opponent.
        try {
            outputStream.writeObject
                    (new CheckersMessage
                                    (new GameServer.game.GamePieceMove
                                            (
                                                    targetPiece.getId(),
                                                    targetPiece.getCoordinates()
                                            ),
                                            username
                                    )
                    );
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Server is unable to read message.  Server may not be bound on socket.");
        }

        this.targetTile.removeActionListener(this.targetTile.getGameTargetTileListener());
        this.targetTile.addActionListener(this.targetTile.getGamePieceListener());
        this.targetTile.setIsOccupied(true);
        this.targetTile.setSelected(false);
        if  (
                (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.RED &&
                        this.targetTile.getCoordinates().endsWith("1")) ||
                        (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.BLACK &&
                                this.targetTile.getCoordinates().endsWith("8"))
                ) {
            targetTile.getGamePiece().promotePiece();
            hasCaptureMove = false;
        }
        targetTile.setPieceTileImages();

        gameBoard.setSelectedTile(null);
        setTargetTile(null);

        // If a jump was performed, check to make sure additional jumps are not required.
        if(hasCaptureMove) {
            if (
                    gameBoard.getPlayerColor() == GamePiece.GamePieceColor.RED && checkerBoard.getBlackPiecesRemaining() == 0 ||
                    gameBoard.getPlayerColor() == GamePiece.GamePieceColor.BLACK && checkerBoard.getRedPiecesRemaining() == 0
                ) {
                try {
                    outputStream.writeObject(StatusMessage.getEndMessage(GameType.CHECKERS, username));
                    outputStream.flush();
                    SoundPlayer.playSound(SoundPlayer.SOUND_PATH_GAME_LOSE);
                } catch (IOException e) {
                    System.out.println("Error.");
                }
            }
            hasCaptureMove = false;
            CheckerTile temp = (CheckerTile) targetTile;

            // Reset all tiles but the piece that just performed a capture.
            for (int row = 0; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    GameTile tile = gameBoard.getGameTiles()[row][col];

                    if(tile.getCoordinates().equals(temp.getCoordinates())) {
                        tile.setEnabled(true);
                    } else {
                        tile.setEnabled(false);
                    }
                }
            }

            checkForAndEnableJumpMoveGamePieces(temp.getGameBoardRow(), temp.getGameBoardCol());

            // If there are no additional captures to be made, reset the board.
            if(!hasCaptureMove) {
                gameBoard.setIsTurn(false);
                enableSelectablePieces();
            }
        } else {
            gameBoard.setIsTurn(false);
            enableSelectablePieces();
        }
    }

    /**
     * Checks to see if any pieces have been jumped.  If so, these pieces are captured.
     */
    protected void checkForAndPerformCaptures () {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        CheckerBoard checkerBoard = (CheckerBoard) gameBoard;
        if (
                hasCaptureMove &&
                selectedPiece.getColor() == playerColor
            ) {
            if (selectedTileRow > targetTileRow && selectedTileCol > targetTileCol) {
                checkerBoard.capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol - 1]);
            } else if (selectedTileRow > targetTileRow && selectedTileCol < targetTileCol) {
                checkerBoard.capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol + 1]);
            } else if (selectedTileRow < targetTileRow && selectedTileCol > targetTileCol) {
                checkerBoard.capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol - 1]);
            } else {
                checkerBoard.capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol + 1]);
            }
        } else {
            SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
        }
    }
}
