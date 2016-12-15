package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

import cn.qqtheme.framework.picker.TimePicker;
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
        setContentView(R.layout.activity_nest);
        final TextView textView = (TextView) findViewById(R.id.wheelview_tips);
        WheelView wheelView = (WheelView) findViewById(R.id.wheelview_single);
        //noinspection RedundantArrayCreation
        wheelView.setItems(Arrays.asList(new String[]{
                "贵州穿青人",
                "少数民族",
                "不在56少数个民族之列",
                "第57个民族"}));
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

}
