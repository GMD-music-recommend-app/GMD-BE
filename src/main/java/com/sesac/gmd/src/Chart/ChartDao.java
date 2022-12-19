package com.sesac.gmd.src.Chart;

import com.sesac.gmd.src.Chart.model.GetChartReq;
import com.sesac.gmd.src.Chart.model.GetChartRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChartDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* 현 위치의 지역구 기준 인기차트 반환 */
    public List<GetChartRes> getChart(GetChartReq getChartReq){
        String query = "select sct.pinIdx, sct.likeCount, pt.city, pt.state, pt.albumImage, pt.songTitle, pt.artist, rank() over (order by likeCount desc) as songRank\n" +
                "from (select pinIdx, count(pinIdx) as 'likeCount' from pin_like_tbl as plt group by pinIdx order by likeCount desc) as sct\n" +
                "right join pin_tbl as pt on sct.pinIdx = pt.pinIdx\n" +
                "where pt.city = ? and likeCount is not null\n" +
                "order by likeCount desc;";

        Object[] params = new Object[]{
                getChartReq.getCity()
        };

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetChartRes(
                        rs.getInt("pinIdx"),
                        rs.getInt("likeCount"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("albumImage"),
                        rs.getString("songTitle"),
                        rs.getString("artist"),
                        rs.getInt("songRank")
                ), params);
    }
}
