package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchLocationReq {
    private int userIdx;
    private String state;
    private String city;
    private String street;
}
