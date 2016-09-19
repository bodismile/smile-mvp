package cn.smile.dialog;

import android.content.Context;

import cn.smile.R;

/**性别选择框
 * @author smile
 * @date 2015-08-27-14:09
 */
public class SexDialog extends QQDialog {

    public SexDialog(Context context) {
        super(context);
        super.hasTitle(true);
        super.setTitle(getString(R.string.choose_sex));
        super.setTitleColor(getContext().getResources().getColor(R.color.color_82));
        super.setItem1(getString(R.string.man));
        super.setItem2(getString(R.string.women));
    }
}
