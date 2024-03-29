package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Repository
public class SongDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** API **/

    /* 핀 생성 API */
    public int createPin(PostPinReq postPinReq) {
        String query = "insert into pin_tbl values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default)";
        Object[] params = new Object[] {
                postPinReq.getUserIdx(), postPinReq.getSongIdx(),
                postPinReq.getSongTitle(), postPinReq.getArtist(),
                postPinReq.getAlbumTitle(), postPinReq.getAlbumImage(),
                postPinReq.getReason(), postPinReq.getHashtag(),
                postPinReq.getLatitude(), postPinReq.getLongitude(),
                postPinReq.getState(), postPinReq.getCity(), postPinReq.getStreet() };

        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    /* 핀 반환 API */
    public Pin getPin(int userIdx, List<Comment> comments, int pinIdx) {
        String query = "select pin.pinIdx, pin.userIdx, user.nickname,\n" +
                "       pin.songIdx, pin.songTitle, pin.artist, pin.albumTitle, pin.albumImage,\n" +
                "       pin.reason, pin.hashtag,\n" +
                "       exists(select * from pin_like_tbl where userIdx=?) as isLiked,\n" +
                "       if(pin.userIdx=?, 1, 0) as isMade, \n" +
                "       pin.latitude, pin.longitude, pin.state, pin.city, pin.street \n" +
                "from pin_tbl as pin\n" +
            "        join user_tbl as user\n " +
                "       on user.userIdx = pin.userIdx\n" +
            "    where pin.pinIdx=? and pin.status='A'";
        Object[] params = new Object[] {
                userIdx, userIdx, pinIdx};

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new Pin(
                    rs.getInt("pinIdx"),
                    rs.getInt("userIdx"),
                    rs.getString("nickname"),
                    rs.getInt("songIdx"),
                    rs.getString("songTitle"),
                    rs.getString("artist"),
                    rs.getString("albumTitle"),
                    rs.getString("albumImage"),
                    rs.getString("reason"),
                    rs.getString("hashtag"),
                    rs.getString("isLiked"),
                    rs.getString("isMade"),
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
        String query = "select comment.pinIdx, " +
                "   comment.userIdx, user.nickname, " +
                "   comment.content " +
                "from pin_comment_tbl as comment\n" +
                "    join user_tbl as user\n" +
                "        on user.userIdx = comment.userIdx\n" +
                "        and user.status='A'\n" +
                "where pinIdx=? and comment.status='A'";

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
        String query = "select pinIdx,\n" +
                "    ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) as distance,\n" +
                "    latitude, longitude, state, city, street, \n" +
                "    albumImage \n" +
                "from pin_tbl \n" +
                "    where ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) <= ? and status='A'";
        Object[] params = new Object[]{
                getPinsReq.getLongitude(), getPinsReq.getLatitude(),
                getPinsReq.getLongitude(), getPinsReq.getLatitude(),
                getPinsReq.getRadius()
        };

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetPinsRes(
                        rs.getInt("pinIdx"),
                        rs.getDouble("distance"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("state"),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("albumImage")
                ), params);
    }

    /* 핀 공감 & 공감 취소 API */
    public String likeSong(PostLikeReq postLikeReq) {
        String getLikeQuery = "select * from pin_like_tbl where userIdx=? and pinIdx=?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(getLikeQuery, postLikeReq.getUserIdx(), postLikeReq.getPinIdx());
        // 해당 사용자가 좋아요를 눌렀는지 확인.
        // userIdx와 pinIdx를 뽑아 rows에 저장해서 rows의 길이가 0인 경우, 즉 좋아요 누른 pin이 없는 경우 pin_like_tbl 테이블에 userIdx와 pinIdx를 저장하자.
        if (rows.size() == 0) {
            String createProductQuery = "insert into pin_like_tbl (userIdx, pinIdx) VALUES (?, ?)";

            Object[] createProductParams = new Object[]{postLikeReq.getUserIdx(), postLikeReq.getPinIdx()};

            this.jdbcTemplate.update(createProductQuery, createProductParams);

            String getLastInsertIdxQuery = "select last_insert_id()";
            int lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, int.class);

            return "핀 공감을 설정하였습니다.";
        } else { // 그럼 그 반대의 경우는 rows의 길이가 0이 아닌, 이미 좋아요를 누른 게시글이다. DELETE 쿼리로 좋아요 취소를 구현한다.
            String createProductQuery = "DELETE FROM pin_like_tbl WHERE userIdx=? and pinIdx=?";

            Object[] createProductParams = new Object[]{postLikeReq.getUserIdx(), postLikeReq.getPinIdx()};

            this.jdbcTemplate.update(createProductQuery, createProductParams);

            return "핀 공감을 취소하였습니다.";

        }
    }

    /* 댓글 작성 API */
    public int postComment(PostCommentReq postCommentReq){
        String postCommentQuery = "insert into pin_comment_tbl values (null, ?, ?, ?, default, default, default)";
        Object[] postCommentParams = new Object[]{postCommentReq.getUserIdx(), postCommentReq.getPinIdx(), postCommentReq.getContent()};

        this.jdbcTemplate.update(postCommentQuery, postCommentParams);

        String lastInsertIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdx, int.class);
    }

    /** 유효성 검사 **/

    /* 노래 중복 검사 */
    public int checkSong(double latitude, double longitude, int userIdx, int songIdx) {
        DecimalFormat format = new DecimalFormat("0.000");

        String query = "select exists(select * from pin_tbl " +
                "           where userIdx=? and songIdx=?\n" +
                "                and round(latitude, 3)=?\n" +
                "                and round(longitude, 3)=?)";
        Object[] params = new Object[] {
                userIdx, songIdx, format.format(latitude), format.format(longitude) };

        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }
}
