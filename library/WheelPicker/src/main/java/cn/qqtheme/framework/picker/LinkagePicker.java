package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Locale;

import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 两级、三级联动选择器。
 * 注：
 * 目前数据全部由构造函数入参来初始化，不太科学。
 * 计划加入provider接口,默认只初始化第一级数据，第二三级数据由事件回调来联动获得。
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/5/6 20:34
 * Builder:Android Studio
 */
public class LinkagePicker extends WheelPicker {
    protected ArrayList<String> firstList = new ArrayList<String>();
    protected ArrayList<ArrayList<String>> secondList = new ArrayList<ArrayList<String>>();
    protected ArrayList<ArrayList<ArrayList<String>>> thirdList = new ArrayList<ArrayList<ArrayList<String>>>();
    protected OnLinkageListener onLinkageListener;
    protected String selectedFirstText = "", selectedSecondText = "", selectedThirdText = "";
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;
    protected boolean onlyTwo = false;//是否只是二级联动
    private double firstColumnWeight = 0;//第一级显示的宽度比
    private double secondColumnWeight = 0;//第二级显示的宽度比
    private double thirdColumnWeight = 0;//第三级显示的宽度比

    public LinkagePicker(Activity activity) {
        super(activity);
    }

    /**
     * 二级联动选择器构造函数
     */
    public LinkagePicker(Activity activity, ArrayList<String> firstList,
                         ArrayList<ArrayList<String>> secondList) {
        this(activity, firstList, secondList, null);
    }

    /**
     * 三级联动选择器构造函数
     */
    public LinkagePicker(Activity activity, ArrayList<String> firstList,
                         ArrayList<ArrayList<String>> secondList,
                         ArrayList<ArrayList<ArrayList<String>>> thirdList) {
        super(activity);
        this.firstList = firstList;
        this.secondList = secondList;
        if (thirdList == null || thirdList.size() == 0) {
            this.onlyTwo = true;
        } else {
            this.thirdList = thirdList;
        }
    }

    public void setSelectedItem(String firstText, String secondText) {
        setSelectedItem(firstText, secondText, "");
    }

    public void setSelectedItem(String firstText, String secondText, String thirdText) {
        for (int i = 0; i < firstList.size(); i++) {
            String ft = firstList.get(i);
            if (ft.contains(firstText)) {
                selectedFirstIndex = i;
                LogUtils.debug("init select first text: " + ft + ", index:" + selectedFirstIndex);
                break;
            }
        }
        ArrayList<String> secondTexts = secondList.get(selectedFirstIndex);
        for (int j = 0; j < secondTexts.size(); j++) {
            String st = secondTexts.get(j);
            if (st.contains(secondText)) {
                selectedSecondIndex = j;
                LogUtils.debug("init select second text: " + st + ", index:" + selectedSecondIndex);
                break;
            }
        }
        if (TextUtils.isEmpty(thirdText) || thirdList.size() == 0) {
            return;//仅仅二级联动
        }
        ArrayList<String> thirdTexts = thirdList.get(selectedFirstIndex).get(selectedSecondIndex);
        for (int k = 0; k < thirdTexts.size(); k++) {
            String tt = thirdTexts.get(k);
            if (tt.contains(thirdText)) {
                selectedThirdIndex = k;
                LogUtils.debug("init select third text: " + tt + ", index:" + selectedThirdIndex);
                break;
            }
        }
    }

    public String getSelectedFirstText() {
        return selectedFirstText;
    }

    public String getSelectedSecondText() {
        return selectedSecondText;
    }

    public String getSelectedThirdText() {
        return selectedThirdText;
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

    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        this.onLinkageListener = onLinkageListener;
    }

