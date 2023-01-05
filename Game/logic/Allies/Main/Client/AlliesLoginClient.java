package Main.Client;

import Constants.AlliesConstants;
import Main.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static Constants.AlliesConstants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static Constants.AlliesConstants.LOGO_IMAGE;

public class AlliesLoginClient extends Application{
    private LoginController loginController;

    @Override
    public void start(Stage primaryStage)throws Exception {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Ally Login ");

        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setController(primaryStage,this);
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
