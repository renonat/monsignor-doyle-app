package com.natalizioapps.monsignordoyle.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.objects.Schedule;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.utils.singletons.FlipweekSingleton;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Reno on 14-08-29.
 */
public class ClassesUtils {
//TODO: COMMENT
    /**
     * Get the current period based on the time
     * Takes into account school and flipweek/lunch schedule
     *
     * @return
     */
    @Schedule.PERIOD
    public static int currentPeriod(Context context) {
        DateTime now = DateTime.now();
        // Not at school on the weekend
        if (now.getDayOfWeek() != DateTimeConstants.SUNDAY &&
                now.getDayOfWeek() != DateTimeConstants.SATURDAY) {

            // Get our schedule and an array of the periods we want to check
            @Schedule.PERIOD int[] periodarray = new int[]{
                    Schedule.PERIOD1, Schedule.PERIOD2,
                    Schedule.PERIOD3, Schedule.PERIOD4, Schedule.LUNCH
            };

            // For each period check if we are within its start and stop time
            // Add in the travel time buffer before the start of each period
            for (@Schedule.PERIOD int period : periodarray) {
                DateTime start = dateTimeForTime(getStartTimeForPeriod(period, context)).
                        minusMinutes(SchoolConstants.getTravelTime());
                DateTime end = dateTimeForTime(getEndTimeForPeriod(period, context));

                if (start.isBeforeNow() && end.isAfterNow()) {
                    // We are in this period!
                    return period;
                }
            }
        }
        return Schedule.NOTATSCHOOL;
    }

    @NonNull
    public static String getTimesForPeriod(SchoolClass c, Context context) {
        int period = PERIODforSchoolClass(c);
        return getStartTimeForPeriod(period, context) + " - " +
                getEndTimeForPeriod(period, context);
    }

    /**
     * Gets the end time for a period/lunch depending on the school
     *
     * @param period : (int) the period timeslot
     * @return @String the end time
     */
    @NonNull
    public static String getEndTimeForPeriod(@Schedule.PERIOD int period, Context context) {
        HighSchools highschool = PrefSingleton.getInstance().getHighSchool();
        Schedule schedule = SchoolConstants.getSchedule();

        if (schedule != null && highschool != HighSchools.UNDEF) {
            // Check for flipweek at Doyle
            if (highschool == HighSchools.MONSIGNORDOYLE) {
                // Flipweek does not affect P1 to LUNCH so return the value normally
                if (period == Schedule.PERIOD1 || period == Schedule.PERIOD2 ||
                        period == Schedule.LUNCH)
                    return schedule.getEndTime(period);
                else if (FlipweekSingleton.getInstance().getFlipweekStatus()) {
                    // It is flipweek, so return the inverse start time
                    if (period == Schedule.PERIOD3) {
                        return schedule.getEndTime(Schedule.PERIOD4);
                    } else {
                        return schedule.getEndTime(Schedule.PERIOD3);
                    }
                } else {
                    // It is not flipweek so return the value normally
                    return schedule.getEndTime(period);
                }
            } else {
                // For any other school simply return their schedule
                return schedule.getEndTime(period);
            }
        } else
            return "";
    }

    /**
     * Gets the end time for a period/lunch depending on the school
     *
     * @param period : (int) the period timeslot
     * @return @String the start time
     */
    @NonNull
    public static String getStartTimeForPeriod(@Schedule.PERIOD int period, Context context) {
        HighSchools highschool = PrefSingleton.getInstance().getHighSchool();
        Schedule schedule = SchoolConstants.getSchedule();

        if (schedule != null && highschool != HighSchools.UNDEF) {
            // Check for flipweek at Doyle
            if (highschool == HighSchools.MONSIGNORDOYLE) {
                // Flipweek does not affect P1 to LUNCH so return the value normally
                if (period == Schedule.PERIOD1 || period == Schedule.PERIOD2 ||
                        period == Schedule.LUNCH)
                    return schedule.getStartTime(period);
                else if (FlipweekSingleton.getInstance().getFlipweekStatus()) {
                    // It is flipweek, so return the inverse start time
                    if (period == Schedule.PERIOD3) {
                        return schedule.getStartTime(Schedule.PERIOD4);
                    } else {
                        return schedule.getStartTime(Schedule.PERIOD3);
                    }
                } else {
                    // It is not flipweek so return the value normally
                    return schedule.getStartTime(period);
                }
            } else {
                // For any other school simply return their schedule
                return schedule.getStartTime(period);
            }
        } else
            return "";
    }

    private static DateTime dateTimeForTime(String time) {
        // Parse the time into the object
        DateTimeFormatter timeformat = DateTimeFormat.forPattern("h:mm a");
        DateTime start = timeformat.parseDateTime(time);

        // Parse the current date into the object
        DateTime now = DateTime.now();
        start = start.withDate(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());
        return start;
    }

    /**
     * Turns the period int in SchoolClass into an @Schedule.PERIOD int
     *
     * @param schoolClass
     * @return
     */
    @Schedule.PERIOD
    private static int PERIODforSchoolClass(SchoolClass schoolClass) {
        int scp = schoolClass.getPeriod();
        // Else return the real periods
        if (scp == 1)
            return Schedule.PERIOD1;
        else if (scp == 2)
            return Schedule.PERIOD2;
        else if (scp == 3)
            return Schedule.PERIOD3;
        else if (scp == 4)
            return Schedule.PERIOD4;
        return Schedule.NOTATSCHOOL;
    }


    /**
     * Get the order of periods for the day
     * This works around Doyle's flipweek and Mary's alternate lunch periods
     *
     * @return
     */
    @Schedule.PERIOD
    public static int[] orderedPERIODS(Context context) {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        int lunch = PrefSingleton.getInstance().getLunch();

        @Schedule.PERIOD int[] order = new int[5];
        order[0] = Schedule.PERIOD1;
        order[1] = Schedule.PERIOD2;

        if (hs == HighSchools.STMARYS && lunch == 2) {
            order[2] = Schedule.PERIOD3;
            order[3] = Schedule.LUNCH;
            order[4] = Schedule.PERIOD4;
        } else {
            order[2] = Schedule.LUNCH;
            if (FlipweekSingleton.getInstance().getFlipweekStatus()) {
                order[3] = Schedule.PERIOD4;
                order[4] = Schedule.PERIOD3;
            } else {
                order[3] = Schedule.PERIOD3;
                order[4] = Schedule.PERIOD4;
            }
        }
        return order;
    }
}
