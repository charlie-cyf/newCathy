package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Serial")
class LoginUI extends JFrame {

    private String character;
    Object object;
    private final int WIDTH = 200, HEIGHT = 90;
    public LoginUI(Object object, String character) {
        this.character = character;
        this.object = object;
        setSize(WIDTH, HEIGHT);
        setTitle("Log In");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        draw();
    }

    private JButton logIn, quit;
    private JLabel welcome;
    private JTextField id_field;
    public void draw() {
        setLayout(new BorderLayout());
        welcome = new JLabel("Please Enter Your "+character+" ID", SwingConstants.CENTER);
        add(welcome, BorderLayout.NORTH);
        id_field = new JTextField();
        add(id_field, BorderLayout.CENTER);
        logIn = new JButton("Log In");
        logIn.addActionListener(new buttonHandler());
        add(logIn, BorderLayout.EAST);
        quit = new JButton("to main menu");
        quit.addActionListener(new buttonHandler());
        add(quit, BorderLayout.SOUTH);
    }

    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == logIn) {
                int id = Integer.parseInt(id_field.getText());
                // todo if successful
                if (character.equals("Employee")) {
                    EmployeeUI ui = (EmployeeUI) object;
                    ui.employeeID = id;
                    ui.repaint();
                    ui.setVisible(true);
                    setVisible(false);
                    dispose();
                } else if (character.equals("Manager")) {
                    // todo
                }
            } else if (source == quit) {
                BranchUI branchUI = new BranchUI();
                branchUI.setVisible(true);
                setVisible(false);
                dispose();
            }
        }
    }
}

