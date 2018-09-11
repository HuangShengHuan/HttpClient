package com.wemind.net;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by HSH on 18-9-11.
 */
public class ResponseBodyWrapper extends ResponseBody {

    private ResponseBody body;

    private BufferedSource bufferedSource;

    private long totalBytesRead;

    public ResponseBodyWrapper(ResponseBody body) {
        this.body = body;
    }

    public long totalBytesRead(){
        return totalBytesRead;
    }

    public boolean emptyBody() {
        return body == null;
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(body.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                return bytesRead;
            }
        };

    }
}
