package com.example.githubportfolio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("repository")
    @Expose
    private Repository repository;
    @SerializedName("subject")
    @Expose
    private Subject subject;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("unread")
    @Expose
    private Boolean unread;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("last_read_at")
    @Expose
    private String lastReadAt;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
