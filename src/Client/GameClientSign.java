package Client;
import com.sun.tools.javac.Main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;


public class GameClientSign extends JFrame {
    /**
     *
     */
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    private static final long serialVersionUID = 1L;
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

    private JButton char1, char2, char3;

    private String selCharNo;

    private GameClientMain main;

    public boolean isPressedChar1 = false, isPressedChar2 = false, isPressedChar3 = false;


//    private Image screenImage; // 스크린 화면
//    private Graphics screenGraphic; //스크린 그래픽
    /**
     * Launch the application.
     */


    /**
     * Create the frame.
     */
    public GameClientSign() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
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

        char1 = new JButton(char1Img);
        char1.setBounds(560,470,80,110);
        char1.setBorderPainted(false);
        char1.setContentAreaFilled(false);
        char1.setRolloverIcon(new ImageIcon("res/mouseOverChar1.png"));
        char1.setOpaque(false);
        char1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isPressedChar2 == true || isPressedChar3 == true) {
                    char1.setIcon(char1Img);
                    selCharNo = "char1";
                    return;
                }
//                if(isPressedChar2 == false && isPressedChar3 == false){
                else {
                    char1.setIcon(mouseOverChar1);
                    selCharNo = "char1";
//                }
                }
            }
        });
        this.add(char1);

        char2 = new JButton(new ImageIcon("res/character2.png"));
        char2.setBounds(710,470,80,110);
        char2.setBorderPainted(false);
        char2.setContentAreaFilled(false);
        char2.setRolloverIcon(new ImageIcon("res/mouseOverChar2.png"));
        char2.setOpaque(false);
        char2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(isPressedChar1 == true || isPressedChar3 == true){
                    char2.setIcon(char2Img);
                    selCharNo = "char2";
                    return;
                }
//                if(isPressedChar1 == false && isPressedChar3 == false){
                else{
                    char2.setIcon(mouseOverChar2);
                    selCharNo = "char2";
                }

//                }
            }

        });
        this.add(char2);

        char3 = new JButton(new ImageIcon("res/character3.png"));
        char3.setBounds(860,470,80,110);
        char3.setBorderPainted(false);
        char3.setContentAreaFilled(false);
        char3.setRolloverIcon(new ImageIcon("res/mouseOverChar3.png"));
        char3.setOpaque(false);
        char3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(isPressedChar1 == true || isPressedChar2 == true){
                    char3.setIcon(char3Img);
                    selCharNo = "char3";
                    return;
                }
//               if(isPressedChar1 == false && isPressedChar3 == false){
                else{
                    char3.setIcon(mouseOverChar3);
                    selCharNo = "char3";
                }

//                }
            }
        });
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

        CharAction charAction = new CharAction();
        char1.addActionListener(charAction);
        char2.addActionListener(charAction);
        char3.addActionListener(charAction);

    }

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


    class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);

            String username = txtUserName.getText().trim();
            String ip_addr = txtIpAddress.getText().trim();
            String port_no = txtPortNumber.getText().trim();

            main = new GameClientMain(username, ip_addr, port_no, selCharNo);

        }
    }

    class CharAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == "char1"){
                if(isPressedChar2 == false && isPressedChar3 == false){
//                    isPressedChar1 == true;
                }
            }
            else if(e.getSource() == "char2"){
                if(isPressedChar1 == false && isPressedChar3 == false){
//                    isPressedChar2 == true;
                }
            }
            else{
                if(isPressedChar1 == false && isPressedChar2 == false){
//                    isPressedChar3 == true;
                }
            }
        }
    }
}