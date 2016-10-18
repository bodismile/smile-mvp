package cn.smile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.smile.R;

/**
 * 自定义底部布局-仿映客底部凹凸布局
 * @author smile
 * @date   2015-7-24 上午11:15:00  
 */
public class BottomBar extends LinearLayout {

	private Context mContext;
	private LayoutInflater inflater;

	private int[] optionsBgNormal;
	private int[] optionsBgActive;

	private int OPTION_NUM = 3;

	private LinearLayout[] optionLayouts;
	private ImageView[] optionImageViews;
	private TextView[] optionTextViews;

	public interface OnTableClickListener {
		void onTableClick(int choice);
	}

	private OnTableClickListener mOnTableClickListener;

	public void setOnTableClickListener(OnTableClickListener myOnTableClickListener) {
		mOnTableClickListener = myOnTableClickListener;
	}

	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void setDatas(int[] iconNormalIds, int[] iconActiveIds) {
		optionsBgNormal = iconNormalIds;
		optionsBgActive = iconActiveIds;
		initViews();
	}

	private void initViews() {
		//custom_table和custom_table1均可以实现底部按钮凸出的效果
		View convertView = inflater.inflate(R.layout.custom_table1, this);
		optionImageViews = new ImageView[OPTION_NUM];
		optionLayouts = new LinearLayout[OPTION_NUM];
		optionTextViews = new TextView[OPTION_NUM];

		optionLayouts[0] = (LinearLayout) convertView.findViewById(R.id.layout_tab1);
		optionLayouts[1] = (LinearLayout) convertView.findViewById(R.id.layout_tab2);
		optionLayouts[2] = (LinearLayout) convertView.findViewById(R.id.layout_tab3);

		optionImageViews[0] = (ImageView) convertView.findViewById(R.id.iv_tab1);
		optionImageViews[1] = (ImageView) convertView.findViewById(R.id.iv_tab2);
		optionImageViews[2] = (ImageView) convertView.findViewById(R.id.iv_tab3);

		optionTextViews[0] = (TextView) convertView.findViewById(R.id.tv_tab1);
		optionTextViews[1] = (TextView) convertView.findViewById(R.id.tv_tab2);
		optionTextViews[2] = (TextView) convertView.findViewById(R.id.tv_tab3);

		optionLayouts[0].setOnClickListener(mOnClickListener);
		optionLayouts[1].setOnClickListener(mOnClickListener);
		optionLayouts[2].setOnClickListener(mOnClickListener);

		for (int i = 0; i < OPTION_NUM; i++) {
			optionImageViews[i].setImageResource(optionsBgActive[i]);
		}
	}

	/**
	 * 点击事件监听，其中调用了接口中的方法
	 */
	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int choice = 0;
			if(v.getId()==R.id.layout_tab1){
				choice = 0;
			}else if(v.getId()==R.id.layout_tab2){
				choice = 1;
			}else if(v.getId()==R.id.layout_tab3){
				choice = 2;
			}
			if (mOnTableClickListener != null) {
				mOnTableClickListener.onTableClick(choice);
			}
		}
	};

	/**设置当前点击位置
	 * @param choice
	 */
	public void setCurItem(int choice) {
		handleClickEffect(choice);
		if (mOnTableClickListener != null) {
			mOnTableClickListener.onTableClick(choice);
		}
	}

	/**点击效果的实现
	 * @param choice
	 */
	public void handleClickEffect(int choice) {
		if(choice!=1){
			for (int i = 0; i < OPTION_NUM; i++) {
				if (i == choice) {
					optionImageViews[i].setImageResource(optionsBgActive[i]);
					optionTextViews[i].setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					optionTextViews[i].setTextColor(getResources().getColor(R.color.color_gray));
					optionImageViews[i].setImageResource(optionsBgNormal[i]);
				}
			}
		}
	}

}
