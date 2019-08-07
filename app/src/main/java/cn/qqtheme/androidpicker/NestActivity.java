package cn.qqtheme.androidpicker;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import cn.qqtheme.framework.picker.CarNumberPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
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
        config.setRatio(1.0f / 10.0f);//线比率
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

        LinearLayout layout = findView(R.id.wheelview_container);
        final CarNumberPicker carNumberPicker = new CarNumberPicker(this);
        carNumberPicker.setOffset(3);
        carNumberPicker.setUseWeight(true);
        carNumberPicker.setShadowColor(0xFFCCCCCC);
        carNumberPicker.setDividerRatio(WheelView.DividerConfig.FILL);
        carNumberPicker.setOnWheelLinkageListener(new CarNumberPicker.OnWheelLinkageListener() {
            @Override
            public void onLinkage(int firstIndex, int secondIndex, int thirdIndex) {
                textView.setText(String.format(Locale.PRC, "%s:%s", carNumberPicker.getSelectedFirstItem(), carNumberPicker.getSelectedSecondItem()));
            }
        });
        layout.addView(carNumberPicker.getContentView());
        final DatePicker datePicker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
        datePicker.setOffset(4);
        datePicker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                textView.setText(String.format("%s年%s月%s日", year, datePicker.getSelectedMonth(), datePicker.getSelectedDay()));
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                textView.setText(String.format("%s年%s月%s日", datePicker.getSelectedYear(), month, datePicker.getSelectedDay()));
            }

            @Override
            public void onDayWheeled(int index, String day) {
                textView.setText(String.format("%s年%s月%s日", datePicker.getSelectedYear(), datePicker.getSelectedMonth(), day));
            }
        });
        //得到选择器视图，可内嵌到其他视图容器，不需要调用show方法
        layout.addView(datePicker.getContentView());
    }

}
