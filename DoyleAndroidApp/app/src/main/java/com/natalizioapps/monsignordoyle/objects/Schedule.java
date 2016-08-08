package com.natalizioapps.monsignordoyle.objects;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Reno on 15-06-18.
 */
public class Schedule {
    //TODO: COMMENT
    @IntDef({PERIOD1, PERIOD2, PERIOD3, PERIOD4, LUNCH, NOTATSCHOOL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PERIOD {}

    public static final int PERIOD1 = 1;
    public static final int PERIOD2 = 2;
    public static final int PERIOD3 = 3;
    public static final int PERIOD4 = 4;
    public static final int LUNCH = 0;
    public static final int NOTATSCHOOL = -1;

    private String P1start = "", P1end = "",
            P2start = "", P2end = "",
            P3start = "", P3end = "",
            P4start = "", P4end = "",
            LUNCHstart = "", LUNCHend = "";

    @NonNull
    public String getStartTime(@PERIOD int period) {
        if (period == PERIOD1)
            return P1start;
        else if (period == PERIOD2)
            return P2start;
        else if (period == PERIOD3)
            return P3start;
        else if (period == PERIOD4)
            return P4start;
        else
            return LUNCHstart;
    }

    @NonNull
    public String getEndTime(@PERIOD int period) {
        if (period == PERIOD1)
            return P1end;
        else if (period == PERIOD2)
            return P2end;
        else if (period == PERIOD3)
            return P3end;
        else if (period == PERIOD4)
            return P4end;
        else
            return LUNCHend;
    }

    public DateTime getEndDateTime(@PERIOD int period) {
        // Parse the time into the object
        DateTimeFormatter timeformat = DateTimeFormat.forPattern("h:mm a");
        DateTime end = timeformat.parseDateTime(getEndTime(period));

        // Parse the current date into the object
        DateTime now = DateTime.now();
        end = end.withDate(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());
        return end;
    }

    public void setP1(String startTime, String endTime) {
        P1start = startTime;
        P1end = endTime;
    }

    public void setP2(String startTime, String endTime) {
        P2start = startTime;
        P2end = endTime;
    }

    public void setP3(String startTime, String endTime) {
        P3start = startTime;
        P3end = endTime;
    }

    public void setP4(String startTime, String endTime) {
        P4start = startTime;
        P4end = endTime;
    }

    public void setLUNCH(String startTime, String endTime) {
        LUNCHstart = startTime;
        LUNCHend = endTime;
    }

}
