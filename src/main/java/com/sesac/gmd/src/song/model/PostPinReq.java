package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPinReq {
    private int userIdx;
    private int songIdx;
    private String title;
    private String artist;
    private String albumTitle;
    private String albumImage;
    private String reason;
    private String hashtag;
    private double latitude;
    private double longitude;
    private String state;
    private String city;
}

