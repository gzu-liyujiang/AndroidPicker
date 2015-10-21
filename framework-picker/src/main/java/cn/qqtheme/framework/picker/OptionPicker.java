package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.qqtheme.framework.helper.Logger;
import cn.qqtheme.framework.view.WheelView.WheelArrayAdapter;
import cn.qqtheme.framework.view.WheelView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author Sai
 */
public class OptionPicker extends WheelPicker<int[]> {
    private WheelView optionView1, optionView2, optionView3;

    public OptionPicker(Activity activity) {
        super(activity);
    }

    @Override
    protected LinearLayout initWheelView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setBackgroundColor(Color.WHITE);
        optionView1 = new WheelView(activity);
        optionView1.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(optionView1);
        optionView2 = new WheelView(activity);
        optionView2.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(optionView2);
        optionView3 = new WheelView(activity);
        optionView3.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(optionView3);
        return rootLayout;
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    @Override
    public int[] getCurrentSelected() {
        int[] currentItems = new int[3];
        currentItems[0] = optionView1.getCurrentItem();
        currentItems[1] = optionView2.getCurrentItem();
        currentItems[2] = optionView3.getCurrentItem();
        Logger.debug(String.format("selected index: %s,%s,%s", currentItems[0], currentItems[1], currentItems[2]));
        return currentItems;
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        optionView1.setCyclic(cyclic);
        optionView2.setCyclic(cyclic);
        optionView3.setCyclic(cyclic);
    }

    public void setCurrentOptions(int index1) {
        setCurrentOptions(index1, -1);
    }

    public void setCurrentOptions(int index1, int index2) {
        setCurrentOptions(index1, index2, -1);
    }

    public void setCurrentOptions(int index1, int index2, int index3) {
        if (index1 != -1) {
            optionView1.setCurrentItem(index1);
        }
        if (index2 != -1) {
            optionView2.setCurrentItem(index2);
        }
        if (index3 != -1) {
            optionView3.setCurrentItem(index3);
        }
    }

    public void setOptions(String[] optionsItems) {
        setOptions(new ArrayList<String>(Arrays.asList(optionsItems)), null, null);
    }

    public void setOptions(ArrayList<String> optionsItems) {
        setOptions(optionsItems, null, null);
    }

    public void setOptions(ArrayList<String> options1Items,
                           ArrayList<ArrayList<String>> options2Items) {
        setOptions(options1Items, options2Items, null);
    }

    public void setOptions(ArrayList<String> options1Items, final ArrayList<ArrayList<String>> options2Items,
                           final ArrayList<ArrayList<ArrayList<String>>> options3Items) {
        int len = WheelArrayAdapter.DEFAULT_LENGTH;
        if (options2Items == null) {
            len = 12;
        }
        if (options3Items == null) {
            len = 8;
        }

        // 选项1
        optionView1.setAdapter(new WheelArrayAdapter<String>(options1Items, len));// 设置显示数据
        optionView1.setCurrentItem(0);// 初始化时显示的数据
        // 选项2
        if (options2Items != null) {
            optionView2.setAdapter(new WheelArrayAdapter<String>(options2Items.get(0)));// 设置显示数据
        }
        optionView2.setCurrentItem(optionView1.getCurrentItem());// 初始化时显示的数据
        // 选项3
        if (options3Items != null) {
            optionView3.setAdapter(new WheelArrayAdapter<String>(options3Items.get(0).get(0)));// 设置显示数据
        }
        optionView3.setCurrentItem(optionView3.getCurrentItem());// 初始化时显示的数据

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = (screenHeight / 100) * 4;

        optionView1.TEXT_SIZE = textSize;
        optionView2.TEXT_SIZE = textSize;
        optionView3.TEXT_SIZE = textSize;

        if (options2Items == null) {
            optionView2.setVisibility(View.GONE);
        }
        if (options3Items == null) {
            optionView3.setVisibility(View.GONE);
        }
        // 添加联动监听
        if (options2Items != null) {
            optionView1.addChangingListener(new WheelView.OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    optionView2.setAdapter(new WheelArrayAdapter<String>(options2Items
                            .get(optionView1.getCurrentItem())));
                    optionView2.setCurrentItem(0);
                    if (options3Items != null) {
                        optionView3.setAdapter(new WheelArrayAdapter<String>(options3Items
                                .get(optionView1.getCurrentItem()).get(
                                        optionView2.getCurrentItem())));
                        optionView3.setCurrentItem(0);
                    }
                }
            });
        }
        if (options3Items != null) {
            optionView2.addChangingListener(new WheelView.OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    optionView3.setAdapter(new WheelArrayAdapter<String>(options3Items
                            .get(optionView1.getCurrentItem()).get(
                                    optionView2.getCurrentItem())));
                    optionView3.setCurrentItem(0);
                }
            });
        }
    }

    public void setLabels(String label1) {
        setLabels(label1, null);
    }

    public void setLabels(String label1, String label2) {
        setLabels(label1, label2, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     * @param label3
     */
    public void setLabels(String label1, String label2, String label3) {
        if (label1 != null) {
            optionView1.setLabel(label1);
        }
        if (label2 != null) {
            optionView2.setLabel(label2);
        }
        if (label3 != null) {
            optionView3.setLabel(label3);
        }
    }

}
