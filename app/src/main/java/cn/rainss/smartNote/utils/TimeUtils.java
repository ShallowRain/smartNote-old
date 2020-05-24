package cn.rainss.smartNote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于转换日期的工具类
 */
public class TimeUtils {
    /**
     * 日期工具
     */
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 时间工具类，将时间转换为字符串
     * @param time
     * @return
     */
    public static String timeToString(Date time){
        return df.format(time);
    }

    /**
     * 时间工具类，将时间字符串转化为日期类型
     * 暂时不做相关日期格式的正则判断处理
     * @param str
     * @return
     */
    public static Date stringToTime(String str){
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //转换失败时直接返回空
        return null;
    }
}
