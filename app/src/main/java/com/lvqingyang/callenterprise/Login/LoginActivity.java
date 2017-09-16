package com.lvqingyang.callenterprise.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import com.lvqingyang.callenterprise.MainActivity;
import com.lvqingyang.callenterprise.R;

import frame.base.BaseActivity;
import frame.tool.MyPrefence;

public class LoginActivity extends BaseActivity implements RegisterFragment.OnRegisterListener ,
        LoginFragment.OnLoginListener{

    private FragmentManager mFragmentManager;
    private LoginFragment mLoginFrag;
    private RegisterFragment mRegisterFrag;
    private ActionBar mActionBar;


    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (MyPrefence.getInstance(this).isLogined()){
            MainActivity.start(this);
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mActionBar=getSupportActionBar();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        mActionBar.setTitle(R.string.login);
        mFragmentManager=getSupportFragmentManager();
        Fragment fragment=mFragmentManager.findFragmentByTag(LoginFragment.class.getName());
        mLoginFrag=fragment==null?LoginFragment.newInstance():(LoginFragment)fragment;
        fragment=mFragmentManager.findFragmentByTag(RegisterFragment.class.getName());
        mRegisterFrag=fragment==null?RegisterFragment.newInstance():(RegisterFragment)fragment;
    }

    @Override
    protected void setData() {
        mFragmentManager.beginTransaction()
                .add(R.id.container,mLoginFrag,LoginFragment.class.getName())
                .commit();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    //登录回调
    @Override
    public void onLogin() {
        mFragmentManager.popBackStack();
        mActionBar.setTitle(R.string.login);
    }


    //注册回调
    @Override
    public void onRegister() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_right_in,R.anim.fragment_left_out,
                        R.anim.fragment_left_in,R.anim.fragment_right_out)
                .replace(R.id.container,mRegisterFrag,RegisterFragment.class.getName())
                .addToBackStack(null)
                .commit();
        mActionBar.setTitle(R.string.register);
    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
    }
}
