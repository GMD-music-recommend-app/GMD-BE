package com.sesac.gmd.src.user;

import com.sesac.gmd.src.user.model.PatchLocationReq;
import com.sesac.gmd.src.user.model.PatchNicknameReq;
import com.sesac.gmd.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
        String query = "insert into user_tbl values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default)";
        Object[] params = new Object[] {
                postUserReq.getNickname(), postUserReq.getGender(), postUserReq.getAge(), postUserReq.getEmail(),
                postUserReq.getState(), postUserReq.getCity(), postUserReq.getStreet(),
                postUserReq.getPushId(), postUserReq.getOauthId() };

        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
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
        String query = "update user_tbl set state=?, city=?, street=? where userIdx=?";
        Object[] params = new Object[] {
                patchLocationReq.getState(), patchLocationReq.getCity(), patchLocationReq.getStreet(),
                userIdx };

        this.jdbcTemplate.update(query, params);

        return "관심 지역이 성공적으로 변경되었습니다.";
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
