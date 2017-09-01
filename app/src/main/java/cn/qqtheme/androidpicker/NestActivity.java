package cn.qqtheme.androidpicker;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import cn.qqtheme.framework.picker.CarNumberPicker;
import cn.qqtheme.framework.picker.LinkagePicker;
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

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_nest);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        findViewById(R.id.nest_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView textView = findView(R.id.wheelview_tips);
        WheelView wheelView = findView(R.id.wheelview_single);
        final String[] strings = {"少数民族", "贵州穿青人", "不在56个少数民族之列", "第57个民族"};
        wheelView.setItems(strings, 2);
        wheelView.setTextColor(0xFFFF00FF);
        wheelView.setTextSize(18);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setRatio((float) (1.0 / 10.0));//线比率
        config.setColor(0xFFFF0000);//线颜色
        config.setAlpha(100);//线透明度
        config.setThick(ConvertUtils.toPx(this, 5));//线粗
        wheelView.setDividerConfig(config);
        wheelView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                textView.setText(String.format(Locale.PRC, "index=%d,item=%s", index, strings[index]));
            }
        });

        final CarNumberPicker picker = new CarNumberPicker(this);
        picker.setOffset(3);
        picker.setOnWheelLinkageListener(new LinkagePicker.OnWheelLinkageListener() {
            @Override
            public void onLinkage(int firstIndex, int secondIndex, int thirdIndex) {
                textView.setText(String.format(Locale.PRC, "%s:%s", picker.getSelectedFirstItem(), picker.getSelectedSecondItem()));
            }
        });
        ViewGroup viewGroup = findView(R.id.wheelview_container);
        //得到选择器视图，可内嵌到其他视图容器，不需要调用show方法
        View pickerContentView = picker.getContentView();
        viewGroup.addView(pickerContentView);
    }

}
