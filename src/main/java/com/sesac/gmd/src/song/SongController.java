package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.*;
import com.sesac.gmd.src.user.model.UserRes;
import com.sesac.gmd.utils.JwtService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

import static com.sesac.gmd.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.sesac.gmd.config.BaseResponseStatus.SUCCESS;
import static com.sesac.gmd.utils.Validation.locationValidation;
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

    /* 핀 생성 API */
    @ApiOperation("핀 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
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
            BaseResponseStatus status = pinValidation(postPinReq);

            if(status == SUCCESS) {
                PostPinRes postPinRes = songService.createPin(postPinReq);
                return new BaseResponse<>(postPinRes);
            } else {
                // 입력되지 않은 게 있으면
                return new BaseResponse<>(status);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 핀 반환 API */
    @ApiOperation("핀 정보 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @GetMapping("/info/{pinIdx}")
    public BaseResponse<Pin> getPin(@PathVariable int pinIdx, @RequestParam int userIdx) {
        try {
            if(userIdx == 0) {
                Pin getPinRes = songProvider.getPin(userIdx, pinIdx);
                return new BaseResponse<>(getPinRes);
            } else {
                // 유효한 JWT인지 확인
                int userIdxByJwt = jwtService.getUserIdx();  // JWT에서 userIdx 추출

                if(userIdx != userIdxByJwt){
                    // userIdx와 접근한 유저가 같은지 확인
                    return new BaseResponse<>(INVALID_USER_JWT);
                }

                Pin getPinRes = songProvider.getPin(userIdx, pinIdx);
                return new BaseResponse<>(getPinRes);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 핀 리스트 반환 API */
    @ApiOperation("반경 내 핀 리스트 반환")
    @GetMapping("/info-list")
    public BaseResponse<List<GetPinsRes>> getPins(@RequestParam int radius, @RequestParam double latitude, @RequestParam double longitude) {
        try {
            BaseResponseStatus status = locationValidation(latitude, longitude);
            GetPinsReq getPinsReq = new GetPinsReq(radius, latitude, longitude);

            if(status == SUCCESS) {
                List<GetPinsRes> getPinsRes = songProvider.getPins(getPinsReq);
                return new BaseResponse<>(getPinsRes);
            } else {
                return new BaseResponse<>(status);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }    
    
    /* 핀 공감 & 공감 취소 API */
    @ApiOperation("핀 공감 & 공감 취소")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @PostMapping("/liking/{userIdx}")
    public BaseResponse<PostLikeRes> likeSong(@PathVariable int userIdx, PostLikeReq postLikeReq){
        try{
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(postLikeReq.getUserIdx() != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }
            //핀 아이디 갖고오기
            int pinIdx = postLikeReq.getPinIdx();
            //유저 아이디랑 핀 아이디 넘겨주기
            PostLikeRes postLikeRes = songService.likeSong(userIdx, pinIdx);
            return new BaseResponse<>(postLikeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
    /* 댓글 작성 API */
    @ApiOperation("핀 댓글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @PostMapping("/comment/{userIdx}")
    public BaseResponse<PostCommentRes> postComment(PostCommentReq postCommentReq, @PathVariable int userIdx){
        try{
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(postCommentReq.getUserIdx() != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            //userid 같으면 댓글 생성
            int commentIdx = songService.postComment(postCommentReq);
            return new BaseResponse<>(new PostCommentRes(userIdx, commentIdx));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}