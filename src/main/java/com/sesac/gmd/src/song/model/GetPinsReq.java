package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPinsReq {
    private double latitude;
    private double longitude;
}
