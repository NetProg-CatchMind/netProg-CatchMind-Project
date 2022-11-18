package Client;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

class RoomMake extends JFrame {
    private Socket socket;
    private ObjectOutputStream oos;

    private String roomTitle, roomSubject;
    private int roomMemberCnt;

    private JLabel title, subject, num;

    public JTextField titleField;
    public JButton makeRoomBtn, cancleBtn;
    public JComboBox<String> subjectCombo, memberCntCombo; //주제, 최대 인원수
    private Vector<GameRoom> gameRooms;

    public RoomMake(Vector<GameRoom> gameRooms, Socket socket, ObjectOutputStream oos) {
        setResizable(false);
        setBounds(400, 200, 300, 300);
        setVisible(true);

        this.gameRooms = gameRooms;
        this.socket = socket;
        this.oos = oos;

        title = new JLabel("방제목 :");
        subject = new JLabel("방주제 :");
        num = new JLabel("인원수 :");

        titleField = new JTextField(15);

        String[] subjects = { "음식", "음악", "영화", "동물", "사물" };
        subjectCombo = new JComboBox<String>(subjects);

        String[] memberCnt = { "2", "3", "4",};
        memberCntCombo = new JComboBox<String>(memberCnt);

        makeRoomBtn = new JButton("만들기");
        RoomMakeAction roomMakeAction  = new RoomMakeAction();
        makeRoomBtn.addActionListener(roomMakeAction);
        cancleBtn = new JButton("취 소");

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel1.add(title);
        panel1.add(titleField);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel3.add(subject);
        panel3.add(subjectCombo);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel4.add(num);
        panel4.add(memberCntCombo);

        JPanel totpanel = new JPanel(new GridLayout(4, 1, 0, 0));
        totpanel.add(panel1);
        totpanel.add(panel3);
        totpanel.add(panel4);

        JPanel btpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btpanel.add(makeRoomBtn);
        btpanel.add(cancleBtn);

        Container c = this.getContentPane();
        c.add("Center", totpanel);
        c.add("South", btpanel);
    }

//                public String getTitle(){
//                    return titleField.getText();
//                }
//
//                public String getSubject(){
//                    return subjectCombo.getSelectedItem().toString();
//                }
//
//                public int getMemberCnt(){
//                    return Integer.parseInt(memberCntCombo.getSelectedItem().toString());
//                }

    class RoomMakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RoomMsg obmr = new RoomMsg(title.getText(), subject.getText(), roomMemberCnt);
//            SendObject(obmr);
                        try {
                            oos.writeObject(obmr);

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

            setVisible(false);

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
