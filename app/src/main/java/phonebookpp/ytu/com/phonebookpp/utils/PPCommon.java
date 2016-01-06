package phonebookpp.ytu.com.phonebookpp.utils;

public class PPCommon {
    public static String sanitizeNumber(String number){
        number.replaceAll("\\s",""); // Erase whitespaces
        if(number.charAt(0) == '9') return "+" + number; // 90536
        if(number.charAt(0) == '0') return "+9" + number; // 0536
        if(!number.contains("+90")) return "+90" + number; // 536
        return number; // +90536
    }
}
