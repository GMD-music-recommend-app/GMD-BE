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
    private String singer;
    private String title;
    private String album;
    private String content;
    private String state;
    private String city;
}
