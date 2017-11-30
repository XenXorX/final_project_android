package finalproject.kmitl.chanapat58070024.mymeme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class MyTextView implements Parcelable {
    private TextView textView;

    protected MyTextView(Parcel in) {
    }

    public static final Creator<MyTextView> CREATOR = new Creator<MyTextView>() {
        @Override
        public MyTextView createFromParcel(Parcel in) {
            return new MyTextView(in);
        }

        @Override
        public MyTextView[] newArray(int size) {
            return new MyTextView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public MyTextView(TextView textView) {
        this.textView = textView;
    }

    public TextView getTextView() {
        return textView;
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(String text) {
        textView.setText(text);
        listener.onMyTextViewChanged(this);
    }

    public int getSize() {
        return (int) textView.getTextSize();
    }

    public void setSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        listener.onMyTextViewChanged(this);
    }

    public int getColor() {
        return textView.getCurrentTextColor();
    }

    public void setColor(int color) {
        textView.setTextColor(color);
        listener.onMyTextViewChanged(this);
    }

    public void remove() {
        textView.setVisibility(View.GONE);
        listener.onMyTextViewRemove(this);
    }

    private MyTextChangeListener listener;

    public interface MyTextChangeListener {
        void onMyTextViewChanged(MyTextView myTextView);

        void onMyTextViewRemove(MyTextView myTextView);
    }

    public void setListener(MyTextChangeListener listener) {
        this.listener = listener;
    }
}
