package com.hyeok.melon;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.hyeok.melon.Exception.Top100SearchException;

public class MelonSearch {
    public static final String POPULAR = "hit";
    public static final String RECENT = "date";
    public static final String ACCURACY = "weight";
    public static final String TOP100REALTIME = "http://m.melon.com/cds/chart/android2/chartrealtime_listSong.json";
    public static final String TOP100WEEKLY = "http://m.melon.com/cds/chart/android2/chartweekly_listSong.json";
    public static final int MAX = 10000;

    private static MelonSearch instance;
    private String SongName;
    private String Order = RECENT;
    private int maxSize = 25;
    private int minSize = 1;
    private ArrayList<String> SongSIDList = new ArrayList<String>();
    private ArrayList<String> TitleList = new ArrayList<String>();
    private ArrayList<String> AlbumartList = new ArrayList<String>();
    private ArrayList<String> SingerList = new ArrayList<String>();
    private ArrayList<SearchData> SearchList = new ArrayList<SearchData>();

    private MelonSearch() {
    }

    /**
     * 음악을 검색하는 메소드.<br>
     * 이 메소드가 사용되기 전에<br>
     * setSongName(String SongName); 를 이용해<br>
     * 검색할 음악 제목을 정해줘야 한다.<br>
     * 그 후 getter메소드들을 사용해서 정보를 가져올 수 있다.
     */
    public void Search() {
        ClearArrayList();
        String name = null;
        try {
            name = URLEncoder.encode(getSongName(), "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            Document doc = Jsoup.connect(
                    "http://m.melon.com/cds/search/android2/searchsong_list.htm?startIndex="
                            + minSize + "&pageSize=" + maxSize + "&sort=" + Order
                            + "&q=" + name).get();
            Elements song = doc.select("img ");
            Elements div = doc.select("div");
            Elements li = doc.select("li");
            Scanner sc = new Scanner(song.toString());
            Scanner scdiv = new Scanner(div.toString());
            Scanner scli = new Scanner(li.toString());
            songsid(scli);
            albumart(sc);
            songtitle(scdiv);
            singer(new Scanner(div.toString()));
            CombineData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 멜론 실시간 TOP100을 검색하는 메소드.<br>
     * 그 후 getter메소드들을 사용해서 정보를 가져올 수 있다.<br>
     * MelonSearch.TOP100REALTIME : 실시간 차트<br>
     * MelonSearch.TOP100WEEKLY : 주간 차트<br>
     *
     * @param Top100Sort
     * @throws Top100SearchException
     */
    public void Top100(String Top100Sort) throws IOException {
        if (!Top100Sort.equals(TOP100REALTIME) && !Top100Sort.equals(TOP100WEEKLY)) {
            try {
                throw new Top100SearchException("You Must use TOP100REALTIME or TOP100WEEKLY");
            } catch (Top100SearchException e) {
                e.printStackTrace();
                return;
            }
        }
        ClearArrayList();
        JSONParser parser = new JSONParser();
        try {
            URL url = new URL(Top100Sort);
            url.openStream();
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            String out = scanner.useDelimiter("\\A").next();
            Object obj = parser.parse(out);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("CONTENTS");
            for (int i = 0; i < 100; i++) {
                JSONObject j = (JSONObject) array.get(i);
                String TOP100 = (String) j.get("SONGID");
                String TOP100name = (String) j.get("SONGTITLE");
                String TOP100artist = (String) j.get("ARTISTNAME");
                String TOP100Albumart = (String) j.get("ALBUMIMGPATH");
                SongSIDList.add(TOP100);
                TitleList.add(TOP100name);
                SingerList.add(TOP100artist);
                AlbumartList.add(TOP100Albumart);
            }
            CombineData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void songsid(Scanner scli) {
        while (scli.hasNext()) {
            String getline = scli.nextLine();
            if (getline.contains("albumid")) {
                String ab = getline.split("albumid")[0]
                        .replaceAll("<li id=", "").replaceAll("\"", "")
                        .replace(" ", "");
                SongSIDList.add(ab);
            }
        }
    }

    private void albumart(Scanner sc) {
        while (sc.hasNext()) {
            String getline = sc.nextLine();
            if (getline.contains("class=\"album_img\"")) {
                String URL = getline.split("src")[1].substring(2).split("\"")[0];
                AlbumartList.add(URL);
            }
        }
    }

    private void songtitle(Scanner scdiv) {
        while (scdiv.hasNext()) {
            String getline = scdiv.nextLine();
            if (getline.contains("class=\"tit\"")) {
                String ab = getline.replaceAll(
                        " <span class=\"tit\" id=\"cTitle\">", "").replaceAll(
                        "</span>", "");
                if (ab.contains("<img alt=\"\" src=\"http://image.melon.co.kr/resource/image/cds/detail/android2/ico_hot.png\" />")) {
                    ab = ab.replaceAll(
                            "<img alt=\"\" src=\"http://image.melon.co.kr/resource/image/cds/detail/android2/ico_hot.png\" />",
                            "");
                }
                if (ab.contains("<img class=\"forbid\" src=\"http://image.melon.co.kr/resource/image/cds/detail/android2/ico_19.png\" alt=\"\" />&nbsp;")) {
                    ab = ab.replaceAll(
                            "<img class=\"forbid\" src=\"http://image.melon.co.kr/resource/image/cds/detail/android2/ico_19.png\" alt=\"\" />&nbsp;",
                            "");
                }
                if (ab.contains("&amp;"))
                    ab = ab.replace("&amp;", "&");
                if (ab.contains("&quot;"))
                    ab = ab.replace("&quot;", "\"");
                TitleList.add(ab);
            }
        }
    }

    private void singer(Scanner scdiv) {
        while (scdiv.hasNext()) {
            String getline = scdiv.nextLine();
            if (getline.contains("class=\"singer\"")) {
                String ab = getline.replaceAll(
                        " <span class=\"singer\" id=\"cArtistName\">", "")
                        .replaceAll("</span>", "");
                if (ab.contains("&amp;"))
                    ab = ab.replace("&amp;", "&");
                if (ab.contains("&quot;"))
                    ab = ab.replace("&quot;", "\"");
                SingerList.add(ab);
            }
        }
    }

    private void ClearArrayList() {
        SongSIDList.clear();
        SingerList.clear();
        SearchList.clear();
        TitleList.clear();
        AlbumartList.clear();
    }

    private void CombineData() {
        try {
            for (int i = 0; i < TitleList.size(); i++) {
                SearchList.add(new SearchData(TitleList.get(i), SongSIDList
                        .get(i), AlbumartList.get(i), SingerList.get(i)));
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 앨범아트 주소를 ArrayList로 반환한다.
     */
    public ArrayList<String> getAlbumartList() {
        return AlbumartList;
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 음악의 SID정보를 ArrayList로 반환한다.
     */
    public ArrayList<String> getSIDList() {
        return SongSIDList;
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 음악의 제목을 ArrayList로 반환한다.
     */
    public ArrayList<String> getSongList() {
        return TitleList;
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 음악의 종합 정보를 ArrayList로 변환한다.
     */
    public ArrayList<SearchData> getAllData() {
        return SearchList;
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 음악의 가수이름을 ArrayList로 반환한다.
     */
    public ArrayList<String> getSingerList() {
        return SingerList;
    }

    /**
     * 이 메소드를 사용하기 전에 <br>
     * Search메소드나 Top100메소드를 사용후<br>
     * 사용해야 한다.
     *
     * @return 음악제목을 ArrayList로 반환한다.
     */
    public String getSongName() {
        return SongName;
    }

    /**
     * 음악 검색 결과 최고 크기를 정함
     *
     * @param maxSize
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 음악 검색 결과 최고 크기를 정함
     *
     * @param minSize
     */
    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    /**
     * 음악 정렬 방법을 정함<br>
     * MelonSearch.POPULAR : 인기도순<br>
     * MelonSearch.RECENT : 최신순<br>
     * MelonSearch.ACCURACY : 정확도순<br>
     *
     * @param Order
     */
    public void setOrder(String Order) {
        this.Order = Order;
    }

    /**
     * 음악 제목 설정<br>
     * setSongName("제목"); <br>
     * 같이 사용한다.
     *
     * @param SongName
     */
    public void setSongName(String SongName) {
        this.SongName = SongName;
    }

    public static synchronized MelonSearch getinstance() {
        if (instance == null) {
            instance = new MelonSearch();
        }
        return instance;
    }

    public class SearchData {
        private String SongName;
        private String SID;
        private String Albumart;
        private String Singer;

        public SearchData(String SongName, String SID, String Albumart,
                          String Singer) {
            this.SongName = SongName;
            this.SID = SID;
            this.Albumart = Albumart;
            this.Singer = Singer;
        }

        public String getSinger() {
            return Singer;
        }

        public String getSongName() {
            return SongName;
        }

        public String getSID() {
            return SID;
        }

        public String getAlbumart() {
            return Albumart;
        }
    }
}