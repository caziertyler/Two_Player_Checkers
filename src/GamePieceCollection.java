package CheckersClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and contains all GamePieces and coordinates needed for a new game.
 *
 * Created by Tyler on 3/26/2016.
 */
public abstract class GamePieceCollection {
    protected List<CheckerPiece> gamePieces;
    protected String[][] firstPlayerCoordinates;
    protected String[][] secondPlayerCoordinates;

    /**
     * Constructs a GamePieceCollection object that stores the GamePieces and coordinates of a game.  It also
     * initializes game pieces starting coordinates.
     */
    protected GamePieceCollection() {
        gamePieces = new ArrayList<>();
    }

    // Getters
    /**
     * Get this GamePieceCollection's first playerCoordinates.
     * @return  the coordinates of the first player's board.
     */
    public String[][] getFirstPlayerCoordinates()  {
        return firstPlayerCoordinates;
    }

    /**
     * Get this GamePieceCollection's second player Coordinates.
     * @return  The coordinates of the second player's board.
     */
    public String[][] getSecondPlayerCoordinates() {
        return secondPlayerCoordinates;
    }

    /**
     * Get this GamePieceCollection's checker pieces.
     * @return - List<CheckerPieces> checkerPieces
     */
    public List<? extends GamePiece> getGamePieces()  {
        return gamePieces;
    }

    /**
     * Initialize GamePiece's starting location.
     */
    public abstract void initializeGamePieces();
}
