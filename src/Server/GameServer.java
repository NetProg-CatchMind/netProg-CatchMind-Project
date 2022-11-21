package Server;

import Client.GameClientView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;

public class GameServer extends JFrame {

    //필요한 변수들 선언하기
    private static final long serialVersionUID = 1L;

    // UI 설정 변수==================================================================================================
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    // 소켓 설정 변수==================================================================================================
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓

    private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
    private Vector userNameVec = new Vector();
    private Vector<GameRoom> RoomVec = new Vector<GameRoom>(); //생성된 방을 저장할 벡터.
    String roomIdList = "";
    String roomTitleList = "";
    String roomSubjectList = "";
    String roomCntList ="";
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    public int gameStart = 0; //시작시 1로 변경하기
    public String argp[] = { "사과", "오징어", "사람", "나무", "토끼" }; //제시어 뿌리는 배열,,다른 방식 생각해볼것



    public String word[] = {"새", "나무", "사람"}; //단어 서버에서 어떤 방식으로 뿌릴지 정하기,, -> 더미 데이터로 처리
    public String turnUser;
    public int wordturn = 0;
    public int gamestart = 0;// 게임 시작하면 1로
    public int UserScore = 0;
    public String UserName = "";
    //public String UserStatus;
    public int backgrounds = 0;


    public static void main(String[] args) {
        //실행시키는 코드
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameServer frame = new GameServer();
                    frame.setVisible(true);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    } //main끝

    //server Frame ==================================================================================================
    public GameServer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 298);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(13, 318, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText("30000");
        txtPortNumber.setBounds(112, 318, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
                } catch (NumberFormatException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                AppendText("Chat Server Running..");
                btnServerStart.setText("Chat Server Running..");
                btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
                txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
                AcceptServer accept_server = new AcceptServer();
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 356, 300, 35);
        contentPane.add(btnServerStart);
    } //GameServer 끝


    // user가 접속했을때 (client가 accept하기 위해) 실행되는 thread =========================================================
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while(true) {
                try {
                    AppendText("Waiting new client...");
                    client_socket = socket.accept(); //accept가 일어날때까지 무한대기
                    AppendText("새로운 참가자 from " + client_socket);

                    UserService new_user = new UserService(client_socket); // User 당 하나씩 Thread 생성
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                    AppendText("현재 참가자 수 " + UserVec.size());
                } catch (IOException e) {
                    AppendText("accept() error");
                    // System.exit(0);
                }
            }
        }
    }

    public void AppendText(String str) {
        // textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
        //msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        int len = textArea.getDocument().getLength();
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }


    //user 당 하나씩 생성될 user를 다루기 위한 thread ====================================================================
    class UserService extends Thread {
        private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Socket client_socket;
        private Vector user_vc;


        public String UserName = "";
        public String UserStatus;

        // userThread 클래스 생성자-----------------------------------------------------------------
        public UserService(Socket client_socket) {
            // TODO Auto-generated constructor stub
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.user_vc = UserVec;

            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream()); //output Stream 세팅 (초기화)
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream()); //input Stream 세팅 (초기화)

                // line1 = dis.readUTF();
                // /login user1 ==> msg[0] msg[1]
