package com.mnnyang.starmusic.util;

import android.text.TextUtils;

import com.mnnyang.starmusic.bean.LyricLine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 歌词工具
 */
public class LrcUtils {

    /**
     * <p>歌词加载</p>
     * 没有找到返回 null
     */
    public static ArrayList<LyricLine> loadLyric(String musicPath) {
        if (TextUtils.isEmpty(musicPath)) {
            return null;
        }
        String lrcString = null;

        /**文件拼接*/
        String prefix = musicPath.substring(0, musicPath.lastIndexOf("."));
        File lrcFile = new File(prefix + ".lrc");
        File txtFile = new File(prefix + ".txt");

        /**读入内存*/
        if (lrcFile.exists()) {
            lrcString = readLyricFile(lrcFile);
        } else if (txtFile.exists()) {
            lrcString = readLyricFile(txtFile);
        }

        /**为空返回*/
        if (lrcString == null) {
            return null;
        }

        ArrayList<LyricLine> lyrics = null;
        //解析
        try {
            lyrics = analysis(lrcString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //排序
        if (lyrics == null) {
            return null;
        }

        Collections.sort(lyrics, new Comparator<LyricLine>() {
            @Override
            public int compare(LyricLine o1, LyricLine o2) {
                return Integer.valueOf(o1.getStartShowPosition()).compareTo(o2.getStartShowPosition());
            }
        });

        return lyrics;
    }

    /**
     * 解析歌词<br>
     * 服务器返回的歌词有的没有换行符号-_-!!<br>
     */
    public static ArrayList<LyricLine> analysis(String lrcString) {
        if (TextUtils.isEmpty(lrcString)) {
            return null;
        }
        ArrayList<LyricLine> lyrics = new ArrayList<LyricLine>();
        ArrayList<String> fragmentList = new ArrayList<String>();

        //分割碎片
        String[] lrcFragment = lrcString.split("\\[");

        for (String s : lrcFragment) {
            String[] lrcFragment2 = s.split("]");
            for (String s1 : lrcFragment2) {
                s1 = s1.trim();
                if (s1.equals("")) {
                    continue;
                }
                if (s1.contains("ti:") || s1.contains("ar:") || s1.contains("al:") || s1.contains("by:")) {
                    continue;
                }
                if (s1.contains("offset")) {
                    continue;
                }
                /**添加真正需要的内容*/
                fragmentList.add(s1);
            }
        }

        for (String string : fragmentList) {
            /**正则表达式运算找出是时间的段*/
            if (string.matches("[0-9]{2}:[0-9]{2}.[0-9]{2}")) {
//                System.out.println("---->" + string);
                if (lyrics.size() >= 1) {
                    if (lyrics.get(lyrics.size() - 1).getText() == null) {
                        lyrics.get(lyrics.size() - 1).setStartShowPosition(parseTime(string));
                        continue;
                    }
                }
                lyrics.add(new LyricLine(parseTime(string), ""));

            } else {
                /**歌词部分*/
                if (lyrics.size() == 0) {
                    continue;
                }
                lyrics.get(lyrics.size() - 1).setText(string);
            }
        }

        return lyrics;
    }

    /**
     * 读取文件到String
     */
    public static String readLyricFile(File lrcFile) {
        StringBuilder buffer = new StringBuilder();
        try {
            InputStream in = new FileInputStream(lrcFile);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 解析时间
     */
    private static int parseTime(String time) {
        String minuteStr = time.substring(0, 2);
        String secondStr = time.substring(3, 5);
        String millisStr = time.substring(6, 8);

        int minuteMillis = Integer.parseInt(minuteStr) * 60 * 1000;
        int second = Integer.parseInt(secondStr) * 1000;
        int millis = Integer.parseInt(millisStr) * 10;
        return millis + minuteMillis + second;
    }

    public static void print(ArrayList<LyricLine> lyricLines) {
        if (lyricLines == null) {
            return;
        }
        for (LyricLine line : lyricLines) {
            System.out.println(line.getStartShowPosition() + "--" + line.getText());
        }
    }


    /*public static void saveLrc(String content) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "oomusic/lrc/" + content + ".lrc");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
