package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
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
    public String patchNickname(PatchNicknameReq patchNicknameReq) throws BaseException {
        // 닉네임 중복 체크
        if (userProvider.checkNickname(patchNicknameReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        try {
            return userDao.patchNickname(patchNicknameReq);

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

