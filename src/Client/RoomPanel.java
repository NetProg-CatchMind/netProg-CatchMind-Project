package Client;

import Server.JoinMsg;
import Server.RoomMsg;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RoomPanel extends JPanel {

    private Socket socket;
    private ObjectOutputStream oos;
    private String roomId;
    private String title;
    private String subject;
    private String cnt;
    private String username, char_no;

    private JLabel subjectImg; // 주제에 따른 이미지 (왼쪽 크게)
    private JLabel titleLabel; //방제
    private JLabel subjectLabel; //주제
    private JButton joinButton; //방 입장.

    private JLabel cntLabel; //인원
    private JPanel memberList; //현재 참가자 그림 보여주기(캐릭터 아이콘)

    private final JPanel roomInfo;
    private ImageIcon roomImg;
//    private boolean isSel = false;
//    public boolean isSel() {
//        return isSel;
//    }
//
//    public void setSel(boolean selRoom) {
//        isSel = selRoom;
//    }


    public RoomPanel(String roomId, ImageIcon roomImg, String title, String subject, String cnt, String username, String char_no,  Socket socket, ObjectOutputStream oos){
        this.setBounds(0,0,600,600);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        this.roomId = roomId;
        this.title = title;
        this.subject = subject;
        this.cnt = cnt;
        this.socket = socket;
        this.oos = oos;

        subjectImg = new JLabel(roomImg);
        subjectImg.setSize(200,200);
        subjectImg.setBackground(Color.white);
        subjectImg.setVisible(true);
        subjectImg.setBounds(10,10, 200,200);
        add(subjectImg, BorderLayout.WEST);
//        add(subjectImg);

        roomInfo = new JPanel();
        roomInfo.setOpaque(true);
//        roomInfo.setVisible(false);
//        roomInfo.setBackground(Color.red);
        roomInfo.setLayout(null);
        add(roomInfo, BorderLayout.CENTER);

        titleLabel = new JLabel(title);
//        titleLabel.setOpaque(true);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setBounds(30,-15, 400,90);
        subjectImg.setVisible(true);
//        titleLabel.setBackground(Color.white);
        roomInfo.add(titleLabel);

        subjectLabel = new JLabel("주제 : "+subject);
//        subjectLabel.setOpaque(true);
        subjectLabel.setBounds(30, 30, 400,90);
        subjectLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        subjectImg.setVisible(true);
//        subjectLabel.setBackground(Color.white);
        roomInfo.add(subjectLabel);

        joinButton = new JButton("입장하기");
        joinButton.setBackground(Color.decode("#88B594"));
        joinButton.setFont(new Font("Serif", Font.PLAIN, 20));
        joinButton.setMargin(new Insets(50, 50, 50, 50));
        joinButton.setBounds(0,0,200,200);
        JoinAction joinAction = new JoinAction();
        joinButton.addMouseListener(joinAction);
        add(joinButton, BorderLayout.EAST);
    }

    class JoinAction implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            joinButton.setBackground(Color.decode("#43654C"));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            joinButton.setBackground(Color.decode("#43654C"));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            joinButton.setBackground(Color.decode("#43654C"));

            JoinMsg objr = new JoinMsg("1201", roomId, "", "","", username, char_no); //방만들기 프로토콜 번호 1200
            SendObject(objr);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            joinButton.setBackground(Color.decode("#43654C"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            joinButton.setBackground(Color.decode("#88B594"));
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

    public void AppendText(String msg) {
        // textArea.append(msg + "\n");
        // AppendIcon(icon1);
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
//        int len = textArea.getDocument().getLength();
        // 끝으로 이동
        //textArea.setCaretPosition(len);
        //textArea.replaceSelection(msg + "\n");

//        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
//        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
//        try {
//            doc.insertString(doc.getLength(), msg + "\n", left);
//        } catch (BadLocationException e) {

//            e.printStackTrace();
//        }
//        len = textArea.getDocument().getLength();
//        textArea.setCaretPosition(len);
    }
}
