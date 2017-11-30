package finalproject.kmitl.chanapat58070024.mymeme;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyFile {
    private  ByteArrayOutputStream bytes;

    public MyFile() {
        bytes = new ByteArrayOutputStream();
    }

    public File createTempImage() {
        return outputFile();
    }

    public File createTempImage(Bitmap thumbnail) {
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return outputFile();
    }

    private File outputFile() {
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

        return destination;
    }
}
