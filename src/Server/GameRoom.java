package Server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GameRoom extends Thread{ //game room 입장 후 서버에서 방에 뿌리는 방송 클래스
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;
    public String roomId;
    public String title, subject;
    int memberCnt;

    public String word[] = {"새", "나무", "사람"};
    public String turnUser;
    public int wordturn = 0;
    public int gamestart = 0;// 게임 시작하면 1로
    public int UserScore = 0;
    public String UserName = "";
    public String UserStatus;
    public int backgrounds = 0;

    public  Vector user_vc; //???
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

    public GameRoom(String roomId, String title, String subject, int memberCnt, ServerSocket socket, Socket client_socket ){
        this.roomId = roomId;
        this.title = title;
        this.subject = subject;
        this.memberCnt = memberCnt;

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
                if(obcm instanceof RoomMsg){
                    gr = (RoomMsg) obcm;
                    //makeRoom(gr.roomTitle, gr.roomSubject, gr.userCnt);
                }
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

                if (cm.code.matches("100")) {
                    UserName = cm.UserName;

                    if (gamestart == 1) {

                        WriteOne2("이미 게임이 시작되었습니다. 잠시 후 게임이 끝난 후 다시 들어와주세요!");
                        Logout2();
                        break;
                    } else {

                        UserStatus = "O"; // Online 상태
                        Login();
                    }

                }

                //채팅 메세지!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (cm.code.matches("200")) {
                    msg = String.format("[%s] %s", cm.UserName, cm.data);
                    AppendText(msg); // server 화면에 출력
                    String[] args = msg.split(" "); // 단어들을 분리한다.
                    //Object[] word;
                    if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
                        UserStatus = "O";
                    } else if (args[1].matches("/exit")) {
//                      Logout();
                        break;

                    }  else if ((cm.data.equals(word[wordturn])) && (gamestart == 1)) {
                        if (UserName != turnUser) {
                            WriteAllObject(cm);
                            UserScore++;

                            if (backgrounds == 1) {
                                // 전체에 배경 사진 보내기
                                ChatMsg obcm6799 = new ChatMsg("SERVER", "301", word[wordturn]);

                                for (int j = 0; j < user_vc.size(); j++) {
                                    GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(j);
                                    if (turnUser == user.UserName) {
                                        user.WriteOneObject(obcm6799); // WriteAll로 바꾸기
                                    }
                                }
                                // 301을 보내서 모두에게 300(이미지)를 보내기
                                backgrounds = 0;
                            }

                            if (wordturn == 4) {
                                wordturn = 0;
                            } else {
                                wordturn++;
                            }
                            ChatMsg obcm67 = new ChatMsg("SERVER", "701", msg);
                            WriteAllObject(obcm67);

                            //WriteAll2("------------------사용자 목록------------------- ");
                            for (int j = 0; j < user_vc.size(); j++) {
                                GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(j);
                                WriteAll2(
                                        "Name:  " + user.UserName + "           Score:  " + user.UserScore + "\n");
                            }

                            WriteAll6(UserName + "님이 정답을 맞췄습니다.\n" + "다음판을 시작하려면 시작을 눌러주세요");
                            gamestart = 0;
                            WriteAll3("null");

                        }
                    }

                    else if(cm.code.matches("400")) {
                        Logout();
                        break;
                    }

                    else { // 일반 채팅 메시지
                        UserStatus = "O";
                        //WriteAll(msg + "\n"); // Write All
                        WriteAllObject(cm);
                    }
                } //code 200 끝

                else if(cm.code.matches("1000")) { //시간이 끝났을때 턴 넘기기(이때 힌트사용시 시간 증가하는거도 구현(?))
                    //
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

    public void Login() {
        AppendText("새로운 참가자 " + UserName + " 입장.");
        WriteOne("Welcome to Catchmind game\n");
        WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
        String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
        //WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
    }

    public void Logout() {
        String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
        UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
        WriteAll(msg); // 나를 제외한 다른 User들에게 전송

        AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
        //WriteAll6("사용자가 갑작스럽게 게임을 나갔습니다. 게임을 잠시 중단합니다. 게임을 다시 시작하시려면 다시 시작을 눌러주세요!");
        //gamestart = 0;
    }

    public void Logout2() {
        // String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
        UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
    }

    // 모든 User들에게 방송. 각각의 UserService Thread의 WriteOne() 을 호출한다.
    public void WriteAll(String str) {
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elements();
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void WriteAll2(String str) { //WriteOne2로 변경하기
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elements();
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void WriteAll3(String str) { //WriteOne3로 변경하기
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void WriteAll4(String str) { //WriteOne4로 변경하기
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void WriteAll5(String str) {
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }

    public void WriteAll6(String str) { //WriteOne6으로 변경하기
        for (int i = 0; i < user_vc.size(); i++) {
            GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
            //GameRoom user = (GameRoom) user_vc.elementAt(i);
            if (user.UserStatus == "O")
                user.WriteOne(str);
        }
    }








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

    public void WriteOne1(String msg) { //room에서 chat 주고받는
        try {
            // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
            ChatMsg obcm = new ChatMsg("SERVER", "603", msg);
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
                // TODO Auto-generated atch block
                e1.printStackTrace();
            }
//            Logout(); // 에러가난 현재 객체를 벡터에서 지운다
        }
    }

    public void WriteOne2(String msg) { //
        try {
            // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
            ChatMsg obcm = new ChatMsg("SERVER", "603", msg);
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
                // TODO Auto-generated atch block
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

    /*public void WriteOthers(String str) {
        for (int i = 0; i < user_vc.size(); i++) {
            UserService user = (UserService) user_vc.elementAt(i);
            if (user != this && user.UserStatus == "O")
                user.WriteOne(str);
        }
    }*/

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




}
