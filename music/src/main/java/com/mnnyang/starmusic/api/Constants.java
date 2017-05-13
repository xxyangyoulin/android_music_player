package com.mnnyang.starmusic.api;

/**
 * <p>常量</p>
 * Created by mnnyang on 17-4-12.
 */

public interface Constants {
    String SPLASH_NAME = "splash";
    String SPLASH_URL = "splash_id";

    String INTENT_TOP_LIST_INFO = "intent_top_list_info";
    String INTENT_NOTIFINATION = "intent_notifination";
    String INTENT_ALBUM = "intent_album";

    String KEY_MODE_NIGHT_YES = "mode_night_yes";
    String KEY_PLAYING_ID = "playing_id";
    String KEY_PLAY_MDOE = "play_mode";

    int NOTIFICATION_FLOG = 0;
    int NOTIFICATION_ID = 8;

    /**
     * 打开主页
     */
    int CODE_OPEN_MAIN_PAGE = 3;
    /**
     * 播放暂停
     */
    int CODE_PLAY_OR_PAUSE = 1;
    /**
     * 退出
     */
    int CODE_EXIT = 4;
    /**
     * 下一曲
     */
    int CODE_NEXT = 2;
}
