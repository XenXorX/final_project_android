package finalproject.kmitl.chanapat58070024.mymeme.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import finalproject.kmitl.chanapat58070024.mymeme.R;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextView;

public class TextEditFragment extends Fragment {
    private static final String MY_TEXT_VIEW = "my_text_view";

    private final String DEFAULT_TEXT = "Your Text";
    private final int MAX_TEXT_SIZE = 200;
    private final int MIN_TEXT_SIZE = 1;

    private MyTextView myTextView;
    private int previousSize;
    private String stringColor;
    private EditText etText;
    private EditText etSize;
    private EditText etColor;
    private Button btnColor;
    private ImageButton btnRemove;

    public TextEditFragment() {
    }

    public static TextEditFragment newInstance(MyTextView myTextView) {
        TextEditFragment fragment = new TextEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(MY_TEXT_VIEW, myTextView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myTextView = getArguments().getParcelable(MY_TEXT_VIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text_edit, container, false);

        etText = rootView.findViewById(R.id.et_text);
        etText.setText(myTextView.getText());
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                myTextView.setText(editable.toString());
            }
        });
        etText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    changeToDefaultText();
                }
            }
        });

        etSize = rootView.findViewById(R.id.et_size);
        etSize.setText(String.valueOf(myTextView.getSize()));
        etSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSizeLimit();
            }
        });
        etSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    previousSize = Integer.parseInt(etSize.getText().toString());
                } else {
                    checkEmptySize();
                    myTextView.setSize(Integer.parseInt(etSize.getText().toString()));
                }
            }
        });

        etColor = rootView.findViewById(R.id.et_color);
        etColor.setText(String.format("#%06X", (0xFFFFFF & myTextView.getColor())));
        etColor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    stringColor = etColor.getText().toString();
                } else {
                    checkValidBGColor();
                }
            }
        });

        btnColor = rootView.findViewById(R.id.btn_color);
        btnColor.setBackgroundColor(myTextView.getColor());

        btnRemove = rootView.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTextView();
            }
        });

        return rootView;
    }

    private void changeToDefaultText() {
        String text = etText.getText().toString();

        if ("".equals(text)) {
            etText.setText(DEFAULT_TEXT);
            myTextView.setText(DEFAULT_TEXT);
        }
    }

    private void checkSizeLimit() {
        String stringSize = etSize.getText().toString();

        if (!"".equals(stringSize)) {
            int size = Integer.parseInt(stringSize);

            if (size < MIN_TEXT_SIZE) {
                etSize.setText(String.valueOf(MIN_TEXT_SIZE));
            } else if (size > MAX_TEXT_SIZE) {
                etSize.setText(String.valueOf(MAX_TEXT_SIZE));
            }
        }
    }

    private void checkEmptySize() {
        String stringSize = etSize.getText().toString();

        if ("".equals(stringSize)) {
            etSize.setText(String.valueOf(previousSize));
        }
    }

    private void checkValidBGColor() {
        try {
            int color = Color.parseColor(etColor.getText().toString());

            btnColor.setBackgroundColor(color);
            myTextView.setColor(color);
        } catch (Exception e) {
            btnColor.setBackgroundColor(Color.parseColor(stringColor));
            etColor.setText(stringColor);
        }
    }

    private void removeTextView() {
        myTextView.remove();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(TextEditFragment.this);
        transaction.commit();
    }
}
