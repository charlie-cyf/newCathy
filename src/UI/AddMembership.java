package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Serial")
public class AddMembership extends JFrame {

    private final int WIDTH = 300, HEIGHT = 100;
    public AddMembership() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Task: Add Membership");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.draw();
    }

    JButton add;
    JPanel inputPanel;
    private void draw() {
        setLayout(new BorderLayout());
        add = new JButton("Add Member");
        add.addActionListener(new buttonHandler());
        add(add, BorderLayout.SOUTH);
        drawInputPanel();
        add(inputPanel, BorderLayout.CENTER);
    }

    JLabel label_name, label_phone;
    JTextArea ta_name, ta_phoneNumber;
    private void drawInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(5);
        JPanel unchangable = new JPanel(layout);
        label_name = new JLabel("Name: ");
        label_phone = new JLabel("Phone Number: ");
        unchangable.add(label_name);
        unchangable.add(label_phone);
        inputPanel.add(unchangable, BorderLayout.WEST);
        JPanel changable = new JPanel(layout);
        ta_name = new JTextArea();
        ta_phoneNumber = new JTextArea();
        changable.add(ta_name);
        changable.add(ta_phoneNumber);
        inputPanel.add(changable, BorderLayout.CENTER);
    }

    private class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == add) {
                try {
                    String name = ta_name.getText();
                    String phoneNumber = ta_phoneNumber.getText();
                    if (Constraints.ifNameFormattingWrong(name)) throw new FormattingException("Wrong Name Format");
                    if (Constraints.ifPhoneFormatWrong(phoneNumber)) throw new FormattingException("Wrong Phone Number Format");
                } catch (FormattingException f) {
                    f.printError();
                }
                // todo get membership id
                int membershipID = 111;
                NotificationUI ui = new NotificationUI("Member "+ta_name.getText()+" is added!",
                        "Membership# is "+membershipID, "Congratulations");
                ui.setVisible(true);
                // todo add member to database
                ta_name.setText("");
                ta_phoneNumber.setText("");
            }
        }
    }
}
