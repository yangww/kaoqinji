package com.android.melo.kaoqinfuxiao.view;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.melo.kaoqinfuxiao.R;
import com.android.melo.kaoqinfuxiao.base.SerialPortActivity;
import com.android.melo.kaoqinfuxiao.bean.BannerBean;
import com.android.melo.kaoqinfuxiao.bean.LoginBean;
import com.android.melo.kaoqinfuxiao.bean.UserMsgBean;
import com.android.melo.kaoqinfuxiao.db.UpDataInfo;
import com.android.melo.kaoqinfuxiao.db.UserInfo;
import com.android.melo.kaoqinfuxiao.global.BaseEvent;
import com.android.melo.kaoqinfuxiao.global.Constants;
import com.android.melo.kaoqinfuxiao.global.Global;
import com.android.melo.kaoqinfuxiao.global.Urls;
import com.android.melo.kaoqinfuxiao.http.HttpUtils;
import com.android.melo.kaoqinfuxiao.http.OkHttpHelper;
import com.android.melo.kaoqinfuxiao.receiver.WifiAndLineNetBroadcastReceiver;
import com.android.melo.kaoqinfuxiao.service.UpDataService;
import com.android.melo.kaoqinfuxiao.utils.ImageUtils;
import com.android.melo.kaoqinfuxiao.utils.JsonUtil;
import com.android.melo.kaoqinfuxiao.utils.LiteOrmDBUtil;
import com.android.melo.kaoqinfuxiao.utils.LogUtils;
import com.android.melo.kaoqinfuxiao.utils.SDcardUtils;
import com.android.melo.kaoqinfuxiao.utils.SPUtils;
import com.android.melo.kaoqinfuxiao.utils.TimeUtil;
import com.android.melo.kaoqinfuxiao.utils.ToastUtil;
import com.android.melo.kaoqinfuxiao.view.adapter.SchoolNumMsgListAdapter;
import com.android.melo.kaoqinfuxiao.weight.AutoVideoview;
import com.android.melo.kaoqinfuxiao.weight.BitmapFillet;
import com.android.melo.kaoqinfuxiao.weight.ClearEditText;
import com.android.melo.kaoqinfuxiao.weight.CustomDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.litesuits.orm.db.assit.QueryBuilder;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends SerialPortActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private WebView bannerView;//首页banner
    private AutoVideoview bannerVideo;//首页banner
    private TextView tvDate, tvQuality, tvOperation;//头部日期,数量,操作
    private EditText etIdCard; //读取卡号
    //显示操作popup
    private PopupWindow mPopupWindow;
    private LayoutInflater mInflater;
    private View popupView;
    //操作中的按钮
    private TextView tvLogin, tvUpData, tvDataList;
    //登录中的按钮
    private ClearEditText mEtAccount, mEtPassWord, mEtJiHuoMa;
    //登录dialog,学校类型
    private CustomDialog dialog;
    //直营和附校登录框
    private RelativeLayout mLayoutDirect, mLayoutFxOrQn;
    private RadioButton mRbZy, mRbFx, mRbQn;
    private String schoolType;
    //登录账号密码,激活码
    private String account, passWord, jiHuoMa;
    //品牌根地址
    private String schoolTypeAddress;
    //校园简介内容
    private TextView mTvIntroContent;
    //校园公告
    private TextView mTvschoolnewsContent;
    //照相
    private SurfaceView surfaceView;
    private Camera camera;
    private AudioManager mAudioManager;
    //上传刷卡信息
    private Intent intent;
    private UpDataService.UpDataBinder upDataIbinder;
    //刷卡信息显示
    private LinearLayout mLayoutCardmsg;
    private ImageView picture;
    private TextView tvIdCard, tvClass, tvUserName;
    //姓名 班级 卡号 身份
    private String userName = "";
    private String className = "";
    private String card = "";
    private String identity = "";
    private String memberUid = "";
    //wifi信号图片
    private int[] wifiStateImgs = new int[]{R.mipmap.sign_1,
            R.mipmap.sign_2, R.mipmap.sign_3,
            R.mipmap.sign_4, R.mipmap.sign_5};
    private ImageView mImageWifi, mImageLineNet;
    private WifiAndLineNetBroadcastReceiver mWifiAndLineNetBroadcastReceiver;
    //音频播放
    private MediaPlayer mPlayer;
    //在和合成时间计算
    private boolean isStartSynthe;//是否开始合成


    //    private ScrollTextView mStTntroduction, mStCshoolNews;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.Time.REFLASH:
                    //更新时间
                    tvDate.setText(TimeUtil.getCurrentDay2());
                    mHandler.sendEmptyMessageDelayed(Constants.Time.REFLASH, 1000);
                    break;
                case Constants.UPDATA_SUCCESS:
                    //数据库更新成功
                    ToastUtil.getInstance().showToast("同步数据成功");
                    //关闭dialog
                    changeDiaLogContent(Constants.LoadingType.UPDATA_SUCCESS);
                    dismissDiaLog(1000);
                    //让edittext重新获取焦点
                    etIdCard.requestFocusFromTouch();
                    break;
                case Constants.Dialog.CLOSE:
                    //关闭dialog
                    mLayoutCardmsg.setVisibility(View.INVISIBLE);
                    //让edittext重新获取焦点
                    etIdCard.requestFocusFromTouch();
                    //继续视频播放
                    if (bannerVideo != null && !bannerVideo.isPlaying()) {
                        bannerVideo.start();
                    }
                    break;
                case Constants.READ_ICCARD:
//                    ToastUtil.getInstance().showToast("读取刷卡数据");
                    //读取卡号，对刷卡信息操作
                    mLayoutCardmsg.setVisibility(View.INVISIBLE);
                    mHandler.removeMessages(Constants.Dialog.CLOSE);

