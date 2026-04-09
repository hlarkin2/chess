package client;

import chess.*;
import java.util.Collection;
import static ui.EscapeSequences.*;

public class BoardRenderer {
    private ChessBoard board;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> highlightMoves;

    public BoardRenderer(ChessBoard board, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.teamColor = teamColor;
    }

    public BoardRenderer(Collection<ChessMove> highlightMoves, ChessBoard board, ChessGame.TeamColor teamColor) {
        this.highlightMoves = highlightMoves;
        this.board = board;
        this.teamColor = teamColor;
    }

    public void render() {
        int startRow = teamColor == ChessGame.TeamColor.WHITE ? 8 : 1;
        int endRow = teamColor == ChessGame.TeamColor.WHITE ? 0 : 9;
        int shift = teamColor == ChessGame.TeamColor.WHITE ? -1 : 1;

        int startCol = teamColor == ChessGame.TeamColor.WHITE ? 1 : 8;
        int endCol = teamColor == ChessGame.TeamColor.WHITE ? 9 : 0;
        int shiftCol = teamColor == ChessGame.TeamColor.WHITE ? 1 : -1;

        printColHeaders(startCol, endCol, shiftCol);

        for (int row = startRow; row != endRow; row += shift) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + RESET_TEXT_COLOR + " " + row + " ");

            for (int col = startCol; col != endCol; col += shiftCol) {
                String bgColor = getSquareColor(row, col);
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String chessman = piece == null ? EMPTY : getChessman(piece);
                System.out.print(bgColor + chessman + " ");
            }

            System.out.print(SET_BG_COLOR_LIGHT_GREY + RESET_TEXT_COLOR + " " + row + " " + RESET_BG_COLOR);
            System.out.println();
        }
        printColHeaders(startCol, endCol, shiftCol);
    }

    private String getSquareColor(int row, int col) {
        if (isStartPosition(row, col)) {
            return SET_BG_COLOR_BLUE;
        }
        if (isHighlighted(row, col)) {
            return SET_BG_COLOR_MAGENTA;
        }
        return (row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_WHITE;
    }

    private boolean isStartPosition(int row, int col) {
        if (highlightMoves == null || highlightMoves.isEmpty()) {
            return false;
        }
        ChessPosition startPos = highlightMoves.iterator().next().getStartPosition();
        return startPos.getRow() == row && startPos.getColumn() == col;
    }

    private boolean isHighlighted(int row, int col) {
        if (highlightMoves == null) {
            return false;
        }
        for (ChessMove move : highlightMoves) {
            if (move.getEndPosition().getRow() == row && move.getEndPosition().getColumn() == col) {
                return true;
            }
        }
        return false;
    }

    private String getChessman(ChessPiece piece) {
        String color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
        return switch (piece.getPieceType()) {
            case KING -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING) + RESET_TEXT_COLOR;
            case QUEEN -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN) + RESET_TEXT_COLOR;
            case BISHOP -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP) + RESET_TEXT_COLOR;
            case KNIGHT -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT) + RESET_TEXT_COLOR;
            case ROOK -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK) + RESET_TEXT_COLOR;
            case PAWN -> color + (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN) + RESET_TEXT_COLOR;
        };
    }

    private void printColHeaders(int startCol, int endCol, int shiftCol) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + RESET_TEXT_COLOR + "   ");

        for (int col = startCol; col != endCol; col += shiftCol) {
            char letter = (char) ('a' + col - 1);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + "  " + letter + "  ");
        }
        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }
}