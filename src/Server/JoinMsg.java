package Server;

import Client.GameClientMain;

import java.io.Serializable;

public class JoinMsg implements Serializable {
    public String code;
    public String roomId;
    public String wordList;

    public String socketList, userList, charList;
    public String username, char_no;

    public GameClientMain main;
    public JoinMsg(String code, String roomId, String socketList, String userList, String charList, String username, String char_no){
        this.code = code;
        this.roomId = roomId;
        this.socketList = socketList;
        this.userList = userList;
        this.charList = charList;
        this.username = username;
        this.char_no = char_no;
    }
}
