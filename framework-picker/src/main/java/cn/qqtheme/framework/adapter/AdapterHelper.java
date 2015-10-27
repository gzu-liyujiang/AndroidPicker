package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;

/**
 * Allows an abstraction of the ViewHolder pattern.<br>
 * <br>
 * <p/>
 * <b>Usage</b>
 * <p/>
 * <pre>
 * return AdapterHelper.get(context, convertView, parent, R.layout.item)
 *         .setText(R.id.tvName, contact.getName())
 *         .setText(R.id.tvEmails, contact.getEmails().toString())
 *         .setText(R.id.tvNumbers, contact.getNumbers().toString())
 *         .getView();
 * </pre>
 */
public class AdapterHelper {

    /**
     * Views indexed with their IDs
     */
    protected final SparseArray<View> views;

    protected final Context context;

    protected int position;

    protected View convertView;

    /**
     * Package private field to retain the associated user object and detect a change
     */
    protected Object associatedObject;

    protected AdapterHelper(Context context, ViewGroup parent, int layoutRes, int position) {
        this.context = context;
        this.position = position;
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        convertView.setTag(this);
    }

    /**
     * This method is the only entry point to get a AdapterHelper.
     *
     * @param context     The current context.
     * @param convertView The convertView arg passed to the getView() method.
     * @param parent      The parent arg passed to the getView() method.
     * @return A AdapterHelper instance.
     */
    public static AdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutRes) {
        return get(context, convertView, parent, layoutRes, -1);
    }

    /**
     * This method is package private and should only be used by QuickAdapter.
     */
    protected static AdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutRes, int position) {
        if (convertView == null) {
            return new AdapterHelper(context, parent, layoutRes, position);
        }

        // Retrieve the existing helper and update its position
        AdapterHelper existingHelper = (AdapterHelper) convertView.getTag();
        existingHelper.position = position;
        return existingHelper;
    }

    /**
     * This method allows you to retrieve a view and perform custom
     * operations on it, not covered by the AdapterHelper.<br/>
     * If you think it's a common use case, please consider creating
     * a new issue at https://github.com/JoanZapata/base-adapter-helper/issues.
     *
     * @param viewId The id of the view you want to retrieve.
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setBackgroundResource(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setTextColorResource(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public AdapterHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Will download an image from a URL or load an image from a path and put it in an ImageView.<br/>
     * Modified by liyujiang at 2015.08.01
     *
     * @param viewId   The view id.
     * @param imageUrl The image URL.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setImageUri(int viewId, String imageUrl) {
        ImageView view = retrieveView(viewId);
        //ImageLoader.getInstance(context).loadBitmap(imageUrl, view);
        return this;
    }

    /**
     * Add by liyujiang at 2015.08.01
     *
     * @param viewId
     * @param imageUrl
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public AdapterHelper setImageUri(int viewId, String imageUrl, int maxWidth, int maxHeight) {
        ImageView view = retrieveView(viewId);
        //ImageLoader.getInstance(context).loadBitmap(imageUrl, view, maxWidth, maxHeight);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public AdapterHelper setAlpha(int viewId, float value) {
        View view = retrieveView(viewId);
        // Pre-honeycomb hack to set Alpha value
        AlphaAnimation alpha = new AlphaAnimation(value, value);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        view.startAnimation(alpha);
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the text size to the given viewId.
     */
    public AdapterHelper setTextSize(int viewId, float size) {
        TextView view = retrieveView(viewId);
        view.setTextSize(size);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public AdapterHelper setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public AdapterHelper setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setProgress(int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setRating(int viewId, float rating, int max) {
        RatingBar view = retrieveView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Add by liyujiang at 2015.07.01
     * Sets the on checked change listener of the view.
     *
     * @param viewId
     * @param listener
     * @return
     */
    public AdapterHelper setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = retrieveView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setChecked(int viewId, boolean checked) {
        View view = retrieveView(viewId);
        // FIXME: 2015/10/26 View不能强制转换为Checkable, thanks https://github.com/Flywhiter
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The AdapterHelper for chaining.
     */
    public AdapterHelper setAdapter(int viewId, Adapter adapter) {
        AdapterView view = retrieveView(viewId);
        //noinspection unchecked
        view.setAdapter(adapter);
        return this;
    }

    /**
     * Retrieve the convertView
     */
    public View getView() {
        return convertView;
    }

    /**
     * Retrieve the overall position of the data in the list.
     *
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the this helper.
     */
    public int getPosition() {
        if (position == -1) {
            throw new IllegalStateException("Use AdapterHelper constructor " +
                    "with position if you need to retrieve the position.");
        }
        return position;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Retrieves the last converted object on this view.
     */
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /**
     * Should be called during convert
     */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }

}
