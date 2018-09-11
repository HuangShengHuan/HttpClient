package com.wm.hsh.httpclient;

import com.wemind.net.BaseModel;

import java.util.List;

/**
 * Created by HSH on 18-9-11.
 */
public class Model2 extends BaseModel {
    public List<MyModel.Data> data;

    public static class Data{
        public String femalename;
    }
}
