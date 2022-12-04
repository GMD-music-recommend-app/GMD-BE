package com.sesac.gmd.src.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongProvider {
    @Autowired
    private final SongDao songDao;


    public SongProvider(SongDao songDao) {
        this.songDao = songDao;
    }
}
