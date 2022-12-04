package com.sesac.gmd.src.song;

import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.config.BaseResponse;
import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.PostCommentReq;
import com.sesac.gmd.src.song.model.PostCommentRes;
import com.sesac.gmd.src.song.model.PostLikeReq;
import com.sesac.gmd.src.song.model.PostLikeRes;
import com.sesac.gmd.src.user.model.UserRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/song")
@RestController
public class SongController {
    @Autowired
    private SongProvider songProvider;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SongService songService;

    @Autowired
    public SongController(SongProvider songProvider, SongService songService,JwtService jwtService){
        this.songProvider = songProvider;
        this.songService = songService;
        this.jwtService = jwtService;
    }


    //PIN곡 공감하기
    @ResponseBody
    @PostMapping("/liking/{userIdx}")
    public BaseResponse<PostLikeRes> likeSong(@AuthenticationPrincipal UserRes userRes, @PathVariable int userIdx){
        try{
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(userRes.getUserIdx() != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PostLikeRes postLikeRes = songService.likeSong(userRes.getUserIdx(), userIdx);
            return new BaseResponse<>(postLikeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //댓글 작성하기
    @ResponseBody
    @PostMapping("/comment/{userIdx}")
    public BaseResponse<PostCommentRes> postComment(@RequestBody PostCommentReq postCommentReq, @PathVariable int userIdx){
        try{
            int userIdxJwt = jwtService.getUserIdx();
            //useridx로 접근한 유저가 같은 유저인지 확인하기
            if(postCommentReq.getUserIdx() != userIdxJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            //userid 같으면 댓글 생성
            int commentidx = songService.postComment(postCommentReq);
            return new BaseResponse<>(new PostCommentRes(userIdx, commentidx));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
