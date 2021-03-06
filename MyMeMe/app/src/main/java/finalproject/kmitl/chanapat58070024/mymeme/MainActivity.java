package finalproject.kmitl.chanapat58070024.mymeme;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Toast;

import java.io.IOException;

import finalproject.kmitl.chanapat58070024.mymeme.model.MyTextViewList;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout editImageLayout;
    private ImageView imageView;
    private int userChoosenTask;
    private FragmentManager fragmentManager;
    private MyTextViewList myTextViewList;
    private MyTextViewController myTextViewController;
    private MyCamera myCamera;
    private MyGallery myGallery;
    private MySave mySave;
    private MyShare myShare;
    private ImageButton btnRotate;
    private ImageButton btnSave;
    private ImageButton btnShare;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        myTextViewList = new MyTextViewList();
        myTextViewController = new MyTextViewController(this, myTextViewList);
        myCamera = new MyCamera(this);
        myGallery = new MyGallery();
        mySave = new MySave(this);
        myShare = new MyShare(this);

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
                userChoosenTask = myCamera.REQUEST_CAMERA;
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
                userChoosenTask = myGallery.SELECT_FILE;
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
                createText();
                showAllBtn();
            }
        });

        btnRotate = findViewById(R.id.btn_rotate);
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setRotation(imageView.getRotation() + 90);
            }
        });

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextViewList.removeSelected();
                saveImage();
                btnSave.setVisibility(View.GONE);
            }
        });

        btnShare = findViewById(R.id.btn_share);
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
            myTextViewList.clear();
            showAllBtn();

            if (requestCode == myGallery.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == myCamera.REQUEST_CAMERA)
                onCaptureImageResult();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask == myCamera.REQUEST_CAMERA)
                        cameraIntent();
                    else if (userChoosenTask == myGallery.SELECT_FILE)
                        galleryIntent();
                }
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = myGallery.galleryIntent();
        startActivityForResult(Intent.createChooser(intent, "Select File"), myGallery.SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = myCamera.cameraIntent();
        photoUri = myCamera.getPhotoUri();
        startActivityForResult(intent, myCamera.REQUEST_CAMERA);
    }

    private void shareIntent() {
        Intent intent = myShare.shareIntent(editImageLayout);
        startActivity(Intent.createChooser(intent, "Share Image"));
    }

    private void saveImage() {
        mySave.saveImage(editImageLayout);

        Toast toast = Toast.makeText(this, "Save Successfully!", Toast.LENGTH_LONG);
        toast.show();
    }

    private void onCaptureImageResult() {
        imageView.setImageURI(photoUri);
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

    private void createText() {
        removeTextProperties();
        myTextViewController.createText();
    }

    private void removeTextProperties() {
        Fragment fragment = fragmentManager.findFragmentByTag(MyTextViewController.TAG_TEXT_EDIT_FRAGMENT);
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    public void showAllBtn() {
        btnRotate.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
    }
}
