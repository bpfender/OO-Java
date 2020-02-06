class TriangleTester
{
    public static void main(String[] args)
    {
        // Tricky bit of code to check that assertions have been enabled !
        boolean assertionsEnabled = false;
        assert(assertionsEnabled = true);
        if (assertionsEnabled) {
            testEquilateral();
            testIsosceles();
            testScalene();
            testFlat();
            testRight();
            testImpossible();
            testZero();
            testNegative();
            testOverflow();
            System.out.println("SUCCESS: All tests passed !!!");
        }
        else {
            System.out.println("You MUST run java with assertions enabled (-ea) to test your program !");
        }
    }

    // Equilateral: all equal
    private static void testEquilateral() {
        checkTriangleType(new Triangle(8, 8, 8), TriangleType.Equilateral);
    }

    // Isosceles: any two equal
    private static void testIsosceles() {
        checkTriangleType(new Triangle(5, 5, 3), TriangleType.Isosceles);
        checkTriangleType(new Triangle(5, 3, 5), TriangleType.Isosceles);
        checkTriangleType(new Triangle(3, 5, 5), TriangleType.Isosceles);
        checkTriangleType(new Triangle(5, 5, 7), TriangleType.Isosceles);
        checkTriangleType(new Triangle(5, 7, 5), TriangleType.Isosceles);
        checkTriangleType(new Triangle(7, 5, 5), TriangleType.Isosceles);
    }

    // Scalene: all three different (but not special)
    private static void testScalene() {
        checkTriangleType(new Triangle(12, 14, 15), TriangleType.Scalene);
        checkTriangleType(new Triangle(14, 12, 15), TriangleType.Scalene);
        checkTriangleType(new Triangle(12, 15, 14), TriangleType.Scalene);
        checkTriangleType(new Triangle(14, 15, 12), TriangleType.Scalene);
        checkTriangleType(new Triangle(15, 12, 14), TriangleType.Scalene);
        checkTriangleType(new Triangle(15, 14, 12), TriangleType.Scalene);
    }

    // Right-angled: Pythagoras's theorem
    private static void testRight() {
        checkTriangleType(new Triangle(5, 12, 13), TriangleType.Right);
        checkTriangleType(new Triangle(12, 5, 13), TriangleType.Right);
        checkTriangleType(new Triangle(5, 13, 12), TriangleType.Right);
        checkTriangleType(new Triangle(12, 13, 5), TriangleType.Right);
        checkTriangleType(new Triangle(13, 5, 12), TriangleType.Right);
        checkTriangleType(new Triangle(13, 12, 5), TriangleType.Right);
    }

    // Flat: two sides add up to the third
    private static void testFlat() {
        checkTriangleType(new Triangle(7, 7, 14), TriangleType.Flat);
        checkTriangleType(new Triangle(7, 14, 7), TriangleType.Flat);
        checkTriangleType(new Triangle(14, 7, 7), TriangleType.Flat);
        checkTriangleType(new Triangle(7, 9, 16), TriangleType.Flat);
        checkTriangleType(new Triangle(7, 16, 9), TriangleType.Flat);
        checkTriangleType(new Triangle(9, 16, 7), TriangleType.Flat);
        checkTriangleType(new Triangle(16, 7, 9), TriangleType.Flat);
    }

    // Impossible: two sides add up to less than the third
    private static void testImpossible() {
        checkTriangleType(new Triangle(2, 3, 13), TriangleType.Impossible);
        checkTriangleType(new Triangle(2, 13, 3), TriangleType.Impossible);
        checkTriangleType(new Triangle(13, 2, 3), TriangleType.Impossible);
    }

    // Illegal: a side is zero
    private static void testZero() {
        checkTriangleType(new Triangle(0, 0, 0), TriangleType.Illegal);
        checkTriangleType(new Triangle(0, 10, 12), TriangleType.Illegal);
        checkTriangleType(new Triangle(10, 0, 12), TriangleType.Illegal);
        checkTriangleType(new Triangle(10, 12, 0), TriangleType.Illegal);
    }

    // Illegal: a side is negative
    private static void testNegative() {
        checkTriangleType(new Triangle(-1, -1, -1), TriangleType.Illegal);
        checkTriangleType(new Triangle(-1, 10, 12), TriangleType.Illegal);
        checkTriangleType(new Triangle(10, -1, 12), TriangleType.Illegal);
        checkTriangleType(new Triangle(10, 12, -1), TriangleType.Illegal);
    }

    // Overflow: check that the program doesn't have overflow problems due to
    // using int, float or double. If there are overflow problems, the program will not say Scalene.
    private static void testOverflow() {
        checkTriangleType(new Triangle(1100000000, 1705032704, 1805032704), TriangleType.Scalene);
        checkTriangleType(new Triangle(2000000001, 2000000002, 2000000003), TriangleType.Scalene);
        checkTriangleType(new Triangle(150000002, 666666671, 683333338), TriangleType.Scalene);
    }

    private static void checkTriangleType(Triangle triangle, TriangleType expectedType)
    {
        assert(triangle.getType() == expectedType): "\nERROR: failed to classify " + triangle.toString() + " as " + expectedType;
    }
}
