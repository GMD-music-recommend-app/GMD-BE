package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPinsReq {
    private double latitude;
    private double longitude;
}
