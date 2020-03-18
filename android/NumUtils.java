public class NumUtils{
	//5的倍数，只要位数是0，5就可以
    public static boolean is5PCount(Integer number) {
        char lastChar = String.valueOf(number).charAt(String.valueOf(number).length() - 1);

        if (lastChar == '5' || lastChar == '0') {
            return true;
        }

        return false;
    }
}
