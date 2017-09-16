package com.lvqingyang.callenterprise.Task;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvqingyang.callenterprise.Base.BaseFragment;
import com.lvqingyang.callenterprise.BuildConfig;
import com.lvqingyang.callenterprise.R;
import com.lvqingyang.callenterprise.bean.Order;
import com.lvqingyang.callenterprise.bean.User;
import com.lvqingyang.callenterprise.net.RequestHelper;
import com.lvqingyang.callenterprise.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

import frame.tool.MyPrefence;
import frame.tool.MyToast;
import frame.tool.SolidRVBaseAdapter;
import frame.view.CircleTextView;

/**
 * Author：LvQingYang
 * Date：2017/9/1
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class TaskFragment extends BaseFragment {

    private Toolbar toolbar;
    private android.support.v7.widget.RecyclerView rvtask;
    private android.support.v4.widget.SwipeRefreshLayout srl;
    private android.support.design.widget.FloatingActionButton fab;
    private List<Order> mOrderList=new ArrayList<>();
    private SolidRVBaseAdapter mAdapter;
    private String[] mTypeArr;
    private Gson mGson=new Gson();
    private static final String TAG = "TaskFragment";
    private MyPrefence ymMyPrefence;
    private static final int REQUEST_ADD = 785;

    public static int[] mColorArr=new int[]{
            R.color.accent_deep_purple,
            R.color.accent_indago,
            R.color.accent_teal,
            R.color.accent_green,
            R.color.accent_orange,
            R.color.accent_brown,
    };

    public static TaskFragment newInstance() {

        Bundle args = new Bundle();

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_task,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        this.fab = (FloatingActionButton) view.findViewById(R.id.fab);
        this.srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        this.rvtask = (RecyclerView) view.findViewById(R.id.rv_task);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbar(toolbar,getString(R.string.task_list),false);
    }

    @Override
    protected void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(AddTaskActivity.newIntent(getContext()),REQUEST_ADD);
            }
        });

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

        mAdapter=new SolidRVBaseAdapter<Order>(getContext(), mOrderList) {
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
                    case 1:
                        showRrePayDialog(bean);
                        break;
                    case 3:
                        MyToast.info(getContext(), R.string.go_to_pay_end,Toast.LENGTH_SHORT);
                        break;
                }
            }
        };
    }

    @Override
    protected void setData() {
        rvtask.setLayoutManager(new LinearLayoutManager(getContext()));
        rvtask.setAdapter(mAdapter);

        srl.setRefreshing(true);
        getTasks();
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    private void getTasks(){
        RequestHelper.getTasks(getActivity(), MyPrefence.getInstance(getContext()).getUser(User.class).getEnterID()+"", new RequestListener() {
            @Override
            public void onResponse(String res) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                srl.setRefreshing(false);
                mOrderList=mGson.fromJson(res, new TypeToken<List<Order>>() {}.getType());
                mAdapter.clearAllItems();
                mAdapter.addItems(mOrderList);
            }

            @Override
            public void onError() {
                srl.setRefreshing(false);
                MyToast.error(getContext(), R.string.load_error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void showRrePayDialog(final Order order){
        final float money=((int)(order.getPhoneCount()*order.getUnitPrice()*40))/100f;
        new AlertDialog.Builder(getContext())
                .setTitle(order.getOrderName())
                .setMessage("是否支付预付款："+money+"元?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyToast.loading(getContext(), R.string.paying);
                        RequestHelper.prePay(getActivity(), order.getOrderID(), new RequestListener() {
                            @Override
                            public void onResponse(String res) {
                                MyToast.cancel();
                                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                                if (res.equals("1")) {
                                    MyToast.success(getContext(), R.string.repay_succ);
                                    getTasks();
                                }else if (res.equals("0")) {
                                    MyToast.info(getContext(), R.string.balance_not_enough);
                                }else{
                                    MyToast.error(getContext(), R.string.repay_fail);
                                }
                            }

                            @Override
                            public void onError() {
                                MyToast.cancel();
                                MyToast.error(getContext(), R.string.load_error);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_ADD) {
            if (resultCode== Activity.RESULT_OK) {
                getTasks();
            }
        }
    }
}
