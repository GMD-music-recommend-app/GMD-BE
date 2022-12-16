package com.sesac.gmd.src.Chart;

import org.springframework.stereotype.Repository;

@Repository
public class ChartDao {
    String query = "select pinIdx, count(pinIdx) as 'count_like' from pin_like_tbl group by pinIdx order by count_like desc";
}
