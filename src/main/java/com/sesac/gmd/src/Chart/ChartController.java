package com.sesac.gmd.src.Chart;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.Chart.model.GetChartReq;
import com.sesac.gmd.src.Chart.model.GetChartRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sesac.gmd.utils.Validation.locationValidation;

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

    //@ResponseBody
    //@GetMapping("")
    /* public BaseResponse<List<GetChartRes>> getChart(@RequestParam double latitude, @RequestParam double longitude, GetChartReq getChartReq){
        try{
            BaseResponseStatus status = locationValidation(latitude, longitude);
            getChartReq = new GetChartReq(latitude, longitude);

        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    } */
}
