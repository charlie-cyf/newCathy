package UI;

public class Constraints {

    public static boolean ifNameFormattingWrong(String name) {
        name.trim();
        if (!name.contains(" ")) return true;
        for (int i = 0; i < name.length(); i++)
            if (Character.isDigit(name.charAt(i)))
                return true;
        return false;
    }

    public static boolean ifPhoneFormatWrong(String number) {
        if (number.length() > 10) return true;
        for (int i = 0; i < number.length(); i++)
            if (!Character.isDigit(number.charAt(i)))
                return true;
        return false;
    }

    public static boolean ifIDFormatWrong(String id) {
        for (int i = 0; i < id.length(); i++)
            if (!Character.isDigit(id.charAt(i)))
                return true;
        return false;
    }
}
