package com.lvqingyang.callenterprise.Task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lvqingyang.callenterprise.BuildConfig;
import com.lvqingyang.callenterprise.R;
import com.lvqingyang.callenterprise.bean.Order;
import com.lvqingyang.callenterprise.bean.User;
import com.lvqingyang.callenterprise.net.RequestHelper;
import com.lvqingyang.callenterprise.net.RequestListener;

import frame.base.BaseActivity;
import frame.tool.MyPrefence;
import frame.tool.MyToast;

public class AddTaskActivity extends BaseActivity {

    private android.widget.EditText etcompany;
    private android.widget.EditText etname;
    private android.widget.Spinner sptype;
    private android.widget.EditText etprice;
    private android.widget.EditText etcallcontent;
    private android.widget.Switch swsms;
    private android.widget.Switch swurgent;
    private android.widget.Button btnpost;
    private User mUser;
    private static final String TAG = "AddTaskActivity";
    private Gson mGson=new Gson();
    private TextView mTvFile;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddTaskActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    public static Intent newIntent(Context context) {
        Intent starter = new Intent(context, AddTaskActivity.class);
//        starter.putExtra();
        return starter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void initView() {
        initeActionbar(getString(R.string.post_task),true);
        this.btnpost = (Button) findViewById(R.id.btn_post);
        this.swurgent = (Switch) findViewById(R.id.sw_urgent);
        this.swsms = (Switch) findViewById(R.id.sw_sms);
        this.etcallcontent = (EditText) findViewById(R.id.et_call_content);
        this.etprice = (EditText) findViewById(R.id.et_price);
        this.sptype = (Spinner) findViewById(R.id.sp_type);
        this.etname = (EditText) findViewById(R.id.et_name);
        this.etcompany = (EditText) findViewById(R.id.et_company);
        mTvFile = (TextView) findViewById(R.id.tv_file);
    }

    @Override
    protected void setListener() {
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order=new Order();
                order.setEnterID(mUser.getEnterID());
                order.setOrderType(sptype.getSelectedItemPosition()+1);
                order.setUnitPrice(Double.parseDouble(etprice.getText().toString()));
                order.setOrderName(etname.getText().toString());
                order.setCallContent(etcallcontent.getText().toString());
                order.setExpress(swurgent.isChecked());
                order.setSendSMS(swsms.isChecked());
                MyToast.loading(AddTaskActivity.this, R.string.uploading);
                RequestHelper.createOrder(AddTaskActivity.this, mGson.toJson(order), new RequestListener() {
                    @Override
                    public void onResponse(String res) {
                        MyToast.cancel();
                        if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: "+res);
                        if (res.equals("1")) {
                            MyToast.success(AddTaskActivity.this, R.string.post_task_succ);
                            setResult(RESULT_OK);
                        }else {
                            MyToast.error(AddTaskActivity.this, R.string.post_task_fail);
                        }
                    }

                    @Override
                    public void onError() {
                        MyToast.cancel();
                        MyToast.error(AddTaskActivity.this, R.string.load_error);
                    }
                });
            }
        });

        findViewById(R.id.ll_upload_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData() {
        mUser=MyPrefence.getInstance(this).getUser(User.class);
        etcompany.setText(mUser.getEnterName());
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[0];
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());

                    MyToast.loading(this, R.string.uploading);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyToast.cancel();
                                        MyToast.success(AddTaskActivity.this, R.string.upload_succ);
                                        mTvFile.setText("201709141089电话列表16个.txt");
                                    }
                                });
                            }
                        }
                    }).start();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