//                    String idCard = etIdCard.getText().toString();
////                    String idCard = "1536544182";
//                    ToastUtil.showToast("idcard:" + idCard);
                    String idCard = String.valueOf(msg.getData().getInt("iValue"));
//                    ToastUtil.getInstance().showToast("idcard:" + idCard);
                    etIdCard.setText("");
//                    ToastUtil.showToast("idCard:"+idCard);
                    //1.查询本地数据库 如果没有就去请求网络，并存如本地数据库,拍照上传
                    //2.如果有该id，就直接拍照上传
                    if (idCard.length() < 4) {
                        //本地不存在该学生数据
                        showDiaLog(R.layout.layout_dialog_loading);
                        changeDiaLogContent(Constants.LoadingType.ICCARD_UNEXIST);
                        dismissDiaLog(1000);
                        ToastUtil.getInstance().showToast("无数据!");
                        return;
                    }
                    List<UserInfo> userInfos = LiteOrmDBUtil.getLiteOrm().query(new QueryBuilder<UserInfo>(UserInfo.class)
                            .where("idCard" + " LIKE ?", new String[]{"%" + idCard + "%"}));
                    if (userInfos.size() > 0) {
                        //本地数据库存在该学生数据，拍照上传服务器，展示信息
                        //清空edit
                        userName = userInfos.get(0).getUserName();
                        className = userInfos.get(0).getClassName();
                        card = idCard;
                        memberUid = userInfos.get(0).getMemberUid();
                        identity = userInfos.get(0).getIdentity();
                        String imgName = userInfos.get(0).getImgName();
                        String filePath = ImageUtils.getBaseUpDataImgUrl() + "/" + imgName;
                        LogUtils.i("path", filePath);
                        if (!"".equals(imgName)) {
                            Bitmap bitmap = ImageUtils.getBitmapByPath(filePath);
                            Bitmap b = BitmapFillet.fillet(BitmapFillet.ALL, bitmap, 50);
                            picture.setImageBitmap(b);
                        }
                        //显示卡号班级姓名
                        mLayoutCardmsg.setVisibility(View.VISIBLE);
                        //让edittext重新获取焦点
                        etIdCard.requestFocusFromTouch();
                        tvIdCard.setText(card);
                        tvClass.setText(className);
                        tvUserName.setText(userName);
                        //暂停视频播放
                        if (bannerVideo != null && bannerVideo.isPlaying()) {
                            bannerVideo.pause();
                        }
                        //无网，播放本地
                        if (mPlayer != null) {
                            mPlayer.stop();
                        }

                        //播放离线语音
                        playMedia();
                        mHandler.sendEmptyMessageDelayed(Constants.Dialog.CLOSE, 1500);
                        try{
                            //拍照
                            if (camera != null&&camera.getParameters()!=null) {
                                Camera.Parameters parameters = camera.getParameters();
                                parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
                                camera.setParameters(parameters);
                                camera.takePicture(new Camera.ShutterCallback() {
                                    @Override
                                    public void onShutter() {
                                        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);
                                    }
                                }, null, mTakePictureCallback);
//                            //播放离线语音
//                            playMedia();
//                            mHandler.sendEmptyMessageDelayed(Constants.Dialog.CLOSE, 1500);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        else {
//                            ToastUtil.getInstance().showToast("刷卡失败");
////                         关闭dialog
//                            mLayoutCardmsg.setVisibility(View.INVISIBLE);
//                        }
                    } else {
                        //判断是否登录过
                        schoolTypeAddress = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_ADDRESS, "");
                        if ("".equals(schoolTypeAddress)) {
                            //未登录过，跳转到登陆
                            loginShow();
                            return;
                        }
                        //本地不存在该学生数据
                        showDiaLog(R.layout.layout_dialog_loading);
                        changeDiaLogContent(Constants.LoadingType.ICCARD_UNEXIST);
                        dismissDiaLog(1000);
                        ToastUtil.getInstance().showToast("无数据!");
                    }
                    break;
            }
        }
    };

    /**
     * 播放离线语音
     */
    private void playMedia() {
        try {
            int number = new Random().nextInt(3);

            AssetFileDescriptor afd = getAssets().openFd(number+".mp3");
            if (afd != null) {
                if (mPlayer == null) {
                    mPlayer = new MediaPlayer();
                }
                mPlayer.reset(); // 设置reset状态，处于Idle状态
                mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());// 设置哪一首歌曲
                mPlayer.prepare(); // 歌曲准备
                mPlayer.start(); // 歌曲开始
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 联系代理Con
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            upDataIbinder = (UpDataService.UpDataBinder) service;
            //同时启动定时上传刷卡信息任务,1小时启动一次
            mHandler.postDelayed(runnable, 300 * 1000);//每五分钟时执行一次runnable. 
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ToastUtil.getInstance().showToast("开启后台任务失败");

        }
    };


    @Override
    public void initView() {
//        mStTntroduction = findViewById(R.id.st_main_introductionContent);
//        mStCshoolNews = findViewById(R.id.st_main_schoolnews_content);
        bannerView = findViewById(R.id.wv_main_banner);
        bannerVideo = findViewById(R.id.vv_main_banner);
        tvDate = findViewById(R.id.tv_topbar_date);
        tvOperation = findViewById(R.id.tv_topbar_operation);
        tvQuality = findViewById(R.id.tv_topbar_quality);
        etIdCard = findViewById(R.id.et_main_idcard);
        mTvIntroContent = findViewById(R.id.tv_main_introductionContent);
        mTvschoolnewsContent = findViewById(R.id.iv_main_schoolnews_content);
        mLayoutCardmsg = findViewById(R.id.layout_main_cardmsg);
        picture = findViewById(R.id.iv_dialog_cardmsgshow);
        tvIdCard = findViewById(R.id.tv_dialog_msgshow_idcard_content);
        tvClass = findViewById(R.id.tv_dialog_msgshow_class_content);
        tvUserName = findViewById(R.id.tv_dialog_msgshow_name_content);
        mImageWifi = findViewById(R.id.iv_topbar_wifi);
        mImageLineNet = findViewById(R.id.iv_topbar_netline);
        tvDate.setOnClickListener(this);
        tvOperation.setOnClickListener(this);
        //相机init
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        surfaceView = findViewById(R.id.sv_main_surfaceView);
        surfaceView.getHolder().setFixedSize(surfaceView.getWidth(), surfaceView.getHeight());
        // 设置SurfaceHolder对象的类型
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
        surfaceView.getHolder().addCallback(new SurfaceCallback());


        //网络状态初始化判断
        if (!HttpUtils.getWifiState()) {
            //wifi'判断
            mImageWifi.setImageResource(wifiStateImgs[4]);
        }

        if (HttpUtils.isIntenetConnected(Global.getInstance())) {
            //以太网判断
            mImageLineNet.setImageResource(R.mipmap.iv_netline_true);
        } else {
            mImageLineNet.setImageResource(R.mipmap.iv_netline_false);
        }
    }


    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        //加载本地图片
        bannerView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadData(Urls.URL_LOCALBANNER, "UTF-8", "text/html");
            }
        });
        bannerView.getSettings().setJavaScriptEnabled(true);
        bannerView.loadUrl(Urls.URL_LOCALBANNER);
        //加载本地信息
        String aboutuUs = SPUtils.getString(Global.getInstance(), Constants.Share.ABOUTUS, "");
        String desc = SPUtils.getString(Global.getInstance(), Constants.Share.COMPANYNAME, "");
        mTvIntroContent.setText("\t\t" + aboutuUs);
        mTvschoolnewsContent.setText("\t\t" + desc);
