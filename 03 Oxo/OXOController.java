// QUESTION one class per file?

class OXOController {
    private OXOModel model;

    private OXOPlayer currPlayer;
    private int playerIndex = 0;

    private int rounds = 0;
    private int turn = 0;

    public OXOController(OXOModel model) {
        this.model = model; // QUESTION, is this correct? this just becomes a reference?
        setNextPlayer();
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException {

        // QUESTION how does exception handling work in view?
        if (!isCommand(command)) {
            // QUESTION what do these refer to?
            throw new InvalidCellIdentifierException(command, command);
        }

        // QUESTION Does this make more sense as class vars? how do i determine what is local and
        // what isn;t?
        int rowNum = Character.toLowerCase(command.charAt(0)) - 'a';
        int colNum = Character.getNumericValue(command.charAt(1)) - 1;

        if (!isCell(rowNum, colNum)) {
            throw new CellDoesNotExistException(rowNum, colNum);
        }

        if (model.getCellOwner(rowNum, colNum) != null) {
            throw new CellAlreadyTakenException(rowNum, colNum);
        }

        model.setCellOwner(rowNum, colNum, model.getCurrentPlayer());

        if (++turn >= model.getNumberOfRows() * model.getNumberOfColumns()) {
            model.setGameDrawn();
        } else if (rounds >= model.getWinThreshold() - 1) {
            if (checkForWin(rowNum, colNum)) {
                model.setWinner(model.getCurrentPlayer());
            }
        }

        setNextPlayer();
    }

    // Check the structure of the input, ensuring single letter followed by single digit. Could be
    // expanded for huge grids, but unclear what preferred
    private boolean isCommand(String command) {
        if (command.matches("^(([a-z]|[A-Z])[0-9])")) {
            return true;
        }
        return false;
    }

    private void setNextPlayer() {
        currPlayer = model.getPlayerByNumber(playerIndex);
        model.setCurrentPlayer(currPlayer);

        // Update player index
        playerIndex = (playerIndex + 1) % model.getNumberOfPlayers();
        if (playerIndex == 0) {
            rounds++;
        }
    }

    private boolean checkForWin(int rowNum, int colNum) {
        if (checkHorizontal(rowNum, colNum) || checkVertical(rowNum, colNum)
                || checkUpDiagonal(rowNum, colNum) || checkDownDiagonal(rowNum, colNum)) {
            return true;
        }
        return false;
    }

    private boolean checkHorizontal(int rowNum, int colNum) {
        int count = 1;

        while (checkCellOwner(rowNum, ++colNum)) {
            count++;
        }
        colNum -= count;
        while (checkCellOwner(rowNum, --colNum)) {
            count++;
        }

        return isWinThreshold(count);
    }

    private boolean checkVertical(int rowNum, int colNum) {
        int count = 1;

        while (checkCellOwner(++rowNum, colNum)) {
            count++;
        }
        rowNum -= count;
        while (checkCellOwner(--rowNum, colNum)) {
            count++;
        }

        return isWinThreshold(count);
    }

    private boolean checkDownDiagonal(int rowNum, int colNum) {
        int count = 1;

        while (checkCellOwner(++rowNum, ++colNum)) {
            count++;
        }
        rowNum -= count;
        colNum -= count;

        while (checkCellOwner(--rowNum, --colNum)) {
            count++;
        }

        return isWinThreshold(count);
    }

    private boolean checkUpDiagonal(int rowNum, int colNum) {
        int count = 1;

        while (checkCellOwner(++rowNum, --colNum)) {
            count++;
        }
        rowNum -= count;
        colNum += count;

        while (checkCellOwner(--rowNum, ++colNum)) {
            count++;
        }

        return isWinThreshold(count);
    }

    // Checks whether cell is on grid and whether it's owned by the current player
    private boolean checkCellOwner(int row, int col) {
        if (isCell(row, col) && model.getCellOwner(row, col) == currPlayer) {
            return true;
        }
        return false;
    }

    private boolean isCell(int row, int col) {
        // QUESTION as below, does it makes more sense to store row/col as attribute? more so here
        return 0 <= row && row < model.getNumberOfRows() && 0 <= col
                && col < model.getNumberOfColumns();
    }

    // Checks whether threshold for player win has been reached
    private boolean isWinThreshold(int count) {
        // QUESTION model.getWinThreshold() will be called many times. Does it make more sense to
        // store as an attribute?
        return count >= model.getWinThreshold();
    }

}
