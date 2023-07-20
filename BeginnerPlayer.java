package MuTorere;

import java.util.ArrayList;
import java.util.Random;

class BeginnerPlayer extends Player {

    private Random rng;

    public BeginnerPlayer(BoardReader boardReader, Board.Piece playerID) {
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
        // print list of valid moves
        if (playerID == Board.Piece.ONE) {
            System.out.println("Player 1's valid moves: " + validMoves);
        } else {
            System.out.println("Player 2's valid moves: " + validMoves);
        }

        // print board state as an array
        System.out.print("[");
        for (int i = 0; i <= 8; i++) {
            if (i <= 7) {
                System.out.print(boardReader.board.boardLocations[i] + ", ");
            } else {
                System.out.print(boardReader.board.boardLocations[i]);
            }
        }
        System.out.println("]");
        
        if (validMoves.size() == 1) {
            return validMoves.get(rng.nextInt(validMoves.size()));
        } else {
            return bestMove(validMoves);
        }
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
    
    public int bestMove(ArrayList<Integer> possibleMoves) {
        Integer moveCheck;
        //Check each possible move on the board,
        //For each move give them a score based on how likely you are
        //to win/lose next turn

        if (boardReader.pieceAt(8) == playerID) {
            System.out.println("Centre piece occupied");
            possibleMoves.remove(new Integer(8));
        } else {
            for (int i = 0; i < possibleMoves.size(); i++) {
                moveCheck = (int) possibleMoves.get(i);

                if (possibleMoves.size() >= 2) {
                    if (moveCheck > 3) {
                        if (boardReader.pieceAt(moveCheck - 4) == playerID) {
                            System.out.println("Opposites");
                            possibleMoves.remove(new Integer(moveCheck));
                            possibleMoves.remove(new Integer(moveCheck - 4));
                        }
                        break;
                    } else {
                        if (boardReader.pieceAt(moveCheck + 4) == playerID) {
                            System.out.println("Opposites");
                            possibleMoves.remove(new Integer(moveCheck));
                            possibleMoves.remove(new Integer(moveCheck + 4));
                            break;
                        }
                    }
                }
            }
        }
        return (int) possibleMoves.get(rng.nextInt(possibleMoves.size()));
    }
}
