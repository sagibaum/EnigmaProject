package Constants;

import com.google.gson.Gson;

public class AlliesConstants {
    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    //public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/resources/login.fxml";
    public final static String Allies_APP_PAGE_FXML_RESOURCE_LOCATION = "/resources/Allies.fxml";

    public final static String LOGO_IMAGE = "/resources/logo.jpg";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String ALLIES_APP_DTO = FULL_SERVER_PATH + "/alliesdata";
    public final static String ANNOUNCE_READY = FULL_SERVER_PATH + "/ReadyAnnounceServletAllies";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/AlliesLogin";
    public static final String CHOOSE_BATTLEFIELD = FULL_SERVER_PATH +"/chooseBattleField";

    public final static String LOGOUT = FULL_SERVER_PATH + "/Logout";

    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public static final String UBOAT = "UBoat";
    public static final String Allies = "Allies";
    public static enum alliesCurrentStatus {
        IDLE,
        STARTING_CONTEST,
        CONTEST_IN_PROGRESS,
        FINISHED
    }


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();


}
