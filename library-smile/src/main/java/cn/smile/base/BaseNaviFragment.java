package cn.smile.base;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.smile.R;
import cn.smile.base.mvp.BaseModel;
import cn.smile.base.mvp.BasePresenter;
import cn.smile.listener.OnToolBarListener;

/**封装了自定义导航栏的Fragment类均需继承该类
 * @author smile
 * @date 2015-08-18-14:19
 */
public abstract class BaseNaviFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment<T,E>{

    private OnToolBarListener listener;
    private TextView tv_title;
    public TextView tv_right;
    public ImageView iv_left;
    public LinearLayout ll_left;
    public LinearLayout ll_navi;

    /**
     * 初始化导航条
     */
    public void initNaviView(){
        ll_navi = (LinearLayout) mRootView.findViewById(R.id.ll_navi);
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        tv_right = (TextView) mRootView.findViewById(R.id.tv_right);
        ll_left = (LinearLayout) mRootView.findViewById(R.id.ll_left);
        iv_left = (ImageView) mRootView.findViewById(R.id.iv_left);

        setListener(setToolBarListener());
        ll_left.setOnClickListener(clickListener);
        tv_right.setOnClickListener(clickListener);
        tv_title.setText(title());
        refreshTop();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.ll_left){
                if (listener != null){
                    listener.clickLeft();
                }
            }else if(v.getId()==R.id.tv_right){
                if (listener != null)
                    listener.clickRight();
            }else{
                throw new RuntimeException("not found this viewId:"+v.getId());
            }
        }
    };

    private void refreshTop() {
        setLeftView(left());
        setValue(R.id.tv_right, right());
        this.tv_title.setText(title());
    }

    private void setLeftView(Object obj){
        if(obj !=null && !obj.equals("")){
            iv_left.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                iv_left.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                iv_left.setImageResource(R.drawable.selector_back);
            }
        }else{
            iv_left.setVisibility(View.INVISIBLE);
        }
    }

    private void setValue(int id,Object obj){
        if (obj != null && !obj.equals("")) {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
            if (obj instanceof String) {
                ((TextView) getView(id)).setText(obj.toString());
            } else if (obj instanceof Integer) {
                getView(id).setBackgroundResource(Integer.parseInt(obj.toString()));
            }
        } else {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void setListener(OnToolBarListener listener) {
        this.listener = listener;
    }

    /**导航栏标题
     * @return
     */
    protected abstract String title();

    /**导航栏右边：可以为string或图片资源id，不是必填项
     * @return
     */
    public Object right(){
        return null;
    }

    /**导航栏左边
     * @return
     */
    public Object left(){return null;}

    /**设置导航条背景色
     * @param color
     */
    public void setNavBackground(int color){
        ll_navi.setBackgroundColor(color);
    }

    /**设置右边按钮的文字大小
     * @param dimenId
     */
    public void setRightTextSize(float dimenId){
        tv_right.setTextSize(dimenId);
    }

    /**设置导航栏监听
     * @return
     */
    public OnToolBarListener setToolBarListener(){return null;}

}
