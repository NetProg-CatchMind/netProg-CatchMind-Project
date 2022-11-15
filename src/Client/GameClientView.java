package Client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;

public class GameClientView extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

//    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");
    private ImageIcon backgroundImg = new ImageIcon("res/basicBackground.png");
    private ImageIcon logoImg = new ImageIcon("res/gameTitle.png");
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

    JPanel panel;
    private JLabel lblMouseEvent;
    private Graphics gc;
    private int pen_size = 2; // minimum 2
    // 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
    private Image panelImage = null;
    private Graphics gc2 = null;
    public Color c = new Color(0,0,0);
    private LineBorder lb;

    /**
     * Create the frame.
     *
     * @throws BadLocationException
     */

    public GameClientView(String username, String ip_addr, String port_no) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
//        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
//        setUndecorated(true);


        contentPane = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        logo = new JLabel(logoImg){
            public void paintComponent(Graphics g) {
                g.drawImage(logoImg.getImage(), 0, 0, 500, 80, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        logo.setIcon(logoImg);
        logo.setBounds(40,0, 500,80);
        contentPane.add(logo);

        mainPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(viewPanelBg.getImage(), 0, 0, 1490, 800, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        mainPanel.setBounds(0,20,1500, 800);
        mainPanel.setLayout(null);
        contentPane.add(mainPanel);


        usersPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        usersPanel.setBounds(50,40,200,700);
//        usersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        usersPanel.setLayout(null);
//        usersPanel.setBackground(Color.white);
        usersPanel.setOpaque(false);
        mainPanel.add(usersPanel);

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(35, 20, 130, 130); //접속한 유저 정보창
        scrollPane1.setBorder(new TitledBorder(new LineBorder(Color.black),"user1"));
        usersPanel.add(scrollPane1);
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(35, 180, 130, 130); //접속한 유저 정보창
        scrollPane2.setBorder(new TitledBorder(new LineBorder(Color.black),"user2"));
        usersPanel.add(scrollPane2);
        JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setBounds(35, 340 , 130, 130); //접속한 유저 정보창
        scrollPane3.setBorder(new TitledBorder(new LineBorder(Color.black),"user3"));
        usersPanel.add(scrollPane3);
        JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.setBounds(35, 500, 130, 130); //접속한 유저 정보창
        scrollPane4.setBorder(new TitledBorder(new LineBorder(Color.black),"user4"));
        usersPanel.add(scrollPane4);



        btnExit = new JButton("방 나가기"); //Exit button
        btnExit.setFont(new Font("굴림", Font.PLAIN, 14));
        btnExit.setBounds(35, 660, 130, 30);
        usersPanel.add(btnExit);


        infoPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        infoPanel.setBounds(250,50,800, 150);
        infoPanel.setLayout(null);
//        infoPanel.setBackground(Color.white);
        infoPanel.setOpaque(false);
        mainPanel.add(infoPanel);

        lb = new LineBorder(Color.black, 1, true);

        wordPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260 , 150, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        wordPanel.setBounds(10,0,260,150);
        infoPanel.add(wordPanel);

        wordLabel = new JLabel("제시어."){}; //안내 공지하는 label
        wordLabel.setForeground(Color.BLACK);
        wordLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
        wordLabel.setBorder(lb);
//        wordLabel.setBounds(10, 0, 260, 150);
        wordPanel.add(wordLabel);

        scorePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260, 150, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scorePanel.setBounds(270,0,260,150);
        infoPanel.add(scorePanel);

        scoreLabel = new JLabel("점수."); //안내 공지하는 label
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBorder(lb);
        scoreLabel.setFont(new Font("나눔고딕", Font.PLAIN, 20));
//        scoreLabel.setBounds(270, 0, 260, 150);
        scorePanel.add(scoreLabel);

        timePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(infoPanelBg.getImage(), 0, 0, 260, 150, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        timePanel.setBounds(530,0,260,150);
        infoPanel.add(timePanel);

        timeLabel = new JLabel("제한시간."); //안내 공지하는 label
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
        hintPanel.setBounds(1050,50,380,150);
        hintPanel.setLayout(null);
        hintPanel.setOpaque(false);
        hintPanel.setBackground(Color.black);
        mainPanel.add(hintPanel);

        JButton bgItemBtn = new JButton(bgItemImg);
        bgItemBtn.setBounds(50, 20, 80, 50);
        hintPanel.add(bgItemBtn);

        JButton timeItemBtn = new JButton(timeItemImg);
        timeItemBtn.setBounds(150, 20, 80, 50);
        hintPanel.add(timeItemBtn);

        JButton initItemBtn = new JButton(initialItemImg);
        initItemBtn.setBounds(250, 20, 80, 50);
        hintPanel.add(initItemBtn);

        JButton cntItemBtn = new JButton(wordCountItemImg);
        cntItemBtn.setBounds(50, 80, 80, 50);
        hintPanel.add(cntItemBtn);

        JButton twiceItemBtn = new JButton(twiceScoreItemImg);
        twiceItemBtn.setBounds(150, 80, 80, 50);
        hintPanel.add(twiceItemBtn);

        JButton ctgItemBtn = new JButton(categoryItemImg);
        ctgItemBtn.setBounds(250, 80, 80, 50);
        hintPanel.add(ctgItemBtn);


        canvasPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
        canvasPanel.setBounds(250,200,800,550);
        canvasPanel.setLayout(null);
//        canvasPanel.setBackground(Color.gray);
        canvasPanel.setBackground(new Color(79, 121, 66));
        mainPanel.add(canvasPanel);

        panel = new JPanel();
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
            }
        });
        btnNewButton1.setForeground(Color.RED);
        btnNewButton1.setBounds(50, 508, 62, 35);
        canvasPanel.add(btnNewButton1);


        JButton btnNewButton2 = new JButton(",");
        btnNewButton2.setBackground(Color.BLUE);
        btnNewButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 255);
            }
        });
        btnNewButton2.setForeground(Color.BLUE);
        btnNewButton2.setBounds(122, 508, 62, 35);
        canvasPanel.add((btnNewButton2));


        JButton btnNewButton3 = new JButton(",");
        btnNewButton3.setBackground(Color.YELLOW);
        btnNewButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(255, 255, 0);
            }
        });
        btnNewButton3.setForeground(Color.YELLOW);
        btnNewButton3.setBounds(194, 508, 62, 35);
        canvasPanel.add((btnNewButton3));


        JButton btnNewButton4 = new JButton(",");
        btnNewButton4.setBackground(Color.GREEN);
        btnNewButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(12, 134, 23);
            }
        });
        btnNewButton4.setForeground(Color.GREEN);
        btnNewButton4.setBounds(266, 508, 62, 35);
        canvasPanel.add((btnNewButton4));

        JButton btnNewButton5 = new JButton(",");
        btnNewButton5.setBackground(Color.BLACK);
        btnNewButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 0);
            }
        });
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

            }
        });


        chatingPanel = new JPanel(){
//            public void paintComponent(Graphics g) {
//                g.drawImage(chatPanelBg.getImage(), 0, 0, 380, 550, null);
//                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
//            }
        };
