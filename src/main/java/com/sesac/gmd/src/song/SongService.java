package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.*;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sesac.gmd.config.BaseResponseStatus.*;

@Service
public class SongService {
    @Autowired
    private SongProvider songProvider;
    @Autowired
    private SongDao songDao;
    @Autowired
    private JwtService jwtService;

    public SongService(SongProvider songProvider, SongDao songDao, JwtService jwtService) {
        this.songProvider = songProvider;
        this.songDao = songDao;
        this.jwtService = jwtService;
    }

    /** API **/

    /* PIN 생성 API */
    public PostPinRes createPin(PostPinReq postPinReq) throws BaseException {
        if(songProvider.checkSong(postPinReq.getLatitude(), postPinReq.getLongitude(), postPinReq.getUserIdx(), postPinReq.getSongIdx()) == 1) {
            throw new BaseException(POST_PINS_EXISTS_SONG);
        }

        try {
            int pinIdx = songDao.createPin(postPinReq);
            return new PostPinRes(pinIdx);
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }
    
    /* 핀 공감 & 공감 취소 API */
    public String likeSong(PostLikeReq postLikeReq) throws BaseException{
        try{
            return songDao.likeSong(postLikeReq);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 댓글 작성 API */
    public int postComment(PostCommentReq postcommentReq) throws BaseException {
        try{
            int commentIdx = songDao.postComment(postcommentReq);
            return commentIdx;
        } catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
