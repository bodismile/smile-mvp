package cn.smile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView基类
 * @author smile
 */
public class WebViewBase extends WebView {

	public final static int FILECHOOSER_RESULTCODE = 1;
	private OnCloseUrlListener onCloseUrlListener;
	private OnProgressListener onProgressListener;
	private OnPageFinishedListener onPageFinishedListener;
	private OnUploadFileResultListener onUploadFileResultListener;

	public OnProgressListener getOnProgressListener() {
		return onProgressListener;
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

	/**
	 * 构造函数
	 */
	public WebViewBase(Context context) {
		super(context);
		init();
	}

	/**
	 * 构造函数
	 */
	public WebViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 构造函数
	 */
	public WebViewBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化WebView
	 */
	public void init() {
		WebSettings webSettings = this.getSettings();
		webSettings.setTextSize(WebSettings.TextSize.NORMAL);
		webSettings.setAllowFileAccess(true); // 设置允许访问文件数据
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setSupportZoom(true); // 设置支持缩放
		webSettings.setBuiltInZoomControls(false); //设置显示内置缩放控件
		webSettings.setDefaultZoom(getZoomDensity()); // 根据屏幕密度设置默认缩放
		webSettings.setUseWideViewPort(true); // 设置此属性，可任意比例缩放

		webSettings.setLoadWithOverviewMode(true);
		this.setWebViewClient(getWebViewClient()); // 设置默认WebViewClient
		this.setWebChromeClient(getWebChromeClient()); // 设置默认WebChromeClient
	}

	public void clear() {
		this.setWebViewClient(null);
		this.setWebChromeClient(null);
		this.setDownloadListener(null);
		this.setOnCreateContextMenuListener(null);
	}

	/**
	 * 加载Url
	 */
	@Override
	public void loadUrl(String url) {
		if (url != null && url.length() > 0) {
			if (!url.startsWith("javascript")) {
			}
			super.loadUrl(url);
		}
	}

	/**
	 * @return 根据屏幕密度获取默认缩放
	 */
	public ZoomDensity getZoomDensity() {
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		ZoomDensity zoomDensity = ZoomDensity.MEDIUM;
		switch (screenDensity) {
			case DisplayMetrics.DENSITY_LOW:
				zoomDensity = ZoomDensity.CLOSE;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				zoomDensity = ZoomDensity.MEDIUM;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				zoomDensity = ZoomDensity.FAR;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				zoomDensity = ZoomDensity.FAR;
				break;
			default:
				break;
		}
		return zoomDensity;
	}

	/**
	 * @return Url关闭事件监听
	 */
	public OnCloseUrlListener getOnCloseUrlListener() {
		return onCloseUrlListener;
	}

	/**
	 * @param onCloseUrlListener
	 *            Url关闭事件监听
	 */
	public void setOnCloseUrlListener(OnCloseUrlListener onCloseUrlListener) {
		this.onCloseUrlListener = onCloseUrlListener;
	}

	/**
	 * @return 页面加载完毕事件监听
	 */
	public OnPageFinishedListener getOnPageFinishedListener() {
		return onPageFinishedListener;
	}

	/**
	 * @param onPageFinishedListener
	 *            页面加载完毕事件监听
	 */
	public void setOnPageFinishedListener(
			OnPageFinishedListener onPageFinishedListener) {
		this.onPageFinishedListener = onPageFinishedListener;
	}

	/**
	 * @return the onUploadFileResultListener
	 */
	public OnUploadFileResultListener getOnUploadFileResultListener() {
		return onUploadFileResultListener;
	}

	/**
	 * @param onUploadFileResultListener
	 *            the onUploadFileResultListener to set
	 */
	public void setOnUploadFileResultListener(
			OnUploadFileResultListener onUploadFileResultListener) {
		this.onUploadFileResultListener = onUploadFileResultListener;
	}

	/**
	 * 获取默认WebViewClient，处理各种通知、请求事件
	 */
	protected WebViewClient getWebViewClient() {
		return new WebViewClient() {
			/**
			 * 处理请求的链接，在当前的WebView里跳转，不跳到默认浏览器
			 */
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}

			/**
			 * 加载资源事件，处理附件的下载
			 */
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			/**
			 * 页面开始加载的处理
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// 屏蔽某些Url
				super.onPageStarted(view, url, favicon);

			}

			/**
			 * 页面加载完毕的处理
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				if (onPageFinishedListener != null)
					onPageFinishedListener.OnPageFinished(url);
				super.onPageFinished(view, url);
			}
		};
	}

	public OnProgressListener getProgressChange() {
		return onProgressListener;
	}

	/**
	 * 获取默认WebChromeClient，处理Javascript的对话框
	 */
	public WebChromeClient getWebChromeClient() {
		return new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (onProgressListener != null) {
					onProgressListener.onProgress(newProgress);
				}
			}

			/**
			 * 显示网页中的alert对话框
			 */

			public boolean onJsAlert(WebView view, String url, String message,
									 final JsResult result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("提醒");

				builder.setMessage(message);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								result.confirm();
							}
						});

