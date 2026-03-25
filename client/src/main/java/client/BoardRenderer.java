package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static client.EscapeSequences.*;

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

        for (int row = startRow; row != endRow; row+= shift) {
            int startCol = teamColor == ChessGame.TeamColor.WHITE ? 1 : 8;
            int endCol = teamColor == ChessGame.TeamColor.WHITE ? 9 : 0;
            int shiftCol = teamColor == ChessGame.TeamColor.WHITE ? 1 : -1;
            for (int col = startCol; col != endCol; col += shiftCol) {
                String bgColor = (row + col) % 2 == 0 ? BG_LIGHT : BG_DARK;
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String chessman = piece == null ? EMPTY : getChessman(piece);
                System.out.print(bgColor + chessman);
            }
            System.out.print(RESET);
            System.out.println();
        }
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
}
