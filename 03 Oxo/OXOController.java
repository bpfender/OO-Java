
class OXOController {
    private OXOModel model;

    private int playerIndex = 0;
    private int turn = 0;

    public OXOController(OXOModel model) {
        this.model = model;
        setNextPlayer();
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException {

        if (!isGameOver()) {
            // Error checking on input could obviously be extended with more detailed
            // checking and error messages, such as length of string etc. For the moment,
            // this will do.
            if (!isCommand(command)) {
                throw new InvalidCellIdentifierException("ref. must be letter followed by number:",
                        command);
            }

            // If command is valid (checked above) coordinates can be extracted.
            int rowNum = Character.toLowerCase(command.charAt(0)) - 'a';
            int colNum = Integer.parseInt(command.substring(1)) - 1;

            if (!isCell(rowNum, colNum)) {
                throw new CellDoesNotExistException(rowNum, colNum);
            }
            if (model.getCellOwner(rowNum, colNum) != null) {
                throw new CellAlreadyTakenException(rowNum, colNum);
            }

            turn++;
            model.setCellOwner(rowNum, colNum, model.getCurrentPlayer());

            if (checkForWin(rowNum, colNum)) {
                model.setWinner(model.getCurrentPlayer());
            }

            if (turn >= model.getNumberOfRows() * model.getNumberOfColumns()) {
                model.setGameDrawn();
            }

            setNextPlayer();
        }
    }

    private boolean isGameOver() {
        return model.isGameDrawn() || model.getWinner() != null;
    }

    // Check the structure of the input, ensuring single letter followed by single
    // digit. Accepts both capital and non-capital input.
    private boolean isCommand(String command) {
        // Have stuck with single digit input, assuming limited grid sizes (A-I, 0-9)
        // for purposes of simplicity. In any case, the provided code breaks,
        // displaying ":" etc. instead of "10" at larger sizes.
        return command.matches("^([a-z]|[A-Z])[0-9]+");
    }

    private void setNextPlayer() {
        model.setCurrentPlayer(model.getPlayerByNumber(playerIndex));

        // Update player index
        playerIndex = (playerIndex + 1) % model.getNumberOfPlayers();
    }

    // Checks horizontal, vertical, diagonal and anti-diagonal star around last
    // placed move for a win condition
    private boolean checkForWin(int rowNum, int colNum) {
        return checkWinVector(rowNum, colNum, 1, 0) || checkWinVector(rowNum, colNum, 0, 1)
                || checkWinVector(rowNum, colNum, 1, 1) || checkWinVector(rowNum, colNum, 1, -1);
    }

    // Counts either side around last placed move for the number of connected cells.
    // If the win threshold is reached, true is returned The dir_ variables
    // determine which direction the search goes (horizontal, vertical, diagonal
    // or anti-diagonal).
    private boolean checkWinVector(int rowNum, int colNum, int dir_x, int dir_y) {
        // Current cell counts as 1 in the row
        int count = 1;

        int col = colNum + dir_x;
        int row = rowNum + dir_y;

        while (checkCellOwner(row, col)) {
            col += dir_x;
            row += dir_y;
            count++;
        }

        col = colNum - dir_x;
        row = rowNum - dir_y;

        while (checkCellOwner(row, col)) {
            col -= dir_x;
            row -= dir_y;
            count++;
        }

        return count >= model.getWinThreshold();
    }

    // Checks whether cell is on grid and whether it's owned by the current player
    private boolean checkCellOwner(int row, int col) {
        return isCell(row, col) && model.getCellOwner(row, col) == model.getCurrentPlayer();
    }

    private boolean isCell(int row, int col) {
        return (0 <= row && row < model.getNumberOfRows())
                && (0 <= col && col < model.getNumberOfColumns());
    }
}
