package Main.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static util.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static util.Constants.LOGO_IMAGE;

public class LoginUBoatClient extends Application{
    private LoginController loginController;

    @Override
    public void start(Stage primaryStage)throws Exception {
        primaryStage.setTitle("Login To Server");

        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            Scene scene = new Scene(root, 500, 500);
            primaryStage.getIcons().add(new Image(LOGO_IMAGE));
            primaryStage.setScene(scene);
            loginController.setController(primaryStage,this);
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

