package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import cn.qqtheme.androidpicker.R;

/**
 * 描述
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/22
 * Created By Android Studio
 */
public class GithubActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://github.com/gzu-liyujiang/AndroidPicker");
    }

}
