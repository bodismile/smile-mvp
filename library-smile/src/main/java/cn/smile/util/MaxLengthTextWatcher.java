package cn.smile.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * EditText的最大长度限制，英文0.5个字符，中文1个字符
 * @author  smile
 */
public class MaxLengthTextWatcher implements TextWatcher {
  
    private int maxLen = 0;  
    private EditText mEditText = null;
    TextView tvNum;
    private int editStart;
    private int editEnd;

    public MaxLengthTextWatcher(int maxLen, EditText editText, TextView tv) {
        this.maxLen = maxLen;  
        this.mEditText = editText;
        this.tvNum = tv;
    }
  
    public void afterTextChanged(Editable s) {
        editStart = mEditText.getSelectionStart();
        editEnd = mEditText.getSelectionEnd();
        // 先去掉监听器，否则会出现栈溢出
        mEditText.removeTextChangedListener(this);
        // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
        // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
        while (calculateLength(s.toString()) > maxLen) { // 当输入字符个数超过限制的大小时，进行截断操作
            s.delete(editStart - 1, editEnd);
            editStart--;
            editEnd--;
        }
        mEditText.setSelection(editStart);
        // 恢复监听器
        mEditText.addTextChangedListener(this);
        if(tvNum!=null){
            tvNum.setText((getInputCount())+"/"+maxLen);
        }
    }
  
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {

    }  
  
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {//英文字符+0.5
                len = len + 0.5;
            } else {//中文+1
                len = len + 1;
            }
        }
        return Math.round(len);
    }

    private long getInputCount() {
        return calculateLength(mEditText.getText().toString());
    }

}  