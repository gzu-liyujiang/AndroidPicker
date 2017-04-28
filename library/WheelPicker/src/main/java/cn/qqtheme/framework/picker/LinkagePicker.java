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

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.LinkageFirst;
import cn.qqtheme.framework.entity.LinkageSecond;
import cn.qqtheme.framework.entity.LinkageThird;
import cn.qqtheme.framework.entity.WheelItem;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 两级、三级联动选择器。默认只初始化第一级数据，第二三级数据由联动获得。
 * <p>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/5/6 20:34
 * Builder:Android Studio
 *
 * @see Provider
 * @see DataProvider
 */
public class LinkagePicker<Fst extends LinkageFirst<Snd>, Snd extends LinkageSecond<Trd>, Trd> extends WheelPicker {
    protected Fst selectedFirstItem;
    protected Snd selectedSecondItem;
    protected Trd selectedThirdItem;
    protected String firstLabel = "", secondLabel = "", thirdLabel = "";
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;
    protected Provider provider;
    private OnPickListener onPickListener;
    private OnLinkageListener onLinkageListener;
    private double firstColumnWeight = 0;//第一级显示的宽度比
    private double secondColumnWeight = 0;//第二级显示的宽度比
    private double thirdColumnWeight = 0;//第三级显示的宽度比
    private OnWheelListener onWheelListener;
    private OnWheelLinkageListener onWheelLinkageListener;

    public LinkagePicker(Activity activity) {
        super(activity);
    }

    public LinkagePicker(Activity activity, DataProvider provider) {
        super(activity);
        this.provider = provider;
    }

    public LinkagePicker(Activity activity, Provider<Fst, Snd, Trd> provider) {
        super(activity);
        this.provider = provider;
    }

    /**
     * 二级联动选择器构造函数
     *
     * @deprecated use {@link #LinkagePicker(Activity, Provider)} instead
     */
    @Deprecated
    public LinkagePicker(Activity activity, List<Fst> firstList, List<List<Snd>> secondList) {
        this(activity, firstList, secondList, null);
    }

    /**
     * 三级联动选择器构造函数
     *
     * @deprecated use {@link #LinkagePicker(Activity, Provider)} instead
     */
    @Deprecated
    public LinkagePicker(Activity activity, List<Fst> f, List<List<Snd>> s, List<List<List<Trd>>> t) {
        super(activity);
        this.provider = new DefaultDataProvider<>(f, s, t);
    }

    protected void setProvider(DataProvider provider) {
        this.provider = provider;
    }

