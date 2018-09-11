package com.wemind.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.functions.Consumer;

/**
 * Created by HSH on 18-9-11.
 */
public class FileUtils {
    public static void writeFile(InputStream in, String filePath, Consumer<Long> consumer) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos;
        fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int len;
        if (consumer != null) {
            consumer.accept(0L);
        }
        while ((len = in.read(b)) != -1) {
            fos.write(b, 0, len);
            if (consumer != null) {
                consumer.accept(0L);
            }
        }
        if (consumer != null) {
            consumer.accept(0L);
        }
        in.close();
        fos.close();
        if (consumer != null) {
            consumer.accept(-1L);
        }
    }
}
