package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class GithubActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://github.com/gzu-liyujiang/AndroidPicker");
    }

}
