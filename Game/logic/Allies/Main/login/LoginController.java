package Main.login;

import Main.Client.AlliesLoginClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import Constants.AlliesConstants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static Constants.AlliesConstants.*;

public class LoginController {

    @FXML
    private TextField UserNameTextField;

    @FXML
    private Button LoginButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Alert alert;

    private Main.Client.AlliesClientController AlliesClientController;
    private Stage stage;
    private Scene scene;
    private AlliesLoginClient alliesLoginClient;

    @FXML
    private Label errorMessageLabel;

    private String alliesTeamName;

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);

    }

    public void setController(Stage fatherStage,AlliesLoginClient fatherController){
        this.stage = fatherStage;
        this.alliesLoginClient = fatherController;
    }
    @FXML
    private void loginButtonClicked(MouseEvent event) {
        String userName = UserNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.AlliesConstants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();



        HttpClientUtil.runAsync(finalUrl, new Callback() {



            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Login Process ");
                    alert.setContentText("Login process failed!\n" + "Reason: " + e.getMessage());
                    alert.showAndWait();

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        errorMessageLabel.setOpacity(1);
                        errorMessageProperty.set("Something went wrong:\n" +responseBody);
                    });

                } else {
                    Platform.runLater(() -> {
                        // Here function to open the ALLIES app! like the manual scene that i opened in the machine tab controller
                        // get controller from the fxmlLoader ...
                        try{
                            alliesTeamName = UserNameTextField.getText();
                            switchToAlliesApp();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // chatAppMainController.updateUserName(userName);
                    });
                }
            }
        });
    }

    public void switchToAlliesApp() throws IOException {
        try{
            URL mainApp = getClass().getResource(Allies_APP_PAGE_FXML_RESOURCE_LOCATION);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainApp);
            Parent root = fxmlLoader.load();
            AlliesClientController = fxmlLoader.getController();
            AlliesClientController.setRootController(this);
            AlliesClientController.setAlliesTeamName(UserNameTextField.getText());
            stage.setTitle("Ally "+UserNameTextField.getText());
            scene = new Scene(root);
            stage.getIcons().add(new Image(LOGO_IMAGE));
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public void switchToAllyLoginPage() throws Exception {
        HttpClientUtil.removeCookiesOf(AlliesConstants.BASE_DOMAIN);
        alliesLoginClient.start(stage);
    }

}
