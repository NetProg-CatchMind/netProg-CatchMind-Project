package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameClientOver extends JFrame {
    public static final int SCREEN_WIDTH = 1500;
    public static final int SCREEN_HEIGHT = 800;


    private ImageIcon signBackgroundImg = new ImageIcon("res/signBackground.png");
    private ImageIcon logo = new ImageIcon("res/logo.png");

    private ImageIcon gameStartBtnPressed = new ImageIcon("res/gameStartBtnPress.png");


    private JPanel contentPane;


    public GameClientOver(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT+38);
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

        JButton gameQuitButton = new JButton(new ImageIcon("res/gameStartBtn.png")); //게임 종료
        gameQuitButton.setBounds(560, 610, 400, 100);
        gameQuitButton.setBorderPainted(false);
        gameQuitButton.setContentAreaFilled(false);
        gameQuitButton.setOpaque(false);
        gameQuitButton.setRolloverIcon(gameStartBtnPressed); // 버튼에 마우스가 올라갈떄 이미지 변환
        contentPane.add(gameQuitButton);

        JButton gameOverButton = new JButton(new ImageIcon("res/gameStartBtn.png")); //방으로 돌아가기
        gameOverButton.setBounds(560, 610, 400, 100);
        gameOverButton.setBorderPainted(false);
        gameOverButton.setContentAreaFilled(false);
        gameOverButton.setOpaque(false);
        gameOverButton.setRolloverIcon(gameStartBtnPressed); // 버튼에 마우스가 올라갈떄 이미지 변환
        contentPane.add(gameOverButton);

    }


}
