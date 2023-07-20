package MuTorere;

class MuTorere {

    static char getSymbol(Board board, int index) {
        if (board.pieceAt(index) == Board.Piece.ONE) {
            return '1';
        } else if (board.pieceAt(index) == Board.Piece.TWO) {
            return '2';
        } else {
            return '_';
        }
    }

    static void printBoard(Board board) {
        String row1 = "  " + getSymbol(board, 7) + "   " + getSymbol(board, 0);
        String row2 = "" + getSymbol(board, 6) + "       " + getSymbol(board, 1);
        String row3 = "    " + getSymbol(board, 8);
        String row4 = "" + getSymbol(board, 5) + "       " + getSymbol(board, 2);
        String row5 = "  " + getSymbol(board, 4) + "   " + getSymbol(board, 3);
        System.out.println(row1);
        System.out.println(row2);
        System.out.println(row3);
        System.out.println(row4);
        System.out.println(row5);
    }

    Board.Piece runGame(Player playerOne, Player playerTwo) {
        return Board.Piece.BLANK;
    }

    Board board;
    BoardReader boardReader;
    Player playerOne, playerTwo;

    public MuTorere() {
        board = new Board();
        boardReader = new BoardReader(board);
    }

    public boolean setup(String playerOneClassName, String playerTwoClassName) {
        Class<?> playerOneClass, playerTwoClass;
        try {
            playerOneClass = Class.forName(playerOneClassName);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + playerOneClassName);
            return false;
        }
        try {
            playerTwoClass = Class.forName(playerTwoClassName);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + playerTwoClassName);
            return false;
        }

        try {
            playerOne = (Player) playerOneClass.getDeclaredConstructor(BoardReader.class, Board.Piece.class).newInstance(boardReader, Board.Piece.ONE);
        } catch (NoSuchMethodException ex) {
            System.out.println("Constructor " + playerOneClassName + "(BoardReader) not found");
            return false;
        } catch (Exception ex) {
            System.out.println("Could not make a new instance of " + playerOneClassName);
            return false;
        }

        try {
            playerTwo = (Player) playerTwoClass.getDeclaredConstructor(BoardReader.class, Board.Piece.class).newInstance(boardReader, Board.Piece.TWO);
        } catch (NoSuchMethodException ex) {
            System.out.println("Constructor " + playerTwoClassName + "(BoardReader) not found");
            return false;
        } catch (Exception ex) {
            System.out.println("Could not make a new instance of " + playerTwoClassName);
            return false;
        }

        return true;
    }

    public Board.Piece play() {
        for (int turn = 0; turn < 10000; ++turn) {
            System.out.println("=== Turn " + (turn + 1) + " ===");
            printBoard(board);
            int move = -1;
            if (turn % 2 == 0) {
                // Player 1
                move = playerOne.getMove();
                if (!board.makeMove(move, Board.Piece.ONE)) {
                    return Board.Piece.TWO;
                }
                System.out.println("Player 1 moves from " + move);
            } else {
                // Player 2
                move = playerTwo.getMove();
                if (!board.makeMove(move, Board.Piece.TWO)) {
                    return Board.Piece.ONE;
                }
                System.out.println("Player 2 moves from " + move);
            }
        }

        return Board.Piece.BLANK;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java MuTorere <player 1 class> <player 2 class>");
            return;
        }
        String playerOneClassName = "MuTorere." + args[0];
        String playerTwoClassName = "MuTorere." + args[1];

        MuTorere game = new MuTorere();

        if (game.setup(playerOneClassName, playerTwoClassName)) {
            Board.Piece result = game.play();
            if (result == Board.Piece.ONE) {
                System.out.println("Player 1 (" + playerOneClassName + ") wins!");
            } else if (result == Board.Piece.TWO) {
                System.out.println("Player 2 (" + playerTwoClassName + ") wins!");
            } else {
                System.out.println("Draw!");
            }
        } else {
            System.out.println("Game setup failed");
        }
        System.out.println("That was fun!");
    }

};
