package Server;

import Client.GameClientMain;

import java.io.Serializable;

public class wordMsg implements Serializable {

    public String code;
    public String wordList;

    public wordMsg(String code){
        this.code = code;
    }
}

