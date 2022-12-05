package Client;

import Server.ChatMsg;
import Server.HintMsg;
import Server.wordMsg;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.net.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GameClientView extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;
    private GameClientMain main;
    private GameClientOver over;

    //    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");
    private ImageIcon backgroundImg = new ImageIcon("res/basicBackground.png");
    private ImageIcon logoImg = new ImageIcon("res/gameTitle.png");
    private JLabel userLabel;
    private ImageIcon viewPanelBg = new ImageIcon("res/gameViewPanel.png");
    private ImageIcon infoPanelBg = new ImageIcon("res/infoPanelImg.png");
    private ImageIcon hintPanelBg = new ImageIcon("res/hintPanelImg.png");
    private ImageIcon chatPanelBg = new ImageIcon("res/chatPanelImg.png");

    private ImageIcon bgItemImg = new ImageIcon("res/item_backImage.png");
    private ImageIcon bgItemImg1 = new ImageIcon("res/item_backImage1.png");
    private ImageIcon mouseOverBgItem = new ImageIcon("res/mouseOverBgItem.png");
    private ImageIcon timeItemImg = new ImageIcon("res/item_time.png");
    private ImageIcon timeItemImg1 = new ImageIcon("res/item_time1.png");
    private ImageIcon mouseOvertimeItem = new ImageIcon("res/mouseOverItemTime.png");
    private ImageIcon initialItemImg = new ImageIcon("res/item_initialChar.png");
    private ImageIcon initialItemImg1 = new ImageIcon("res/item_initialChar1.png"); //view에서의 힌트(크기조절)
    private ImageIcon mouseOverInitialItem = new ImageIcon("res/mouseOverItemInitial.png");
    private ImageIcon wordCountItemImg = new ImageIcon("res/item_wordCount.png");
    private ImageIcon wordCountItemImg1 = new ImageIcon("res/item_wordCount1.png"); //view에서의 힌트(글자수세기)
    private ImageIcon mouseOverWordCntItem = new ImageIcon("res/mouseOverItemWordCnt.png");
    private ImageIcon twiceScoreItemImg = new ImageIcon("res/item_twiceScore.png");
    private ImageIcon twiceScoreItemImg1 = new ImageIcon("res/item_twiceScore1.png");
    private ImageIcon mouseOverScoreItem = new ImageIcon("res/mouseOverItemScore.png");
    private ImageIcon categoryItemImg = new ImageIcon("res/item_showCategory.png");
    private ImageIcon categoryItemImg1 = new ImageIcon("res/item_showCategory1.png");
    private ImageIcon mouseOverCategoryItem = new ImageIcon("res/mouseOverItemCategory.png");

    private ImageIcon char1Img = new ImageIcon("res/character1.png");
    private ImageIcon char2Img = new ImageIcon("res/character2.png");
    private ImageIcon char3Img = new ImageIcon("res/character3.png");
    private ImageIcon answerChar1 = new ImageIcon("res/answerChar1.png");
    private ImageIcon answerChar2 = new ImageIcon("res/answerChar2.png");
    private ImageIcon answerChar3 = new ImageIcon("res/answerChar3.png");
    private ImageIcon wrongChar1 = new ImageIcon("res/wrongChar1.png");
    private ImageIcon wrongChar2 = new ImageIcon("res/wrongChar2.png");
    private ImageIcon wrongChar3 = new ImageIcon("res/wrongChar3.png");

    private ImageIcon correctIcon = new ImageIcon("res/correctIcon.png");
    private ImageIcon wrongIcon = new ImageIcon("res/wrongIcon.png");
    private JPanel contentPane;
    private JPanel mainPanel;

    private JPanel infoPanel;
    private JPanel hintPanel;
    public JPanel canvasPanel;
    private JPanel chatingPanel;
    private JPanel usersPanel;
    private JLabel logo;
    private JPanel hintResultPanel;
    Image buffImg;
    Graphics buffG;

    public int score=0;

    private static final long serialVersionUID = 1L;
    private int currentWordIndex;
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
    private JLabel wordInfoLabel;
    private JLabel wordLabel;
    private JLabel wordTitle;
    public static Image panelImage = null;
    private JPanel scorePanel;
    private JLabel scoreInfoLabel;
    private JLabel scoreLabel;

    private JPanel timePanel;
    private JLabel timeInfoLabel;
    private static JLabel timeLabel;
    private JLabel timeTitle;
    private int presenterIndex;

    private JLabel lblNewLabel; //공지사항 알려주는 label
    //private JTextArea textArea;
    private JTextPane textArea;
    private JTextPane textArea1;

    public JButton timeItemBtn;

    private Frame frame;
    private FileDialog fd;
    private JButton imgBtn;
    private JPanel resultPanel;
    private JPanel presenterPanel;

    public String[] wordList;
    public int indexWordList = 0;

    public JPanel panel;
    private JLabel lblMouseEvent;
    private boolean isClear = false;
    private int preseterIndex;
    JButton btnNewButtonStart;
    private MyMouseEvent mouse = new MyMouseEvent();
    public static Graphics gc;
    private int pen_size = 2; // minimum 2
    // 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
    private Graphics gc2 = null;

    public Color c = new Color(0,0,0);
    public int shapes;
    private LineBorder lb;
    public List<Point> pointss = new ArrayList<Point>();  //pointss arraylist
    List<Point> points = new ArrayList<Point>();
    public MyMouseWheelEvent wheel;

    public Graphics2D g2;

    public String getCurWord() {
        return curWord;
    }

    public void setCurWord(String curWord) {
        this.curWord = curWord;
    }

    public String curWord="";
    public boolean linee = true;
    public boolean isStart = false;
    public boolean isOver = false;
    private JTextField textField;

