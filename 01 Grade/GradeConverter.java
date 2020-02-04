import java.io.*;

// Class to convert unit marks to grades
class GradeConverter {
    // Converts a numerical mark (0 to 100) into a textual grade
    // Returns "Invalid" if the number is invalid
    String convertMarkToGrade(int mark) {
        if (inclusiveRange(0, 49, mark)) {
            return "Fail";
        } else if (inclusiveRange(50, 59, mark)) {
            return "Pass";
        } else if (inclusiveRange(60, 69, mark)) {
            return "Merit";
        } else if (inclusiveRange(70, 100, mark)) {
            return "Distinction";
        } else {
            return "Invalid";
        }
    }

    // Reads a mark from a String and returns the mark as an int (0 to 100)
    // Returns -1 if the string is invalid
    int convertStringToMark(String text) {
        int mark = 0;
        int len = text.length();

        // Decided to practice some REGEX
        if (!text.matches("^([0]|(100)|([1-9][0-9]))([.][0-9]+)?([ ]?%)?")) {
            return -1;
        }

        for (int i = 0; i < len; i++) {
            if (text.charAt(i) == ' ' || text.charAt(i) == '%') {
                return mark;
            }

            if (text.charAt(i) == '.') {
                if (convertCharToInt(text.charAt(i + 1)) >= 5) {
                    mark += 1;
                }
                return mark;
            }

            mark = mark * 10 + convertCharToInt(text.charAt(i));
        }
        return mark;

    }

    // Convert a single character to an int (0 to 9)
    // Returns -1 if char is not numerical
    int convertCharToInt(char c) {
        if (Character.isDigit(c)) {
            return c - '0';
        } else {
            return -1;
        }
    }

    boolean inclusiveRange(int lower, int upper, int val) {
        return val >= lower && val <= upper;
    }

    public static void main(String[] args) throws IOException {
        GradeConverter converter = new GradeConverter();
        while (true) {
            System.out.print("Please enter your mark: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            int mark = converter.convertStringToMark(input);
            String grade = converter.convertMarkToGrade(mark);
            System.out.println("A mark of " + input + " is " + grade);
        }
    }
}
