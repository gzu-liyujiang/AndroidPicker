package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qqtheme.framework.helper.LogUtils;
import cn.qqtheme.framework.view.WheelView;
import cn.qqtheme.framework.view.WheelView.WheelArrayAdapter;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/9/29
 * Created By Android Studio
 */
public class OptionPicker extends WheelPicker<int[]> {
    private WheelView optionView1, optionView2, optionView3;

    public OptionPicker(Activity activity) {
        super(activity);
    }

    @Override
    protected LinearLayout initWheelView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
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
     */
    @Override
    protected int[] getCurrentItem() {
        int[] currentItems = new int[3];
        currentItems[0] = optionView1.getCurrentItem();
        currentItems[1] = optionView2.getCurrentItem();
        currentItems[2] = optionView3.getCurrentItem();
        LogUtils.debug(String.format("selected index: %s,%s,%s", currentItems[0], currentItems[1], currentItems[2]));
        return currentItems;
    }

    @Override
    public void setCyclic(boolean cyclic) {
        optionView1.setCyclic(cyclic);
        optionView2.setCyclic(cyclic);
        optionView3.setCyclic(cyclic);
    }

    @Override
    public void setScrollingDuration(int scrollingDuration) {
        optionView1.setScrollingDuration(scrollingDuration);
        optionView2.setScrollingDuration(scrollingDuration);
        optionView3.setScrollingDuration(scrollingDuration);
    }

    public void setSelectedOption(int index1) {
        setSelectedOption(index1, -1);
    }

    public void setSelectedOption(int index1, int index2) {
        setSelectedOption(index1, index2, -1);
    }

    public void setSelectedOption(int index1, int index2, int index3) {
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
        setOptions(new ArrayList<String>(Arrays.asList(optionsItems)));
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
        int len;
        if (options3Items != null) {
            len = 4;//三级联动选择
        } else if (options2Items != null) {
            len = 10;//二级联动选择
        } else {
            len = 20;//单项选择
        }
        // 选项1
        optionView1.setAdapter(new OptionAdapter(options1Items, len));// 设置显示数据
        optionView1.setCurrentItem(0);// 初始化时显示的数据
        // 选项2
        if (options2Items != null) {
            optionView2.setAdapter(new OptionAdapter(options2Items.get(0), len));// 设置显示数据
        }
        optionView2.setCurrentItem(optionView1.getCurrentItem());// 初始化时显示的数据
        // 选项3
        if (options3Items != null) {
            optionView3.setAdapter(new OptionAdapter(options3Items.get(0).get(0), len));// 设置显示数据
        }
        optionView3.setCurrentItem(optionView3.getCurrentItem());// 初始化时显示的数据

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = (int) ((screenHeight / 100) * 2.5f);
        optionView1.setTextSize(textSize);
        optionView2.setTextSize(textSize);
        optionView3.setTextSize(textSize);

        if (options2Items == null) {
            optionView2.setVisibility(View.GONE);
        }
        if (options3Items == null) {
            optionView3.setVisibility(View.GONE);
        }
        final int finalLen = len;
        // 添加联动监听
        if (options2Items != null) {
            optionView1.addChangingListener(new WheelView.OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    optionView2.setAdapter(new OptionAdapter(options2Items
                            .get(optionView1.getCurrentItem()), finalLen));
                    optionView2.setCurrentItem(0);
                    if (options3Items != null) {
                        optionView3.setAdapter(new OptionAdapter(options3Items
                                .get(optionView1.getCurrentItem()).get(
                                        optionView2.getCurrentItem()), finalLen));
                        optionView3.setCurrentItem(0);
                    }
                }
            });
        }
        if (options3Items != null) {
            optionView2.addChangingListener(new WheelView.OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    optionView3.setAdapter(new OptionAdapter(options3Items
                            .get(optionView1.getCurrentItem()).get(
                                    optionView2.getCurrentItem()), finalLen));
                    optionView3.setCurrentItem(0);
                }
            });
        }
    }

    private class OptionAdapter extends WheelArrayAdapter<String> {

        public OptionAdapter(ArrayList<String> items, int length) {
            super(items, length);
        }

        @Override
        public String getItem(int index) {
            String item = super.getItem(index);
            // FIXME: 2015/10/23 如果名称较长，整个文字排版上就容易有问题，故截取显条目字数
            if (optionView3.isShown()) {
                item = cutString(item, 4, "...");
            } else if (optionView2.isShown()) {
                item = cutString(item, 10, "...");
            } else {
                item = cutString(item, 20, "...");
            }
            return item;
        }

        private String cutString(String text, int length, String endWith) {
            //GBK每个汉字长2，UTF-8每个汉字长度为3
            final String CHINESE_ENCODE = "UTF-8";
            final int CHINESE_LENGTH = 3;
            int textLength = text.length();
            int byteLength = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < textLength && byteLength < length * CHINESE_LENGTH; i++) {
                String str_i = text.substring(i, i + 1);
                if (str_i.getBytes().length == 1) {
                    byteLength++;//英文
                } else {
                    byteLength += CHINESE_LENGTH;//中文
                }
                sb.append(str_i);
            }
            try {
                if (byteLength < text.getBytes(CHINESE_ENCODE).length) {
                    sb.append(endWith);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

    }

}
