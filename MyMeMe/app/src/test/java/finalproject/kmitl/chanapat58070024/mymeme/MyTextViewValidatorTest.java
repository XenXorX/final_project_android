package finalproject.kmitl.chanapat58070024.mymeme;

import org.junit.BeforeClass;
import org.junit.Test;

import finalproject.kmitl.chanapat58070024.mymeme.validator.MyTextViewValidator;

import static org.junit.Assert.*;

public class MyTextViewValidatorTest {
    static MyTextViewValidator validator;

    @BeforeClass
    public static void init() {
        validator = new MyTextViewValidator();
    }

    @Test
    public void changeToDefaultText() {
        String result = validator.changeToDefaultText("");
        assertEquals(validator.DEFAULT_TEXT, result);
    }

    @Test
    public void notChangeToDefaultText() {
        String text = "a";
        String result = validator.changeToDefaultText(text);
        assertEquals(text, result);
    }

    @Test
    public void checkEmptySizeLimit() {
        String text = "";
        String result = validator.checkSizeLimit(text);
        assertEquals(text, result);
    }

    @Test
    public void checkNormalSize() {
        String text = "50";
        String result = validator.checkSizeLimit(text);
        assertEquals(text, result);
    }

    @Test
    public void checkOverSize() {
        String result = validator.checkSizeLimit("201");
        assertEquals(validator.MAX_TEXT_SIZE, result);
    }

    @Test
    public void checkUnderSize() {
        String result = validator.checkSizeLimit("0");
        assertEquals(validator.MIN_TEXT_SIZE, result);
    }

    @Test
    public void checkEmptySize() {
        String previousSize = "100";
        String result = validator.checkEmptySize("", previousSize);
        assertEquals(previousSize, result);
    }

    @Test
    public void checkNotEmptySize() {
        String previousSize = "100";
        String size = "50";
        String result = validator.checkEmptySize(size, previousSize);
        assertEquals(size, result);
    }

    @Test
    public void checkValidBGColor() {
        String stringColor = "#ffffff";
        String tcolor = "#000024";
        String result = validator.checkValidBGColor(stringColor, tcolor);
        assertEquals(tcolor, result);
    }

    @Test
    public void checkInvalidBGColor() {
        String stringColor = "#ffffff";
        String tcolor = "#abcdefg";
        String result = validator.checkValidBGColor(tcolor, stringColor);
        assertEquals(stringColor, result);
    }

    @Test
    public void checkBGColorWithoutSharp() {
        String stringColor = "#ffffff";
        String tcolor = "024024";
        String result = validator.checkValidBGColor(tcolor, stringColor);
        assertEquals(stringColor, result);
    }
}
