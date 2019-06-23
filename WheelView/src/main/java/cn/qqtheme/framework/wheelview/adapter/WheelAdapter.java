package cn.qqtheme.framework.wheelview.adapter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮数据适配器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 20:02
 */
@SuppressWarnings({"unused"})
public class WheelAdapter<T> {
    private List<T> data;

    public WheelAdapter() {
        this.data = new ArrayList<>();
    }

    public int getItemCount() {
        return data.size();
    }

    public T getItem(int position) {
        final int itemCount = getItemCount();
        if (itemCount == 0) {
            return null;
        }
        int index = (position + itemCount) % itemCount;
        return data.get(index);
    }

    public String getItemText(int position, Formatter formatter) {
        T item = getItem(position);
        if (item == null) {
            return "";
        }
        if (formatter != null) {
            return formatter.formatItemText(position, item);
        }
        return item.toString();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public void addData(List<T> data) {
        this.data.addAll(data);
    }

    public int getItemPosition(T item) {
        int position = -1;
        if (data != null) {
            return data.indexOf(item);
        }
        return position;
    }

    public interface Formatter {
        String formatItemText(int position, @NonNull Object object);
    }

}
