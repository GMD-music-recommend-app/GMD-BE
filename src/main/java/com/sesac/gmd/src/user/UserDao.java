package com.sesac.gmd.src.user;

import com.sesac.gmd.src.user.model.GetMyPinsRes;
import com.sesac.gmd.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** API **/

    /* 회원 가입 API */
    public int createUser(PostUserReq postUserReq) {
        String query = "insert into user_tbl values(null, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default)";
        Object[] params = new Object[] {
                postUserReq.getNickname(), postUserReq.getGender(), postUserReq.getAge(), postUserReq.getEmail(),
                postUserReq.getState(), postUserReq.getCity(),
                postUserReq.getPushId(), postUserReq.getOauthId() };

        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    /* 유저 정보 반환 API */
    public User getUser(int userIdx) {
        String query = "select userIdx, nickname from user_tbl where userIdx=?";

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("nickname")
                ), userIdx);
    }

    /* 닉네임 변경 API */
    public String patchNickname(PatchNicknameReq patchNicknameReq, int userIdx) {
        String query = "update user_tbl set nickname=? where userIdx=?";
        Object[] params = new Object[] {
                patchNicknameReq.getNickname(), userIdx};

        this.jdbcTemplate.update(query, params);

        return "닉네임이 성공적으로 변경되었습니다.";
    }

    /* 관심 지역 변경 API */
    public String patchLocation(PatchLocationReq patchLocationReq, int userIdx) {
        String query = "update user_tbl set state=?, city=? where userIdx=?";
        Object[] params = new Object[] {
                patchLocationReq.getState(), patchLocationReq.getCity(),
                userIdx };

        this.jdbcTemplate.update(query, params);

        return "관심 지역이 성공적으로 변경되었습니다.";
    }

    /* 내가 생성한 핀 리스트 반환 API */
    public List<GetMyPinsRes> getMyPins(int userIdx) {
        String query = "select pinIdx, userIdx, \n" +
                "   title, artist, albumImage, \n" +
                "   state, city \n" +
                "from pin_tbl \n" +
                "   where userIdx=? and status='A'";

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetMyPinsRes(
                        rs.getInt("pinIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("albumImage"),
                        rs.getString("state"),
                        rs.getString("city")
                ), userIdx);
    }

    /* 핀 삭제 API */
    public String deletePin(int pinIdx) {
        String query = "update pin_tbl set status='I' where pinIdx=?";

        this.jdbcTemplate.update(query, pinIdx);
        return "성공적으로 삭제되었습니다";
    }

    /* 댓글 리스트 반환 API */
    public List<GetCommentRes> getComment(int userIdx){
        String query = "select pct.commentIdx, pct.content, pct.userIdx, pct.pinIdx, pt.title, pt.singer, pt.album, pt.state, pt.city from pin_comment_tbl as pct left join pin_tbl as pt on pct.userIdx = pt.userIdx where pct.userIdx = ?";

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("pinIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("album"),
                        rs.getString("title"),
                        rs.getString("singer"),
                        rs.getString("content"),
                        rs.getString("state"),
                        rs.getString("city")
                ), userIdx);
    }

    /* 내가 단 댓글 삭제 API */
    public String deleteComment(int commentIdx){
        String query = "update pin_comment_tbl set status='I' where commentIdx=?";

        this.jdbcTemplate.update(query, commentIdx);
        return "댓글이 성공적으로 삭제되었습니다.";
    }

    /** 유효성 검사 **/

    /* 중복 이메일 검사 */
    public int checkEmail(String email) {
        String query = "select exists(select * from user_tbl where email = ?)";

        return this.jdbcTemplate.queryForObject(query, int.class, email);
    }

    /* 중복 닉네임 검사 */
    public int checkNickname(String nickname) {
        String query = "select exists(select * from user_tbl where nickname = ?)";

        return this.jdbcTemplate.queryForObject(query, int.class, nickname);
    }
}
