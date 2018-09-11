package com.wemind.net;

/**
 * Created by HSH on 18-9-10.
 */
public interface Config {
    String TAG = "HTTP_CLIENT";

    String BASE_URL = "";

    int CONNECT_TIMEOUT = 15;
    int READ_TIMEOUT = 20;
    int WRITE_TIMEOUT = 20;
    boolean RETRY_CONNECTED = true;
}
