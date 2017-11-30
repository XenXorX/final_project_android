package finalproject.kmitl.chanapat58070024.mymeme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;

import java.io.File;

public class MySave {
    private Context mContext;

    public MySave(Context context) {
        mContext = context;
    }

    public void saveImage(ConstraintLayout editImageLayout) {
        editImageLayout.buildDrawingCache();
        Bitmap thumbnail = editImageLayout.getDrawingCache();

        MyFile myFile = new MyFile();
        File destination = myFile.createImage(thumbnail);

        galleryAddPic(destination);
        editImageLayout.destroyDrawingCache();
    }

    private void galleryAddPic(File photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photoPath);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }
}
