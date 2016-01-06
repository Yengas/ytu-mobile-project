package phonebookpp.ytu.com.phonebookpp.utils;

import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by DARK on 1/2/2016.
 */
public class PPCommon {

    public static String sanitizeNumber(String number){
        return number.substring(1);
    }


    public static String[] sanitizeName(String fullname){
        String[] split = fullname.trim().split(" ");
        if(split.length == 1) return split;
        return new String[]{TextUtils.join(" ", Arrays.copyOfRange(split, 0, split.length - 1)), split[split.length - 1]};
    }
}
