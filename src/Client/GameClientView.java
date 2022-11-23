package Client;

import Server.ChatMsg;
import Server.GameServer;
import Server.JoinMsg;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.net.*;
import java.util.*;
import java.util.List;

public class GameClientView extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    //    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");
    private ImageIcon backgroundImg = new ImageIcon("res/basicBackground.png");
    private ImageIcon logoImg = new ImageIcon("res/gameTitle.png");
    private JLabel userLabel;
    private ImageIcon viewPanelBg = new ImageIcon("res/gameViewPanel.png");
    private ImageIcon infoPanelBg = new ImageIcon("res/infoPanelImg.png");
    private ImageIcon hintPanelBg = new ImageIcon("res/hintPanelImg.png");
    private ImageIcon chatPanelBg = new ImageIcon("res/chatPanelImg.png");

    private ImageIcon bgItemImg = new ImageIcon("res/item_backImage.png");
    private ImageIcon mouseOverBgItem = new ImageIcon("res/mouseOverBgItem.png");
    private ImageIcon timeItemImg = new ImageIcon("res/item_time.png");
    private ImageIcon mouseOvertimeItem = new ImageIcon("res/mouseOverItemTime.png");
    private ImageIcon initialItemImg = new ImageIcon("res/item_initialChar.png");
    private ImageIcon mouseOverInitialItem = new ImageIcon("res/mouseOverItemInitial.png");
    private ImageIcon wordCountItemImg = new ImageIcon("res/item_wordCount.png");
    private ImageIcon mouseOverWordCntItem = new ImageIcon("res/mouseOverItemWordCnt.png");
    private ImageIcon twiceScoreItemImg = new ImageIcon("res/item_twiceScore.png");
    private ImageIcon mouseOverScoreItem = new ImageIcon("res/mouseOverItemScore.png");
    private ImageIcon categoryItemImg = new ImageIcon("res/item_showCategory.png");
    private ImageIcon mouseOverCategoryItem = new ImageIcon("res/mouseOverItemCategory.png");

    private ImageIcon char1Img = new ImageIcon("res/character1.png");
    private ImageIcon char2Img = new ImageIcon("res/character2.png");
    private ImageIcon char3Img = new ImageIcon("res/character3.png");
    private JPanel contentPane;
    private JPanel mainPanel;

    private JPanel infoPanel;
    private JPanel hintPanel;
    private JPanel canvasPanel;
    private JPanel chatingPanel;
    private JPanel usersPanel;
    private JLabel logo;


    private static final long serialVersionUID = 1L;

    private JTextField txtInput;
    private String UserName;
    private JButton btnSend;
    private JButton btnExit;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private JLabel lblUserName;

    private JPanel wordPanel;
    private JLabel wordLabel;
    private JLabel wordTitle;

    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JLabel scoreTitle;

    private JPanel timePanel;
    private JLabel timeLabel;
    private JLabel timeTitle;

    private JLabel lblNewLabel; //공지사항 알려주는 label
    //private JTextArea textArea;
    private JTextPane textArea;
    private JTextPane textArea1;

    private Frame frame;
    private FileDialog fd;
    private JButton imgBtn;

    JPanel panel; //뭐였지,,
    private JLabel lblMouseEvent;
    private Graphics gc;
    private int pen_size = 2; // minimum 2
    // 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
    private Image panelImage = null;
    private Graphics gc2 = null;
    public Color c = new Color(0,0,0);
    public int shapes;
    private LineBorder lb;
    public List<Point> pointss = new ArrayList<Point>();  //pointss arraylist
    public	MyMouseEvent mouse;
    public MyMouseWheelEvent wheel;

    public boolean linee = true;
    private JTextField textField;

