package finalproject.kmitl.chanapat58070024.mymeme;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFile {
    private final String MEME_FOLDER = "Meme";

    private ByteArrayOutputStream bytes;

    public MyFile() {
        bytes = new ByteArrayOutputStream();
    }

    public File createImage(Bitmap thumbnail) {
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return outputFile(getMemePath(), timeStamp + ".jpg");
    }

    public File createTempImage() {
        return outputFile(Environment.getExternalStorageDirectory().getAbsolutePath(),
                System.currentTimeMillis() + ".jpg");
    }

    public File createTempImage(Bitmap thumbnail) {
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return outputFile(Environment.getExternalStorageDirectory().getAbsolutePath(),
                System.currentTimeMillis() + ".jpg");
    }

    private File outputFile(String path, String name) {
        File destination = new File(path, name);
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

        return destination;
    }

    private String getMemePath() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + MEME_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder.getAbsolutePath();
    }
}
