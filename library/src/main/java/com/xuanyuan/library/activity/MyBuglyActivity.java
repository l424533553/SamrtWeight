package com.xuanyuan.library.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;
import com.xuanyuan.library.R;

public class MyBuglyActivity extends AppCompatActivity implements View.OnClickListener {

//    /**
//     * 如果想更新so，可以将System.loadLibrary替换成Beta.loadLibrary
//     */
//    static {
//        Beta.loadLibrary("mylib");
//    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bugly);

        TextView tvCurrentVersion = findViewById(R.id.tvCurrentVersion);
        Button btnShowToast = findViewById(R.id.btnShowToast);
        btnShowToast.setOnClickListener(this);
        Button btnKillSelf = findViewById(R.id.btnKillSelf);
        btnKillSelf.setOnClickListener(this);
        Button btnLoadPatch = findViewById(R.id.btnLoadPatch);
        btnLoadPatch.setOnClickListener(this);
        Button btnLoadLibrary = findViewById(R.id.btnLoadLibrary);
        btnLoadLibrary.setOnClickListener(this);
        Button btnDownloadPatch = findViewById(R.id.btnDownloadPatch);
        btnDownloadPatch.setOnClickListener(this);
        Button btnUserPatch = findViewById(R.id.btnPatchDownloaded);
        btnUserPatch.setOnClickListener(this);
        Button btnCheckUpgrade = findViewById(R.id.btnCheckUpgrade);
        btnCheckUpgrade.setOnClickListener(this);

        tvCurrentVersion.setText("当前版本：" + getCurrentVersion(this));

//        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MyBuglyActivity.this,"新增的功能",Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    /**
     * 根据应用patch包前后来测试是否应用patch包成功.
     *
     * 应用patch包前，提示"This is a bug class"
     * 应用patch包之后，提示"The bug has fixed"
     */
    public void testToast() {
        Toast.makeText(this, "是否成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();// 测试热更新功能
// 杀死进程
// 本地加载补丁测试
// 本地加载so库测试
        if (i == R.id.btnShowToast) {
            testToast();
        } else if (i == R.id.btnKillSelf) {
            Process.killProcess(Process.myPid());
        } else if (i == R.id.btnLoadPatch) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Beta.applyTinkerPatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
        } else if (i == R.id.btnLoadLibrary) {//                TestJNI testJNI = new TestJNI();
//                testJNI.createANativeCrash();
        } else if (i == R.id.btnDownloadPatch) {
            Beta.downloadPatch();
        } else if (i == R.id.btnPatchDownloaded) {
            Beta.applyDownloadedPatch();
        } else if (i == R.id.btnCheckUpgrade) {
            Beta.checkUpgrade();
        }
    }

    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    public String getCurrentVersion(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            return versionName + "." + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
}
