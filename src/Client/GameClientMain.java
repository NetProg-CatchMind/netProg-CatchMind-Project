package Client;

import Server.ChatMsg;
import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
public class GameClientMain extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    private ImageIcon backgroundImg = new ImageIcon("res/basicBackground.png");
    //    private ImageIcon backgroundPanelImg = new ImageIcon("res/panelBackground.png");
    private ImageIcon loadingPanelImg = new ImageIcon("res/loadingPanelImg.png");
    private ImageIcon userPanelImg = new ImageIcon("res/userPanelImg.png");

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
    private ImageIcon coinImg = new ImageIcon("res/coinImg.png");

    private JTable questionItemTable;
    private JTable answerItemTabel;
    private JButton bgItemBtn;
    private JLabel bgItemLabel;
    private JButton timeItemBtn;
    private JLabel timeItemLabel;
    private JButton initialItemBtn;
    private JLabel initialItemLabel;
    private JButton wordCountBtn;
    private JLabel wordCountLabel;
    private JButton twiceScoreItemBtn;
    private JLabel twiceScoreItemLabel;
    private JButton categoryItemBtn;
    private JLabel categoryItemLabel;

    private Boolean isSelectedItem[] = {false,false,false,false,false,false };


    private JPanel loadingPanel; //프레임 바로 위에 놓인 왼쪽에 위치한 loading 화면을 보여줄 panel
    private JPanel userPanel; //프레임 바로 위에 놓인 오른쪽에 위치한 user정보들을 보여주는 panel

    private JPanel profilePanel; // userPanel위에 사용자의 프로필을 보여주는 panel
    private JLabel profileImgLabel;
    private JLabel profileInfoLabel;
    private int coin = 10;
    private JLabel profileCoinImgLabel;
    private JLabel profileCoinCntLabel;

    private int score = 0;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JLabel userScoreLabel;

    private JPanel itemPanel;


//    public JavaGameClientViewDrawing drawing;

    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private JLabel profileLabel;

    //Communication
//    public Socket socket = null;
    public PrintWriter pw = null;
    public BufferedReader br = null;


    private static final long serialVersionUID = 1L;

    private JTextField txtInput;
    private String UserName;
    private JButton btnSend;


    private JLabel lblUserName;
    // private JTextArea textArea;
    private JTextPane textArea;

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

    /**
     * Create the frame.
     *
     * @throws BadLocationException
     */

    public GameClientMain(String username, String ip_addr, String port_no, String char_no) {
//        setUndecorated(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT+38);
//        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(null);


        loadingPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(loadingPanelImg.getImage(), 0, 0, SCREEN_WIDTH / 2 + 2, SCREEN_HEIGHT + 1, null);
                setOpaque(false);
            }
        };

//        loadingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        loadingPanel.setLayout(null);
        loadingPanel.setBounds(0, 0, SCREEN_WIDTH / 2 + 1, SCREEN_HEIGHT);
        add(loadingPanel);

        userPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(userPanelImg.getImage(), 0, 0, SCREEN_WIDTH / 2 + 2, SCREEN_HEIGHT + 1, null);
                setOpaque(false);
            }
        };

