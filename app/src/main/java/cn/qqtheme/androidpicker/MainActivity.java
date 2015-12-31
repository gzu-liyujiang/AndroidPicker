package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.qqtheme.framework.picker.ChineseZodiacPicker;
import cn.qqtheme.framework.picker.ColorPicker;
import cn.qqtheme.framework.picker.ConstellationPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SexPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.StorageUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onYearMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this);
        picker.setRange(2000, 2016);
        picker.setSelectedItem(2015, 10, 10);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                showToast(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    public void onYearMonthPicker(View view) {
        DatePicker picker = new DatePicker(this, DatePicker.Mode.YEAR_MONTH);
        picker.setRange(1990, 2015);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                showToast(year + "-" + month);
            }
        });
        picker.show();
    }

    public void onMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this, DatePicker.Mode.MONTH_DAY);
        picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
            @Override
            public void onDatePicked(String month, String day) {
                showToast(month + "-" + day);
            }
        });
        picker.show();
    }

    public void onTimePicker(View view) {
        //默认选中当前时间
        TimePicker picker = new TimePicker(this);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                showToast(hour + ":" + minute);
            }
        });
        picker.show();
    }

    public void onOptionPicker(View view) {
        OptionPicker picker = new OptionPicker(this, new String[]{
                "第一项", "第二项", "这是一个很长很长很长很长很长很长很长很长很长的很长很长的很长很长的项"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
    }

    public void onConstellationPicker(View view) {
        ConstellationPicker picker = new ConstellationPicker(this);
        picker.setTopLineColor(0xFFEE0000);
        picker.setTextColor(0xFFFF0000, 0xFF999999);
        picker.setLineColor(0xFFEE0000);
        picker.setSelectedItem("射手");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
    }

    public void onChineseZodiacPicker(View view) {
        ChineseZodiacPicker picker = new ChineseZodiacPicker(this);
        picker.setLineVisible(false);
        picker.setSelectedItem("羊");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
    }

    public void onNumberPicker(View view) {
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(145, 200);//数字范围
        picker.setSelectedItem(172);
        picker.setLabel("厘米");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
    }

    public void onSexPicker(View view) {
        SexPicker picker = new SexPicker(this);
        picker.onlyMaleAndFemale();
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
    }

    public void onAddressPicker(View view) {
        new AddressInitTask(this).execute("贵州", "毕节", "纳雍");
    }

    public void onColorPicker(View view) {
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                showToast(ConvertUtils.toColorString(pickedColor));
            }
        });
        picker.show();
    }

    public void onFilePicker(View view) {
        FilePicker picker = new FilePicker(this);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setMode(FilePicker.Mode.File);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
    }

    public void onDirPicker(View view) {
        FilePicker picker = new FilePicker(this);
        picker.setMode(FilePicker.Mode.Directory);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
    }

    public void onGithub(View view) {
        Intent intent = new Intent(this, GithubActivity.class);
        startActivity(intent);
    }

    public void onContact(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:liyujiang_tk@yeah.net"));
        intent.putExtra(Intent.EXTRA_CC, new String[]
                {"1032694760@qq.com"});
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_TEXT, "欢迎提供意您的见或建议");
        startActivity(Intent.createChooser(intent, "选择邮件客户端"));
    }

}
