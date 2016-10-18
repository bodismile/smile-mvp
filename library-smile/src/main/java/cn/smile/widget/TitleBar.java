package cn.smile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.smile.R;
import cn.smile.listener.OnToolBarListener;

/**
 * 标题栏控件-自定义属性：背景色、中间文本及颜色、左边文本及按钮、右边文本及按钮等
 * @author smile
 */
public class TitleBar extends RelativeLayout {

    private int titleBg;
    private String titleText;
    private int titleColor;
    private int titleSize;
    /**
     * 左边图片
     */
    private int leftResId;
    /**
     * 左边文本颜色
     */
    private int leftTextColor;
    /**
     * 左边文本大小
     */
    private int leftTextSize;
    /**
     * 是否隐藏左边按钮
     */
    private boolean leftVisible;
    /**
     * 左边文本
     */
    private String leftText;

    private int rightResId;
    private String rightText;
    private int rightTextColor;
    private int rightTextSize;

    RelativeLayout rl_titlebar;
    LinearLayout ll_left;
    LinearLayout ll_right;
    private TextView tv_title;
    private TextView tv_left;
    private ImageView iv_left;

    private TextView tv_right;
    private ImageView iv_right;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, 0, 0);
        try {
            //背景色
            titleBg = ta.getColor(R.styleable.TitleBar_titleBg,getContext().getResources().getColor(R.color.color_theme));
            //标题
            titleText = ta.getString(R.styleable.TitleBar_titleText);
            titleSize = (int)ta.getDimension(R.styleable.TitleBar_titleSize, 18);
            titleColor = ta.getColor(R.styleable.TitleBar_titleColor,getContext().getResources().getColor(R.color.color_white));
            //返回
            leftVisible = ta.getBoolean(R.styleable.TitleBar_leftVisible, false);
            leftText = ta.getString(R.styleable.TitleBar_leftText);
            leftResId = ta.getResourceId(R.styleable.TitleBar_leftResId,R.drawable.selector_back);
            leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor,getContext().getResources().getColor(R.color.color_white));
            leftTextSize = (int)ta.getDimension(R.styleable.TitleBar_leftTextSize,16);
            //更多
            rightResId = ta.getResourceId(R.styleable.TitleBar_rightResId, 0);
            rightText = ta.getString(R.styleable.TitleBar_rightText);
            rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, getContext().getResources().getColor(R.color.color_white));
            rightTextSize = (int)ta.getDimension(R.styleable.TitleBar_rightTextSize,16);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this);
        setUpView();
    }

    private void setUpView(){
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_titlebar.setBackgroundColor(titleBg);

        tv_title = (TextView) findViewById(R.id.tv_title);

        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_left.setVisibility(leftVisible ? VISIBLE : INVISIBLE);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_left = (TextView) findViewById(R.id.tv_left);

        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_right = (TextView) findViewById(R.id.tv_right);

        //中间布局
        if(!TextUtils.isEmpty(titleText)){
            tv_title.setText(titleText);
            tv_title.setTextColor(titleColor);
//            tv_title.setTextSize(titleSize);
        }
        //左边布局存在
        if (leftVisible){
            if(!TextUtils.isEmpty(leftText)){
                tv_left.setText(leftText);
                tv_left.setTextColor(leftTextColor);
//                tv_left.setTextSize(leftTextSize);
                tv_left.setVisibility(View.VISIBLE);
                iv_left.setVisibility(View.GONE);
            }else if(leftResId !=0){
                iv_left.setVisibility(View.VISIBLE);
                tv_left.setVisibility(View.GONE);
                iv_left.setImageDrawable(getContext().getResources().getDrawable(leftResId));
            }
        }
        //右边布局
        if(!TextUtils.isEmpty(rightText)){
            tv_right.setText(rightText);
            tv_right.setTextColor(rightTextColor);
//            tv_right.setTextSize(rightTextSize);
        }else if(rightResId!=0){
            iv_right.setImageDrawable(getContext().getResources().getDrawable(rightResId));
        }
    }

    /**
     * 设置Title背景色
     * @param color
     */
    public void setBackground(int color){
        this.titleBg =color;
        rl_titlebar.setBackgroundColor(getContext().getResources().getColor(titleBg));
    }

    /**
     * 标题控件
     * @param titleText 设置标题文案
     */
    public void setTitle(String titleText){
        this.titleText = titleText;
        tv_title.setText(titleText);
    }

    public void setTitleColor(int color){
        this.titleColor =color;
        tv_title.setTextColor(titleColor);
    }

