package com.android.melo.kaoqinfuxiao.http;

/**
 * Created by melo on 2018/10/14.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;


import com.android.melo.kaoqinfuxiao.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

;

/**
 * Created by melo on 2018/04/08.
 * okhttp访问
 */
public class OkHttpHelper {
    public static String TAG = OkHttpHelper.class.getSimpleName();
    private static OkHttpHelper sOkHttpHelper;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private OkHttpHelper() {
        /**
         * 构建OkHttpClient
         */
        mOkHttpClient = new OkHttpClient();
        /**
         * 设置连接的超时时间
         */
        mOkHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);

        /**
         * 设置响应的超时时间
         */
        mOkHttpClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        /**
         * 请求的超时时间
         */
        mOkHttpClient.newBuilder().readTimeout(30, TimeUnit.SECONDS);
        /**
         * 获取主线程的handler
         */
        mHandler = new Handler(Looper.getMainLooper());

    }


    public interface NetResultCallback {
        void onFail(Object o);

        void onSuccess(String jsonString);
    }

//    static {
//        okHttpClient = new OkHttpClient();
//    }

    /**
     * 单例模式构造对象
     *
     * @return OkHttpHelper
     */
    public static OkHttpHelper getInstance() {
        if (sOkHttpHelper == null) {
            synchronized (OkHttpHelper.class) {
                if (sOkHttpHelper == null) {
                    sOkHttpHelper = new OkHttpHelper();
                }
            }
        }
        return sOkHttpHelper;
    }


    /**
     * get请求服务器
     *
     * @param url               访问地址
     * @param netResultCallback 访问结果回调
     */
    public void doGet(String url, final NetResultCallback netResultCallback) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //非UI线程
                //创建一个UI线程中的handler

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        netResultCallback.onFail(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Handler handler = new Handler(Looper.getMainLooper());
                if (response.code() == 200) {
                    final String jsonString = response.body().string().toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netResultCallback.onSuccess(jsonString);
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netResultCallback.onFail(response.code() + "");
                        }
                    });
                }
            }


        });

    }

    /**
     * post请求服务器提交键值对
     *
     * @param url               访问地址
     * @param params            访问参数集合
     * @param netResultCallback 访问结果回调
     */
    public void doPost(String url, HashMap<String, String> params, final NetResultCallback netResultCallback) {
        Iterator<String> iterator = params.keySet().iterator();
        FormBody.Builder builder = new FormBody.Builder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            LogUtils.i(TAG, "value:" + value + ",key:" + key + "----------------" + builder);
            builder.add(key, value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //非UI线程
                //创建一个UI线程中的handler
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netResultCallback.onFail(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Handler handler = new Handler(Looper.getMainLooper());
                if (response.code() == 200) {
                    final String jsonString = response.body().string().toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.i(TAG, "jsonString:" + jsonString);
                            netResultCallback.onSuccess(jsonString);
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netResultCallback.onFail(response.code() + "");
                        }
                    });
                }
            }
        });
    }

    /**
     *
     * 构建Request
     * @param url  上传跟地址
     * @param file 上传图片文件
     * @param maps 上传其他参数
     * @param netResultCallback  请求结果回调
     */
    public void upData(String url, File file, Map<String, String> maps , final NetResultCallback netResultCallback){
        Request request=getFileRequest(url,file,maps);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //非UI线程
                //创建一个UI线程中的handler
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netResultCallback.onFail(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Handler handler = new Handler(Looper.getMainLooper());
                if (response.code() == 200) {
                    final String jsonString = response.body().string().toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.i(TAG, "jsonString:" + jsonString);
                            netResultCallback.onSuccess(jsonString);
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netResultCallback.onFail(response.code() + "");
                        }
                    });
                }
            }
        });
    }

    /**
     * 构建Request
     * @param url  上传跟地址
     * @param file 上传图片文件
     * @param maps 上传其他参数
     * @return 返回request实体
     */
    public Request getFileRequest(String url, File file, Map<String, String> maps) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (maps == null) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"), file)
            ).build();

        } else {
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }

            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"), file)
            );

        }
        RequestBody body = builder.build();
        return new Request.Builder().url(url).post(body).build();

    }

}
