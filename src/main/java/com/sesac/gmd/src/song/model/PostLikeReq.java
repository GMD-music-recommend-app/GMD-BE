package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLikeReq {
    private int userIdx;
    private int songIdx;
}
