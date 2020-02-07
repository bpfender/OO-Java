import java.util.Arrays;

class Triangle {
    private int firstLength;
    private int secondLength;
    private int thirdLength;
    private TriangleType type;

    // Class to represent trinagles
    Triangle(int first, int second, int third) {
        firstLength = first;
        secondLength = second;
        thirdLength = third;

        int[] lengths = { first, second, third };
        Arrays.sort(lengths);

        type = identifyTriangleType(lengths[0], lengths[1], lengths[2]);
    }

    // Returns the (previously indentified) type of this triangle
    TriangleType getType() {
        return type;
    }

    // Returns a printable string that describes this triangle
    public String toString() {
        return "(" + firstLength + "," + secondLength + "," + thirdLength + ")";
    }

    // Works out what kind of triangle this is !
    static TriangleType identifyTriangleType(int first, int second, int third) {
        if (!checkValid(first, second, third)) {
            return TriangleType.Illegal;
        }
        if (!checkPossible(first, second, third)) {
            return TriangleType.Impossible;
        }
        if (checkEqual(first, second, third)) {
            return TriangleType.Equilateral;
        }
        if (checkFlat(first, second, third)) {
            return TriangleType.Flat;
        }
        if (checkIso(first, second, third)) {
            return TriangleType.Isosceles;
        }
        if (checkRA(first, second, third)) {
            return TriangleType.Right;
        }
        if (checkScalene(first, second, third)) {
            return TriangleType.Scalene;
        }
        return null;
    }

    static boolean checkValid(int first, int second, int third) {
        return (first > 0 && second > 0 && third > 0);
    }

    static boolean checkPossible(int first, int second, int third) {
        return (((long) first + second) >= third);
    }

    static boolean checkEqual(int first, int second, int third) {
        return (first == second && first == third);
    }

    static boolean checkFlat(int first, int second, int third) {
        return (((long) first + second) == third);
    }

    static boolean checkIso(int first, int second, int third) {
        return (first == second || second == third);
    }

    static boolean checkRA(int first, int second, int third) {
        long first_sq = first * first;
        long second_sq = second * second;
        long third_sq = third * third;

        return (first_sq + second_sq == third_sq);
    }

    static boolean checkScalene(int first, int second, int third) {
        return (first != second && second != third && first != third);
    }

}
