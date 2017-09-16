package com.lvqingyang.callenterprise.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lvqingyang.callenterprise.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import frame.tool.Md5Util;
import frame.tool.MyOkHttp;
import frame.tool.MyToast;
import frame.tool.NetWorkUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 　　┏┓　　  ┏┓+ +
 * 　┏┛┻━ ━ ━┛┻┓ + +
 * 　┃　　　             ┃
 * 　┃　　　━　　   ┃ ++ + + +
 * ████━████     ┃+
 * 　┃　　　　　　  ┃ +
 * 　┃　　　┻　　  ┃
 * 　┃　　　　　　  ┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug！
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 * ━━━━━━神兽出没━━━━━━
 * Author：LvQingYang
 * Date：2017/6/12
 * Email：biloba12345@gamil.com
 * Info：
 */

public class RequestHelper {
    private static final String key="wust_cloud_call";
    private static final String KEY_API = "http://call.zeblog.cn/api/Android/";
    private static final String API_GET_TASK = KEY_API+"GetOrdersByEnterID";
    private static final String API_LOGIN = KEY_API+"EnterpriseLogin";
    private static final String API_CREATE_ORDER = KEY_API+"CreateOrder";
    private static final String API_PRE_PAY = KEY_API+"PrePay";
    private static final String API_FINAL_PAY = KEY_API+"FinalPay";
    private static final String API_EXPRESS_CALL = KEY_API+"ExpressCall";
    private static final String API_RECALL = KEY_API+"ReCall";
    private static final String API_CHARGE_BALANGE = KEY_API+"ChargeBalance";
    private static final String API_GET_FINAL_PAY_MONEY = KEY_API+"GetFinalPayRate";

    private static final String TAG = "RequestHelper";

    private static void getResult(Context c, StringBuilder sb, final RequestListener listener){
        if (NetWorkUtils.isNetworkConnected(c)) {
            //相关参数
            Date date=new Date();
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time=format.format(date);
            String chkvalue=key+time;
            chkvalue= Md5Util.MD5(chkvalue);
            chkvalue=chkvalue.substring(3).toLowerCase();

            final String url=sb.append("Time="+time).append("&Checkvalue="+chkvalue).toString();
            Log.d(TAG, "getResult: "+url);
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        subscriber.onNext(MyOkHttp.getInstance()
                                .run(url));
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            })
                    .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onNext(String response) {
                            listener.onResponse(response);
                        }

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            listener.onError();
                        }
                    });
        }else {
            MyToast.info(c, c.getString(R.string.check), Toast.LENGTH_SHORT);
        }
    }

    public static void getTasks(Context c, String id, RequestListener listener){
        StringBuilder sb=new StringBuilder(API_GET_TASK)
                .append("?EnterID="+id+"&");
        getResult(c,sb,listener );
    }

    public static void login(Context c, String email,String pwd, RequestListener listener){
        StringBuilder sb=new StringBuilder(API_LOGIN)
                .append("?Email="+email+"&")
                .append("Password="+pwd+"&");
        getResult(c,sb,listener );
    }

    public static void createOrder(Context c, String orderJson, RequestListener listener){
        StringBuilder sb=new StringBuilder(API_CREATE_ORDER)
                .append("?Json="+orderJson+"&");
        getResult(c,sb,listener );
    }

    public static void prePay(Context c,int orderId,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_PRE_PAY)
                .append("?OrderID="+orderId+"&");
        getResult(c,sb,listener );
    }

    public static void finalPay(Context c,int orderId,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_FINAL_PAY)
                .append("?OrderID="+orderId+"&");
        getResult(c,sb,listener );
    }

    public static void expressCall(Context c,int runningId,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_EXPRESS_CALL)
                .append("?RunningID="+runningId+"&");
        getResult(c,sb,listener );
    }

    public static void recall(Context c,int runningId,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_RECALL)
                .append("?RunningID="+runningId+"&");
        getResult(c,sb,listener );
    }

    public static void chargeBalance(Context c,int enterId,float money,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_CHARGE_BALANGE)
                .append("?EnterID="+enterId+"&")
                .append("Money=" +money+"&");
        getResult(c,sb,listener );
    }

    public static void getFinalPayRate(Context c,int orderId,RequestListener listener){
        StringBuilder sb=new StringBuilder(API_GET_FINAL_PAY_MONEY)
                .append("?OrderID="+orderId+"&");
        getResult(c,sb,listener );
    }
}
