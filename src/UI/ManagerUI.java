package UI;

import javax.swing.*;

@SuppressWarnings("serial")
class ManagerUI extends JFrame {

    private final int WIDTH = 500, HEIGHT = 1000;
    ManagerUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("ManagerUI");
        setVisible(false);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        draw();
        LoginUI loginUI = new LoginUI(this, "Manager");
        loginUI.setVisible(true);
        loginUI.setResizable(false);
    }

    private JPanel buttonsPanel, northPanel;
//    private buttonHandler handler;
    public void draw() {

    }


}
