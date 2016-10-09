package cn.smile.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import cn.smile.R;
import cn.smile.util.Utils;

/**
 * @author smile
 * @date 2015-08-27-14:28
 */
public final class DialogHelper {

    private static Dialog dialog =null;

    /**显示加载框
     * @method showDialog
     * @param context
     * @return void
     * @exception
     */
    public static void showLoadingsDialog(Context context, String msg){
        try {
            dialog = new Dialog(context, R.style.CustomProgressDialog);
            dismissLoadingDialog();
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_circular_progress, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_message);
            if(TextUtils.isEmpty(msg)){
                tv.setVisibility(View.GONE);
            }else{
                tv.setVisibility(View.VISIBLE);
                tv.setText(msg);
            }
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = Utils.dip2px(context, 80);
            params.height = Utils.dip2px(context, 80);
            params.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(params);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            dismissLoadingDialog();
        }
    }

    /** 取消进度框
     * @method dismissDialog
     * @return void
     * @exception
     */
    public static void dismissLoadingDialog(){
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /** 显示进度条
     * @param context
     * @param title
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String title){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(title);
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * Builder
     * @param builder
     */
    public static void showDialog(DialogTips.Builder builder){
        DialogTips dialog =new DialogTips(builder);
        dialog.show();
    }

    public static void showDialog(Context context, String title, String content, DialogInterface.OnClickListener listener) {
        DialogTips tips = new DialogTips(context, title, content);
        tips.setOnSuccessListener(listener);
        tips.show();
    }

    public static void showDialog(Context context, boolean hasTwo, String content, DialogInterface.OnClickListener listener) {
        DialogTips tips = new DialogTips(context, hasTwo, content);
        tips.setOnSuccessListener(listener);
        tips.show();
    }

    /**显示性别选择
     * @param context
     * @param listener
     */
    public static void showSexDialog(Context context, QQDialog.OnDialogClickListener listener){
        SexDialog dialog = new SexDialog(context);
        dialog.setOnDialogListener(listener);
        dialog.show();
    }

    /**显示头像选择
     * @param context
     * @param listener
     */
    public static void showAvatorDialog(Context context, QQDialog.OnDialogClickListener listener){
        SelectDialog dialog = new SelectDialog(context,null,false,context.getString(R.string.photo),context.getString(R.string.picture));
        dialog.setOnDialogListener(listener);
        dialog.show();
    }

    /**
     * 显示生日选择框
     * @param context
     * @param year
     * @param month
     * @param date
     * @param okListener
     */
    public static void showBirthdayDialog(Context context, int year, int month, final int date, final OnDateChangeListener okListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, null, year, month, date);
        datePickerDialog.setTitle("选择您的生日");
        datePickerDialog.setButton(Dialog.BUTTON_POSITIVE, "设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatePicker datePicker = ((DatePickerDialog)dialogInterface).getDatePicker();
                okListener.onChange(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
            }
        });
        datePickerDialog.setButton(Dialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        datePickerDialog.show();
    }

    public interface OnDateChangeListener{
        void onChange(int year,int month,int date);
    }
}
