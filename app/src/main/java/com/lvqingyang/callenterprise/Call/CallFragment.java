package com.lvqingyang.callenterprise.Call;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
public class CallFragment extends BaseFragment {

    private Toolbar toolbar;
    private android.support.v7.widget.RecyclerView rvcall;
    private android.support.v4.widget.SwipeRefreshLayout srl;
    private List<Order> mOrderList=new ArrayList<>();
    private List<Order.CallTasksBean> mCallTaskList=new ArrayList<>();
    private SolidRVBaseAdapter mAdapter;
    private static final String TAG = "CallFragment";
    private Gson mGson=new Gson();
    private android.widget.Spinner sp;
    private android.widget.EditText et;
    private ImageView ivsearch;
    private List<String> mTasks=new ArrayList<>();
    private ArrayAdapter mSpAdapter;
    private MediaPlayer mMediaPlayer;

    public static CallFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CallFragment fragment = new CallFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_call,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        this.srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        this.rvcall = (RecyclerView) view.findViewById(R.id.rv_call);
        this.toolbar =  view.findViewById(R.id.toolbar);
        initToolbar(toolbar, getString(R.string.serarch_call),false);

        this.ivsearch = (ImageView) view.findViewById(R.id.iv_search);
        this.et = (EditText) view.findViewById(R.id.et);
        this.sp = (Spinner) view.findViewById(R.id.sp);
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCalls();
            }
        });

        ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText=et.getText().toString();
                List<Order.CallTasksBean> list=new ArrayList<Order.CallTasksBean>();
                if (sp.getSelectedItemPosition()==0) {
                    if (TextUtils.isEmpty(searchText)) {
                        for (Order order : mOrderList) {
                            list.addAll(order.getCallTasks());
                        }
                    }else {
                        for (Order order : mOrderList) {
                            for (Order.CallTasksBean callTasksBean : order.getCallTasks()) {
                                if (isContainKey(callTasksBean, searchText)) {
                                    list.add(callTasksBean);
                                }
                            }
                        }
                    }
                }else {
                    String task=mTasks.get(sp.getSelectedItemPosition());
                    if (TextUtils.isEmpty(searchText)) {
                        for (Order order : mOrderList) {
                            if (order.getOrderName().equals(task)) {
                                list.addAll(order.getCallTasks());
                            }
                        }
                    }else {
                        for (Order order : mOrderList) {
                            if (order.getOrderName().equals(task)) {
                                for (Order.CallTasksBean callTasksBean : order.getCallTasks()) {
                                    if (isContainKey(callTasksBean, searchText)) {
                                        list.add(callTasksBean);
                                    }
                                }
                            }
                        }
                    }
                }
                changeData(list);
            }
        });
    }

    @Override
    protected void initData() {
        mMediaPlayer= MediaPlayer.create(getActivity(), R.raw.call_default);
        mAdapter=new SolidRVBaseAdapter<Order.CallTasksBean>(getContext(), mCallTaskList) {
            @Override
            protected void onBindDataToView(SolidCommonViewHolder holder, Order.CallTasksBean bean) {
                ((CircleTextView)holder.getView(R.id.ctv_name)).setText(bean.getName());
                holder.setText(R.id.tv_tel,bean.getCallNo());
                holder.setText(R.id.tv_hint,bean.getCallResult().getNote());

                TextView tvState=holder.getView(R.id.tv_state);
                switch (bean.getCallResult().getResultType()) {
                    case -1:
                        tvState.setBackgroundResource(R.drawable.bg_task_refuse);
                        tvState.setText("失败");
                        holder.getView(R.id.iv_voice).setVisibility(View.VISIBLE);
                        break;
                    case 0:
                        tvState.setBackgroundResource(R.drawable.bg_task_uncheck);
                        tvState.setText("待接单");
                        holder.getView(R.id.iv_voice).setVisibility(View.GONE);
                        break;
                    case 1:
                        tvState.setBackgroundResource(R.drawable.bg_task_doing);
                        tvState.setText("进行中");
                        holder.getView(R.id.iv_voice).setVisibility(View.GONE);
                        break;
                    case 2:
                        tvState.setBackgroundResource(R.drawable.bg_task_complete);
                        tvState.setText("完成");
                        holder.getView(R.id.iv_voice).setVisibility(View.VISIBLE);
                        break;
                }

                holder.getView(R.id.iv_voice).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AnimationDrawable ad= (AnimationDrawable) ((ImageView)view).getDrawable();
                        if (mMediaPlayer.isPlaying()) {
                            if (ad.isRunning()) {
                                ad.stop();
                                mMediaPlayer.pause();
                                mMediaPlayer.seekTo(0);
                            } else{
                                for (int i = 0; i < rvcall.getChildCount(); i++) {
                                    ((AnimationDrawable) (((ImageView) rvcall.getChildAt(i).findViewById(R.id.iv_voice)).getDrawable())).stop();
                                }
                                ad.start();
                                mMediaPlayer.seekTo(0);
                                mMediaPlayer.start();
                             }
                        }else {
                            ad.start();
                            mMediaPlayer.start();
                        }
                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                ad.stop();
                                mMediaPlayer.seekTo(0);
                            }
                        });
                    }
                });
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_call;
            }

            @Override
            protected void onItemClick(int position, Order.CallTasksBean bean) {
                super.onItemClick(position, bean);
                switch (bean.getCallResult().getResultType()) {
                    case -1:
                        showReCallDialog(position,bean);
                        break;
                    case 0:
                        showExpressDialog(bean);
                        break;
                    case 2:
                        showReCallDialog(position,bean);
                        break;
                }
            }
        };

        mSpAdapter=new ArrayAdapter<String>
                (getContext(), R.layout.item_sp, mTasks);
    }

    @Override
    protected void setData() {
        rvcall.setLayoutManager(new LinearLayoutManager(getContext()));
        rvcall.setAdapter(mAdapter);

        sp.setAdapter(mSpAdapter);

        srl.setRefreshing(true);
        getCalls();
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    private void getCalls(){
        RequestHelper.getTasks(getActivity(), MyPrefence.getInstance(getContext()).getUser(User.class).getEnterID()+"", new RequestListener() {
            @Override
            public void onResponse(String res) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                srl.setRefreshing(false);
                mOrderList=mGson.fromJson(res, new TypeToken<List<Order>>() {}.getType());
                mCallTaskList.clear();
                mTasks.clear();
                for (Order order : mOrderList) {
                    if (order.getState()>1) {
                        mCallTaskList.addAll(order.getCallTasks());
                        mTasks.add(order.getOrderName());
                    }else {
                        mCallTaskList.remove(order);
                    }

                }
                mTasks.add(0,"");
                mAdapter.notifyDataSetChanged();
                mSpAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                srl.setRefreshing(false);
                MyToast.error(getContext(), R.string.load_error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void changeData(List<Order.CallTasksBean> list){
        mCallTaskList.clear();
        mCallTaskList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isContainKey(Order.CallTasksBean callTasksBean,String key){
        return mGson.toJson(callTasksBean).contains(key);
    }

    private void showExpressDialog(final Order.CallTasksBean callTasksBean){
        new AlertDialog.Builder(getContext())
                .setTitle(callTasksBean.getCallNo())
                .setMessage("是否加急?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyToast.loading(getContext(), R.string.paying);
                        RequestHelper.expressCall(getActivity(), callTasksBean.getRunningID(), new RequestListener() {
                            @Override
                            public void onResponse(String res) {
                                MyToast.cancel();
                                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                                if (res.equals("1")) {
                                    MyToast.success(getContext(), R.string.express_succ);
                                }else{
                                    MyToast.error(getContext(), R.string.express_fail);
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

    private void showReCallDialog(final int pos, final Order.CallTasksBean callTasksBean){
        new AlertDialog.Builder(getContext())
                .setTitle(callTasksBean.getCallNo())
                .setMessage("是否重拨?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyToast.loading(getContext(), R.string.paying);
                        RequestHelper.recall(getActivity(), callTasksBean.getRunningID(), new RequestListener() {
                            @Override
                            public void onResponse(String res) {
                                MyToast.cancel();
                                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                                if (res.equals("1")) {
                                    MyToast.success(getContext(), R.string.recall_succ);
                                    callTasksBean.getCallResult().setResultType(0);
                                    mAdapter.updateItem(pos);
                                }else{
                                    MyToast.error(getContext(), R.string.recall_fail);
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
    public void onDestroyView() {
        super.onDestroyView();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer=null;
    }
}
