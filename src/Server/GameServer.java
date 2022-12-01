package Server;

import Client.GameClientMain;
import Client.GameClientView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;

public class GameServer extends JFrame {
    public GameClientMain mainView;

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
    String totalRoomList="";
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    public int gameStart = 0; //시작시 1로 변경하기


//    public String[] wordFood = {"사과", "배", "떡볶이", "라면", "떡국", "파스타", "피자", "비빔밥", "소고기"};
    public String[] wordFood = {"사과", "배", "떡볶이", "라면"};
    public String[] wordMusic = {"Love Dive", "ASAP", "자격지심", "콘서트", "AntiFragile", "나의 X에게", "아이브", "잔나비", "Dynamite"};
    public String[] wordMovie = {"매드맥스", "인사이드 아웃", "인셉션", "아바타", "너의이름은", "극한직업", "겨울왕국", "스파이더맨", "공조"};
    public String[] wordAnimal = {"고양이", "호랑이", "햄스터", "강아지", "목도리도마뱀", "라쿤", "앵무새", "자라", "장수풍뎅이"};
    public String[] wordThing = {"옷장", "건조기", "노트북", "어항", "교탁", "자전거", "형광등", "장구", "나침반"};
    public HashMap<String, String[]> wordMap = new HashMap<>();
    public String turnUser;
    public int wordturn = 0;
    //public int gamestart = 0;// 게임 시작하면 1로(gameStart로 대체 일단 냅두기)
    public int UserScore = 0;
    public String UserName = "";
    //public String UserStatus;
    public int backgrounds = 0;

    Map<String, String> userInfo =  new HashMap<String, String>();


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
        wordMap.put("food", wordFood);
        wordMap.put("music", wordMusic);
        wordMap.put("movie", wordMovie);
        wordMap.put("animal", wordAnimal);
        wordMap.put("thing", wordThing);

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

        GameRoom curRoom = null;

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
                HintMsg hm = null;
                wordMsg wm = null;

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

