package cn.smile.dialog;

import android.content.Context;
import android.view.WindowManager;

import cn.smile.R;
import cn.smile.util.Utils;


/**提示对话框
 * @author smile
 * @date 2015-08-07-10:56
 */
public class DialogTips extends DialogBase {
	
	/**自定义标题和内容
	 * @param context
	 * @param title
	 * @param content
	 */
	public DialogTips(Context context, String title, String content){
		super(context);
		super.setContent(content);
		super.setNegativeButtonText(getContext().getString(R.string.cancel));
		super.setPositiveButtonText(getContext().getString(R.string.yes));
		super.setPositiveTextColor(context.getResources().getColor(R.color.color_blue));
		super.setNegativeTextColor(context.getResources().getColor(R.color.color_64));
		super.setTitle(title);
	}

	/**无标题，单按钮
	 * @param context
	 * @param content
	 */
	public DialogTips(Context context, boolean hasTwo, String content){
		super(context);
		super.setHasTitle(false);
		super.setContent(content);
		if(hasTwo){
			super.setNegativeButtonText("取消");
		}
		super.setPositiveButtonText("确定");
		super.setPositiveTextColor(context.getResources().getColor(R.color.color_blue));
	}

	/**  
	 * 单按钮
	 * @param context
	 * @param positiveText
	 */
	public DialogTips(Context context, String positiveText) {
		super(context);
		super.setHasTitle(false);
		super.setContent("");
		super.setPositiveTextColor(context.getResources().getColor(R.color.color_black));
		super.setPositiveButtonText(positiveText);
	}
	
	/**自定义标题、内容、单按钮文字
	 * @param context
	 * @param title
	 * @param message
	 * @param buttonText
	 */
	public DialogTips(Context context, String title, String message, String buttonText) {
		super(context);
		super.setContent(message);
		super.setPositiveButtonText(buttonText);
		super.setTitle(title);
		super.setPositiveTextColor(context.getResources().getColor(R.color.color_blue));
		super.setCancel(false);
	}
	
	  
	/**自定义title、content、单按钮文字，设置对话框点击外部是否可取消
	 * @param context
	 * @param isCancel
	 * @param title
	 * @param message
	 * @param buttonText  
	 */
	public DialogTips(Context context, boolean isCancel, String title, String message, String buttonText) {
		super(context);
		super.setContent(message);
		super.setPositiveButtonText(buttonText);
		super.setTitle(title);
		super.setCancel(isCancel);
	}
	  
	/**自定义title、content、双按钮文字
	 * @param context
	 * @param title
	 * @param message
	 * @param buttonText
	 * @param negetiveText
	 */
	public DialogTips(Context context, boolean hasTwo,String title, String message, String buttonText, String negetiveText) {
		super(context);
		super.setHasTitle(false);
		super.setTitle(title);
		super.setContent(message);
		if(hasTwo){
			super.setNegativeButtonText(negetiveText);
		}
		super.setPositiveButtonText(buttonText);
		super.setCancel(true);
	}

	/**
	 * 创建对话框
	 */
	@Override
	protected void onBuilding() {
		super.setWidth(Utils.dip2px(mContext, 300));
		super.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void onDismiss() { }

	@Override
	protected void onClickNegativeButton() {
		if(onCancelListener != null){
			onCancelListener.onClick(this, 0);
		}
	}

	/**
	 * 确认按钮，触发onSuccessListener的onClick
	 */
	@Override
	protected boolean onClickPositiveButton() {
		if(onSuccessListener != null){
			onSuccessListener.onClick(this, 1);
		}
		return true;
	}
}
