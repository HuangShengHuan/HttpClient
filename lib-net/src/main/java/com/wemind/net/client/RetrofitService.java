package com.wemind.net.client;

import com.wemind.net.BaseModel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by HSH on 18-9-10.
 */
public interface RetrofitService {

    @FormUrlEncoded
    @POST("{path}")
    Call<ResponseBody> post(@HeaderMap Map<String, String> headers,
                                             @Path(value = "path", encoded = true) String path,
                                             @FieldMap Map<String, String> params);

    @GET
    Call<ResponseBody> get(@HeaderMap Map<String, String> headers, @Url String url);


    @Multipart
    @POST("{path}")
    Call<ResponseBody> upload(@HeaderMap Map<String, String> headers,
                                               @Path(value = "path", encoded = true) String path,
                                               @Part("description") RequestBody description,
                                               @Part MultipartBody.Part file);

    @Multipart
    @POST("{path}")
    Call<ResponseBody> multiUpload(@HeaderMap Map<String, String> headers,
                                   @Path(value = "path", encoded = true) String path,
                                   @Body RequestBody body);
}
