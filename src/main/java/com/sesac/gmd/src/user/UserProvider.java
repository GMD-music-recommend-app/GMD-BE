package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.src.user.model.GetMyPinsRes;
import com.sesac.gmd.src.user.model.GetCommentRes;
import com.sesac.gmd.src.user.model.User;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /* 유저 정보 반환 API */
    public User getUser(int userIdx) throws BaseException {
        try {
            return userDao.getUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 내가 생성한 핀 리스트 반환 API */
    public List<GetMyPinsRes> getMyPins(int userIdx) throws BaseException {
        try {
            List<GetMyPinsRes> getMyPinsRes = userDao.getMyPins(userIdx);
            return getMyPinsRes;
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }

    /* 유저 댓글 반환 API */
    public List<GetCommentRes> getComment(int userIdx) throws BaseException{
        try{
            return userDao.getComment(userIdx);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /** 유효성 검사 **/

    /* 이메일 중복 검사 */
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 닉네임 중복 검사 */
    public int checkNickname(String nickname) throws BaseException {
        try {
            return userDao.checkNickname(nickname);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
