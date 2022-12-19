package com.sesac.gmd.src.Chart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChartRes {
    //핀 인덱스, 순위, 앨범 커버, 제목, 가수, 공감 수
    private int pinIdx;
    private int songRank;
    private String albumImage;
    private String songTitle;
    private String artist;
    private String city;
    private String state;
    private int likeCount;
}
