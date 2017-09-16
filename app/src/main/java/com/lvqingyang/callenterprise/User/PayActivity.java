package com.lvqingyang.callenterprise.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvqingyang.callenterprise.BuildConfig;
import com.lvqingyang.callenterprise.R;
import com.lvqingyang.callenterprise.bean.Order;
import com.lvqingyang.callenterprise.bean.User;
import com.lvqingyang.callenterprise.net.RequestHelper;
import com.lvqingyang.callenterprise.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

import frame.base.BaseActivity;
import frame.tool.MyPrefence;
import frame.tool.MyToast;
import frame.tool.SolidRVBaseAdapter;
import frame.view.CircleTextView;

public class PayActivity extends BaseActivity {

    private android.support.v7.widget.RecyclerView rvtask;
    private android.support.v4.widget.SwipeRefreshLayout srl;
    private List<Order> mOrderList=new ArrayList<>();
    private SolidRVBaseAdapter mAdapter;
    private String[] mTypeArr;
    private Gson mGson=new Gson();
    private static final String TAG = "PayActivity";

    public static int[] mColorArr=new int[]{
            R.color.accent_deep_purple,
            R.color.accent_indago,
            R.color.accent_teal,
            R.color.accent_green,
            R.color.accent_orange,
            R.color.accent_brown,
    };

    public static void start(Context context) {
        Intent starter = new Intent(context, PayActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTasks();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        initeActionbar(R.string.count,true);
        this.srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        this.rvtask = (RecyclerView) findViewById(R.id.rv_task);
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasks();
            }
        });
    }

    @Override
    protected void initData() {
        mTypeArr=getResources().getStringArray(R.array.type_task);

        mAdapter=new SolidRVBaseAdapter<Order>(this, mOrderList) {
            @Override
            protected void onBindDataToView(SolidCommonViewHolder holder, Order bean) {
                ((CircleTextView)holder.getView(R.id.ctv_type))
                        .text(mTypeArr[bean.getOrderType()-1])
                        .bgColor(getResources().getColor(mColorArr[bean.getOrderType()-1]))
                        .paint();
                holder.setText(R.id.tv_name,bean.getOrderName());
                holder.setText(R.id.tv_total,bean.getPhoneCount()+"");
                holder.setText(R.id.tv_price,bean.getUnitPrice()+"");
                int called=0;
                if (bean.getCallTasks() != null) {
                    for (Order.CallTasksBean callTasksBean : bean.getCallTasks()) {
                        if (callTasksBean.getCallResult().getResultType()==2) {
                            called++;
                        }
                    }
                }
                holder.setText(R.id.tv_complete,called+"");

                TextView tvType=holder.getView(R.id.tv_state);
                ((ProgressBar)holder.getView(R.id.pb)).setProgress((int) (called*1f/bean.getPhoneCount()*100));
                switch (bean.getState()) {
                    case -1:
                        tvType.setText("审核拒绝");
                        tvType.setBackgroundResource(R.drawable.bg_task_refuse);
                        break;
                    case 0:
                        tvType.setText("待审核");
                        tvType.setBackgroundResource(R.drawable.bg_task_uncheck);
                        break;
                    case 1:
                        tvType.setText("待预付");
                        tvType.setBackgroundResource(R.drawable.bg_task_advance);
                        break;
                    case 2:
                        tvType.setText("进行中");
                        tvType.setBackgroundResource(R.drawable.bg_task_doing);
                        break;
                    case 3:
                        tvType.setText("待尾款");
                        tvType.setBackgroundResource(R.drawable.bg_task_end);
                        break;
                    case 4:
                        tvType.setText("完成");
                        tvType.setBackgroundResource(R.drawable.bg_task_complete);
                        break;
                }
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_task;
            }

            @Override
            protected void onItemClick(int position, Order bean) {
                super.onItemClick(position, bean);
                if (BuildConfig.DEBUG) Log.d(TAG, "onItemClick: "+bean.getState());
                switch (bean.getState()) {
                    case 3:
                        showFinalPayDialog(bean);
                        break;
                }
            }
        };
    }

    @Override
    protected void setData() {
        rvtask.setLayoutManager(new LinearLayoutManager(this));
        rvtask.setAdapter(mAdapter);

        srl.setRefreshing(true);
        getTasks();
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[0];
    }

    private void getTasks(){
        RequestHelper.getTasks(this, MyPrefence.getInstance(this).getUser(User.class).getEnterID()+"", new RequestListener() {
            @Override
            public void onResponse(String res) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                srl.setRefreshing(false);
                mOrderList.clear();
                List<Order> orderList=mGson.fromJson(res, new TypeToken<List<Order>>() {}.getType());
                for (Order order : orderList) {
                    if (order.getState()==3) {
                        mOrderList.add(order);
                    }
                }
                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+mOrderList.size());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                srl.setRefreshing(false);
                MyToast.error(PayActivity.this, R.string.load_error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void showFinalPayDialog(final Order order){
        if (order.isExpress()) {
            RequestHelper.getFinalPayRate(PayActivity.this, order.getOrderID(), new RequestListener() {
                @Override
                public void onResponse(String res) {
                    MyToast.cancel();
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                    float rate=Float.parseFloat(res);
                    showDialog(order, rate);
                }

                @Override
                public void onError() {
                    MyToast.cancel();
                    MyToast.error(PayActivity.this, R.string.load_error);
                }
            });
        }else {
            showDialog(order, 1);
        }
    }

    private void showDialog(final Order order, float rate){
        final float finalPay=(int) (order.getPhoneCount()*order.getUnitPrice()*(order.isExpress()?rate:1)*100)/100f;
        new AlertDialog.Builder(PayActivity.this)
                .setTitle(order.getOrderName())
                .setMessage("呼叫单价："+order.getUnitPrice()
                        +"\n呼叫数量："+order.getPhoneCount()
                        +"\n已预付："+order.getPrePay()
                        +(order.isExpress()?"是否加急：是"+"\n（加急倍率）："+rate:"")
                        +"\n本次尾款："+(finalPay-order.getPrePay()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyToast.loading(PayActivity.this, R.string.paying);

                        RequestHelper.finalPay(PayActivity.this, order.getOrderID(), new RequestListener() {
                            @Override
                            public void onResponse(String res) {
                                MyToast.cancel();
                                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                                if (res.equals("1")) {
                                    MyToast.success(PayActivity.this, R.string.final_pay_succ);
                                    getTasks();
                                }else if (res.equals("0")) {
                                    MyToast.info(PayActivity.this, R.string.balance_not_enough);
                                }else{
                                    MyToast.error(PayActivity.this, R.string.final_pay_fail);
                                }
                            }

                            @Override
                            public void onError() {
                                MyToast.cancel();
                                MyToast.error(PayActivity.this, R.string.load_error);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create()
                .show();
    }


}