//    private Vector userVec = new Vector();
    public String[] socketList;
    public String[] userList;
    public HashMap<String, Boolean> isPresenter = new HashMap<>();
    public String[] charList;

    public String roomId, username, char_no;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private int time = 20;
    private BufferedImage imageBuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    /**
     * Create the frame.
     *
     * @throws BadLocationException
     */

    public String getRoomId() {
        return roomId;
    }
    public GameClientView(GameClientMain main, String roomId, String socketList, String userList, String charList, String username, String char_no, String wordList, Socket socket, ObjectInputStream ois, ObjectOutputStream oos ) {
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
//       setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
//        setUndecorated(true);

        this.main = main;
        this.roomId = roomId;
        this.username = username;
        this.char_no = char_no;

        this.wordList = wordList.split(" ");
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
//            JPanel userPanel = new JPanel();
            userLabel = new JLabel();
            if(this.charList[i].equals("char1")) userLabel.setIcon(char1Img);
            else if(this.charList[i].equals("char2")) userLabel.setIcon(char2Img);
            else userLabel.setIcon(char3Img);
            userLabel.setBounds(40,35 + (i*160),110,110);
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
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
//              SendObject(msg);
//              System.exit(0);
                ChatMsg obc = new ChatMsg(UserName, "700","null");
                obc.roomId = roomId;
                main.SendObject(obc); //버튼 클릭시 main사용

                setVisible(false);
                //2명이상이면 start
            }
        });
        btnExit.revalidate();
        btnExit.repaint();
        btnExit.setFont(new Font("굴림", Font.PLAIN, 14));
        btnExit.setBounds(35, 660, 130, 30);
        usersPanel.add(btnExit);


        infoPanel = new JPanel() {
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
        wordPanel.setLayout(null);
        wordPanel.setBounds(00,0,260,150);
//        wordPanel.setLayout(new BorderLayout());
        infoPanel.add(wordPanel);

        wordInfoLabel = new JLabel("제시어"){}; //안내 공지하는 label
        wordInfoLabel.revalidate();
        wordInfoLabel.repaint();
        wordInfoLabel.setForeground(Color.BLACK);
        wordInfoLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
//        wordInfoLabel.setBorder(lb);
        wordInfoLabel.setBounds(100, 20, 260, 20);
        wordPanel.add(wordInfoLabel);

        wordLabel = new JLabel();
        wordLabel.revalidate();
        wordLabel.repaint();
        wordLabel.setFont(new Font("나눔고딕", Font.PLAIN, 50));
        wordLabel.setBounds(80,40,260, 90);
        wordPanel.add(wordLabel);


        scorePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260, 150, this);
                repaint();
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scorePanel.revalidate();
        scorePanel.repaint();
        scorePanel.setLayout(null);
        scorePanel.setBounds(270,0,260,150);
        infoPanel.add(scorePanel);

        scoreInfoLabel = new JLabel("점수"); //안내 공지하는 label
        scoreInfoLabel.revalidate();
        scoreInfoLabel.repaint();
        scoreInfoLabel.setForeground(Color.BLACK);
        scoreInfoLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
        scoreInfoLabel.setBounds(120, 20, 260, 20);
        scorePanel.add(scoreInfoLabel);

        scoreLabel = new JLabel();
        scoreLabel.revalidate();
        scoreLabel.repaint();
        scoreLabel.setFont(new Font("나눔고딕", Font.PLAIN, 40));
        scoreLabel.setBounds(120,40,260, 90);
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
        timePanel.setLayout(null);
        timePanel.setBounds(530,0,260,150);
        infoPanel.add(timePanel);

        timeInfoLabel = new JLabel("제한시간"); //안내 공지하는 label
        timeInfoLabel.revalidate();
        timeInfoLabel.repaint();
        timeInfoLabel.setForeground(Color.BLACK);
        timeInfoLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
        timeInfoLabel.setBounds(78, 20, 260, 20);
        timePanel.add(timeInfoLabel);

        timeLabel = new JLabel();
        timeLabel.revalidate();
        timeLabel.repaint();
        timeLabel.setFont(new Font("나눔고딕", Font.PLAIN, 40));
        timeLabel.setBounds(100,40,260, 90);
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

        /*JButton bgItemBtn = new JButton(bgItemImg1);
        bgItemBtn.revalidate();
        bgItemBtn.repaint();
        bgItemBtn.setBorderPainted(false);
        bgItemBtn.setContentAreaFilled(false);
        bgItemBtn.setOpaque(false);
        bgItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgItemBtn.setBounds(50, 20, 80, 50);
        hintPanel.add(bgItemBtn);
        bgItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("background img!\n");
                HintMsg obc = new HintMsg(UserName, "1002", null); //배경그림 보여주는 힌트
                main.SendObject(obc);

            }
        }); */

        JButton timeItemBtn = new JButton(timeItemImg1); //시간증가 힌트 버튼
        timeItemBtn.setBorderPainted(false);
        timeItemBtn.setContentAreaFilled(false);
        timeItemBtn.setOpaque(false);
        timeItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        timeItemBtn.setBounds(40, 50, 90, 55);
        hintPanel.add(timeItemBtn);
        timeItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("time event!\n");
                timeItemBtn.setEnabled(false);
                HintMsg obc = new HintMsg(UserName, "1001",String.valueOf(time)); //시간 증가 구현중
                System.out.println(isStart);
                obc.isStart = isStart;
                obc.roomId= roomId;
                main.SendObject(obc);
                repaint();
                revalidate();
                update(gc2);
            }
        });


