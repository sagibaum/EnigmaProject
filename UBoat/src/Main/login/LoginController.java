package Main.login;

import Main.Client.UBoatClientController;
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
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static util.Constants.LOGO_IMAGE;
import static util.Constants.U_BOAT_APP_PAGE_FXML_RESOURCE_LOCATION;

public class LoginController {

    @FXML
    private TextField UserNameTextField;

    @FXML
    private Button LoginButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Alert alert;

    private UBoatClientController uBoatClientController;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label errorMessageLabel;

    private LoginUBoatClient loginUBoatClient;


    @FXML
    public void initialize() {

        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    public void setController(Stage fatherStage, LoginUBoatClient loginUBoatClient){
        this.stage = fatherStage;
        this.loginUBoatClient = loginUBoatClient;
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
                .parse(Constants.LOGIN_PAGE)
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
                        // Here function to open the Uboat app! like the manual scene that i opened in the machine tab controller
                        try{
                            switchToUBoatApp();}
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    public void switchToUBoatApp() throws IOException {
        try{
        URL mainApp = getClass().getResource(U_BOAT_APP_PAGE_FXML_RESOURCE_LOCATION);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(mainApp);
        Parent root = fxmlLoader.load();
        uBoatClientController = fxmlLoader.getController();
        uBoatClientController.setUBoatClientController(this);
        uBoatClientController.setMachineTabImageScales(stage);;
        uBoatClientController.setClientName(UserNameTextField.getText());
        stage.setTitle("U-Boat" );
        scene = new Scene(root,1200,800);
        stage.getIcons().add(new Image(LOGO_IMAGE));
        stage.setScene(scene);
        stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void switchToUBoatLoginPage() throws Exception {
        HttpClientUtil.removeCookiesOf(Constants.BASE_DOMAIN);
        loginUBoatClient.start(stage);
    }


}
