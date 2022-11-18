package Server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GameRoom extends Thread{
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;
    String title, subject;
    int memberCnt;
    Vector memberList = new Vector(); //현재 방에 있는 사용자들 리스트

    Vector UserVec = new Vector(); //방에 상관없이 접속한 모든 사용자들.

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓

    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Vector user_vc;

    public String UserName = "";
    public String UserStatus;

    public GameRoom(String title, String subject, int memberCnt, ServerSocket socket, Socket client_socket, Vector UserVec ){
        this.title = title;
        this.subject = subject;
        this.memberCnt = memberCnt;
        this.memberList = memberList;

        this.socket = socket;
        this.client_socket = client_socket;
    }

    public void run() {
        while (true) { // 사용자 접속을 계속해서 받기 위해 while문
            try {
                Object obcm = null;
                String msg = null;
                RoomMsg gr = null;
                ChatMsg cm = null;

                if (socket == null)
                    break;

                try {
                    obcm = ois.readObject(); //object 읽어들이기.
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }

                if (obcm == null)
                    break;

//                if(obcm instanceof RoomMsg){
//                    gr = (RoomMsg) obcm;
//                    makeRoom(gr.roomTitle, gr.roomSubject, gr.userCnt);
//                }

                if (obcm instanceof ChatMsg) { //obcm(읽어들인 object)이 ChatMsg라면
                    cm = (ChatMsg) obcm; //ChatMsg 형식으로 바꿔서
                    AppendObject(cm);
                } else
                    continue;

//                //로그인!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                if (cm.code.matches("100")) {
//                    UserName = cm.UserName;
//                    UserStatus = "O"; // Online 상태
//                    Login();
//                }

                //채팅 메세지!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (cm.code.matches("200")) {
                    msg = String.format("[%s] %s", cm.UserName, cm.data);
                    AppendText(msg); // server 화면에 출력
                    String[] args = msg.split(" "); // 단어들을 분리한다.
                    if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
                        UserStatus = "O";
                    } else if (args[1].matches("/exit")) {
//                        Logout();
                        break;
                    } else if (args[1].matches("/list")) {
                        WriteOne("User list\n");
                        WriteOne("Name\tStatus\n");
                        WriteOne("-----------------------------\n");
                        for (int i = 0; i < user_vc.size(); i++) {
                            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
                            WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
                        }
                        WriteOne("-----------------------------\n");
                    } else if (args[1].matches("/sleep")) {
                        UserStatus = "S";
                    } else if (args[1].matches("/wakeup")) {
                        UserStatus = "O";
                    } else if (args[1].matches("/to")) { // 귓속말
                        for (int i = 0; i < user_vc.size(); i++) {
                            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
                            if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
                                String msg2 = "";
                                for (int j = 3; j < args.length; j++) {// 실제 message 부분
                                    msg2 += args[j];
                                    if (j < args.length - 1)
                                        msg2 += " ";
                                }
                                // /to 빼고.. [귓속말] [user1] Hello user2..
                                user.WritePrivate(args[0] + " " + msg2 + "\n");
                                //user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
                                break;
                            }
                        }
                    } else { // 일반 채팅 메시지
                        UserStatus = "O";
                        //WriteAll(msg + "\n"); // Write All
                        WriteAllObject(cm);
                    }
                }

//                //로그아웃!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                else if (cm.code.matches("400")) { // logout message 처리
//                    Logout();
//                    break;
//                } else { // 300, 500, ... 기타 object는 모두 방송한다.
//                    WriteAllObject(cm);
//                }

            } catch (IOException e) {
                AppendText("ois.readObject() error");
                try {
//						dos.close();
//						dis.close();
                    ois.close();
                    oos.close();
                    client_socket.close();
//                    Logout(); // 에러가난 현재 객체를 벡터에서 지운다
                    break;
                } catch (Exception ee) {
                    break;
                } // catch문 끝
            } // 바깥 catch문끝
        } // while
    } // run


    public void AppendText(String str) {
        // textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    // UserService Thread가 담당하는 Client 에게 1:1 전송
    public void WriteOne(String msg) {
        try {
            // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
            ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
            oos.writeObject(obcm);
        } catch (IOException e) {
            AppendText("dos.writeObject() error");
            try {
//					dos.close();
//					dis.close();
                ois.close();
                oos.close();
                client_socket.close();
                client_socket = null;
                ois = null;
                oos = null;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
//            Logout(); // 에러가난 현재 객체를 벡터에서 지운다
        }
    }

    // 읽어들인 object가 ChatMsg라면.
    public void AppendObject(ChatMsg msg) {
        // textArea.append("사용자로부터 들어온 object : " + str+"\n");
        textArea.append("code = " + msg.code + "\n");
        textArea.append("id = " + msg.UserName + "\n");
        textArea.append("data = " + msg.data + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    // 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
    public void WriteAllObject(Object ob) {
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOneObject(ob);
        }
    }

    // 귓속말 전송
    public void WritePrivate(String msg) {
        try {
            ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
            oos.writeObject(obcm);
        } catch (IOException e) {
            AppendText("dos.writeObject() error");
            try {
                oos.close();
                client_socket.close();
                client_socket = null;
                ois = null;
                oos = null;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Logout(); // 에러가난 현재 객체를 벡터에서 지운다
        }
    }

    public void WriteAll(String str) {
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void Logout() {
        String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
        UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
        WriteAll(msg); // 나를 제외한 다른 User들에게 전송
        AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
    }
}
