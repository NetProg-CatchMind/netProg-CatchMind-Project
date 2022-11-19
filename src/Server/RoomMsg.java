package Server;

import java.io.Serializable;
import java.util.Vector;

public class RoomMsg implements Serializable {
    private static final long serialVersionUID = 2L;
    public String code;
    public String roomId;
    public String roomTitle, roomSubject;
    public int userCnt;
    private Vector userList = new Vector(); // 연결된 사용자를 저장할 벡터

    public RoomMsg(String code, String roomId, String roomTitle, String roomSubject, int userCnt) {
        this.code = code;
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.roomSubject = roomSubject;
        this.userCnt = userCnt;
    }

}
