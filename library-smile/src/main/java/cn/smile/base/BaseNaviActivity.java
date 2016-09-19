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

/**封装了自定义导航栏的类均需继承该类
 * @author smile
 * @date 2015-08-18-11:29
 */
public abstract class BaseNaviActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity<T,E>{

    public OnToolBarListener listener;
    public TextView tv_title;
    public LinearLayout ll_left;
    public ImageView iv_left;
    public TextView tv_right;

    /**导航栏标题:必填项
     * @return
     */
    protected abstract String title();

    /**导航栏左边：可以为string或图片资源id,非必须
     * @return
     */
    public Object left(){return null;}

    /**导航栏右边：可以为string或图片资源id,非必须
     * @return
     */
    public Object right(){return null;}

    /**设置导航栏监听,非必须
     * @return
     */
    public OnToolBarListener setOnToolBarListener(){return null;}

    /**
     * 初始化导航条
     */
    public void initNaviView(){
        tv_title = getView(R.id.tv_title);
        tv_right = getView(R.id.tv_right);
        ll_left = getView(R.id.ll_left);
        iv_left = getView(R.id.iv_left);
        setNaviListener(setOnToolBarListener());
        ll_left.setOnClickListener(clickListener);
        tv_right.setOnClickListener(clickListener);
        tv_title.setText(title());
        refreshTop();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.ll_left){
                if (listener == null)
                    finish();
                else{
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

    protected void refreshTop() {
        setLeftView(left()==null ? R.drawable.selector_back: left());
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

    protected void setValue(int id,Object obj){
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

    private void setNaviListener(OnToolBarListener listener) {
        this.listener = listener;
    }

    public boolean handleBackPressed(){
        return false;
    }

}
