package CheckersClient;

import java.io.ObjectOutputStream;

/**
 * GameBoardController's handle movement and rules for GamePiece's.
 *
 * Created by Tyler on 4/22/2016.
 */
public abstract class GameBoardController {
    protected boolean hasCaptureMove;
    protected GameBoard gameBoard;
    protected GamePiece selectedPiece;
    protected GamePiece targetPiece;
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
     * Constructs a new GameBoardController.
     *
     * @param gameBoard the game board for moving pieces.
     */
    protected GameBoardController (
            GameBoard gameBoard, ObjectOutputStream outputStream,
            GamePiece.GamePieceColor playerColor, String username
        ) {
        hasCaptureMove = false;
        this.gameBoard = gameBoard;
        gameTiles = new CheckerTile[8][8];
        hasCaptureMove = false;
        this.outputStream = outputStream;
        this.playerColor = playerColor;
        this.username = username;
        setSelectedTile(null);
        setTargetTile(null);
    }

    // Setters

    /**
     * Select a tile's game piece for movement
     *
     * @param selectedTile  the tile with a game piece to move.
     */
    protected void setSelectedTile (GameTile selectedTile) {
        if (selectedTile != null) {
            this.selectedTile = selectedTile;
            this.selectedTileCol = selectedTile.getGameBoardCol();
            this.selectedTileRow = selectedTile.getGameBoardRow();
            this.selectedPiece = selectedTile.getGamePiece();
        } else {
            selectedTile = null;
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
    protected void setTargetTile (GameTile targetTile) {
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
     * Checks to see if any pieces have been jumped.  If so, these pieces are captured.
     */
    protected abstract void checkForAndPerformCaptures ();
}
