package cn.smile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.smile.R;
import cn.smile.util.Utils;

/**
 * 单选、多选基类
 * @author smile
 */
public abstract class DialogChoiceBase extends Dialog implements OnClickListener {

	protected Context mContext;
	TextView tv_wheel_cancel,tv_wheel_title,tv_wheel_confirm;
	ListView mListView;

	protected List<String> mList;
	private String title;
	protected OnClickListener mOkClickListener;

	public DialogChoiceBase(Context context, String title, List<String> list) {
		super(context, R.style.Dialog_Theme);
		mContext = context;
		mList = list;
		this.title = title;
		initView();
	}

	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_choice);
		tv_wheel_cancel = (TextView)findViewById(R.id.tv_wheel_cancel);
		tv_wheel_title = (TextView)findViewById(R.id.tv_wheel_title);
		tv_wheel_confirm = (TextView)findViewById(R.id.tv_wheel_confirm);
		mListView = (ListView)findViewById(R.id.listView);

		tv_wheel_cancel.setOnClickListener(this);
		tv_wheel_confirm.setOnClickListener(this);

		if(!TextUtils.isEmpty(title)){
			tv_wheel_title.setText(title);
		}

		Window w = getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		onWindowAttributesChanged(lp);

		setCanceledOnTouchOutside(true);
		setCancelable(true);
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, Utils.dip2px(getContext(),250));
	}

	public void setTitle(String title) {
		tv_wheel_title.setText(title);
	}

	public void setOnOKButtonListener(OnClickListener onClickListener) {
		mOkClickListener = onClickListener;
	}

	public void onClick(View v) {
		if(v.getId()==R.id.tv_wheel_cancel){
			onButtonCancel();
		}else if(v.getId()==R.id.tv_wheel_confirm){
			if(onButtonOK()){
				dismiss();
			}
		}
	}

	protected abstract boolean onButtonOK();

	protected void onButtonCancel() {
		dismiss();
	}
}
