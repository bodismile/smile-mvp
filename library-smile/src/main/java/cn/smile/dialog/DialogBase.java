package cn.smile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.smile.R;

/**自定义对话框基类
 * @author smile
 * @date 2015-08-07-10:56
 * 可自由控制上中下布局，允许自定义对话框，一个button或两个
 */
public abstract class DialogBase extends Dialog {

	protected Context mContext;
	/**
	 * 确定监听
	 */
	protected OnClickListener onSuccessListener;
	/**
	 * 取消监听
	 */
	protected OnClickListener onCancelListener;
	/**
	 * 消失监听
	 */
	protected OnDismissListener onDismissListener;
	/**
	 * RootView
	 */
	LinearLayout ll_dialog;
	/**
	 * 标题
	 */
	TextView tv_title;
	/**
	 * 内容
	 */
	TextView tv_content;
	/**
	 *中间View
	 */
	LinearLayout dialog_middle_msg;
	FrameLayout dialog_middle;
	FrameLayout dialog_custom;
	/**
	 * 自定义的内容view
	 */
	protected View middleView;
	/**
	 * 按钮文本
	 */
	protected Button rightBtn, leftBtn;
	/**
	 * 底部View
	 */
	private LinearLayout dialog_bottom;
	/**
	 * 是否全屏显示
	 */
	private boolean isFullScreen = false;
	/**
	 * 是否有title
	 */
	private boolean titleVisible = true;
	/**
	 * 是否有Bottom
	 */
	private boolean bottomVisible = true;
	/**
	 * 是否可点击back按键/点击外部区域取消对话框，默认：可取消
	 */
	private boolean mCancelable = true;

	private int width = 0, height = 0;
	private String content, title;
	private String rightText, leftText;
	private Drawable bgColor;
	private int titleColor =0, rightBtnColor =0, leftBtnColor =0;
	private final int MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

	/**
	 * 构造函数
	 * @param context 对象应该是Activity
	 */
	public DialogBase(Context context) {
		super(context, R.style.Dialog_Theme);
		this.mContext = context;
	}

	/**自定义dialog主题
	 * @param context
	 * @param theme  
	 */
	public DialogBase(Context context, int theme) {
		super(context,theme);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.dialog_base);
		onBuilding();
		initView();
		initData();
		// 设置对话框的位置和大小
		LayoutParams params = this.getWindow().getAttributes();
		if(this.getWidth()>0)
			params.width = this.getWidth();
		if(this.getHeight()>0){
			params.height = this.getHeight();
		}
		// 如果设置为全屏
		if(isFullScreen) {
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.MATCH_PARENT;
		}
		params.gravity = Gravity.CENTER;
		getWindow().setAttributes(params);
		setCanceledOnTouchOutside(mCancelable);
		setCancelable(mCancelable);
		this.setOnDismissListener(getOnDismissListener());
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	/**
	 * 初始化View
	 */
	private void initView(){
		//rootView
		ll_dialog = (LinearLayout)findViewById(R.id.ll_dialog);
		//标题
		tv_title =(TextView)findViewById(R.id.dialog_title);
		//文本View
		dialog_middle_msg = (LinearLayout)findViewById(R.id.dialog_middle_msg);
		//内容文本
		tv_content = (TextView)findViewById(R.id.dialog_msg);
		//自定义View
		dialog_middle = (FrameLayout)findViewById(R.id.dialog_middle);
		//自定义内容View
		dialog_custom = (FrameLayout)findViewById(R.id.dialog_custom);
		//底部布局
		dialog_bottom = (LinearLayout)findViewById(R.id.dialog_bottom);
		//底部按钮
		rightBtn = (Button)findViewById(R.id.dialog_right);
		leftBtn = (Button)findViewById(R.id.dialog_left);
	}

