package Server;

import java.io.Serializable;

public class JoinMsg implements Serializable {
    public String code;
    public String roomId;
    public String userList;

    public JoinMsg(String code, String roomId, String userList){
        this.code = code;
        this.roomId = roomId;
        this.userList = userList;

    }
}
