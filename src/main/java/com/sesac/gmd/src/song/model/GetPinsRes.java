package com.sesac.gmd.src.song.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPinsRes {
    private int pinIdx;
    private double distance;
    private double latitude;
    private double longitude;
    private String state;
    private String city;
    private String street;
}
