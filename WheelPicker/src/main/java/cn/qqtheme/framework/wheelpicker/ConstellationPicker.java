package cn.qqtheme.framework.wheelpicker;

import android.support.v4.app.FragmentActivity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 星座选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/17 13:10
 */
@SuppressWarnings("WeakerAccess")
public class ConstellationPicker extends SinglePicker<String> {
    public static final List<String> DATA_CN = Arrays.asList(
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
    );
    public static final List<String> DATA_EN = Arrays.asList(
            "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
            "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
    );

    public ConstellationPicker(FragmentActivity activity) {
        this(activity, false);
    }

    public ConstellationPicker(FragmentActivity activity, boolean includeUnlimited) {
        super(activity);
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        LinkedList<String> data = new LinkedList<>();
        if (isChinese) {
            data.addAll(DATA_CN);
            if (includeUnlimited) {
                data.addFirst("不限");
            }
        } else {
            data.addAll(DATA_EN);
            if (includeUnlimited) {
                data.addFirst("Unlimited");
            }
        }
        setData(data);
    }

}
