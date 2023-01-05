package EncryptDecryptTabController.Stats;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class StatsController {
    @FXML
    private TextArea StatisticsTextAreaT2;

    public void setTextArea(String machineHistoryAndStatistics) {
        StatisticsTextAreaT2.setText(machineHistoryAndStatistics);
    }
}
