package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

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
        String query = "select song.songIdx as pinIdx, song.userIdx, user.nickname,\n" +
                "       song.title, song.singer, song.album, song.albumCover,\n" +
                "       song.reason, song.hashtag,\n" +
                "       exists(select * from song_like_tbl where userIdx=?) as isLiked,\n" +
                "       song.latitude, song.longitude, song.state, song.city, song.street\n" +
                "from song_tbl as song\n" +
                "join user_tbl user on user.userIdx = song.userIdx\n" +
                "    where song.status='A'";
        int params = getPinReq.getUserIdx();

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new Pin(
                    rs.getInt("pinIdx"),
                    rs.getInt("userIdx"),
                    rs.getString("nickname"),
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

    /* 핀 공감 & 공감 취소 API */
    public PostLikeRes likeSong(int useridx, int idx) {
        String getLikeQuery = "select * from song_like_tbl where song_idx=? and user_idx=?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(getLikeQuery, useridx, idx);
        // 해당 사용자가 좋아요를 눌렀는지 확인. 
        // member_idx와 product_idx를 뽑아 rows에 저장해서 rows의 길이가 0인 경우, 즉 좋아요 누른 게시글이 없는 경우 like 테이블에 member_idx와 product_idx를 저장하자.
        if (rows.size() == 0) {
            String createProductQuery = "insert into likes (song_idx, user_idx) VALUES (?, ?)";

            Object[] createProductParams = new Object[]{useridx, idx};

            this.jdbcTemplate.update(createProductQuery, createProductParams);

            String getLastInsertIdxQuery = "select last_insert_id()";
            int lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, int.class);

            return new PostLikeRes(useridx, idx);
        } else { // 그럼 그 반대의 경우는 rows의 길이가 0이 아닌, 이미 좋아요를 누른 게시글이다. DELETE 쿼리로 좋아요 취소를 구현한다.
            String createProductQuery = "DELETE FROM song_like_tbl WHERE song_idx=? and user_idx=?";

            Object[] createProductParams = new Object[]{useridx, idx};

            this.jdbcTemplate.update(createProductQuery, createProductParams);

            String getLastInsertIdxQuery = "select last_insert_id()";
            int lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, int.class);

            return new PostLikeRes(useridx, idx);

        }

    }

    /* 댓글 작성 API */
    public int postComment(PostCommentReq postCommentReq){
        String postCommentQuery = "insert into song_comment_tbl values (null, ?, ?, ?, default, default, default) select user_idx, song_idx from song_comment_tbl where user_idx = ? and song_idx";
        Object[] postCommentParams = new Object[]{postCommentReq.getUserIdx(), postCommentReq.getSongIdx(), postCommentReq.getContent()};

        this.jdbcTemplate.update(postCommentQuery, postCommentParams);

        String lastInsertIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdx, int.class);
    }
}
