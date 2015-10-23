package cn.qqtheme.androidpicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import cn.qqtheme.framework.helper.Common;
import cn.qqtheme.framework.picker.ColorPicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;

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

    public void onDatePicker(View view) {
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.YEAR_MONTH_DAY);
        picker.setRange(1990, 2015);
        picker.setSelectedDate(1999, 10, 11);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                showToast(result.toLocaleString());
            }
        });
        picker.showAtBottom();
    }

    public void onTimePicker(View view) {
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.HOUR_MINUTE);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                showToast(result.toLocaleString());
            }
        });
        picker.showAtBottom();
    }

    public void on1OptionPicker(View view) {
        OptionPicker picker = new OptionPicker(this);
        final String[] sex = {"男", "女", "保密"};
        picker.setOptions(sex);
        picker.setCurrentOptions(2);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                showToast(sex[result[0]]);
            }
        });
        picker.showAtBottom();
    }

    public void on2OptionPicker(View view) {
        final ArrayList<String> option1 = new ArrayList<String>();
        option1.add("技术方案");
        option1.add("开发工具");
        option1.add("运行环境");
        final ArrayList<ArrayList<String>> option2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> language = new ArrayList<String>();
        language.add("Java/XML");
        language.add("PHP/MySQL");
        language.add("H5+/MUI");
        option2.add(language);
        ArrayList<String> tool = new ArrayList<String>();
        tool.add("IntelliJ IDEA");
        tool.add("PhpStorm");
        tool.add("HBuilder");
        option2.add(tool);
        ArrayList<String> environment = new ArrayList<String>();
        environment.add("Android");
        environment.add("WAMP/LAMP");
        environment.add("Android/IOS");
        option2.add(environment);
        OptionPicker picker = new OptionPicker(this);
        picker.setOptions(option1, option2);
        picker.setCurrentOptions(1);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                showToast(option1.get(result[0]) + "-" + option2.get(result[0]).get(result[1]));
            }
        });
        picker.showAtBottom();
    }

    public void on3OptionPicker(View view) {
        //选项1
        final ArrayList<String> option1 = new ArrayList<String>();
        option1.add("贵州");
        option1.add("北京");
        //选项2
        final ArrayList<ArrayList<String>> option2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options2_1 = new ArrayList<String>();
        options2_1.add("贵阳");
        options2_1.add("毕节");
        ArrayList<String> options2_2 = new ArrayList<String>();
        options2_2.add("北京");
        option2.add(options2_1);
        option2.add(options2_2);
        //选项3
        final ArrayList<ArrayList<ArrayList<String>>> option3 = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> option3_1 = new ArrayList<ArrayList<String>>();
        ArrayList<String> option3_1_1 = new ArrayList<String>();
        option3_1_1.add("花溪");
        option3_1_1.add("南明");
        option3_1_1.add("金阳");
        option3_1.add(option3_1_1);
        ArrayList<String> option3_1_2 = new ArrayList<String>();
        option3_1_2.add("这里测试很长很长的文字");
        option3_1_2.add("七星关");
        option3_1_2.add("纳雍");
        option3_1.add(option3_1_2);
        ArrayList<ArrayList<String>> option3_2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> option3_2_1 = new ArrayList<String>();
        option3_2_1.add("北京");
        option3_2.add(option3_2_1);
        option3.add(option3_1);
        option3.add(option3_2);
        OptionPicker picker = new OptionPicker(this);
        picker.setLabels("省", "市", "县");
        picker.setOptions(option1, option2, option3);
        picker.setCurrentOptions(0, 1, 2);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                String province = option1.get(result[0]);
                String city = option2.get(result[0]).get(result[1]);
                String district = option3.get(result[0]).get(result[1]).get(result[2]);
                showToast(province + "-" + city + "-" + district);
            }
        });
        picker.showAtBottom();
    }

    public void onColorPicker(View view) {
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                showToast(Common.toColorString(pickedColor));
            }
        });
        picker.showAtBottom();
    }

    public void onFilePicker(View view) {
        FilePicker picker = new FilePicker(this, FilePicker.Mode.File);
        picker.setShowHideDir(false);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.showAtBottom();
    }

    public void onDirPicker(View view) {
        FilePicker picker = new FilePicker(this, FilePicker.Mode.Directory);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.showAtBottom();
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
