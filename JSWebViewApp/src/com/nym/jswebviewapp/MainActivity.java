package com.nym.jswebviewapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity implements OnClickListener {
	private static final int FILECHOOSER_RESULTCODE = 99;
	/**
	 * 其他
	 */
	private WebView mWebView;
	private ValueCallback<Uri> mUploadMessage;	//webView上传文件
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		url = "http://wsq.qq.com/reflow/255443386";
		findViewById(R.id.netBack).setOnClickListener(this);
		findViewById(R.id.netForward).setOnClickListener(this);
		findViewById(R.id.netRefresh).setOnClickListener(this);
		mWebView = (WebView) findViewById(R.id.webView1);
		initWebView();
	}
	
	@SuppressLint({ "JavascriptInterface", "NewApi", "SetJavaScriptEnabled" })
	private void initWebView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.canGoBackOrForward(10);
		mWebView.getSettings().setAllowContentAccess(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		//启用数据库
		mWebView.getSettings().setDatabaseEnabled(true);  
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		//设置数据库路径
		mWebView.getSettings().setDatabasePath(dir);	
		//启用地理定位
		mWebView.getSettings().setGeolocationEnabled(true);		//(定位)
		//设置定位的数据库路径
		mWebView.getSettings().setGeolocationDatabasePath(dir);	//(定位)
		mWebView.getSettings().setDomStorageEnabled(true);		//支持Dom
		// mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
//		 mWebView.getSettings().setUseWideViewPort(true);
		// mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mWebView.setWebChromeClient(new WebChromeClient() {
			// Android > 4.1.1 调用这个方法 webView上传文件
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
//				Log.i("chooser1:acceptType=%s,capture=%s", acceptType + "",
//						capture + "");
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType(acceptType);
				startActivityForResult(
						Intent.createChooser(intent, "完成操作需要使用"),
						FILECHOOSER_RESULTCODE);

			}

			// 3.0 + 调用这个方法webView上传文件
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
//				Log.i("chooser2:acceptType=%s", acceptType + "");
				openFileChooser(uploadMsg, acceptType, "");
			}

			// Android < 3.0 调用这个方法webView上传文件
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {

				openFileChooser(uploadMsg, "", "");

			}
			
			//配置权限(定位)
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, 
			               GeolocationPermissions.Callback callback) {
			    callback.invoke(origin, true, false);
			    super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

		});
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("file:///android_asset/blank.html");
			}

		});

		mWebView.addJavascriptInterface(new JSCall(), "Game");
		loadUrl();
	}

	private void loadUrl() {
		mWebView.clearHistory();
		mWebView.loadUrl(url);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.netBack:
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}
			break;
		case R.id.netForward:
			if (mWebView.canGoForward()) {
				mWebView.goForward();
			}
			break;
		case R.id.netRefresh:
			mWebView.reload();
			break;
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//webView上传文件
		if(mUploadMessage != null)
		{
			Uri result = data == null ? null : data.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
			
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mWebView.stopLoading();
		mWebView.clearCache(true);
		mWebView.destroy();
		super.onBackPressed();
	}
	
	private class JSCall extends Object{
		@JavascriptInterface
		public void loginTimeOutCall()
		{
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
					
				}
			});
		}
	}

}
