package Client;
import com.sun.tools.javac.Main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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

        contentPane = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(signBackgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel logoLabel = new JLabel(logo);
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
        contentPane.add(txtUserName);
        txtUserName.setColumns(10);

        JLabel lblIpAddress = new JLabel("IP Address");
        lblIpAddress.setBounds(520, 370, 250, 50);
        lblIpAddress.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(lblIpAddress);

        txtIpAddress = new JTextField();
        txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
        txtIpAddress.setText("127.0.0.1");
        txtIpAddress.setBounds(680, 370, 350, 50);
        contentPane.add(txtIpAddress);

        JLabel lblPortNumber = new JLabel("Port Number");
        lblPortNumber.setBounds(520, 490, 250, 50);
        lblPortNumber.setFont(new Font("Serif", Font.PLAIN, 25));
        contentPane.add(lblPortNumber);

        txtPortNumber = new JTextField();
        txtPortNumber.setText("30000");
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setColumns(10);
        txtPortNumber.setBounds(680, 490, 350, 50);
        contentPane.add(txtPortNumber);

        setVisible(true);
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
            GameClientMain main = new GameClientMain(username, ip_addr, port_no);
        }
    }
}

//    JPanel loginPanel = new JPanel(){
//        public void paintComponent(Graphics g) {
//            g.drawImage(backgroundImg, 0, 0,1500,800, null);
//            setOpaque(false); //그림을 표시하게 설정,투명하게 조절
//            super.paintComponent(g);
//        }
//    };
////        loginPanel.setSize(700,800);
//        loginPanel.setVisible(true);
//                loginPanel.setBackground(Color.BLACK);
//                loginPanel.setBounds(150, 150, 700, 800);