package EngineFunctionallity;

import user.data.transfer.unit.UserDataTransferUnit;

public interface UICommands {


    void UploadXmlFile(String filePath, UserDataTransferUnit UICommunicationUnit);//option one in the UI menu

    void ViewMachineSpecifications(UserDataTransferUnit UICommunicationUnit);//Shows machine configurations

    void ManuallyInitialCodeConf(String Code);//manually setting the machine code

    void AutomaticallyInitialCodeConf(UserDataTransferUnit UICommunicationUnit);//Automatically setting the machine code

    String InputEncryptionOrDecoding(String message, UserDataTransferUnit UICommunicationUnit,boolean byLetter);//Input processing(encryption or decoding)

    void ResetCode(UserDataTransferUnit UICommunicationUnit);//resetting current code after encoding or decoding one message

    String MachineHistoryAndStatistics();//Returning the stats of the activity in current machine

    void SaveMachineState(String Path);//Saving machine state in java binary code

    EngineObject LoadMachineState(String Path,UserDataTransferUnit UICommunicationUnit);// loading machine state from java binary code (.sre)
}
