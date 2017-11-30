package finalproject.kmitl.chanapat58070024.mymeme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;

import java.io.File;

public class MyShare {
    private Context mContext;

    public MyShare(Context context) {
        mContext = context;
    }

    public Intent shareIntent(ConstraintLayout editImageLayout) {
        editImageLayout.buildDrawingCache();
        Bitmap thumbnail = editImageLayout.getDrawingCache();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");

        MyFile myFile = new MyFile();
        File destination = myFile.createTempImage(thumbnail);
        Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", destination);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        editImageLayout.destroyDrawingCache();

        return intent;
    }
}
