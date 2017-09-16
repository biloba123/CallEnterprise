package com.lvqingyang.callenterprise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.lvqingyang.callenterprise.Call.CallFragment;
import com.lvqingyang.callenterprise.Task.TaskFragment;
import com.lvqingyang.callenterprise.User.UserFragment;

import frame.base.BaseActivity;

public class MainActivity extends BaseActivity {


    /**
     * view
     */
    private BottomNavigationView navigation;

    /**
     * fragment
     */
    private TaskFragment mTaskFragment;
    private CallFragment mCallFragment;
    private UserFragment mUserFragment;

    /**
     * data
     */
    private FragmentManager mFragmentManager;


    /**
     * tag
     */


    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager=getSupportFragmentManager();
        if (savedInstanceState!=null) {
            mTaskFragment= (TaskFragment) mFragmentManager
                    .findFragmentByTag(TaskFragment.class.getName());
            mCallFragment = (CallFragment) mFragmentManager
                    .findFragmentByTag(CallFragment.class.getName());
            mUserFragment= (UserFragment) mFragmentManager
                    .findFragmentByTag(UserFragment.class.getName());
            mFragmentManager.beginTransaction()
                    .show(mTaskFragment)
                    .hide(mCallFragment)
                    .hide(mUserFragment)
                    .commit();
        }else {
            mTaskFragment=TaskFragment.newInstance();
            mCallFragment =CallFragment.newInstance();
            mUserFragment=UserFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.content, mTaskFragment, TaskFragment.class.getName())
                    .add(R.id.content, mCallFragment, CallFragment
                            .class.getName())
                    .add(R.id.content, mUserFragment, UserFragment.class.getName())
                    .hide(mCallFragment)
                    .hide(mUserFragment)
                    .commit();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

    @Override
    protected void setListener() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_task:
                        mFragmentManager.beginTransaction()
                                .hide(mCallFragment)
                                .hide(mUserFragment)
                                .show(mTaskFragment)
                                .commit();
                        return true;
                    case R.id.navigation_call:
                        mFragmentManager.beginTransaction()
                                .hide(mTaskFragment)
                                .hide(mUserFragment)
                                .show(mCallFragment)
                                .commit();
                        return true;
                    case R.id.navigation_user:
                        mFragmentManager.beginTransaction()
                                .hide(mCallFragment)
                                .hide(mTaskFragment)
                                .show(mUserFragment)
                                .commit();
                        return true;
                }
                return false;
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
    protected String[] getNeedPermissions() {
        return new String[0];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        if (paramMenuItem.getItemId()== R.id.item_exit) {
            return false;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }
}
