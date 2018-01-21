package cn.qssq666.plugin.demo;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PackageInfo packageInfo = MainActivity.this.getApplicationContext().getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
                    File sourceDir = new File(packageInfo.applicationInfo.sourceDir);
                    /*
                    -a：此参数的效果和同时指定"-dpR"参数相同；
-d：当复制符号连接时，把目标文件或目录也建立为符号连接，并指向与源文件或目录连接的原始文件或目录；
-f：强行复制文件或目录，不论目标文件或目录是否已存在；
-i：覆盖既有文件之前先询问用户；
-l：对源文件建立硬连接，而非复制文件；
-p：保留源文件或目录的属性；
-R/r：递归处理，将指定目录下的所有文件与子目录一并处理；
-s：对源文件建立符号连接，而非复制文件；
-u：使用这项参数后只会在源文件的更改时间较目标文件更新时或是名称相互对应的目标文件并不存在时，才复制文件；
-S：在备份文件时，用指定的后缀“SUFFIX”代替文件的默认后缀；
-b：覆盖已存在的文件目标前将目标文件备份；
-v：详细显示命令执行的操作。
                     */
                    File pluginDir = new File("/data/data/cn.qssq666.robot/app_plugin", BuildConfig.APPLICATION_ID + ".apk");
              /*      try {
                        Process exec = Runtime.getRuntime().exec("su");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    boolean exists = pluginDir.getParentFile().exists();
                    if (!exists) {
                        try {
                            Runtime.getRuntime().exec(new String[]{"su", "-c", "mkdir", "-p", pluginDir.getParentFile().getAbsolutePath()});
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, " 插件目录尚未初始化!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }*/


                    try {

                        Process proc = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(proc.getOutputStream());
                        os.writeBytes(String.format("cp -p %s %s\n", sourceDir.getAbsolutePath(), pluginDir.getAbsolutePath()));
                        os.flush();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "utf-8"));

                        os.close();
          /*              String cp = sourceDir.getAbsolutePath() + " " + pluginDir.getAbsolutePath() + "";
                        Process exec = Runtime.getRuntime().exec(new String[]{"su", "-c","cp", cp});
                        InputStream errorStream = exec.getErrorStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(errorStream, "utf-8");
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                        String s = bufferedReader1.readLine();
                        Log.w("MainActivity", cp);
                        Toast.makeText(MainActivity.this, "安装结果!" + s.toString(), Toast.LENGTH_SHORT).show();
                        inputStreamReader.close();
                        bufferedReader1.close();*/
                        String line = bufferedReader.readLine();
                        if (TextUtils.isEmpty(line)) {
                            Toast.makeText(MainActivity.this, "安装成功,插件名为:" + pluginDir.getName(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "安装结果!" + bufferedReader.readLine(), Toast.LENGTH_SHORT).show();

                        }
                        bufferedReader.close();


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "请手动把apk文件复制到" + pluginDir + "中去,并使用刷新命令进行刷新!", Toast.LENGTH_SHORT).show();
                    }


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "安装失败,找不到自身apk文件路径!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean execute(String str) {
//        Log.w("EXECUTE", Log.getStackTraceString(new Throwable()) + str);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"su", "-c", str});
            processBuilder.redirectErrorStream(true);
            Process start = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String readLine = bufferedReader.readLine();
            if (readLine == null || readLine.compareTo("Permission denied") != 0) {
                start.waitFor();
                return true;
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