//        mStTntroduction.setText("\t\t" + aboutuUs);
//        mStCshoolNews.setText("\t\t" + desc);
        List<UpDataInfo> upDataInfos = LiteOrmDBUtil.getQueryAll(UpDataInfo.class);
        tvQuality.setText("还有" + upDataInfos.size() + "条尚未上传");
        //获取系统时间
        Message msg = Message.obtain();
        msg.what = Constants.Time.REFLASH;
        mHandler.sendMessage(msg);
        //开启后台刷卡服务
        bindService();
        //注册wifi监听广播
        registReceiver();
        //判断自动登录
        autoLogin();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    //    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String sr = new String(buffer, 0, size);
                String rb = replaceBlank(sr);

                // int iValue = Integer.parseInt(rb, 16);
                // String xx = rb.substring(0,8);
                int iValue = Integer.parseInt(sr.trim(), 16);
                // int ksz = byte2int(buffer);
//                    if (kh != null) {
//                    ToastUtil.getInstance().showToast("idcard1:"+iValue);
//                    tvIdCard.setText(iValue + "");
//                        kht.setText(iValue + "");
                                /* 读卡 */
                // kh.setText(iValue + "");
                Message msg = Message.obtain();
                msg.what = Constants.READ_ICCARD;
                Bundle bundle = new Bundle();
                bundle.putInt("iValue", iValue);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
    }
    //播放语音

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            dest = dest.replaceAll(" ", "");
        }
        return dest;
    }

    //菜单列表显示
    private boolean isShow = false;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_topbar_operation:
                //首页操作按钮
                if (!isShow) {
                    //未打开菜单
                    showOperations();
                    isShow = true;
                } else {
                    closePopupwindow();
                    isShow = false;
                }

                break;
            case R.id.tv_operation_login:
                //登录框显示
                closePopupwindow();
                loginShow();
                break;
            case R.id.iv_loginDialog_close://关闭
            case R.id.btn_loginDialog_cancle://取消登录
                //关闭登录dialog
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
            case R.id.btn_loginDialog_login:
                //登录框品牌选择
                schoolTypeSelect();
                break;
            case R.id.tv_operation_updata:
                //同时更新数据
                closePopupwindow();
                String memberUid = SPUtils.getString(Global.getInstance(), Constants.Share.MEMBERUID, "");
                if (!"".equals(memberUid)) {
                    //删除数据库,然后更新

                    StringBuilder sbUpData = new StringBuilder();
                    sbUpData.append(schoolTypeAddress).append(Urls.URL_UPDATA)
                            .append("memberUid=").append(memberUid);
                    upData(sbUpData.toString());
                } else {
                    //登录框显示
                    loginShow();
                    ToastUtil.getInstance().showToast("请登陆后再同步数据");
                }

                break;
            case R.id.tv_operation_dataList:
                closePopupwindow();
                //判断是否登录过
                schoolTypeAddress = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_ADDRESS, "");
                if ("".equals(schoolTypeAddress)) {
                    //未登录过，跳转到登陆
                    loginShow();
                    return;
                }
                List<UserInfo> infoList = LiteOrmDBUtil.getQueryAll(UserInfo.class);
                LogUtils.i("userinfo", infoList.size() + "");
                dialog = new CustomDialog(this,
                        1100, 1800, R.layout.layout_dialog_datalist,
                        R.style.pop_anim_style, Gravity.CENTER);
                dialog.show();
                ListView lvSchoolNum = dialog.findViewById(R.id.lv_dialog_schoolNumInfo);
                ImageView ivDialogClose = dialog.findViewById(R.id.iv_loginDialog_close);
                TextView tvSchoolNum = dialog.findViewById(R.id.tv_dialog_schoolNumber);
                TextView tvTeacherNum = dialog.findViewById(R.id.tv_dialog_teacherNumber);
                TextView tvStudentNum = dialog.findViewById(R.id.tv_dialog_studentNumber);
                ivDialogClose.setOnClickListener(this);
                SchoolNumMsgListAdapter numMsgListAdapter = new SchoolNumMsgListAdapter(this, infoList);
                lvSchoolNum.setAdapter(numMsgListAdapter);
                List<UserInfo> Students = LiteOrmDBUtil.getQueryByWhere(UserInfo.class, "identity", new String[]{"学生"});
                tvSchoolNum.setText(infoList.size() + "");
                tvStudentNum.setText(Students.size() + "");
                tvTeacherNum.setText((infoList.size() - Students.size()) + "");
                break;

        }
    }


    /**
     * 登录框显示
     */
    private void loginShow() {
        //如果已经登陆过，判断当前该学校品牌类型，对应显示登录方式
        schoolType = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_TYPE, Constants.SchoolType.SCHOOL_DIRECT);
        dialog = new CustomDialog(this,
                540, 600, R.layout.login_global,
                R.style.pop_anim_style, Gravity.CENTER);
        dialog.show();
        ImageView ivLoginDialogClose = dialog.findViewById(R.id.iv_loginDialog_close);
        mLayoutDirect = dialog.findViewById(R.id.layout_loginDialog_direct);
        mLayoutFxOrQn = dialog.findViewById(R.id.layout_loginDialog_joinOrQingNiao);
        Button mBtnLogin = dialog.findViewById(R.id.btn_loginDialog_login);
        Button mBtnCancle = dialog.findViewById(R.id.btn_loginDialog_cancle);
        RadioGroup mRg = dialog.findViewById(R.id.rg_loginDialog);
        mRg.setOnCheckedChangeListener(this);
        ivLoginDialogClose.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
        mRbZy = dialog.findViewById(R.id.rb_loginDialog_direct);
        mRbFx = dialog.findViewById(R.id.rb_loginDialog_join);
        mRbQn = dialog.findViewById(R.id.rb_loginDialog_qn);
        switch (schoolType) {
            //判断品牌
            case Constants.SchoolType.SCHOOL_DIRECT:
                //直营
                mLayoutDirect.setVisibility(View.VISIBLE);
                mLayoutFxOrQn.setVisibility(View.INVISIBLE);
                mEtJiHuoMa = dialog.findViewById(R.id.et_loginDialog_jiHuoMa);
                mRbZy.setChecked(true);
                break;
            case Constants.SchoolType.SCHOOL_FUXIAO:
                //附校
                mRbFx.setChecked(true);
                mLayoutDirect.setVisibility(View.INVISIBLE);
                mLayoutFxOrQn.setVisibility(View.VISIBLE);
                mEtAccount = dialog.findViewById(R.id.et_loginDialog_userName);
                mEtPassWord = dialog.findViewById(R.id.et_loginDialog_password);
                break;
            case Constants.SchoolType.SCHOOL_QINGNIAO:
                mRbQn.setChecked(true);
                //青鸟
                mLayoutDirect.setVisibility(View.INVISIBLE);
                mLayoutFxOrQn.setVisibility(View.VISIBLE);
                mEtAccount = dialog.findViewById(R.id.et_loginDialog_userName);
                mEtPassWord = dialog.findViewById(R.id.et_loginDialog_password);
                break;
        }
    }

    /**
     * 登录框学校品牌选择
     */
    private void schoolTypeSelect() {
        //登录,判断学校品牌类型
        StringBuilder sb = new StringBuilder();
        switch (schoolType) {
            //判断品牌
            case Constants.SchoolType.SCHOOL_DIRECT:
                //直营
                schoolTypeAddress = Urls.BASEURL_ZY;
                jiHuoMa = mEtJiHuoMa.getText().toString();
//                        jiHuoMa = "9siqk3";
                if ("".equals(jiHuoMa)) {
                    ToastUtil.getInstance().showToast("激活码不能为空");
                    return;
                }
                dialog.dismiss();
                sb.append(Urls.BASEURL_ZY).append(Urls.URL_LOGIN)
                        .append("LoginName=").append(jiHuoMa);
                //登录
                Login(sb.toString());

                break;
            case Constants.SchoolType.SCHOOL_FUXIAO:
            case Constants.SchoolType.SCHOOL_QINGNIAO:
                //附校  或者青鸟
                account = mEtAccount.getText().toString();
                passWord = mEtPassWord.getText().toString();
//                        account = "admin";
//                        passWord = "pku2017s";
                if ("".equals(account)) {
                    ToastUtil.getInstance().showToast("账号不能为空");
                    return;
                }
                if ("".equals(passWord)) {
                    ToastUtil.getInstance().showToast("密码不能为空");
                    return;
                }
                dialog.dismiss();


                if (schoolType.equals(Constants.SchoolType.SCHOOL_FUXIAO)) {
                    //附校
                    schoolTypeAddress = Urls.BASEURL_FX;
                    sb.append(Urls.BASEURL_FX).append(Urls.URL_LOGIN)
                            .append("LoginName=").append(account)
                            .append("&pwd=").append(passWord);
                } else if (schoolType.equals(Constants.SchoolType.SCHOOL_QINGNIAO)) {
                    //青鸟
                    schoolTypeAddress = Urls.BASEURL_QN;
                    sb.append(Urls.BASEURL_QN).append(Urls.URL_LOGIN)
                            .append("LoginName=").append(account)
                            .append("&pwd=").append(passWord);
                }
                //登录
                Login(sb.toString());
                break;
        }
    }

    //RadioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_loginDialog_direct:
                //直营,切换到直营时更新登录框
