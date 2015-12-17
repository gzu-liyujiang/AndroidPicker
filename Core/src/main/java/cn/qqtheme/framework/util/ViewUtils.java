package cn.qqtheme.framework.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * 描述
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/7/19
 *          Created by IntelliJ IDEA 14.1
 */
public class ViewUtils {

    /**
     * ScrollView嵌套ListView，需动态设置高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        int totalHeight = calculateHeight(listView);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * 计算view的高
     *
     * @param view
     * @return
     */
    public static int calculateHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        if (view instanceof ListView) {
            ListView listView = (ListView) view;
            Adapter adapter = listView.getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    View convertView = adapter.getView(i, null, listView);
                    convertView.measure(widthMeasureSpec, heightMeasureSpec);
                    totalHeight += convertView.getMeasuredHeight();
                }
                totalHeight += listView.getDividerHeight() * (count - 1);
                LogUtils.debug("ListView height is " + totalHeight + " dp");
            }
        } else if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                totalHeight += calculateHeight(scrollView.getChildAt(i));
            }
        } else if (view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            if (layout.getOrientation() == LinearLayout.VERTICAL) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    totalHeight += calculateHeight(layout.getChildAt(i));
                }
            } else {
                layout.measure(widthMeasureSpec, heightMeasureSpec);
                totalHeight += layout.getMeasuredHeight();
            }
            LogUtils.debug("LinearLayout height is " + totalHeight + " dp");
        } else {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            totalHeight += view.getMeasuredHeight();
            LogUtils.debug("View height is " + totalHeight + "");
        }
        LogUtils.debug("View height is " + totalHeight + "");
        return totalHeight;
    }

    /**
     * 获取R.*.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    private static int R(Context context, String className, String name) {
        return context.getResources().getIdentifier(name, className, context.getApplicationContext().getPackageName());
    }

    /**
     * 获取R.layout.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    public static int layoutRes(Context context, String name) {
        return R(context, "layout", name);
    }

    /**
     * 获取R.id.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    public static int idRes(Context context, String name) {
        return R(context, "id", name);
    }

    /**
     * 获取R.drawable.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    public static int drawableRes(Context context, String name) {
        return R(context, "drawable", name);
    }

    /**
     * 获取R.string.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    public static int stringRes(Context context, String name) {
        return R(context, "string", name);
    }

    /**
     * 获取R.color.**的唯一值
     *
     * @param context
     * @param name
     * @return
     */
    public static int colorRes(Context context, String name) {
        return R(context, "color", name);
    }

    public static int styleRes(Context context, String name) {
        return R(context, "style", name);
    }

    public static int dimenRes(Context context, String name) {
        return R(context, "dimen", name);
    }

    public static int mipmapRes(Context context, String name) {
        return R(context, "mipmap", name);
    }

}