//    private Vector userVec = new Vector();
    private String[] socketList;
    private String[] userList;
    private String[] charList;
    /**
     * Create the frame.
     *
     * @throws BadLocationException
     */

    public GameClientView(String roomId, String socketList, String userList, String charList, String username, Socket socket, ObjectInputStream ois, ObjectOutputStream oos ) {
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
//       setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
//        setUndecorated(true);

        this.socketList = socketList.split(" ");
        this.userList = userList.split(" ");
        this.charList = charList.split(" ");


        contentPane = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
                repaint();
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };

        contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        logo = new JLabel(logoImg);
        logo.setIcon(logoImg);
        logo.setBounds(40,0, 500,80);
        logo.revalidate();
        logo.repaint();
        contentPane.add(logo);

        mainPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(viewPanelBg.getImage(), 10, 10, 1480, 790, this);
                repaint();
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        mainPanel.revalidate();
        mainPanel.repaint();
        mainPanel.setOpaque(false);
        mainPanel.setBounds(0,20,1500, 800);
        mainPanel.setLayout(null);
        contentPane.add(mainPanel);


        usersPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        usersPanel.revalidate();
        usersPanel.repaint();
        usersPanel.setBounds(50,40,200,700);
//        usersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        usersPanel.setLayout(null);
//        usersPanel.setBackground(Color.white);
        usersPanel.setOpaque(false);
        mainPanel.add(usersPanel);

        for(int i=0; i<this.socketList.length; i++){
            System.out.println(this.charList[0]);
//            JPanel userPanel = new JPanel();
            userLabel = new JLabel();
            if(this.charList[i].equals("char1")) userLabel.setIcon(char1Img);
            else if(this.charList[i].equals("char2")) userLabel.setIcon(char2Img);
            else userLabel.setIcon(char3Img);
            userLabel.setBounds(50,35 + (i*160),90,110);
            usersPanel.add(userLabel);
        }

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.revalidate();
        scrollPane1.repaint();
        scrollPane1.setBounds(35, 20, 130, 130); //접속한 유저 정보창
        scrollPane1.setBorder(new TitledBorder(new LineBorder(Color.black),"user1"));
        usersPanel.add(scrollPane1);
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.revalidate();
        scrollPane2.repaint();
        scrollPane2.setBounds(35, 180, 130, 130); //접속한 유저 정보창
        scrollPane2.setBorder(new TitledBorder(new LineBorder(Color.black),"user2"));
        usersPanel.add(scrollPane2);
        JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.revalidate();
        scrollPane3.repaint();
        scrollPane3.setBounds(35, 340 , 130, 130); //접속한 유저 정보창
        scrollPane3.setBorder(new TitledBorder(new LineBorder(Color.black),"user3"));
        usersPanel.add(scrollPane3);
        JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.revalidate();
        scrollPane4.repaint();
        scrollPane4.setBounds(35, 500, 130, 130); //접속한 유저 정보창
        scrollPane4.setBorder(new TitledBorder(new LineBorder(Color.black),"user4"));
        usersPanel.add(scrollPane4);

        btnExit = new JButton("방 나가기"); //Exit button
        btnExit.revalidate();
        btnExit.repaint();
        btnExit.setFont(new Font("굴림", Font.PLAIN, 14));
        btnExit.setBounds(35, 660, 130, 30);
        usersPanel.add(btnExit);


        infoPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        infoPanel.revalidate();
        infoPanel.repaint();
        infoPanel.setBounds(250,50,800, 150);
        infoPanel.setLayout(null);
//        infoPanel.setBackground(Color.white);
        infoPanel.setOpaque(false);
        mainPanel.add(infoPanel);

        lb = new LineBorder(Color.black, 1, true);

        wordPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260 , 150, this);
                repaint();

                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        wordPanel.revalidate();
        wordPanel.repaint();
        wordPanel.setBounds(10,0,260,150);
        infoPanel.add(wordPanel);

        wordLabel = new JLabel("제시어."){}; //안내 공지하는 label
        wordLabel.revalidate();
        wordLabel.repaint();
        wordLabel.setForeground(Color.BLACK);
        wordLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
        wordLabel.setBorder(lb);