//    public void setTitleSize(int size){
//        this.titleSize =size;
//        tv_title.setTextSize(size);
//    }

    /**
     * 设置左边按钮的图片id
     * @param resId
     */
    public void setLeftDrawable(int resId){
        this.leftResId =resId;
        if(tv_left.getVisibility()!=View.GONE){
            tv_left.setVisibility(View.GONE);
        }
        if(iv_left.getVisibility()!=View.VISIBLE){
            iv_left.setVisibility(View.VISIBLE);
        }
        iv_left.setImageDrawable(getContext().getResources().getDrawable(leftResId));
    }

    /**
     * 设置左边按钮文字
     * @param text
     */
    public void setLeftText(String text){
        if(iv_left.getVisibility()!=View.GONE){
            iv_left.setVisibility(View.GONE);
        }
        if(tv_left.getVisibility()!=View.VISIBLE){
            tv_left.setVisibility(View.VISIBLE);
        }
        tv_left.setText(text);
    }

    /**
     * 设置左边文本颜色
     * @param color
     */
    public void setLeftTextColor(int color){
        this.leftTextColor = color;
        if(iv_left.getVisibility()!=View.GONE){
            iv_left.setVisibility(View.GONE);
        }
        if(tv_left.getVisibility()!=View.VISIBLE){
            tv_left.setVisibility(View.VISIBLE);
        }
        tv_left.setTextColor(color);
    }
//    /**
//     * 设置左边文本大小
//     * @param size
//     */
//    public void setLeftTextSize(int size){
//        this.leftTextSize = size;
//        if(iv_left.getVisibility()!=View.GONE){
//            iv_left.setVisibility(View.GONE);
//        }
//        if(tv_left.getVisibility()!=View.VISIBLE){
//            tv_left.setVisibility(View.VISIBLE);
//        }
//        tv_left.setTextSize(size);
//    }

    /**
     * 标题更多按钮
     * @param img 设置更多按钮
     */
    public void setRightDrawable(int img){
        this.rightResId = img;
        if(tv_right.getVisibility()!=View.GONE){
            tv_right.setVisibility(View.GONE);
        }
        if(iv_right.getVisibility()!=View.VISIBLE){
            iv_right.setVisibility(View.VISIBLE);
        }
        iv_right.setImageDrawable(getContext().getResources().getDrawable(rightResId));
    }

    /**
     * 设置右边文字内容
     * @param text 更多文本
     */
    public void setRightText(String text){
        tv_right.setText(text);
        if(iv_right.getVisibility()!=View.GONE){
            iv_right.setVisibility(View.GONE);
        }
        if(tv_right.getVisibility()!=View.VISIBLE){
            tv_right.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置更多文本颜色
     * @param color
     */
    public void setRightTextColor(int color){
        this.rightTextColor = color;
        tv_right.setTextColor(color);
        if(iv_right.getVisibility()!=View.GONE){
            iv_right.setVisibility(View.GONE);
        }
        if(tv_right.getVisibility()!=View.VISIBLE){
            tv_right.setVisibility(View.VISIBLE);
        }
    }
//    /**
//     * 设置更多文本颜色
//     * @param size
//     */
//    public void setRightTextSize(int size){
//        this.rightTextSize = size;
//        tv_right.setTextSize(rightTextSize);
//        if(iv_right.getVisibility()!=View.GONE){
//            iv_right.setVisibility(View.GONE);
//        }
//        if(tv_right.getVisibility()!=View.VISIBLE){
//            tv_right.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * 设置点击事件
     * @param listener
     */
    public void setToolBarListener(final OnToolBarListener listener){
        if(listener!=null){
            if(leftVisible){
                ll_left.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.clickLeft();
                    }
                });
            }else{
                throw new RuntimeException("left layout is gone");
            }
            //点击事件
            ll_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.clickRight();
                }
            });
        }
    }

}
