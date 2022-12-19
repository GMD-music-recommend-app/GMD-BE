package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentRes {
    private int commentIdx;
    private int pinIdx;
    private int userIdx;
    private String artist;
    private String songTitle;
    private String albumTitle;
    private String content;
    private String state;
    private String city;
    private String street;
}
