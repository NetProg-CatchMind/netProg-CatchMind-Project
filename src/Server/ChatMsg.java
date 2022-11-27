package Server;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;
    public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
    public String UserName;
    public String char_no;
    public String data;
    public String roomId;
    public ImageIcon img;
    public MouseEvent mouse_e;
    public int pen_size; // pen size
    public Color co;
    public int shape;
    public boolean lines;
    public boolean isStart;

    public ChatMsg(String UserName, String code, String msg) {
//        this.roomId = roomId;
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
    }
}
