package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 两级、三级联动选择器。默认只初始化第一级数据，第二三级数据由联动获得。
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/5/6 20:34
 * Builder:Android Studio
 *
 * @see DataProvider
 */
public class LinkagePicker extends WheelPicker {
    protected String selectedFirstItem = "", selectedSecondItem = "", selectedThirdItem = "";
    protected String firstLabel = "", secondLabel = "", thirdLabel = "";
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;
    protected DataProvider provider;
    private OnLinkageListener onLinkageListener;
    private double firstColumnWeight = 0;//第一级显示的宽度比
    private double secondColumnWeight = 0;//第二级显示的宽度比
    private double thirdColumnWeight = 0;//第三级显示的宽度比
    private OnWheelListener onWheelListener;

    public LinkagePicker(Activity activity) {
        super(activity);
    }

    public LinkagePicker(Activity activity, DataProvider provider) {
        super(activity);
        this.provider = provider;
    }

    /**
     * 二级联动选择器构造函数
     *
     * @deprecated use {@link #LinkagePicker(Activity, DataProvider)} instead
     */
    @Deprecated
    public LinkagePicker(Activity activity, ArrayList<String> firstList,
                         ArrayList<ArrayList<String>> secondList) {
        this(activity, firstList, secondList, null);
    }

    /**
     * 三级联动选择器构造函数
     *
     * @deprecated use {@link #LinkagePicker(Activity, DataProvider)} instead
     */
    @Deprecated
    public LinkagePicker(Activity activity, final ArrayList<String> firstList,
                         final ArrayList<ArrayList<String>> secondList,
                         final ArrayList<ArrayList<ArrayList<String>>> thirdList) {
        super(activity);
        this.provider = new DefaultDataProvider(firstList, secondList, thirdList);
    }

    protected void setProvider(DataProvider provider) {
        this.provider = provider;
    }

    public void setSelectedIndex(int firstIndex, int secondIndex) {
        setSelectedIndex(firstIndex, secondIndex, 0);
    }

    public void setSelectedIndex(int firstIndex, int secondIndex, int thirdIndex) {
        selectedFirstIndex = firstIndex;
        selectedSecondIndex = secondIndex;
        selectedThirdIndex = thirdIndex;
    }

    public void setSelectedItem(String firstText, String secondText) {
        setSelectedItem(firstText, secondText, "");
    }

    public void setSelectedItem(String firstText, String secondText, String thirdText) {
        if (null == provider) {
            throw new IllegalArgumentException("please set data provider at first");
        }
        List<String> firstData = provider.provideFirstData();
        for (int i = 0; i < firstData.size(); i++) {
            String ft = firstData.get(i);
            if (ft.contains(firstText)) {
                selectedFirstIndex = i;
                LogUtils.verbose("init select first text: " + ft + ", index:" + selectedFirstIndex);
                break;
            }
        }
        List<String> secondData = provider.provideSecondData(selectedFirstIndex);
        for (int j = 0; j < secondData.size(); j++) {
            String st = secondData.get(j);
            if (st.contains(secondText)) {
                selectedSecondIndex = j;
                LogUtils.verbose("init select second text: " + st + ", index:" + selectedSecondIndex);
                break;
            }
        }
        if (provider.isOnlyTwo()) {
            return;//仅仅二级联动
        }
        List<String> thirdData = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
        for (int k = 0; k < thirdData.size(); k++) {
            String tt = thirdData.get(k);
            if (tt.contains(thirdText)) {
                selectedThirdIndex = k;
                LogUtils.verbose("init select third text: " + tt + ", index:" + selectedThirdIndex);
                break;
            }
        }
    }

    public void setLabel(String firstLabel, String secondLabel) {
        setLabel(firstLabel, secondLabel, "");
    }

    public void setLabel(String firstLabel, String secondLabel, String thirdLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
        this.thirdLabel = thirdLabel;
    }

    public String getSelectedFirstItem() {
        return selectedFirstItem;
    }

    public String getSelectedSecondItem() {
        return selectedSecondItem;
    }

    public String getSelectedThirdItem() {
        return selectedThirdItem;
    }

    public int getSelectedFirstIndex() {
        return selectedFirstIndex;
    }

    public int getSelectedSecondIndex() {
        return selectedSecondIndex;
    }

