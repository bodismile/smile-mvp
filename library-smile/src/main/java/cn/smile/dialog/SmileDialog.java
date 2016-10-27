package cn.smile.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import cn.smile.R;
import cn.smile.util.Utils;


/**提示对话框
 * @author smile
 * @date 2015-08-07-10:56
 */
public class SmileDialog extends DialogBase {

	private Builder mBuilder;

	/**
	 * Builder模式
	 * @param builder
     */
	public SmileDialog(Builder builder){
		super(builder.mContext);
		super.setCancelable(builder.isCancelable());
		super.setContent(builder.getContent());
		super.setBottomVisible(builder.isBottomVisible());
		super.setTitle(builder.getTitle());
		super.setContent(builder.getContent());
		super.setLeftText(builder.getLeftText());
		super.setRightText(builder.getRightText());
		super.setBgColor(builder.getBgColor());
		super.setTitleColor(builder.getTitleColor());
		super.setLeftBtnColor(builder.getLeftBtnColor());
		super.setRightBtnColor(builder.getRightBtnColor());
		super.setMiddleView(builder.getMiddleView());
		super.setTitleVisible(builder.isTitleVisible());
		super.setBottomVisible(builder.isBottomVisible());
		this.mBuilder=builder;
	}

	/**
	 * 创建对话框
	 */
	@Override
	protected void onBuilding() {
		if(mBuilder!=null && mBuilder.getHeight()!=0 && mBuilder.getWidth()!=0){
			super.setWidth(mBuilder.getWidth());
			super.setHeight(mBuilder.getHeight());
		}else{
			super.setWidth(Utils.dip2px(mContext, 300));
			super.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		}
	}

	@Override
	protected void onDismiss() {}

	@Override
	protected void onClickLeftBtn() {
		if(onCancelListener != null){
			onCancelListener.onClick(this, 0);
		}
		if(mBuilder!=null && mBuilder.getOnClickListener()!=null){
			mBuilder.getOnClickListener().onClick(this,0);
		}
	}

	/**
	 * 确认按钮，触发onSuccessListener的onClick
	 */
	@Override
	protected boolean onClickRightBtn() {
		if(onSuccessListener != null){
			onSuccessListener.onClick(this, 1);
		}
		if(mBuilder!=null && mBuilder.getOnClickListener()!=null){
			mBuilder.getOnClickListener().onClick(this,1);
		}
		return true;
	}

	/**
	 * Builder模式
	 */
	public static class Builder{
		private Context mContext;
		private boolean titleVisible;
		private boolean bottomVisible;
		private boolean mCancelable;
		private String title;
		private String content;
		private String rightText;
		private String leftText;
		private Drawable bgColor;
		private int rightBtnColor;
		private int leftBtnColor;
		private int titleColor;
		private int width,height;
		private OnClickListener onClickListener;
		private View middleView;

		public Builder(Context context) {
			mContext = context;
			titleVisible =true;
			bottomVisible =true;
			mCancelable =true;
			title = "提示";
			content = "";
			leftText = "";
			rightText = "";
			bgColor = ContextCompat.getDrawable(mContext, R.drawable.dialog_bg);
			rightBtnColor = context.getResources().getColor(R.color.color_blue);
			leftBtnColor = context.getResources().getColor(R.color.color_64);
			titleColor = context.getResources().getColor(R.color.color_1e1e1e);
			height= WindowManager.LayoutParams.WRAP_CONTENT;
			width = Utils.dip2px(mContext,300);
			onClickListener=null;
			middleView =null;
		}

		public OnClickListener getOnClickListener() {
			return onClickListener;
		}

		public Builder setOnClickListener(OnClickListener listener){
			this.onClickListener = listener;
			return this;
		}

		public boolean isTitleVisible() {
			return titleVisible;
		}

		public Builder setTitleVisible(boolean titleVisible) {
			this.titleVisible = titleVisible;
			return this;
		}

		public boolean isBottomVisible() {
			return bottomVisible;
		}

		public Builder setBottomVisible(boolean bottomVisible) {
			this.bottomVisible = bottomVisible;
			return this;
		}

		public boolean isCancelable() {
			return mCancelable;
		}

		public Builder setCancelable(boolean cancel) {
			mCancelable = cancel;
			return this;
		}
		public String getTitle() {
			return title;
		}
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		public String getContent() {
			return content;
		}
		public Builder setContent(String content) {
			this.content = content;
			return this;
		}
		public String getRightText() {
			return rightText;
		}
		public Builder setRightText(String positive) {
			this.rightText = positive;
			return this;
		}
		public String getLeftText() {
			return leftText;
		}
		public Builder setLeftText(String negative) {
			this.leftText = negative;
			return this;
		}
		public Drawable getBgColor() {
			return bgColor;
		}
		public Builder setBgColor(Drawable bgColor) {
			this.bgColor = bgColor;
			return this;
		}
		public int getRightBtnColor() {
			return rightBtnColor;
		}
		public Builder setRightBtnColor(int positiveColor) {
			this.rightBtnColor = positiveColor;
			return this;
		}
		public int getLeftBtnColor() {
			return leftBtnColor;
		}
		public Builder setLeftBtnColor(int negativeColor) {
			this.leftBtnColor = negativeColor;
			return this;
		}
		public int getTitleColor() {
			return titleColor;
		}
		public Builder setTitleColor(int titleColor) {
			this.titleColor = titleColor;
			return this;
		}
		public int getWidth() {
			return width;
		}
		public Builder setWidth(int width) {
			this.width = width;
			return this;
		}
		public int getHeight() {
			return height;
		}
		public Builder setHeight(int height) {
			this.height = height;
			return this;
		}
		public View getMiddleView() {
			return middleView;
		}
		public Builder setMiddleView(View middleView) {
			this.middleView = middleView;
			return this;
		}
		public SmileDialog build() {
			return new SmileDialog(this);
		}

	}

}
