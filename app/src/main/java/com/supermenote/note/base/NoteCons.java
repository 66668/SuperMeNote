package com.supermenote.note.base;

/**
 * 全局常量管理
 */

public class NoteCons {

    /**
     * ==============================================================================
     * ======================================缓存文件管理========================================
     * ==============================================================================
     */
    public static final String APP_HTTP_ACACHE_FILE = "qingbiji_cache_file";//网络路径缓存
    public static final String DEFAULT_TITLE = "未命名";
    public static final String ATT_MISSING_MSG = "文件尚未上传";

    public static final String FOLDER_WORK_UNFINISHED = "未完成工作";
    public static final String FOLDER_WORK_FINISHED = "已完成工作";
    public static final String FOLDER_WORK_NOTE = "工作笔记";
    public static final String FOLDER_LIFE_DIARY = "日记";
    public static final String FOLDER_LIFE_PHOTO = "照片";
    public static final String FOLDER_LIFE_KNOWLEDGE = "常识";
    public static final String FOLDER_FUN_MOVIE = "电影";
    public static final String FOLDER_FUN_GAME = "游戏";
    public static final String FOLDER_FUN_TRAVEL = "旅游";
    public static final String FOLDER_MEMO = "备忘";
    public static final String FOLDER_DEFAULT = "便签";

    public static final String GROUP_WORK = "工作";
    public static final String GROUP_LIFE = "生活";
    public static final String GROUP_FUN = "娱乐";
    public static final String GROUP_TRASH = "回收站";

    public static final String TAG_IMPORTANT = "重要";
    public static final String TAG_TODO = "待办";
    public static final String TAG_GOODSOFT = "好用的软件";

    public static final String FIRSTNOTE_TITLE = "欢迎加入轻笔记的大家庭";
    public static final String FIRSTNOTE_TAG = "重要,好用的软件";

    public static final int ATT_MAX_LENTH = 20*1024*1024;

    public static final String CLIENT_ID = "A7FB7E98A536718E009A6FA36F278AAE";
    public static final String CLIENT_SECRET = "BA71552CC627AB5BEDBD0E11950DB955";

    public static final int PAGE_SIZE = 20;
    public static final int PAGE_SIZE_BIG = 100;
    public static final int MAX_PAGE_SIZE = Integer.MAX_VALUE;
    public static final String CREATETIME = "create_at";
    public static final String UPDATETIME = "update_at";

    public static final String QQ_APP_ID = "101399197";
    public static final String QQ_SCOPE = "all";

    public static final String WX_APP_ID = "wx2c2721939e9d54e3";

    public static final String ERROR_CODE = "未知网络错误";

    // 应用的key 请到官方申请正式的appkey替换APP_KEY
    public static final String SINA_APP_KEY = "2897968978";
    // 替换为开发者REDIRECT_URL
    public static final String SINA_REDIRECT_URL = "http://www.qingbiji.cn/binding/weibo_token";
    // 新支持scope：支持传入多个scope权限，用逗号分隔
    public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

//	public static final String OAUTH_LOGOIN_URL = "http://www.qingbiji.cn/open/login_v2";

}