                    if(rm.code.matches("800")){
                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(rm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }
//                        ChatMsg chatMsg = new ChatMsg("800", cm.roomId, cm.data);
//
//                        String curPresenter = cm.data;
//                        for(int i=0; i<curRoom.socketList.size(); i++){
//                            if(curRoom.memberList.get(i).equals(curPresenter)){
//                                GameServer.UserService user = (GameServer.UserService) curRoom.UserVec.get(Integer.parseInt(cm.data));
//                                user.WriteOneObject(chatMsg);
//                                GameServer.UserService nextUser = null;
//                                if(i >= curRoom.socketList.size()-1) nextUser = (GameServer.UserService) UserVec.elementAt(0);
//                                else nextUser = (GameServer.UserService) UserVec.elementAt(i+1);
//                                user.WriteOneObject(chatMsg);
//                            }
//                        }
//                        wordMsg newWordMsg = new wordMsg("800", cm.roomId);
//                        newWordMsg.presenterIndex = wm.presenterIndex+1;

//                        WriteRoomObject(curRoom, wm.code, newWordMsg);


                        String[] data = rm.data.split(" ");
                        int presenterIndex = Integer.parseInt(data[0]);
                        int wordIndex = Integer.parseInt(data[1]);

                        int newPresenterIndex = 0;
                        int newWordIndex = 0;

                        if(presenterIndex+1 >= curRoom.socketList.size()) newPresenterIndex = 0;
                        else newPresenterIndex = presenterIndex+1;

                        if(newWordIndex+1 >= wordMap.get(curRoom.subject).length) newWordIndex = 0;
                        else newWordIndex = wordIndex+1;

                        curRoom.presenterIndex = newPresenterIndex;
                        curRoom.wordIndex = newWordIndex;

                        UserService user = (UserService) curRoom.socketList.get(newPresenterIndex);
                        System.out.println("server 800 msg:" +  String.valueOf(newPresenterIndex) + " "+ String.valueOf(newWordIndex));
                        Client.RoomMsg changeMsg = new Client.RoomMsg( "800", String.valueOf(newPresenterIndex) + " "+ String.valueOf(newWordIndex));
                        WriteRoomObject(curRoom, changeMsg.code ,changeMsg);

//                        curRoom.socketList.get(newPresenterIndex).WriteOneObject(changeMsg);
//                        user.WriteOneObject(changeMsg);
//                        WriteRoomObject(curRoom, "800", changeMsg);
//

                    }
                }

                //방 입장 메세지일 경우 --------------------------------------------------------------
                if(obcm instanceof JoinMsg) {
                    jm = (JoinMsg) obcm;

                    //방 입장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if(jm.code.matches("1201")) {
                        String socketList = "";
                        String userList = "";
                        String charList = "";

                        mainView = jm.main;

                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(jm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }

                        Vector<UserService> sockets = new Vector<UserService>();
                        String[] users = jm.userList.split(" ");
                        Vector<String> chars = new Vector<String>();
                        for(int i=0; i< users.length; i++){
                            chars.add(userInfo.get(users[i]));
                        }

//                        String[] chars = jm.charList.split(" ");

                        for(int i=0; i<users.length; i++){
                            curRoom.UserVec.add(users[i]);
                        }

                        for(int i=0; i<userNameVec.size(); i++){
                            for(int j=0; j<users.length; j++){
                                if(users[j].equals(userNameVec.get(i))){
                                    sockets.add((UserService) user_vc.get(i));
                                }
                            }
                        }


                        for(int i=0; i<sockets.size(); i++){
                            curRoom.socketList.add(sockets.get(i));
                            curRoom.memberList.add(users[i]);

                            curRoom.charList.add(chars.get(i));
                        }

                        curRoom.presenterIndex = 0;

                        if(curRoom.memberList.size() != 0){
                            for (int i = 0; i < curRoom.socketList.size(); i++) {
                                socketList += curRoom.socketList.get(i) + " ";
                                userList += curRoom.memberList.get(i) + " ";
                                charList += curRoom.charList.get(i) + " ";
                            }
                        }


                        JoinMsg joinMsg = new JoinMsg("1201", jm.roomId, socketList, userList, charList, "", "");
                        joinMsg.presenterIndex = 0;
                        if(curRoom.subject.equals("food")){
                            for(int i=0; i< wordFood.length; i++){
                                if(joinMsg.wordList == null) joinMsg.wordList = wordFood[i] + " ";
                                else joinMsg.wordList += wordFood[i] + " ";
                            }
                        }
                        else  if(curRoom.subject.equals("music")){
                            for(int i=0; i< wordMusic.length; i++){
                                if(joinMsg.wordList == null) joinMsg.wordList = wordMusic[i] + " ";
                                else joinMsg.wordList += wordMusic[i] + " ";
                            }
                        }
                        else  if(curRoom.subject.equals("movie")){
                            for(int i=0; i< wordMovie.length; i++){
                                if(joinMsg.wordList == null) joinMsg.wordList = wordMovie[i] + " ";
                                else joinMsg.wordList += wordMovie[i] + " ";
                            }
                        }
                        else  if(curRoom.subject.equals("animal")){
                            for(int i=0; i< wordAnimal.length; i++){
                                if(joinMsg.wordList == null) joinMsg.wordList = wordAnimal[i] + " ";
                                else joinMsg.wordList += wordAnimal[i] + " ";
                            }
                        }
                        else  if(curRoom.subject.equals("thing")){
                            for(int i=0; i< wordThing.length; i++){
                                if(joinMsg.wordList == null) joinMsg.wordList = wordThing[i] + " ";
                                else joinMsg.wordList += wordThing[i] + " ";
                            }
                        }

                        WriteRoomObject(curRoom, jm.code, joinMsg);
                    }
                }

                //채팅 메세지일 경우 -------------------------------------------------------------- (다른 클래스에서 구현할 가능성 있음)
                if (obcm instanceof Server.ChatMsg) { //obcm(읽어들인 object)이 ChatMsg라면
                    cm = (Server.ChatMsg) obcm; //ChatMsg 형식으로 바꿔서
//                    AppendObject(cm);
                    System.out.println(cm.code);

                    //로그인!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if (cm.code.matches("100")) {
                        UserName = cm.UserName;
                        if (gameStart == 1) { //게임이 진행중일시
//                            WriteOne6("게임중에는 접속할 수 없습니다!");
                            Logout2();
                            break;
                        } else {
                            UserStatus = "O"; // Online 상태
                            Login(cm.char_no); //~님 환영합니다 msg 출력되도록 하기
                        }
                    } else if (cm.code.matches("200")) {

                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(cm.roomId))
                                curRoom = RoomVec.get(i);
                        }

                        msg = String.format("[%s] %s", cm.UserName, cm.data);
                        AppendText(msg); // server F 출력
                        String[] args = msg.split(" "); // 단어들을 분리한다
                        //Object[] word;
                        if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
                            UserStatus = "O";
                        } else {
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(cm.roomId))
                                    curRoom = RoomVec.get(i);
                            }
                            if (cm.isStart == false) {
                                cm = new ChatMsg(cm.UserName, "200", cm.data); //그냥 채팅
                                WriteRoomObject(curRoom, cm.code, cm);
                            } else {
                                boolean isAnswer = false;
                                String[] subjectWordList = wordMap.get(curRoom.subject);
                                for(int i=0; i<subjectWordList.length; i++){
                                    if(cm.data.equals(subjectWordList[i])) {
                                        isAnswer = true;
                                    }
                                }

                                if(isAnswer)
                                    cm = new ChatMsg(cm.UserName, "201", cm.data); //정답
                                else
                                    cm = new ChatMsg(cm.UserName,"202", cm.data);

                                WriteRoomObject(curRoom, cm.code, cm);
                            }

                        }
                    }
