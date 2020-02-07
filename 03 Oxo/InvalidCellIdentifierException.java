class InvalidCellIdentifierException extends Exception
{
    String identifierType;
    String identifier;

    public InvalidCellIdentifierException(String type, String ident)
    {
        identifierType = type;
        identifier = ident;
    }

    public InvalidCellIdentifierException(String type, char ident)
    {
        identifierType = type;
        identifier = "" + ident;
    }

    public String toString()
    {
        return this.getClass().getName() + ": Cell " + identifierType + " '" + identifier + "' is invalid";
    }
}
