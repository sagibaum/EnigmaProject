package Main.Client;

import ServerDTOs.Uboat.UBoatDTO;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.UPDATE_UBOAT_PAGE;

public class UBoatAlliesRefresher extends TimerTask {

    private Consumer<UBoatDTO> UBoatDataPackage;

    public UBoatAlliesRefresher(Consumer<UBoatDTO> UBoatDataPackage) {
        this.UBoatDataPackage = UBoatDataPackage;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl.parse(UPDATE_UBOAT_PAGE).newBuilder().build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //allies users were not received, message failed to deliver
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonUBoatDTo = response.body().string();
                if(response.code()!=200) {
                    Platform.runLater(() -> {
                        //System.out.println("Error : " + jsonUBoatDTo);
                    });}
                else {
                    Platform.runLater(() -> {
                        UBoatDTO uBoatDTO =GSON_INSTANCE.fromJson(jsonUBoatDTo, UBoatDTO.class);
                        UBoatDataPackage.accept(uBoatDTO);
                    });
                }

            }

        });

    }

}
