package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class BoardRenderer {
    private ChessBoard board;
    private ChessGame.TeamColor teamColor;

    public BoardRenderer(ChessBoard board, ChessGame.TeamColor teamColor) {
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

        for (int row = startRow; row != endRow; row+= shift) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + row + " ");

            for (int col = startCol; col != endCol; col += shiftCol) {
                String bgColor = (row + col) % 2 == 0 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_DARK_GREEN;
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String chessman = piece == null ? EMPTY : getChessman(piece);
                System.out.print(bgColor + chessman);
            }

            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + row + " " + RESET_BG_COLOR);
            System.out.println();
        }
        printColHeaders(startCol, endCol, shiftCol);
    }

    private String getChessman(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }

    private void printColHeaders(int startCol, int endCol, int shiftCol) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "   ");

        for (int col = startCol; col != endCol; col += shiftCol) {
            char letter = (char) ('a' + col - 1);
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + letter + " ");
        }
        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }
}
