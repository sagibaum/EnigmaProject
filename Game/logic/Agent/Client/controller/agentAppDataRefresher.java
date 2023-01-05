package Client.controller;

import Constants.AgentConstants;
import ServerDTOs.Agent.AgentDTO;
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

public class agentAppDataRefresher extends TimerTask {

    private final Consumer<AgentDTO> alliesDataPackage;

    public agentAppDataRefresher(Consumer<AgentDTO> usersListConsumer) {
        this.alliesDataPackage = usersListConsumer;
    } //consumers for contests data

    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(AgentConstants.ALLIES_APP_DTO)
                .newBuilder()
                //.addQueryParameter("userName", alliesTeamName) //USE COOKIES TO GET USERNAME ON SERVER SIDE
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //allies users were not received, message failed to deliver
                Platform.runLater(() -> {
                    //System.out.println("message was not received in agentAppDataRefresher");
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String jsonAgentDTO = response.body().string();

                Platform.runLater(() -> {

                    //System.out.println(jsonAgentDTO);

                    //String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                    //usersListConsumer.accept(Arrays.asList(usersNames));

                    AgentDTO data = GSON_INSTANCE.fromJson(jsonAgentDTO, AgentDTO.class);

                    //System.out.println(data);

                    alliesDataPackage.accept(data);
                });
            }
        });
    }
}