//        JButton initItemBtn = new JButton(initialItemImg1); //첫글자 알려주는 힌트(?)  //일단 주석처리
//        timeItemBtn.revalidate();
//        timeItemBtn.repaint();
//        timeItemBtn.setBorderPainted(false);
//        timeItemBtn.setContentAreaFilled(false);
//        timeItemBtn.setOpaque(false);
//        timeItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        initItemBtn.setBounds(260, 50, 90, 55);
//        hintPanel.add(initItemBtn);
//        initItemBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                HintMsg obc = new HintMsg(UserName, "1000", null);
//                obc.roomId = roomId;
//                obc.wordIndex = currentWordIndex;
//                main.SendObject(obc); //버튼 클릭시 1000으로,,
//            }
//        });


        /*JButton cntItemBtn = new JButton(wordCountItemImg1); //글자 수 알려주는 힌트
        cntItemBtn.revalidate();
        cntItemBtn.repaint();
        cntItemBtn.setBorderPainted(false);
        cntItemBtn.setContentAreaFilled(false);
        cntItemBtn.setOpaque(false);
        cntItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cntItemBtn.setBounds(50, 80, 80, 50);
        hintPanel.add(cntItemBtn);
        cntItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HintMsg obc = new HintMsg(UserName, "1003", null);
                SendObject(obc);
            }
        }); */

        JButton twiceItemBtn = new JButton(twiceScoreItemImg1); //점수 두배 이벤트
        twiceItemBtn.setBorderPainted(false);
        twiceItemBtn.setContentAreaFilled(false);
        twiceItemBtn.setOpaque(false);
        twiceItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        twiceItemBtn.setBounds(150, 50, 90, 55);
        hintPanel.add(twiceItemBtn);
        twiceItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("twice socre event\n");
                HintMsg obc = new HintMsg(UserName, "1004", null);
                obc.UserName = UserName;
                //showScore(2*score);
                score*=2;
                scoreLabel.setText(String.valueOf(score));
                revalidate();
                main.SendObject(obc);
                twiceItemBtn.setEnabled(false);
            }
        });


        JButton ctgItemBtn = new JButton(initialItemImg1); //초성힌트(?)
        ctgItemBtn.setBorderPainted(false);
        ctgItemBtn.setContentAreaFilled(false);
        ctgItemBtn.setOpaque(false);
        ctgItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ctgItemBtn.setBounds(260, 50, 90, 55);
        hintPanel.add(ctgItemBtn);
        ctgItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //초성힌트 구현
                HintMsg obc = new HintMsg(UserName, "1000", curWord);
                ctgItemBtn.setEnabled(false);
                main.SendObject(obc);
                repaint();
                revalidate();
                update(gc2);
            }
        });

        canvasPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                  Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        canvasPanel.revalidate();
        canvasPanel.repaint();
        canvasPanel.setBounds(250,200,800,550);
        canvasPanel.setLayout(null);
        canvasPanel.setBackground(Color.WHITE);
