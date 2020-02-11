class OXOController {
    // QUESTION does it make sense to store these so they don't always have to be
    // called?
    OXOModel model;

    public OXOController(OXOModel model) {
        this.model = model; // QUESTION, is this correct? this just becomes a reference?
    }

    public void handleIncomingCommand(String command)
            throws InvalidCellIdentifierException, CellAlreadyTakenException, CellDoesNotExistException {

        if (!isCommand(command)) {
            throw new InvalidCellIdentifierException(command, command); // QUESTION what do these refer to?
        }

        // QUESTION Does this make more sense as class vars?
        int rowNum = Character.toLowerCase(command.charAt(0)) - 'a';
        int colNum = Character.getNumericValue(command.charAt(1));

        if (!isCellValid(rowNum, colNum)) {
            throw new CellDoesNotExistException(rowNum, colNum);
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
}