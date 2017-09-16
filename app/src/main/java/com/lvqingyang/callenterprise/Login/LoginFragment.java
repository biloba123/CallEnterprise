package com.lvqingyang.callenterprise.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lvqingyang.callenterprise.Base.BaseFragment;
import com.lvqingyang.callenterprise.BuildConfig;
import com.lvqingyang.callenterprise.MainActivity;
import com.lvqingyang.callenterprise.R;
import com.lvqingyang.callenterprise.bean.User;
import com.lvqingyang.callenterprise.net.RequestHelper;
import com.lvqingyang.callenterprise.net.RequestListener;

import frame.tool.MyPrefence;
import frame.tool.MyToast;

/**
 * Author：LvQingYang
 * Date：2017/9/1
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class LoginFragment extends BaseFragment {
    private android.widget.EditText etemail;
    private android.widget.EditText etpwd;
    private android.widget.Button btnlogin;
    private android.widget.TextView tvregister;
    private static final String TAG = "LoginFragment";
    private MyPrefence mMyPrefence;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        this.btnlogin = (Button) view.findViewById(R.id.btn_login);
        this.etpwd = (EditText) view.findViewById(R.id.et_pwd);
        this.etemail = (EditText) view.findViewById(R.id.et_email);
        this.tvregister = (TextView) view.findViewById(R.id.tv_register);
    }

    @Override
    protected void setListener() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                String email=etemail.getText().toString(),
                        pwd=etpwd.getText().toString();
                MyToast.loading(getContext(), R.string.logining);
                RequestHelper.login(getActivity(), email, pwd, new RequestListener() {
                    @Override
                    public void onResponse(String res) {
                        MyToast.cancel();
                        if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                        Gson gson=new Gson();

                        try {
                            User u=gson.fromJson(res, User.class);
                            mMyPrefence.saveUser(u);
                            MainActivity.start(getContext());
                            getActivity().finish();
                            MyToast.success(getContext(), R.string.login_succ,Toast.LENGTH_SHORT);
                        }catch (Exception e){
                            MyToast.error(getContext(), R.string.wrong_data, Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError() {
                        MyToast.cancel();
                        MyToast.error(getContext(), R.string.load_error, Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRegister();
            }
        });
    }

    @Override
    protected void initData() {
        mMyPrefence=MyPrefence.getInstance(getContext());
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    private OnLoginListener mListener;

    public  interface OnLoginListener{
        void onRegister();
    }

    //    与Activity交互
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
            mListener = (OnLoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddStudentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