    protected void setProvider(Provider<Fst, Snd, Trd> provider) {
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

    public void setSelectedItem(Fst fst, Snd snd) {
        setSelectedItem(fst, snd, null);
    }

    public void setSelectedItem(Fst fst, Snd snd, Trd trd) {
        if (null == provider) {
            throw new IllegalArgumentException("please set data provider at first");
        }
        List<Fst> fsts = provider.initFirstData();
        int i = 0;
        for (Fst f : fsts) {
            if (f.equals(fst)) {
                selectedFirstIndex = i;
                break;
            } else if (f.getId().equals(fst.getId()) || f.getName().equals(fst.getName())) {
                selectedFirstIndex = i;
                break;
            }
            i++;
        }
        LogUtils.verbose("init select first: " + fst.getName() + ", index:" + selectedFirstIndex);
        List<Snd> snds = provider.linkageSecondData(selectedFirstIndex);
        int j = 0;
        for (Snd s : snds) {
            if (s.equals(snd)) {
                selectedFirstIndex = i;
                break;
            } else if (s.getId().equals(snd.getId()) || s.getName().equals(snd.getName())) {
                selectedSecondIndex = j;
                break;
            }
            j++;
        }
        LogUtils.verbose("init select second: " + snd.getName() + ", index:" + selectedSecondIndex);
        if (provider.isOnlyTwo()) {
            return;//仅仅二级联动
        }
        List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
        int k = 0;
        for (Trd t : trds) {
            if (t.equals(trd)) {
                selectedThirdIndex = k;
                break;
            } else if (t instanceof LinkageThird) {
                LinkageThird ltrd = (LinkageThird) trd;
                LinkageThird lt = (LinkageThird) t;
                if (lt.getId().equals(ltrd.getId()) || lt.getName().equals(ltrd.getName())) {
                    selectedThirdIndex = k;
                    break;
                }
            }
            k++;
        }
        LogUtils.verbose("init select third: " + trd + ", index:" + selectedThirdIndex);
    }

    public void setLabel(String firstLabel, String secondLabel) {
        setLabel(firstLabel, secondLabel, "");
    }

    public void setLabel(String firstLabel, String secondLabel, String thirdLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
        this.thirdLabel = thirdLabel;
    }

    public Fst getSelectedFirstItem() {
        return selectedFirstItem;
    }

    public Snd getSelectedSecondItem() {
        return selectedSecondItem;
    }

    public Trd getSelectedThirdItem() {
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
     * 设置滑动过程数据联动监听器
     */
    public void setOnWheelLinkageListener(OnWheelLinkageListener onWheelLinkageListener) {
        this.onWheelLinkageListener = onWheelLinkageListener;
    }

    /**
     * @deprecated use {@link #setOnWheelLinkageListener} instead
     */
    @Deprecated
    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    /**
     * 设置完成选泽监听器
     */
    public void setOnPickListener(OnPickListener<Fst, Snd, Trd> onPickListener) {
        this.onPickListener = onPickListener;
    }

    /**
     * @deprecated use {@link #setOnPickListener} instead
     */
    @Deprecated
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
        firstView.setDividerConfig(dividerConfig);
        firstView.setOffset(offset);
        firstView.setCycleDisable(cycleDisable);
        firstView.setLabel(firstLabel);
        layout.addView(firstView);
        if (TextUtils.isEmpty(firstLabel)) {
            firstView.setLayoutParams(new LinearLayout.LayoutParams(widths[0], WRAP_CONTENT));
        } else {
            firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        }

        final WheelView secondView = new WheelView(activity);
        secondView.setTextSize(textSize);
        secondView.setTextColor(textColorNormal, textColorFocus);
        secondView.setDividerConfig(dividerConfig);
        secondView.setOffset(offset);
        secondView.setCycleDisable(cycleDisable);
        secondView.setLabel(secondLabel);
        layout.addView(secondView);
        if (TextUtils.isEmpty(secondLabel)) {
            secondView.setLayoutParams(new LinearLayout.LayoutParams(widths[1], WRAP_CONTENT));
        } else {
            secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        }

        final WheelView thirdView = new WheelView(activity);
        if (!provider.isOnlyTwo()) {
            thirdView.setTextSize(textSize);
            thirdView.setTextColor(textColorNormal, textColorFocus);
            thirdView.setDividerConfig(dividerConfig);
            thirdView.setOffset(offset);
            thirdView.setCycleDisable(cycleDisable);
            thirdView.setLabel(thirdLabel);
            layout.addView(thirdView);
            if (TextUtils.isEmpty(thirdLabel)) {
                thirdView.setLayoutParams(new LinearLayout.LayoutParams(widths[2], WRAP_CONTENT));
            } else {
                thirdView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
            }
        }

        final List<Fst> firstData = provider.initFirstData();
        firstView.setItems(firstData, selectedFirstIndex);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstItem = firstData.get(index);
                selectedFirstIndex = index;
                if (onWheelLinkageListener != null) {
                    onWheelLinkageListener.onLinkage(selectedFirstIndex, 0, 0);
                }
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, selectedFirstItem.getName());
                }
                LogUtils.verbose(this, "change second data after first wheeled");
                selectedSecondIndex = 0;//重置第二级索引
                selectedThirdIndex = 0;//重置第三级索引
                //根据第一级数据获取第二级数据
                List<Snd> snds = provider.linkageSecondData(selectedFirstIndex);
                secondView.setItems(snds, selectedSecondIndex);
                if (provider.isOnlyTwo()) {
                    return;//仅仅二级联动
                }
                //根据第二级数据获取第三级数据
                List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                thirdView.setItems(trds, selectedThirdIndex);
            }
        });

        final List<Snd> secondData = provider.linkageSecondData(selectedFirstIndex);
        secondView.setItems(secondData, selectedSecondIndex);
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondItem = secondData.get(index);
                selectedSecondIndex = index;
                if (onWheelLinkageListener != null) {
                    onWheelLinkageListener.onLinkage(selectedFirstIndex, selectedSecondIndex, 0);
                }
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, selectedSecondItem.getName());
                }
                if (provider.isOnlyTwo()) {
                    return;//仅仅二级联动
                }
                LogUtils.verbose(this, "change third data after second wheeled");
                selectedThirdIndex = 0;//重置第三级索引
                List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                //根据第二级数据获取第三级数据
                thirdView.setItems(trds, selectedThirdIndex);
            }
        });
        if (provider.isOnlyTwo()) {
            return layout;//仅仅二级联动
        }

        final List<Trd> thirdData = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
        thirdView.setItems(thirdData, selectedThirdIndex);
        thirdView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedThirdItem = thirdData.get(index);
                selectedThirdIndex = index;
                if (onWheelLinkageListener != null) {
                    onWheelLinkageListener.onLinkage(selectedFirstIndex, selectedSecondIndex, selectedThirdIndex);
                }
                if (onWheelListener != null) {
                    String thirdName;
                    if (selectedThirdItem instanceof LinkageThird) {
                        thirdName = ((LinkageThird) selectedThirdItem).getName();
                    } else {
                        thirdName = selectedThirdItem.toString();
                    }
                    onWheelListener.onThirdWheeled(selectedThirdIndex, thirdName);
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (provider.isOnlyTwo()) {
            if (onPickListener != null) {
                onPickListener.onPicked(selectedFirstItem, selectedSecondItem, null);
            }
            if (onLinkageListener != null) {
                onLinkageListener.onPicked(selectedFirstItem.getName(), selectedSecondItem.getName(), null);
            }
        } else {
            if (onPickListener != null) {
                onPickListener.onPicked(selectedFirstItem, selectedSecondItem, selectedThirdItem);
            }
            if (onLinkageListener != null) {
                String thirdName;
                if (selectedThirdItem instanceof LinkageThird) {
                    thirdName = ((LinkageThird) selectedThirdItem).getName();
                } else {
                    thirdName = selectedThirdItem.toString();
                }
                onLinkageListener.onPicked(selectedFirstItem.getName(), selectedSecondItem.getName(), thirdName);
            }
        }
    }

    /**
     * 数据选择完成监听器
     */
    public interface OnPickListener<Fst, Snd, Trd> {

        void onPicked(Fst first, Snd second, Trd third);

    }

    /**
     * 兼容旧版API
     *
     * @deprecated use {@link OnPickListener} instead
     */
    @Deprecated
    public interface OnLinkageListener {

        void onPicked(String first, String second, String third);

    }

    /**
     * 滑动过程数据联动监听器
     */
    public interface OnWheelLinkageListener {

        void onLinkage(int firstIndex, int secondIndex, int thirdIndex);

    }

    /**
     * 兼容旧版API
     *
     * @deprecated use {@link OnWheelLinkageListener} instead
     */
    @Deprecated
    public static abstract class OnWheelListener {

        public abstract void onFirstWheeled(int index, String item);

        public abstract void onSecondWheeled(int index, String item);

        public void onThirdWheeled(int index, String item) {

        }

    }

    /**
     * 数据提供接口
     */
    public interface Provider<Fst extends LinkageFirst<Snd>, Snd extends LinkageSecond<Trd>, Trd> {

        /**
         * 是否只是二级联动
         */
        boolean isOnlyTwo();

        /**
         * 初始化第一级数据
         */
        @NonNull
        List<Fst> initFirstData();

        /**
         * 根据第一级数据联动第二级数据
         */
        @NonNull
        List<Snd> linkageSecondData(int firstIndex);

        /**
         * 根据第一二级数据联动第三级数据
         */
        @NonNull
        List<Trd> linkageThirdData(int firstIndex, int secondIndex);

    }

    private static class StringLinkageFirst implements LinkageFirst<StringLinkageSecond> {
        private String name;
        private List<StringLinkageSecond> seconds = new ArrayList<>();

        private StringLinkageFirst(String name, List<StringLinkageSecond> seconds) {
            this.name = name;
            this.seconds = seconds;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getId() {
            return name;
        }

        @Override
        public List<StringLinkageSecond> getSeconds() {
            return seconds;
        }
    }

    private static class StringLinkageSecond implements LinkageSecond<String> {
        private String name;
        private List<String> thirds = new ArrayList<>();

        private StringLinkageSecond(String name, List<String> thirds) {
            this.name = name;
            this.thirds = thirds;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getId() {
            return name;
        }

        @Override
        public List<String> getThirds() {
            return thirds;
        }

    }

    /**
     * 兼容旧版API
     */
    public static abstract class DataProvider implements Provider<StringLinkageFirst, StringLinkageSecond, String> {

        public abstract List<String> provideFirstData();

        public abstract List<String> provideSecondData(int firstIndex);

        public abstract List<String> provideThirdData(int firstIndex, int secondIndex);

        @NonNull
        @Override
        public List<StringLinkageFirst> initFirstData() {
            List<StringLinkageFirst> firsts = new ArrayList<>();
            List<String> data = provideFirstData();
            int i = 0;
            for (String str : data) {
                firsts.add(new StringLinkageFirst(str, linkageSecondData(i)));
                i++;
            }
            return firsts;
        }

        @NonNull
        @Override
        public List<StringLinkageSecond> linkageSecondData(int firstIndex) {
            List<StringLinkageSecond> seconds = new ArrayList<>();
            List<String> data = provideSecondData(firstIndex);
            int i = 0;
            for (String str : data) {
                seconds.add(new StringLinkageSecond(str, linkageThirdData(firstIndex, i)));
                i++;
            }
            return seconds;
        }

        @NonNull
        @Override
        public List<String> linkageThirdData(int firstIndex, int secondIndex) {
            return provideThirdData(firstIndex, secondIndex);
        }

    }

    /**
     * 默认的数据提供者
     */
    private static class DefaultDataProvider<Fst extends LinkageFirst<Snd>, Snd extends LinkageSecond<Trd>, Trd> implements Provider<Fst, Snd, Trd> {
        private List<Fst> firstList = new ArrayList<>();
        private List<List<Snd>> secondList = new ArrayList<>();
        private List<List<List<Trd>>> thirdList = new ArrayList<>();
        private boolean onlyTwo = false;

        public DefaultDataProvider(List<Fst> f, List<List<Snd>> s, List<List<List<Trd>>> t) {
            this.firstList = f;
            this.secondList = s;
            if (t == null || t.size() == 0) {
                this.onlyTwo = true;
            } else {
                this.thirdList = t;
            }
        }

        public boolean isOnlyTwo() {
            return onlyTwo;
        }

        @Override
        @NonNull
        public List<Fst> initFirstData() {
            return firstList;
        }

        @Override
        @NonNull
        public List<Snd> linkageSecondData(int firstIndex) {
            return secondList.get(firstIndex);
        }

        @Override
        @NonNull
        public List<Trd> linkageThirdData(int firstIndex, int secondIndex) {
            if (onlyTwo) {
                return new ArrayList<>();
            } else {
                return thirdList.get(firstIndex).get(secondIndex);
            }
        }

    }

}
