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
    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 600;

    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");

    private JPanel contentPane;


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

    /**
     * Create the frame.
     *
     * @throws BadLocationException
     */

    public GameClientView(String username, String ip_addr, String port_no) {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);


        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(620, 190, 270, 310); //채팅창
        contentPane.add(scrollPane);
        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(620, 70, 270, 100); //접속한 유저 정보창
        contentPane.add(scrollPane1);
        textArea1 = new JTextPane();
        textArea1.setEditable(true);
        textArea1.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane1.setViewportView(textArea1);


        lblNewLabel = new JLabel("   CatchMind Game    "); //안내 공지하는 label
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("나눔고딕", Font.PLAIN, 17));
        lblNewLabel.setBounds(35, 20, 550, 40);
        contentPane.add(lblNewLabel);

        txtInput = new JTextField();
        txtInput.setBounds(675, 510, 150, 30); //채팅 치는 area
        contentPane.add(txtInput);
        txtInput.setColumns(10);

        btnSend = new JButton("Send"); //send button
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(825, 510, 70, 30);
        contentPane.add(btnSend);

        btnExit = new JButton("방 나가기"); //Exit button
        btnExit.setFont(new Font("굴림", Font.PLAIN, 14));
        btnExit.setBounds(20, 520, 100, 40);
        contentPane.add(btnExit);

        lblUserName = new JLabel("Name"); //이름 라벨
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setBounds(620, 545, 90, 40);
        contentPane.add(lblUserName);
        setVisible(true);


        UserName = username;
        lblUserName.setText(username);

        imgBtn = new JButton("+"); //+버튼
        imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
        imgBtn.setBounds(620, 510, 50, 30);
        contentPane.add(imgBtn);

        JButton btnNewButton = new JButton("종 료"); //종료버튼
        btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                    ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
//                    SendObject(msg);
//                    System.exit(0);
            }
        });
        btnNewButton.setBounds(825, 545, 69, 40);
        contentPane.add(btnNewButton);

        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBackground(Color.WHITE);
        panel.setBounds(20, 70, 565, 430); //그림판 판넬
        contentPane.add(panel);
        gc = panel.getGraphics();

        // Image 영역 보관용. paint() 에서 이용한다.
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

        JButton buttonFirst = new JButton("첫글자");
        buttonFirst.setBounds(620, 20, 70, 40);
        contentPane.add(buttonFirst);

        JButton buttonInit = new JButton("초성");
        buttonInit.setBounds(710, 20, 70, 40);
        contentPane.add(buttonInit);

        JButton buttonBack = new JButton("배경");
        buttonBack.setBounds(800, 20, 70, 40);
        contentPane.add(buttonBack);

        JButton btnNewButton1 = new JButton(".");
        btnNewButton1.setBackground(Color.RED);
        btnNewButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                c = new Color(255, 0, 0);
            }
        });
        btnNewButton1.setForeground(Color.RED);
        btnNewButton1.setBounds(160, 520, 62, 42);
        contentPane.add(btnNewButton1);


        JButton btnNewButton2 = new JButton(",");
        btnNewButton2.setBackground(Color.BLUE);
        btnNewButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 255);
            }
        });
        btnNewButton2.setForeground(Color.BLUE);
        btnNewButton2.setBounds(222, 520, 62, 42);
        contentPane.add((btnNewButton2));


        JButton btnNewButton3 = new JButton(",");
        btnNewButton3.setBackground(Color.YELLOW);
        btnNewButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(255, 255, 0);
            }
        });
        btnNewButton3.setForeground(Color.YELLOW);
        btnNewButton3.setBounds(284, 520, 62, 42);
        contentPane.add((btnNewButton3));


        JButton btnNewButton4 = new JButton(",");
        btnNewButton4.setBackground(Color.GREEN);
        btnNewButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(12, 134, 23);
            }
        });
        btnNewButton4.setForeground(Color.GREEN);
        btnNewButton4.setBounds(346, 520, 62, 42);
        contentPane.add((btnNewButton4));

        JButton btnNewButton5 = new JButton(",");
        btnNewButton5.setBackground(Color.BLACK);
        btnNewButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c = new Color(0, 0, 0);
            }
        });
        btnNewButton5.setForeground(Color.BLACK);
        btnNewButton5.setBounds(404, 520, 62, 42);
        contentPane.add((btnNewButton5));


        JButton btnNewButton6 = new JButton("지우개"); //지우개버튼
        btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton6.setBounds(500, 520, 85, 30);
        contentPane.add(btnNewButton6);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

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
        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
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