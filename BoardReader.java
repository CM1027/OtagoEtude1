package MuTorere;

public class BoardReader {

    Board board;

    public BoardReader(Board board) {
        this.board = board;
    }

    public Board.Piece pieceAt(int index) {
        return board.pieceAt(index);
    }
}
