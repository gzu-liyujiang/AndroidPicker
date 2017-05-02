package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
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
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 两级、三级联动选择器。默认只初始化第一级数据，第二三级数据由联动获得。
 * <p/>
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
    protected float firstColumnWeight = 1.0f;//第一级显示的宽度比重
    protected float secondColumnWeight = 1.0f;//第二级显示的宽度比重
    protected float thirdColumnWeight = 1.0f;//第三级显示的宽度比重
    private OnPickListener onPickListener;
    private OnLinkageListener onLinkageListener;
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
        //noinspection unchecked
        List<Fst> fsts = provider.initFirstData();
        int i = 0;
        for (Fst f : fsts) {
            if (f.equals(fst)) {
                selectedFirstIndex = i;
                break;
            } else if (f.getId().equals(fst.getId()) || f.getName().contains(fst.getName())) {
                selectedFirstIndex = i;
                break;
            }
            i++;
        }
        LogUtils.verbose("init select first: " + fst.getName() + ", index:" + selectedFirstIndex);
        //noinspection unchecked
        List<Snd> snds = provider.linkageSecondData(selectedFirstIndex);
        int j = 0;
        for (Snd s : snds) {
            if (s.equals(snd)) {
                selectedFirstIndex = i;
                break;
            } else if (s.getId().equals(snd.getId()) || s.getName().contains(snd.getName())) {
                selectedSecondIndex = j;
                break;
            }
            j++;
        }
        LogUtils.verbose("init select second: " + snd.getName() + ", index:" + selectedSecondIndex);
        if (provider.isOnlyTwo()) {
            return;//仅仅二级联动
        }
        //noinspection unchecked
        List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
        int k = 0;
        for (Trd t : trds) {
            if (t.equals(trd)) {
                selectedThirdIndex = k;
                break;
            } else if (t instanceof LinkageThird) {
                LinkageThird ltrd = (LinkageThird) trd;
                LinkageThird lt = (LinkageThird) t;
                if (lt.getId().equals(ltrd.getId()) || lt.getName().contains(ltrd.getName())) {
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
     * 设置每列的宽度比例，将屏幕分为三列，每列范围为0.0～1.0，如0.3333表示约占宽度的三分之一。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) float firstColumnWeight,
                                @FloatRange(from = 0, to = 1) float secondColumnWeight,
                                @FloatRange(from = 0, to = 1) float thirdColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = thirdColumnWeight;
    }

    /**
     * 设置每列的宽度比例，将屏幕分为两列，每列范围为0.0～1.0，如0.5表示占宽度的一半。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) float firstColumnWeight,
                                @FloatRange(from = 0, to = 1) float secondColumnWeight) {
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
     * 设置完成选泽监听器
     */
    public void setOnStringPickListener(OnStringPickListener onStringPickListener) {
        this.onPickListener = onStringPickListener;
    }

    /**
     * @deprecated use {@link #setOnStringPickListener} instead
     */
    @Deprecated
    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        this.onLinkageListener = onLinkageListener;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (null == provider) {
            throw new IllegalArgumentException("please set data provider before make view");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView firstView = createWheelView();
        firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, firstColumnWeight));
        //firstView.setLabel(firstLabel);
        layout.addView(firstView);
        if (!TextUtils.isEmpty(firstLabel)) {
            TextView labelView = createLabelView();
            labelView.setText(firstLabel);
            layout.addView(labelView);
        }

        final WheelView secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, secondColumnWeight));
        //secondView.setLabel(secondLabel);
        layout.addView(secondView);
        if (!TextUtils.isEmpty(secondLabel)) {
            TextView labelView = createLabelView();
            labelView.setText(secondLabel);
            layout.addView(labelView);
        }

        final WheelView thirdView = createWheelView();
        if (!provider.isOnlyTwo()) {
            thirdView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, thirdColumnWeight));
            //thirdView.setLabel(thirdLabel);
            layout.addView(thirdView);
            if (!TextUtils.isEmpty(thirdLabel)) {
                TextView labelView = createLabelView();
                labelView.setText(thirdLabel);
                layout.addView(labelView);
            }
        }

        firstView.setItems(provider.initFirstData(), selectedFirstIndex);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                //noinspection unchecked
                selectedFirstItem = (Fst) provider.initFirstData().get(index);
                selectedFirstIndex = index;
                LogUtils.verbose(this, "change second data after first wheeled");
                selectedSecondIndex = 0;//重置第二级索引
                selectedThirdIndex = 0;//重置第三级索引
                //根据第一级数据获取第二级数据
                //noinspection unchecked
                List<Snd> snds = provider.linkageSecondData(selectedFirstIndex);
                selectedSecondItem = snds.get(selectedSecondIndex);
                secondView.setItems(snds, selectedSecondIndex);
                if (!provider.isOnlyTwo()) {
                    //根据第二级数据获取第三级数据
                    //noinspection unchecked
                    List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                    selectedThirdItem = trds.get(selectedThirdIndex);
                    thirdView.setItems(trds, selectedThirdIndex);
                }
                if (onWheelLinkageListener != null) {
                    onWheelLinkageListener.onLinkage(selectedFirstIndex, 0, 0);
                }
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, selectedFirstItem.getName());
                }
            }
        });

        secondView.setItems(provider.linkageSecondData(selectedFirstIndex), selectedSecondIndex);
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                //noinspection unchecked
                selectedSecondItem = (Snd) provider.linkageSecondData(selectedFirstIndex).get(index);
                selectedSecondIndex = index;
                if (!provider.isOnlyTwo()) {
                    LogUtils.verbose(this, "change third data after second wheeled");
                    selectedThirdIndex = 0;//重置第三级索引
                    //noinspection unchecked
                    List<Trd> trds = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                    selectedThirdItem = trds.get(selectedThirdIndex);
                    //根据第二级数据获取第三级数据
                    thirdView.setItems(trds, selectedThirdIndex);
                }
                if (onWheelLinkageListener != null) {
                    onWheelLinkageListener.onLinkage(selectedFirstIndex, selectedSecondIndex, 0);
                }
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, selectedSecondItem.getName());
                }
            }
        });
        if (provider.isOnlyTwo()) {
            return layout;//仅仅二级联动
        }

        thirdView.setItems(provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
        thirdView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                //noinspection unchecked
                selectedThirdItem = (Trd) provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex).get(index);
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
                //noinspection unchecked
                onPickListener.onPicked(selectedFirstItem, selectedSecondItem, null);
            }
            if (onLinkageListener != null) {
                onLinkageListener.onPicked(selectedFirstItem.getName(), selectedSecondItem.getName(), null);
            }
        } else {
            if (onPickListener != null) {
                //noinspection unchecked
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
     * 数据选择完成监听器
     */
    public static abstract class OnStringPickListener implements OnPickListener<StringLinkageFirst, StringLinkageSecond, String> {

        public abstract void onPicked(String first, String second, String third);

        @Override
        public void onPicked(StringLinkageFirst first, StringLinkageSecond second, String third) {
            onPicked(first.getName(), second.getName(), third);
        }

    }

    /**
     * 兼容旧版API
     *
     * @deprecated use {@link OnStringPickListener} instead
     */
    @Deprecated
    public static abstract class OnLinkageListener extends OnStringPickListener {

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
