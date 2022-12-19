package com.sesac.gmd.src.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sesac.gmd.config.BaseException;
import com.sesac.gmd.src.user.model.PatchLocationReq;
import com.sesac.gmd.src.user.model.PatchNicknameReq;
import com.sesac.gmd.src.user.model.PostUserReq;
import com.sesac.gmd.src.user.model.PostUserRes;
import com.sesac.gmd.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

    /** API **/

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

    /** 카카오 로그인 **/

    /* 카카오 A.T 받기 (프론트가 없는 경우) */
    public String getKaKaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=be43e245f4aedc78d201da5b853a1a4e"); // REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9000/users/kakao"); // 인가코드 받은 redirect_uri 입력
            sb.append("&code=").append(code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    /* Access Token을 사용하여 사용자 정보 받아오기 */
    public String createKakaoUser(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("email : " + email);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return email;
    }

}

