package com.supermenote.note.base;

import android.app.Application;

import com.supermenote.note.utils.Loger;


/**
 * sjy 0507
 */
public class NoteApplication extends Application {
    private static final String TAG = "TNApplication";
    private static NoteApplication application;

    public static NoteApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        initialize();

        // log初始化
        Loger.init(true, "SJY");//release版 false


        //leakcanary初始化（打包时清除）
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }

    // private methods
    //-------------------------------------------------------------------------------

    /**
     * 初始化顺序
     */
    private void initialize() {
        //（1）
        NoteSettings settings = NoteSettings.getInstance();
        settings.appContext = this;

    }



}
