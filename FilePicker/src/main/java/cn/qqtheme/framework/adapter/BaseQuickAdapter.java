/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 *
 * @param <T> The type of the items in the list.
 * @param <H> the type parameter
 */
public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

    /**
     * The constant TAG.
     */
    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    /**
     * The Context.
     */
    protected final Context context;

    /**
     * The Layout res id.
     */
    protected final int layoutResId;

    /**
     * The Data.
     */
    protected final List<T> data;

    /**
     * The Display indeterminate progress.
     */
    protected boolean displayIndeterminateProgress = false;

    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        int extra = displayIndeterminateProgress ? 1 : 0;
        return data.size() + extra;
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= data.size() ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            final H helper = getAdapterHelper(position, convertView, parent);
            T item = getItem(position);
            convert(helper, item);
            helper.setAssociatedObject(item);
            return helper.getView();
        }

        return createIndeterminateProgressView(convertView, parent);
    }

    private View createIndeterminateProgressView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            FrameLayout container = new FrameLayout(context);
            container.setForegroundGravity(Gravity.CENTER);
            ProgressBar progress = new ProgressBar(context);
            container.addView(progress);
            convertView = container;
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    /**
     * Add.
     *
     * @param elem the elem
     */
    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    /**
     * Add all.
     *
     * @param elem the elem
     */
    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * Set.
     *
     * @param oldElem the old elem
     * @param newElem the new elem
     */
    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    /**
     * Set.
     *
     * @param index the index
     * @param elem  the elem
     */
    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    /**
     * Remove.
     *
     * @param elem the elem
     */
    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    /**
     * Remove.
     *
     * @param index the index
     */
    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    /**
     * Replace all.
     *
     * @param elem the elem
     */
    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * Contains boolean.
     *
     * @param elem the elem
     * @return the boolean
     */
    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * Show indeterminate progress.
     *
     * @param display the display
     */
    public void showIndeterminateProgress(boolean display) {
        if (display == displayIndeterminateProgress) return;
        displayIndeterminateProgress = display;
        notifyDataSetChanged();
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, T item);

    /**
     * You can override this method to use a custom BaseAdapterHelper in order to fit your needs
     *
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view                    is non-null and of an appropriate type before using. If it is not possible to convert                    this view to display the correct data, this method can create a new view.                    Heterogeneous lists can specify their number of view types, so that this View is                    always of the right type (see {@link #getViewTypeCount()} and                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return An instance of BaseAdapterHelper
     */
    protected abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

}
