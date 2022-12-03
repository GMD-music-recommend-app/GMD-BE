package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.PostPinReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SongDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* 핀 생성 API */
    public int createPin(PostPinReq postPinReq) {
        String query = "insert into song_tbl values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default)";
        Object[] params = new Object[] {
                postPinReq.getUserIdx(), postPinReq.getTitle(), postPinReq.getSinger(),
                postPinReq.getAlbum(), postPinReq.getAlbumCover(),
                postPinReq.getReason(), postPinReq.getHashtag(),
                postPinReq.getLatitude(), postPinReq.getLongitude(),
                postPinReq.getState(), postPinReq.getCity(), postPinReq.getStreet() };

        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }
}
