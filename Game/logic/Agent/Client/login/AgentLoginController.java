package Client.login;

import Client.controller.AgentClientController;
import Constants.AgentConstants;
import ServerDTOs.Allies.ContestsDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static Constants.AgentConstants.*;

public class AgentLoginController implements Closeable {

    @FXML
    private ScrollPane AgentLoginScrollPane;

    @FXML
    private TextField UserNameTextField;

    @FXML
    private ComboBox<String> AlliesTeamsComboBox;

    @FXML
    private Slider ThreadsNumberSlider;

    @FXML
    private TextField MissionSizeTextField;

    @FXML
    private Button RegisterAgent;

    @FXML
    private Label errorMessageLabel;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    private Alert alert;

    private AgentClientController agentClientController;
    private Stage stage;
    private Scene scene;
    private Parent root;

    //-----
    private Timer timer;
    private TimerTask listRefresher;
    private AgentLoginDTO pack;

    private AgentLoginClient rootController;
    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        startListRefresher();
    }


    public void setController(Stage fatherStage,AgentLoginClient agentClientController){
        this.rootController=agentClientController;
        this.stage = fatherStage;
    }

    @FXML
    void registerAgentButtonClicked(MouseEvent event) {
        String userName = UserNameTextField.getText();
        if(AlliesTeamsComboBox.getItems().isEmpty() || AlliesTeamsComboBox.getValue() == null
                || AlliesTeamsComboBox.getValue().equals("")){
            errorMessageLabel.setOpacity(1);
            errorMessageProperty.set("You must choose an allies team \n(if no allies are online yet, you need to wait)");
            return;
        }                            //ACTIVATE THIS WHEN PULLING ALLIES DATA FROM SERVER WORKS!
        if (userName.isEmpty()) {
            errorMessageLabel.setOpacity(1);
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        else if(!MissionSizeTextField.getText().matches("[0-9]+"))
        {
            errorMessageLabel.setOpacity(1);
            errorMessageProperty.set("Mission size isnt a number or not positive!");
            MissionSizeTextField.clear();
            return;
        }
        this.pack = new AgentLoginDTO(userName, AlliesTeamsComboBox.getValue(), (int)ThreadsNumberSlider.getValue()
                ,Integer.valueOf(MissionSizeTextField.getText()));

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.AgentConstants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("userName", userName)
                .addQueryParameter("AlliesTeam", pack.getAlliesTeam())
                .addQueryParameter("ThreadNumber", pack.getThreadNumber().toString())
                .addQueryParameter("MissionSize", pack.getMissionSize().toString())
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
                            switchToAgentApp();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        // chatAppMainController.updateUserName(userName);
                    });
                }
            }
        });
    }

    private void updateAlliesList(List<String> usersNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = AlliesTeamsComboBox.getItems();
            List<String> temp = new ArrayList<>(items);
             for (String str: usersNames) { //IF USERNAME ISNT IN COMBOBOX ADD IT
                if(!items.contains(str))
                    items.add(str);
            }

            boolean isThere = false;

            for (String item:temp){
                for (String str : usersNames) {
                    if(item.equals(str))
                    {
                        isThere=true;
                        break;
                    }
                }
                if(!isThere)
                    items.remove(item);
                else isThere=false;
            }

        });
    }

    public void startListRefresher() {
        listRefresher = new alliesListRefresher(
                this::updateAlliesList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void switchToAgentApp() throws IOException {
        try{
            URL mainApp = getClass().getResource(AGENT_APP_PAGE_FXML_RESOURCE_LOCATION);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainApp);
            Parent root = fxmlLoader.load();
            stage.setTitle("Agent " +UserNameTextField.getText());
            agentClientController = fxmlLoader.getController();
            agentClientController.setRootController(this);
            scene = new Scene(root);
            stage.getIcons().add(new Image(LOGO_IMAGE));
            stage.setScene(scene);
            stage.show();
            close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void switchToAgentLoginPage() throws Exception {
        HttpClientUtil.removeCookiesOf(AgentConstants.BASE_DOMAIN);
        rootController.start(stage);
    }

    @Override
    public void close() {
        if (AlliesTeamsComboBox != null)
            AlliesTeamsComboBox.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

}
