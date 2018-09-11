package com.wm.hsh.httpclient;

import com.wemind.net.BaseModel;

import java.util.List;

/**
 * Created by HSH on 18-9-10.
 */
public class MyModel extends BaseModel{
    public boolean error;
    public List<Data> results;


    /**
     * 		"_id": "5b7105eb9d212234189c24ce",
     "createdAt": "2018-08-13T12:15:39.942Z",
     "desc": "又一个Android权限管理器。",
     "publishedAt": "2018-08-28T00:00:00.0Z",
     "source": "chrome",
     "type": "Android",
     "url": "https://github.com/yanzhenjie/AndPermission",
     "used": true,
     "who": "lijinshanmx"
     */
    public static class Data{
        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public String used;
        public String who;
    }
}
