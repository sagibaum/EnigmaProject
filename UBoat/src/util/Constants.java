package util;

import com.google.gson.Gson;

public class Constants {
    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/resources/login.fxml";
    public final static String U_BOAT_APP_PAGE_FXML_RESOURCE_LOCATION = "/resources/UBoatMain.fxml";
    public final static String U_BOAT_POPUP_MANUAL_CONF_PAGE_FXML_RESOURCE_LOCATION = "/Main/Client/MachineTabController/ManualConfController/ManualConfiguration.fxml";

    public final static String LOGO_IMAGE = "/resources/logo.jpg";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPDATE_UBOAT_PAGE = FULL_SERVER_PATH + "/uBoatUpdater";
    public final static String SEND_MACHINE_DETAILS = FULL_SERVER_PATH + "/UBoatContestDataUpdaterServlet";

    public final static String ANNOUNCE_READY = FULL_SERVER_PATH + "/ReadyAnnounceUBoat";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/UBoatLogin";
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/UploadFile";
    public final static String LOGOUT = FULL_SERVER_PATH + "/Logout";


    public final static String WINNER_ANNOUNCE = FULL_SERVER_PATH + "/winnerAnnounce";
    public final static String RESET_COMPETITION_DATA = FULL_SERVER_PATH + "/resetContestData";

    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public static final String UBOAT = "UBoat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public enum TableRow{
        NAME_COL(0),
        AGENT_NUMBER_COL(1),
        MISSION_SIZE_COL(2),
        READY_STATUS_TAB(3);
        private final int value;
        TableRow( final Integer newValue) {value = newValue;}

        public int getValue() { return value;}
    }


}
