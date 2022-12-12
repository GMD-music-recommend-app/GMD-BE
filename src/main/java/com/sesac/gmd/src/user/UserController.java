package com.sesac.gmd.src.user;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.user.model.DeletePinReq;
import com.sesac.gmd.src.user.model.GetMyPinsRes;
import com.sesac.gmd.src.user.model.GetCommentRes;
import com.sesac.gmd.src.user.model.*;
import com.sesac.gmd.utils.JwtService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation("임시 회원가입")
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

    /* 유저 정보 반환 API */
    @ApiOperation("유저 정보 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/info/{userIdx}")
    public BaseResponse<User> getUser(@PathVariable int userIdx) {
        try {
            // 유효한 JWT인지 확인
            int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

            if(userIdx != userIdxByJwt){
                // userIdx와 접근한 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            User user = userProvider.getUser(userIdx);
            return new BaseResponse<>(user);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 닉네임 변경 API */
    @ApiOperation("닉네임 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
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
    @ApiOperation("관심 지역 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
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

    /* 내가 생성한 핀 리스트 반환 API */
    @ApiOperation("내가 생성한 핀 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @GetMapping("/pin/{userIdx}")
    public BaseResponse<List<GetMyPinsRes>> getMyPins(@PathVariable int userIdx) {
        try{
            // 유효한 JWT인지 확인
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(userIdx != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetMyPinsRes> getMyPinsRes = userProvider.getMyPins(userIdx);
            return new BaseResponse<>(getMyPinsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 핀 삭제 API  */
    @ApiOperation("핀 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @PatchMapping("/pin/status/{pinIdx}")
    public BaseResponse<String> deletePin(@PathVariable int pinIdx, @RequestBody DeletePinReq deletePinReq) {
        try{
            // 유효한 JWT인지 확인
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(deletePinReq.getUserIdx() != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            String result = userService.deletePin(pinIdx);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 내가 단 댓글 반환 API */
    @ApiOperation("내가 단 댓글 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/comment/{userIdx}")
    public BaseResponse<List<GetCommentRes>> getComment(@PathVariable int userIdx){
        try{
            // 유효한 JWT인지 확인
            int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

            if(userIdx != userIdxByJwt){
                // userIdx와 접근한 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetCommentRes> getCommentRes = userProvider.getComment(userIdx);
            return new BaseResponse<>(getCommentRes);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 내가 단 댓글 삭제 API */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @PatchMapping("/comment/status/{commentIdx}")
    public BaseResponse<String> deleteComment(@PathVariable int commentIdx, @RequestBody DeletePinReq deleteCommentReq){
        try{
            // 유효한 JWT인지 확인
            int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

            if(deleteCommentReq.getUserIdx() != userIdxByJwt){
                // userIdx와 접근한 유저가 같은지 확인
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            String result = userService.deleteComment(commentIdx);
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
