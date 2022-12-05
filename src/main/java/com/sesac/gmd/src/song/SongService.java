package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.PostCommentReq;
import com.sesac.gmd.src.song.model.PostLikeReq;
import com.sesac.gmd.src.song.model.PostLikeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    @Autowired
    private SongDao songDao;

    @Autowired
    public SongService(SongDao songDao){
        this.songDao = songDao;
    }

    public PostLikeRes likeSong(int useridx, int idx){
        return songDao.likeSong(useridx, idx);
    }

    public int postComment(PostCommentReq postcommentReq) throws BaseException {
        try{
            int commentIdx = songDao.postComment(postcommentReq);
            return commentIdx;
        } catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
