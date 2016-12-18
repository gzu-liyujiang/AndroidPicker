package cn.qqtheme.framework.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * @since 2015/9/29
 */
public class FilePicker extends ConfirmPopup<LinearLayout> implements AdapterView.OnItemClickListener {
    public static final int DIRECTORY = 0;
    public static final int FILE = 1;

    private String initPath;
    private FileAdapter adapter;
    private TextView emptyView;
    private MarqueeTextView textView;
    private OnFilePickListener onFilePickListener;
    private int mode;
    private CharSequence emptyHint;

    @IntDef(value = {DIRECTORY, FILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    /**
     * @see #FILE
     * @see #DIRECTORY
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public FilePicker(Activity activity, @Mode int mode) {
        super(activity);
        setHalfScreen(true);
        try {
            this.initPath = StorageUtils.getDownloadPath();
        } catch (RuntimeException e) {
            this.initPath = StorageUtils.getInternalRootPath(activity);
        }
        this.mode = mode;
        this.emptyHint = (mode == DIRECTORY ? "没有所需目录" : "没有所需文件");
        this.adapter = new FileAdapter(activity);
        adapter.setOnlyListDir(mode == DIRECTORY);
        adapter.setShowHideDir(false);
        adapter.setShowHomeDir(false);
        adapter.setShowUpDir(true);
    }

    @Override
    @NonNull
    protected LinearLayout makeCenterView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.WHITE);
        ListView listView = new ListView(activity);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(0xFFDDDDDD));
        listView.setDividerHeight(1);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        rootLayout.addView(listView);
        emptyView = new TextView(activity);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        txtParams.gravity = Gravity.CENTER;
        emptyView.setLayoutParams(txtParams);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        emptyView.setTextColor(Color.BLACK);
        rootLayout.addView(emptyView);
        return rootLayout;
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        textView = new MarqueeTextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        int padding = ConvertUtils.toPx(activity, 10);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    public void setRootPath(String initPath) {
        this.initPath = initPath;
    }

    public void setAllowExtensions(String[] allowExtensions) {
        adapter.setAllowExtensions(allowExtensions);
    }

    public void setShowUpDir(boolean showUpDir) {
        adapter.setShowUpDir(showUpDir);
    }

    public void setShowHomeDir(boolean showHomeDir) {
        adapter.setShowHomeDir(showHomeDir);
    }

    public void setShowHideDir(boolean showHideDir) {
        adapter.setShowHideDir(showHideDir);
    }

    public void setEmptyHint(CharSequence emptyHint) {
        this.emptyHint = emptyHint;
    }

    @Override
    protected void setContentViewBefore() {
        boolean isPickFile = mode == FILE;
        setCancelVisible(!isPickFile);
        setSubmitText(isPickFile ? "取消" : "确定");
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        refreshCurrentDirPath(initPath);
    }

    @Override
    protected void onSubmit() {
        if (mode == FILE) {
            LogUtils.verbose("已放弃选择！");
        } else {
            String currentPath = adapter.getCurrentPath();
            LogUtils.debug("已选择目录：" + currentPath);
            if (onFilePickListener != null) {
                onFilePickListener.onFilePicked(currentPath);
            }
        }
    }

    public FileAdapter getAdapter() {
        return adapter;
    }

    public String getCurrentPath() {
        return adapter.getCurrentPath();
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
            if (mode == DIRECTORY) {
                LogUtils.warn("选择的不是有效的目录: " + clickPath);
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
        int adapterCount = adapter.getCount();
        if (adapterCount <= 1) {
            //仅仅只有返回上一级目录？
            LogUtils.verbose(this, "no files, or dir is empty");
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(emptyHint);
        } else {
            LogUtils.verbose(this, "files or dirs count: " + adapterCount);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void setOnFilePickListener(OnFilePickListener listener) {
        this.onFilePickListener = listener;
    }

    public interface OnFilePickListener {

        void onFilePicked(String currentPath);

    }

}
