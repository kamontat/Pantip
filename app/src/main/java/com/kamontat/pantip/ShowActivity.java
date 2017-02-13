package com.kamontat.pantip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.*;
import com.kamontat.model.Topic;

public class ShowActivity extends AppCompatActivity {
	private Topic topic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);

		topic = (Topic) getIntent().getSerializableExtra("topic");
		settingWebView(topic);
	}

	public void settingWebView(final Topic t) {

		WebView webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				return false;
			}
		});

		webView.loadUrl(topic.getTopicLink());
	}

	@Override
	public void onBackPressed() {
		WebView webView = (WebView) findViewById(R.id.webView);
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}
}
