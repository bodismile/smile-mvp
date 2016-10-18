package cn.smile.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * EditText的最大长度限制，英文0.5个字符，中文1个字符
 * @author  smile
 */
public class MaxTextWatcher implements TextWatcher {
  
    private int maxLen = 0;  
    private EditText mEditText = null;
    TextView tvNum;
    private int editStart;
    private int editEnd;

    public MaxTextWatcher(int maxLen, EditText editText, TextView tv) {
        this.maxLen = maxLen;  
        this.mEditText = editText;
        this.tvNum = tv;
    }
  
    public void afterTextChanged(Editable s) {
        editStart = mEditText.getSelectionStart();
        editEnd = mEditText.getSelectionEnd();
        mEditText.removeTextChangedListener(this);
        while (calculateLength(s.toString()) > maxLen) {
            s.delete(editStart - 1, editEnd);
            editStart--;
            editEnd--;
        }
        mEditText.setSelection(editStart);
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