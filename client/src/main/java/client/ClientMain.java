package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String serverUrl = "http://localhost:8080";

        if (args.length == 1) {
            serverUrl = args[0];
        }

        try {
            new Repl(serverUrl).run();
        } catch (Throwable e) {
            System.out.printf("Unable to start the server: %s%n", e.getMessage());
        }
    }

}
