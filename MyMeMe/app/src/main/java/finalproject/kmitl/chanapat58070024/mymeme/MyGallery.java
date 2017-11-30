package finalproject.kmitl.chanapat58070024.mymeme;

import android.content.Intent;

public class MyGallery {
    public static final int SELECT_FILE = 1;

    public Intent galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        return intent;
    }
}