//        chatingPanel.setBackground(Color.white);
        chatingPanel.setLayout(null);
        chatingPanel.setOpaque(false);
        chatingPanel.setBounds(1060,200, 380, 550);
        mainPanel.add(chatingPanel);

        JScrollPane scrollPane = new JScrollPane(){
            public void paintComponent(Graphics g) {
                g.drawImage(chatPanelBg.getImage(), 0, 0, 380, 550, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scrollPane.setBounds(20, 0, 340, 440); //채팅창
        scrollPane.setBackground(new Color(0,0,0,0));
        chatingPanel.add(scrollPane);
        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        imgBtn = new JButton("+"); //+버튼
        imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
        imgBtn.setBounds(20, 450, 50, 40);
        chatingPanel.add(imgBtn);

        txtInput = new JTextField();
        txtInput.setBounds(80, 450, 210, 40); //채팅 치는 area
        chatingPanel.add(txtInput);
        txtInput.setColumns(10);

        btnSend = new JButton("Send"); //send button
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(300, 450, 70, 40);
        chatingPanel.add(btnSend);

//        textArea1 = new JTextPane();
//        textArea1.setEditable(true);
//        textArea1.setFont(new Font("굴림체", Font.PLAIN, 14));
//        scrollPane1.setViewportView(textArea1);
//        chatingPanel.add(textArea1);

        lblUserName = new JLabel("Name"); //이름 라벨
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
            socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            // SendMessage("/login " + UserName);
//                ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
//                SendObject(obcm);


        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }


    public void paint(Graphics g) {
        super.paint(g);
//        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        // Image 영역이 가려졌다 다시 나타날 때 그려준다.
        gc.drawImage(panelImage, 0, 0, this);
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

    public static void main(String[] args) {
        GameClientView game = new GameClientView("user", "192", "1111");

    }
}