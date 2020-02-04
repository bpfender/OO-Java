class GradeConverterTester
{
  static void testDigitConvertion(GradeConverter converter)
  {
    assert(converter.convertCharToInt('/') == -1): "\nERROR: failed to catch / as an invalid numerical digit";
    assert(converter.convertCharToInt('0') == 0): "\nERROR: failed to convert 0 into a numerical digit";
    assert(converter.convertCharToInt('1') == 1): "\nERROR: failed to convert 1 into a numerical digit";
    assert(converter.convertCharToInt('5') == 5): "\nERROR: failed to convert 5 into a numerical digit";
    assert(converter.convertCharToInt('9') == 9): "\nERROR: failed to convert 9 into a numerical digit";
    assert(converter.convertCharToInt(':') == -1): "\nERROR: failed to catch : as an invalid numerical digit";
  }

  static void testMarkToGradeConversion(GradeConverter converter)
  {
    assert(converter.convertMarkToGrade(45).equals("Fail")): "\nERROR: failed to convert 45 into a grade";
    assert(converter.convertMarkToGrade(55).equals("Pass")): "\nERROR: failed to convert 55 into a grade";
    assert(converter.convertMarkToGrade(65).equals("Merit")): "\nERROR: failed to convert 65 into a grade";
    assert(converter.convertMarkToGrade(75).equals("Distinction")): "\nERROR: failed to convert 75 into a grade";
  }

  static void testGradeBoundaries(GradeConverter converter)
  {
    assert(converter.convertMarkToGrade(0).equals("Fail")): "\nERROR: failed to convert 0 into a grade";
    assert(converter.convertMarkToGrade(49).equals("Fail")): "\nERROR: failed to convert 49 into a grade";
    assert(converter.convertMarkToGrade(50).equals("Pass")): "\nERROR: failed to convert 50 into a grade";
    assert(converter.convertMarkToGrade(59).equals("Pass")): "\nERROR: failed to convert 59 into a grade";
    assert(converter.convertMarkToGrade(60).equals("Merit")): "\nERROR: failed to convert 60 into a grade";
    assert(converter.convertMarkToGrade(69).equals("Merit")): "\nERROR: failed to convert 69 into a grade";
    assert(converter.convertMarkToGrade(70).equals("Distinction")): "\nERROR: failed to convert 70 into a grade";
    assert(converter.convertMarkToGrade(100).equals("Distinction")): "\nERROR: failed to convert 100 into a grade";
  }

  static void testInvalidGradeMarks(GradeConverter converter)
  {
    assert(converter.convertMarkToGrade(-1).equals("Invalid")): "\nERROR: failed to catch -1 as an invalid mark";
    assert(converter.convertMarkToGrade(101).equals("Invalid")): "\nERROR: failed to catch 101 as an invalid mark";
  }

  static void testStringToMarkConversion(GradeConverter converter)
  {
    assert(converter.convertStringToMark("0") == 0): "\nERROR: failed to convert \"0\" into an int";
    assert(converter.convertStringToMark("53") == 53): "\nERROR: failed to convert \"53\" into an int";
    assert(converter.convertStringToMark("100") == 100): "\nERROR: failed to convert \"100\" into an int";
  }

  static void testInvalidMarkStrings(GradeConverter converter)
  {
    assert(converter.convertStringToMark("") == -1): "\nERROR: failed to catch \"\" as an invalid mark";
    assert(converter.convertStringToMark("x") == -1): "\nERROR: failed to catch x as an invalid mark";
    assert(converter.convertStringToMark("5x") == -1): "\nERROR: failed to catch 5x as an invalid mark";
    assert(converter.convertStringToMark("5x6") == -1): "\nERROR: failed to catch 5x6 as an invalid mark";
    assert(converter.convertStringToMark("-1") == -1): "\nERROR: failed to catch -1 as an invalid mark";
    assert(converter.convertStringToMark("101") == -1): "\nERROR: failed to catch 101 as an invalid mark";
    assert(converter.convertStringToMark("01") == -1): "\nERROR: failed to catch 01 as an invalid mark";
    assert(converter.convertStringToMark("099") == -1): "\nERROR: failed to catch 099 as an invalid mark";
  }

  static void testRobustnessStrings(GradeConverter converter)
  {
      assert(converter.convertStringToMark("55%") == 55): "\nERROR: failed to cope with % character";
      assert(converter.convertStringToMark("77.1") == 77): "\nERROR: failed to cope with float marks";
      assert(converter.convertStringToMark("24.9") == 25): "\nERROR: failed to correctly round float marks";
      assert(converter.convertStringToMark("52.5") == 53): "\nERROR: failed to correctly round float marks";
  }

  public static void main(String[] args)
  {
    // Tricky bit of code to check that assertions have been enabled !
    boolean assertionsEnabled = false;
    assert(assertionsEnabled = true);
    if (assertionsEnabled) {
      GradeConverter converter = new GradeConverter();
      testDigitConvertion(converter);
      testMarkToGradeConversion(converter);
      testGradeBoundaries(converter);
      testInvalidGradeMarks(converter);
      testStringToMarkConversion(converter);
      testInvalidMarkStrings(converter);
      testRobustnessStrings(converter);
      System.out.println("SUCCESS: All tests passed !!!");
    }
    else {
      System.out.println("You MUST run java with assertions enabled (-ea) to test your program !");
    }
  }
}
