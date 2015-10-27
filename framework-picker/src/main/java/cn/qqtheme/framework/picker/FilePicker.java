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
import cn.qqtheme.framework.helper.Common;
import cn.qqtheme.framework.helper.LogUtils;
import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.view.MarqueeTextView;

/**
 * 文件目录选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/9/29
 * Created By Android Studio
 */
public class FilePicker extends ConfirmPopup<LinearLayout> implements AdapterView.OnItemClickListener {
    private Mode mode;
    private String initPath;
    private FileAdapter adapter;
    private MarqueeTextView textView;
    private OnFilePickListener onFilePickListener;

    public enum Mode {
        Directory, File
    }

    public FilePicker(Activity activity) {
        super(activity);
        initPath = Common.getRootPath(activity);
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
        textView.setPadding(8, 8, 8, 8);
        rootLayout.addView(textView);
        ListView listView = new ListView(activity);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(0xDDDDDDDD));
        listView.setDividerHeight(1);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        rootLayout.addView(listView);
        return rootLayout;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode.equals(Mode.Directory)) {
            adapter.setOnlyListDir(true);
        }
    }

    public void setInitPath(String initPath) {
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

    @Override
    protected void setContentViewBefore() {
        setCancelVisible(false);
        final boolean isPickFile = mode.equals(Mode.File);
        setSubmitText(isPickFile ? "取消" : "确定");
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (isPickFile) {
                    LogUtils.debug("已放弃选择！");
                    if (onFilePickListener != null) {
                        onFilePickListener.onCancel();
                    }
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
        setHeight(screenHeight / 2);
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
        adapter.loadData(currentPath, true);
    }

    @Deprecated
    @Override
    public final void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        throw new RuntimeException("Please use OnFilePickListener instead");
    }

    public void setOnFilePickListener(OnFilePickListener listener) {
        this.onFilePickListener = listener;
    }

    public static abstract class OnFilePickListener {

        public abstract void onFilePicked(String currentPath);

        public void onCancel() {

        }

    }

}
