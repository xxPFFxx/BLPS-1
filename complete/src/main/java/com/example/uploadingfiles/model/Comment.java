package com.example.uploadingfiles.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "videoinfo_id", nullable = false)
    private VideoInfo videoinfo;

    private String text;

    public Comment(String text) {
        this.text = text;
    }

    public Comment() {

    }

    public Comment(User user, VideoInfo videoinfo, String text) {
        this.user = user;
        this.videoinfo = videoinfo;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VideoInfo getVideoInfo() {
        return videoinfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoinfo = videoInfo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
