package CheckersClient;

import javax.swing.*;

/**
 * The CheckerTile class represents a tile on a CheckerBoard  They are capable of holding CheckerPiece objects.  They
 * contain coordinates, row and column information, and listener objects for CheckerPiece movement.
 *
 * Created by Tyler on 3/23/2016.
 */
public class CheckerTile extends GameTile {
    public static String IMAGE_PATH_EMPTY_TILE = "res\\EmptyTile.png";
    public static String IMAGE_PATH_EMPTY_HOVER_TILE = "res\\HoverTile.png";
    public static String IMAGE_PATH_EMPTY_SELECTED_TILE = "res\\SelectedTile.png";

    /**
     * Constructs a new, empty (Does not contain CheckerPiece) CheckerTile object that is capable of holding
     * a CheckerPiece.
     *
     * @param checkerBoard  The CheckerBoard to which this CheckerPiece belongs.
     * @param gameBoardRow  The row that this CheckerTile object occupies on the CheckerBoard.
     * @param gameBoardCol  The column That this CheckerTile object occupies on the CheckerBoard.
     * @param coordinates   The location of this CheckerTile (does not change) on the CheckerBoard.
     */
    public CheckerTile(CheckerBoard checkerBoard, int gameBoardRow, int gameBoardCol, String coordinates) {
        super(false, checkerBoard, null, gameBoardRow, gameBoardCol, coordinates);

        // Listeners
        gamePieceListener = new CheckerPieceListener(this);
        gameTargetTileListener = new CheckerTargetTileListener(this);

        addActionListener(gameTargetTileListener);
        setEmptyTileImages();
        setEnabled(false);
    }

    /**
     * Constructs a new, occupied (Contains CheckerPiece) CheckerTile object that is capable of holding
     * a CheckerPiece.
     *
     * @param checkerBoard  The CheckerBoard to which this CheckerPiece belongs.
     * @param gamePiece   The location of this CheckerTile (does not change) on the CheckerBoard.
     * @param gameBoardRow  The row that this CheckerTile object occupies on the CheckerBoard.
     * @param gameBoardCol  The column That this CheckerTile object occupies on the CheckerBoard.
     */
    public CheckerTile(CheckerBoard checkerBoard, CheckerPiece gamePiece, int gameBoardRow, int gameBoardCol) {
        super(true, checkerBoard, gamePiece, gameBoardRow, gameBoardCol, gamePiece.getCoordinates());

        // Listeners
        gamePieceListener = new CheckerPieceListener(this);
        gameTargetTileListener = new CheckerTargetTileListener(this);

        addActionListener(gamePieceListener);
        setPieceTileImages();
        setEnabled(false);
    }

    // Methods

    /**
     * Set this CheckerTile's images (For empty tile).
     */
    public void setEmptyTileImages() {
        setIcon(new ImageIcon(IMAGE_PATH_EMPTY_TILE));
        setDisabledIcon(new ImageIcon(IMAGE_PATH_EMPTY_TILE));
        setDisabledSelectedIcon(new ImageIcon(IMAGE_PATH_EMPTY_TILE));
        setRolloverIcon(new ImageIcon(IMAGE_PATH_EMPTY_HOVER_TILE));
        setRolloverSelectedIcon(new ImageIcon(IMAGE_PATH_EMPTY_HOVER_TILE));
        setPressedIcon(new ImageIcon(IMAGE_PATH_EMPTY_SELECTED_TILE));
        setSelectedIcon(new ImageIcon(IMAGE_PATH_EMPTY_SELECTED_TILE));
    }

    /**
     * Set this CheckerTile's images (For occupied tile).
     */
    public void setPieceTileImages() {
        setIcon(gamePiece.getImage());
        setDisabledIcon(gamePiece.getImage());
        setDisabledSelectedIcon(gamePiece.getImage());
        setRolloverIcon(gamePiece.getSelectedImage());
        setRolloverSelectedIcon(gamePiece.getSelectedImage());
        setPressedIcon(gamePiece.getSelectedImage());
        setSelectedIcon(gamePiece.getSelectedImage());
    }
}
