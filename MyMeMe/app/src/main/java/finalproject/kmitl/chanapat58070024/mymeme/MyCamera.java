package finalproject.kmitl.chanapat58070024.mymeme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

public class MyCamera {
    public static final int REQUEST_CAMERA = 0;

    private Context mContext;
    private Uri photoUri;

    public MyCamera(Context context) {
        mContext = context;
    }

    public Intent cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        MyFile myFile = new MyFile();
        File destination = myFile.createTempImage();
        photoUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        return intent;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }
}
