package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sesac.gmd.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class UserProvider {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtService jwtService;

    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    /** API **/

    /** 유효성 검사 **/

    // 해당 전화번호가 이미 있는지 검사
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
