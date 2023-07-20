package MuTorere;

public class Board {

    public enum Piece {
        BLANK, ONE, TWO
    }

    Piece[] boardLocations;
    int blankLocation;

    public Board() {
        boardLocations = new Piece[]{Piece.ONE, Piece.ONE, Piece.ONE, Piece.ONE, Piece.TWO, Piece.TWO, Piece.TWO, Piece.TWO, Piece.BLANK};
        blankLocation = 8;
    }

    public Piece pieceAt(int index) {
        if (index < 0 || index > boardLocations.length) {
            return Piece.BLANK;
        } else {
            return boardLocations[index];
        }
    }

    public boolean makeMove(int index, Piece playerID) {
        if (!isValidMove(index, playerID)) {
            return false;
        }
        boardLocations[blankLocation] = playerID;
        boardLocations[index] = Piece.BLANK;
        blankLocation = index;
        return true;
    }

    boolean isValidMove(int index, Piece playerID) {
        if (playerID != boardLocations[index]) {
            return false;
        }
        if (blankLocation == 8) {
            // Move to centre, check for valid neighbour
            int prev = index - 1;
            if (prev < 0) {
                prev = 7;
            }
            int next = index + 1;
            if (next > 7) {
                next = 0;
            }
            if (pieceAt(prev) == playerID && pieceAt(next) == playerID) {
                return false;
            }
        } else {
            // Either move from centre to kewai...
            if (index == 8) {
                return true;
            }
            // ... or from one kewai to next, make sure they are neighbours
            int prev = index - 1;
            if (prev < 0) {
                prev = 7;
            }
            int next = index + 1;
            if (next > 7) {
                next = 0;
            }
            if (prev != blankLocation && next != blankLocation) {
                return false;
            }
        }
        return true;
    }

}
