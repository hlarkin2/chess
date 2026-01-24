package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        if (this.type == PieceType.KING) {
            return calcKingMoves(board, myPosition);
        } else if (this.type == PieceType.QUEEN) {
            return calcQueenMoves(board, myPosition);
        } else if (this.type == PieceType.BISHOP) {
            return calcBishopMoves(board, myPosition);
        } else if (this.type == PieceType.KNIGHT) {
            return calcKnightMoves(board, myPosition);
        } else if (this.type == PieceType.ROOK) {
            return calcRookMoves(board, myPosition);
        } else if (this.type == PieceType.PAWN) {
            return calcPawnMoves(board, myPosition);
        }

        return moves;
    }

    private Collection<ChessMove> calcKingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // move north
        ChessPosition moveN = new ChessPosition(currentRow + 1, currentCol);
        // move south
        ChessPosition moveS = new ChessPosition(currentRow - 1, currentCol);
        // move east
        ChessPosition moveE = new ChessPosition(currentRow, currentCol + 1);
        // move west
        ChessPosition moveW = new ChessPosition(currentRow, currentCol - 1);
        // move northeast
        ChessPosition moveNE = new ChessPosition(currentRow + 1, currentCol + 1);
        // move northwest
        ChessPosition moveNW = new ChessPosition(currentRow + 1, currentCol - 1);
        // move southeast
        ChessPosition moveSE = new ChessPosition(currentRow - 1, currentCol + 1);
        // move southwest
        ChessPosition moveSW = new ChessPosition(currentRow - 1, currentCol - 1);

        if ((currentRow + 1) <= 8 && (1 <= currentCol && currentCol <= 8)){
            ChessPiece atPosPiece = board.getPiece(moveN);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveN, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveN, null));
            }
        }

        if ((currentRow - 1) >= 1 && (1 <= currentCol && currentCol <= 8)){
            ChessPiece atPosPiece = board.getPiece(moveS);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveS, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveS, null));
            }
        }

        if ((1 <= currentRow && currentRow <= 8) && (currentCol + 1) <=8){
            ChessPiece atPosPiece = board.getPiece(moveE);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveE, null));
            }
        }

        if ((1 <= currentRow && currentRow <= 8) && (currentCol - 1) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveW);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveW, null));
            }
        }

        if ((currentRow + 1) <= 8 && (currentCol + 1) <= 8){
            ChessPiece atPosPiece = board.getPiece(moveNE);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveNE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveNE, null));
            }
        }

        if ((currentRow + 1) <= 8 && (currentCol - 1) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveNW);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveNW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveNW, null));
            }
        }

        if ((currentRow - 1) >= 1 && (currentCol + 1) <=8){
            ChessPiece atPosPiece = board.getPiece(moveSE);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveSE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveSE, null));
            }
        }

        if ((currentRow - 1) >= 1 && (currentCol - 1) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveSW);
            if (atPosPiece == null) {
                kingMoves.add(new ChessMove(myPosition, moveSW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, moveSW, null));
            }
        }

        return kingMoves;
    }

    private Collection<ChessMove> calcQueenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new ArrayList<>();

        return queenMoves;
    }

    private Collection<ChessMove> calcBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();

        return bishopMoves;
    }

    private Collection<ChessMove> calcKnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new ArrayList<>();

        return knightMoves;
    }

    private Collection<ChessMove> calcRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new ArrayList<>();

        return rookMoves;
    }

    private Collection<ChessMove> calcPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        return pawnMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
