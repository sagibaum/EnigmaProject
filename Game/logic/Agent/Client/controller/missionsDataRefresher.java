package Client.controller;

import Constants.AgentConstants;
import ServerDTOs.Agent.MissionDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Constants.AgentConstants.GSON_INSTANCE;

public class missionsDataRefresher extends TimerTask {

    private final Consumer<MissionDTO> missionsDataPackage;

    public missionsDataRefresher(Consumer<MissionDTO> usersListConsumer) {
        this.missionsDataPackage = usersListConsumer;
    } //consumers for contests data

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(AgentConstants.MISSIONS_PULL_DTO)
                .newBuilder()
                //.addQueryParameter("userName", alliesTeamName) //USE COOKIES TO GET USERNAME ON SERVER SIDE
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //allies users were not received, message failed to delive
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String jsonMissionDTO = response.body().string();

                Platform.runLater(() -> {

                    if(response.code() == 200) {
                        MissionDTO data = GSON_INSTANCE.fromJson(jsonMissionDTO, MissionDTO.class);



                        missionsDataPackage.accept(data);
                    }

                });
            }
        });
    }
}
