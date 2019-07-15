package cn.qqtheme.androidpicker;

import android.app.Application;
import android.view.Gravity;

import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import cn.qqtheme.androidpicker.impl.LoggerImpl;
import cn.qqtheme.framework.logger.CqrLog;

/**
 * Global application state
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2016/7/20 20:28
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //CqrLog.useDefaultLogger(BuildConfig.DEBUG, LOG_TAG);
        CqrLog.setLogger(new LoggerImpl());
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        if (!ProcessUtils.isMainProcess()) {
            CqrLog.w("当前非主进程，不要初始化: pid=" + android.os.Process.myPid());
            return;
        }
        initInMainProcess();
    }

    private void initInMainProcess() {

    }

}
