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

        // repurpose all rook moves
        queenMoves.addAll(calcRookMoves(board, myPosition));
        //repurpose all bishop moves
        queenMoves.addAll(calcBishopMoves(board, myPosition));

        return queenMoves;
    }

    private Collection<ChessMove> calcBishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // move northeast
        for (int i = 1; currentRow + i<=8 && currentCol + i <=8; i++) {
            ChessPosition moveNE = new ChessPosition(currentRow + i, currentCol + i);
            ChessPiece atPosPiece = board.getPiece(moveNE);
            if (atPosPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, moveNE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                bishopMoves.add(new ChessMove(myPosition, moveNE, null));
                break;
            } else {
                break;
            }
        }

        // move southeast
        for (int i = 1; currentRow - i>=1 && currentCol + i <=8; i++) {
            ChessPosition moveSE = new ChessPosition(currentRow - i, currentCol + i);
            ChessPiece atPosPiece = board.getPiece(moveSE);
            if (atPosPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, moveSE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                bishopMoves.add(new ChessMove(myPosition, moveSE, null));
                break;
            } else {
                break;
            }
        }

        // move northwest
        for (int i = 1; currentRow + i <=8 && currentCol - i >=1; i++) {
            ChessPosition moveNW = new ChessPosition(currentRow + i, currentCol - i);
            ChessPiece atPosPiece = board.getPiece(moveNW);
            if (atPosPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, moveNW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                bishopMoves.add(new ChessMove(myPosition, moveNW, null));
                break;
            } else {
                break;
            }
        }

        // move southwest
        for (int i = 1; currentRow - i>=1 && currentCol - i >=1; i++) {
            ChessPosition moveSW = new ChessPosition(currentRow - i, currentCol - i);
            ChessPiece atPosPiece = board.getPiece(moveSW);
            if (atPosPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, moveSW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                bishopMoves.add(new ChessMove(myPosition, moveSW, null));
                break;
            } else {
                break;
            }
        }

        return bishopMoves;
    }

    private Collection<ChessMove> calcKnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // move up2right1
        ChessPosition moveUp2Right1 = new ChessPosition(currentRow + 2, currentCol + 1);
        // move up2left1
        ChessPosition moveUp2Left1 = new ChessPosition(currentRow + 2 , currentCol - 1);
        // move down2right1
        ChessPosition moveDown2Right1 = new ChessPosition(currentRow - 2, currentCol + 1);
        // move down2left1
        ChessPosition moveDown2Left1 = new ChessPosition(currentRow - 2, currentCol - 1);
        // move up1right2
        ChessPosition moveUp1Right2 = new ChessPosition(currentRow + 1, currentCol + 2);
        // move up1left2
        ChessPosition moveUp1Left2 = new ChessPosition(currentRow + 1, currentCol - 2);
        // move down1right2
        ChessPosition moveDown1Right2 = new ChessPosition(currentRow - 1, currentCol + 2);
        // move down1left2
        ChessPosition moveDown1Left2 = new ChessPosition(currentRow - 1, currentCol - 2);

        if ((currentRow + 2) <= 8 && (currentCol + 1) <= 8){
            ChessPiece atPosPiece = board.getPiece(moveUp2Right1);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveUp2Right1, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveUp2Right1, null));
            }
        }

        if ((currentRow + 2) <= 8 && (currentCol - 1) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveUp2Left1);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveUp2Left1, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveUp2Left1, null));
            }
        }

        if ((currentRow - 2) >= 1 && (currentCol + 1) <= 8){
            ChessPiece atPosPiece = board.getPiece(moveDown2Right1);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveDown2Right1, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveDown2Right1, null));
            }
        }

        if ((currentRow - 2) >= 1 && (currentCol - 1) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveDown2Left1);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveDown2Left1, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveDown2Left1, null));
            }
        }

        if ((currentRow + 1) <= 8 && (currentCol + 2) <= 8){
            ChessPiece atPosPiece = board.getPiece(moveUp1Right2);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveUp1Right2, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveUp1Right2, null));
            }
        }

        if ((currentRow + 1) <= 8 && (currentCol - 2) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveUp1Left2);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveUp1Left2, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveUp1Left2, null));
            }
        }

        if ((currentRow - 1) >= 1 && (currentCol + 2) <=8){
            ChessPiece atPosPiece = board.getPiece(moveDown1Right2);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveDown1Right2, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveDown1Right2, null));
            }
        }

        if ((currentRow - 1) >= 1 && (currentCol - 2) >= 1){
            ChessPiece atPosPiece = board.getPiece(moveDown1Left2);
            if (atPosPiece == null) {
                knightMoves.add(new ChessMove(myPosition, moveDown1Left2, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                knightMoves.add(new ChessMove(myPosition, moveDown1Left2, null));
            }
        }

        return knightMoves;
    }

    private Collection<ChessMove> calcRookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // move north
        for (int i = currentRow + 1; i<=8; i++) {
            ChessPosition moveN = new ChessPosition(i, currentCol);
            ChessPiece atPosPiece = board.getPiece(moveN);
            if (atPosPiece == null) {
                rookMoves.add(new ChessMove(myPosition, moveN, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                rookMoves.add(new ChessMove(myPosition, moveN, null));
                break;
            } else {
                break;
            }
        }

        // move south
        for (int i = currentRow - 1; i>=1; i--) {
            ChessPosition moveS = new ChessPosition(i, currentCol);
            ChessPiece atPosPiece = board.getPiece(moveS);
            if (atPosPiece == null) {
                rookMoves.add(new ChessMove(myPosition, moveS, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                rookMoves.add(new ChessMove(myPosition, moveS, null));
                break;
            } else {
                break;
            }
        }

        // move east
        for (int i = currentCol + 1; i<=8; i++) {
            ChessPosition moveE = new ChessPosition(currentRow, i);
            ChessPiece atPosPiece = board.getPiece(moveE);
            if (atPosPiece == null) {
                rookMoves.add(new ChessMove(myPosition, moveE, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                rookMoves.add(new ChessMove(myPosition, moveE, null));
                break;
            } else {
                break;
            }
        }

        // move west
        for (int i = currentCol - 1; i>=1; i--) {
            ChessPosition moveW = new ChessPosition(currentRow, i);
            ChessPiece atPosPiece = board.getPiece(moveW);
            if (atPosPiece == null) {
                rookMoves.add(new ChessMove(myPosition, moveW, null));
            } else if (atPosPiece.getTeamColor() != this.getTeamColor()) {
                rookMoves.add(new ChessMove(myPosition, moveW, null));
                break;
            } else {
                break;
            }
        }

        return rookMoves;
    }

    private Collection<ChessMove> calcPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int direction;

        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }

        ChessPosition newRow = new ChessPosition((currentRow + direction), currentCol);
        if (1<= currentRow && currentRow <= 8) {
            if (board.getPiece(newRow) == null) {
                pawnMoves.add(new ChessMove(myPosition, newRow, null));
            }
        }

        boolean atStartPos;
        if (currentRow == 2 && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            atStartPos = true;
        } else if (currentRow == 7 && this.getTeamColor() == ChessGame.TeamColor.BLACK) {
            atStartPos = true;
        } else {
            atStartPos = false;
        }

        ChessPosition move2Pos = new ChessPosition((currentRow + (direction * 2)), currentCol);
        if (atStartPos) {
            if (board.getPiece(move2Pos) == null && board.getPiece(newRow) == null) {
                pawnMoves.add(new ChessMove(myPosition, move2Pos, null));
            }
        }

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