//                ToastUtil.showToast("直营");
                mLayoutDirect.setVisibility(View.VISIBLE);
                mLayoutFxOrQn.setVisibility(View.INVISIBLE);
                schoolType = Constants.SchoolType.SCHOOL_DIRECT;
                mEtJiHuoMa = dialog.findViewById(R.id.et_loginDialog_jiHuoMa);
                break;
            case R.id.rb_loginDialog_join:

                //附校,切换到附校时更新登录框
//                ToastUtil.showToast("附校");
                mLayoutDirect.setVisibility(View.INVISIBLE);
                mLayoutFxOrQn.setVisibility(View.VISIBLE);
                schoolType = Constants.SchoolType.SCHOOL_FUXIAO;
                mEtAccount = dialog.findViewById(R.id.et_loginDialog_userName);
                mEtPassWord = dialog.findViewById(R.id.et_loginDialog_password);
                break;
            case R.id.rb_loginDialog_qn:
                //青鸟,切换到青鸟时更新登录框
//                ToastUtil.showToast("青鸟");
                mLayoutDirect.setVisibility(View.INVISIBLE);
                mLayoutFxOrQn.setVisibility(View.VISIBLE);
                schoolType = Constants.SchoolType.SCHOOL_QINGNIAO;
                mEtAccount = dialog.findViewById(R.id.et_loginDialog_userName);
                mEtPassWord = dialog.findViewById(R.id.et_loginDialog_password);
                break;
        }
    }

    /**
     * 自动登录
     */
    public void autoLogin() {
        schoolTypeAddress = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_ADDRESS, "");
        if (!"".equals(schoolTypeAddress)) {
            schoolType = SPUtils.getString(Global.getInstance(), Constants.Share.SCHOOL_TYPE, "");
            if ("".equals(schoolType)) {
                ToastUtil.getInstance().showToast("上次登录未成功保存地址");
                return;
            }
            final StringBuilder sb = new StringBuilder();
            switch (schoolType) {
                //判断品牌
                case Constants.SchoolType.SCHOOL_DIRECT:
                    //直营
                    jiHuoMa = SPUtils.getString(Global.getInstance(), Constants.Share.LOGIN_JIHUOMA, "");
                    if ("".equals(jiHuoMa)) {
                        ToastUtil.getInstance().showToast("上次登录未成功保存激活码");
                        return;
                    }
                    sb.append(schoolTypeAddress).append(Urls.URL_LOGIN)
                            .append("LoginName=").append(jiHuoMa);
                    //登录

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2018/7/10 延迟一秒防止dialog 显示失败
                            Login(sb.toString());
                        }
                    },1000);
                    break;
                case Constants.SchoolType.SCHOOL_FUXIAO:
                case Constants.SchoolType.SCHOOL_QINGNIAO:
                    //附校 或者青鸟
                    account = SPUtils.getString(Global.getInstance(), Constants.Share.LOGIN_ACCOUNT, "");
                    passWord = SPUtils.getString(Global.getInstance(), Constants.Share.lOGIN_PASSWORD, "");
                    if ("".equals(account) | "".equals(passWord)) {
                        ToastUtil.getInstance().showToast("上次登录未成功保存账号密码");
                        return;
                    }
                    sb.append(schoolTypeAddress).append(Urls.URL_LOGIN)
                            .append("LoginName=").append(account)
                            .append("&pwd=").append(passWord);
                    //登录
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2018/7/10 延迟一秒防止dialog 显示失败
                            Login(sb.toString());
                        }
                    },1000);
                    break;
            }
        }
    }

    /**
     * 登录，并保存数据
     *
     * @param url 登录地址
     */
    public void Login(final String url) {
        LogUtils.i("Login", url);
        if (!HttpUtils.isNetWorkConn(Global.getInstance())) {
            ToastUtil.getInstance().showToast("当前网络未连接");
            //生成dialog
            showDiaLog(R.layout.layout_dialog_loading);
            changeDiaLogContent(Constants.LoadingType.INTERNET_ERROR);
            dismissDiaLog(1000);
            return;
        }
        //生成dialog
        showDiaLog(R.layout.layout_dialog_loading);
        changeDiaLogContent(Constants.LoadingType.LOGIN_LOADING);
        ToastUtil.getInstance().showToast("登录中...");
        //删除数据库，然后下载数据
        LiteOrmDBUtil.deleteAll(UserInfo.class);
        OkHttpHelper.getInstance().doGet(url, new OkHttpHelper.NetResultCallback() {
            @Override
            public void onFail(Object o) {
                ToastUtil.getInstance().showToast("登录失败");
                changeDiaLogContent(Constants.LoadingType.LOGIN_ERROR);
                dismissDiaLog(1000);
            }

            @Override
            public void onSuccess(String jsonString) {
                //请求成功
                JSONObject jsonObject = null;
                String er = "";
                LogUtils.i("DDDDD",jsonString);
                try {
                    jsonObject = new JSONObject(jsonString);
                    er = jsonObject.optString("error");
                } catch (JSONException e) {
                    e.printStackTrace();
                    changeDiaLogContent(Constants.LoadingType.LOGIN_ERROR);
                    dismissDiaLog(1000);
                    ToastUtil.getInstance().showToast("登录失败");

                }
                if ("".equals(er)) {
                    LoginBean loginBean = JsonUtil.json2Bean(jsonString, LoginBean.class);
                    if (loginBean == null) {
                        ToastUtil.getInstance().showToast("登录失败,请尝试重新登录");
                        dismissDiaLog(0);
                        return;
                    }
                    //登录成功
                    LogUtils.i("json", jsonString);
                    ToastUtil.getInstance().showToast("登录成功");
                    //登陆成功，关闭dialog
                    changeDiaLogContent(Constants.LoadingType.LOGIN_SUCCESS);
                    //登录成功,保存学校类型，保存品牌根地址，然后进行view数据填充,并且更新数据到本地
                    SPUtils.putString(Global.getInstance(), Constants.Share.SCHOOL_TYPE, schoolType);
                    SPUtils.putString(Global.getInstance(), Constants.Share.SCHOOL_ADDRESS, schoolTypeAddress);
                    if (schoolType.equals(Constants.SchoolType.SCHOOL_DIRECT)) {
                        //直营,保存激活码
                        SPUtils.putString(Global.getInstance(), Constants.Share.LOGIN_JIHUOMA, jiHuoMa);
                        //同时更新banner
                        String zyBannerUrl_Check = Urls.URL_BANNER_ZY_CHECK + "cid=" + loginBean.getMember_Uid();
                        String zyBannerUrl = Urls.URL_BANNER_ZY + "cid=" + loginBean.getMember_Uid();
                        updataBanner(zyBannerUrl_Check, zyBannerUrl);
                    } else if (schoolType.equals(Constants.SchoolType.SCHOOL_FUXIAO)) {
                        //附校或青鸟,保存账号密码
                        SPUtils.putString(Global.getInstance(), Constants.Share.LOGIN_ACCOUNT, account);
                        SPUtils.putString(Global.getInstance(), Constants.Share.lOGIN_PASSWORD, passWord);
                        //同时更新banner
                        String fxBannerUrl_Check = Urls.URL_BANNER_FX_CHECK + "cid=" + loginBean.getMember_Uid();
                        String fxBannerUrl = Urls.URL_BANNER_FX + "cid=" + loginBean.getMember_Uid();
                       Log.i("video","cid--->"+loginBean.getMember_Uid());

                        updataBanner(fxBannerUrl_Check, fxBannerUrl);
                    } else if (schoolType.equals(Constants.SchoolType.SCHOOL_QINGNIAO)) {
                        //青鸟,保存账号密码
                        SPUtils.putString(Global.getInstance(), Constants.Share.LOGIN_ACCOUNT, account);
                        SPUtils.putString(Global.getInstance(), Constants.Share.lOGIN_PASSWORD, passWord);
                        //同时更新banner
                        String qnBannerUrl_Check = Urls.URL_BANNER_QN_CHECK + "cid=" + loginBean.getMember_Uid();
                        String qnBannerUrl = Urls.URL_BANNER_QN + "cid=" + loginBean.getMember_Uid();
                        updataBanner(qnBannerUrl_Check, qnBannerUrl);
                    }
                    //保存校园信息
                    SPUtils.putString(Global.getInstance(), Constants.Share.MEMBERUID, loginBean.getMember_Uid());
                    SPUtils.putString(Global.getInstance(), Constants.Share.COMPANYUID, loginBean.getCompany_Uid());
                    SPUtils.putString(Global.getInstance(), Constants.Share.COMPANYNAME, loginBean.getCompany_Name());
                    SPUtils.putString(Global.getInstance(), Constants.Share.CAOMPANYDESC, loginBean.getCompany_Desc());
                    SPUtils.putString(Global.getInstance(), Constants.Share.ABOUTUS, loginBean.getAboutus());
                    LogUtils.i("Aboutus", loginBean.getAboutus());
                    LogUtils.i("Aboutus", url);
                    mTvIntroContent.setText("\t\t" + loginBean.getAboutus());
//                        mStTntroduction.setText("\t\t" + loginBean.getAboutus());
                    mTvschoolnewsContent.setText("\t\t" + loginBean.getCompany_Name());
//                        mStCshoolNews.setText("\t\t" + loginBean.getCompany_Desc());
                    //同时更新数据
                    StringBuilder sb = new StringBuilder();
                    sb.append(schoolTypeAddress).append(Urls.URL_UPDATA)
                            .append("memberUid=").append(loginBean.getMember_Uid());
                    upData(sb.toString());
                } else {
                    dismissDiaLog(0);
                    loginShow();
                    ToastUtil.getInstance().showToast("激活码或用户名或密码错误");
                }

            }
        });
    }

    /**
     * 每一个小时启动一次上传刷卡任务
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情

            if (upDataIbinder!=null){
                upDataIbinder.startUpData();
                //启动成功
                mHandler.postDelayed(this, 300* 1000);
            }

        }
    };

    /**
     * 更新banner
     *
     * @param checkUrl 检测类型url
     * @param trueUrl  真实trueUrl
     */
    public void updataBanner(final String checkUrl, final String trueUrl) {
        LogUtils.i("url", "checkUrl" + checkUrl);
        LogUtils.i("url", "trueUrl" + trueUrl);
        if (!HttpUtils.isNetWorkConn(Global.getInstance())) {
            ToastUtil.getInstance().showToast("当前网络未连接");
            showDiaLog(R.layout.layout_dialog_loading);
            changeDiaLogContent(Constants.LoadingType.INTERNET_ERROR);
            dismissDiaLog(1000);
            return;
        }
        OkHttpHelper.getInstance().doGet(checkUrl, new OkHttpHelper.NetResultCallback() {
            @Override
            public void onFail(Object o) {
                ToastUtil.getInstance().showToast("广告数据请求失败");
                changeDiaLogContent(Constants.LoadingType.LOGIN_ERROR);
                dismissDiaLog(1000);
            }

            @Override
            public void onSuccess(String jsonString) {
                BannerBean bannerBean = JsonUtil.json2Bean(jsonString, BannerBean.class);
                if (bannerBean == null) {
                    ToastUtil.getInstance().showToast("广告获取失败");
                    dismissDiaLog(0);
                    bannerView.setVisibility(View.VISIBLE);
                    bannerVideo.setVisibility(View.INVISIBLE);
                    return;
                }
                if (bannerBean.getStatus() == 0) {
                    Log.i("video","json--->"+jsonString);
                    //请求成功
                    if (bannerBean.getType().equals("web")) {
                        //web
                        bannerView.loadUrl(trueUrl);
                        bannerView.setVisibility(View.VISIBLE);
                        bannerVideo.setVisibility(View.INVISIBLE);
                    } else {
                        //视频
                        bannerVideo.setVisibility(View.VISIBLE);
                        bannerView.setVisibility(View.INVISIBLE);
                        Uri uri = Uri.parse(bannerBean.getUrl());
                        bannerVideo.setMediaController(new MediaController(MainActivity.this));
                        bannerVideo.setOnCompletionListener(new MyPlayerOnCompletionListener());
                        bannerVideo.setVideoURI(uri);
                        bannerVideo.start();

                    }
                } else {
                    //请求失败
                    bannerView.loadUrl(Urls.URL_LOCALBANNER);
                    ToastUtil.getInstance().showToast("广告数据请求失败");
                    changeDiaLogContent(Constants.LoadingType.LOGIN_ERROR);
                    dismissDiaLog(1000);
                }
            }
        });


    }

    //播放视频监听
    public class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
