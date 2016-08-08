package com.natalizioapps.monsignordoyle.objects;

import com.natalizioapps.monsignordoyle.utils.SocialUtils;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * An object which contains the necessary elements of a tweet
 */
public class Tweet {
    //TODO: COMMENT
    private String content;
    private String time;
    private String link;

    /**
     * Blank constructor
     */
    public Tweet() {}

    /**
     * Get the content of the post
     *
     * @return {String} : content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the post
     *
     * @param content {String}
     */
    public void setContent(String content) {
        this.content = StringEscapeUtils.unescapeHtml4(content);
    }

    /**
     * Get the time of the post as formatted in the xml
     *
     * @return {String} : Time of the post
     */
    public String getTime() {
        return time;
    }

    /**
     * Set the time of the post as formatted for display
     *
     * @param time {String} : Time of the post
     */
    public void setTime(String time) {
        this.time = SocialUtils.getOffsetFromRSS(time);
    }

    public String getLink() {return link;}

    /**
     * Creates the link to a tweet based on set parameters.
     * @param screenname {String} : the tweeter's screen name
     * @param id {String} : the tweet id
     */
    public void setLink(String screenname, String id) {
        this.link = "https://twitter.com/" + screenname + "/status/" + id;
    }

}
