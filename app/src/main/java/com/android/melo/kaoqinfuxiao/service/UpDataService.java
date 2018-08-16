package com.android.melo.kaoqinfuxiao.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.android.melo.kaoqinfuxiao.db.UpDataInfo;
import com.android.melo.kaoqinfuxiao.global.BaseEvent;
import com.android.melo.kaoqinfuxiao.global.Constants;
import com.android.melo.kaoqinfuxiao.global.Global;
import com.android.melo.kaoqinfuxiao.global.Urls;
import com.android.melo.kaoqinfuxiao.http.OkHttpHelper;
import com.android.melo.kaoqinfuxiao.utils.ImageUtils;
import com.android.melo.kaoqinfuxiao.utils.LiteOrmDBUtil;
import com.android.melo.kaoqinfuxiao.utils.LogUtils;
import com.android.melo.kaoqinfuxiao.utils.SPUtils;
import com.android.melo.kaoqinfuxiao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by melo on 2018/4/17.
 * 上传刷卡信息服务
 */

public class UpDataService extends Service {

    public static final int UPDATA_COMPLETE = 1;//上传刷卡信息成功
    private LinkedList<String> msgList = new LinkedList<>();
    private boolean isTaskRuning;//是否已经有一个上传任务了
    @SuppressLint("HandlerLeak")
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_COMPLETE:
                    isTaskRuning = false;
                    List<UpDataInfo> upDataInfos = LiteOrmDBUtil.getQueryAll(UpDataInfo.class);
                    EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.UPDATA_COMPLETE, upDataInfos.size() + ""));
                    startUp();
                    break;
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UpDataBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //开机检测刷卡信息本地未上传任务
        startUp();
    }

    //开启上传任务
    public void startUp() {
        //是否已经有一条上传任务
        List<UpDataInfo> upDataInfos = LiteOrmDBUtil.getQueryAll(UpDataInfo.class);
        EventBus.getDefault().post(new BaseEvent<>(BaseEvent.Type.UPDATA_COMPLETE, upDataInfos.size() + ""));
        LogUtils.i("upDataInfos", "isTaskRuning :" + isTaskRuning + "，upDataInfos" + upDataInfos.size());
        if (!isTaskRuning && upDataInfos.size() > 0) {
            isTaskRuning = true;
            UpDataInfo info = upDataInfos.get(0);
            final String dateTime = info.getIcDateTime();
            String memberUid = info.getMemberUid();
            String icNumber = info.getIcNumber();
            String picPath = info.getPicPath();
            String type = info.getType();
            String schoolType = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_TYPE, "");
            String url = "";
            if (schoolType.equals(Constants.SchoolType.SCHOOL_DIRECT)) {
                url = Urls.BASEURL_ZY + "api/studentgetall/getAddCard/";
            } else if (schoolType.equals(Constants.SchoolType.SCHOOL_FUXIAO)) {
                url = Urls.BASEURL_FX + "api/studentgetall/getAddCard/";
            } else if (schoolType.equals(Constants.SchoolType.SCHOOL_QINGNIAO)) {
                url = Urls.BASEURL_QN + "api/studentgetall/getAddCard/";
            }
            HashMap<String, String> params = new HashMap();
            params.put("icNumber", icNumber);
            params.put("icDateTime", dateTime);
            params.put("memberUid", memberUid);
//            params.put("type", type);
            params.put("picName ", dateTime);
//                    Bitmap bitmap = ImageUtils.getBitmapByPath(picPath);
//            File file = new File(picPath);
//            OkHttpHelper.getInstance().upData(url, file, params, new OkHttpHelper.NetResultCallback() {
//                @Override
//                public void onFail(Object o) {
//                    ToastUtil.showToast("上传刷卡信息失败");
//                }
//
//                @Override
//                public void onSuccess(String jsonString) {
//                    ToastUtil.showToast("上传刷卡信息成功");
//                    //删除数据库该条数据
//                    LiteOrmDBUtil.deleteWhere(UpDataInfo.class, "icDateTime", new String[]{dateTime});
//                    mHandle.sendEmptyMessage(UPDATA_COMPLETE);
//                }
//            });
            File picfile = new File(picPath);
            String encodeString = null;
            if (picfile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
                    byte[] bytes = baos.toByteArray();
                    byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
                    encodeString = new String(encode);
                } else {
                    encodeString = "";
                }

            } else {
                encodeString = "";
            }
            params.put("picData", encodeString);
            LogUtils.i("updata", url);
            //上传刷卡信息
//            url="http://i.pkucollege.com/api/studenttest/getAddCard/";
            LogUtils.i("url", "上传刷卡:" + url);
            OkHttpHelper.getInstance().doPost(url, params, new OkHttpHelper.NetResultCallback() {

                @Override
                public void onFail(Object o) {
                    ToastUtil.getInstance().showToast("上传刷卡信息失败");
                    isTaskRuning = false;
                }

                @Override
                public void onSuccess(String jsonString) {
                    ToastUtil.getInstance().showToast("上传刷卡信息成功");
                    //删除数据库该条数据
                    LiteOrmDBUtil.deleteWhere(UpDataInfo.class, "icDateTime", new String[]{dateTime});
                    mHandle.sendEmptyMessage(UPDATA_COMPLETE);
                }
            });
        }
    }

    public class UpDataBinder extends Binder {

        public void startUpData() {
            startUp();
        }
    }
}
