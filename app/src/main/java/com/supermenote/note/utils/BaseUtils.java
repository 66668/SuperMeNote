package com.supermenote.note.utils;

import android.content.Context;

import com.supermenote.note.base.NoteSettings;

public class BaseUtils {


    public static Context getAppContext() {
        return NoteSettings.getInstance().appContext;
    }


}
