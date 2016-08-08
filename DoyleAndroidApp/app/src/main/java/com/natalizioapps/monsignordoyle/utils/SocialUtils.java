package com.natalizioapps.monsignordoyle.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Reno on 15-07-01.
 */
public class SocialUtils {

    /**
     * Read the passed in date string with the twitter formatting
     * @param rss {String} : date string
     * @return {DateTime} : an object representing the date
     */
    private static DateTime getTwitterDate(String rss) {
        String form = "EEE MMM dd HH:mm:ss Z yyyy";
        DateTimeFormatter format = DateTimeFormat.forPattern(form).withLocale(Locale.ENGLISH);
        return format.parseDateTime(rss);
    }

    /**
     * Gets the elapsed time since a date
     * If less than 7 days old, it will return #d##h##m
     * If older, it will return the month and day
     *
     * @param date {DateTime} : The original date
     * @return {String} : The elapsed time in String format
     */
    private static String getOffsetFromDate(DateTime date) {
        DateTime now = DateTime.now();
        // Get the intervals of time between the date and now
        Minutes min = Minutes.minutesBetween(date, now);
        Hours hours = Hours.hoursBetween(date, now);
        Days days = Days.daysBetween(date, now);
        // Turn those numbers into a string
        if (min.getMinutes() < 60) {
            return min.getMinutes() + "m";
        } else if (hours.getHours() < 24) {
            return hours.getHours() + "h";
        } else if (days.getDays() < 7) {
            return days.getDays() + "d";
        } else {
            DateTimeFormatter format = DateTimeFormat.forPattern("MMM d");
            return format.print(date).toUpperCase();
        }
    }

    /**
     * Returns the elapsed time since a date in strng format (twitter)
     * @param rss {String} : the date
     * @return {String} : the elapsed time i string format
     */
    public static String getOffsetFromRSS(String rss) {
        return getOffsetFromDate(getTwitterDate(rss));
    }

}
