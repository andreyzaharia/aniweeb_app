package aniweeb.com.URLs;

public class URLs {

    //base api
    private static final String API_MAL = "https://api.myanimelist.net/v2/";

    private static final String API_JIKAN = "https://api.jikan.moe/v4/";


    public static String getAnimeList = API_MAL + "anime";
    public static String getRankingAnimeList = API_MAL + "anime/ranking";

    public static String getGenres = API_JIKAN + "genres/anime";
    public static String getAnimeJikanList = API_JIKAN + "anime";
    public static String getEpisodes = API_JIKAN + "schedules";
    public static String getRandomAnime = API_JIKAN + "random/anime";


}
