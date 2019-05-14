package com.supermenote.note;

import android.Manifest;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.supermenote.note.permission.BaseAct;

import com.google.android.material.snackbar.Snackbar;
import com.supermenote.note.permission.MyPermissionListener;

/**
 * 主界面
 */
public class MainAct extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //封装权限使用，拒绝的情况已封装在base中，不需要自己再处理
        final LinearLayout layout = findViewById(R.id.ly_main);
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},//读写权限
                layout,
                new MyPermissionListener() {
                    @Override
                    public void onGranted() {
                        //权限通过，使用功能
                        Snackbar.make(layout, "MyPermissionAct--权限通过", Snackbar.LENGTH_SHORT)
                                .show();

                    }
                });
    }

}
