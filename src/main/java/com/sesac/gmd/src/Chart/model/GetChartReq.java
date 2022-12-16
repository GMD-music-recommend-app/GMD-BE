package com.sesac.gmd.src.Chart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChartReq {
    private double latitude;
    private double longitude;
}
