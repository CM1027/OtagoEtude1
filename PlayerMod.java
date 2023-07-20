package MuTorere;

import java.util.ArrayList;
import java.util.Random;

class PlayerMod extends Player {

    private Random rng;


    /*
  BoardReader provides a method pieceAt(int index) which returns either
  Board.Piece.BLANK, Board.Piece.ONE, or Board.Piece.TWO for the empty space,
  first player's pieces, or second player's pieces. The index is the location 
  from 0 - 8. 0-7 are the kaawai, clockwise around the board, and 8 is the 
  puutahi:
    7   0
  6       1
      8
  5       2
    4   3
     */
    protected BoardReader boardReader;

    /*
  Player ID, either Board.Piece.ONE or Board.Piece.TWO
     */
    protected Board.Piece playerID;

    /*
  Constructor
  
  boardReader provides access to the current state of the game
  playerID determines whether you are player 1 or 2.
  You must provide a constructor with the same signature that calls 
  this to create a concrete Player object.
     */
    public PlayerMod(BoardReader boardReader, Board.Piece playerID) {
        super(boardReader, playerID);
        this.boardReader = boardReader;
        this.rng = new Random();
        this.playerID = playerID;
    }

    /*
  Need to implement this.
  Return the index of the piece that you want to move.
  If the result is not a valid move, you lose.
  If there are no valid moves, just return something - don't leave us hanging!
     */
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
            System.out.println("Centre piece");
            possibleMoves.remove(new Integer(8));
            return (int) possibleMoves.get(rng.nextInt(possibleMoves.size()));
        } else {
            for (int i = 0; i < possibleMoves.size(); i++) {
                moveCheck = (int) possibleMoves.get(i);

                if (possibleMoves.size() >= 3) {
                    if (moveCheck > 3) {
                        if (boardReader.pieceAt(moveCheck - 4) == playerID) {
                            System.out.println("Oppsites");
                            possibleMoves.remove(new Integer(moveCheck));
                            possibleMoves.remove(new Integer(moveCheck - 4));
                        }
                        break;
                    } else {
                        if (boardReader.pieceAt(moveCheck + 4) == playerID) {
                            System.out.println("Oppsites");
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
