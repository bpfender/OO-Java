// QUESTION one class per file?

class OXOController {
    OXOModel model;
    int playerIndex = 0;
    int rounds = 0;

    public OXOController(OXOModel model) {
        this.model = model; // QUESTION, is this correct? this just becomes a reference?
        setNextPlayer();
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException {

        if (!isCommand(command)) {
            // QUESTION what do these refer to?
            throw new InvalidCellIdentifierException(command, command);
        }

        // QUESTION Does this make more sense as class vars?
        int rowNum = Character.toLowerCase(command.charAt(0)) - 'a';
        int colNum = Character.getNumericValue(command.charAt(1));

        if (!isCellValid(rowNum, colNum)) {
            throw new CellDoesNotExistException(rowNum, colNum);
        }

        OXOPlayer owner = model.getCellOwner(rowNum, colNum);

        if (owner != null) {
            throw new CellAlreadyTakenException(rowNum, colNum);
        } else {
            model.setCellOwner(rowNum, colNum, model.getCurrentPlayer());
        }

        if (rounds >= model.getWinThreshold()) {
            if (checkForWin(rowNum, colNum)) {
                model.setWinner(model.getCurrentPlayer());
            }
        }

    }

    private boolean isCellValid(int rowNum, int colNum) {
        // QUESTION would it make more snese to have these as method vars?
        // FIXME should I check for zero bounds?
        if (rowNum <= model.getNumberOfRows() && colNum <= model.getNumberOfColumns()) {
            return true;
        }
        return false;
    }

    // Check the structure of the input, ensuring single letter followed by
    // single digit
    private boolean isCommand(String command) {
        if (command.matches("^(([a-z]|[A-Z])[0-9])")) {
            return true;
        }
        return false;
    }

    private void setNextPlayer() {
        OXOPlayer player = model.getPlayerByNumber(playerIndex);
        model.setCurrentPlayer(player);

        playerIndex = (playerIndex + 1) % model.getNumberOfPlayers();
        if (playerIndex == 0) {
            rounds++;
        }
    }

    private boolean checkForWin(int rowNum, int colNum) {
        if (checkHorizontal(rowNum, colNum) || checkVertical(rowNum, colNum)) {
            return true;
        }

        return false;
    }

    private boolean checkHorizontal(int rowNum, int colNum) {
        int count = 1;
        int col = colNum;

        while (checkCell(rowNum, ++col)) {
            count++;
        }
        col = colNum;

        while (checkCell(rowNum, --col)) {
            count++;
        }

        return isWin(count);
    }

    private boolean checkVertical(int rowNum, int colNum) {
        int count = 1;
        int row = rowNum;

        while (checkCell(++row, colNum)) {
            count++;
        }
        row = rowNum;
        while (checkCell(--row, colNum)) {
            count++;
        }

        return isWin(count);
    }

    private boolean checkDiagonals(int rowNum, int colNum) {
        int count = 1;
        int col = colNum;
        int row = rowNum;

        while (checkCell(++row, ++col)) {
            count++;
        }
        col = colNum;
        row = rowNum;

        while (checkCell(--row, --col)) {
            count++;
        }

        if (isWin(count)) {
            return true;
        }

        col = colNum;
        row = rowNum;
        count = 1;

        while (checkCell(++row, --col)) {
            count++;
        }
        col = colNum;
        row = rowNum;

        while (checkCell(--row, ++col)) {
            count++;
        }

        return isWin(count);
    }

    private boolean isWin(int count) {
        return count >= model.getWinThreshold();
    }

    private boolean checkCell(int row, int col) {
        OXOPlayer player = model.getCurrentPlayer();

        if (0 <= row && row < model.getNumberOfRows() && col <= 0
                && col < model.getNumberOfColumns() && model.getCellOwner(row, col) == player) {
            return true;
        }
        return false;
    }



}
