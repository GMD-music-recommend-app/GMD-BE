package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.src.song.model.*;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sesac.gmd.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SongProvider {
    @Autowired
    private SongDao songDao;
    @Autowired
    private JwtService jwtService;

    public SongProvider(SongDao songDao, JwtService jwtService) {
        this.songDao = songDao;
        this.jwtService = jwtService;
    }

    /** API **/

    /* 핀 반환 API */
    public Pin getPin(GetPinReq getPinReq, int pinIdx) throws BaseException {
        try {
            List<Comment> comments = songDao.getComments(pinIdx);
            return songDao.getPin(getPinReq, comments, pinIdx);
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }

    /* 핀 리스트 반환 API */
    public List<GetPinsRes> getPins(GetPinsReq getPinsReq) throws BaseException {
        try {
            return songDao.getPins(getPinsReq);
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }

    /** 유효성 검사 **/

}
