package Client.login;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import http.HttpClientUtil;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import static Constants.AgentConstants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static Constants.AgentConstants.LOGO_IMAGE;
import static java.lang.System.exit;

public class AgentLoginClient extends Application{
    private AgentLoginController loginController;

    @Override
    public void start(Stage primaryStage)throws Exception {

        primaryStage.setMinHeight(390);
        primaryStage.setMinWidth(660);
        primaryStage.setTitle("Agent - Login");

        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setController(primaryStage,this);
            loginController.initialize();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (loginController != null) {
                        loginController.close();
                        exit(0);
                    }
                }
            });
            Scene scene = new Scene(root, 400, 400);
            primaryStage.getIcons().add(new Image(LOGO_IMAGE));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        // loginController..close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}