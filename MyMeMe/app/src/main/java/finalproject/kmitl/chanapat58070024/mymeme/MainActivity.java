package finalproject.kmitl.chanapat58070024.mymeme;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import finalproject.kmitl.chanapat58070024.mymeme.fragment.TextEditFragment;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextView;
import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextViewList;

public class MainActivity extends AppCompatActivity implements MyTextView.MyTextChangeListener {
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private final String TAG_TEXT_EDIT_FRAGMENT = "tag_text_edit_fragment";

    private ConstraintLayout editImageLayout;
    private ImageView imageView;
    private int userChoosenTask;
    private FragmentManager fragmentManager;
    private MyTextViewList myTextViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        myTextViewList = new MyTextViewList();

        editImageLayout = findViewById(R.id.editImageLayout);
        editImageLayout.setDrawingCacheEnabled(true);
        editImageLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        removeTextProperties();
                        myTextViewList.removeSelected();
                        break;
                }

                return true;
            }
        });

        imageView = findViewById(R.id.imageView);

        ImageButton btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = REQUEST_CAMERA;
                boolean result = Utility.checkPermission(MainActivity.this);
                if (result) {
                    cameraIntent();
                }
            }
        });

        ImageButton btnGallery = findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = SELECT_FILE;
                boolean result = Utility.checkPermission(MainActivity.this);
                if (result) {
                    galleryIntent();
                }
            }
        });

        ImageButton btnText = findViewById(R.id.btn_text);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crateText();
            }
        });

        ImageButton btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextViewList.removeSelected();
                saveImage();
            }
        });

        ImageButton btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextViewList.removeSelected();
                shareIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask == REQUEST_CAMERA)
                        cameraIntent();
                    else if (userChoosenTask == SELECT_FILE)
                        galleryIntent();
                }
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void shareIntent() {
        editImageLayout.buildDrawingCache();
        Bitmap thumbnail = editImageLayout.getDrawingCache();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                thumbnail,
                String.valueOf(System.currentTimeMillis()),
                null);
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        editImageLayout.destroyDrawingCache();

        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void saveImage() {
        editImageLayout.buildDrawingCache();
        Bitmap thumbnail = editImageLayout.getDrawingCache();

        MediaStore.Images.Media.insertImage(getContentResolver(),
                thumbnail,
                String.valueOf(System.currentTimeMillis()),
                null);

        Toast toast = Toast.makeText(this, "Save Successful!", Toast.LENGTH_LONG);
        toast.show();

        editImageLayout.destroyDrawingCache();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        createTempImage(thumbnail);

        imageView.setImageBitmap(thumbnail);
    }

    private void createTempImage(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
    }

    private void crateText() {
        final TextView textView = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(layoutParams);
        textView.setPadding(20, 20, 20, 20);
        textView.setText("Your text");
        textView.setTextSize(36);
        textView.setTextColor(Color.BLACK);
        textView.setBackground(getDrawable(R.drawable.selected_textview));
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        moveText(view, motionEvent);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        myTextViewList.removeSelected((TextView) view);
                        view.setBackground(getDrawable(R.drawable.selected_textview));
                        addTextProperties(view, motionEvent);
                        break;
                }

                return true;
            }
        });

        MyTextView myTextView = new MyTextView(textView);
        myTextView.setListener(MainActivity.this);
        myTextViewList.addMyTextView(myTextView);
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
    }

    private void addTextProperties(View view, MotionEvent motionEvent) {
        MyTextView myTextView = myTextViewList.findMyTextView((TextView) view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new TextEditFragment().newInstance(myTextView),
                TAG_TEXT_EDIT_FRAGMENT);
        transaction.commit();
    }

    private void removeTextProperties() {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_TEXT_EDIT_FRAGMENT);
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    @Override
    public void onMyTextViewChanged(MyTextView myTextView) {

    }

    @Override
    public void onMyTextViewRemove(MyTextView myTextView) {
        myTextViewList.removeMyTextView(myTextView);
    }
}
