package cn.qqtheme.framework.wheelpicker;

import android.support.v4.app.FragmentActivity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 性别选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23 11:48
 */
@SuppressWarnings("WeakerAccess")
public class SexPicker extends SinglePicker<String> {
    public static final List<String> DATA_CN = Arrays.asList(
            "男", "女"
    );
    public static final List<String> DATA_EN = Arrays.asList(
            "Male", "Female"
    );

    public SexPicker(FragmentActivity activity) {
        this(activity, false);
    }

    public SexPicker(FragmentActivity activity, boolean includeSecrecy) {
        super(activity);
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        LinkedList<String> data = new LinkedList<>();
        if (isChinese) {
            data.addAll(DATA_CN);
            if (includeSecrecy) {
                data.addFirst("保密");
            }
        } else {
            data.addAll(DATA_EN);
            if (includeSecrecy) {
                data.addFirst("Unlimited");
            }
        }
        setData(data);
    }

}
