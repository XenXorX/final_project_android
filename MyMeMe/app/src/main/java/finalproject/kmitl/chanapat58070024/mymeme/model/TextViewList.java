package finalproject.kmitl.chanapat58070024.mymeme.model;

import android.widget.TextView;

import java.util.ArrayList;

public class TextViewList {
    private ArrayList<TextView> textList;

    public TextViewList() {
        textList = new ArrayList<>();
    }

    public void addTextView(TextView textView) {
        textList.add(textView);
    }

    public void removeTextView(TextView textView) {
        textList.remove(textView);
    }
}
