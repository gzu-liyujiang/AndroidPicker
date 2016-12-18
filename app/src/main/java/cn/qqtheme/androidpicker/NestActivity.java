package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 内嵌选择器
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/12/16 00:42
 * Builder:Android Studio
 */
public class NestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nest);
        final TextView textView = (TextView) findViewById(R.id.wheelview_tips);
        WheelView wheelView = (WheelView) findViewById(R.id.wheelview_single);
        wheelView.setItems(new String[]{
                "少数民族",
                "贵州穿青人",
                "不在56个少数民族之列",
                "第57个民族"});
        wheelView.setTextColor(0xFFFF00FF);
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFFF00FF);//线颜色
        config.setAlpha(100);//线透明度
        config.setRatio((float) (1.0 / 10.0));//线比率
        config.setThick(ConvertUtils.toPx(this, 10));//线粗
        wheelView.setLineConfig(config);
        wheelView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                textView.setText("index=" + index + ",item=" + item);
            }
        });
        final TimePicker picker = new TimePicker(this, TimePicker.HOUR_12);
        picker.setOnWheelListener(new TimePicker.OnWheelListener() {
            @Override
            public void onHourWheeled(int index, String hour) {
                textView.setText(hour + ":" + picker.getSelectedMinute());
            }

            @Override
            public void onMinuteWheeled(int index, String minute) {
                textView.setText(picker.getSelectedHour() + ":" + minute);
            }
        });
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.wheelview_container);
        viewGroup.addView(picker.getContentView());
    }

    public void onBack(View view) {
        finish();
    }

}
