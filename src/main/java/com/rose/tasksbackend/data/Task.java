package com.rose.tasksbackend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String uuid;
    private String title;
    private boolean completed;
    private int status;
    private long deadline;
    private long createdTime;
    private long modifiedTime;
    private boolean deleted;
    private String username;

    public Task() {
    }

    public Task(String uuid, String title, boolean completed, int status, long deadline,
                long createdTime, long modifiedTime, boolean deleted, String username) {
        this.uuid = uuid;
        this.title = title;
        this.completed = completed;
        this.status = status;
        this.deadline = deadline;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.deleted = deleted;
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
