package com.miss.server.alert.domain.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtil {

    private static final String[] PARSEPATTERNS = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm", "yyyy.MM.dd HH:mm", "yyyy-MM-dd HH",
            "yyyy/MM/dd HH", "yyyy.MM.dd HH", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"};
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将字符串转换成日期类型,自动匹配格式
     *
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        try {
            return DateUtils.parseDate(date, PARSEPATTERNS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串格式转日期
     *
     * @param date
     * @param parsePatterns
     * @return
     */
    public static Date stringToDate(String date, String... parsePatterns) {
        try {
            return DateUtils.parseDate(date, parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转字符串 根据给定日期格式，格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 日期转字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, PATTERN);
    }

    /**
     * 增加n天后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);// 增加n天
        return calendar.getTime();
    }

    /**
     * 增加n个月后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);// 增加n个月
        return calendar.getTime();
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    /**
     * 在日期上加分钟数，得到新的日期
     *
     * @return
     */
    public final static Date addMinToDate(Date date, int min) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, min);
        return c.getTime();
    }

    /**
     * 在日期上加days天，得到新的日期
     *
     * @return
     */
    public final static Date addDaysToDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * 在日期上加months月，得到新的日期
     *
     * @return
     */
    public final static Date addMonthsToDate(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 比较两个日期的大小,type =1加时分秒判断。=0不加时分秒
     *
     * @param: @param
     * date1
     * @param: @param
     * date2
     * @param: @param
     * type =1加时分秒判断。=0不加时分秒
     * @param: @return
     * 1大于。-1小于。0等于
     * @return: int
     */
    public final static int compareDate(Date date1, Date date2, int type) {
        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Date newDate1 = null;
        Date newDate2 = null;
        try {
            if (type == 0) {
                newDate1 = sd1.parse(sd1.format(date1));
                newDate2 = sd1.parse(sd1.format(date2));
            } else if (type == 1) {
                newDate1 = sd2.parse(sd2.format(date1));
                newDate2 = sd2.parse(sd2.format(date2));
            } else {
                newDate1 = date1;
                newDate2 = date2;
            }
            c1.setTime(newDate1);
            c2.setTime(newDate2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c1.compareTo(c2);
    }

    /**
     * map转json
     *
     * @param: @param
     * map
     * @return: String
     */
    public final static String simpleMapToJsonStr(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "null";
        }
        String jsonStr = "{";
        Set<?> keySet = map.keySet();
        for (Object key : keySet) {
            jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
        }
        jsonStr = jsonStr.substring(1, jsonStr.length() - 2);
        jsonStr += "}";
        return jsonStr;
    }

    /**
     * 计算两日期之间的天数
     *
     * @return
     */
    public final static int getDaysBetweenDate(String date1, String date2) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = sd.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sd.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        long diff = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return ((Long) diff).intValue();
    }

    /**
     * 计算两日期之间的天数
     *
     * @return
     */
    public final static Integer getDaysBetweenDate(Date date1, Date date2) {
        Date d1 = date1;
        Date d2 = date2;
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        long diff = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return ((Long) diff).intValue();
    }

    /**
     * @param dateString  日期字符串 如2011-01-03
     * @param dateFormate 日期格式 如yyyy-MM-dd
     * @return 根据传入的日期字符串和日期格式返回指定格式的日期
     */
    public final static Date parseDate(String dateString, String dateFormate) {
        SimpleDateFormat sd = new SimpleDateFormat(dateFormate);
        Date date = null;
        try {
            date = sd.parse(dateString);
        } catch (Exception ex) {

        }
        return date;
    }

    /**
     * 计算两日期之间相隔月份和天数
     *
     * @return
     */
    public static Map<Integer, Integer> getMonthAndDaysBetweenDate(String date1, String date2) {
        Map<Integer, Integer> map = new HashMap();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = sd.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sd.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int months = 0;// 相差月份
        int days = 0;
        int y1 = d1.getYear();
        int y2 = d2.getYear();
        int dm1 = d2.getMonth();// 起始日期月份
        int dm2 = d2.getMonth();// 结束日期月份
        int dd1 = d1.getDate(); // 起始日期天
        int dd2 = d2.getDate(); // 结束日期天
        if (d1.getTime() < d2.getTime()) {
            months = d2.getMonth() - d1.getMonth() + (y2 - y1) * 12;
            if (dd2 < dd1) {
                months = months - 1;
            }
            days = getDaysBetweenDate(
                    getFormatDateTime(addMonthsToDate(DateUtil.parseDate(date1, "yyyy-MM-dd"), months), "yyyy-MM-dd"),
                    date2);
            map.put(1, months);
            map.put(2, days);
        }
        return map;
    }

    /**
     * @param dateFormat
     * @return
     * @function 得到自定义 日期格式
     */
    public final static String getFormatDateTime(Date date, String dateFormat) {

        SimpleDateFormat sf = null;
        try {
            sf = new SimpleDateFormat(dateFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return sf.format(date);
    }

    /**
     * @param dateFormat
     * @return
     * @function 得到自定义 日期格式
     */
    public final static String getFormatDateTime(String date, String dateFormat) {

        SimpleDateFormat sf = null;
        try {
            sf = new SimpleDateFormat(dateFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return sf.format(date);
    }

    /**
     * 返回字符串形式----当前时间的年月日时分秒
     */
    public static String getNewDate() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * 时间按分钟加减
     *
     * @param: @param
     * date 时间
     * @param: @param
     * minute 分钟数 加传正数 -传负数
     * @return: Date
     */
    public static Date dateAddMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    public static Date dateAddSecond(Date date, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 比较两个时间相差多少分钟
     *
     * @param: @param
     * date1 较大时间
     * @param: @param
     * date2 较小时间
     * @return: int
     */
    public static int compareDateMinute(Date date1, Date date2) {
        Calendar dateOne = Calendar.getInstance();
        dateOne.setTime(date1); // 设置date1
        Calendar dateTwo = Calendar.getInstance();
        dateTwo.setTime(date2); // 设置date2
        long timeOne = dateOne.getTimeInMillis();
        long timeTwo = dateTwo.getTimeInMillis();
        long minute = (timeOne - timeTwo) / (1000 * 60);// 转化minute
        return Long.valueOf(minute).intValue();
    }

    /**
     * 比较两个时间相差多少个space分钟
     *
     * @param: @param
     * date1 较大时间
     * @param: @param
     * date2 较小时间
     * @param: @param
     * space 时间间隔
     * @return: int
     */
    public static int compareDateMinuteSpace(Date date1, Date date2, int space) {
        Calendar dateOne = Calendar.getInstance();
        dateOne.setTime(date1); // 设置date1
        Calendar dateTwo = Calendar.getInstance();
        dateTwo.setTime(date2); // 设置date2
        long timeOne = dateOne.getTimeInMillis();
        long timeTwo = dateTwo.getTimeInMillis();
        long minute = (timeOne - timeTwo) / (1000 * 60 * space);// 转化minute
        return Long.valueOf(minute).intValue();
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(PARSEPATTERNS[0]);
        String str = sdf.format(new Date());
        Date date = sdf.parse(str);
        System.out.println(date.getTime());
    }


    public static Date getPeriodMin(Date date, int index) {
        int num = Integer.valueOf(getFormatDateTime(date, "mm")) / index;
        if (num == 0) {
            return stringToDate(getFormatDateTime(date, "yyyy-MM-dd HH"));
        } else {
            return stringToDate(getFormatDateTime(date, "yyyy-MM-dd HH") + ":" + index * num, "yyyy-MM-dd HH:mm");
        }
    }

    /**
     * 比较两个时间的大小
     *
     * @param: @param
     * dt1
     * @param: @param
     * dt2
     * @return: int
     */
    public static int compare_date(Date dt1, Date dt2) {
        try {
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * Wed Jul 06 2016 00:00:00 GMT+0800 (中国标准时间) 带时区的时间格式转换
     * <p>
     * 日期格式转换
     * </p>
     *
     * @param: @param
     * date
     * @param: @param
     * index
     * @return: Date
     */
    public static String conversion(String date, String format) {
        Date d = null;
        try {
            date = date.split("GMT")[0];
            d = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US).parse(date + "GMT+08:00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String dd = sf2.format(d);
        return dd;
    }

    /**
     * Sun Jan 18 09:22:12 CST 1970
     * <p>
     * 日期格式转换
     * </p>
     *
     * @param: @param
     * str
     * @param: @return
     * @return: String
     */
    public static String formatByUS(String str) {
        Date date;
        try {
            date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(str);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dd = format.format(date);
            return dd;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将时间戳转换为时间
     *
     * @param: @param
     * s
     * @return: String
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getLastAmonthDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
    }

    /**
     * @param: @param
     * date '2016'
     */
    public static Integer getBetweenYear(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date now = new Date();
        String today = sdf.format(now);
        Integer int_today = Integer.valueOf(today);
        Integer int_date = Integer.valueOf(date);
        return (int_today - int_date);
    }

}
