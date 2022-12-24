package com.sesac.gmd.src.Chart;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.Chart.model.GetChartReq;
import com.sesac.gmd.src.Chart.model.GetChartRes;
import com.sesac.gmd.src.song.model.GetPinsRes;
import com.sesac.gmd.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sesac.gmd.config.BaseResponseStatus.SUCCESS;
import static com.sesac.gmd.utils.Validation.locationValidation;

@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ChartProvider chartProvider;

    public ChartController(ChartProvider chartProvider){
        this.chartProvider = chartProvider;
    }

    /* 현 위치의 지역구 기준 인기차트 반환 */
    @ApiOperation("지역 내 인기차트 반환")
    @ResponseBody
    @GetMapping("/{city}")
    public BaseResponse<List<GetChartRes>> getChart(@PathVariable("city") String city){
        try{
            List<GetChartRes> getChartRes = chartProvider.getChart(city);
            return new BaseResponse<>(getChartRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