    public int getSelectedThirdIndex() {
        return selectedThirdIndex;
    }

    /**
     * 设置每列的宽度比例，将屏幕分为三列，每列范围为0.0～1.0，如0.3333表示约占屏幕的三分之一。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) double firstColumnWeight,
                                @FloatRange(from = 0, to = 1) double secondColumnWeight,
                                @FloatRange(from = 0, to = 1) double thirdColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = thirdColumnWeight;
    }

    /**
     * 设置每列的宽度比例，将屏幕分为两列，每列范围为0.0～1.0，如0.5表示占屏幕的一半。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) double firstColumnWeight,
                                @FloatRange(from = 0, to = 1) double secondColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = 0;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        this.onLinkageListener = onLinkageListener;
    }

    /**
     * 根据比例计算，获取每列的实际宽度。
     * 三级联动默认每列宽度为屏幕宽度的三分之一，两级联动默认每列宽度为屏幕宽度的一半。
     */
    @Size(3)
    protected int[] getColumnWidths(boolean onlyTwoColumn) {
        LogUtils.verbose(this, String.format(java.util.Locale.CHINA, "column weight is: %f-%f-%f"
                , firstColumnWeight, secondColumnWeight, thirdColumnWeight));
        int[] widths = new int[3];
        // fixed: 17-1-7 Equality tests should not be made with floating point values.
        if ((int) firstColumnWeight == 0 && (int) secondColumnWeight == 0
                && (int) thirdColumnWeight == 0) {
            if (onlyTwoColumn) {
                widths[0] = screenWidthPixels / 2;
                widths[1] = widths[0];
                widths[2] = 0;
            } else {
                widths[0] = screenWidthPixels / 3;
                widths[1] = widths[0];
                widths[2] = widths[0];
            }
        } else {
            widths[0] = (int) (screenWidthPixels * firstColumnWeight);
            widths[1] = (int) (screenWidthPixels * secondColumnWeight);
            widths[2] = (int) (screenWidthPixels * thirdColumnWeight);
        }
        return widths;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (null == provider) {
            throw new IllegalArgumentException("please set data provider before make view");
        }
        int[] widths = getColumnWidths(provider.isOnlyTwo());
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView firstView = new WheelView(activity);
        firstView.setTextSize(textSize);
        firstView.setTextColor(textColorNormal, textColorFocus);
        firstView.setLineConfig(lineConfig);
        firstView.setOffset(offset);
        firstView.setCycleDisable(cycleDisable);
        layout.addView(firstView);
        if (TextUtils.isEmpty(firstLabel)) {
            firstView.setLayoutParams(new LinearLayout.LayoutParams(widths[0], WRAP_CONTENT));
        } else {
            firstView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            TextView labelView = new TextView(activity);
            labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            labelView.setTextSize(textSize);
            labelView.setTextColor(textColorFocus);
            labelView.setText(firstLabel);
            layout.addView(labelView);
        }

        final WheelView secondView = new WheelView(activity);
        secondView.setTextSize(textSize);
        secondView.setTextColor(textColorNormal, textColorFocus);
        secondView.setLineConfig(lineConfig);
        secondView.setOffset(offset);
        secondView.setCycleDisable(cycleDisable);
        layout.addView(secondView);
        if (TextUtils.isEmpty(secondLabel)) {
            secondView.setLayoutParams(new LinearLayout.LayoutParams(widths[1], WRAP_CONTENT));
        } else {
            secondView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            TextView labelView = new TextView(activity);
            labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            labelView.setTextSize(textSize);
            labelView.setTextColor(textColorFocus);
            labelView.setText(secondLabel);
            layout.addView(labelView);
        }

        final WheelView thirdView = new WheelView(activity);
        if (!provider.isOnlyTwo()) {
            thirdView.setTextSize(textSize);
            thirdView.setTextColor(textColorNormal, textColorFocus);
            thirdView.setLineConfig(lineConfig);
            thirdView.setOffset(offset);
            thirdView.setCycleDisable(cycleDisable);
            layout.addView(thirdView);
            if (TextUtils.isEmpty(thirdLabel)) {
                thirdView.setLayoutParams(new LinearLayout.LayoutParams(widths[2], WRAP_CONTENT));
            } else {
                thirdView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextSize(textSize);
                labelView.setTextColor(textColorFocus);
                labelView.setText(thirdLabel);
                layout.addView(labelView);
            }
        }

        firstView.setItems(provider.provideFirstData(), selectedFirstIndex);
        firstView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedFirstItem = item;
                selectedFirstIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, selectedFirstItem);
                }
                if (!isUserScroll) {
                    return;
                }
                LogUtils.verbose(this, "change second data after first wheeled");
                selectedSecondIndex = 0;//重置第二级索引
                selectedThirdIndex = 0;//重置第三级索引
                //根据第一级数据获取第二级数据
                List<String> secondData = provider.provideSecondData(selectedFirstIndex);
                secondView.setItems(secondData, selectedSecondIndex);
                if (provider.isOnlyTwo()) {
                    return;//仅仅二级联动
                }
                //根据第二级数据获取第三级数据
                List<String> thirdData = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                thirdView.setItems(thirdData, selectedThirdIndex);
            }
        });

        secondView.setItems(provider.provideSecondData(selectedFirstIndex), selectedSecondIndex);
        secondView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedSecondItem = item;
                selectedSecondIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, selectedSecondItem);
                }
                if (!isUserScroll) {
                    return;
                }
                if (provider.isOnlyTwo()) {
                    return;//仅仅二级联动
                }
                LogUtils.verbose(this, "change third data after second wheeled");
                selectedThirdIndex = 0;//重置第三级索引
                List<String> thirdData = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                //根据第二级数据获取第三级数据
                thirdView.setItems(thirdData, selectedThirdIndex);
            }
        });
        if (provider.isOnlyTwo()) {
            return layout;//仅仅二级联动
        }

        thirdView.setItems(provider.provideThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
        thirdView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedThirdItem = item;
                selectedThirdIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onThirdWheeled(selectedThirdIndex, selectedThirdItem);
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onLinkageListener == null) {
            return;
        }
        if (provider.isOnlyTwo()) {
            onLinkageListener.onPicked(selectedFirstItem, selectedSecondItem, null);
        } else {
            onLinkageListener.onPicked(selectedFirstItem, selectedSecondItem, selectedThirdItem);
        }
    }

    public interface OnLinkageListener {

        void onPicked(String first, String second, String third);

    }

    public static abstract class OnWheelListener {

        public abstract void onFirstWheeled(int index, String item);

        public abstract void onSecondWheeled(int index, String item);

        public void onThirdWheeled(int index, String item) {

        }

    }

    /**
     * 数据提供接口
     */
    public interface DataProvider {

        /**
         * 是否只是二级联动
         */
        boolean isOnlyTwo();

        /**
         * 提供第一级数据
         */
        List<String> provideFirstData();

        /**
         * 提供第二级数据
         */
        List<String> provideSecondData(int firstIndex);

        /**
         * 提供第三级数据
         */
        List<String> provideThirdData(int firstIndex, int secondIndex);

    }

    /**
     * 默认的数据提供者
     */
    public static class DefaultDataProvider implements DataProvider {
        private ArrayList<String> firstList = new ArrayList<>();
        private ArrayList<ArrayList<String>> secondList = new ArrayList<>();
        private ArrayList<ArrayList<ArrayList<String>>> thirdList = new ArrayList<>();
        private boolean onlyTwo = false;

        public DefaultDataProvider(ArrayList<String> firstList, ArrayList<ArrayList<String>> secondList,
                                   ArrayList<ArrayList<ArrayList<String>>> thirdList) {
            this.firstList = firstList;
            this.secondList = secondList;
            if (thirdList == null || thirdList.size() == 0) {
                this.onlyTwo = true;
            } else {
                this.thirdList = thirdList;
            }
        }

        public boolean isOnlyTwo() {
            return onlyTwo;
        }

        @Override
        public List<String> provideFirstData() {
            return firstList;
        }

        @Override
        public List<String> provideSecondData(int firstIndex) {
            return secondList.get(firstIndex);
        }

        @Override
        public List<String> provideThirdData(int firstIndex, int secondIndex) {
            if (onlyTwo) {
                return new ArrayList<>();
            } else {
                return thirdList.get(firstIndex).get(secondIndex);
            }
        }

    }

}
