package com.hyeok.melon;

/**
 * Created by GwonHyeok on 14. 12. 14..
 */
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