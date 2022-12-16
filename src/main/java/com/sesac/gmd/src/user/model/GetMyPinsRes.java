package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyPinsRes {
    private int pinIdx;
    private int userIdx;
    private String songTitle;
    private String artist;
    private String albumImage;
    private String state;
    private String city;
}