	private void initData(){
		//设置背景色
		Drawable bg;
		if(bgColor!=null){
			bg = bgColor;
		}else{
			bg = getContext().getResources().getDrawable(R.drawable.dialog_bg);
		}
		if(Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN){
			ll_dialog.setBackgroundDrawable(bg);
		}else{
			ll_dialog.setBackground(bg);
		}
		//是否有title
		if(titleVisible){
			tv_title.setVisibility(View.VISIBLE);
		}else{
			tv_title.setVisibility(View.GONE);
		}
		// 设置标题
		tv_title.setText(title);
		if(titleColor>0){
			tv_title.setTextColor(titleColor);
		}
		//content为文字时
		if(!TextUtils.isEmpty(content)){
			if(!titleVisible){
				tv_content.setGravity(Gravity.CENTER);
			}else{
				tv_content.setGravity(Gravity.LEFT| Gravity.CENTER);
			}
			tv_content.setText(content);
			dialog_middle_msg.setVisibility(View.VISIBLE);
			dialog_middle.setVisibility(View.GONE);
		} else{
			dialog_middle_msg.setVisibility(View.GONE);
			dialog_middle.setVisibility(View.VISIBLE);
		}
		//自定义内容View
		if (middleView != null) {
			dialog_custom.addView(middleView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
			dialog_middle_msg.setVisibility(View.GONE);
			dialog_middle.setVisibility(View.VISIBLE);
		} else {
			dialog_middle_msg.setVisibility(View.VISIBLE);
			dialog_middle.setVisibility(View.GONE);
		}
		//底部布局
		if(bottomVisible){
			dialog_bottom.setVisibility(View.VISIBLE);
		}else{
			dialog_bottom.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(rightText)){
			rightBtn.setText(rightText);
			if(rightBtnColor>0){
				rightBtn.setTextColor(rightBtnColor);
			}
			rightBtn.setOnClickListener(getPositiveButtonOnClickListener());
		} else {
			rightBtn.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(leftText)){
			leftBtn.setText(leftText);
			if(leftBtnColor>0){
				leftBtn.setTextColor(leftBtnColor);
			}
			leftBtn.setOnClickListener(getNegativeButtonOnClickListener());
		} else {
			leftBtn.setVisibility(View.GONE);
		}

	}

	/**
	 * 获取OnDismiss事件监听，释放资源
	 * @return OnDismiss事件监听
	 */
	protected OnDismissListener getOnDismissListener() {
		return new OnDismissListener(){
			public void onDismiss(DialogInterface arg0) {
				DialogBase.this.onDismiss();
				DialogBase.this.setOnDismissListener(null);
				middleView = null;
				mContext = null;
				rightBtn = null;
				leftBtn = null;
				if(onDismissListener != null){
					onDismissListener.onDismiss(null);
				}
			}			
		};
	}

	/**
	 * 获取确认按钮单击事件监听
	 * @return 确认按钮单击事件监听
	 */
	protected View.OnClickListener getPositiveButtonOnClickListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				if(onClickRightBtn())
					DialogBase.this.dismiss();
			}
		};
	}
	
	/**
	 * 获取取消按钮单击事件监听
	 * @return 取消按钮单击事件监听
	 */
	protected View.OnClickListener getNegativeButtonOnClickListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				onClickLeftBtn();
				DialogBase.this.dismiss();
			}
		};
	}
	
	/**
	 * 获取焦点改变事件监听，设置EditText文本默认全选
	 * @return 焦点改变事件监听
	 */
	protected OnFocusChangeListener getOnFocusChangeListener() {
		return new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && v instanceof EditText) {
					((EditText) v).setSelection(0, ((EditText) v).getText().length());
				}
			}
		};
	}
	
	/**
	 * 设置成功事件监听，用于提供给调用者的回调函数
	 * @param listener 成功事件监听
	 */
	public void setOnSuccessListener(OnClickListener listener){
		onSuccessListener = listener;
	}
	
	/**
	 * 设置关闭事件监听，用于提供给调用者的回调函数
	 * @param listener 关闭事件监听
	 */
	public void setOnDismissListener(OnDismissListener listener){
		onDismissListener = listener;
	}

	/**提供给取消按钮，用于实现类定制
	 * @param listener
	 */
	public void setOnCancelListener(OnClickListener listener){
		onCancelListener = listener;
	}
	
	/**
	 * 创建方法，用于子类定制创建过程
	 */
	protected abstract void onBuilding();

	/**
	 * 确认按钮单击方法，用于子类定制
	 */
	protected abstract boolean onClickRightBtn();

	/**
	 * 取消按钮单击方法，用于子类定制
	 */
	protected abstract void onClickLeftBtn();

	/**
	 * 关闭方法，用于子类定制
	 */
	protected abstract void onDismiss();

	/**
	 * 点击其他区域是否可Dismiss
	 * @param isCancel
     */
	public void setCancelable(boolean isCancel) {
		this.mCancelable = isCancel;
	}

	/**
	 * @return 对话框标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title 对话框标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return 对话框提示信息
	 */
	protected String getContent() {
		return content;
	}

	/**
	 * @param message 对话框提示信息
	 */
	protected void setContent(String message) {
		this.content = message;
	}

	/**
	 * @return 对话框标题颜色
	 */
	public int getTitleColor() {
		return titleColor;
	}

	/**
	 * @param color 对话框标题颜色
	 */
	public void setTitleColor(int color) {
		this.titleColor = color;
	}

	/**
	 * 获取左边按钮颜色
	 * @return
     */
	public int getLeftBtnColor() {
		return leftBtnColor;
	}

	/**
	 * 设置左边按钮颜色
	 * @param color
     */
	protected void setLeftBtnColor(int color){
		this.leftBtnColor = color;
	}

	/**
	 * 获取右边按钮颜色
	 * @return
     */
	public int getRightBtnColor() {
		return rightBtnColor;
	}

	/**
	 *设置右边按钮颜色
	 * @param positiveColor
     */
	public void setRightBtnColor(int positiveColor) {
		this.rightBtnColor = positiveColor;
	}
	/**
	 * 背景色
	 * @return
	 */
	public Drawable getBgColor() {
		return bgColor;
	}

	/**
	 * 设置背景色
	 * @param bgColor
     */
	public void setBgColor(Drawable bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return 对话框View
	 */
	protected View getMiddleView() {
		return middleView;
	}

	/**
	 * @param view 对话框View
	 */
	protected void setMiddleView(View view) {
		this.middleView = view;
	}

	/**
	 * @return 是否全屏
	 */
	public boolean isFullScreen() {
		return isFullScreen;
	}

	/**
	 * @param isFullScreen 是否全屏
	 */
	public void setFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	/**
	 * 设置标题是否隐藏
	 * @param titleVisible
     */
	public void setTitleVisible(boolean titleVisible) {
		this.titleVisible = titleVisible;
	}

	/**
	 * 设置底部是否隐藏
	 * @param bottomVisible
     */
	public void setBottomVisible(boolean bottomVisible) {
		this.bottomVisible = bottomVisible;
	}

	/**
	 * @return 对话框宽度
	 */
	protected int getWidth() {
		return width;
	}

	/**
	 * @param width 对话框宽度
	 */
	protected void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return 对话框高度
	 */
	protected int getHeight() {
		return height;
	}

	/**
	 * @param height 对话框高度
	 */
	protected void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return 右边按钮名称
	 */
	protected String getRightText() {
		return rightText;
	}

	/**
	 * @param text 右边按钮名称
	 */
	protected void setRightText(String text) {
		this.rightText = text;
	}

	/**
	 * @return 左边按钮名称
	 */
	protected String getLeftText() {
		return leftText;
	}

	/**
	 * @param text 左边按钮名称
	 */
	protected void setLeftText(String text) {
		this.leftText = text;
	}

}