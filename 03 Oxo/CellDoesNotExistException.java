class CellDoesNotExistException extends Exception
{
    int rowNumber;
    int colNumber;

    public CellDoesNotExistException(int rowNum, int colNum)
    {
        rowNumber = rowNum;
        colNumber = colNum;
    }

    public String toString()
    {
        return this.getClass().getName() + ": Cell [" + rowNumber + "," + colNumber + "] does not exist";
    }
}
