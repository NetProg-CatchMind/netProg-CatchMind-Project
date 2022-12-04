package Client;

import Server.HintMsg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GameClientOver extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;


    private ImageIcon signBackgroundImg = new ImageIcon("res/signBackground.png");
    private ImageIcon logo = new ImageIcon("res/logo.png");

    private ImageIcon gameStartBtnPressed = new ImageIcon("res/gameStartBtnPress.png");
    private ImageIcon scorePanelImg = new ImageIcon("res/scorePanelImg.png");


    private JPanel contentPane;
    HashMap<String, String> scoreMap = new HashMap<String, String>();


    public GameClientOver(String scores){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
        setVisible(true);

        String[] scoreList = scores.split("/");
        System.out.println(scoreList[0]);
        for(int i=0; i<scoreList.length; i++){
            String[] score = scoreList[i].split(" ");
            scoreMap.put(score[0], score[1]);
        }

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

        JPanel scoreTitlePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(scorePanelImg.getImage(), 0, 0, 500, 400, null);
                // Image 영역이 가려졌다 다시 나타날 때 그려준다.
            }
        };
        scoreTitlePanel.setBounds(500,200, 500, 400);
        scoreTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scoreTitlePanel.setOpaque(false);
        this.add(scoreTitlePanel);


        int i=1;
        for(Map.Entry<String, String> score : scoreMap.entrySet()){
            JPanel scorePanel = new JPanel();
            scorePanel.setBounds(500,200+(100*i), 400, 100);
            scorePanel.setOpaque(false);
            scorePanel.setLayout(null);
            this.add(scorePanel);
            System.out.println("score!!!"+scoreMap.toString());

            JLabel userLabel = new JLabel(score.getKey());
            userLabel.setBounds(50,0,200,100);
            userLabel.setFont(new Font("Serif", Font.PLAIN, 50));
            scorePanel.add(userLabel);
            JLabel scoreLabel = new JLabel(score.getValue());
            scoreLabel.setBounds(350,0,200,100);
            scoreLabel.setFont(new Font("Serif", Font.PLAIN, 50));
            scorePanel.add(scoreLabel);

            i++;

        }

        JButton gameOverButton = new JButton(new ImageIcon("res/gameStartBtn.png")); //방으로 돌아가기
        gameOverButton.setBounds(560, 610, 400, 100);
        gameOverButton.setBorderPainted(false);
        gameOverButton.setContentAreaFilled(false);
        gameOverButton.setOpaque(false);
        gameOverButton.setRolloverIcon(gameStartBtnPressed); // 버튼에 마우스가 올라갈떄 이미지 변환
        contentPane.add(gameOverButton);

        gameOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameClientSign sign = new GameClientSign();
                setVisible(false);
                sign.setVisible(true);
            }
        });

    }

}