//                        else if ((cm.data.equals(word[wordturn])) && (gamestart == 1)) {
//                            if (UserName != turnUser) {
//                                WriteAllObject(cm);
//                                UserScore++;
//
//                                if (backgrounds == 1) {
//                                    // 전체에 배경 사진 보내기
////                                    ChatMsg obcm6799 = new ChatMsg("SERVER", "301", word[wordturn]);
//
//                                    for (int j = 0; j < user_vc.size(); j++) {
//                                        GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(j);
//                                        if (turnUser == user.UserName) {
////                                            user.WriteOneObject(obcm6799); // WriteAll로 바꾸기
//                                        }
//                                    }                                 // 301을 보내서 모두에게 300(이미지)를 보내기
//                                    backgrounds = 0;
//                                }
//
//                                if (wordturn == 4) {
//                                    wordturn = 0;
//                                } else {
//                                    wordturn++;
//                                }
////                                ChatMsg obcm67 = new ChatMsg("SERVER", "701", msg);
////                                WriteAllObject(obcm67);
//
//                                //WriteAll2("------------------사용자 목록------------------- ");
//                                for (int j = 0; j < user_vc.size(); j++) {
//                                    GameServer.UserService user = (GameServer.UserService) user_vc.elementAt(j);
//                                    WriteAll2(
//                                            "Name:  " + user.UserName + "           Score:  " + UserScore + "\n");
//                                }
//
//                                WriteAll6(UserName + "님이 정답을 맞췄습니다.\n" + "다음판을 시작하려면 시작을 눌러주세요");
//                                gamestart = 0;
//                                WriteAll3("null");
//                            }
//                        }

                    else if (cm.code.matches("400")) {
                        Logout();
                        break;
                    } else if (cm.code.matches("600")) {
                        //시간 뿌려주고
                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }

                        wordMsg newWordMsg = new wordMsg("600", cm.roomId);
                        newWordMsg.presenterIndex = 0;

                        WriteRoomObject(curRoom, cm.code, newWordMsg);

                        // 1. 출제자한테 메세지 1:1로 전송
                        Client.RoomMsg changeMsg = new Client.RoomMsg("800", String.valueOf(curRoom.presenterIndex) + " " + String.valueOf(curRoom.wordIndex));
                        WriteRoomObject(curRoom, changeMsg.code, changeMsg);
