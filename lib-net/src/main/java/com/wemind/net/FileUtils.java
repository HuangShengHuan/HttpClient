package com.wemind.net;

import java.io.File;
import java.io.InputStream;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by HSH on 18-9-11.
 */
public class FileUtils {
    /**
     * 当同时存在Source和Sink时，不建议完全采用链式的调用，因为source、sink在结束时
     * 必须关闭；
     * 小心使用writeAll,因为writeAll并不会返回Sink，所以采用链式调用最后没法直接close；
     *
     * 对于压缩文件，如果在最后没有调用close将流正确关闭，则会损坏到压缩文件
     */
    public static void writeFile(InputStream in, String filePath) throws Exception {
        BufferedSink sink = Okio.buffer(Okio.sink(new File(filePath)));

        BufferedSource source = Okio.buffer(Okio.source(in));

        sink.writeAll(source);

        sink.close();
        source.close();
    }
}
