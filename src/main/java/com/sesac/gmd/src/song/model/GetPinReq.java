package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPinReq {
    private int userIdx;
    private int pinIdx;
}
