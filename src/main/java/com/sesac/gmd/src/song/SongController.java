package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.PostPinReq;
import com.sesac.gmd.src.song.model.PostPinRes;
import com.sesac.gmd.src.user.UserProvider;
import com.sesac.gmd.src.user.UserService;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sesac.gmd.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.sesac.gmd.config.BaseResponseStatus.SUCCESS;
import static com.sesac.gmd.utils.Validation.pinValidation;

@RestController
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private SongProvider songProvider;
    @Autowired
    private SongService songService;
    @Autowired
    private JwtService jwtService;

    public SongController(SongProvider songProvider, SongService songService, JwtService jwtService) {
        this.songProvider = songProvider;
        this.songService = songService;
        this.jwtService = jwtService;
    }

    /* 핀 추가하기 API */
    @PostMapping("")
    public BaseResponse<PostPinRes> createPin(@RequestBody PostPinReq postPinReq) {
        try {
            // 유효한 JWT인지 확인
            int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

            if(postPinReq.getUserIdx() != userIdxByJwt){
                // userIdx와 접근한 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 빈 값 확인
            BaseResponseStatus validation = pinValidation(postPinReq);

            if(validation == SUCCESS) {
                PostPinRes postPinRes = songService.createPin(postPinReq);
                return new BaseResponse<>(postPinRes);
            } else {
                // 입력되지 않은 게 있으면
                return new BaseResponse<>(validation);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    }
}

