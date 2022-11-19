package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

public class GameRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    public String roomTitle, roomSubject;
    public int userCnt;

    private boolean isSel = false;
    public boolean isSel() {
        return isSel;
    }
    public void setSel(boolean sel) {
        isSel = sel;
    }


    private Vector userList = new Vector(); // 연결된 사용자를 저장할 벡터

    public GameRoom( String roomTitle, String roomSubject, int userCnt) {
        this.roomTitle = roomTitle;
        this.roomSubject = roomSubject;
        this.userCnt = userCnt;
    }


}

