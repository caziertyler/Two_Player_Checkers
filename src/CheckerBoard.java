package CheckersClient;

import GameServer.game.GameType;
import GameServer.game.checkers.CheckersMessage;
import GameServer.game.message.StatusMessage;
import java.awt.*;
import java.io.*;

/**
 * Represents a CheckerBoard that contains CheckerTiles and CheckerPieces.  It has the necessary methods for moving
 * pieces and for capturing opponent checker pieces.
 *
 * Created by Tyler on 4/2/2016.
 */
public class CheckerBoard extends GameBoard {
    private boolean hasCaptureMove;
    private boolean hasOpponentCaptureMove;
    private int blackPiecesRemaining;
    private int redPiecesRemaining;

    // Getters

    int getBlackPiecesRemaining() { return blackPiecesRemaining; }

    int getRedPiecesRemaining() { return redPiecesRemaining; }

    // Methods

    /**
     * Place player and opponent pieces on the board.
     *
     * @param playerColor - The color of the player's checkers pieces.
     */
    public void initializeGameBoard(GamePiece.GamePieceColor playerColor, String username, ObjectOutputStream outputStream) {
        blackPiecesRemaining = 12;
        gamePieces = new CheckerPieceCollection();
        gameTiles = new CheckerTile[8][8];
        hasCaptureMove = false;
        hasOpponentCaptureMove = false;
        this.outputStream = outputStream;
        isTurn = false;
        this.playerColor = playerColor;
        redPiecesRemaining = 12;
        this.username = username;
        setSelectedTile(null);
        setTargetTile(null);

        if (playerColor.equals(GamePiece.GamePieceColor.RED)) {
            int checkerPieceNumber = 0;
            for (int row = 0; row <= 2; row++) {
                for (int col = 0; col <= 7; col++) {
                    if (row % 2 == 0) {
                        if (col % 2 == 1) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber++), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getFirstPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    } else {
                        if (col % 2 == 0) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber++), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getFirstPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    }
                }
            }
            for (int row = 3; row <= 4; row++) {
                for (int col = 0; col <= 7; col++) {
                    CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getFirstPlayerCoordinates()[row][col]);
                    addGameTile(checkerTile);
                }
            }
            for (int row = 5; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    if (row % 2 == 1) {
                        if (col % 2 == 0) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber++), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getFirstPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    } else {
                        if (col % 2 == 1) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber++), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getFirstPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    }
                }
            }
        } else {
            int checkerPieceNumber = 23;
            for (int row = 0; row <= 2; row++) {
                for (int col = 0; col <= 7; col++) {
                    if (row % 2 == 0) {
                        if (col % 2 == 1) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece) gamePieces.getGamePieces().get(checkerPieceNumber--), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getSecondPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    } else {
                        if (col % 2 == 0) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece) gamePieces.getGamePieces().get(checkerPieceNumber--), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getSecondPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    }
                }
            }
            for (int row = 3; row <= 4; row++) {
                for (int col = 0; col <= 7; col++) {
                    CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getSecondPlayerCoordinates()[row][col]);
                    addGameTile(checkerTile);
                }
            }
            for (int row = 5; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    if (row % 2 == 1) {
                        if (col % 2 == 0) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber--), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getSecondPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    } else {
                        if (col % 2 == 1) {
                            CheckerTile checkerTile = new CheckerTile(this, (CheckerPiece)gamePieces.getGamePieces().get(checkerPieceNumber--), row, col);
                            addGameTile(checkerTile);
                        } else {
                            CheckerTile checkerTile = new CheckerTile(this, row, col, gamePieces.getSecondPlayerCoordinates()[row][col]);
                            addGameTile(checkerTile);
                        }
                    }
                }
            }
        }
    }

    /**
     * Add a CheckerTile to this GameBoard.
     *
     * @param gameTile - The CheckerTile to add to this checkerBoard.
     */
    protected void addGameTile(GameTile gameTile) {
        CheckerTile checkerTile = (CheckerTile) gameTile;
        checkerTile.setBackground(new Color(0, 0, 0, 0));
        checkerTile.setPreferredSize(new Dimension(this.getWidth() / 8, this.getHeight() / 8));
        checkerTile.setOpaque(false);
        gameTiles[checkerTile.getGameBoardRow()][checkerTile.getGameBoardCol()] = checkerTile;
        add(checkerTile);
    }

    /**
     * Disables all CheckerTile buttons.
     */
    public void freezeBoard() {
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameTiles[row][col];
                tile.setEnabled(false);
            }
        }
    }

    // Player Moves

    /**
     * Enable the selected tile and all empty CheckerTile.
     */
    void enableSelectablePieces() {
        if(!isTurn) {
            freezeBoard();
            return;
        }

        hasCaptureMove = false;

        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameTiles[row][col];
                CheckerPiece piece = (CheckerPiece) tile.getGamePiece();
                if (!(tile.isOccupied())) {
                    tile.setEnabled(false);
                } else if (piece.getColor() == playerColor) {
                    checkForAndEnableJumpMoveCheckers(row, col);
                }
            }
        }

        if (!hasCaptureMove) {
            for (int row = 0; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    GameTile tile = gameTiles[row][col];
                    CheckerPiece piece = (CheckerPiece) tile.getGamePiece();
                    if ((tile.isOccupied()) &&
                            piece.getColor() == playerColor) {
                        enableValidNonJumpMoveCheckers(row, col);
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
    private void checkForAndEnableJumpMoveCheckers(int row, int col) {
        CheckerPiece checkerPiece = (CheckerPiece) gameTiles[row][col].getGamePiece();
        if (
                !checkerPiece.isKing() &&
                    ((row >= 2 && col >= 2 &&
                        gameTiles[row-1][col-1].isOccupied() &&
                        !gameTiles[row-2][col-2].isOccupied() &&
                        gameTiles[row-1][col-1].getGamePiece().getColor() != playerColor) ||
                    (row >= 2 && col <= 5 &&
                        gameTiles[row-1][col+1].isOccupied() &&
                        !gameTiles[row-2][col+2].isOccupied() &&
                        gameTiles[row-1][col+1].getGamePiece().getColor() != playerColor))
            ) {
            gameTiles[row][col].setEnabled(true);
            hasCaptureMove = true;
        } else if (
                checkerPiece.isKing() &&
                    ((row >= 2 && col >= 2 &&
                        gameTiles[row-1][col-1].isOccupied() &&
                        !gameTiles[row-2][col-2].isOccupied() &&
                        gameTiles[row-1][col-1].getGamePiece().getColor() != playerColor) ||
                    (row >= 2 && col <= 5 &&
                        gameTiles[row-1][col+1].isOccupied() &&
                        !gameTiles[row-2][col+2].isOccupied() &&
                        gameTiles[row-1][col+1].getGamePiece().getColor() != playerColor) ||
                    (row <= 5 && col >= 2 &&
                        gameTiles[row+1][col-1].isOccupied() &&
                        !gameTiles[row+2][col-2].isOccupied() &&
                        gameTiles[row+1][col-1].getGamePiece().getColor() != playerColor) ||
                    (row <= 5 && col <= 5 &&
                        gameTiles[row+1][col+1].isOccupied() &&
                        !gameTiles[row+2][col+2].isOccupied() &&
                        gameTiles[row+1][col+1].getGamePiece().getColor() != playerColor))
                ) {
            gameTiles[row][col].setEnabled(true);
            hasCaptureMove = true;
        } else {
            gameTiles[row][col].setEnabled(false);
        }
    }

    /**
     * Check for valid non-jump moves.  If available, enable the tiles with a checker and a valid non-jump move as a
     * selectable CheckerTile.
     */
    private void enableValidNonJumpMoveCheckers(int row, int col) {
        CheckerPiece checkerPiece = (CheckerPiece) gameTiles[row][col].getGamePiece();
        if (
            !checkerPiece.isKing() &&
                ((row >= 1 && col >= 1 &&
                    !gameTiles[row-1][col-1].isOccupied()) ||
                (row >= 1 && col <= 6 &&
                    !gameTiles[row-1][col+1].isOccupied()))
            ) {
            gameTiles[row][col].setEnabled(true);
        } else if (
                checkerPiece.isKing() &&
                ((row >= 1 && col >= 1 &&
                    !gameTiles[row-1][col-1].isOccupied()) ||
                (row >= 1 && col <= 6 &&
                    !gameTiles[row-1][col+1].isOccupied()) ||
                (row <= 6 && col >= 1 &&
                    !gameTiles[row+1][col-1].isOccupied()) ||
                (row <= 6 && col <= 6 &&
                    !gameTiles[row+1][col+1].isOccupied()))
                ) {
            gameTiles[row][col].setEnabled(true);
        } else {
            gameTiles[row][col].setEnabled(false);
        }
    }

    /**
     * Set this CheckerTile as its GameBoard's selectedTile. This disables all invalid tiles for GamePiece move and
     * enables all valid tiles for GamePiece move.
     */
    public void selectGamePiece(GameTile selectedTile) {
        // Check to see if this tile already selected.  If so, deselect and enable selectable pieces.
        if (this.selectedTile != null && this.selectedTile.getCoordinates().equals(selectedTile.getCoordinates())) {
            setSelectedTile(null);
            enableSelectablePieces();
            SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
            return;
        }

        SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
        setSelectedTile(selectedTile);
        enableTargetTiles();
    }

    /**
     * Enable all CheckerTiles that contain CheckerPieces
     */
    public void enableTargetTiles() {

        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameTiles[row][col];

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
    public void enableCaptureMoves() {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        if (
                selectedTileRow >= 2 && selectedTileCol >= 2 &&
                gameTiles[selectedTileRow -1][selectedTileCol -1].isOccupied() &&
                !gameTiles[selectedTileRow -2][selectedTileCol -2].isOccupied() &&
                gameTiles[selectedTileRow -1][selectedTileCol -1].getGamePiece().getColor() != playerColor
            ) {
            gameTiles[selectedTileRow -2][selectedTileCol -2].setEnabled(true);
        }
        if
            (
            selectedTileRow >= 2 && selectedTileCol <= 5 &&
                    gameTiles[selectedTileRow -1][selectedTileCol +1].isOccupied() &&
                    !gameTiles[selectedTileRow -2][selectedTileCol +2].isOccupied() &&
                    gameTiles[selectedTileRow -1][selectedTileCol +1].getGamePiece().getColor() != playerColor
            ) {
            gameTiles[selectedTileRow -2][selectedTileCol +2].setEnabled(true);
        }
        if (selectedPiece.isKing()) {
            if
                (
                    selectedTileRow <= 5 && selectedTileCol >= 2 &&
                    gameTiles[selectedTileRow +1][selectedTileCol -1].isOccupied() &&
                    !gameTiles[selectedTileRow +2][selectedTileCol -2].isOccupied() &&
                    gameTiles[selectedTileRow +1][selectedTileCol -1].getGamePiece().getColor() != playerColor
                ) {
                gameTiles[selectedTileRow +2][selectedTileCol -2].setEnabled(true);
            }
            if
                (
                    selectedTileRow <= 5 && selectedTileCol <= 5 &&
                    gameTiles[selectedTileRow +1][selectedTileCol +1].isOccupied() &&
                    !gameTiles[selectedTileRow +2][selectedTileCol +2].isOccupied() &&
                    gameTiles[selectedTileRow +1][selectedTileCol +1].getGamePiece().getColor() != playerColor
                ) {
                gameTiles[selectedTileRow +2][selectedTileCol +2].setEnabled(true);
            }
        }
    }

    /**
     * Enables all possible target CheckerTiles that would represent a valid non-Capture move for the selectedPiece.
     */
    public void enableValidNonCaptureMoves() {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        if (
                selectedTileRow >= 1 && selectedTileCol >= 1 &&
                !gameTiles[selectedTileRow -1][selectedTileCol -1].isOccupied()
            ) {
            gameTiles[selectedTileRow -1][selectedTileCol -1].setEnabled(true);
        }
        if
            (
                selectedTileRow >= 1 && selectedTileCol <= 6 &&
                !gameTiles[selectedTileRow -1][selectedTileCol +1].isOccupied()
            ) {
            gameTiles[selectedTileRow -1][selectedTileCol +1].setEnabled(true);
        }

        if (selectedPiece.isKing()) {
            if (
                    selectedTileRow <= 6 && selectedTileCol >= 1 &&
                    !gameTiles[selectedTileRow +1][selectedTileCol -1].isOccupied()
                ) {
                gameTiles[selectedTileRow +1][selectedTileCol - 1].setEnabled(true);
            }
            if
            (
                selectedTileRow <= 6 && selectedTileCol <= 6 &&
                !gameTiles[selectedTileRow +1][selectedTileCol +1].isOccupied()
            ) {
                gameTiles[selectedTileRow +1][selectedTileCol +1].setEnabled(true);
            }
        }
    }

    /**
     * Move this selectedTile Checker to the targetTile.
     */
    public void moveGamePiece(GameTile targetTile) {
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
        CheckerPiece tempPiece = (CheckerPiece) targetTile.getGamePiece();
        if  (
                !tempPiece.isKing() &&
                (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.RED &&
                    this.targetTile.getCoordinates().endsWith("1")) ||
                (this.targetTile.getGamePiece().getColor() == GamePiece.GamePieceColor.BLACK &&
                    this.targetTile.getCoordinates().endsWith("8"))
            ) {
            targetTile.getGamePiece().promotePiece();
            hasCaptureMove = false;
        }
        targetTile.setPieceTileImages();

        setSelectedTile(null);
        setTargetTile(null);

        // If a jump was performed, check to make sure additional jumps are not required.
        if(hasCaptureMove) {
            if (
                    playerColor == GamePiece.GamePieceColor.RED && blackPiecesRemaining == 0 ||
                    playerColor == GamePiece.GamePieceColor.BLACK && redPiecesRemaining == 0
                ) {
                        SoundPlayer.playSound(SoundPlayer.SOUND_PATH_GAME_WIN);
            }
            hasCaptureMove = false;
            CheckerTile temp = (CheckerTile) targetTile;

            // Reset all tiles but the piece that just performed a capture.
            for (int row = 0; row <= 7; row++) {
                for (int col = 0; col <= 7; col++) {
                    GameTile tile = gameTiles[row][col];

                    if(tile.getCoordinates().equals(temp.getCoordinates())) {
                        tile.setEnabled(true);
                    } else {
                        tile.setEnabled(false);
                    }
                }
            }

            checkForAndEnableJumpMoveCheckers(temp.getGameBoardRow(), temp.getGameBoardCol());

            // If there are no additional captures to be made, reset the board.
            if(!hasCaptureMove) {
                isTurn = false;
                enableSelectablePieces();
            }
        } else {
                isTurn = false;
                enableSelectablePieces();
            }
    }

    /**
     * Checks to see if any pieces have been jumped.  If so, these pieces are captured.
     */
    private void checkForAndPerformCaptures() {
        CheckerPiece selectedPiece = (CheckerPiece) this.selectedPiece;
        if(selectedPiece.getColor() == playerColor) { // Player Jumped.
            if (
                    hasCaptureMove &&
                    selectedPiece.getColor() == playerColor
                ) {
                if (selectedTileRow > targetTileRow && selectedTileCol > targetTileCol) {
                    capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol - 1]);
                } else if (selectedTileRow > targetTileRow && selectedTileCol < targetTileCol) {
                    capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol + 1]);
                } else if (selectedTileRow < targetTileRow && selectedTileCol > targetTileCol) {
                    capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol - 1]);
                } else {
                    capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol + 1]);
                }
            } else {
                SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_PLACE);
            }
        } else { // Opponent Jumped.
            if (Math.abs(selectedTileRow - targetTileRow) == 2) {
                if (selectedTileRow < targetTileRow && selectedTileCol > targetTileCol) {
                    capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol - 1]);
                    hasOpponentCaptureMove = true;
                } else if (selectedTileRow < targetTileRow && selectedTileCol < targetTileCol) {
                    capturePiece(gameTiles[selectedTileRow + 1][selectedTileCol + 1]);
                    hasOpponentCaptureMove = true;
                } else if (selectedPiece.isKing()) {
                    if (selectedTileRow > targetTileRow && selectedTileCol > targetTileCol) {
                        capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol - 1]);
                        hasOpponentCaptureMove = true;
                    } else if (selectedTileRow > targetTileRow && selectedTileCol < targetTileCol) {
                        capturePiece(gameTiles[selectedTileRow - 1][selectedTileCol + 1]);
                        hasOpponentCaptureMove = true;
                    }
                }
            }
        }
    }

    /**
     * Removes captured CheckerPiece from the game.
     *
     * @param capturedPieceTile - The CheckerTile with the CheckerPiece to capture.s
     */
    public void capturePiece(GameTile capturedPieceTile) {
        SoundPlayer.playSound(SoundPlayer.SOUND_PATH_CHECKER_JUMP);
        // Update Score.
        if (capturedPieceTile.getGamePiece().getColor() == GamePiece.GamePieceColor.RED) {
            redPiecesRemaining--;
        } else {
            blackPiecesRemaining--;
        }

        // Empty the CheckerTile that contains the captured CheckerPiece.
        capturedPieceTile.removeActionListener(capturedPieceTile.getGamePieceListener());
        capturedPieceTile.addActionListener(capturedPieceTile.getGameTargetTileListener());
        capturedPieceTile.setIsOccupied(false);
        capturedPieceTile.setSelected(false);
        capturedPieceTile.setEmptyTileImages();
        capturedPieceTile.setEnabled(false);
        getGamePieces().getGamePieces().remove(capturedPieceTile.getGamePiece());
        capturedPieceTile.setGamePiece(null);

    }

    // Opponent Moves

    /**
     * This receives an opponent's move from the server and updates the board accordingly.
     * @param cp - The update checker piece from the opponent.
     * @return boolean - Is it this player's turn?
     */
    boolean receiveOpponentMove (GameServer.game.GamePieceMove cp) {
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                GameTile tile = gameTiles[row][col];
                if (tile.getGamePiece() != null && tile.getGamePiece().getId() == cp.getId()) {
                    setSelectedTile(tile);
                }

                if (tile.getCoordinates().equals(cp.getCoordinates())) {
                    setTargetTile(tile);
                }
            }
        }
        moveOpponentPiece();

        return !hasOpponentCaptureMove;
    }

    /**
     * After an opponent move is received, this method updates the checker board.
     */
    public void moveOpponentPiece() {
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
            hasOpponentCaptureMove = false;
        }
        targetTile.setPieceTileImages();
        CheckerTile temp = (CheckerTile) targetTile;
        setSelectedTile(null);
        setTargetTile(null);

        // If a jump was performed, check to make sure additional jumps are not required.
        if(hasOpponentCaptureMove) {
            if (
                    playerColor == GamePiece.GamePieceColor.RED && redPiecesRemaining == 0 ||
                            playerColor == GamePiece.GamePieceColor.BLACK && blackPiecesRemaining == 0
                    ) {
                try {
                    try {
                        Thread.sleep(1000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    outputStream.writeObject(StatusMessage.getEndMessage(GameType.CHECKERS, username));
                    outputStream.flush();
                    SoundPlayer.playSound(SoundPlayer.SOUND_PATH_GAME_LOSE);
                } catch (IOException e) {
                    System.out.println("Error.");
                }
            }
            hasOpponentCaptureMove = false;

            checkForOpponentJumpMove(temp.getGameBoardRow(), temp.getGameBoardCol());

            // If there are no additional captures to be made, reset the board.
            if(!hasOpponentCaptureMove) {
                isTurn = true;
                enableSelectablePieces();
            }
        } else {
            isTurn = true;
            enableSelectablePieces();
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
                (row <= 5 && col >= 2 &&
                        gameTiles[row+1][col-1].isOccupied() &&
                        !gameTiles[row+2][col-2].isOccupied() &&
                        gameTiles[row+1][col-1].getGamePiece().getColor() == playerColor) ||
                (row <= 5 && col <= 5 &&
                        gameTiles[row+1][col+1].isOccupied() &&
                        !gameTiles[row+2][col+2].isOccupied() &&
                        gameTiles[row+1][col+1].getGamePiece().getColor() == playerColor)
            ) {
            hasOpponentCaptureMove = true;
        } else if (
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
            hasOpponentCaptureMove = true;
        }
    }
}