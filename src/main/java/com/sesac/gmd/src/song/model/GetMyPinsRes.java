package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyPinsRes {
    private int pinIdx;
    private int userIdx;
    private String title;
    private String singer;
    private String albumCover;
    private String state;
    private String city;
    private String street;
}
