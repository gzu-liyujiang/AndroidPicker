package cn.qqtheme.framework.wheelview.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.wheelview.interfaces.TextProvider;

/**
 * 滚轮数据适配器
 *
 * @param <Item>
 * @author liyujiang
 * @date 2019/5/14 20:02
 */
public class WheelAdapter<Item> {
    private List<Item> data;

    public WheelAdapter() {
        this(new ArrayList<Item>());
    }

    public WheelAdapter(List<Item> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public int getItemCount() {
        return data.size();
    }

    public Item getItem(int position) {
        final int itemCount = getItemCount();
        return itemCount == 0 ? null : data.get((position + itemCount) % itemCount);
    }

    public String getItemText(int position) {
        Item item = data.get(position);
        if (item instanceof TextProvider) {
            return ((TextProvider) item).provideItemText();
        }
        return String.valueOf(item);
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public void addData(List<Item> data) {
        this.data.addAll(data);
    }

    public int getItemPosition(Item item) {
        int position = -1;
        if (data != null) {
            return data.indexOf(item);
        }
        return position;
    }

}