//        canvasPanel.setBackground(new Color(79, 121, 66));
        mainPanel.add(canvasPanel);

        panel = new JPanel();
        panel.revalidate();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBackground(Color.WHITE);
        panel.setBounds(20, 20, 760, 480); //그림판 판넬
        canvasPanel.add(panel);
        gc = panel.getGraphics();
        MyMouseEvent mouse = new MyMouseEvent();
        panel.addMouseMotionListener(mouse);
        panel.addMouseListener(mouse);
        MyMouseWheelEvent wheel = new MyMouseWheelEvent();
        panel.addMouseWheelListener(wheel);

        JButton btnNewButton1 = new JButton(".");
        btnNewButton1.setBackground(Color.RED);
        btnNewButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                c = new Color(255, 0, 0);
                startss();
            }
        });
        btnNewButton1.revalidate();
        btnNewButton1.repaint();
        btnNewButton1.setForeground(Color.RED);
        btnNewButton1.setBounds(50, 508, 62, 35);
        canvasPanel.add(btnNewButton1);


        JButton btnNewButton2 = new JButton(".");
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


        //JLabel lbe = new JLabel();
        //lbe.setBounds(600, 503, 10,20);
        //lbe.setText("지우개"); //지우개 라벨(버튼위에)
        //lbe.setHorizontalAlignment(JLabel.CENTER);
        //canvasPanel.add(lbe);
        JButton btnNewButton6 = new JButton("지우개"); //지우개버튼
        btnNewButton6.setFont(new Font("굴림", Font.PLAIN, 12));
        btnNewButton6.setBounds(630, 505, 70, 40);
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


        JButton btnNewButton7 = new JButton("clear"); //모두지우기 버튼
        btnNewButton7.setFont(new Font("굴림", Font.PLAIN, 12));
        btnNewButton7.setBounds(710, 505, 70, 40);
        canvasPanel.add(btnNewButton7);
        btnNewButton7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gc2!=null) {
                    gc2.setColor(panel.getBackground());
//                    gc2.flush();
                    gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    gc2.setColor(Color.BLACK);
                    //gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);
                    pointss.clear();
                    points.clear();

                    main.SendMouseEvent(null, c, pen_size, shapes, linee);
                    repaint();
                }
            }
        });
        btnNewButton7.revalidate();
        btnNewButton7.repaint();


        JButton btnNewButton8 = new JButton("●"); //동그라미 그리기
        btnNewButton8.setFont(new Font("굴림", Font.PLAIN, 12));
        btnNewButton8.setBounds(410, 508, 60, 35);
        btnNewButton8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        canvasPanel.add(btnNewButton8);
        btnNewButton8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e1) {
                shapes=1;
                linee = false;
                //c = new Color(255,255,255);
                //startss();
            }
        });
        btnNewButton8.revalidate();
        btnNewButton8.repaint();

        JButton btnNewButton10 = new JButton("◼"); //사각형 그리기
        btnNewButton10.setFont(new Font("굴림", Font.PLAIN, 12));
        btnNewButton10.setBounds(482, 508, 60, 35);
        btnNewButton10.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        canvasPanel.add(btnNewButton10);
        btnNewButton10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e2) {
                shapes=3;
                //c = new Color(255,255,255);
                //startss();
            }
        });
        btnNewButton10.revalidate();
        btnNewButton10.repaint();


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
        SendAction sendACtion = new SendAction();
        btnSend.addActionListener(sendACtion);

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
                    ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
                    main.SendObject(msg);
                    System.exit(0);
            }
        });
        btnNewButton.setBounds(300, 490, 70, 30);
        chatingPanel.add(btnNewButton);

        btnNewButtonStart = new JButton("시 작"); //시작버튼
        btnNewButtonStart.revalidate();
        btnNewButtonStart.repaint();
        btnNewButtonStart.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButtonStart.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                showWord(indexWordList);
