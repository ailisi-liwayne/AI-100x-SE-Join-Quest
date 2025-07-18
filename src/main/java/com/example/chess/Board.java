package com.example.chess;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Position, Piece> pieces;

    public Board() {
        pieces = new HashMap<>();
    }

    public void addPiece(Piece piece, Position position) {
        pieces.put(position, piece);
    }

    public Piece getPieceAt(Position position) {
        return pieces.get(position);
    }

    public Map<Position, Piece> getAllPieces() {
        return pieces;
    }
}
