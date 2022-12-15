package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.src.user.model.PatchLocationReq;
import com.sesac.gmd.src.user.model.PatchNicknameReq;
import com.sesac.gmd.src.user.model.PostUserReq;
import com.sesac.gmd.src.user.model.PostUserRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sesac.gmd.config.BaseResponseStatus.*;

@Service
public class UserService {
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtService jwtService;

    public UserService(UserProvider userProvider, UserDao userDao, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    /* 회원 가입 API */
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 체크
        if (userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        // 닉네임 중복 체크
        if (userProvider.checkNickname(postUserReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        try {
            int userIdx = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userIdx);

            return new PostUserRes(userIdx, jwt);

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 닉네임 변경 API */
    public String patchNickname(PatchNicknameReq patchNicknameReq, int userIdx) throws BaseException {
        // 닉네임 중복 체크
        if (userProvider.checkNickname(patchNicknameReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        try {
            return userDao.patchNickname(patchNicknameReq, userIdx);

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 관심 지역 변경 API */
    public String patchLocation(PatchLocationReq patchLocationReq, int userIdx) throws BaseException {
        try {
            return userDao.patchLocation(patchLocationReq, userIdx);

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 핀 삭제 API */
    public String deletePin(int pinIdx) throws BaseException {
        try {

            return userDao.deletePin(pinIdx);
        } catch(Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }

    /* 내가 단 댓글 삭제 API */
    public String deleteComment(int commentIdx) throws BaseException {
        try{
            return userDao.deleteComment(commentIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 푸시 알림 활성화 API */
    public String activeIsPushed(int userIdx) throws BaseException {
        try {
            return userDao.activeIsPushed(userIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 푸시 알림 비활성화 API */
    public String patchIsPushed(int userIdx) throws BaseException {
        try {
            return userDao.patchIsPushed(userIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

