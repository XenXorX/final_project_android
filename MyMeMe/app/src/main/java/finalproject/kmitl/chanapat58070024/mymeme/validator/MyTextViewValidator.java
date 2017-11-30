package finalproject.kmitl.chanapat58070024.mymeme.validator;

import android.graphics.Color;

public class MyTextViewValidator {
    public static final String DEFAULT_TEXT = "Your Text";
    public static final String MIN_TEXT_SIZE = "1";
    public static final String MAX_TEXT_SIZE = "200";

    public String changeToDefaultText(String text) {

        if ("".equals(text)) {
            return DEFAULT_TEXT;
        }
        return text;
    }

    public String checkSizeLimit(String tsize) {

        if (!"".equals(tsize)) {
            int size = Integer.parseInt(tsize);

            if (size < Integer.parseInt(MIN_TEXT_SIZE)) {
                return MIN_TEXT_SIZE;
            } else if (size > Integer.parseInt(MAX_TEXT_SIZE)) {
                return MAX_TEXT_SIZE;
            }
        }

        return tsize;
    }

    public String checkEmptySize(String tsize, String previousSize) {

        if ("".equals(tsize)) {
            return String.valueOf(previousSize);
        }

        return tsize;
    }

    public String checkValidBGColor(String tcolor, String stringColor) {
        try {
            Color.parseColor(tcolor);

            return tcolor;
        } catch (Exception e) {
            return stringColor;
        }
    }
}
