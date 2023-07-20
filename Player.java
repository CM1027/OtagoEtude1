package MuTorere;

import MuTorere.Player;

abstract class Player {

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
    public Player(BoardReader boardReader, Board.Piece playerID) {
        this.boardReader = boardReader;
        this.playerID = playerID;
    }

    /*
  Need to implement this.
  Return the index of the piece that you want to move.
  If the result is not a valid move, you lose.
  If there are no valid moves, just return something - don't leave us hanging!
     */
    public abstract int getMove();

}
