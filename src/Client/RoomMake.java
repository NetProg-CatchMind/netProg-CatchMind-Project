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
import Server.RoomMsg;

class RoomMake extends JFrame {

    private String username;
    private Socket socket;
    private ObjectOutputStream oos;

    private JLabel title, subject, num;

    public JTextField titleField;
    public JButton makeRoomBtn, cancleBtn;
    public JComboBox<String> subjectCombo, memberCntCombo; //주제, 최대 인원수

    private Vector<GameRoom> gameRooms;
    String[] subjects = { "food", "music", "movie", "animal", "thing" };

    private ImageIcon subFoodImg = new ImageIcon("res/subFood.png");
    private ImageIcon subMusicImg = new ImageIcon("res/subMusic.png");
    private ImageIcon subMovieImg = new ImageIcon("res/subMovie.png");
    private ImageIcon subAnimalImg = new ImageIcon("res/subAnimal.png");
    private ImageIcon subThingImg = new ImageIcon("res/subThing.png");
    private ImageIcon bgImg = new ImageIcon("res/makeRoomFrameBg.png");

    private ImageIcon roomImg;

    public RoomMake(String username, Socket socket, ObjectOutputStream oos) {
        setResizable(false);
        setBounds(400, 200, 300, 300);
        setVisible(true);
        RoomMakePanel rmPanel = new RoomMakePanel();
        rmPanel.setLayout(null);
        setContentPane(rmPanel);

        this.username = username;
        this.socket = socket;
        this.oos = oos;

        title = new JLabel("방제목 :");
        subject = new JLabel("방주제 :");
        num = new JLabel("인원수 :");

        titleField = new JTextField(15);

//        String[] subjects = { "음식", "음악", "영화", "동물", "사물" };
        subjectCombo = new JComboBox<String>(subjects);
        SubjectAction subAction = new SubjectAction();
        subjectCombo.addActionListener(subAction);

        String[] memberCnt = { "2", "3", "4",};
        memberCntCombo = new JComboBox<String>(memberCnt);

        makeRoomBtn = new JButton("만들기");
        RoomMakeAction roomMakeAction  = new RoomMakeAction();
        makeRoomBtn.addActionListener(roomMakeAction);
        cancleBtn = new JButton("취 소");
        CancleAction cancleAction = new CancleAction();
        cancleBtn.addActionListener(cancleAction);

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel1.setOpaque(false);
        panel1.add(title);
        panel1.add(titleField);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel3.setOpaque(false);
        panel3.add(subject);
        panel3.add(subjectCombo);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel4.setOpaque(false);
        panel4.add(num);
        panel4.add(memberCntCombo);

        JPanel totpanel = new JPanel(new GridLayout(4, 1, 0, 0));
        totpanel.setBounds(50,40,200,180);
//        totpanel.setBounds(40,50,150,150);
        totpanel.setOpaque(false);
        totpanel.add(panel1);
        totpanel.add(panel3);
        totpanel.add(panel4);

        JPanel btpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btpanel.setBounds(70,195,150,100);
        btpanel.setOpaque(false);
        btpanel.add(makeRoomBtn);
        btpanel.add(cancleBtn);

        Container c = this.getContentPane();
        rmPanel.add("Center", totpanel);
        rmPanel.add("South", btpanel);

    }

    // JPanel을 상속받는 새 패널 구현
    class RoomMakePanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            g.drawImage(bgImg.getImage(),-5,-5,300,275, null);

        }
    }

    class RoomMakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String roomId, title, subject;
            int cnt;

            title = titleField.getText();
            subject = subjects[subjectCombo.getSelectedIndex()];
            cnt = Integer.parseInt(memberCntCombo.getSelectedItem().toString());
            roomId = "room@" + username+ "/" + title+ "/" + subject;
            Client.RoomMsg obmr = new Client.RoomMsg("1200", ""); //방만들기 프로토콜 번호 1200
            obmr.roomId = roomId;
            obmr.roomTitle = title;
            obmr.roomSubject = subject;
            obmr.userCnt = cnt;
            SendObject(obmr);

            setVisible(false);
        }
    }

    class SubjectAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
//            if(e.getSource() == "food") roomImg = subFoodImg;
//            if(e.getSource() == "music") roomImg = subMusicImg;
//            if(e.getSource() == "movie") roomImg = subMovieImg;
//            if(e.getSource() == "animal") roomImg = subAnimalImg;
//            if(e.getSource() == "thing") roomImg = subThingImg;

        }
    }

    class CancleAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
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
