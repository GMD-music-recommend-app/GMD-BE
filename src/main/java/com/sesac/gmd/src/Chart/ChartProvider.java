package com.sesac.gmd.src.Chart;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.Chart.model.GetChartReq;
import com.sesac.gmd.src.Chart.model.GetChartRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sesac.gmd.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ChartProvider {
    @Autowired
    private ChartDao chartDao;

    public ChartProvider(ChartDao chartDao){
        this.chartDao = chartDao;
    }

    /* 현 위치의 지역구 기준 인기차트 반환 */
    public List<GetChartRes> getChart(String city) throws BaseException{
        try{
            return chartDao.getChart(city);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
