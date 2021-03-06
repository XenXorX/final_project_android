package finalproject.kmitl.chanapat58070024.mymeme.fragment;

import android.content.DialogInterface;
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

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import finalproject.kmitl.chanapat58070024.mymeme.R;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextView;
import finalproject.kmitl.chanapat58070024.mymeme.validator.MyTextViewValidator;

public class TextEditFragment extends Fragment {
    private static final String MY_TEXT_VIEW = "my_text_view";

    private MyTextView myTextView;
    private int previousSize;
    private String stringColor;
    private EditText etText;
    private EditText etSize;
    private EditText etColor;
    private Button btnColor;
    private ImageButton btnRemove;
    private MyTextViewValidator validator;

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
            validator = new MyTextViewValidator();
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
                    String text = etText.getText().toString();

                    String result = validator.changeToDefaultText(text);
                    etText.setText(result);
                    myTextView.setText(result);
                }
            }
        });

        etSize = rootView.findViewById(R.id.et_size);
        etSize.setText(String.valueOf(myTextView.getSize()));
        etSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    previousSize = Integer.parseInt(etSize.getText().toString());
                } else {
                    String stringSize = etSize.getText().toString();

                    String result = validator.checkEmptySize(stringSize, String.valueOf(previousSize));
                    if(result.equals(stringSize)) {
                        result = validator.checkSizeLimit(stringSize);
                    }

                    etSize.setText(result);
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
                    String result = validator.checkValidBGColor(etColor.getText().toString(), stringColor);
                    btnColor.setBackgroundColor(Color.parseColor(result));
                    myTextView.setColor(Color.parseColor(result));
                    etColor.setText(result);

                }
            }
        });

        btnColor = rootView.findViewById(R.id.btn_color);
        btnColor.setBackgroundColor(myTextView.getColor());
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(myTextView.getColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .lightnessSliderOnly()
                        .density(12)
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        btnRemove = rootView.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTextView();
            }
        });

        return rootView;
    }

    private void changeBackgroundColor(int selectedColor){
        btnColor.setBackgroundColor(selectedColor);
        etColor.setText(String.valueOf(selectedColor));
        myTextView.setColor(selectedColor);
    }

    private void removeTextView() {
        myTextView.remove();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(TextEditFragment.this);
        transaction.commit();
    }
}
