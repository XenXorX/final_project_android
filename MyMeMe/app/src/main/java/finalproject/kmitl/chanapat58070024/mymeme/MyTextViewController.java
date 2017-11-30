package finalproject.kmitl.chanapat58070024.mymeme;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import finalproject.kmitl.chanapat58070024.mymeme.fragment.TextEditFragment;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextView;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextViewList;

public class MyTextViewController implements View.OnTouchListener, MyTextView.MyTextChangeListener {
    public static final String TAG_TEXT_EDIT_FRAGMENT = "tag_text_edit_fragment";

    private Context mContext;
    private FragmentManager fragmentManager;
    private ConstraintLayout editImageLayout;
    private ImageButton btnSave;
    private MyTextViewList myTextViewList;

    public MyTextViewController(AppCompatActivity activity,
                                MyTextViewList myTextViewList) {
        mContext = activity;
        fragmentManager = activity.getSupportFragmentManager();
        editImageLayout = activity.findViewById(R.id.editImageLayout);
        btnSave = activity.findViewById(R.id.btn_save);

        this.myTextViewList = myTextViewList;
    }

    public void createText() {
        TextView textView = new TextView(mContext.getApplicationContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(layoutParams);
        textView.setPadding(20, 20, 20, 20);
        textView.setText("Your text");
        textView.setTextSize(36);
        textView.setTextColor(Color.BLACK);
        textView.setBackground(mContext.getDrawable(R.drawable.selected_textview));
        textView.setOnTouchListener(this);

        MyTextView myTextView = new MyTextView(textView);
        myTextView.setListener(this);

        myTextViewList.add(myTextView);
        myTextViewList.removeSelected(textView);
        editImageLayout.addView(textView);
    }

    private void moveText(View view, MotionEvent motionEvent) {
        view.setX(motionEvent.getRawX() - view.getWidth() * 0.5f);
        view.setY(motionEvent.getRawY() - view.getHeight() - editImageLayout.getY());

        if (view.getX() <= 0) {
            view.setX(0);
        }
        if (view.getX() + view.getWidth() >= editImageLayout.getWidth()) {
            view.setX(editImageLayout.getWidth() - view.getWidth());
        }

        if (view.getY() <= 0) {
            view.setY(0);
        }
        if (view.getY() + view.getHeight() >= editImageLayout.getHeight()) {
            view.setY(editImageLayout.getHeight() - view.getHeight());
        }

        btnSave.setVisibility(View.VISIBLE);
    }

    private void addTextProperties(View view) {
        MyTextView myTextView = myTextViewList.findMyTextView((TextView) view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new TextEditFragment().newInstance(myTextView),
                TAG_TEXT_EDIT_FRAGMENT);
        transaction.commit();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveText(view, motionEvent);
                break;
            case MotionEvent.ACTION_DOWN:
                myTextViewList.removeSelected((TextView) view);
                view.setBackground(mContext.getDrawable(R.drawable.selected_textview));
                addTextProperties(view);
                break;
        }

        return true;
    }

    @Override
    public void onMyTextViewChanged(MyTextView myTextView) {
        btnSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMyTextViewRemove(MyTextView myTextView) {
        myTextViewList.remove(myTextView);
    }
}
