package com.backend.NawaBari.service;

import com.backend.NawaBari.converter.MemberConverter;
import com.backend.NawaBari.converter.TokenConverter;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.repository.MemberRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final TokenConverter tokenConverter;

    public String getKakaoAccessToken (String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=27eb3015ee913717948781cb023560f3"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8080/login"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            Gson gson = new Gson();

            JsonElement element = gson.fromJson(result, JsonElement.class);

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

    public String getNewAccessToken(String refresh_token) {
        String newAccessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // request body에 파라미터 설정
            String parameter = "grant_type=refresh_token"
                    + "&client_id=" + "your_client_id"
                    + "&refresh_token=" + refresh_token;

            // request body에 파라미터 셋팅
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(parameter.getBytes());
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            StringBuffer response = new StringBuffer();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            System.out.println("response body : " + response.toString());

            // Gson 라이브러리를 사용하여 JSON 형태의 response body 파싱
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            newAccessToken = jsonObject.get("access_token").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return newAccessToken;
    }
    public HashMap<String, Object> getUserInfo (String access_Token) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            Gson gson = new Gson();
            JsonElement element = gson.fromJson(result, JsonElement.class);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String kakao_id = element.getAsJsonObject().get("id").getAsString();

            if (kakao_id == null) {
                throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다");
            }


            String profile_nickname = properties.has("nickname") ? properties.get("nickname").getAsString() : null;
            String profileImage = properties.has("profile_image") ? properties.get("profile_image").getAsString() : null;
            String age = kakao_account.has("age_range") ? kakao_account.get("age_range").getAsString() : null;
            String gender = kakao_account.has("gender") ? kakao_account.get("gender").getAsString() : null;

            userInfo.put("kakao_id", kakao_id);
            userInfo.put("profile_nickname", profile_nickname);
            userInfo.put("profile_image", profileImage);
            userInfo.put("age_range", age);
            userInfo.put("gender", gender);

            /*Member member = memberRepository.findByKakao_Id(kakao_id);

            if (member == null) {
                Member newMember = Member.builder()
                        .kakao_id(kakao_id)
                        .profile_nickname(profile_nickname)
                        .profile_image(profileImage)
                        .age(age)
                        .gender(gender)
                        .build();
                memberRepository.save(newMember);

            } else {
                member.setProfile_nickname(profile_nickname);
                member.setProfile_image(profileImage);
                member.setAge(age);
                member.setGender(gender);
                memberRepository.save(member);
            }*/

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userInfo;
    }


    public void kakaoLogout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

/*    public void registerMember(String kakao_id, String profile_nickname, String profile_image, String gender, String age) {
        MemberDTO memberDTO = MemberDTO.builder()
                .kakao_id(kakao_id)
                .profile_nickname(profile_nickname)
                .profile_image(profile_image)
                .gender(gender)
                .age(age)
                .build();

        memberRepository.save(memberConverter.toEntity(memberDTO));
    }

    public MemberDTO findByKakaoId(String kakaoId) {
        memberRepository.findByKakao_Id(kakaoId);
        if (optionalMember.isPresent()) {
            return memberConverter.toDTO(optionalMember.get());
        } else {
            return null;
        }
    }*/

}