//				byte[] b = new byte[BUF_LEN];
//				dis.read(b);
//				String line1 = new String(b);
//
//				//String[] msg = line1.split(" ");
//				//UserName = msg[1].trim();
//				UserStatus = "O"; // Online 상태
//				Login();
            } catch (Exception e) {
//                AppendText("userService error");
            }
        }

        // userThread 클래스 메인-----------------------------------------------------------------
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문 / 포트번호에 따라 if문 쓰기

                Object obcm = null;
                String msg = null;
                Client.RoomMsg rm = null;
                JoinMsg jm = null;
                ChatMsg cm = null;

                if (socket == null)
                    break;

                try {
                    obcm = ois.readObject(); //object 읽어들이기
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (obcm == null){
                    break;
                }

                //방만들기 메세지일 경우 --------------------------------------------------------------
                if(obcm instanceof Client.RoomMsg){
                    rm = (Client.RoomMsg) obcm;

                    //방만들기!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if(rm.code.matches("1200")){
                        makeRoom(rm.roomId, rm.roomTitle, rm.roomSubject, rm.userCnt);
                    }
                }

                //방 입장 메세지일 경우 --------------------------------------------------------------
                if(obcm instanceof JoinMsg) {
                    jm = (JoinMsg) obcm;

                    //방 입장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if(jm.code.matches("1201")) {
                        String socketList = "";
                        String userList = "";
                        String charList = "" ;
                        GameRoom curRoom = null;
                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(jm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }

//                        curRoom.socketList.add(this);
//                        curRoom.memberList.add(jm.username);
//                        curRoom.charList.add(jm.char_no);


//                        String[] sockets_b = jm.socketList.split(" ");
                        Vector<UserService> sockets = new Vector<UserService>();
                        String[] users = jm.userList.split(" ");

//                        String[] chars = jm.charList.split(" ");

                        for(int i=0; i<users.length; i++){
                            curRoom.UserVec.add(users[i]);
                        }

                        System.out.println(user_vc);

                        for(int i=0; i<userNameVec.size(); i++){
                            for(int j=0; j<users.length; j++){
                                System.out.println("users :: " + userNameVec.get(i) +"/" + users[j]);
                                if(users[j].equals(userNameVec.get(i))){
                                    sockets.add((UserService) user_vc.get(i));
                                }
                            }
                        }

                        for(int i=0; i<sockets.size(); i++){
                            curRoom.socketList.add(sockets.get(i));
                            curRoom.memberList.add(users[i]);
//                            curRoom.charList.add(chars[i]);
                        }

//                        if(curRoom.memberList.size() != 0){
//                            for (int i = 0; i < curRoom.socketList.size(); i++) {
//                                socketList += curRoom.socketList.get(i) + " ";
//                                userList += curRoom.memberList.get(i) + " ";
////                                charList += curRoom.charList.get(i) + " ";
//                            }
//                        }

                        System.out.println(curRoom.UserVec);
                        JoinMsg joinMsg = new JoinMsg("1201", jm.roomId, jm.socketList, jm.userList, jm.charList, "", "");
                        WriteRoomObject(curRoom, joinMsg);
                    }
                }

                //채팅 메세지일 경우 -------------------------------------------------------------- (다른 클래스에서 구현할 가능성 있음)
                if (obcm instanceof ChatMsg) { //obcm(읽어들인 object)이 ChatMsg라면

                    cm = (ChatMsg) obcm; //ChatMsg 형식으로 바꿔서
//                    AppendObject(cm);

                    //로그인!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if (cm.code.matches("100")) {
                        UserName = cm.UserName;
                        if (gameStart == 1) { //게임이 진행중일시
                            WriteOne6("게임중에는 접속할 수 없습니다!");
                            Logout2();
                            break;
                        } else {
                            UserStatus = "O"; // Online 상태
                            Login(); //~님 환영합니다 msg 출력되도록 하기
                        }
                    }

                    else if (cm.code.matches("200")) {
                        msg = String.format("[%s] %s", cm.UserName, cm.data);
                        AppendText(msg); // server F 출력
                        String[] args = msg.split(" "); // 단어들을 분리한다.
                        //Object[] word;
                        if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
                            UserStatus = "O";
                        } else if (args[1].matches("/exit")) {
//                          Logout();
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
                                    }                                 // 301을 보내서 모두에게 300(이미지)를 보내기
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
                                            "Name:  " + user.UserName + "           Score:  " + UserScore + "\n");
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


                    //로그아웃!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    else if (cm.code.matches("400")) { // logout message 처리
                        Logout();
                        break;
                    } else { // 300, 500, ... 기타 object는 모두 방송한다.
                        WriteAllObject(cm);
                    }

                }
            }
        } // run

        public void makeRoom(String roomId, String title, String subject, int cnt){
            GameRoom gameRoom = new GameRoom(roomId, title, subject, cnt, socket , client_socket);

            RoomVec.add(gameRoom);

            String totalUserList = "";
            for(int i=0; i<userNameVec.size(); i++){
                totalUserList += userNameVec.get(i).toString() + " ";
            }

            roomIdList += (gameRoom.roomId + " ");
            roomTitleList += (gameRoom.title + " ");
            roomSubjectList += (gameRoom.subject + " ");
            roomCntList += (gameRoom.memberCnt + " ");

//            RoomMsg roomMsg = new RoomMsg("1200", gameRoom.roomId, title, subject, cnt);
            Server.RoomMsg roomMsg = new Server.RoomMsg("1200", totalUserList, roomIdList, roomTitleList, roomSubjectList, roomCntList);
            WriteAllObject(roomMsg);
//            WriteAllObject(roomMsg);
        }

