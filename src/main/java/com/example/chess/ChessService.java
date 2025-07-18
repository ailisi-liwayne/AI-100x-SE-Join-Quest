package com.example.chess;

import java.util.HashMap;
import java.util.Map;

public class ChessService {

    private Board board;

    public ChessService() {
        this.board = new Board();
    }

    public void addPiece(Piece piece, Position position) {
        board.addPiece(piece, position);
    }

    public boolean validateMove(Piece piece, Position from, Position to) {
        // General can only move within the palace (rows 1-3 and 8-10, columns 4-6)
        if (piece instanceof General) {
            if (to.getRow() < 1 || to.getRow() > 3 && to.getRow() < 8 || to.getRow() > 10) {
                return false;
            }
            if (to.getCol() < 4 || to.getCol() > 6) {
                return false;
            }

            // Check for generals facing each other
            if (isGeneralFacing(from, to, piece.getColor())) {
                return false;
            }
        } else if (piece instanceof Guard) {
            // Guards can only move diagonally one step
            int rowDiff = Math.abs(to.getRow() - from.getRow());
            int colDiff = Math.abs(to.getCol() - from.getCol());

            if (rowDiff != 1 || colDiff != 1) {
                return false;
            }

            // Guards cannot leave the palace
            if (piece.getColor() == Color.RED) {
                if (to.getRow() < 1 || to.getRow() > 3 || to.getCol() < 4 || to.getCol() > 6) {
                    return false;
                }
            } else if (piece.getColor() == Color.BLACK) {
                if (to.getRow() < 8 || to.getRow() > 10 || to.getCol() < 4 || to.getCol() > 6) {
                    return false;
                }
            }
        } else if (piece instanceof Rook) {
            // Rooks move horizontally or vertically
            boolean isHorizontal = from.getRow() == to.getRow();
            boolean isVertical = from.getCol() == to.getCol();

            if (!isHorizontal && !isVertical) {
                return false; // Not a straight move
            }

            // Check for obstacles
            if (isHorizontal) {
                int startCol = Math.min(from.getCol(), to.getCol());
                int endCol = Math.max(from.getCol(), to.getCol());
                for (int col = startCol + 1; col < endCol; col++) {
                    if (board.getPieceAt(new Position(from.getRow(), col)) != null) {
                        return false; // Obstacle in the way
                    }
                }
            } else { // Vertical
                int startRow = Math.min(from.getRow(), to.getRow());
                int endRow = Math.max(from.getRow(), to.getRow());
                for (int row = startRow + 1; row < endRow; row++) {
                    if (board.getPieceAt(new Position(row, from.getCol())) != null) {
                        return false; // Obstacle in the way
                    }
                }
            }
        } else if (piece instanceof Horse) {
            int rowDiff = Math.abs(to.getRow() - from.getRow());
            int colDiff = Math.abs(to.getCol() - from.getCol());

            // Check for L-shape move
            if (!((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1))) {
                return false;
            }

            // Check for leg-block
            Position blockPos = null;
            if (rowDiff == 1) { // Moved 1 row, 2 cols
                blockPos = new Position(from.getRow(), from.getCol() + (to.getCol() > from.getCol() ? 1 : -1));
            } else { // Moved 2 rows, 1 col
                blockPos = new Position(from.getRow() + (to.getRow() > from.getRow() ? 1 : -1), from.getCol());
            }
            if (board.getPieceAt(blockPos) != null) {
                return false; // Leg is blocked
            }
        } else if (piece instanceof Cannon) {
            boolean isHorizontal = from.getRow() == to.getRow();
            boolean isVertical = from.getCol() == to.getCol();

            if (!isHorizontal && !isVertical) {
                return false; // Not a straight move
            }

            int obstacles = 0;
            if (isHorizontal) {
                int startCol = Math.min(from.getCol(), to.getCol());
                int endCol = Math.max(from.getCol(), to.getCol());
                for (int col = startCol + 1; col < endCol; col++) {
                    if (board.getPieceAt(new Position(from.getRow(), col)) != null) {
                        obstacles++;
                    }
                }
            } else { // Vertical
                int startRow = Math.min(from.getRow(), to.getRow());
                int endRow = Math.max(from.getRow(), to.getRow());
                for (int row = startRow + 1; row < endRow; row++) {
                    if (board.getPieceAt(new Position(row, from.getCol())) != null) {
                        obstacles++;
                    }
                }
            }

            Piece targetPiece = board.getPieceAt(to);

            if (targetPiece == null) { // Not capturing
                return obstacles == 0; // Must have no obstacles
            } else { // Capturing
                return obstacles == 1; // Must have exactly one obstacle (cannon platform)
            }
        } else if (piece instanceof Elephant) {
            int rowDiff = Math.abs(to.getRow() - from.getRow());
            int colDiff = Math.abs(to.getCol() - from.getCol());

            // Must move exactly two steps diagonally
            if (!(rowDiff == 2 && colDiff == 2)) {
                return false;
            }

            // Cannot cross the river
            if (piece.getColor() == Color.RED && to.getRow() > 5) {
                return false;
            }
            if (piece.getColor() == Color.BLACK && to.getRow() < 6) {
                return false;
            }

            // Check for blocked midpoint
            int midRow = (from.getRow() + to.getRow()) / 2;
            int midCol = (from.getCol() + to.getCol()) / 2;
            if (board.getPieceAt(new Position(midRow, midCol)) != null) {
                return false; // Midpoint is blocked
            }
        } else if (piece instanceof Soldier) {
            int rowDiff = to.getRow() - from.getRow();
            int colDiff = Math.abs(to.getCol() - from.getCol());

            if (piece.getColor() == Color.RED) {
                // Cannot move backward
                if (rowDiff < 0) {
                    return false;
                }
                // Before crossing river (rows 1-5), can only move forward one step
                if (from.getRow() <= 5) {
                    if (!(rowDiff == 1 && colDiff == 0)) {
                        return false;
                    }
                } else { // After crossing river (rows 6-10), can move forward or sideways one step
                    if (!((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))) {
                        return false;
                    }
                }
            } else { // Black Soldier
                // Cannot move backward
                if (rowDiff > 0) {
                    return false;
                }
                // Before crossing river (rows 6-10), can only move forward one step
                if (from.getRow() >= 6) {
                    if (!(rowDiff == -1 && colDiff == 0)) {
                        return false;
                    }
                } else { // After crossing river (rows 1-5), can move forward or sideways one step
                    if (!((rowDiff == -1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkWinCondition(Piece movingPiece, Position from, Position to) {
        // Simulate the move
        Map<Position, Piece> tempPieces = new HashMap<>(board.getAllPieces());
        Piece targetPiece = tempPieces.get(to);

        if (targetPiece != null && targetPiece instanceof General && targetPiece.getColor() != movingPiece.getColor()) {
            return true; // Captured opponent's General
        }
        return false;
    }

    private boolean isGeneralFacing(Position from, Position to, Color movingPieceColor) {
        // Simulate the move to check the new board state
        Map<Position, Piece> tempPieces = new HashMap<>(board.getAllPieces());
        tempPieces.remove(from);
        tempPieces.put(to, new General(movingPieceColor));

        Position redGeneralPos = null;
        Position blackGeneralPos = null;

        for (Map.Entry<Position, Piece> entry : tempPieces.entrySet()) {
            if (entry.getValue() instanceof General) {
                General general = (General) entry.getValue();
                if (general.getColor() == Color.RED) {
                    redGeneralPos = entry.getKey();
                } else if (general.getColor() == Color.BLACK) {
                    blackGeneralPos = entry.getKey();
                }
            }
        }

        if (redGeneralPos != null && blackGeneralPos != null) {
            if (redGeneralPos.getCol() == blackGeneralPos.getCol()) { // Same column
                int minRow = Math.min(redGeneralPos.getRow(), blackGeneralPos.getRow());
                int maxRow = Math.max(redGeneralPos.getRow(), blackGeneralPos.getRow());

                for (int r = minRow + 1; r < maxRow; r++) {
                    if (tempPieces.containsKey(new Position(r, redGeneralPos.getCol()))) {
                        return false; // There's a piece in between
                    }
                }
                return true; // Generals are facing each other with no pieces in between
            }
        }
        return false;
    }
}