//                showScore(0);
//                showTime();

                isStart = true;

                ChatMsg obc = new ChatMsg(UserName, "600","null");
                obc.roomId = roomId;
                main.SendObject(obc);
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

        lblMouseEvent = new JLabel("<dynamic>"); //그림판 밑에 / 색깔팬이랑 지우개 추가하기
        lblMouseEvent.setHorizontalAlignment(SwingConstants.CENTER);
        lblMouseEvent.setFont(new Font("굴림", Font.BOLD, 14));
        lblMouseEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblMouseEvent.setBackground(Color.WHITE);
        lblMouseEvent.setBounds(160, 520, 450, 40);
        contentPane.add(lblMouseEvent);
    }


    class SendAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // 서버로 메세지 보내기
            System.out.println("txt input !!! "+txtInput.getText());
            ChatMsg cm = new ChatMsg(username, "200", txtInput.getText());
            cm.isStart = isStart;
            cm.roomId = roomId;

            main.SendObject(cm);
            txtInput.setText("");

            String sendTxt = "["+ username + "]" + txtInput.getText();

            // 채팅창에 라벨로 append
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

    @Override
    public void paint(Graphics g) {
//        super.paint(g);

        if(gc!= null && panel != null) gc.drawImage(panelImage, 0,0 , panel.getWidth(), panel.getHeight(), panel);

    }

    @Override
    public void update(Graphics gc) {
        if(panel != null) gc.drawImage(panelImage, 0,0 , panel.getWidth(), panel.getHeight(), panel);
        repaint();
    }


    // Mouse Event 수신 처리
    public void DoMouseEvent(ChatMsg cm,Color co,int shape) {
        //if (cm.UserName.matches(UserName)) // 본인 것은 이미 Local 로 그렸다.
          //  return;
        //System.out.println(cm.lines);

        gc2.setColor(co);

        if(cm.mouse_e==null){
            if(gc2!=null) {
                gc2.setColor(panel.getBackground());
//                    gc2.flush();
                gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                gc2.setColor(Color.BLACK);
                //gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);
                pointss.clear();
                points.clear();

            }
        }
        else{
            if(cm.lines==false) {
                pointss.clear();
                if(shape==1) {
                    gc2.drawOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2,cm.pen_size, cm.pen_size);
                    shape=0;

                }else if(shape==3) {
                    gc2.drawRect(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
                    shape=0;

                }else if(shape==0) {

                    gc2.fillRect(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);

                }
                shape=0;
            }else if(cm.lines==true) {

                pointss.add(cm.mouse_e.getPoint());

                if (pointss.size() >1 ) {

                    Point p3 = pointss.get(0);
                    Graphics2D g2=(Graphics2D)gc2;

                    g2.setStroke(new BasicStroke(cm.pen_size));

                    for (int i = 1; i < pointss.size(); i++) {
                        Point p4 = pointss.get(i);
                        gc2.drawLine(p3.x, p3.y, p4.x, p4.y);
                        p3 = p4;
                    }
                } else if(pointss.size() ==1)
                    pointss.add(cm.mouse_e.getPoint());
            }
        }

        update(gc);
        revalidate();
    }


