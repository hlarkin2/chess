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
        return switch (type) {
            case KING -> calcKingMoves(board, myPosition);
            case QUEEN -> calcQueenMoves(board, myPosition);
            case BISHOP -> calcBishopMoves(board, myPosition);
            case KNIGHT -> calcKnightMoves(board, myPosition);
            case ROOK -> calcRookMoves(board, myPosition);
            case PAWN -> calcPawnMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> calcKingMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {
                {1,0}, {-1,0}, {0,1}, {0,-1},
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };
        return calcJumpMoves(board, pos, dirs);
    }

    private Collection<ChessMove> calcKnightMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {
                {2,1}, {2,-1}, {-2,1}, {-2,-1},
                {1,2}, {1,-2}, {-1,2}, {-1,-2}
        };
        return calcJumpMoves(board, pos, dirs);
    }

    private Collection<ChessMove> calcJumpMoves(ChessBoard board, ChessPosition pos, int[][] dirs) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow();
        int col = pos.getColumn();

        for (int[] d : dirs) {
            int newRow = row + d[0];
            int newCol = col + d[1];
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece piece = board.getPiece(newPos);

                if(piece == null || piece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(pos, newPos, null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> calcQueenMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(calcBishopMoves(board, pos));
        moves.addAll(calcRookMoves(board, pos));
        return moves;
    }

    private Collection<ChessMove> calcBishopMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        return calcSlidingMoves(board, pos, dirs);
    }

    private Collection<ChessMove> calcRookMoves(ChessBoard board, ChessPosition pos) {
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        return calcSlidingMoves(board, pos, dirs);
    }

    private Collection<ChessMove> calcSlidingMoves(ChessBoard board, ChessPosition pos, int[][] dirs) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow();
        int col = pos.getColumn();

        for (int[] d : dirs) {
            for (int i = 1; i <=8; i++) {
                int newRow = row + (d[0] * i);
                int newCol = col + (d[1] * i);
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {break;}

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece piece = board.getPiece(newPos);

                if (piece == null) {
                    moves.add(new ChessMove(pos, newPos, null));
                } else {
                    if (piece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(pos, newPos, null));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> calcPawnMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow();
        int col = pos.getColumn();
        int dir = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promoRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 8 : 1;

        ChessPosition forward = new ChessPosition(row + dir, col);

        if (board.getPiece(forward) == null) {
            boolean isPromo = (forward.getRow() == promoRow);
            addPawnMove(moves, pos, forward, isPromo);

            if (row == startRow) {
                ChessPosition forward2 = new ChessPosition(row + (dir * 2), col);
                if (board.getPiece(forward2) == null) {
                    moves.add(new ChessMove(pos, forward2, null));
                }
            }
        }

        int[] captureCols = {col - 1, col + 1};
        for (int captureCol : captureCols) {
            if (captureCol >= 1 && captureCol <= 8) {
                ChessPosition capturePos = new ChessPosition(row + dir, captureCol);
                ChessPiece piece = board.getPiece(capturePos);

                if (piece != null && piece.getTeamColor() != pieceColor) {
                    boolean isPromo = (capturePos.getRow() == promoRow);
                    addPawnMove(moves, pos, capturePos, isPromo);
                }
            }
        }

        return moves;
    }

    private void addPawnMove(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, boolean isPromotion) {
        if (isPromotion) {
            moves.add(new ChessMove(from, to, PieceType.QUEEN));
            moves.add(new ChessMove(from, to, PieceType.BISHOP));
            moves.add(new ChessMove(from, to, PieceType.KNIGHT));
            moves.add(new ChessMove(from, to, PieceType.ROOK));
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
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
