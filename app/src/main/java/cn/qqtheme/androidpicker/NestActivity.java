package cn.qqtheme.androidpicker;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.qqtheme.framework.entity.WheelItem;
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
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xFFFF00FF);//线颜色
        config.setAlpha(100);//线透明度
        config.setRatio((float) (1.0 / 10.0));//线比率
        config.setThick(ConvertUtils.toPx(this, 10));//线粗
        wheelView.setDividerConfig(config);
        wheelView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, WheelItem item) {
                textView.setText("index=" + index + ",item=" + item.getName());
            }
        });

        picker = new CarNumberPicker(this);
        picker.setOffset(3);
        picker.setOnWheelLinkageListener(new LinkagePicker.OnWheelLinkageListener() {
            @Override
            public void onLinkage(int firstIndex, int secondIndex, int thirdIndex) {
                textView.setText(picker.getSelectedFirstItem() + ":" + picker.getSelectedSecondItem());
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
