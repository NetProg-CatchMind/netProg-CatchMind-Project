package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
public class GameClientView extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");

    private JPanel contentPane;


    private static final long serialVersionUID = 1L;

    private JTextField txtInput;
    private String UserName;
    private JButton btnSend;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

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
    public GameClientView(String username, String ip_addr, String port_no)  {
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
        scrollPane.setBounds(12, 10, 352, 471);
        contentPane.add(scrollPane);

        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        txtInput = new JTextField();
        txtInput.setBounds(74, 489, 209, 40);
        contentPane.add(txtInput);
        txtInput.setColumns(10);

        btnSend = new JButton("Send");
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(295, 489, 69, 40);
        contentPane.add(btnSend);

        lblUserName = new JLabel("Name");
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setBounds(12, 539, 62, 40);
        contentPane.add(lblUserName);
        setVisible(true);


        UserName = username;
        lblUserName.setText(username);

        imgBtn = new JButton("+");
        imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
        imgBtn.setBounds(12, 489, 50, 40);
        contentPane.add(imgBtn);

        JButton btnNewButton = new JButton("종 료");
        btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                    ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
//                    SendObject(msg);
//                    System.exit(0);
            }
        });
        btnNewButton.setBounds(295, 539, 69, 40);
        contentPane.add(btnNewButton);

        panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBackground(Color.WHITE);
        panel.setBounds(376, 10, 400, 520);
        contentPane.add(panel);
        gc = panel.getGraphics();

        // Image 영역 보관용. paint() 에서 이용한다.
        panelImage = createImage(panel.getWidth(), panel.getHeight());
        gc2 = panelImage.getGraphics();
        gc2.setColor(panel.getBackground());
        gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
        gc2.setColor(Color.BLACK);
        gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);

        lblMouseEvent = new JLabel("<dynamic>");
        lblMouseEvent.setHorizontalAlignment(SwingConstants.CENTER);
        lblMouseEvent.setFont(new Font("굴림", Font.BOLD, 14));
        lblMouseEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblMouseEvent.setBackground(Color.WHITE);
        lblMouseEvent.setBounds(376, 539, 400, 40);
        contentPane.add(lblMouseEvent);

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



}
