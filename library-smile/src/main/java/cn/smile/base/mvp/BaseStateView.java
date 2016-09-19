package cn.smile.base.mvp;

/**
 * 状态View
 * @author smile
 */
public interface BaseStateView extends BaseView{
    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void dismissLoading();

    /**
     * 显示消息
     */
    void showToast(String msg);
}