//            ToastUtil.getInstance().showToast("播放完成了");
            mp.start();
            mp.setLooping(true);
        }
    }

    /**
     * 更新下载数据
     *
     * @param url 下载地址
     */
    public void upData(String url) {
        LogUtils.i("upData", url);
        if (!HttpUtils.isNetWorkConn(Global.getInstance())) {
            ToastUtil.getInstance().showToast("当前网络未连接");
            showDiaLog(R.layout.layout_dialog_loading);
            changeDiaLogContent(Constants.LoadingType.INTERNET_ERROR);
            dismissDiaLog(1000);
            return;
        }
        //显示dialog
        showDiaLog(R.layout.layout_dialog_loading);
        changeDiaLogContent(Constants.LoadingType.UPDATA_LOADING);
        ToastUtil.getInstance().showToast("数据同步中...");
        OkHttpHelper.getInstance().doGet(url, new OkHttpHelper.NetResultCallback() {
            @Override
            public void onFail(Object o) {
                ToastUtil.getInstance().showToast("同步数据失败");
                changeDiaLogContent(Constants.LoadingType.UPDATA_ERROR);
                dismissDiaLog(1000);
            }

            @Override
            public void onSuccess(String jsonString) {
                //删除之前的数据
                LiteOrmDBUtil.deleteAll(UserInfo.class);
                //删除之前保存的图片
                ImageUtils.deleteAllFiles(new File(ImageUtils.getBaseUpDataImgUrl()));
                List<UserInfo> queryAll = LiteOrmDBUtil.getQueryAll(UserInfo.class);
                LogUtils.i("sssss", "queryAll:" + queryAll.size());
                String result = jsonString.replaceAll("\r", "");
                LogUtils.i("tag", result);
                final UserMsgBean userMsgBean = JsonUtil.json2Bean(result, UserMsgBean.class);
                if (userMsgBean == null) {
                    ToastUtil.getInstance().showToast("没有数据,请检查数据");
                    dismissDiaLog(0);
                    return;
                }
                LogUtils.i("tag", userMsgBean.getStudent().size() + userMsgBean.getTeacher().size() + "");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //保存数据库
                        for (final UserMsgBean.StudentBean studentBean : userMsgBean.getStudent()) {
                            String realName = studentBean.getMember_Real_Name();
                            String icNumber = studentBean.getMember_Ic_Number();
                            String className = studentBean.getClass_Name();
                            String memberUid = studentBean.getMember_Uid();
                            final String imgName = System.currentTimeMillis() + ".png";//本地照片名称
                            String identity = "学生";
                            UserInfo userInfo = null;
                            if (!"".equals(studentBean.getMember_Head_Img())) {
                                userInfo = new UserInfo(realName, memberUid, icNumber, className, identity, imgName);
                            } else {
                                userInfo = new UserInfo(realName, memberUid, icNumber, className, identity, "");
                            }

                            LiteOrmDBUtil.insert(userInfo);
                            if (!"".equals(studentBean.getMember_Head_Img())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //下载图片任务
                                        Glide.with(Global.getInstance()).load(studentBean.getMember_Head_Img()).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
                                            @Override
                                            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                                                try {
                                                    ImageUtils.savaBitmap(imgName, bytes);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                });

                            }

                        }
                        for (final UserMsgBean.TeacherBean teacherBean : userMsgBean.getTeacher()) {
                            String realName = teacherBean.getMember_Real_Name();
                            String icNumber = teacherBean.getMember_Ic_Number();
                            String className = teacherBean.getClass_Name();
                            String memberUid = teacherBean.getMember_Uid();
                            final String imgName = System.currentTimeMillis() + ".png";//本地照片名称
                            String identity = "老师";
                            UserInfo userInfo = null;
                            if (!"".equals(teacherBean.getMember_Head_Img())) {
                                userInfo = new UserInfo(realName, memberUid, icNumber, className, identity, imgName);
                            } else {
                                userInfo = new UserInfo(realName, memberUid, icNumber, className, identity, "");
                            }
                            LiteOrmDBUtil.insert(userInfo);
                            if (!"".equals(teacherBean.getMember_Head_Img())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //下载图片任务
                                        Glide.with(Global.getInstance()).load(teacherBean.getMember_Head_Img()).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
                                            @Override
                                            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                                                try {
                                                    ImageUtils.savaBitmap(imgName, bytes);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                });

                            }
                        }
                        Message msg = Message.obtain();
                        msg.what = Constants.UPDATA_SUCCESS;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }


    // 相机回调接口
    private final class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            camera = Camera.open();
            if (camera != null) {
                camera.setDisplayOrientation(0);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Camera.Parameters parameters = camera.getParameters();
                List<int[]> range = parameters.getSupportedPreviewFpsRange();
                LogUtils.d(TAG, "range:" + range.size());
                for (int j = 0; j < range.size(); j++) {
                    int[] r = range.get(j);
                    for (int k = 0; k < r.length; k++) {
                        Log.d(TAG, TAG + r[k]);
                    }
                }
                //parameters.setPreviewFpsRange(8, 30);
                // 设置照片格式
                parameters.setPictureFormat(PixelFormat.JPEG);

//                List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
//                ToastUtil.showToast(supportedPictureSizes.size() + "");
//                for (int i = 0; i < supportedPictureSizes.size(); i++) {
//                    LogUtils.i("size", "分辨率" + supportedPictureSizes.get(i).height
//                            + "," + supportedPictureSizes.get(i).width);
//                }
//                //parameters.set("jpeg-quality", 85);
                // 设置拍摄照片的实际分辨率，本例中的分辨率是1024×768
                parameters.setPictureSize(800, 480);
//                parameters.setPictureSize(640, 480);
                // 设置保存的图像大小
                camera.setParameters(parameters);

                try {
                    // 设置用于显示拍照影像的SurfaceHolder对象
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    camera.startPreview();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgReceiver(BaseEvent msg) {
        //默认代表改方法在发送的线程中 ThreadMode.MAIN代表改方法运行于UI主线程
        if (msg == null) {
            return;
        }
        switch (msg.getType()) {
            case BaseEvent.Type.UPDATA_COMPLETE:
                //更新页面
                LogUtils.i("tag", (String) msg.getT());
                tvQuality.setText("还有" + String.valueOf(msg.getT()) + "条尚未上传");
                break;
            case BaseEvent.Type.WIFI_STRENGTH:
                //更新wifi图标
                int strength = (int) msg.getT();
//                Toast.makeText(Global.getInstance(), "网络状态强度：" + strength, Toast.LENGTH_SHORT).show();
                mImageWifi.setImageResource(wifiStateImgs[strength]);
                if (upDataIbinder != null) {
                    upDataIbinder.startUpData();
                }

                break;
            case BaseEvent.Type.LINENET_ISAVALIABLE:
                //以太网是否可用
                boolean isAvaliable = (boolean) msg.getT();
                if (isAvaliable) {
                    mImageLineNet.setImageResource(R.mipmap.iv_netline_true);
                    if (upDataIbinder!=null){
                        upDataIbinder.startUpData();
                    }

                } else {
                    mImageLineNet.setImageResource(R.mipmap.iv_netline_false);
                }
                break;
            case BaseEvent.Type.REQUEST_GETFCOUS:
                //让edittext重新获取焦点
                LogUtils.i("ssss", "让edittext重新获取焦点");
                etIdCard.requestFocusFromTouch();
                break;
        }
    }

    /**
     * 显示操作列表
     */
    public void showOperations() {
        mInflater = LayoutInflater.from(this);
        popupView = mInflater.inflate(R.layout.operations_main, null);
        mPopupWindow = new PopupWindow(popupView, 105,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(tvOperation);
        tvLogin = popupView.findViewById(R.id.tv_operation_login);
        tvUpData = popupView.findViewById(R.id.tv_operation_updata);
        tvDataList = popupView.findViewById(R.id.tv_operation_dataList);
        tvLogin.setOnClickListener(this);
        tvUpData.setOnClickListener(this);
        tvDataList.setOnClickListener(this);
    }

    /**
     * 关闭popupwindow
     */
    public void closePopupwindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    private TakePictureCallback mTakePictureCallback = new TakePictureCallback();

    /**
     * 该方法用于处理拍摄后的照片数据
     */
    private final class TakePictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mAudioManager.setRingerMode(mAudioManager.getRingerMode());
            // data参数值就是照片数据，将这些数据以key-value形式保存，以便其他调用该Activity的程序可以获得照片数据
            try {
                Matrix matrix = new Matrix();
                matrix.reset();
                matrix.setRotate(0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                //保存bitmap
                String fileName = ImageUtils.getTempFileName();//当前时间命名
                String basicPath = SDcardUtils.isExist(SDcardUtils.getSDPath() + "/SignInPhoto/");
                String filePath = basicPath + fileName + ".jpg";
                ImageUtils.saveImageToSD(Global.getInstance(), filePath, bitmap, 60);
                // 停止照片拍摄
                camera.stopPreview();
                camera.startPreview();
                //显示图片
//                Bitmap b = BitmapFillet.fillet(BitmapFillet.ALL, bitmap, 40);
//                picture.setImageBitmap(b);
                //五秒后关闭dialog
//                mHandler.sendEmptyMessageDelayed(Constants.Dialog.CLOSE, 5000);
                //存储信息，后台开启上传任务
//                String memberUid = SPUtils.getString(Global.getInstance(), Constants.Share.MEMBERUID, "");
                String dateTime = TimeUtil.getCurrentDay2();
                String type = identity;
                UpDataInfo upDataInfo = new UpDataInfo(card, dateTime, memberUid, filePath, type);
                LiteOrmDBUtil.insert(upDataInfo);
                //开启任务
                if (upDataIbinder!=null){
                    upDataIbinder.startUpData();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }


    //绑定服务
    private void bindService() {
        intent = new Intent(this, UpDataService.class);
        //混合调用  
        //把服务所在进程变成服务进程  
        startService(intent);
        //为了拿到中间人对象  
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    //注销服务
    private void unBindService() {
        unbindService(mServiceConnection);
        stopService(intent);
    }

    //注册wifi监听广播
    private void registReceiver() {
        if (mWifiAndLineNetBroadcastReceiver != null)
            return;
        mWifiAndLineNetBroadcastReceiver = new WifiAndLineNetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NETWORK_STATE_CHANGE);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(mWifiAndLineNetBroadcastReceiver, filter);
    }

    //注销wifi监听广播
    private void unregisterReceiver() {
        if (mWifiAndLineNetBroadcastReceiver == null)
            return;
        unregisterReceiver(mWifiAndLineNetBroadcastReceiver);
        mWifiAndLineNetBroadcastReceiver = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unBindService();
        unregisterReceiver();
        mHandler.removeCallbacks(runnable);
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (bannerVideo != null) {
            bannerVideo = null;
        }
    }
}
