package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class HintMsg implements Serializable {

    private static final long serialVersionUID = 1L;
    public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
    public String UserName;
    public String data;
    public String roomId;
    public int wordIndex;
    public boolean isStart = false;

    public HintMsg(String UserName, String code, String msg) {
//        this.roomId = roomId;
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
    }
}
