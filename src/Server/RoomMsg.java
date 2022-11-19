package Server;

import java.io.Serializable;
import java.util.Vector;

public class RoomMsg implements Serializable {
    private static final long serialVersionUID = 2L;
    public String code;
    public String roomId;
    public String roomTitle, roomSubject;
    public String roomList;
    public int userCnt;
    private Vector userList = new Vector(); // 연결된 사용자를 저장할 벡터
    public String roomIdList;
    public String roomTitleList;
    public String roomSubjectList;
    public String roomCntList;

//    public RoomMsg(String code, String roomId, String roomTitle, String roomSubject, int userCnt) {
    public RoomMsg(String code, String roomIdList, String roomTitleList, String roomSubjectList, String roomCntList) {
        this.code = code;
        this.roomIdList = roomIdList;
        this.roomTitleList = roomTitleList;
        this.roomSubjectList = roomSubjectList;
        this.roomCntList = roomCntList;

//        this.roomList = roomList;
//        this.roomId = roomId;
//        this.roomTitle = roomTitle;
//        this.roomSubject = roomSubject;
//        this.userCnt = userCnt;
    }

}
