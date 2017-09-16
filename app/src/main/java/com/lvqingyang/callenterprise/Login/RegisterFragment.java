package com.lvqingyang.callenterprise.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvqingyang.callenterprise.Base.BaseFragment;
import com.lvqingyang.callenterprise.R;

/**
 * Author：LvQingYang
 * Date：2017/9/1
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class RegisterFragment extends BaseFragment {

    private TextView tvlogin;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        this.tvlogin = (TextView) view.findViewById(R.id.tv_login);
    }

    @Override
    protected void setListener() {
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogin();
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

    private OnRegisterListener mListener;

    public  interface OnRegisterListener{
        void onLogin();
    }

    //    与Activity交互
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterListener) {
            mListener = (OnRegisterListener) context;
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
