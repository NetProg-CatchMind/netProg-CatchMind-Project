package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

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
    private JPanel contentPane;

    private JPanel loadingPanel; //프레임 바로 위에 놓인 왼쪽에 위치한 loading 화면을 보여줄 panel
    private JPanel userPanel; //프레임 바로 위에 놓인 오른쪽에 위치한 user정보들을 보여주는 panel

    private JPanel profilePanel; // userPanel위에 사용자의 프로필을 보여주는 panel
    private JLabel profileImgLabel;
    private JLabel profileInfoLabel;

    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JLabel userScoreLabel;

    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private JLabel profileLabel;




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
     * @throws BadLocationException
     */

    public GameClientMain(String username, String ip_addr, String port_no, String char_no)  {
        setUndecorated(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);


        loadingPanel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(loadingPanelImg.getImage(), 0, 0, SCREEN_WIDTH/2+2, SCREEN_HEIGHT+1, null);
                setOpaque(false);
            }
        };

//        loadingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        loadingPanel.setLayout(null);
        loadingPanel.setBounds(0,0,SCREEN_WIDTH/2+1,SCREEN_HEIGHT);
        add(loadingPanel);

        userPanel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(userPanelImg.getImage(), 0, 0, SCREEN_WIDTH/2+2, SCREEN_HEIGHT+1, null);
                setOpaque(false);
            }
        };

//        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userPanel.setLayout(null);
        userPanel.setBounds(SCREEN_WIDTH/2,0,SCREEN_WIDTH/2,SCREEN_HEIGHT);
        add(userPanel);


        profilePanel = new JPanel();
        profilePanel.setBounds(80,80,400,100);
        profilePanel.setBorder(BorderFactory.createLineBorder(Color.decode("#5D8A5D")));
        profilePanel.setBackground(Color.white);
        profilePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40,0));
//        profilePanel.setLayout(null);
        userPanel.add(profilePanel);

        char_no = "char1"; //앞에서 오류 해결 못해서 잠시 초기화.
        profileImgLabel = new JLabel();
        if(char_no == "char1") profileImgLabel.setIcon(new ImageIcon("res/profileChar1.png"));
        else if(char_no == "char2") profileImgLabel.setIcon(new ImageIcon("res/profileChar2.png"));
        else profileImgLabel.setIcon(new ImageIcon("res/profileChar3.png"));
        profileImgLabel.setBounds(100, 30, 10, 10);
        profileImgLabel.setText(null);
        profilePanel.add(profileImgLabel);

        profileInfoLabel = new JLabel(username);
        profileInfoLabel.setBounds(150,30,750, 200);
        profileInfoLabel.setFont(new Font("Serif", Font.PLAIN, 40));
        profilePanel.add(profileInfoLabel);

        scorePanel = new JPanel();
        scorePanel.setBounds(495,80,200,100);
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,10));
        scorePanel.setOpaque(false);
        userPanel.add(scorePanel);

        scoreLabel = new JLabel("SCORE");
        scoreLabel.setBounds(0,80,200,50);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        scorePanel.add(scoreLabel);

        userScoreLabel = new JLabel("user score");
        userScoreLabel.setBounds(0,130,200,50);
        userScoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        scorePanel.add(userScoreLabel);
    }

    public void paint(Graphics g) {
        super.paint(g);
//        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

}
