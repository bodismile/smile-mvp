package cn.smile.base.mvp;

/**状态View简化版：多用组合少用继承
 * @author smile
 */
public abstract class BaseSimpleStateView implements BaseStateView{
    /**
     * 显示加载框
     */
    public void showLoading(){}
    /**
     * 隐藏加载框
     */
    public void dismissLoading(){}
    /**
     * 显示消息
     */
    public void showToast(String msg){}
}
