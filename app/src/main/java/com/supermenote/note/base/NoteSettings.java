package com.supermenote.note.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import com.supermenote.note.utils.BaseUtils;
import com.supermenote.note.utils.Loger;

import java.util.Queue;

/**
 * 全局用户信息类
 */
public class NoteSettings {
    public static final String kThinkerNotePackage = "com.thinkernote.ThinkerNote";
    public static final String kActivityPackage = "com.thinkernote.ThinkerNote.Activity";

    private static final String TAG = "TNSettings";
    private static final String PREF_NAME = "thinkernote";

    private static NoteSettings singleton = null;

    public String shortRevision = "5.7";
    public String revision = "5.7.4";

    // app status
    public boolean hasDbError;

    public String skinName = "default";

    //devkey 注册渠道key，用于记录注册量来自哪个推广程序
    public String devKey;

    // user login status
    public String loginname;
    public String username;
    public String password;
    public String token;
    public long expertTime;
    public long userId;
    public String phone;
    public String email;
    public int emailVerify;
    public long defaultCatId;
    public String sort = NoteCons.UPDATETIME;
    public int totalCount;
    public boolean highVersion;
    public boolean syncOldDb;//是否同步老数据库
    public String version;
    public int phoneDialogShowCount = 0;

    public int isAutoLogin; // 0 NO; 1 YES
    public long projectLocalId;
    public boolean needShowLock;
    public boolean remindLockGroup = true;
    public boolean remindLockNote = true;

    //Auth
    public String accessToken;    //sina、QQ、google没有refreshToken直接使用accessToken
    public String sinaUid;
    public String uniqueId;
    public String refreshToken; //百度、360的使用refreshToken拿accessToken
    public int userType; // 0    轻笔记; 1   百度; 2  新浪微博;  3 qq;  4  谷歌;  5 360; 6   人人网; 8 天翼189

    public int tokenStatus = 0;    //0  无效；1 有效;(轻笔记token是否有效)
    public String tnAccessToken;    //轻笔记开放API的accessToken

    public long originalSyncTime = (long) 0;
    public long originalSyncShareNotesTime = (long) 0;
    public long originalSyncProjectTime = (long) 0;

    public int userStatus = 0; //	0 、老用户  	1、新用户
    public int appStartCount = 0; //用于统计程序的启动次数
    public int pictureCompressionMode = 1; //暂为：0,不压缩  1,压缩(默认). 为-1时,是第一次添加图片附件用于弹窗询问用户是否使用压缩模式
    public int bootViewSeen = 0x00; //做二进制数使用，1表示该界面已显示过引导，0表示否
    //第0位表示mian界面，第1位表示标签界面，第2位表示文件夹列表界面，第3位表示笔记列表界面，第4位表示混排界面,
    //第5位表示noteview界面;

    // settings for user
    public int sync; // 0,auto 1,wifi 2,manual
    public int noteListOrder; // 0 lastUpdate, 1 createTime, 2 title
    public int catListOrder;    //0 Default, 1 createTime, 2 name
    public String voice; // xiaoyan, xiaoyu
    public int speed; // 50
    public int volume; // 50
    public String searchWord;
    public boolean firstLaunch;
    public int showDialogType = 0x00;//第0位为bindingDialog，1表示不再提醒，0表示提醒, 第1位为首页的同步dialog

    public Queue<Integer> lockPattern;

    // below not save
    public Context appContext;
    public Activity topAct;

    public boolean isLogout = false;

    public boolean serviceRuning = false;

    private SharedPreferences sp = null;

    private NoteSettings() {
    }

    public static NoteSettings getInstance() {
        if (singleton == null) {
            synchronized (NoteSettings.class) {
                if (singleton == null) {
                    singleton = new NoteSettings();
                }
            }
        }
        return singleton;
    }

    public boolean isInProject() {
        return projectLocalId > 0;
    }

