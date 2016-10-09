package cn.smile.base;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ProgressBar;

import cn.smile.R;
import cn.smile.widget.WebViewBase;

/**查看网页
 * @author smile
 * @date 2015-09-24-17:36
 */
public class WebViewActivity extends BaseActivityWithNavi {

    @Override
    protected String title() {
        String title = getBundle().getString("title");
        if(!TextUtils.isEmpty(title)){
            return title;
        }else{
            return getString(R.string.app_name);
        }
    }

    @Override
    public boolean canSwipeBack() {
        return true;
    }

    @Override
    protected View layout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.base_webview,null);
    }

    WebViewBase webView;
    ProgressBar mProgressBar;

    @Override
    public void initView(){
        super.initView();
        webView = (WebViewBase)findView(R.id.webView);
        mProgressBar = (ProgressBar)findView(R.id.progress);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocus();
        webView.setOnPageFinishedListener(new WebViewBase.OnPageFinishedListener() {

            @Override
            public void OnPageFinished(String url) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        webView.setOnProgressListener(new WebViewBase.OnProgressListener() {

            @Override
            public void onProgress(int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });
        String link = getBundle().getString("link");
        if(link.startsWith("http")){
            webView.loadUrl(link);
        }else{
            webView.loadUrl("https://"+link);
        }
    }
}
