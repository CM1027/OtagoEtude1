package MuTorere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
  * SimulatorPlayer.java 
  * 
  * Created by:
  *     Cameron Bradley - 3344991
  *     Castipher McSkimming - 8287490
  *     Jacob Cone - 3977920
  *     Luke Tang - 4258935
  * */ 
class SimulatorPlayer extends Player {

    private Random rng;
    private Board currentBoard = boardReader.board;
    private Board opponentBoard = new Board();
    private Board futureBoard = new Board();
    private Board.Piece opponentID;

    public SimulatorPlayer(BoardReader boardReader, Board.Piece playerID) {
        super(boardReader, playerID);
        rng = new Random();

        if (playerID == Board.Piece.ONE) {
            opponentID = Board.Piece.TWO;
        } else {
            opponentID = Board.Piece.ONE;
        }
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
            // System.out.println("Player 1's valid moves: " + validMoves);
        } else {
            // System.out.println("Player 2's valid moves: " + validMoves);
        }

        // initialize opponent board 
        opponentBoard = copyBoard(currentBoard);

        // store original opponent board for easy retrieval later
        Board originalOpponentBoard = copyBoard(opponentBoard);

        // for each of our valid moves, try to anticipate the opponent's moves 
        for (int a = 0; a < validMoves.size(); a++) {
            int immediateMove = validMoves.get(a); 

            // make a move
            opponentBoard.makeMove(immediateMove, playerID);

            // print state of the board after making the move
            // System.out.println("\t-> " + immediateMove);
            // System.out.println(printBoard(opponentBoard, 1));

            // find and print all valid moves for opponent
            ArrayList<Integer> opponentValidMoves = new ArrayList<>();
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {
                    if (isValid(opponentBoard, i, j, opponentID)) {
                        opponentValidMoves.add(i);
                        continue;
                    }
                }
            }
            // System.out.println("\tOpponent's valid moves: " + opponentValidMoves);

            // check if we've found a winning move
            if (opponentValidMoves.size() <= 0) {
                // System.out.println("\tWinning move found");
                return immediateMove;
            }

            // check if the opponent's only move is to move from the center
            if (opponentValidMoves.size() == 1 && opponentValidMoves.get(0) == 8) {
                // System.out.println("\tGive up centre position");
                return immediateMove;
            }

            if (opponentValidMoves.size() > 0) {
                // initialize future board
                futureBoard = copyBoard(opponentBoard);

                // store original future board
                Board originalFutureBoard = copyBoard(futureBoard);

                // try to also anticipate what our moves will be after each potential opponent move
                for (int b = 0; b < opponentValidMoves.size(); b++) {
                    int opponentMove = opponentValidMoves.get(b);

                    // make a move
                    futureBoard.makeMove(opponentMove, opponentID);

                    // print out the state of the board after making the move
                    // System.out.println("\t\t-> " + opponentMove);
                    // System.out.println(printBoard(futureBoard, 2));

                    // find and print all valid moves for this future board
                    ArrayList<Integer> futureMoves = new ArrayList<Integer>();
                    for (int i = 0; i < 9; ++i) {
                        for (int j = 0; j < 9; ++j) {
                            if (isValid(futureBoard, i, j, playerID)) {
                                futureMoves.add(i);
                                continue;
                            }
                        }
                    }
                    // System.out.println("\t\tMy future moves: " + futureMoves);

                    // check if we've found a losing move
                    if (futureMoves.size() <= 0) {
                        // System.out.println("\t\tI probably shouldn't move from " + immediateMove + "...");
                        if (validMoves.size() > 1) {
                            // System.out.println("\t\tRemoving " + immediateMove + " from my valid moves...");
                            validMoves.remove((Integer) immediateMove);
                        } else {
                            // System.out.println("\t\tggwp");
                        }
                        // System.out.println("\t\tValid moves: " + validMoves);
                    }

                    futureBoard = copyBoard(originalFutureBoard);
                }                
            }

            // revert the opponent's board
            opponentBoard = copyBoard(originalOpponentBoard);
        }
        
        if (validMoves.size() > 1) {
            return bestMove(validMoves);
        }
        return validMoves.get(rng.nextInt(validMoves.size()));
    }

    public int bestMove(ArrayList<Integer> possibleMoves) {
        Integer moveCheck;
        // Check each possible move on the board,
        // For each move give them a score based on how likely you are
        // to win/lose next turn

        if (boardReader.pieceAt(8) == playerID) {
            // System.out.println("Centre piece occupied");
            possibleMoves.remove((Integer) 8);
            return (int) possibleMoves.get(rng.nextInt(possibleMoves.size()));
        } else {
            for (int i = 0; i < possibleMoves.size(); i++) {
                moveCheck = (int) possibleMoves.get(i);

                if (possibleMoves.size() >= 3) {
                    if (moveCheck > 3) {
                        if (boardReader.pieceAt(moveCheck - 4) == playerID) {
                            // System.out.println("Opposites");
                            possibleMoves.remove(moveCheck);
                            moveCheck -= 4;
                            possibleMoves.remove(moveCheck);
                        }
                        break;
                    } else {
                        if (boardReader.pieceAt(moveCheck + 4) == playerID) {
                            // System.out.println("Opposites");
                            possibleMoves.remove(moveCheck);
                            moveCheck += 4;
                            possibleMoves.remove(moveCheck);
                            break;
                        }
                    }
                }
            }
        }
        return (int) possibleMoves.get(rng.nextInt(possibleMoves.size()));
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

    boolean isValid(Board board, int moveFrom, int moveTo, Board.Piece id) {

        if (board.pieceAt(moveTo) != Board.Piece.BLANK) {
            return false;
        }

        if (board.pieceAt(moveFrom) != id) {
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
            if (board.pieceAt(prev) == id && board.pieceAt(next) == id) {
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
            if (board.pieceAt(prev) != Board.Piece.BLANK
                    && board.pieceAt(next) != Board.Piece.BLANK) {
                return false;
            }
        }
        return true;
    }

    int getSymbolFromPiece(Board.Piece piece) {
        if (piece == Board.Piece.ONE) {
            return 1;
        } else {
            return 2;
        }
    }

    Board copyBoard(Board board) {
        Board boardCopy = new Board();
        boardCopy.boardLocations = Arrays.copyOf(board.boardLocations, board.boardLocations.length);
        boardCopy.blankLocation = board.blankLocation;

        return boardCopy;
    }

    String printBoard(Board board, int numberOfTabs) {
        String row1 = "  " + MuTorere.getSymbol(board, 7) + "   " + MuTorere.getSymbol(board, 0);
        String row2 = "" + MuTorere.getSymbol(board, 6) + "       " + MuTorere.getSymbol(board, 1);
        String row3 = "    " + MuTorere.getSymbol(board, 8);
        String row4 = "" + MuTorere.getSymbol(board, 5) + "       " + MuTorere.getSymbol(board, 2);
        String row5 = "  " + MuTorere.getSymbol(board, 4) + "   " + MuTorere.getSymbol(board, 3);

        String output;
        if (numberOfTabs == 1) {
            output = "\t" + row1 + "\n" + "\t" + row2 + "\n" + "\t" + row3 + "\n" + "\t" + row4 + "\n" + "\t" + row5;
        } else if (numberOfTabs == 2) {
            output = "\t\t" + row1 + "\n" + "\t\t" + row2 + "\n" + "\t\t" + row3 + "\n" + "\t\t" + row4 + "\n" + "\t\t" + row5;
        } else {
            output = row1 + "\n" + row2 + "\n" + row3 + "\n" + row4 + "\n" + row5;
        }

        return output;
    }
}
