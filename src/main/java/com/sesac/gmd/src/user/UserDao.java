package com.sesac.gmd.src.user;

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
        String query = "insert into user_tbl values(null, ?, ?, ?, ?, ?, ?, ?, default, default, default)";
        Object[] params = new Object[] {
                postUserReq.getNickname(), postUserReq.getGender(), postUserReq.getAge(),
                postUserReq.getEmail(), postUserReq.getLocation(),
                postUserReq.getPushId(), postUserReq.getOauthId() };

        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    /** 유효성 검사 **/

    /* 중복 이메일 검사 */
    public int checkEmail(String email) {
        String query = "select exists(select * from user_tbl where email = ?)";

        return this.jdbcTemplate.queryForObject(query, int.class, email);
    }
}