				builder.create().show();
				return true;
			}

			/**
			 * 显示网页中的确认对话框
			 */

			public boolean onJsConfirm(WebView view, String url,
									   String message, final JsResult result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("提醒");
				// builder.setCancelable(false);
				builder.setMessage(message);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								result.confirm();

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								result.cancel();

							}
						});

				builder.create().show();
				return true;
			}

			/**
			 * 显示网页中带输入的对话框
			 */

			public boolean onJsPrompt(WebView view, String url, String message,
									  String defaultValue, final JsPromptResult result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("提醒");
				builder.setMessage(message);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								result.confirm("123");
							}
						});
				builder.create().show();
				return true;
			}

			/**
			 * 显示文件选择对话框，For Android < 3.0
			 */

			public void openFileChooser(ValueCallback<Uri> uploadFile) {
				openFileChooser(uploadFile, "");
			}

			/**
			 * 显示文件选择对话框，For Android 3.0+
			 */

			public void openFileChooser(ValueCallback<Uri> uploadFile,String acceptType) {
				if (onUploadFileResultListener != null) {
					if (onUploadFileResultListener.getUploadFile() != null)
						return;
					onUploadFileResultListener.setUploadFile(uploadFile);
					Intent intent = new Intent(Intent.ACTION_PICK);
					// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					// i.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("*/*");
					onUploadFileResultListener.openFileChooser(
							Intent.createChooser(intent, "选择附件"),
							FILECHOOSER_RESULTCODE);
				}
			}

			public boolean onCreateWindow(WebView view, boolean dialog,
										  boolean userGesture, android.os.Message resultMsg) {
				((WebViewTransport) resultMsg.obj)
						.setWebView(WebViewBase.this);
				resultMsg.sendToTarget();
				return true;
			}
		};
	}

	public interface OnProgressListener {
		/**
		 * Url关闭事件
		 */
		void onProgress(int newProgress);
	}

	/**
	 * Url关闭事件接口
	 */
	public interface OnCloseUrlListener {
		/**
		 * Url关闭事件
		 */
		void onCloseUrl();
	}

	/**
	 * 页面加载完毕事件接口
	 */
	public interface OnPageFinishedListener {
		/**
		 * 页面加载完毕事件
		 *
		 * @param url
		 *            Url
		 */
		void OnPageFinished(String url);
	}

	/**
	 * 上传附件交互接口
	 */
	public interface OnUploadFileResultListener {
		/**
		 * @return 获取上传的附件
		 */
		ValueCallback<Uri> getUploadFile();

		/**
		 * @param uploadFile
		 *            设置上传的附件
		 */
		void setUploadFile(ValueCallback<Uri> uploadFile);

		/**
		 * 打开文件选择对话框
		 *
		 * @param intent
		 * @param requestCode
		 */
		void openFileChooser(Intent intent, int requestCode);
	}


}
