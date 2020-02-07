class CellAlreadyTakenException extends Exception
{
    int rowNumber;
    int colNumber;

    public CellAlreadyTakenException(int rowNum, int colNum)
    {
        rowNumber = rowNum;
        colNumber = colNum;
    }

    public String toString()
    {
        return this.getClass().getName() + ": Cell [" + rowNumber + "," + colNumber + "] has already been claimed";
    }
}