//        wordLabel.setBounds(10, 0, 260, 150);
        wordPanel.add(wordLabel);

        scorePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260, 150, this);
                repaint();

                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scorePanel.revalidate();
        scorePanel.repaint();
        scorePanel.setBounds(270,0,260,150);
        infoPanel.add(scorePanel);

        scoreLabel = new JLabel("점수."); //안내 공지하는 label
        scoreLabel.revalidate();
        scoreLabel.repaint();
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBorder(lb);
        scoreLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
//        scoreLabel.setBounds(270, 0, 260, 150);
        scorePanel.add(scoreLabel);

        timePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260, 150, this);
                repaint();

                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        timePanel.revalidate();
        timePanel.repaint();
        timePanel.setBounds(530,0,260,150);
        infoPanel.add(timePanel);

        timeLabel = new JLabel("제한시간."); //안내 공지하는 label
        timeLabel.revalidate();
        timeLabel.repaint();
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setBorder(lb);
        timeLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
//        timeLabel.setBounds(530, 0, 260, 150);
        timePanel.add(timeLabel);


        hintPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(hintPanelBg.getImage(), 0, 0, 380, 150, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        hintPanel.revalidate();
        hintPanel.repaint();
        hintPanel.setBounds(1050,50,380,150);
        hintPanel.setLayout(null);
        hintPanel.setOpaque(false);
        hintPanel.setBackground(Color.black);
        mainPanel.add(hintPanel);

        JButton bgItemBtn = new JButton(bgItemImg);
        bgItemBtn.revalidate();
        bgItemBtn.repaint();
        bgItemBtn.setBounds(50, 20, 80, 50);
        hintPanel.add(bgItemBtn);

        JButton timeItemBtn = new JButton(timeItemImg);
        timeItemBtn.revalidate();
        timeItemBtn.repaint();
        timeItemBtn.setBounds(150, 20, 80, 50);
        hintPanel.add(timeItemBtn);

        JButton initItemBtn = new JButton(initialItemImg);
        timeItemBtn.revalidate();
        timeItemBtn.repaint();
        initItemBtn.setBounds(250, 20, 80, 50);
        hintPanel.add(initItemBtn);

        JButton cntItemBtn = new JButton(wordCountItemImg);
        cntItemBtn.revalidate();
        cntItemBtn.repaint();
        cntItemBtn.setBounds(50, 80, 80, 50);
        hintPanel.add(cntItemBtn);

        JButton twiceItemBtn = new JButton(twiceScoreItemImg);
        twiceItemBtn.revalidate();
        twiceItemBtn.repaint();
        twiceItemBtn.setBounds(150, 80, 80, 50);
        hintPanel.add(twiceItemBtn);

        JButton ctgItemBtn = new JButton(categoryItemImg);
        ctgItemBtn.revalidate();
        ctgItemBtn.repaint();
        ctgItemBtn.setBounds(250, 80, 80, 50);
        hintPanel.add(ctgItemBtn);


        canvasPanel = new JPanel(){
            /* public void paintComponent(Graphics g) {
                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                 Image 영역이 가려졌다 다시 나타날 때 그려준다.
            } */
        };
        canvasPanel.revalidate();
        canvasPanel.repaint();
        canvasPanel.setBounds(250,200,800,550);
        canvasPanel.setLayout(null);
//        canvasPanel.setBackground(Color.gray);
        canvasPanel.setBackground(new Color(79, 121, 66));
        mainPanel.add(canvasPanel);

        panel = new JPanel();
        panel.revalidate();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBackground(Color.WHITE);
        panel.setBounds(20, 20, 760, 480); //그림판 판넬
        canvasPanel.add(panel);
        gc = panel.getGraphics();

        JButton btnNewButton1 = new JButton(".");
        btnNewButton1.setBackground(Color.RED);
        btnNewButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                c = new Color(255, 0, 0);
                startss();
            }
        });
        //btnNewButton1.revalidate();
        //btnNewButton1.repaint();
        btnNewButton1.setForeground(Color.RED);
        btnNewButton1.setBounds(50, 508, 62, 35);
        canvasPanel.add(btnNewButton1);


        JButton btnNewButton2 = new JButton(",");
        btnNewButton2.setBackground(Color.BLUE);
        btnNewButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 255);
                //startss();
            }
        });
        btnNewButton2.revalidate();
        btnNewButton2.repaint();
        btnNewButton2.setForeground(Color.BLUE);
        btnNewButton2.setBounds(122, 508, 62, 35);
        canvasPanel.add((btnNewButton2));


        JButton btnNewButton3 = new JButton(",");
        btnNewButton3.setBackground(Color.YELLOW);
        btnNewButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(255, 255, 0);
                //startss();
            }
        });
        btnNewButton3.revalidate();
        btnNewButton3.repaint();
        btnNewButton3.setForeground(Color.YELLOW);
        btnNewButton3.setBounds(194, 508, 62, 35);
        canvasPanel.add((btnNewButton3));


        JButton btnNewButton4 = new JButton(",");
        btnNewButton4.setBackground(Color.GREEN);
        btnNewButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(12, 134, 23);
                //startss();
            }
        });
        btnNewButton4.revalidate();
        btnNewButton4.repaint();
        btnNewButton4.setForeground(Color.GREEN);
        btnNewButton4.setBounds(266, 508, 62, 35);
        canvasPanel.add((btnNewButton4));

        JButton btnNewButton5 = new JButton(",");
        btnNewButton5.setBackground(Color.BLACK);
        btnNewButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 0);
                //startss();
            }
        });
        btnNewButton5.revalidate();
        btnNewButton5.repaint();
        btnNewButton5.setForeground(Color.BLACK);
        btnNewButton5.setBounds(338, 508, 62, 35);
        canvasPanel.add((btnNewButton5));


        JButton btnNewButton6 = new JButton("지우개"); //지우개버튼
        btnNewButton6.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton6.setBounds(410, 508, 85, 35);
        canvasPanel.add(btnNewButton6);
        btnNewButton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(255,255,255);
                //startss();
            }
        });
        btnNewButton6.revalidate();
        btnNewButton6.repaint();


        chatingPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(chatPanelBg.getImage(), 0, 0, 380, 550, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        chatingPanel.revalidate();
        chatingPanel.repaint();
//        chatingPanel.setBackground(Color.white);
        chatingPanel.setLayout(null);
        chatingPanel.setOpaque(false);
        chatingPanel.setBounds(1060,200, 380, 550);
        mainPanel.add(chatingPanel);

        JScrollPane scrollPane = new JScrollPane(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(chatPanelBg.getImage(), 0, 0, 380, 550, this);
                repaint();

                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scrollPane.revalidate();
        scrollPane.repaint();
        scrollPane.setBounds(20, 0, 340, 440); //채팅창
        scrollPane.setBackground(new Color(0,0,0,0));
        chatingPanel.add(scrollPane);
        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        imgBtn = new JButton("+"); //+버튼
        imgBtn.revalidate();
        imgBtn.repaint();
        imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
        imgBtn.setBounds(20, 450, 50, 40);
        chatingPanel.add(imgBtn);

        txtInput = new JTextField();
        txtInput.revalidate();
        txtInput.repaint();
        txtInput.setBounds(80, 450, 210, 40); //채팅 치는 area
        chatingPanel.add(txtInput);
        txtInput.setColumns(10);

        btnSend = new JButton("Send"); //send button
        btnSend.revalidate();
        btnSend.repaint();
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(300, 450, 70, 40);
        chatingPanel.add(btnSend);

//        textArea1 = new JTextPane();
//        textArea1.setEditable(true);
//        textArea1.setFont(new Font("굴림체", Font.PLAIN, 14));
//        scrollPane1.setViewportView(textArea1);
//        chatingPanel.add(textArea1);

        lblUserName = new JLabel("Name"); //이름 라벨
        lblUserName.revalidate();
        lblUserName.repaint();
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setBounds(20, 490, 90, 30);
        chatingPanel.add(lblUserName);
        setVisible(true);

        UserName = username;
        lblUserName.setText(username);

        JButton btnNewButton = new JButton("종 료"); //종료버튼
        btnNewButton.revalidate();
        btnNewButton.repaint();
        btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                    ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
//                    SendObject(msg);
//                    System.exit(0);
            }
        });
        btnNewButton.setBounds(300, 490, 70, 30);
        chatingPanel.add(btnNewButton);

        JButton btnNewButtonStart = new JButton("시 작"); //시작버튼
        btnNewButtonStart.revalidate();
        btnNewButtonStart.repaint();
        btnNewButtonStart.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButtonStart.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
