package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.src.song.model.PostPinReq;
import com.sesac.gmd.src.song.model.PostPinRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sesac.gmd.config.BaseResponseStatus.DATABASE_ERROR;

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
        try {
            System.out.println(postPinReq);
            int pinIdx = songDao.createPin(postPinReq);

            return new PostPinRes(pinIdx);
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }
}
