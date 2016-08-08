package com.natalizioapps.monsignordoyle.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.objects.Schedule;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import org.javatuples.Pair;

import java.util.ArrayList;

/**
 * Fetches data for the app based on the current school.
 */
public class SchoolConstants {

    /**
     * The flipweek module should only be shown for Monsignor Doyle
     *
     * @return boolean : show the flipweek module
     */
    public static boolean moduleFlipweek() {
        return PrefSingleton.getInstance().getHighSchool() == HighSchools.MONSIGNORDOYLE;
    }

    /**
     * The school map module should only be shown for Monsignor Doyle
     * Enable it for other schools when development has finished for their maps
     *
     * @return boolean : show the school map module
     */
    public static boolean moduleSchoolMap() {
        return PrefSingleton.getInstance().getHighSchool() == HighSchools.MONSIGNORDOYLE;
    }

    /**
     * Creates the {@link Schedule} object for the specific school
     * If no high school is selected, this will return null
     *
     * @return Schedule
     */
    @Nullable
    public static Schedule getSchedule() {
        switch (PrefSingleton.getInstance().getHighSchool()) {
            case MONSIGNORDOYLE: {
                Schedule DoyleSchedule = new Schedule();
                DoyleSchedule.setP1("8:35 AM", "9:50 AM");
                DoyleSchedule.setP2("9:56 AM", "11:11 AM");
                DoyleSchedule.setLUNCH("11:11 AM", "11:57 AM");
                DoyleSchedule.setP3("12:03 PM", "1:18 PM");
                DoyleSchedule.setP4("1:24 PM", "2:39 PM");
                return DoyleSchedule;
            }
            case STBENEDICTS: {
                Schedule StBenedictSchedule = new Schedule();
                StBenedictSchedule.setP1("8:00 AM", "9:15 AM");
                StBenedictSchedule.setP2("9:20 AM", "10:35 AM");
                StBenedictSchedule.setLUNCH("10:40 AM", "11:25 AM");
                StBenedictSchedule.setP3("11:30 AM", "12:45 PM");
                StBenedictSchedule.setP4("12:50 PM", "2:05 PM");
                return StBenedictSchedule;
            }
            case RESURRECTION: {
                Schedule ResurrectionSchedule = new Schedule();
                ResurrectionSchedule.setP1("8:10 AM", "9:25 AM");
                ResurrectionSchedule.setP2("9:30 AM", "10:45 AM");
                ResurrectionSchedule.setLUNCH("10:45 AM", "11:30 AM");
                ResurrectionSchedule.setP3("11:35 AM", "12:50 PM");
                ResurrectionSchedule.setP4("12:55 PM", "2:10 PM");
                return ResurrectionSchedule;
            }
            case STDAVIDS: {
                Schedule StDavidSchedule = new Schedule();
                StDavidSchedule.setP1("8:15 AM", "9:30 AM");
                StDavidSchedule.setP2("9:35 AM", "10:50 AM");
                StDavidSchedule.setLUNCH("10:50 AM", "11:40 AM");
                StDavidSchedule.setP3("11:45 AM", "1:00 PM");
                StDavidSchedule.setP4("1:05 PM", "2:20 PM");
                return StDavidSchedule;
            }
            case STMARYS: {
                // Two schedules to accommodate two lunch periods
                if (PrefSingleton.getInstance().getLunch() == 1) {
                    Schedule StMarySchedule1 = new Schedule();
                    StMarySchedule1.setP1("9:05 AM", "10:20 AM");
                    StMarySchedule1.setP2("10:27 AM", "11:42 AM");
                    StMarySchedule1.setLUNCH("11:49 AM", "12:27 AM");
                    StMarySchedule1.setP3("12:33 PM", "1:48 PM");
                    StMarySchedule1.setP4("1:55 PM", "3:10 PM");
                    return StMarySchedule1;
                } else {
                    Schedule StMarySchedule2 = new Schedule();
                    StMarySchedule2.setP1("9:05 AM", "10:20 AM");
                    StMarySchedule2.setP2("10:27 AM", "11:42 AM");
                    StMarySchedule2.setP3("11:49 AM", "1:04 PM");
                    StMarySchedule2.setLUNCH("1:11 PM", "1:48 PM");
                    StMarySchedule2.setP4("1:55 PM", "3:10 PM");
                    return StMarySchedule2;
                }
            }
            case UNDEF:
                return null;
        }
        return null;
    }