    /**
     * 根据比例计算，获取每列的实际宽度。
     * 三级联动默认每列宽度为屏幕宽度的三分之一，两级联动默认每列宽度为屏幕宽度的一半。
     */
    protected final int[] getColumnWidths(boolean onlyTwoColumn) {
        LogUtils.verbose(this, String.format(java.util.Locale.CHINA, "column weight is: %f-%f-%f"
                , firstColumnWeight, secondColumnWeight, thirdColumnWeight));
        int[] widths = new int[3];
        if (firstColumnWeight == 0 && secondColumnWeight == 0 && thirdColumnWeight == 0) {
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
        if (firstList.size() == 0 || secondList.size() == 0) {
            throw new IllegalArgumentException("please initial data at first, can't be empty");
        }
        int[] widths = getColumnWidths(onlyTwo);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView firstView = new WheelView(activity);
        firstView.setLayoutParams(new LinearLayout.LayoutParams(widths[0], WRAP_CONTENT));
        firstView.setTextSize(textSize);
        firstView.setTextColor(textColorNormal, textColorFocus);
        firstView.setLineVisible(lineVisible);
        firstView.setLineColor(lineColor);
        firstView.setOffset(offset);
        layout.addView(firstView);
        final WheelView secondView = new WheelView(activity);
        secondView.setLayoutParams(new LinearLayout.LayoutParams(widths[1], WRAP_CONTENT));
        secondView.setTextSize(textSize);
        secondView.setTextColor(textColorNormal, textColorFocus);
        secondView.setLineVisible(lineVisible);
        secondView.setLineColor(lineColor);
        secondView.setOffset(offset);
        layout.addView(secondView);
        final WheelView thirdView = new WheelView(activity);
        thirdView.setLayoutParams(new LinearLayout.LayoutParams(widths[2], WRAP_CONTENT));
        thirdView.setTextSize(textSize);
        thirdView.setTextColor(textColorNormal, textColorFocus);
        thirdView.setLineVisible(lineVisible);
        thirdView.setLineColor(lineColor);
        thirdView.setOffset(offset);
        layout.addView(thirdView);
        if (onlyTwo) {
            thirdView.setVisibility(View.GONE);
        }
        firstView.setItems(firstList, selectedFirstIndex);
        firstView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedFirstText = item;
                selectedFirstIndex = selectedIndex;
                ArrayList<String> secondData = secondList.get(selectedFirstIndex);
                if (secondData.size() < selectedSecondIndex) {
                    //上一次的第二级选择的项的索引超出了第二级的数据数
                    selectedSecondIndex = 0;
                }
                selectedThirdIndex = 0;
                //根据第一级数据获取第二级数据。若不是用户手动滚动，说明联动需要指定默认项
                secondView.setItems(secondData, selectedSecondIndex);
                if (thirdList.size() == 0) {
                    return;//仅仅二级联动
                }
                //根据第二级数据获取第三级数据
                thirdView.setItems(thirdList.get(selectedFirstIndex).get(selectedSecondIndex), selectedThirdIndex);
            }
        });
        secondView.setItems(secondList.get(selectedFirstIndex), selectedSecondIndex);
        secondView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedSecondText = item;
                selectedSecondIndex = selectedIndex;
                if (thirdList.size() == 0) {
                    return;//仅仅二级联动
                }
                ArrayList<String> thirdData = thirdList.get(selectedFirstIndex).get(selectedSecondIndex);
                if (thirdData.size() < selectedThirdIndex) {
                    //上一次的第三级选择的项的索引超出了第三级的数据数
                    selectedThirdIndex = 0;
                }
                //根据第二级数据获取第三级数据
                thirdView.setItems(thirdData, selectedThirdIndex);
            }
        });
        if (thirdList.size() == 0) {
            return layout;//仅仅二级联动
        }
        thirdView.setItems(thirdList.get(selectedFirstIndex).get(selectedSecondIndex), selectedThirdIndex);
        thirdView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedThirdText = item;
                selectedThirdIndex = selectedIndex;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onLinkageListener != null) {
            if (onlyTwo) {
                onLinkageListener.onPicked(selectedFirstText, selectedSecondText, null);
            } else {
                onLinkageListener.onPicked(selectedFirstText, selectedSecondText, selectedThirdText);
            }
        }
    }

    public interface OnLinkageListener {

        void onPicked(String first, String second, String third);

    }

}
