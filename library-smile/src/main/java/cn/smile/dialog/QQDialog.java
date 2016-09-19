package cn.smile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.smile.R;

/**仿QQ退出框:最多支持三个Item
 * @author smile
 * @date 2015-08-27-13:06
 */
public abstract class QQDialog extends Dialog implements View.OnClickListener{

    Context mainContext;
    TextView tv_title,tv_item1,tv_item2,tv_item3,mCancel;

    /**
     * 构造函数
     * @param context 对象应该是Activity
     */
    public QQDialog(Context context) {
        super(context, R.style.Dialog_QQ);
        this.mainContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_qq, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        tv_title = (TextView)layout.findViewById(R.id.tv_title);
        tv_item1 = (TextView)layout.findViewById(R.id.tv_item1);
        tv_item2 = (TextView)layout.findViewById(R.id.tv_item2);
        tv_item3 = (TextView)layout.findViewById(R.id.tv_item3);
        mCancel = (TextView)layout.findViewById(R.id.tv_cancel);

        tv_item1.setOnClickListener(this);
        tv_item2.setOnClickListener(this);
        tv_item3.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        if(has){
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(getTitle());
            tv_item1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_middle_selector));
        }else{
            tv_title.setVisibility(View.GONE);
            tv_item1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_top_selector));
        }

        tv_item1.setText(item1);
        tv_item2.setText(item2);
        if(!TextUtils.isEmpty(item3)){
            tv_item3.setVisibility(View.VISIBLE);
            tv_item3.setText(item3);
            tv_item2.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_middle_selector));
        }else {
            tv_item3.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(item2)){
                tv_item2.setVisibility(View.VISIBLE);
                tv_item2.setText(item2);
                tv_item2.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_bottom_selector));
                if(!has){
                    tv_item1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_top_selector));
                }else{
                    tv_item1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_middle_selector));
                }
            }else{
                tv_item2.setVisibility(View.GONE);
                tv_item1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.actionsheet_single_selector));
            }
        }

        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
        setCanceledOnTouchOutside(true);
        if (cancelListener != null)
            setOnCancelListener(cancelListener);
        setContentView(layout);
    }

    int color;

    public void setTitleColor(int color){
        this.color =color;
    }

    boolean has=true;//默认有标题

    public void hasTitle(boolean has){
        this.has =has;
    }

    String title,item1,item2,item3;
    public void setItem1(String item1){
        this.item1 =item1;
    }
    public void setItem2(String item2){
        this.item2 =item2;
    }
    public void setItem3(String item3){
        this.item3 =item3;
    }

    public void setTitle(String title){ this.title=title; }
    public String getTitle(){
        return title;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if(v.getId()==R.id.tv_item1){
            listener.onClick(this,0);
        }else if(v.getId()==R.id.tv_item2){
            listener.onClick(this,1);
        }else if(v.getId()==R.id.tv_item3){
            listener.onClick(this,2);
        }else if(v.getId()==R.id.tv_cancel){
            listener.onClick(this,3);
        }
    }

    public interface OnDialogClickListener {
        void onClick(DialogInterface dialog, int whichButton);
    }

    OnCancelListener cancelListener;
    OnDialogClickListener listener;

    public void setOnDialogListener(OnDialogClickListener listener){
        this.listener = listener;
    }

    public void setOnCancelListener(OnCancelListener listener){
        this.cancelListener = listener;
    }

    public String getString(int id){
        return getContext().getString(id);
    }
}