    public boolean isCanLogin() {
        if (expertTime * 1000 > System.currentTimeMillis()) {
            return false;
        } else if (username.length() == 0
                || password.length() != 32 || token.length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isLogin() {
        if (expertTime * 1000 - System.currentTimeMillis() > 0 && loginname.length() != 0
                && token.length() != 0 && !isLogout) {
            return true;
        }
        return false;
    }

    public boolean isTNTokenActivie() {
        Log.i(TAG, "userId" + userId + "tokenStatus=" + tokenStatus + " accessToken=" + tnAccessToken + " uniqueId=" + uniqueId);
        if (tokenStatus == 1 && tnAccessToken != null && tnAccessToken.length() > 0) {
            if (userType > 0) {
                if (uniqueId != null && uniqueId.length() > 0)
                    return true;
            } else {
                return true;
            }
        }
        return false;
    }

    public void readPref() {
        if (sp == null) {
            sp = BaseUtils.getAppContext().getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
        }
        hasDbError = sp.getBoolean("hasDbError", false);
        originalSyncTime = sp.getLong("originalSyncTime", 0);
        skinName = sp.getString("skinName", "default");
        devKey = sp.getString("devKey", "");

        pictureCompressionMode = sp.getInt("pictureCompressionMode", 1);
        bootViewSeen = sp.getInt("bootViewSeen0711", 0x00);

        loginname = sp.getString("loginname", "");
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        token = sp.getString("token", "");
        expertTime = sp.getLong("expertTime", 0);
        userId = sp.getLong("userId", 0);
        phone = sp.getString("phone", "");
        email = sp.getString("email", "");
        emailVerify = sp.getInt("emailVerify", -1);
        defaultCatId = sp.getLong("defaultCatId", 0);
        sort = sp.getString("sort", NoteCons.UPDATETIME);
        totalCount = sp.getInt("totalCount", 0);
        highVersion = sp.getBoolean("highVersion", false);
        syncOldDb = sp.getBoolean("syncOldDb", false);
        version = sp.getString("version", "");
        phoneDialogShowCount = sp.getInt("phoneDialogShowCount", 0);

        userStatus = sp.getInt("userStatus", 0);
        isAutoLogin = sp.getInt("isAutoLogin", 0);
        needShowLock = sp.getBoolean("needShowLock", true);

        refreshToken = sp.getString("refreshToken", null);
        accessToken = sp.getString("accessToken", null);
        sinaUid = sp.getString("sinaUid", "");
        userType = sp.getInt("userType", 0);
        uniqueId = sp.getString("uniqueId", "");

        sync = sp.getInt(username + "sync", 1);
        noteListOrder = sp.getInt(username + "noteListOrder", 0);
        catListOrder = sp.getInt(username + "catListOrder", 0);
        voice = sp.getString(username + "voice", "xiaoyan");
        speed = sp.getInt(username + "speed", 50);
        volume = sp.getInt(username + "volume", 50);
        searchWord = sp.getString(username + "searchWord", "");
        //TODO
//        lockPattern = TNUtils.getPath(
//                sp.getString(username + "lockPattern", "[]"));
        firstLaunch = sp.getBoolean(username + "firstLaunch", false);
        appStartCount = sp.getInt(username + "appStartCount1218", 0);
        remindLockGroup = sp.getBoolean(username + "remindLockGroup", false);
        remindLockNote = sp.getBoolean(username + "remindLockNote", true);
        showDialogType = sp.getInt(username + "showDialogType", 0x00);

        Loger.i(TAG, "readPref OK " + firstLaunch);
    }

    public void savePref(boolean saveUserInfo) {
        Editor editor = sp.edit();
        editor.putLong("originalSyncTime", originalSyncTime);
        editor.putString("skinName", skinName);
        editor.putString("devKey", devKey);
        editor.putBoolean("hasDbError", hasDbError);

        editor.putString("loginname", loginname);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.putLong("expertTime", expertTime);
        editor.putLong("userId", userId);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putInt("emailVerify", emailVerify);
        editor.putLong("defaultCatId", defaultCatId);
        editor.putString("sort", sort);
        editor.putInt("totalCount", totalCount);
        editor.putBoolean("highVersion", highVersion);
        editor.putBoolean("syncOldDb", syncOldDb);
        editor.putString("version", version);
        editor.putInt("phoneDialogShowCount", phoneDialogShowCount);

        editor.putInt("isAutoLogin", isAutoLogin);
        editor.putBoolean("needShowLock", needShowLock);

        editor.putString("refreshToken", refreshToken);
        editor.putString("accessToken", accessToken);
        editor.putString("sinaUid", sinaUid);
        editor.putInt("userType", userType);
        editor.putString("uniqueId", uniqueId);

        editor.putInt("pictureCompressionMode", pictureCompressionMode);
        editor.putInt("bootViewSeen0711", bootViewSeen);
        editor.putInt("userStatus", userStatus);

        if (saveUserInfo && username.length() > 0) {
            editor.putInt(username + "sync", sync);
            editor.putInt(username + "noteListOrder", noteListOrder);
            editor.putInt(username + "catListOrder", catListOrder);
            editor.putString(username + "voice", voice);
            editor.putInt(username + "speed", speed);
            editor.putInt(username + "volume", volume);
            editor.putString(username + "searchWord", searchWord);
            editor.putString(username + "lockPattern", lockPattern.toString());
            editor.putBoolean(username + "firstLaunch", firstLaunch);
            editor.putInt(username + "appStartCount1218", appStartCount);
            editor.putBoolean(username + "remindLockGroup", remindLockGroup);
            editor.putBoolean(username + "remindLockNote", remindLockNote);
            editor.putInt(username + "showDialogType", showDialogType);
        }

        editor.commit();
        Loger.i(TAG, "savePref OK " + firstLaunch);
    }

}
