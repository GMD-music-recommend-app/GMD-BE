package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private int pinIdx;
    private int userIdx;
    private String nickname;
    private String content;
}
