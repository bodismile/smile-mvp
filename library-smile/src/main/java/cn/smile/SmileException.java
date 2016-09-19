package cn.smile;

/**
 * Created by smile on 2016/7/8.
 */
public class SmileException extends Exception{

    private int code;

    public SmileException(String msg){
        super(msg);
        this.code=0;
    }

    public SmileException(Throwable e){
        super(e);
        this.code =0;
    }

    public SmileException(int code,String msg){
        super(msg);
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
