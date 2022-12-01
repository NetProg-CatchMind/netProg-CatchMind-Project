package Client;

import javax.swing.*;
import java.io.Serializable;
import java.util.Vector;

public class RoomMsg implements Serializable {

    private static final long serialVersionUID = 2L;
    public String code;
    public String data;
    public String roomId;
    public String roomTitle, roomSubject;
    public int userCnt;
    private Vector userList = new Vector();  // 연결된 사용자를 저장할 벡터

//    String code, String roomId, String roomTitle, String roomSubject, int userCnt
    public RoomMsg(String code, String data) {
        this.code = code;
        this.data = data;
    }
}
