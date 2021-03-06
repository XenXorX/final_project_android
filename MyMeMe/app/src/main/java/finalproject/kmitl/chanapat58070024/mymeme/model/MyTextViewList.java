package finalproject.kmitl.chanapat58070024.mymeme.model;

import android.graphics.Color;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTextViewList {
    ArrayList<MyTextView> myTextViews;

    public MyTextViewList() {
        myTextViews = new ArrayList<>();
    }

    public void add(MyTextView myTextView) {
        myTextViews.add(myTextView);
    }

    public void remove(MyTextView myTextView) {
        myTextViews.remove(myTextView);
    }

    public void clear() {
        for (MyTextView myTextView : myTextViews) {
            myTextView.remove();
        }

        myTextViews.clear();
    }

    public MyTextView findMyTextView(TextView textView) {
        for (MyTextView myTextView : myTextViews) {
            if (textView.equals(myTextView.getTextView())) {
                return myTextView;
            }
        }

        return null;
    }

    public void removeSelected() {
        for (MyTextView myTextView : myTextViews) {
            myTextView.getTextView().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void removeSelected(TextView textView) {
        for (MyTextView myTextView : myTextViews) {
            if (!textView.equals(myTextView.getTextView())) {
                myTextView.getTextView().setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
