package Server;

import java.io.Serializable;

public class JoinMsg implements Serializable {
    public String code;
    public String roomId;

    public String socketList, userList, charList;
    public String username, char_no;
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
