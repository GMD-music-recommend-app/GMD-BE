package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    /* 핀 반환 API */
    public Pin getPin(GetPinReq getPinReq, List<Comment> comments) {
        String query = "select songIdx as pinIdx, userIdx, " +
                "title, singer, album, albumCover, " +
                "reason, hashtag, " +
                "exists(select * from song_like_tbl where userIdx=?) as isLiked, " +
                "latitude, longitude, state, city, street " +
                "from song_tbl\n" +
                "where status='A'";
        int params = getPinReq.getUserIdx();

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new Pin(
                    rs.getInt("pinIdx"),
                    rs.getInt("userIdx"),
                    rs.getString("title"),
                    rs.getString("singer"),
                    rs.getString("album"),
                    rs.getString("albumCover"),
                    rs.getString("reason"),
                    rs.getString("hashtag"),
                    rs.getString("isLiked"),
                    comments,
                    rs.getInt("latitude"),
                    rs.getInt("longitude"),
                    rs.getString("state"),
                    rs.getString("city"),
                    rs.getString("street")
                ), params);
    }

    /* 댓글 반환 API */
    public List<Comment> getComments(int pinIdx) {
        String query = "select comment.songIdx as pinIdx, " +
                "   comment.userIdx, user.nickname, " +
                "   comment.content " +
                "from song_comment_tbl as comment\n" +
                "    join user_tbl as user\n" +
                "        on user.userIdx = comment.userIdx\n" +
                "        and user.status='A'\n" +
                "where songIdx=? and comment.status='A'";

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new Comment(
                        rs.getInt("pinIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("content")
                ), pinIdx);
    }

    /* 핀 리스트 반환 API */
    public List<GetPinsRes> getPins(GetPinsReq getPinsReq) {
        String query = "select songIdx as pinIdx,\n" +
                "    ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) as distance,\n" +
                "    latitude, longitude, state, city, street\n" +
                "from song_tbl\n" +
                "    where ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) <= 5000 and status='A'";
        Object[] params = new Object[] {
                getPinsReq.getLongitude(), getPinsReq.getLatitude(),
                getPinsReq.getLongitude(), getPinsReq.getLatitude(),
        };

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetPinsRes(
                        rs.getInt("pinIdx"),
                        rs.getDouble("distance"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("state"),
                        rs.getString("city"),
                        rs.getString("street")
                ), params);
    }
}
