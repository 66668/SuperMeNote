package com.supermenote.note.permission;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

/**
 * 自定义封装方式1：提取到基类中处理，listener处理返回
 */

public class BaseAct extends AppCompatActivity {
    MyPermissionListener listener;//监听返回，处理申请权限的返回
    View permissionView;//权限结果提示的布局
    String[] permissions;
    private static final int REQUEST_CODE = 10001;
    private static final int REQUEST_OPEN = 10002;


    //==================================================================================================
    //=======================================权限封装---开始============================================
    //==================================================================================================

    /**
     * 申请权限第一个调用方法
     */
    public void requestPermission(final String[] permissions, View v, MyPermissionListener l) {
        this.listener = l;
        this.permissionView = v;
        this.permissions = permissions;
        //1.判断sdk 和targetsdk是否满足
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {

            //2.判断是否需要申请权限()
            boolean isCheck = false;
            for (int i = 0; i < permissions.length; i++) {
                isCheck = (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) || isCheck;
            }
            if (isCheck) {
                //解释为何申请权限的代码
                Snackbar.make(permissionView, "该功能需要使用权限（BaseAct）",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("允许", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //3.申请权限
                                ActivityCompat.requestPermissions(BaseAct.this, permissions, REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //3.申请权限
                ActivityCompat.requestPermissions(BaseAct.this, permissions, REQUEST_CODE);
            }

        } else {
            //说明：这里需要打印结果，粗心的程序员会对这两个地方不处理，导致没法申请权限
            Log.d("SJY", "当前sdk=" + Build.VERSION.SDK_INT + "当前targetsdk=" + this.getApplicationInfo().targetSdkVersion);
            //不要申请，直接使用
            listener.onGranted();
        }

    }

    /**
     * 04
     *
     * @param requestCode
     * @param permissions  多个权限的数组
     * @param grantResults 多个权限结果数组，通过的是0，不通过的是-1
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                boolean isGranted = true;
                final String[] deniedArray = new String[grantResults.length];
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] < 0) {
                        isGranted = false;
                        deniedArray[i] = permissions[i];
                    }
                }

                if (isGranted) {
                    //可以执行功能了
                    listener.onGranted();

                } else {
                    //权限申请拒绝的处理
                    Snackbar.make(permissionView, deniedArray.length + "个权限被拒绝！", Snackbar.LENGTH_SHORT)
                            .setAction("设置", new View.OnClickListener() {//TODO
                                @Override
                                public void onClick(View view) {
                                    //不询问，直接打开设置界面
                                    boolean isReqest = false;
                                    for (int i = 0; i < deniedArray.length; i++) {
                                        isReqest = (!ActivityCompat.shouldShowRequestPermissionRationale(BaseAct.this, deniedArray[0])) || isReqest;
                                    }
                                    if (isReqest) {
                                        //3.手动打开权限
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", BaseAct.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_OPEN);
                                    }
                                }
                            })
                            .show();
                }

                break;
        }

    }

    //==================================================================================================
    //=======================================权限封装---结束============================================
    //==================================================================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case REQUEST_OPEN://永久拒绝后，手动打开权限的返回处理
                //3.再次申请
                ActivityCompat.requestPermissions(BaseAct.this, permissions, REQUEST_CODE);
                break;
        }
    }


}
