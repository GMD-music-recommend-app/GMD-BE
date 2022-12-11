package com.sesac.gmd.src.Chart;

import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ChartProvider chartProvider;
    @Autowired
    private JwtService jwtService;

    public ChartController(ChartProvider chartProvider, JwtService jwtService){
        this.chartProvider = chartProvider;
        this.jwtService = jwtService;
    }
}
