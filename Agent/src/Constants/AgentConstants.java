package Constants;

import com.google.gson.Gson;
import okhttp3.MediaType;

public class AgentConstants {
    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/resources/AgentLogin.fxml";
    public final static String AGENT_APP_PAGE_FXML_RESOURCE_LOCATION = "/resources/Agent.fxml";
    public final static String LOGO_IMAGE = "/resources/logo.jpg";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String ALLIES_LIST = FULL_SERVER_PATH + "/allieslist";
    public static final String ALLIES_APP_DTO = FULL_SERVER_PATH + "/agentData";
    public static final String MISSIONS_PULL_DTO = FULL_SERVER_PATH + "/pullMissions";
    public static final String SEND_MISSION_RESULTS = FULL_SERVER_PATH + "/sendResults";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/AgentLogin";
    public final static String LOGOUT = FULL_SERVER_PATH + "/Logout";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public static final String UBOAT = "UBoat";
    public static final String Allies = "Allies";
    public static final String AGENT = "Agent";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
