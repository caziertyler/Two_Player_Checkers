package CheckersClient;

/**
 * This CheckerPieceCollection class constructs an array of new checker pieces with ID's, images, and coordinates
 * for player and opponent.
 *
 * @author Tyler Cazier
 * @version 8/13/15
 */
public class CheckerPieceCollection extends GamePieceCollection
{
    /**
     * Constructs a CheckerPieceCollection object that stores the CheckerPieces and coordinates of a game.  It also
     * initializes checker pieces starting coordinates and color.
     */
    public CheckerPieceCollection() {
        firstPlayerCoordinates = new String[][] {
                    {"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1"},
                    {"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2"},
                    {"A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3"},
                    {"A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4"},
                    {"A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5"},
                    {"A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6"},
                    {"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7"},
                    {"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8"}
        };

        secondPlayerCoordinates = new String[][] {
                {"H8", "G8", "F8", "E8", "D8", "C8", "B8", "A8"},
                {"H7", "G7", "F7", "E7", "D7", "C7", "B7", "A7"},
                {"H6", "G6", "F6", "E6", "D6", "C6", "B6", "A6"},
                {"H5", "G5", "F5", "E5", "D5", "C5", "B5", "A5"},
                {"H4", "G4", "F4", "E4", "D4", "C4", "B4", "A4"},
                {"H3", "G3", "F3", "E3", "D3", "C3", "B3", "A3"},
                {"H2", "G2", "F2", "E2", "D2", "C2", "B2", "A2"},
                {"H1", "G1", "F1", "E1", "D1", "C1", "B1", "A1"}
        };

        initializeGamePieces();
    }

    // Methods

    /**
     * Initialize Checker Pieces color and starting location.
     */
    public void initializeGamePieces() {
        // Initialize Checker Pieces.
        int pieceNumber = 0;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 7; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        gamePieces.add(new CheckerPiece(GamePiece.GamePieceColor.BLACK, pieceNumber++, firstPlayerCoordinates[i][j]));
                    }
                } else {
                    if (j % 2 == 0) {
                        gamePieces.add(new CheckerPiece(GamePiece.GamePieceColor.BLACK, pieceNumber++, firstPlayerCoordinates[i][j]));
                    }
                }
            }
        }
        for (int i = 5; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (i % 2 == 1) {
                    if (j % 2 == 0) {
                        gamePieces.add(new CheckerPiece(GamePiece.GamePieceColor.RED, pieceNumber++, firstPlayerCoordinates[i][j]));
                    }
                } else {
                    if (j % 2 == 1) {
                        gamePieces.add(new CheckerPiece(GamePiece.GamePieceColor.RED, pieceNumber++, firstPlayerCoordinates[i][j]));
                    }
                }
            }
        }
    }
}