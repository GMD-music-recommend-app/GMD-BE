package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.SequenceGenerators;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeReq {
    private int userIdx;
    private int pinIdx;
}
