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
    private PickMode pickMode;
    private FileAdapter adapter;
    private String currentPath = "/";
    private MarqueeTextView textView;
    private ListView listView;
    private OnFilePickListener onFilePickListener;

    public enum PickMode {
        Directory, File
    }

    public FilePicker(Activity activity, PickMode pickMode) {
        super(activity);
        this.pickMode = pickMode;
        setHeight(screenHeight / 2);
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
        rootLayout.addView(textView);
        View lineView = new View(activity);
        lineView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        lineView.setBackgroundColor(0xDDDDDDDD);
        rootLayout.addView(lineView);
        listView = new ListView(activity);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(0xDDDDDDDD));
        listView.setDividerHeight(1);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setOnItemClickListener(this);
        rootLayout.addView(listView);
        return rootLayout;
    }

    public void setAdapter(FileAdapter adapter) {
        this.adapter = adapter;
        //noinspection unchecked
        listView.setAdapter(adapter);
    }

    @Override
    protected void onShowPrepare() {
        setCancelVisible(false);
        final boolean isPickFile = pickMode.equals(PickMode.File);
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
                    Logger.debug("已选择目录：" + currentPath);
                    if (onFilePickListener != null) {
                        onFilePickListener.onPicked(currentPath);
                    }
                }
            }
        });
        super.onShowPrepare();
        if (adapter == null) {
            FileAdapter adapter = new FileAdapter(activity);
            currentPath = Common.getRootPath(activity);
            refreshCurrentDirPath();
            adapter.loadData(currentPath);
            setAdapter(adapter);
        }
    }

    /**
     * 响应选择器的列表项点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        FileItem fileItem = adapter.getItem(position);
        if (fileItem.isDirectory()) {
            currentPath = fileItem.getPath();
            refreshCurrentDirPath();
            adapter.loadData(currentPath);
        } else {
            String clickPath = fileItem.getPath();
            if (pickMode.equals(PickMode.Directory)) {
                Logger.debug("选择的不是有效的目录: " + clickPath);
            } else {
                dismiss();
                Logger.debug("已选择文件：" + clickPath);
                if (onFilePickListener != null) {
                    onFilePickListener.onPicked(clickPath);
                }
            }
        }
    }

    private void refreshCurrentDirPath() {
        if (currentPath.equals("/")) {
            textView.setText("根目录");
        } else {
            textView.setText(currentPath);
        }
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

        public abstract void onPicked(String currentPath);

        public void onCancel() {

        }

    }

}