//              SendObject(msg);
//              System.exit(0);
                ChatMsg obc = new ChatMsg(UserName, "600","null");
                SendObject(obc);
                //2명이상이면 start
            }

        });
        btnNewButtonStart.setBounds(150, 490, 80, 40);
        chatingPanel.add(btnNewButtonStart);

//         Image 영역 보관용. paint() 에서 이용한다.
        panelImage = createImage(panel.getWidth(), panel.getHeight());
        gc2 = panelImage.getGraphics();
        gc2.setColor(panel.getBackground());
        gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        gc2.setColor(Color.BLACK);
        gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);

        /*lblMouseEvent = new JLabel("<dynamic>"); //그림판 밑에 / 색깔팬이랑 지우개 추가하기
        lblMouseEvent.setHorizontalAlignment(SwingConstants.CENTER);
        lblMouseEvent.setFont(new Font("굴림", Font.BOLD, 14));
        lblMouseEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblMouseEvent.setBackground(Color.WHITE);
        lblMouseEvent.setBounds(160, 520, 450, 40);
        contentPane.add(lblMouseEvent);*/

        try {
            this.oos = oos;
            this.ois = ois;
            this.socket = socket;

            ListenNetwork new_room = new ListenNetwork(); // User 당 하나씩 Thread 생성
            new_room.start();

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    Object obcm = null;
                    String msg = null;
                    Server.JoinMsg jm;
                    ChatMsg cm;
                    try {
                        obcm = ois.readObject();
                        System.out.println(obcm);
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        break;
                    }
                    if (obcm == null)
                        break;

                    if(obcm instanceof Server.JoinMsg) {
                        jm = (Server.JoinMsg) obcm;

                        if(jm.code.matches("1201")){

                            System.out.println(jm);

                        }
                    }

                    if (obcm instanceof ChatMsg) {
                        cm = (ChatMsg) obcm;
                        msg = String.format("[%s]\n%s", cm.UserName, cm.data);
                    } else
                        continue;
                    switch (cm.code) {
                        case "200": // chat message
                            if (cm.UserName.equals(UserName))
                                AppendTextR(msg); // 내 메세지는 우측에
                            else
                                AppendText(msg);
                            break;

                        case "201": //정답 message
                            if(cm.UserName.equals(UserName))
                                AppendTextR(msg);
                            else
                                AppendText(msg);

                        case "202": //오답 message
                            if(cm.UserName.equals(UserName))
                                AppendTextR(msg);
                            else
                                AppendText(msg);

                        case "300": // Image 첨부
                            if (cm.UserName.equals(UserName))
                                AppendTextR("[" + cm.UserName + "]");
                            else
                                AppendText("[" + cm.UserName + "]");
                            AppendImage(cm.img);
                            break;
                        case "500": // Mouse Event 수신
                            DoMouseEvent(cm, cm.co, cm.shape);
                            startss();
                            break;

                        case"600":  //game start
                            String arg2[]=msg.split("]");
                            textField.setText(arg2[1]);
                            panel.removeAll();
                            panel.repaint();
                            panel.revalidate();
                            if(cm.data.matches("문제를 맞춰보세요")) {
                                endss();
                            }else {
                                startss();
                            }
                            break;

                        case "800": //순서 변경
                            String arg1[]=msg.split("]");
                            textField.setText(arg1[1]);
                            break;
                    }

                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
//						dos.close();
//						dis.close();
                        ois.close();
                        oos.close();
                        socket.close();

                        break;
                    } catch (Exception ee) {
                        break;
                    } // catch문 끝
                } // 바깥 catch문끝

            }
        }
    }


    public void startss() {
        mouse = new MyMouseEvent();
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        wheel = new MyMouseWheelEvent();
        panel.addMouseWheelListener(wheel);
    }

    public void endss() {
        //panel.removeAll();
        panel.removeMouseListener(mouse);
        panel.removeMouseMotionListener(mouse);
    }


    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(panelImage, 0, 0, this);
