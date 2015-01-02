package com.hyeok.melon;

import java.io.IOException;

import com.hyeok.melon.Exception.GetSongDataException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MelonSong {
    private String SongData[];
    private String Year, Genre, Albumname;
    private String SID;
    private String keyCookie;
    private Jsonparse parser = new Jsonparse();

    public MelonSong(Melon melon) {
        this.keyCookie = melon.getKeyCookie();
    }

    public MelonSong(String keyCookie) {
        this.keyCookie = keyCookie;
    }

    public void getSongData(String SID) throws GetSongDataException {
        this.SID = SID;
        String data = "cpid=AS40&command=stream&act=getPath&ctype=1&cid=#MCID&metatype=HD&ukey=";
        data = data + keyCookie;
        data = data.replace("#MCID", SID);
        data = "http://m.melon.com/cds/delivery/smartapp/mediadelivery_androidDelivery.json"
                + "?" + data;
        SongData = parser.JsonSongInfo(data);
        // 앨범 정보 파싱..
        try {
//			songinfoelements.select("dd").get(2) 발매사
//			songinfoelements.select("dd").get(3).ownText() 기획사 
            Document doc = Jsoup.connect("http://www.melon.com/album/detail.htm?albumId=" + SongData[5]).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();
            Elements elements = doc.getElementsByClass("wrap_album_info");
            Elements albumnameelements = elements.get(0).getElementsByClass("albumname");
            Elements songinfoelements = elements.get(0).getElementsByClass("song_info");
            this.Year = songinfoelements.select("dd").get(1).ownText();
            this.Genre = songinfoelements.select("dd").get(4).ownText();
            this.Albumname = albumnameelements.get(0).ownText();
        } catch (IOException e) {
            throw new GetSongDataException("Error When Song Data parsing\n" + e.getMessage());
        }
    }

    public String getSongName() {
        return SongData[1];
    }

    public String getSingerName() {
        return SongData[2];
    }

    public String getBitrate() {
        return SongData[3];
    }

    public String getAlbumID() {
        return SongData[5];
    }

    public String getMusicURL() {
        return SongData[0];
    }

    public String getLyricsURL() {
        return SongData[4];
    }

    public String getYear() {
        return Year;
    }

    public String getGenre() {
        return Genre;
    }

    public String getAlbumName() {
        return Albumname;
    }

    public boolean isBanSong() {
        String url = "cpid=AS40&command=stream&act=getPath&ctype=1&cid=#MCID&metatype=HD&ukey="
                + keyCookie;
        url = url.replace("#MCID", SID);
        url = "http://m.melon.com/cds/delivery/smartapp/mediadelivery_androidDelivery.json"
                + "?" + url;
        String error[] = parser.melonerror(url, "RESULT", "PERIOD");
        return error[0].equals("-1007");
    }

    public String getStringLyrics() {
        String url = "http://www.melon.com/song/detail.htm?songId=" + SID;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements LyricsElements = doc.getElementsByClass("lyric");
            return LyricsElements.get(0).html().replaceAll("<br />", "").split("> ")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHtmlLyrics() {
        String url = "http://www.melon.com/song/detail.htm?songId=" + SID;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements LyricsElements = doc.getElementsByClass("lyric");
            return "<html>" + LyricsElements.get(0).html().replaceAll("<br />", "<br>").split("> ")[1] + "</html>";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMvURL() {
        String BASEURL = "http://m.melon.com/cds/musicvideo/android2/musicvideofwd_stream.json?songId="
                + "#SONGID#";
        BASEURL = BASEURL.replace("#SONGID#", SID);
        String mvid = parser.urljsonparse(BASEURL, "mvId");
        @SuppressWarnings("unused")
        String isadult = parser.urljsonparse(BASEURL, "adultFlg");
        if (mvid.equals("")) {
            // 뮤직비디오가 없음.
        } else {
            String MVURL = "http://m.melon.com/cds/delivery/smartapp/mediadelivery_androidDelivery.json?cpid=AS40&command=stream&act=getPath&ukey="
                    + keyCookie
                    + "&ctype=21&cid="
                    + mvid
                    + "&metatype=MP4&bitrate=3072";
            MVURL = parser.urljsonparse(MVURL, "PATH");
            return MVURL;
        }
        return null;
    }

    public String getAlbumArtURL() {
        String Albumarturl = null;
        int length = getAlbumID().length();
        switch (length) {
            case 1:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/000/00/00" + getAlbumID().substring(0, 1) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 2:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/000/00/0" + getAlbumID().substring(0, 2) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 3:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/000/00/" + getAlbumID().substring(0, 3) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 4:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/000" + "/0" + getAlbumID().substring(0, 1) + "/" + getAlbumID().substring(1, 4) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 5:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/000" + "/" + getAlbumID().substring(0, 2) + "/" + getAlbumID().substring(2, 5) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 6:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/00" + getAlbumID().substring(0, 1) + "/" + getAlbumID().substring(1, 3) + "/" + getAlbumID().substring(3, 6) + "/" + getAlbumID() + "_500.jpg";
                break;
            case 7:
                Albumarturl = "http://image.melon.co.kr/cm/album/images/0" + getAlbumID().substring(0, 2) + "/" + getAlbumID().substring(2, 4) + "/" + getAlbumID().substring(4, 7) + "/" + getAlbumID() + "_500.jpg";
                break;
        }
        return Albumarturl;
    }
}