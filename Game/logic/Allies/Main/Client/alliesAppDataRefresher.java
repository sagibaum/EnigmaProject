package Main.Client;

import Constants.AlliesConstants;
import ServerDTOs.Allies.AlliesDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Constants.AlliesConstants.GSON_INSTANCE;

public class alliesAppDataRefresher extends TimerTask {

    private final Consumer<AlliesDTO> alliesDataPackage;
    private final String alliesTeamName;

    public alliesAppDataRefresher( Consumer<AlliesDTO> usersListConsumer, String AlliesTeamName) {
        this.alliesDataPackage = usersListConsumer;
        this.alliesTeamName = AlliesTeamName;
    } //consumers for contests data

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(AlliesConstants.ALLIES_APP_DTO)
                .newBuilder()
                .addQueryParameter("userName", alliesTeamName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //allies users were not received, message failed to deliver
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                ResponseBody body = response.body();
                String jsonAlliesDTO = body.string();

                if(response.code()!=200) { //not good
                    //Platform.runLater(() -> System.out.println("Error : " + jsonAlliesDTO));
                }
                else {
                    Platform.runLater(() -> {
                        //System.out.println("200: " + jsonAlliesDTO + "   ");
                        AlliesDTO AlliesDTO = GSON_INSTANCE.fromJson(jsonAlliesDTO, AlliesDTO.class);
                        alliesDataPackage.accept(AlliesDTO);
                    });
                }
                //AlliesDTO data = GSON_INSTANCE.fromJson(jsonAlliesDTO, AlliesDTO.class);
                //alliesDataPackage.accept(data);

            }
        });
    }
}