package cn.rainss.smartNote.schedule.model;

import cn.bmob.v3.BmobObject;

/**
 * Note 的 bean 类
 */
public class Note extends BmobObject {

    private int id;
    private String title;
    private String content;
    private String time;
    private String priority;
    private String user;
    private Long clockTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getClockTime() {
        return clockTime;
    }

    public void setClockTime(Long clockTime) {
        this.clockTime = clockTime;
    }

}
