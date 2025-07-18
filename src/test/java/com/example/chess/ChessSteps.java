package com.example.chess;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChessSteps {

    private ChessService chessService;
    private boolean isMoveLegal;
    private boolean redWinsImmediately;
    private boolean gameContinues;

    @Given("^the board is empty except for a Red General at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_general_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new General(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Guard at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_guard_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Guard(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Rook at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_rook_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Rook(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Horse at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_horse_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Horse(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Cannon at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_cannon_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Cannon(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Elephant at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_elephant_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Elephant(Color.RED), new Position(row, col));
    }

    @Given("^the board is empty except for a Red Soldier at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_red_soldier_at(Integer row, Integer col) {
        chessService = new ChessService();
        chessService.addPiece(new Soldier(Color.RED), new Position(row, col));
    }

    @Given("^the board has:$")
    public void the_board_has(DataTable dataTable) {
        chessService = new ChessService();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String pieceType = columns.get("Piece");
            String positionStr = columns.get("Position");
            // Parse position string (e.g., "(2, 4)") to row and col
            positionStr = positionStr.replaceAll("[()]", "");
            String[] parts = positionStr.split(", ");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if ("Red General".equals(pieceType)) {
                chessService.addPiece(new General(Color.RED), new Position(row, col));
            } else if ("Black General".equals(pieceType)) {
                chessService.addPiece(new General(Color.BLACK), new Position(row, col));
            } else if ("Red Rook".equals(pieceType)) {
                chessService.addPiece(new Rook(Color.RED), new Position(row, col));
            } else if ("Black Soldier".equals(pieceType)) {
                chessService.addPiece(new Soldier(Color.BLACK), new Position(row, col));
            } else if ("Black Rook".equals(pieceType)) {
                chessService.addPiece(new Rook(Color.BLACK), new Position(row, col));
            } else if ("Black Cannon".equals(pieceType)) {
                chessService.addPiece(new Cannon(Color.BLACK), new Position(row, col));
            } else if ("Red Soldier".equals(pieceType)) {
                chessService.addPiece(new Soldier(Color.RED), new Position(row, col));
            } else if ("Black Guard".equals(pieceType)) {
                chessService.addPiece(new Guard(Color.BLACK), new Position(row, col));
            }
        }
    }

    @When("^Red moves the General from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_general_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new General(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @When("^Red moves the Guard from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_guard_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Guard(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @When("^Red moves the Rook from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_rook_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Rook(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
        redWinsImmediately = chessService.checkWinCondition(new Rook(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
        gameContinues = !redWinsImmediately;
    }

    @When("^Red moves the Horse from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_horse_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Horse(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @When("^Red moves the Cannon from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_cannon_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Cannon(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @When("^Red moves the Elephant from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_elephant_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Elephant(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @When("^Red moves the Soldier from \\((\\d+), (\\d+)\\)\\ to \\((\\d+), (\\d+)\\)$")
    public void red_moves_the_soldier_from_to(Integer fromRow, Integer fromCol, Integer toRow, Integer toCol) {
        isMoveLegal = chessService.validateMove(new Soldier(Color.RED), new Position(fromRow, fromCol), new Position(toRow, toCol));
    }

    @Then("^the move is legal$")
    public void the_move_is_legal() {
        assertTrue(isMoveLegal);
    }

    @Then("^the move is illegal$")
    public void the_move_is_illegal() {
        assertFalse(isMoveLegal);
    }

    @Then("^Red wins immediately$")
    public void red_wins_immediately() {
        assertTrue(redWinsImmediately);
    }

    @Then("^the game is not over just from that capture$")
    public void the_game_is_not_over_just_from_that_capture() {
        assertTrue(gameContinues);
    }
}
