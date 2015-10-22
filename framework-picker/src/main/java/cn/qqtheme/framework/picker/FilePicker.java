package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import cn.qqtheme.framework.adapter.FileAdapter;
import cn.qqtheme.framework.entity.FileItem;
import cn.qqtheme.framework.helper.Common;
import cn.qqtheme.framework.helper.Logger;
import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.view.MarqueeTextView;

public class FilePicker extends ConfirmPopup<LinearLayout> implements AdapterView.OnItemClickListener {
    private Mode mode;
    private FileAdapter adapter;
    private MarqueeTextView textView;
    private OnFilePickListener onFilePickListener;

    public enum Mode {
        Directory, File
    }

    public FilePicker(Activity activity, Mode mode) {
        super(activity);
        this.mode = mode;
        adapter = new FileAdapter(activity);
    }

    @Override
    protected LinearLayout initContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
                    Logger.debug("已放弃选择！");
                    if (onFilePickListener != null) {
                        onFilePickListener.onCancel();
                    }
                } else {
                    String currentPath = adapter.getCurrentPath();
                    Logger.debug("已选择目录：" + currentPath);
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
        refreshCurrentDirPath(Common.getRootPath(activity));
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
                Logger.debug("选择的不是有效的目录: " + clickPath);
            } else {
                dismiss();
                Logger.debug("已选择文件：" + clickPath);
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
