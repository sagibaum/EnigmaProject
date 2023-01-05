package Main.Client.ContestTabController.Candidates;

public class TableCandidate {

    private String decryptedMessage;
    private String codeConfiguration;

    private String allyName;
    private long encryptionTime;

    public TableCandidate(String candidate, String codeConfiguration, String allieReResponsible, long encryptionTime) {
        this.decryptedMessage = candidate;
        this.codeConfiguration = codeConfiguration;
        this.allyName = allieReResponsible;
        this.encryptionTime = encryptionTime;
    }

    public String getDecryptedMessage() {
        return decryptedMessage;
    }

    public void setDecryptedMessage(String decryptedMessage) {
        this.decryptedMessage = decryptedMessage;
    }

    public String getCodeConfiguration() {
        return codeConfiguration;
    }

    public void setCodeConfiguration(String codeConfiguration) {
        this.codeConfiguration = codeConfiguration;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public long getEncryptionTime() {
        return encryptionTime;
    }

    public void setEncryptionTime(long encryptionTime) {
        this.encryptionTime = encryptionTime;
    }

    @Override
    public String toString() {
        return "TableCandidate{" +
                "decryptedMessage='" + decryptedMessage + '\'' +
                ", codeConfiguration='" + codeConfiguration + '\'' +
                ", allyName='" + allyName + '\'' +
                ", encryptionTime=" + encryptionTime +
                '}';
    }
}
