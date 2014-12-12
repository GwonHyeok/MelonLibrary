package com.hyeok.melon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.hyeok.melon.Exception.LoginFailException;

public class Melon {
    private String id;
    private String pw;
    private String idcookie;
    private String keycookie;

    /**
     * @param id 멜론 아이디
     * @param pw 멜론 비밀번호
     */
    public Melon(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public boolean Login() throws LoginFailException {
        try {
            String url_id = URLEncoder.encode(id, "UTF-8");
            String url_pw = URLEncoder.encode(pw, "UTF-8");
            String url = "https://www.melon.com/muid/login/web/login_informProcs.htm";
            String param = "memberId=" + url_id + "&memberPwd=" + url_pw;
            URL targetURL;
            targetURL = new URL(url);
            URLConnection urlConn = targetURL.openConnection();
            HttpsURLConnection request = (HttpsURLConnection) urlConn;
            request.addRequestProperty("User-Agent",
                    "Android; AS40; Android 4.4.2; 2.7.4; Nexus 5");
            request.addRequestProperty("Cookie", "PCID=;");
            request.addRequestProperty("Host", "www.melon.com");
            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);
            request.setRequestMethod("POST");
            OutputStream opstrm = request.getOutputStream();
            opstrm.write(param.getBytes());
            opstrm.flush();
            opstrm.close();
            List<String> cookies = request.getHeaderFields().get("Set-Cookie");
            if (cookies == null) throw new LoginFailException("Login Fail");
            String tmp = cookies.toString();
            if (tmp != null && !tmp.equals("")) {
                this.idcookie = tmp.split(";")[2].substring(18);
                this.keycookie = tmp.split(";")[4].substring(8).substring(11);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setKeyCookie(String keycookie) {
        this.keycookie = keycookie;
    }

    public void setIdCookie(String idcookie) {
        this.idcookie = idcookie;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setPW(String pw) {
        this.pw = pw;
    }

    public String getIdCookie() {
        return this.idcookie;
    }

    public String getKeyCookie() {
        return this.keycookie;
    }

    public String getID() {
        return this.id;
    }

    public String getPW() {
        return this.pw;
    }
}
