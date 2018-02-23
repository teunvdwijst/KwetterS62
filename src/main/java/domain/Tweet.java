/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Teun
 */
@Entity
@NamedQuery(name = "Tweet.allTweets", query = "SELECT t FROM Tweet t")
public class Tweet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String content;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date published;
    private List<String> tags;
    @ManyToOne
    private Account tweetedBy;
    @OneToMany
    private final List<Account> likedBy = new ArrayList<>();
    @OneToMany
    private final List<Account> mentions = new ArrayList<>();

    // <editor-fold desc="Getters and Setters" defaultstate="collapsed">
    public List<Account> getLikedBy() {
        return Collections.unmodifiableList(likedBy);
    }

    public List<Account> getMentions() {
        return Collections.unmodifiableList(mentions);
    }

    public Account getTweetedBy() {
        return tweetedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Long getId() {
        return id;
    }
    // </editor-fold>

    public Tweet() {
    }

    public Tweet(String content, Account tweetedBy) {
        this.tweetedBy = tweetedBy;
        this.content = content;
        this.tags = findTags(content);
    }

    public Tweet(String content, Account tweetedBy, Date published) {
        this(content, tweetedBy);
        this.published = published;
        this.tags = findTags(content);
    }

    /**
     * Finds tags preceded with a '#' and returns them as a List of Strings
     *
     * @param message String
     * @return List of String objects
     */
    private List<String> findTags(String message) {
        return findRegexMatches(message, "(?:\\s#)([A-Za-z0-9_]+)");
    }

    /**
     * Searches for passed regex matches in the message and returns them as a
     * list
     *
     * @param message String
     * @param regex String
     * @return List of String objects
     */
    private List<String> findRegexMatches(String message, String regex) {
        List<String> matches = new ArrayList<>();
        String prefixedString = " ".concat(message);
        Matcher m = Pattern.compile(regex).matcher(prefixedString);
        while (m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }

    /**
     * Adds an Account to the list of accounts that liked this tweet
     *
     * @param a Account
     */
    public void addLike(Account a) {
        if (!likedBy.contains(a)) {
            likedBy.add(a);
        }
    }

    /**
     * Removes an Account from the list of accounts that liked this tweet
     *
     * @param a
     */
    public void removeLike(Account a) {
        if (likedBy.contains(a)) {
            likedBy.remove(a);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Tweet)) {
            return false;
        }

        final Tweet other = (Tweet) obj;

        if (!Objects.equals(this.content, other.content)) {
            return false;
        } else if (!Objects.equals(this.published, other.published)) {
            return false;
        } else if (!Objects.equals(this.tweetedBy, other.tweetedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.content);
        hash = 67 * hash + Objects.hashCode(this.published);
        hash = 67 * hash + Objects.hashCode(this.tags);
        hash = 67 * hash + Objects.hashCode(this.tweetedBy);
        hash = 67 * hash + Objects.hashCode(this.likedBy);
        hash = 67 * hash + Objects.hashCode(this.mentions);
        return hash;
    }
}
