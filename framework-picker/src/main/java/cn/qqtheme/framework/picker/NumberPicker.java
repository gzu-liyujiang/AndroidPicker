package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.qqtheme.framework.helper.LogUtils;
import cn.qqtheme.framework.view.WheelView;

/**
 * 描述
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/24
 * Created By Android Studio
 */
public class NumberPicker extends WheelPicker<Integer> {
    private WheelView wheelView;
    private ArrayList<String> items = new ArrayList<String>();

    public NumberPicker(Activity activity) {
        super(activity);
    }

    @Override
    protected LinearLayout initWheelView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.WHITE);
        wheelView = new WheelView(activity);
        wheelView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.addView(wheelView);
        return rootLayout;
    }

    @Override
    protected Integer getCurrentItem() {
        int size = items.size();
        if (size == 0) {
            return 0;
        }
        int index = wheelView.getCurrentItem();
        LogUtils.debug(String.format("selected index: %s", index));
        return Integer.parseInt(items.get(index));
    }

    public void setRange(int startNumber, int endNumber) {
        setRange(startNumber, endNumber, 1);
    }

    public void setRange(int startNumber, int endNumber, int step) {
        for (int i = startNumber; i <= endNumber; i = i + step) {
            items.add(String.valueOf(i));
        }
        wheelView.setAdapter(new NumberAdapter(items));
        wheelView.setCurrentItem(0);
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        wheelView.setTextSize((int) ((screenHeight / 100) * 3.5f));
    }


    @Override
    public void setCyclic(boolean cyclic) {
        wheelView.setCyclic(cyclic);
    }

    @Override
    public void setScrollingDuration(int scrollingDuration) {
        wheelView.setScrollingDuration(scrollingDuration);
    }

    public void setSelectedNumber(int number) {
        int size = items.size();
        if (size == 0) {
            wheelView.setCurrentItem(0);
        }
        for (int i = 0; i < size; i++) {
            if (items.get(i).equals(String.valueOf(number))) {
                wheelView.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 设置选项的单位
     */
    public void setLabel(String label) {
        if (label != null) {
            wheelView.setLabel(label);
        }
    }

    private class NumberAdapter extends WheelView.WheelArrayAdapter<String> {

        public NumberAdapter(ArrayList<String> items) {
            super(items);
        }

    }

}

