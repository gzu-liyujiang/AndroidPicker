package cn.qqtheme.framework.wheelpicker;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import cn.qqtheme.framework.wheelview.interfaces.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号前缀选择
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/10 16:44
 * @since 2.0
 */
public class PhoneCodePicker extends SinglePicker<String> {

    public static void showAtBottom(FragmentActivity activity, final TextView anchor) {
        PhoneCodePicker picker = new PhoneCodePicker(activity);
        picker.setSelectCallback(new SelectCallback() {
            @Override
            public void onItemSelected(int position, String item) {
                anchor.setTag(position);
                anchor.setText(item.replaceAll(".*(\\+\\d{2,3})", "$1"));
            }
        });
        Object tag = anchor.getTag();
        if (tag != null) {
            picker.setDefaultItemPosition(Integer.parseInt(tag.toString()));
        }
        picker.showAtBottom();
    }

    public PhoneCodePicker(FragmentActivity activity) {
        super(activity);
        List<String> data = new ArrayList<>();
        data.add("中国大陆+86");
        data.add("香港+852");
        data.add("澳门+853");
        data.add("台湾+886");
        setData(data);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        getConfirmTextView().setTextColor(0xFFF3816F);
    }

    /**
     * 使用{@link #setSelectCallback(SelectCallback)}代替
     *
     * @deprecated
     */
    @Deprecated
    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener<String> onItemSelectedListener) {
        super.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void setSelectCallback(SelectCallback selectCallback) {
        super.setOnItemSelectedListener(selectCallback);
    }

    public interface SelectCallback extends OnItemSelectedListener<String> {
    }

}