//    public void SendMouseEvent(MouseEvent e) {
//        Server.ChatMsg cm = new Server.ChatMsg( UserName, "500", "MOUSE");
//        cm.roomId = roomId;
//        cm.mouse_e = e;
//        cm.pen_size = pen_size;
//        cm.co=c;
//        cm.shape=shapes;
//        //System.out.println(linee);
//
//        cm.lines=linee;
//        main.SendObject(cm);
//    }

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
        }
    }

    // Mouse Event Handler

    class MyMouseEvent implements MouseListener, MouseMotionListener {

        List<Point> points = new ArrayList<Point>();


        @Override
        public void mouseDragged(MouseEvent e) {
            linee=true;
            gc2.setColor(c);
            points.add(e.getPoint());

            linee=true;
            if (points.size() > 1) {
                Point p1 = points.get(0);
                g2=(Graphics2D)gc2;

                g2.setStroke(new BasicStroke(pen_size));

                for (int i = 1; i < points.size(); i++){
                    Point p2 = points.get(i);
//                    gc.drawLine(p1.x, p1.y, p2.x, p2.y);
                    gc2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    p1 = p2;
                }
            }
            else
                points.add(e.getPoint());
            update(gc);
            main.SendMouseEvent(e, c, pen_size, shapes, linee);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("hi" + linee);
            if(linee=false){
                gc2.setColor(c);
                gc.setColor(c);

                if(shapes==1) {
                    gc2.drawOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size*2, pen_size*2);
//                    gc2.drawImage(panelImage, 0, 0, panel);

                }else if(shapes==2) {
                    gc2.fillRect(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size*2, pen_size*2);
//                    gc2.drawImage(panelImage, 0, 0, panel);
                }

                update(gc);
                main.SendMouseEvent(e, c, pen_size, shapes, linee);
//                shapes=0;
            }
            //points.clear();
            //lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            points.clear();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
//            points.add(e.getPoint());
            points.clear();
            linee=false;
            main.SendMouseEvent(e, c, pen_size, shapes, linee);
        }
    }

    public void showTime(){
        btnNewButtonStart.setEnabled(false);
        java.util.Timer timer = new java.util.Timer();
        java.util.TimerTask task = new  java.util.TimerTask(){
            public void run(){
                if(isStart) {
//                    gc.drawImage(panelImage, 0, 0, panel);

                    timeLabel.setText(String.valueOf(time--));

                    if (time < 0) {
                        //게임 종료 프로토콜 필요. -> 700
                        isStart = false;
                        btnNewButtonStart.setEnabled(true);
//                        exitView();
//                        showScore(score);
//                        setTime(20);
//                        showTime();
                        System.out.println("in view::" + UserName + "//" + userList[presenterIndex]);
                        if(username.equals(userList[presenterIndex])){
                            if(presenterIndex > 0){
                                showPresenter(presenterIndex);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                removePresenter();
                            }
                            removeWord();
                            changeRole();
                        }
                        else{
                            if(presenterIndex > 0){
                                showPresenter(presenterIndex);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                removePresenter();
                            }
                            showWord(indexWordList+1);
                            changeRole();
                        }

                        System.out.println("in view /// 800번 ");
                        Client.RoomMsg changeMsg = new Client.RoomMsg("800",String.valueOf(presenterIndex) + " "+ String.valueOf(indexWordList));
                        changeMsg.roomId = roomId;
                        main.SendObject(changeMsg);

                        btnNewButtonStart.setEnabled(true);
//                      ChatMsg cm = new ChatMsg(UserName, "800", presenterIndex); //800번 프로토콜 -> 순서 바꾸기 초기화
//                      main.SendObject(cm);

//                      changeRole();
                    }
                }


            }
        };
            timer.scheduleAtFixedRate(task, 0L, 1000);
    }

    public void changeRole(){
        //캔버스 초기화
        if(gc2!=null && panel!=null) {
            gc2.setColor(panel.getBackground());
            //gc2.flush();
            gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            gc2.setColor(Color.BLACK);
            pointss.clear();
            points.clear();
            //gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);
        }

    }

    public void showPresenter(int presenterIndex){
        this.presenterIndex = presenterIndex;
        String presenter = userList[presenterIndex];

        if(this.wordList.length-1 > indexWordList){
            curWord = this.wordList[++indexWordList];
        }

        presenterPanel = new JPanel();
        presenterPanel.setBackground(Color.decode("#569A49"));
        presenterPanel.setOpaque(true);
        presenterPanel.setLayout(null);
        presenterPanel.setBounds(80,60,600, 380);
        canvasPanel.add(presenterPanel);
        presenterPanel.setVisible(true);

        JLabel presenterLabel;
        presenterLabel = new JLabel(presenter +"님이 출제할 차례입니다.");
        presenterLabel.setFont(new Font("굴림", Font.PLAIN, 40));
//        presenterLabel.setIcon(correctIcon);
        presenterLabel.setOpaque(true);
        presenterLabel.setVisible(true);
        presenterLabel.setBackground(Color.white);
        presenterLabel.setBounds(0,0,600,380);
        panel.setVisible(false);
        presenterPanel.add(presenterLabel);

    }

    public void removePresenter(){
        presenterPanel.setVisible(false);
        panel.setVisible(true);
        repaint();
    }

    public void showWord(int index){
        if(this.wordList.length == index){
            Client.RoomMsg overRoomMsg = new Client.RoomMsg("900", UserName + " "+ String.valueOf(score));
            main.SendObject(overRoomMsg);
            this.setVisible(false);
        }
        else{
            wordLabel.setText(this.wordList[index]);
            wordLabel.setVisible(true);
        }
    }
    public void overGame(String scores){
        over = new GameClientOver(scores);
        this.setVisible(false);
        over.setVisible(true);
    }

    public void removeWord(){
        wordLabel.setVisible(false);
    }

    public void showScore(int score){

        if(score <= 0 ) {
            this.score = 0 ;
        }

        else this.score = score;
        scoreLabel.setText(String.valueOf(this.score));
        revalidate();
    }

    public int getScore(){
        return this.score;
    }
    public String getUserName(){
        return this.UserName;
    }

    public void showResultPanel(String code){
        resultPanel = new JPanel();
        resultPanel.setBackground(Color.decode("#569A49"));
        resultPanel.setOpaque(true);
        resultPanel.setLayout(null);
//        resultPanel.update(gc);
//        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBounds(80,60,600, 380);
        canvasPanel.add(resultPanel);
        resultPanel.setVisible(true);

        JLabel resultLabel;
        if(code.matches("201")) { //정답 : answer
            if(userLabel!= null) {
                if(userLabel.getIcon() == char1Img)
                userLabel.setIcon(answerChar1);
                else if(userLabel.getIcon() == char2Img)
                    userLabel.setIcon(answerChar2);
                else userLabel.setIcon(answerChar3);
            }

            resultLabel = new JLabel(correctIcon);
            resultLabel.setIcon(correctIcon);
            resultLabel.setOpaque(true);
            resultLabel.setVisible(true);
            resultLabel.setBackground(Color.white);
            resultLabel.setBounds(0,0,600,380);
            canvasPanel.add(resultLabel);
            panel.setVisible(false);
            resultPanel.add(resultLabel, BorderLayout.CENTER);
        }
        else if(code.matches("202")) {
            if(userLabel!= null){
                if(userLabel.getIcon() == char1Img)
                    userLabel.setIcon(wrongChar1);
                else if(userLabel.getIcon() == char2Img)
                    userLabel.setIcon(wrongChar2);
                else userLabel.setIcon(wrongChar3);
            }
            resultLabel = new JLabel(wrongIcon);
            resultLabel.setIcon(wrongIcon);
            resultLabel.setOpaque(true);
            resultLabel.setVisible(true);
            resultLabel.setBackground(Color.white);
            resultLabel.setBounds(0,0,600,380);
//            canvasPanel.add(resultLabel);
            panel.setVisible(false);
            resultPanel.add(resultLabel, BorderLayout.CENTER);
        }
    }

    public void removeResultPanel(){
        resultPanel.setVisible(false);
        panel.setVisible(true);
        repaint();
        if(userLabel!= null){
            if(userLabel.getIcon().equals("res/answerChar1.png") || userLabel.getIcon().equals("res/wrongChar1.png"))
                userLabel.setIcon(char1Img);
            else if(userLabel.getIcon().equals("res/answerChar2.png") || userLabel.getIcon().equals("res/wrongChar2.png"))
                userLabel.setIcon(char2Img);
            else userLabel.setIcon(char3Img);
        }
    }

    public void showHintResultPanel(){
        hintResultPanel = new JPanel();
        hintResultPanel.setLayout(null);
        hintResultPanel.setBounds(1150, 0, 500, 80);
        hintResultPanel.setOpaque(true);
        hintResultPanel.setVisible(true);
        contentPane.add(hintResultPanel);
    }

    public void removeHintPanel(){
        hintResultPanel.setVisible(false);
    }


    public void exitView() {
        main.setVisible(true);
        System.exit(0);
    }

    public void leaveRoom(){
        main.setVisible(true);
        this.setVisible(false);
    }

    // 화면에 출력
    public void AppendText(String msg) { //채팅창에 msg를 뿌려주는 메서드
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.

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
        // ImageViewAction viewaction = new Image ViewAction();
        // new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
        // panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

//        gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
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

//    public static void main(String[] args) {
//    }

}