//                        curRoom.socketList.get(curRoom.presenterIndex).WriteOneObject(changeMsg);

                    }

                    //게임창에서 방나가기 버튼 눌렀을때
                    else if (cm.code.matches("700")) {
                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }

                        curRoom.UserVec.clear();
                        ChatMsg chatMsg = new ChatMsg(cm.UserName, "700", "null");
                        chatMsg.roomId = cm.roomId;
                        WriteRoomObject(curRoom, cm.code, chatMsg);
                    }

//                    else if(cm.code.matches("800")){ //출제자 바꾸기
//                        for (int i = 0; i < RoomVec.size(); i++) {
//                            if (RoomVec.get(i).roomId.equals(cm.roomId)) {
//                                curRoom = RoomVec.get(i);
//                            }
//                        }
////                        ChatMsg chatMsg = new ChatMsg("800", cm.roomId, cm.data);
////
////                        String curPresenter = cm.data;
////                        for(int i=0; i<curRoom.socketList.size(); i++){
////                            if(curRoom.memberList.get(i).equals(curPresenter)){
////                                GameServer.UserService user = (GameServer.UserService) curRoom.UserVec.get(Integer.parseInt(cm.data));
////                                user.WriteOneObject(chatMsg);
////                                GameServer.UserService nextUser = null;
////                                if(i >= curRoom.socketList.size()-1) nextUser = (GameServer.UserService) UserVec.elementAt(0);
////                                else nextUser = (GameServer.UserService) UserVec.elementAt(i+1);
////                                user.WriteOneObject(chatMsg);
////                            }
////                        }
////                        wordMsg newWordMsg = new wordMsg("800", cm.roomId);
////                        newWordMsg.presenterIndex = wm.presenterIndex+1;
//
////                        WriteRoomObject(curRoom, wm.code, newWordMsg);
//
//
//                        String[] data = cm.data.split(" ");
//                        int presenterIndex = Integer.parseInt(data[0]);
//                        int wordIndex = Integer.parseInt(data[1]);
//
//                        int newPresenterIndex = 0;
//                        int newWordIndex = 0;
//
//                        if(presenterIndex+1 >= curRoom.socketList.size()) newPresenterIndex = 0;
//                        else newPresenterIndex = presenterIndex+1;
//
//                        if(newWordIndex+1 >= wordMap.get(curRoom.subject).length) newWordIndex = 0;
//                        else newWordIndex = wordIndex+1;
//
//                        curRoom.presenterIndex = newPresenterIndex;
//                        curRoom.wordIndex = newWordIndex;
//
//                        UserService user = (UserService) curRoom.socketList.get(newPresenterIndex);
//                        System.out.println("server 800 msg:" +  String.valueOf(newPresenterIndex) + " "+ String.valueOf(newWordIndex));
//                        ChatMsg changeMsg = new ChatMsg(UserName, "800", String.valueOf(newPresenterIndex) + " "+ String.valueOf(newWordIndex));
//                        WriteRoomObject(curRoom, changeMsg.code ,changeMsg);
//
////                        curRoom.socketList.get(newPresenterIndex).WriteOneObject(changeMsg);
////                        user.WriteOneObject(changeMsg);
////                        WriteRoomObject(curRoom, "800", changeMsg);
////
//                    }
//
////                        else { // 일반 채팅 메시지
////                            UserStatus = "O";
////                            //WriteAll(msg + "\n"); // Write All
////                            WriteAllObject(cm);
////                        }

                    //로그아웃!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    else if (cm.code.matches("400")) { // logout message 처리
                        Logout();
                        break;
                    }

                    else if (cm.code.matches("500")) {

                        for (int i = 0; i < RoomVec.size(); i++) {
                            if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                curRoom = RoomVec.get(i);
                            }
                        }
                        WriteRoomObject(curRoom, cm.code, cm);

                    }
                }

                if(obcm instanceof  Server.wordMsg){
                    wm = (Server.wordMsg) obcm;

//                    if(wm.code.matches("800")){ //출제자 바꾸기
//                        for (int i = 0; i < RoomVec.size(); i++) {
//                            if (RoomVec.get(i).roomId.equals(wm.roomId)) {
//                                curRoom = RoomVec.get(i);
//                            }
//                        }
//                        String curPresen
//                        wordMsg newWordMsg = new wordMsg("800", wm.roomId);
//                        newWordMsg.presenterIndex = wm.presenterIndex+1;
//
//                        WriteRoomObject(curRoom, wm.code, newWordMsg);
//                    }
                }


                //힌트 메세지 일때.
                if (obcm instanceof Server.HintMsg) { //obcm(읽어들인 object)이 HintMsg 라면
                    hm = (Server.HintMsg) obcm;
                    //1000~1005 힌트&보너스 구현
                    if(hm.code.matches("1000")) { //첫글자(ex.코끼리/ 코) //일단 동물만 구현

                        HashMap<String, String[]> wordMap = new HashMap<>();
                        wordMap.put("food", wordFood);
                        wordMap.put("music", wordMusic);
                        wordMap.put("movie", wordMovie);
                        wordMap.put("animal", wordAnimal);
                        wordMap.put("thing", wordThing);
                        if(turnUser != cm.UserName && gameStart == 1) {

                            String arg = wordFood[wordturn];
                            String arg1 = wordMusic[wordturn];
                            String arg2 = wordMovie[wordturn];
                            String arg3 = wordAnimal[wordturn];
                            String arg4 = wordThing[wordturn];
                            //  String initial = wordMap.get(curRoom.subject)[hm.wordIndex];

                            String str = "첫번째 글자는 ' " + arg.charAt(0) + " ' 입니다.";
                            HintMsg newHintMsg = new HintMsg("SERVER", "1000", str);
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(hm.roomId)) {
                                    curRoom = RoomVec.get(i);
                                }
                            }
//                            WriteRoomObject(curRoom, hm.code, newHintMsg);
//                            Logout();
                        }
                    }

                    else if(hm.code.matches("1001")) { //시간 60초로 초기화
                        if(turnUser != cm.UserName && gameStart == 1) {
                            //
                            String str = "시간이 60초로 증가했습니다.";
                            HintMsg newHintMsg = new HintMsg("SERVER", "1001", str);
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                    curRoom = RoomVec.get(i);
                                }
                            }
                            WriteRoomObject(curRoom, hm.code, newHintMsg);
                            //Logout();
                        }
                    }

                    else if(hm.code.matches("1002")) { //배경그림 보여주기
                        if(turnUser.equals(UserName)) {
                            backgrounds = 1;
                            String arg = wordAnimal[wordturn];

                            HintMsg newHintMsg = new HintMsg("SERVER", "1002", arg);
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                    curRoom = RoomVec.get(i);
                                }
                            }
                            WriteRoomObject(curRoom, hm.code, newHintMsg);
                            Logout();
                        }
                    }

                    else if(hm.code.matches("1003")) { //글자수 힌트
                        if(turnUser.equals(UserName)) {

                            String arg = wordFood[wordturn];
                            String str = "글자수는 " + arg.length() + "입니다";
                            HintMsg newHintMsg = new HintMsg("SERVER", "1003", str);
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                    curRoom = RoomVec.get(i);
                                }
                            }
                            WriteRoomObject(curRoom, hm.code, newHintMsg);
                            Logout();
                        }
                    }

                    else if(hm.code.matches("1004")) { //정답시 점수 두배
                        if(turnUser.equals(UserName)) {
                            String str = "맞추면 점수 2배!";
                            HintMsg newHintMsg = new HintMsg("SERVER", "1004", str);
                            for (int i = 0; i < RoomVec.size(); i++) {
                                if (RoomVec.get(i).roomId.equals(cm.roomId)) {
                                    curRoom = RoomVec.get(i);
                                }
                            }
                            WriteRoomObject(curRoom, hm.code, newHintMsg);
                            Logout();
                        }
                    }

                    else if(hm.code.matches("1005")) { // 초성

                    }

                } //if
            } //while
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

            for(int i=0; i<RoomVec.size(); i++){
                totalRoomList += RoomVec.get(i).roomId;
            }

