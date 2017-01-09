package cn.qqtheme.androidpicker;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.qqtheme.framework.picker.CarNumberPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 内嵌选择器
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/12/16 00:42
 * Builder:Android Studio
 */
public class NestActivity extends BaseActivity {
    private CarNumberPicker picker;

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_nest);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        final TextView textView = findView(R.id.wheelview_tips);
        WheelView wheelView = findView(R.id.wheelview_single);
        wheelView.setItems(new String[]{"少数民族", "贵州穿青人", "不在56个少数民族之列", "第57个民族"}, 1);
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

        picker = new CarNumberPicker(this);
        picker.setOffset(3);
        picker.setOnWheelListener(new CarNumberPicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                textView.setText(item + ":" + picker.getSelectedSecondItem());
            }

            @Override
            public void onSecondWheeled(int index, String item) {
                textView.setText(picker.getSelectedFirstItem() + ":" + item);
            }
        });
        ViewGroup viewGroup = findView(R.id.wheelview_container);
        viewGroup.addView(picker.getContentView());

        findViewById(R.id.nest_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.nest_carnumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show();
            }
        });
    }

}