    /**
     * Returns the travel time for each individual school
     * i.e. the time in between bells to travel from one period to another
     * Travel time is used for padding when calculating which period is up next
     */
    public static int getTravelTime() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return 6;
        else if (hs == HighSchools.STBENEDICTS)
            return 5;
        else if (hs == HighSchools.STDAVIDS)
            return 5;
        else if (hs == HighSchools.RESURRECTION)
            return 5;
        else if (hs == HighSchools.STMARYS)
            return 7;
        else
            return 0;
    }

    /**
     * The main contact phone number for each school
     * @return {String} : the phone number
     */
    @Nullable
    public static String getSchoolPhone() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "(519) 622-1290";
        else if (hs == HighSchools.STBENEDICTS)
            return "(519) 621-4050";
        else if (hs == HighSchools.STDAVIDS)
            return "(519) 885-1340";
        else if (hs == HighSchools.RESURRECTION)
            return "(519) 741-1990";
        else if (hs == HighSchools.STMARYS)
            return "(519) 745-6891";
        else
            return null;
    }

    /**
     * If a school has a separate attendance phone number, it will be here
     * @return {String} : phone number
     */
    @Nullable
    public static String getAttendancePhone() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.STBENEDICTS)
            return "(519) 621-4001";
        else if (hs == HighSchools.STDAVIDS)
            return "(519) 885-7352";
        else if (hs == HighSchools.RESURRECTION)
            return "(519) 741-8542";
        else if (hs == HighSchools.STMARYS)
            return "(519) 745-6789";
        else
            return null;
    }

    /**
     * If office hours were found, they are here
     * @return {String} : office hours
     */
    @Nullable
    public static String getOfficeHours() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "Monday - Friday 7:30am - 3:30pm";
        else if (hs == HighSchools.STMARYS)
            return "Monday - Friday 8:00am - 4:00pm";
        else
            return null;
    }

    /**
     * The display name for each school
     * @return {String} : school name
     */
    @NonNull
    public static String getSchoolName() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "Monsignor Doyle";
        else if (hs == HighSchools.STBENEDICTS)
            return "St. Benedict's";
        else if (hs == HighSchools.STDAVIDS)
            return "St. David's";
        else if (hs == HighSchools.RESURRECTION)
            return "Resurrection";
        else if (hs == HighSchools.STMARYS)
            return "St. Mary's";
        else
            return "WCDSB";
    }

    /**
     * The main website for the school
     * @return {String} : website url
     */
    @NonNull
    public static String getSchoolWebsite() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "http://doyle.wcdsb.ca";
        else if (hs == HighSchools.STBENEDICTS)
            return "http://stbenedict.wcdsb.ca";
        else if (hs == HighSchools.STDAVIDS)
            return "http://stdavid.wcdsb.ca";
        else if (hs == HighSchools.RESURRECTION)
            return "http://resurrection.wcdsb.ca";
        else if (hs == HighSchools.STMARYS)
            return "http://stmary.wcdsb.ca";
        else
            return "";
    }

    @NonNull
    public static String getSchoolClassnet() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "https://classnet.wcdsb.ca/sec/MD/Pages/default.aspx";
        else if (hs == HighSchools.STBENEDICTS)
            return "https://classnet.wcdsb.ca/SEC/STB/Pages/default.aspx";
        else if (hs == HighSchools.STDAVIDS)
            return "https://classnet.wcdsb.ca/sec/StD/Pages/default.aspx";
        else if (hs == HighSchools.RESURRECTION)
            return "https://classnet.wcdsb.ca/sec/RCS/Pages/default.aspx";
        else if (hs == HighSchools.STMARYS)
            return "https://classnet.wcdsb.ca/sec/StM/Pages/default.aspx";
        else
            return "";
    }

    @NonNull
    public static String getSchoolAddress() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "185 Myers Road, Cambridge ON, N1R 7H2";
        else if (hs == HighSchools.STBENEDICTS)
            return "50 Saginaw Pkwy, Cambridge ON, N1R 5W1";
        else if (hs == HighSchools.STDAVIDS)
            return "4 High St, Waterloo ON, N2L 3X5";
        else if (hs == HighSchools.RESURRECTION)
            return "455 University Ave. West, Kitchener ON, N2N 3B9";
        else if (hs == HighSchools.STMARYS)
            return "1500 Block Line Rd, Kitchener ON, N2C 2S2";
        else
            return "";
    }

    @NonNull
    public static String getSchoolGMaps() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "http://goo.gl/maps/tnwqW";
        else if (hs == HighSchools.STBENEDICTS)
            return "http://goo.gl/maps/jEfam";
        else if (hs == HighSchools.STDAVIDS)
            return "http://goo.gl/maps/jc755";
        else if (hs == HighSchools.RESURRECTION)
            return "http://goo.gl/maps/H9uBH";
        else if (hs == HighSchools.STMARYS)
            return "http://goo.gl/maps/ndTTF";
        else
            return "";
    }

    @NonNull
    public static String getSchoolWeatherLink() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return "http://api.openweathermap.org/data/2.5/weather?id=5913695&units=metric";
        else if (hs == HighSchools.STBENEDICTS)
            return "http://api.openweathermap.org/data/2.5/weather?id=5913695&units=metric";
        else if (hs == HighSchools.STDAVIDS)
            return "http://api.openweathermap.org/data/2.5/weather?id=6176823&units=metric";
        else if (hs == HighSchools.RESURRECTION)
            return "http://api.openweathermap.org/data/2.5/weather?id=6176823&units=metric";
        else if (hs == HighSchools.STMARYS)
            return "http://api.openweathermap.org/data/2.5/weather?id=6176823units=metric";
        else
            return "";
    }

    /**
     * Returns a Url for the specific school's calendar.
     * If web is false, the Url will be either an html file or web address.
     * If web is true than only the web address will be returned, so that the link may be opened
     * in the browser
     *
     * @param web boolean : web addresses only
     * @return String : the calendar url
     */
    @NonNull
    public static String getSchoolCalendarUrl(boolean web) {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return web ? "https://www.google.com/calendar/embed?title=Monsignor%20Doyle&amp;" +
                    "showPrint=0&amp;showTz=0&amp;height=600&amp;wkst=1&amp;" +
                    "bgcolor=%23FFFFFF&amp;src=caigdkmpdr0of1e8ojavrlgrtg%40group.calendar.google" +
                    ".com&amp;color=%238C500B&amp;src=fis20u6sqalb6q2ofijm7t47ok%40group.calendar" +
                    ".google.com&amp;color=%238C500B&amp;src=qi9iuccug3qt688d2l5fdb58a0%40group" +
                    ".calendar.google.com&amp;color=%238C500B&amp;" +
                    "src=kd9dccblpd3cf6qa33f6ft0guc%40group.calendar.google.com&amp;" +
                    "color=%238C500B&amp;src=ctod08u8mvftbpgsh2r4ge3sqk%40group.calendar.google" +
                    ".com&amp;color=%238C500B&amp;src=dp83dlj5j30frdojbq3j2n0qug%40group.calendar" +
                    ".google.com&amp;color=%238C500B&amp;ctz=America%2FToronto"
                    : "file:///android_asset/doylecalendar.html";
        else if (hs == HighSchools.STBENEDICTS)
            return web ? "http://stbenedict.wcdsb.ca/index.php?p=calendar/fetchcal.php&show" :
                    "file:///android_asset/benedictcalendar.html";
        else if (hs == HighSchools.STDAVIDS)
            return web ? "https://www.google.com/calendar/embed?title=St.%20David&#39;s&amp;" +
                    "showPrint=0&amp;showTz=0&amp;height=600&amp;wkst=1&amp;" +
                    "bgcolor=%23FFFFFF&amp;src=stdavidcss%40gmail.com&amp;color=%232952A3&amp;" +
                    "ctz=America%2FToronto"
                    : "file:///android_asset/davidcalendar.html";
        else if (hs == HighSchools.RESURRECTION)
            return web ? "https://www.google.com/calendar/embed?title=Resurrection&amp;" +
                    "showPrint=0&amp;showTz=0&amp;height=600&amp;wkst=1&amp;" +
                    "bgcolor=%23FFFFFF&amp;src=en.canadian%23holiday%40group.v.calendar.google" +
                    ".com&amp;color=%23875509&amp;src=vcfuua42plai6r018dp2b4fbgo%40group.calendar" +
                    ".google.com&amp;color=%23875509&amp;src=tr1opn9th59h9hvmqc1p025fqg%40group" +
                    ".calendar.google.com&amp;color=%23B1365F&amp;src=resurrection%40wcdsb" +
                    ".ca&amp;color=%236B3304&amp;src=fqeamfrtq6h43smc2iogjj0ltg%40group.calendar" +
                    ".google.com&amp;color=%23B1440E&amp;ctz=America%2FToronto"
                    : "file:///android_asset/resurrectioncalendar.html";
        else if (hs == HighSchools.STMARYS)
            return "https://docs.google" +
                    ".com/document/d/1KA6m_PV8dQsgpRxb9vRVKeFn9TnMfpaxL7VfKDEEVz4/edit";
        else
            return "";
    }

    /**
     * Returns the drawable id for the main image displayed for each school
     * @return {int} : dawable resource id
     */
    public static int getHeroImage() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE)
            return R.drawable.heroimagedoyle;
        else if (hs == HighSchools.STBENEDICTS)
            return R.drawable.heroimagebenedict;
        else if (hs == HighSchools.STDAVIDS)
            return R.drawable.heroimagedavids;
        else if (hs == HighSchools.RESURRECTION)
            return R.drawable.heroimageresurrection;
        else if (hs == HighSchools.STMARYS)
            return R.drawable.heroimagemarys;
        else
            return -1;
    }

    /**
     * Return an array of all the social accounts for the school
     * Note the data link MUST be twitter because that is all I feel like parsing right now
     */
    @Nullable
    public static SocialAccount[] getSocialAccounts() {
        HighSchools hs = PrefSingleton.getInstance().getHighSchool();
        if (hs == HighSchools.MONSIGNORDOYLE) {
            SocialAccount main = new SocialAccount();
            main.setTitle("Monsignor Doyle");
            main.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=215034133&screen_name=mondoylecss");
            main.addSocialLink("FACEBOOK", "https://www.facebook" +
                    ".com/pages/Monsignor-Doyle-Catholic-Secondary-School/471286489571173");
            main.addSocialLink("TWITTER", "https://twitter.com/MonDoyleCSS");

            SocialAccount sac = new SocialAccount();
            sac.setTitle("Doyle SAC");
            sac.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=1692693853&screen_name=doylesac");
            sac.addSocialLink("TWITTER", "https://twitter.com/DoyleSAC");
            sac.addSocialLink("INSTAGRAM", "https://i.instagram.com/doylesac/");
            return new SocialAccount[]{main, sac};
        } else if (hs == HighSchools.STBENEDICTS) {
            SocialAccount main = new SocialAccount();
            main.setTitle("St. Benedict CSS");
            main.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=402958517&screen_name=StBenedictCSS");
            main.addSocialLink("TWITTER", "https://twitter.com/stbenedictcss");

            SocialAccount sac = new SocialAccount();
            sac.setTitle("SAC Updates");
            sac.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=1673144989&screen_name=SAC_Updates");
            sac.addSocialLink("TWITTER", "https://twitter.com/SAC_Updates");
            sac.addSocialLink("INSTAGRAM", "https://instagram.com/bennies_sac_updates");
            sac.addSocialLink("BLOG", "http://sacupdates.tumblr.com/");
            return new SocialAccount[]{main, sac};
        } else if (hs == HighSchools.STDAVIDS) {
            SocialAccount main = new SocialAccount();
            main.setTitle("St. David Celtics");
            main.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=916948706&screen_name=StDavidCeltics");
            main.addSocialLink("TWITTER", "https://twitter.com/StDavidCeltics");

            SocialAccount athletics = new SocialAccount();
            athletics.setTitle("St. David Athletics");
            athletics.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=902984120&screen_name=StDavidAthletic");
            athletics.addSocialLink("TWITTER", "https://twitter.com/StDavidAthletic");
            athletics.addSocialLink("FACEBOOK", "https://www.facebook" +
                    ".com/pages/St-David-Athletics/526745857338958");
            return new SocialAccount[]{main, athletics};
        } else if (hs == HighSchools.RESURRECTION) {
            SocialAccount main = new SocialAccount();
            main.setTitle("Resurrection C.S.S");
            main.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=780618392&screen_name=ResCSS");
            main.addSocialLink("TWITTER", "https://twitter.com/ResCSS");
            main.addSocialLink("FACEBOOK", "https://www.facebook" +
                    ".com/pages/Resurrection-Catholic-Secondary-School-Official/136725813138271");

            SocialAccount athletics = new SocialAccount();
            athletics.setTitle("Rez Athletics");
            athletics.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=2848659880&screen_name=RezAthletics");
            athletics.addSocialLink("TWITTER", "https://twitter.com/RezAthletics");
            return new SocialAccount[]{main, athletics};
        } else if (hs == HighSchools.STMARYS) {
            SocialAccount main = new SocialAccount();
            main.setTitle("Jerome the Eagle");
            main.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=324120339&screen_name=St_Marys_Kitch");
            main.addSocialLink("TWITTER", "https://twitter.com/St_Marys_Kitch");
            main.addSocialLink("FACEBOOK", "https://www.facebook.com/SMH.Kitchener");

            SocialAccount athletics = new SocialAccount();
            athletics.setTitle("SMHS Athletics");
            athletics.setDataurl("https://api.twitter.com/1.1/statuses/user_timeline" +
                    ".json?count=20&user_id=2853772409&screen_name=SMHS_Athletics");
            athletics.addSocialLink("TWITTER", "https://twitter.com/SMHS_Athletics");
            return new SocialAccount[]{main, athletics};
        } else
            return null;
    }

    /**
     * A social account is a data model for a social media account.
     * This account can be across multiple social networks. Each social link has a URL to a
     * different social media account. The data url is used to display tweet, and must only be
     * a call to the twitter api.
     */
    public static class SocialAccount {
        String title;
        String dataurl;
        ArrayList<Pair<String, String>> socialLinks;

        public SocialAccount() {
            socialLinks = new ArrayList<>();
        }

        public void setTitle(String t) {
            title = t;
        }

        public void setDataurl(String url) {
            dataurl = url;
        }

        public void addSocialLink(String title, String url) {
            socialLinks.add(new Pair<>(title, url));
        }

        public void setSocialLinks(ArrayList<Pair<String, String>> links) {
            socialLinks = links;
        }

        public String getTitle() {
            return title;
        }

        public String getDataurl() {
            return dataurl;
        }

        public ArrayList<Pair<String, String>> getSocialLinks() {
            return socialLinks;
        }
    }
}
