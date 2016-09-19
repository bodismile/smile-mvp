package cn.smile.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.smile.R;

/**自定义对话框基类
 * @author  smile
 * @date  2015-08-07-10:56
 * 可自由控制上中下布局，允许自定义对话框，一个button或两个
 */
public abstract class DialogBase extends Dialog {

	protected Context mContext;
	protected OnClickListener onSuccessListener;
	protected OnClickListener onCancelListener;//提供给取消按钮
	protected OnDismissListener onDismissListener;
	//自定义view
	protected View view;
	protected Button positiveButton, negativeButton;
	private boolean isFullScreen = false;
	
	private LinearLayout ll_bottom;
	
	private boolean hasTitle = true;//是否有title
	private boolean hasBottom = true;//是否有bottom
	
	private int width = 0, height = 0, x = 0, y = 0;
	private String content, title;
	private int color=0;

	private String namePositiveButton, nameNegativeButton;
	private int bgColor=0,positiveColor=0,negativeColor=0;

	private final int MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

	private boolean isCancel = true;//默认是否可点击back按键/点击外部区域取消对话框

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

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
	
	/** 
	 * 创建事件
	 */
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.dialog_base);
		this.onBuilding();
		//设置背景
		LinearLayout ll_dialog = (LinearLayout)findViewById(R.id.ll_dialog);
		if(bgColor!=0){
			ll_dialog.setBackgroundColor(bgColor);
		}else{
			ll_dialog.setBackground(getContext().getResources().getDrawable(R.drawable.dialog_bg));
		}
		// 设置标题和消息
		RelativeLayout dialog_top = (RelativeLayout)findViewById(R.id.dialog_top);
		//是否有title
		if(hasTitle){
			dialog_top.setVisibility(View.VISIBLE);
		}else{
			dialog_top.setVisibility(View.GONE);
		}
		//标题
		TextView titleTextView = (TextView)findViewById(R.id.dialog_title);
		titleTextView.setText(this.getTitle());

		if(this.getTitleColor()>0){
			titleTextView.setTextColor(this.getTitleColor());
		}else{
			titleTextView.setTextColor(getContext().getResources().getColor(R.color.color_1e1e1e));
		}
		//内容
		TextView content = (TextView)findViewById(R.id.dialog_message);

		if(!TextUtils.isEmpty(getContent())){
			if(!hasTitle){
				content.setGravity(Gravity.CENTER);
			}else{
				content.setGravity(Gravity.LEFT| Gravity.CENTER);
			}
			content.setText(this.getContent());
			findViewById(R.id.dialog_contentPanel).setVisibility(View.VISIBLE);
			findViewById(R.id.dialog_customPanel).setVisibility(View.GONE);
		} else{
			findViewById(R.id.dialog_contentPanel).setVisibility(View.GONE);
			findViewById(R.id.dialog_customPanel).setVisibility(View.VISIBLE);
		}

		//是否包含自定义的内容view
		if (view != null) {
			FrameLayout custom = (FrameLayout) findViewById(R.id.dialog_custom);
			custom.addView(view, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
			findViewById(R.id.dialog_contentPanel).setVisibility(View.GONE);
			findViewById(R.id.dialog_customPanel).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.dialog_contentPanel).setVisibility(View.VISIBLE);
			findViewById(R.id.dialog_customPanel).setVisibility(View.GONE);
		}
		//底部布局
		ll_bottom = (LinearLayout)findViewById(R.id.dialog_bottom);
		if(hasBottom){
			ll_bottom.setVisibility(View.VISIBLE);
		}else{
			ll_bottom.setVisibility(View.GONE);
		}
		//底部按钮
		positiveButton = (Button)findViewById(R.id.dialog_positivebutton);
		negativeButton = (Button)findViewById(R.id.dialog_negativebutton);

		if(!TextUtils.isEmpty(namePositiveButton)){
			positiveButton.setText(namePositiveButton);
			if(getPositiveTextColor()>0){
				positiveButton.setTextColor(getPositiveTextColor());
			}else{
				positiveButton.setTextColor(getContext().getResources().getColor(R.color.color_blue));
			}
			positiveButton.setOnClickListener(getPositiveButtonOnClickListener());
		} else {
			positiveButton.setVisibility(View.GONE);
			findViewById(R.id.dialog_leftspacer).setVisibility(View.VISIBLE);
			findViewById(R.id.dialog_rightspacer).setVisibility(View.VISIBLE);
		}

		if(!TextUtils.isEmpty(nameNegativeButton)){
			negativeButton.setText(nameNegativeButton);
			if(this.getNegativeTextColor()>0){
				negativeButton.setTextColor(this.getNegativeTextColor());
			}else{
				negativeButton.setTextColor(getContext().getResources().getColor(R.color.color_64));
			}
			negativeButton.setOnClickListener(getNegativeButtonOnClickListener());
		} else {
			negativeButton.setVisibility(View.GONE);
		}
		
		// 设置对话框的位置和大小
		LayoutParams params = this.getWindow().getAttributes();
		if(this.getWidth()>0)
			params.width = this.getWidth();
		if(this.getHeight()>0){
			params.height = this.getHeight();
		}
		if(this.getX()>0)
			params.width = this.getX();
		if(this.getY()>0)
			params.height = this.getY();
		// 如果设置为全屏
		if(isFullScreen) {
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.MATCH_PARENT;
		}
		params.gravity = Gravity.CENTER;
		getWindow().setAttributes(params);
		//设置点击dialog外部区域可取消
		setCanceledOnTouchOutside(isCancel);
		setCancelable(isCancel);
		this.setOnDismissListener(getOnDismissListener());
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
				view = null;
				mContext = null;
				positiveButton = null;
				negativeButton = null;
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
				if(onClickPositiveButton())
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
				onClickNegativeButton();
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
	protected abstract boolean onClickPositiveButton();

	/**
	 * 取消按钮单击方法，用于子类定制
	 */
	protected abstract void onClickNegativeButton();

	/**
	 * 关闭方法，用于子类定制
	 */
	protected abstract void onDismiss();

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
	 * @return 对话框标题颜色
	 */
	public int getTitleColor() {
		return color;
	}
	
	/**
	 * @param color 对话框标题颜色
	 */
	public void setTitleColor(int color) {
		this.color = color;
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

	protected void setNegativeTextColor(int color){
		this.negativeColor = color;
	}

	public int getNegativeTextColor() {
		return negativeColor;
	}

	public int getPositiveTextColor() {
		return positiveColor;
	}

	public void setPositiveTextColor(int positiveColor) {
		this.positiveColor = positiveColor;
	}

	/**背景色
	 * @return
	 */
	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return 对话框View
	 */
	protected View getView() {
		return view;
	}

	/**
	 * @param view 对话框View
	 */
	protected void setView(View view) {
		this.view = view;
	}

	/**
	 * @return 是否全屏
	 */
	public boolean getIsFullScreen() {
		return isFullScreen;
	}

	/**
	 * @param isFullScreen 是否全屏
	 */
	public void setIsFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	public boolean isHasTitle() {
		return hasTitle;
	}


	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}
	
	public boolean isHasBottom() {
		return hasBottom;
	}
	
	public void setHasBottom(boolean hasBottom) {
		this.hasBottom = hasBottom;
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
	 * @return 对话框X坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x 对话框X坐标
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return 对话框Y坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y 对话框Y坐标
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return 确认按钮名称
	 */
	protected String getPositiveButtonText() {
		return namePositiveButton;
	}

	/**
	 * @param namePositiveButton 确认按钮名称
	 */
	protected void setPositiveButtonText(String namePositiveButton) {
		this.namePositiveButton = namePositiveButton;
	}

	/**
	 * @return 取消按钮名称
	 */
	protected String getNegativeButtonText() {
		return nameNegativeButton;
	}

	/**
	 * @param nameNegativeButton 取消按钮名称
	 */
	protected void setNegativeButtonText(String nameNegativeButton) {
		this.nameNegativeButton = nameNegativeButton;
	}
}