//            RoomMsg roomMsg = new RoomMsg("1200", gameRoom.roomId, title, subject, cnt);
            Server.RoomMsg roomMsg = new Server.RoomMsg("1200", totalUserList, roomIdList, roomTitleList, roomSubjectList, roomCntList);
            roomMsg.totalRoomList = totalRoomList;
            WriteAllObject(roomMsg);
//            WriteAllObject(roomMsg);
        }

//        public void joinRoom(String roomId, String title, String subject, int cnt){
//            RoomMsg roomMsg = new RoomMsg("1201", roomIdList, roomTitleList, roomSubjectList, roomCntList);
//            WriteOneObject(roomMsg);
//        }

        //code가 100일때 Login함수 호출
        public void Login(String char_no) {
            String totalUserList = "";
            for(int i=0; i<user_vc.size(); i++){
                totalUserList += user_vc.get(i).toString() + " ";
            }
            userInfo.put(UserName , char_no);

            AppendText("새로운 참가자 " + UserName + " 입장.");
            WriteOne("Welcome to CatchMind Server\n");
            WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
            Server.RoomMsg roomMsg = new Server.RoomMsg("100", totalUserList, roomIdList, roomTitleList, roomSubjectList, roomCntList);
            WriteOneObject(roomMsg);
            String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
            userNameVec.add(UserName);
//            WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.

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

        public void WriteRoomObject(GameRoom room, String code, Object ob) {
            for (int i = 0; i < room.socketList.size(); i++) {
                GameServer.UserService user = (GameServer.UserService) room.socketList.get(i);
                user.WriteOneObject(ob);
            }
            if(code.matches("700")) {
                room.socketList.clear();
                room.memberList.clear();
                this.curRoom.socketList = room.socketList;
                this.curRoom.memberList = room.memberList;
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
                ChatMsg obcm = new ChatMsg("SERVER", "100", msg);
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


        public void WriteOne5(String msg) {
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1000", msg);
                oos.writeObject(obcm);
                Logout();
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
                ; // 에러가난 현재 객체를 벡터에서 지운다
            }
        }

        public void WriteOne5_1(String msg) {

            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1001", msg);
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

        public void WriteOne5_2(String msg) {  //1002 배경사진 뿌려주는 메서드(문제 그리는 사람한테만 활성화할것)
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1002", msg);
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

        public void WriteOne5_3(String msg) {
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1003", msg);
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

        public void WriteOne5_4(String msg) {
            try {
                // dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
                ChatMsg obcm = new ChatMsg("SERVER", "1004", msg);
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


        public void WriteOne5_5(String msg) {
            try {
                ChatMsg obcm = new ChatMsg("SERVER", "1005", msg);
                oos.writeObject(obcm);
            } catch (IOException e) {
                AppendText("dos.writeObject() error");
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

//        public void WriteOne11(String msg) {
//            try {
//                // dos.writeUTF(msg);
////				byte[] bb;
////				bb = MakePacket(msg);
////				dos.write(bb, 0, bb.length);
//                ChatMsg obcm = new ChatMsg("SERVER", "1002", msg);
//                oos.writeObject(obcm);
//            } catch (IOException e) {
//                AppendText("dos.writeObject() error");
//                try {
////					dos.close();
////					dis.close();
//                    ois.close();
//                    oos.close();
//                    client_socket.close();
//                    client_socket = null;
//                    ois = null;
//                    oos = null;
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
//            }
//        }


        // 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteAll(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOne(str);
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
//                if (user != this && user.UserStatus == "O")
//                    user.WriteOne6(str);
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
                ChatMsg obcm = new ChatMsg("귓속말", "100" , msg);
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