//        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//        gc.drawImage(panelImage, 0, 0, this);
    }




    // Mouse Event 수신 처리
    public void DoMouseEvent(ChatMsg cm,Color co,int shape) {
        //if (cm.UserName.matches(UserName)) // 본인 것은 이미 Local 로 그렸다.
          //  return;
        //System.out.println(cm.lines);

        gc.setColor(co);

        if(cm.lines==false) {
            pointss.clear();
            if(shape==1) {
                gc.drawOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2,cm.pen_size, cm.pen_size);
                shape=0;

            }else if(shape==3) {
                gc.drawRect(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
                shape=0;

            }else if(shape==0) {

                gc.fillRect(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);

            }
            shape=0;
        }else if(cm.lines==true) {

            pointss.add(cm.mouse_e.getPoint());

            if (pointss.size() >1 ) {

                Point p3 = pointss.get(0);
                Graphics2D g2=(Graphics2D)gc;

                g2.setStroke(new BasicStroke(cm.pen_size));

                for (int i = 1; i < pointss.size(); i++){
                    Point p4 = pointss.get(i);
                    gc.drawLine(p3.x, p3.y, p4.x, p4.y);
                    p3 = p4;
                }

            }else if(pointss.size() ==1)
                pointss.add(cm.mouse_e.getPoint());
        }
    }


    public void SendMouseEvent(MouseEvent e) {
        Server.ChatMsg cm = new Server.ChatMsg(UserName, "500", "MOUSE");
        cm.mouse_e = e;
        cm.pen_size = pen_size;
        cm.co=c;
        cm.shape=shapes;
        //System.out.println(linee);

        cm.lines=linee;
        SendObject(cm);
    }

    class MyMouseWheelEvent implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // TODO Auto-generated method stub
            if (e.getWheelRotation() < 0) { // 위로 올리는 경우 pen_size 증가
                if (pen_size < 20)
                    pen_size++;
            } else {
                if (pen_size > 2)
                    pen_size--;
            }
            //lblMouseEvent.setText("mouseWheelMoved Rotation=" + e.getWheelRotation() + " pen_size = " + pen_size + " " + e.getX() + "," + e.getY());
        }
    }

    // Mouse Event Handler
    class MyMouseEvent implements MouseListener, MouseMotionListener {

        List<Point> points = new ArrayList<Point>();

        @Override
        public void mouseDragged(MouseEvent e) {
            linee=true;
            //lblMouseEvent.setText(e.getButton() + " mousedragged " + e.getX() + "," + e.getY()+points);
            gc.setColor(c);
            points.add(e.getPoint());

            linee=true;
            if (points.size() > 1) {
                Point p1 = points.get(0);
                Graphics2D g2=(Graphics2D)gc;

                g2.setStroke(new BasicStroke(pen_size));

                for (int i = 1; i < points.size(); i++){
                    Point p2 = points.get(i);
                    gc.drawLine(p1.x, p1.y, p2.x, p2.y);
                    p1 = p2;
                }
            }
            else
                points.add(e.getPoint());

            gc.drawImage(panelImage, 0, 0, panel); //

            SendMouseEvent(e);
            linee=false;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            linee=false;

            //points.clear();
            //lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());
            gc.setColor(c);
            if(shapes==1) {

                gc.drawOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
                gc.drawImage(panelImage, 0, 0, panel);

            }else if(shapes==3) {

                gc.drawRect(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
                gc.drawImage(panelImage, 0, 0, panel);
            }
            else if(shapes==0) {

                gc.fillRect(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
                gc.drawImage(panelImage, 0, 0, panel);
            }

            SendMouseEvent(e);
            shapes=0;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," + e.getY());
            // panel.setBackground(Color.YELLOW);

        }

        @Override
        public void mouseExited(MouseEvent e) {
            //lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," + e.getY());
            // panel.setBackground(Color.CYAN);

        }

        @Override
        public void mousePressed(MouseEvent e) {
            points.clear();
            //lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());
        }

        @Override
        public void mouseReleased(MouseEvent e) { //구현해야함!
            lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY()); //안그려지는 이유???!!!
            // 드래그중 멈출시 보임
            points.add(e.getPoint());
            points.clear();
            linee=false;
            SendMouseEvent(e);
        }
    }

    // 화면에 출력
    public void AppendText(String msg) { //채팅창에 msg를 뿌려주는 메서드
        // textArea.append(msg + "\n");
        // AppendIcon(icon1);
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        //textArea.setCaretPosition(len);
        //textArea.replaceSelection(msg + "\n");

        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        try {
            doc.insertString(doc.getLength(), msg+"\n", left );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len); //끝으로 이동
        //textArea.replaceSelection("\n");


    }
    // 화면 우측에 출력
    public void AppendTextR(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.RED);
        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
        try {
            doc.insertString(doc.getLength(),msg+"\n", right );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        //textArea.replaceSelection("\n");

    }

    public void AppendImage(ImageIcon ori_icon) {
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len); // place caret at the end (with no selection)
        Image ori_img = ori_icon.getImage();
        Image new_img;
        ImageIcon new_icon;
        int width, height;
        double ratio;
        width = ori_icon.getIconWidth();
        height = ori_icon.getIconHeight();
        // Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
        if (width > 200 || height > 200) {
            if (width > height) { // 가로 사진
                ratio = (double) height / width;
                width = 200;
                height = (int) (width * ratio);
            } else { // 세로 사진
                ratio = (double) width / height;
                height = 200;
                width = (int) (height * ratio);
            }
            new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            new_icon = new ImageIcon(new_img);
            textArea.insertIcon(new_icon);
        } else {
            textArea.insertIcon(ori_icon);
            new_img = ori_img;
        }
        len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        textArea.replaceSelection("\n");
        // ImageViewAction viewaction = new ImageViewAction();
        // new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
        // panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

        gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
        gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
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
            System.exit(0);
        }
        for (i = 0; i < bb.length; i++)
            packet[i] = bb[i];
        return packet;
    }

    public void SendMessage(String msg) {
        try {
            ChatMsg obcm = new ChatMsg("UserName", "200", msg);
            oos.writeObject(obcm);
        } catch (IOException e) {
            AppendText("oos.writeObject error");
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch(IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void SendMessage2(String msg) {
        try {
            ChatMsg obcm61 = new ChatMsg(UserName, "701", msg);
            oos.writeObject(obcm61);
            ChatMsg obcm = new ChatMsg(UserName, "700", msg);
            oos.writeObject(obcm);
        } catch (IOException e) {
            AppendText("oos writeObject() error");
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }


    public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
        try {
            oos.writeObject(ob);
        } catch (IOException e) {
            //textArea.append("메세지 송신 에러!!\n");
            AppendText("SendObject Error");
        }
    }

    /*public static void main(String[] args) {
        GameClientView game = new GameClientView("user", "192", "1111");
        game.startss();
    } */
}