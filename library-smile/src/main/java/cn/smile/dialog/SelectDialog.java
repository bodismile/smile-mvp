package cn.smile.dialog;

import android.content.Context;

/**类似QQ的选择框
 * @author smile
 * @date 2015-08-27-14:09
 */
public class SelectDialog extends QQDialog {

    public SelectDialog(Context context, String title, boolean has, String... item1) {
        super(context);
        if(has){
            super.setTitle(title);
            super.hasTitle(true);
        }else{
            super.hasTitle(false);
        }
        if(item1.length>2){
            super.setItem1(item1[0]);
            super.setItem2(item1[1]);
            super.setItem3(item1[2]);
        }else if(item1.length>1){
            super.setItem1(item1[0]);
            super.setItem2(item1[1]);
        }else{
            super.setItem1(item1[0]);
        }
    }
}
