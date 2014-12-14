package com.hyeok.test;

import java.io.IOException;

import com.hyeok.melon.Melon;
import com.hyeok.melon.MelonSearch;
import com.hyeok.melon.Exception.LoginFailException;
import com.hyeok.melon.MelonSong;
import com.hyeok.melon.SearchData;

public class Main {
    private static String id = "kh4975";
    private static String pw = "";

    public static void main(String[] args) throws IOException {
        MelonSearch search = MelonSearch.getinstance();
        search.setOrder(MelonSearch.POPULAR);
        search.setMaxSize(100);
        search.setMinSize(1);
        search.setSongName("괜찮아");
        search.Search();
        search.getSIDList();
        search.getAlbumartList();
        System.out.println(search.getAllData().size());
//        for (SearchData DATA : search.getAllData()) {
//            System.out.println(DATA.getSID() + ":" + DATA.getSongName() + ":" + DATA.getAlbumart() + ":" + DATA.getSinger());
//        }

//		search.Top100(MelonSearch.TOP100WEEKLY);
//		System.out.println(search.getAllData().size());

        for (SearchData DATA : search.getAllData()) {
            System.out.println(DATA.getSID() + ":" + DATA.getSongName() + ":" + DATA.getAlbumart() + ":" + DATA.getSinger());
        }
        /*
         * Melon Class는
		 * Melon(아이디, 페스워드); 로 초기화.
		 * melon.Login();  Keycookie와 IdCookie생성.
		 * 아이디와 비밀번호를 따로 지정 할 수 있음.
		 * melon.setID(String ID);
		 * melon.setPW(String PW);
		 * melon.Login();
		 * 이과정을 걸치면 다시 Keycookie와 IdCookie를 다시 설정.
		 */
        try {
            Melon melon = new Melon(id, pw);
            melon.Login();
            System.out.println(melon.getKeyCookie());
            melon.setKeyCookie(String.valueOf(Integer.parseInt(melon.getKeyCookie()) + 100));

		/*
         * MelonSong Class에는 Melon Class Object를 파라미터로 한다.
		 * MelonSong Class에서는 음악 ID를 입력 getSongData(String SONGID); 와 같은 형식으로
		 * 음악 정보를 받아와서 getMusicURL(); getSongName(); getSingerName(); getBitrate(); getAlbumID(); getLyricsURL();
		 * 를 이용해 그 음악 ID의 음악 정보를 볼 수 있다.
		 */
            MelonSong melonsong = new MelonSong(melon);
            melonsong.getSongData(search.getSIDList().get(1));
            System.out.println("AlbumID : " + melonsong.getAlbumID());
            System.out.println("Bitrate : " + melonsong.getBitrate());
            System.out.println("AlbumName : " + melonsong.getAlbumName());
            System.out.println("Lyrics : " + melonsong.getLyricsURL());
            System.out.println("Year : " + melonsong.getYear().substring(0, 4));
            System.out.println("Music : " + melonsong.getMusicURL());
            System.out.println("Singer : " + melonsong.getSingerName());
            System.out.println("Title : " + melonsong.getSongName());
            System.out.println("AlbumArt : " + melonsong.getAlbumArtURL());
            System.out.println("MusicVideo : " + melonsong.getMvURL());
            System.out.println("StringLyrics : " + melonsong.getStringLyrics());
            System.out.println("BanSong : " + melonsong.isBanSong());
        } catch (LoginFailException e) {
            e.printStackTrace();
        }

    }
}
