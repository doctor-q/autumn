package cc.doctor.framework.functions;

import cc.doctor.framework.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Dates {

    public interface DateIterateFun {
        void fun(Date date);
    }

    /**
     * 天级别迭代
     *
     * @param from 起始天 起始天要大于结束天
     * @param to   到达天
     */
    public static void iterateDay(Date from, Date to, DateIterateFun dateIterateFun) {
        if (from == null || to == null || from.before(to)) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        while (!calendar.getTime().before(to)) {
            dateIterateFun.fun(calendar.getTime());
            calendar.add(Calendar.DATE, -1);
        }
    }

    /**
     * 月级别迭代
     *
     * @param from           从哪一天开始 起始天要大于结束天
     * @param to             到哪一天
     * @param dateIterateFun 迭代函数
     */
    public static void iterateMonth(Date from, Date to, DateIterateFun dateIterateFun) {
        if (from == null || to == null || from.before(to)) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        to = DateUtils.monthTime(to);
        while (!calendar.getTime().before(to)) {
            dateIterateFun.fun(calendar.getTime());
            calendar.add(Calendar.MONTH, -1);
        }
    }
}