//        public void joinRoom(String roomId, String title, String subject, int cnt){
//            RoomMsg roomMsg = new RoomMsg("1201", roomIdList, roomTitleList, roomSubjectList, roomCntList);
//            WriteOneObject(roomMsg);
//        }

        //code가 100일때 Login함수 호출
        public void Login() {
            String totalUserList = "";
            for(int i=0; i<user_vc.size(); i++){
                totalUserList += user_vc.get(i).toString() + " ";
            }

            AppendText("새로운 참가자 " + UserName + " 입장.");
            WriteOne("Welcome to CatchMind Server\n");
            WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
            Server.RoomMsg roomMsg = new Server.RoomMsg("100", totalUserList, roomIdList, roomTitleList, roomSubjectList, roomCntList);
            WriteOneObject(roomMsg);
            String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
            userNameVec.add(UserName);
            WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.

        }

        public void Logout() {
            String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
            UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
            WriteAll(msg); // 나를 제외한 다른 User들에게 전송
            AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
        }

        public void Logout2() {
            // String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
            UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
        }

        public void WriteAllObject(Object ob) {
            for (int i = 0; i < UserVec.size(); i++) {

                GameServer.UserService user = (GameServer.UserService) UserVec.elementAt(i);
                user.WriteOneObject(ob);
            }
        }

        public void WriteRoomObject(GameRoom room, Object ob) {
            System.out.println("room socket List :: " + room.socketList);
            for (int i = 0; i < room.socketList.size(); i++) {
                GameServer.UserService user = (GameServer.UserService) room.socketList.get(i);
                System.out.println(room.socketList.get(i));
                user.WriteOneObject(ob);
            }
        }

        public void WriteOneObject(Object ob) {
            try {
                oos.writeObject(ob);
            }
            catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
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
                Logout();
            }
        }

        public void WriteOthers(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user != this && user.UserStatus == "O")
                    user.WriteOne(str);
            }
        }

        public void AppendObject(ChatMsg msg) {
            // textArea.append("사용자로부터 들어온 object : " + str+"\n");
            textArea.append("code = " + msg.code + "\n");
            textArea.append("id = " + msg.UserName + "\n");
            textArea.append("data = " + msg.data + "\n");
            textArea.setCaretPosition(textArea.getText().length());
        }

        // UserService Thread가 담당하는 Client 에게 1:1 전송 / 프로토콜 200
        public void WriteOne(String msg) {
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
                oos.writeObject(obcm);
            } catch (IOException e) {
                AppendText(" dos.writeObject() error");
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

        public void WriteAll6(String str) { //WriteOne6으로 변경하기
            for (int i = 0; i < user_vc.size(); i++) {
                GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(i);
                //GameRoom user = (GameRoom) user_vc.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOne(str);
            }
        }


        public void WriteOne2(String msg) { //사용자 목록 업데이트 방송 / 프로토콜 603
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
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne6(String msg) { //protocol 600, game start시
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "600", msg);
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
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne6_1(String msg) { //protocol 601 게임 상황 전달(?)
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "601", msg);
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
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne6_3(String msg) { //protocol 603 사용자 목록 업데이트
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
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }


        public void WriteOne7(String msg) { //protocol 700 퇴장
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "700", msg);
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
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne8(String msg) { //순서 변경시의 안내방송 / 프로토콜 800

            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "800", msg);
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
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne11(String msg) { //1100 배경사진 뿌려주는 메서드(문제 그리는 사람한테만 활성화할것)
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1100", msg);
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
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }


        // 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteAll(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOne(str);
            }
        }

        public void WriteAll2(String str) { //클라이언트에게 사용자 목록과 점수를 보여줌
            for(int i=0; i< user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if(user.UserStatus == "0")
                    user.WriteOne2(str);
            }
        }


        public void WriteAll3(String str) { //
            for(int i=0; i<user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if(user.UserStatus == "0")
                    user.WriteOne(str);
            }
        }

        // 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteAll4(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOne6_1(str);
            }
        }




        // 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.


        public void WriteOthersimg(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserName != this.UserName && user.UserStatus == "O")
                    user.WriteOneObject(ob);
            }
        }

        public void WriteOthers2(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user != this && user.UserStatus == "O")
                    user.WriteOne6(str);
            }
        }

        // Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
        public byte[] MakePacket(String msg) {
            byte[] packet = new byte[BUF_LEN];
            byte[] bb = null;
            int i;
            for (i = 0; i < BUF_LEN; i++)
                packet[i] = 0;
            try {
                bb = msg.getBytes("euc-kr");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (i = 0; i < bb.length; i++)
                packet[i] = bb[i];
            return packet;
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

//
//        public void WriteAll4(String str) {
//            for(int i=0; i<user_vc.size(); i++) {
//                UserService user = (UserService) user_vc.elementAt(i);
//                if(user.UserStatus == "0")
//                    user.WriteOne(str);
//            }
//        }
//
//
//

//
//        // Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
//        public byte[] MakePacket(String msg) {
//            byte[] packet = new byte[BUF_LEN];
//            byte[] bb = null;
//            int i;
//            for (i = 0; i < BUF_LEN; i++)
//                packet[i] = 0;
//            try {
//                bb = msg.getBytes("euc-kr");
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            for (i = 0; i < bb.length; i++)
//                packet[i] = bb[i];
//            return packet;
//        }
    }

}
