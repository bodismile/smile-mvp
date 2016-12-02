package cn.smile.base;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import cn.smile.R;

/**查看网页
 * @author smile
 */
public class WebViewActivity extends BaseActivityWithNavi {

    String url;
    BridgeWebView  webView;
    ProgressBar mProgressBar;

    @Override
    protected String title() {
        url = getBundle().getString("link");
        String title = getBundle().getString("title");
        if(!TextUtils.isEmpty(title)){
            return title;
        }else{
            return getString(cn.smile.R.string.app_name);
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

    @Override
    public void initView(){
        super.initView();
        webView = (BridgeWebView)findView(R.id.webView);
        mProgressBar = (ProgressBar)findView(cn.smile.R.id.progress);
        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new BridgeWebViewClient(webView){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        //注册js内部方法，响应js的点击事件
        webView.registerHandler("visit", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                visitBrowser();
            }
        });
        webView.loadUrl(url);
    }

    /**
     *  访问浏览器
     */
    public void visitBrowser() {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(webView!=null){
                webView.removeAllViews();
                webView.destroy();
                webView=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

