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
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
public class GameClientMain extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;

    private ImageIcon backgroundImg = new ImageIcon("res/mainBackground.png");
    private JPanel userPanel;
    private JPanel loadingPanel;

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
    public GameClientMain(String username, String ip_addr, String port_no)  {
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        userPanel = new JPanel();
        userPanel.setBackground(Color.decode("#FFFF99"));
        userPanel.setBounds(10,10,SCREEN_WIDTH/2-10, SCREEN_HEIGHT-10);
        add(userPanel);

        loadingPanel = new JPanel();
        add(loadingPanel);



    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImg.getImage(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }



}
