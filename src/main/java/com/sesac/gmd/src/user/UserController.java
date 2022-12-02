package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.user.model.PostUserReq;
import com.sesac.gmd.src.user.model.PostUserRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sesac.gmd.config.BaseResponseStatus.POST_USERS_INVALID_EMAIL;
import static com.sesac.gmd.config.BaseResponseStatus.SUCCESS;
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
        BaseResponseStatus validation = userValidation(postUserReq);

        if(validation == SUCCESS) {
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
            return new BaseResponse<>(validation);
        }
    }
}
