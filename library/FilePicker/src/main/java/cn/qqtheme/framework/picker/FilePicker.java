package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.qqtheme.framework.adapter.FileAdapter;
import cn.qqtheme.framework.entity.FileItem;
import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.util.StorageUtils;
import cn.qqtheme.framework.widget.MarqueeTextView;

/**
 * 文件目录选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /9/29
 */
public class FilePicker extends ConfirmPopup<LinearLayout> implements AdapterView.OnItemClickListener {
    private Mode mode;
    private String initPath;
    private FileAdapter adapter;
    private MarqueeTextView textView;
    private OnFilePickListener onFilePickListener;

    /**
     * The enum Mode.
     */
    public enum Mode {
        /**
         * Directory mode.
         */
        Directory, /**
         * File mode.
         */
        File
    }

    /**
     * Instantiates a new File picker.
     *
     * @param activity the activity
     */
    public FilePicker(Activity activity) {
        super(activity);
        initPath = StorageUtils.getRootPath(activity);
        adapter = new FileAdapter(activity);
    }

    @Override
    protected LinearLayout initContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.WHITE);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        textView = new MarqueeTextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        int padding = ConvertUtils.toDp(activity, 10);
        textView.setPadding(padding, padding, padding, padding);
        rootLayout.addView(textView);
        View lineView = new View(activity);
        lineView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, 1));
        lineView.setBackgroundColor(0xFFDDDDDD);
        rootLayout.addView(lineView);
        ListView listView = new ListView(activity);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(0xFFDDDDDD));
        listView.setDividerHeight(1);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        rootLayout.addView(listView);
        return rootLayout;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode.equals(Mode.Directory)) {
            adapter.setOnlyListDir(true);
        }
    }

    /**
     * Sets root path.
     *
     * @param initPath the init path
     */
    public void setRootPath(String initPath) {
        this.initPath = initPath;
    }

    /**
     * Sets allow extensions.
     *
     * @param allowExtensions the allow extensions
     */
    public void setAllowExtensions(String[] allowExtensions) {
        adapter.setAllowExtensions(allowExtensions);
    }

    /**
     * Sets show up dir.
     *
     * @param showUpDir the show up dir
     */
    public void setShowUpDir(boolean showUpDir) {
        adapter.setShowUpDir(showUpDir);
    }

    /**
     * Sets show home dir.
     *
     * @param showHomeDir the show home dir
     */
    public void setShowHomeDir(boolean showHomeDir) {
        adapter.setShowHomeDir(showHomeDir);
    }

    /**
     * Sets show hide dir.
     *
     * @param showHideDir the show hide dir
     */
    public void setShowHideDir(boolean showHideDir) {
        adapter.setShowHideDir(showHideDir);
    }

    @Override
    protected boolean isFixedHeight() {
        return true;
    }

    @Override
    protected void setContentViewBefore() {
        final boolean isPickFile = mode.equals(Mode.File);
        setCancelVisible(!isPickFile);
        setSubmitText(isPickFile ? "取消" : "确定");
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (isPickFile) {
                    LogUtils.debug("已放弃选择！");
                } else {
                    String currentPath = adapter.getCurrentPath();
                    LogUtils.debug("已选择目录：" + currentPath);
                    if (onFilePickListener != null) {
                        onFilePickListener.onFilePicked(currentPath);
                    }
                }
            }
        });
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        refreshCurrentDirPath(initPath);
    }

    /**
     * 响应选择器的列表项点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        FileItem fileItem = adapter.getItem(position);
        if (fileItem.isDirectory()) {
            refreshCurrentDirPath(fileItem.getPath());
        } else {
            String clickPath = fileItem.getPath();
            if (mode.equals(Mode.Directory)) {
                LogUtils.debug("选择的不是有效的目录: " + clickPath);
            } else {
                dismiss();
                LogUtils.debug("已选择文件：" + clickPath);
                if (onFilePickListener != null) {
                    onFilePickListener.onFilePicked(clickPath);
                }
            }
        }
    }

    private void refreshCurrentDirPath(String currentPath) {
        if (currentPath.equals("/")) {
            textView.setText("根目录");
        } else {
            textView.setText(currentPath);
        }
        adapter.loadData(currentPath);
    }

    /**
     * Sets on file pick listener.
     *
     * @param listener the listener
     */
    public void setOnFilePickListener(OnFilePickListener listener) {
        this.onFilePickListener = listener;
    }

    /**
     * The interface On file pick listener.
     */
    public interface OnFilePickListener {

        /**
         * On file picked.
         *
         * @param currentPath the current path
         */
        void onFilePicked(String currentPath);

    }

}
