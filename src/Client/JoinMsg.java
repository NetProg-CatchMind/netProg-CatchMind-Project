package Client;

import java.io.Serializable;

public class JoinMsg implements Serializable {

    public String code;
    public String roomId;
    public JoinMsg(String code, String roomId){
        this.code = code;
        this.roomId = roomId;
    }
}
