package Client;
import com.sun.tools.javac.Main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GameClientSign extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    private static final long serialVersionUID = 1L;

    //UI 변수들 선언====================================================================================================
    private JPanel contentPane;
    private JTextField txtUserName;
    private JTextField txtIpAddress;
    private JTextField txtPortNumber;

    private ImageIcon signBackgroundImg = new ImageIcon("res/signBackground.png");
    private ImageIcon gameStartBtn = new ImageIcon("res/gameStartBtn.png");
    private ImageIcon gameStartBtnPressed = new ImageIcon("res/gameStartBtnPress.png");
    private ImageIcon logo = new ImageIcon("res/logo.png");

    private ImageIcon char1Img = new ImageIcon("res/character1.png");
    private ImageIcon mouseOverChar1 = new ImageIcon("res/mouseOverChar1.png");

    private ImageIcon char2Img = new ImageIcon("res/character2.png");
    private ImageIcon mouseOverChar2 = new ImageIcon("res/mouseOverChar2.png");

    private ImageIcon char3Img = new ImageIcon("res/character3.png");
    private ImageIcon mouseOverChar3 = new ImageIcon("res/mouseOverChar3.png");

    private ButtonGroup g;
    private JRadioButton char1, char2, char3;


    private String selCharNo;


    // 다음으로 넘어갈 Frame들 변수 =====================================================================================
    private GameClientMain main;
    private GameClientView view;


    //프레임 만들기 ===================================================================================================
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameClientSign frame = new GameClientSign();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //GameClientSign 프레임 기본 설정 ==================================================================================
    public GameClientSign() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
//        setResizable(false);
//        setLocationRelativeTo(null);
//        setUndecorated(true);
        setVisible(true);

        contentPane = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(signBackgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(logo);
        logoLabel.setBounds(380,60,750,140);
        logoLabel.setText(null);
        this.add(logoLabel);

        JLabel lblNewLabel = new JLabel("User Name");
        lblNewLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        lblNewLabel.setBounds(520, 250, 250, 50);
        contentPane.add(lblNewLabel);

        txtUserName = new JTextField();
        txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
        txtUserName.setBounds(680, 250, 350, 50);
        txtUserName.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(txtUserName);
        txtUserName.setColumns(10);

        JLabel lblIpAddress = new JLabel("IP Address");
        lblIpAddress.setBounds(520, 320, 250, 50);
        lblIpAddress.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(lblIpAddress);

        txtIpAddress = new JTextField();
        txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
        txtIpAddress.setText("127.0.0.1");
        txtIpAddress.setBounds(680, 320, 350, 50);
        txtIpAddress.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(txtIpAddress);

        JLabel lblPortNumber = new JLabel("Port Number");
        lblPortNumber.setBounds(520, 390, 250, 50);
        lblPortNumber.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(lblPortNumber);

        txtPortNumber = new JTextField();
        txtPortNumber.setText("30000");
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setColumns(10);
        txtPortNumber.setBounds(680, 390, 350, 50);
        txtPortNumber.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(txtPortNumber);

        g = new ButtonGroup();

        char1 = new JRadioButton("char1", char1Img);
        char1.setBounds(560,470,90,110);
        char1.setBorderPainted(false);
        char1.setContentAreaFilled(false);
        char1.setOpaque(false);
        char1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        char1.setRolloverIcon(mouseOverChar1);
        char1.setSelectedIcon(mouseOverChar1);

        char2 = new JRadioButton("char2", char2Img);
        char2.setBounds(710,470,90,110);
        char2.setBorderPainted(false);
        char2.setContentAreaFilled(false);
        char2.setOpaque(false);
        char2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        char2.setRolloverIcon(mouseOverChar2);
        char2.setSelectedIcon(mouseOverChar2);

        char3 = new JRadioButton("char3", char3Img);
        char3.setBounds(860,470,90,110);
        char3.setBorderPainted(false);
        char3.setContentAreaFilled(false);
        char3.setOpaque(false);
        char3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        char3.setRolloverIcon(mouseOverChar3);
        char3.setSelectedIcon(mouseOverChar3);

        CharAction charAction = new CharAction();
        char1.addActionListener(charAction);
        char2.addActionListener(charAction);
        char3.addActionListener(charAction);

        g.add(char1);
        g.add(char2);
        g.add(char3);

        this.add(char1);
        this.add(char2);
        this.add(char3);

        JButton gameStartBtn = new JButton(new ImageIcon("res/gameStartBtn.png"));
        gameStartBtn.setBounds(560, 610, 400, 100);
        gameStartBtn.setBorderPainted(false);
        gameStartBtn.setContentAreaFilled(false);
        gameStartBtn.setOpaque(false);
        gameStartBtn.setRolloverIcon(gameStartBtnPressed); // 버튼에 마우스가 올라갈떄 이미지 변환
        contentPane.add(gameStartBtn);

        Myaction action = new Myaction();
        gameStartBtn.addActionListener(action);
        txtUserName.addActionListener(action);
        txtIpAddress.addActionListener(action);
        txtPortNumber.addActionListener(action);
    }



    // GameStart 버튼 눌렀을 때 액션 처리 -> GameClientMain 실행 ========================================================
    class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);

            String username = txtUserName.getText().trim();
            String ip_addr = txtIpAddress.getText().trim();
            String port_no = txtPortNumber.getText().trim();
            String char_no = selCharNo;

            main = new GameClientMain(username, ip_addr, port_no, char_no);
//            view = new GameClientView(username, ip_addr, port_no);
//            return null;
        }
    }

    // TODO Not Complete
    // 각 character 눌렀을 때, 캐릭터 설정 액션 처리 =====================================================================
    class CharAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == "char1"){
//                char1.setCursor(Cursor.getDefaultCursor());
                selCharNo = "char1";
            }

            else if(e.getSource() == "char2"){
//                char2.setCursor(Cursor.getDefaultCursor());
                selCharNo = "char2";
            }

            else{
//                char3.setCursor(Cursor.getDefaultCursor());
                selCharNo = "char3";
            }


        }
    }
}