package com.example.chess;

enum Color {
    RED,
    BLACK
}

public class Piece {
    protected Color color;

    public Color getColor() {
        return color;
    }
}