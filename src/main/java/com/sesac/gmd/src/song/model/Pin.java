package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pin {
    private int pinIdx;
    private int userIdx;
    private String title;
    private String singer;
    private String album;
    private String albumCover;
    private String reason;
    private String hashtag;
    private String isLiked;
    private List<Comment> comments;
    private double latitude;
    private double longitude;
    private String state;
    private String city;
    private String street;
}
