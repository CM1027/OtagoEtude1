package MuTorere;

import java.util.ArrayList;
import java.util.Random;

class NaivePlayer extends Player {

    private Random rng;

    public NaivePlayer(BoardReader boardReader, Board.Piece playerID) {
        super(boardReader, playerID);
        rng = new Random();
    }

    public int getMove() {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (isValid(i, j)) {
                    validMoves.add(i);
                    continue;
                }
            }
        }
        if (validMoves.isEmpty()) {
            return 0;
        }
        return validMoves.get(rng.nextInt(validMoves.size()));
    }

    boolean isValid(int moveFrom, int moveTo) {

        if (boardReader.pieceAt(moveTo) != Board.Piece.BLANK) {
            return false;
        }

        if (boardReader.pieceAt(moveFrom) != playerID) {
            return false;
        }
        if (moveTo == 8) {
            // Move to centre, check for valid neighbour
            int prev = moveFrom - 1;
            if (prev < 0) {
                prev = 7;
            }
            int next = moveFrom + 1;
            if (next > 7) {
                next = 0;
            }
            if (boardReader.pieceAt(prev) == playerID && boardReader.pieceAt(next) == playerID) {
                return false;
            }
        } else {
            // Either move from centre to kewai...
            if (moveFrom == 8) {
                return true;
            }
            // ... or from one kewai to next, make sure they are neighbours
            int prev = moveFrom - 1;
            if (prev < 0) {
                prev = 7;
            }
            int next = moveFrom + 1;
            if (next > 7) {
                next = 0;
            }
            if (boardReader.pieceAt(prev) != Board.Piece.BLANK
                    && boardReader.pieceAt(next) != Board.Piece.BLANK) {
                return false;
            }
        }
        return true;
    }

}
