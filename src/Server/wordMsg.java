package Server;

import Client.GameClientMain;

import java.io.Serializable;

public class wordMsg implements Serializable {

    public String code;
    public String wordList;
    public String roomId;
    public int presenterIndex; //문제 내는 사람

    public wordMsg(String code, String roomId){
        this.code = code;
        this.roomId = roomId;
    }
}