//        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userPanel.setLayout(null);
        userPanel.setBounds(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        add(userPanel);


        profilePanel = new JPanel();
        profilePanel.setBounds(60, 100, 400, 100); //150, 200
        profilePanel.setBorder(BorderFactory.createLineBorder(Color.decode("#5D8A5D")));
        profilePanel.setBackground(Color.white);
        profilePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
//        profilePanel.setLayout(null);
        userPanel.add(profilePanel);


        profileImgLabel = new JLabel();
        System.out.println(char_no);
        if (char_no == "char1") profileImgLabel.setIcon(new ImageIcon("res/profileChar1.png"));
        else if (char_no == "char2") profileImgLabel.setIcon(new ImageIcon("res/profileChar2.png"));
        else profileImgLabel.setIcon(new ImageIcon("res/profileChar3.png"));
//        profileImgLabel.setBounds(30, 30, 10, 10);
        profileImgLabel.setText(null);
        profilePanel.add(profileImgLabel);

        profileInfoLabel = new JLabel(username);
//        profileInfoLabel.setBounds(100, 30, 750, 200);
        profileInfoLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        profilePanel.add(profileInfoLabel);

        profileCoinImgLabel = new JLabel(coinImg);
        profileCoinImgLabel.setBounds(400, 30, 30,30);
        profilePanel.add(profileCoinImgLabel);

        profileCoinCntLabel = new JLabel(String.valueOf(coin));
        profileCoinCntLabel.setBounds(500,30,80,50);
        profileCoinCntLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        profilePanel.add(profileCoinCntLabel);

        scorePanel = new JPanel();
        scorePanel.setBounds(495, 100, 200, 100);
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        scorePanel.setOpaque(false);
        userPanel.add(scorePanel);

        scoreLabel = new JLabel("SCORE");
        scoreLabel.setBounds(0, 100, 200, 50);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        scorePanel.add(scoreLabel);

        userScoreLabel = new JLabel(String.valueOf(score));
        userScoreLabel.setBounds(0, 130, 200, 50);
        userScoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        scorePanel.add(userScoreLabel);

        itemPanel = new JPanel();
        itemPanel.setBounds(20,220,700,550);
        itemPanel.setLayout(null);
        itemPanel.setOpaque(false);
        userPanel.add(itemPanel);

//        questionItemTable = new JTable([bgItemBtn, ]);
//        answerItemTabel = new JTable();

        bgItemBtn = new JButton(bgItemImg);
        bgItemBtn.setBounds(5,50, 260, 210);
        bgItemBtn.setIcon(bgItemImg);
        bgItemBtn.setBorderPainted(false);
        bgItemBtn.setContentAreaFilled(false);
        bgItemBtn.setFocusPainted(false);
        bgItemBtn.setOpaque(false);
        bgItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgItemBtn.setRolloverIcon(mouseOverBgItem);
        bgItemBtn.setSelectedIcon(mouseOverBgItem);
        itemPanel.add(bgItemBtn);

        timeItemBtn = new JButton(timeItemImg);
        timeItemBtn.setBounds(225,50, 260, 210);
        timeItemBtn.setIcon(timeItemImg);
        timeItemBtn.setBorderPainted(false);
        timeItemBtn.setContentAreaFilled(false);
        timeItemBtn.setFocusPainted(false);
        timeItemBtn.setOpaque(false);
        timeItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        timeItemBtn.setRolloverIcon(mouseOvertimeItem);
        timeItemBtn.setSelectedIcon(mouseOvertimeItem);
        itemPanel.add(timeItemBtn);

        initialItemBtn = new JButton(initialItemImg);
        initialItemBtn.setBounds(450,50, 260, 210);
        initialItemBtn.setIcon(initialItemImg);
        initialItemBtn.setBorderPainted(false);
        initialItemBtn.setContentAreaFilled(false);
        initialItemBtn.setFocusPainted(false);
        initialItemBtn.setOpaque(false);
        initialItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        initialItemBtn.setRolloverIcon(mouseOverInitialItem);
        initialItemBtn.setSelectedIcon(mouseOverInitialItem);
        itemPanel.add(initialItemBtn);

        bgItemLabel = new JLabel("배경 그림 보여주기");
        bgItemLabel.setBounds(5,10, 240, 30);
        bgItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bgItemLabel.setFont(new Font("Serif", Font.BOLD, 20));

        timeItemLabel = new JLabel("시간 2배 증가");
        timeItemLabel.setBounds(225,10, 240, 30);
        timeItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeItemLabel.setFont(new Font("Serif", Font.BOLD, 20));

        initialItemLabel = new JLabel("초성 보여주기");
        initialItemLabel.setBounds(450,10, 240, 30);
        initialItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        initialItemLabel.setFont(new Font("Serif", Font.BOLD, 20));

        itemPanel.add(bgItemLabel);
        itemPanel.add(timeItemLabel);
        itemPanel.add(initialItemLabel);


        wordCountBtn = new JButton(wordCountItemImg);
        wordCountBtn.setBounds(5,290, 260, 210);
        wordCountBtn.setIcon(wordCountItemImg);
        wordCountBtn.setBorderPainted(false);
        wordCountBtn.setContentAreaFilled(false);
        wordCountBtn.setFocusPainted(false);
        wordCountBtn.setOpaque(false);
        wordCountBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wordCountBtn.setRolloverIcon(mouseOverWordCntItem);
        wordCountBtn.setSelectedIcon(mouseOverWordCntItem);
        itemPanel.add(wordCountBtn);

        twiceScoreItemBtn = new JButton(twiceScoreItemImg);
        twiceScoreItemBtn.setBounds(225,290, 260, 210);
        twiceScoreItemBtn.setIcon(twiceScoreItemImg);
        twiceScoreItemBtn.setBorderPainted(false);
        twiceScoreItemBtn.setContentAreaFilled(false);
        twiceScoreItemBtn.setFocusPainted(false);
        twiceScoreItemBtn.setOpaque(false);
        twiceScoreItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        twiceScoreItemBtn.setRolloverIcon(mouseOverScoreItem);
        twiceScoreItemBtn.setSelectedIcon(mouseOverScoreItem);
        itemPanel.add(twiceScoreItemBtn);

        categoryItemBtn = new JButton(categoryItemImg);
        categoryItemBtn.setBounds(450,290, 260, 210);
        categoryItemBtn.setIcon(categoryItemImg);
        categoryItemBtn.setBorderPainted(false);
        categoryItemBtn.setContentAreaFilled(false);
        categoryItemBtn.setFocusPainted(false);
        categoryItemBtn.setOpaque(false);
        categoryItemBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        categoryItemBtn.setRolloverIcon(mouseOverCategoryItem);
        categoryItemBtn.setSelectedIcon(mouseOverCategoryItem);
        itemPanel.add(categoryItemBtn);

        wordCountLabel = new JLabel("글자 수 알려주기");
        wordCountLabel.setBounds(5,490, 240, 30);
        wordCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordCountLabel.setFont(new Font("Serif", Font.BOLD, 20));

        twiceScoreItemLabel = new JLabel("점수 두배");
        twiceScoreItemLabel.setBounds(225,490, 240, 30);
        twiceScoreItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        twiceScoreItemLabel.setFont(new Font("Serif", Font.BOLD, 20));

        categoryItemLabel = new JLabel("단어 테마 보여주기");
        categoryItemLabel.setBounds(450,490, 240, 30);
        categoryItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryItemLabel.setFont(new Font("Serif", Font.BOLD, 20));

        itemPanel.add(wordCountLabel);
        itemPanel.add(twiceScoreItemLabel);
        itemPanel.add(categoryItemLabel);

        GameClientMain.itemAction itemAction = new GameClientMain.itemAction();
        bgItemBtn.addActionListener(itemAction);
        timeItemBtn.addActionListener(itemAction);
        initialItemBtn.addActionListener(itemAction);
        wordCountBtn.addActionListener(itemAction);
        twiceScoreItemBtn.addActionListener(itemAction);
        categoryItemBtn.addActionListener(itemAction);


        try {
            socket = new Socket(ip_addr, Integer.parseInt(port_no));
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            // SendMessage("/login " + UserName);
            ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
            SendObject(obcm);

            ListenNetwork net = new ListenNetwork();
            net.start();
            TextSendAction action = new TextSendAction();
//            btnSend.addActionListener(action);
//            txtInput.addActionListener(action);
            txtInput.requestFocus();
            ImageSendAction action2 = new ImageSendAction();
            imgBtn.addActionListener(action2);


        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AppendText("connect error");
        }
    }

    class itemAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == bgItemBtn){
                isSelectedItem[0] = !isSelectedItem[0];
                if(isSelectedItem[0]){
                    coin --;
                    bgItemBtn.setIcon(mouseOverBgItem);
                }
                else {
                    coin ++;
                    bgItemBtn.setIcon(bgItemImg);
                }
            }

            if(e.getSource() == timeItemBtn){
                isSelectedItem[1] = !isSelectedItem[1];
                if(isSelectedItem[1]){
                    coin --;
                    timeItemBtn.setIcon(mouseOvertimeItem);
                }
                else{
                    coin ++;
                    timeItemBtn.setIcon(timeItemImg);
                }

            }

            if(e.getSource() == initialItemBtn){
                isSelectedItem[2] = !isSelectedItem[2];
                if(isSelectedItem[2]){
                    coin --;
                    initialItemBtn.setIcon(mouseOverInitialItem);
                }
                else{
                    coin ++;
                    initialItemBtn.setIcon(initialItemImg);
                }

            }

            if(e.getSource() == wordCountBtn){
                isSelectedItem[3] = !isSelectedItem[3];
                if(isSelectedItem[3]){
                    coin --;
                    wordCountBtn.setIcon(mouseOverWordCntItem);
                }
                else{
                    coin ++;
                    wordCountBtn.setIcon(wordCountItemImg);
                }

            }

            if(e.getSource() == twiceScoreItemBtn){
                isSelectedItem[4] = !isSelectedItem[4];
                if(isSelectedItem[4]){
                    coin --;
                    twiceScoreItemBtn.setIcon(mouseOverScoreItem);
                }
                else{
                    coin ++;
                    twiceScoreItemBtn.setIcon(twiceScoreItemImg);
                }

            }

            if(e.getSource() == categoryItemBtn){
                isSelectedItem[5] = !isSelectedItem[5];
                if(isSelectedItem[5]){
                    coin --;
                    categoryItemBtn.setIcon(mouseOverCategoryItem);
                }
                else{
                    coin ++;
                    categoryItemBtn.setIcon(categoryItemImg);
                }
            }
            profileCoinCntLabel.setText(String.valueOf(coin));
        }
    }

    // keyboard enter key 치면 서버로 전송
    class TextSendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Send button을 누르거나 메시지 입력하고 Enter key 치면
            if (e.getSource() == btnSend || e.getSource() == txtInput) {
                String msg = null;
                // msg = String.format("[%s] %s\n", UserName, txtInput.getText());
                msg = txtInput.getText();
                SendMessage(msg);
                txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
                txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
                if (msg.contains("/exit")) // 종료 처리
                    System.exit(0);
            }
        }
    }

    class ImageSendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
            if (e.getSource() == imgBtn) {
                frame = new Frame("이미지첨부");
                fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
                // frame.setVisible(true);
                // fd.setDirectory(".\\");
                fd.setVisible(true);
                // System.out.println(fd.getDirectory() + fd.getFile());
                if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
                    ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
                    ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
                    obcm.img = img;
                    SendObject(obcm);
                }
            }
        }
    }

    // Server Message를 수신해서 화면에 표시
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {

                    Object obcm = null;
                    String msg = null;
                    ChatMsg cm;
                    try {
                        obcm = ois.readObject();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        break;
                    }
                    if (obcm == null)
                        break;
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
                        case "300": // Image 첨부
                            if (cm.UserName.equals(UserName))
                                AppendTextR("[" + cm.UserName + "]");
                            else
                                AppendText("[" + cm.UserName + "]");
                            AppendImage(cm.img);
                            break;
                        case "500": // Mouse Event 수신
//                            drawing.DoMouseEvent(cm);
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

    // 화면에 출력
    public void AppendText(String msg) {
        // textArea.append(msg + "\n");
        // AppendIcon(icon1);
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        int len = textArea.getDocument().getLength();
        // 끝으로 이동
        //textArea.setCaretPosition(len);
        //textArea.replaceSelection(msg + "\n");

        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        try {
            doc.insertString(doc.getLength(), msg + "\n", left);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);


    }

    // 화면 우측에 출력
    public void AppendTextR(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);
        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
        try {
            doc.insertString(doc.getLength(), msg + "\n", right);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);

    }

    public void AppendImage(ImageIcon ori_icon) {
//        drawing.AppendImage(ori_icon);

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
        // ImageViewAction viewaction = new ImageViewAction();
        // new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
        // panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

        //gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
        //gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);

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

    // Server에게 network으로 전송
    public void SendMessage(String msg) {
        try {
            // dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
            ChatMsg obcm = new ChatMsg(UserName, "200", msg);
            oos.writeObject(obcm);
        } catch (IOException e) {
            // AppendText("dos.write() error");
            AppendText("oos.writeObject() error");
            try {
//				dos.close();
//				dis.close();
                ois.close();
                oos.close();
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
        try {
            oos.writeObject(ob);
        } catch (IOException e) {
            // textArea.append("메세지 송신 에러!!\n");
            AppendText("SendObject Error");
        }
    }

}
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {


            }
        }

    }



//    public void paint(Graphics g) {
//        super.paint(g);
////        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
//    }


