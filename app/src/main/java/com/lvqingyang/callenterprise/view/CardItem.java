package com.lvqingyang.callenterprise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvqingyang.callenterprise.R;


/**
 * Author：LvQingYang
 * Date：2017/8/26
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class CardItem extends FrameLayout {


    private ImageView ivicon;
    private TextView tvname;
    private ImageView ivarrow;
    private TextView tvhint;
    private TextView tvcontent;
    private android.widget.RelativeLayout rl;

    public CardItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View v= LayoutInflater.from(context)
                .inflate(R.layout.item_card,this);
        this.rl = (RelativeLayout) v.findViewById(R.id.rl);
        this.tvcontent = (TextView) findViewById(R.id.tv_content);
        this.tvhint = (TextView) findViewById(R.id.tv_hint);
        this.ivarrow = (ImageView) findViewById(R.id.iv_arrow);
        this.tvname = (TextView) findViewById(R.id.tv_name);
        this.ivicon = (ImageView) findViewById(R.id.iv_icon);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                        attrs,
                        R.styleable.CardItem,
                        0, 0);
        try {
            String name=a.getString(R.styleable.CardItem_name);
            tvname.setText(name);

            if (a.getBoolean(R.styleable.CardItem_showInfo,false)) {
                String content=a.getString(R.styleable.CardItem_info);
                tvcontent.setText(content);
            }

            if (a.getBoolean(R.styleable.CardItem_showHint,false)) {
                String hint=a.getString(R.styleable.CardItem_hint);
                tvhint.setText(hint);
            }

            ivarrow.setVisibility(a.getBoolean(R.styleable.CardItem_showArrow,true)?VISIBLE:GONE);

            int resId=a.getResourceId(R.styleable.CardItem_icon,R.mipmap.ic_launcher);
            ivicon.setImageResource(resId);
        } finally {
            a.recycle();
        }
    }

    public void setContent(String content){
        tvcontent.setText(content);
    }

    public void setHint(String hint){
        tvhint.setText(hint);
    }

    public void setOnClickListener(OnClickListener listener){
        rl.setOnClickListener(listener);
    }
}
