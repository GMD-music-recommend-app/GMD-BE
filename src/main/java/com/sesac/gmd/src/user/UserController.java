package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.user.model.PatchLocationReq;
import com.sesac.gmd.src.user.model.PatchNicknameReq;
import com.sesac.gmd.src.user.model.PostUserReq;
import com.sesac.gmd.src.user.model.PostUserRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.sesac.gmd.config.BaseResponseStatus.*;
import static com.sesac.gmd.utils.Validation.userValidation;
import static com.sesac.gmd.utils.ValidationRegex.isRegexEmail;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /* 회원가입 API (카카오 제외) */
    @PostMapping("/sign-up")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // NULL 값 체크
        BaseResponseStatus status = userValidation(postUserReq);

        if(status == SUCCESS) {
            // 형식 체크 : 이메일 형식 체크
            if(!isRegexEmail(postUserReq.getEmail())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            try {
                PostUserRes postUserRes = userService.createUser(postUserReq);
                return new BaseResponse<>(postUserRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }

        } else {
            // 입력되지 않은 게 있으면
            return new BaseResponse<>(status);
        }
    }

    /* 닉네임 변경 API */
    @PatchMapping("/nickname/{userIdx}")
    public BaseResponse<String> patchNickname(@RequestBody PatchNicknameReq patchNicknameReq, @PathVariable int userIdx) {
        try {
            // 닉네임이 비어있는지 확인
            if(patchNicknameReq.getNickname().isBlank()) {
                return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
            } else {
                // 유효한 JWT인지 확인
                int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

                if(userIdx != userIdxByJwt){
                    // userIdx와 접근한 유저가 같은지 확인
                    return new BaseResponse<>(INVALID_USER_JWT);
                }

                String result = userService.patchNickname(patchNicknameReq, userIdx);
                return new BaseResponse<>(result);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 관심 지역 변경 API */
    @PatchMapping("/location/{userIdx}")
    public BaseResponse<String> patchLocation(@RequestBody PatchLocationReq patchLocationReq, @PathVariable int userIdx) {
        try {
            // 유효한 JWT인지 확인
            int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

            if(userIdx != userIdxByJwt){
                // userIdx와 접근한 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = userService.patchLocation(patchLocationReq, userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
