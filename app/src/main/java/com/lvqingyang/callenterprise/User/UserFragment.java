package com.lvqingyang.callenterprise.User;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lvqingyang.callenterprise.Base.BaseFragment;
import com.lvqingyang.callenterprise.BuildConfig;
import com.lvqingyang.callenterprise.Login.LoginActivity;
import com.lvqingyang.callenterprise.R;
import com.lvqingyang.callenterprise.bean.User;
import com.lvqingyang.callenterprise.net.RequestHelper;
import com.lvqingyang.callenterprise.net.RequestListener;
import com.lvqingyang.callenterprise.view.CardItem;

import de.hdodenhof.circleimageview.CircleImageView;
import frame.tool.MyPrefence;
import frame.tool.MyToast;

/**
 * Author：LvQingYang
 * Date：2017/9/1
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class UserFragment extends BaseFragment {

    private android.support.v7.widget.Toolbar toolbar;
    private de.hdodenhof.circleimageview.CircleImageView civhead;
    private android.widget.TextView tvusername;
    private android.widget.TextView tvposition;
    private CardItem mCiWallet;
    private CardItem mCiPay;
    private User user;
    private static final String TAG = "UserFragment";
    private Gson mGson=new Gson();
    private CardItem mCiExit;

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        showUserInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden&&mCiPay!=null) {
            showUserInfo();
        }
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_uesr,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        this.tvposition = (TextView) view.findViewById(R.id.tv_position);
        this.tvusername = (TextView) view.findViewById(R.id.tv_username);
        this.civhead = (CircleImageView) view.findViewById(R.id.civ_head);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mCiWallet = (CardItem) view.findViewById(R.id.ci_wallet);
        mCiPay = (CardItem) view.findViewById(R.id.ci_pay);
        mCiExit = (CardItem) view.findViewById(R.id.ci_exit);
        initToolbar(toolbar,getString(R.string.account_info),false);
    }

    @Override
    protected void setListener() {
        mCiPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayActivity.start(getContext());
            }
        });

        mCiWallet.setOnClickListener(new View.OnClickListener() {
            private android.widget.Button btn;
            private android.widget.EditText et;

            @Override
            public void onClick(View view) {
                View view1=getActivity().getLayoutInflater()
                        .inflate(R.layout.dialog_charge,null);
                this.et = (EditText) view1.findViewById(R.id.et);
                final AlertDialog alertDialog=new AlertDialog.Builder(getContext())
                        .setView(view1)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Float money=((int)Float.parseFloat(et.getText().toString())*100)/100f;
                                if (money>=100&&money<=50000) {
                                    RequestHelper.chargeBalance(getActivity(), user.getEnterID(), money, new RequestListener() {
                                        @Override
                                        public void onResponse(String res) {
                                            if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                                            if (res.equals("1")) {
                                                MyToast.success(getContext(), R.string.charge_succ);
                                                showUserInfo();
                                            }else {
                                                MyToast.error(getContext(), R.string.charge_fail);
                                            }
                                        }

                                        @Override
                                        public void onError() {
                                            MyToast.error(getActivity(), R.string.load_error);
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel,null)
                        .create();
                alertDialog.show();
            }
        });

        mCiExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPrefence.getInstance(getActivity()).logOut();
                LoginActivity.start(getActivity());
                getActivity().finish();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setData() {
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.item_exit) {
            MyPrefence.getInstance(getActivity()).logOut();
            LoginActivity.start(getActivity());
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUserInfo(){
        user= MyPrefence.getInstance(getContext()).getUser(User.class);
        RequestHelper.login(getActivity(), user.getEmail(), user.getPassword(), new RequestListener() {
            @Override
            public void onResponse(String res) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                user=mGson.fromJson(res, User.class);
                tvusername.setText(user.getEnterName());
                tvposition.setText(user.getEmail());
                mCiWallet.setHint(((int)user.getBalance()*100)/100f+"");
                MyPrefence.getInstance(getContext()).saveUser(user);
            }

            @Override
            public void onError() {

            }
        });


    }
}
