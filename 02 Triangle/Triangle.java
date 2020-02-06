class Triangle
{
    private int firstLength;
    private int secondLength;
    private int thirdLength;
    private TriangleType type;

    // Class to represent trinagles
    Triangle(int first, int second, int third)
    {
        firstLength = first;
        secondLength = second;
        thirdLength = third;
        type = identifyTriangleType(first, second, third);
    }

    // Returns the (previously indentified) type of this triangle
    TriangleType getType()
    {
        return type;
    }

    // Returns a printable string that describes this triangle
    public String toString()
    {
        return "(" + firstLength + "," + secondLength + "," + thirdLength + ")";
    }

    // Works out what kind of triangle this is !
    static TriangleType identifyTriangleType(int first, int second, int third)
    {
        return null;
    }
}
