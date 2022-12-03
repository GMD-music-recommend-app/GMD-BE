package com.sesac.gmd.src.song;

import com.sesac.gmd.src.song.model.PostPinReq;
import com.sesac.gmd.src.song.model.PostPinRes;
import com.sesac.gmd.src.user.UserDao;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /** 유효성 검사 **/
}
