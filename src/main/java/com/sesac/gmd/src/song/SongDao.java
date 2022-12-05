package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.PostCommentReq;
import com.sesac.gmd.src.song.model.PostLikeReq;
import com.sesac.gmd.src.song.model.PostLikeRes;
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
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostLikeRes likeSong(int useridx, int idx) {
        String getLikeQuery = "select * from song_like_tbl where song_idx=? and user_idx=?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(getLikeQuery, useridx, idx);
        //해당 사용자가 좋아요를 눌렀는지 확인. member_idx와 product_idx를 뽑아 rows에 저장해서 rows의 길이가 0인 경우, 즉 좋아요 누른 게시글이 없는 경우 like 테이블에 member_idx와 product_idx를 저장하자.
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

    public int postComment(PostCommentReq postCommentReq){
        String postCommentQuery = "insert into song_comment_tbl values (null, ?, ?, ?, default, default, default) select user_idx, song_idx from song_comment_tbl where user_idx = ? and song_idx";
        Object[] postCommentParams = new Object[]{postCommentReq.getUserIdx(), postCommentReq.getSongIdx(), postCommentReq.getContent()};

        this.jdbcTemplate.update(postCommentQuery, postCommentParams);

        String lastInsertIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdx, int.class);
    }
}