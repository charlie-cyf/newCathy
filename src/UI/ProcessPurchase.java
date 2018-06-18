package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Serial")
public class ProcessPurchase extends JFrame {

    private final int WIDTH = 400, HEIGHT = 700;
    public ProcessPurchase() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Task: Process Purchase");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.draw();
    }

    private JPanel processPanel;
    private JScrollPane tablePanel;
    private buttonHandler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        drawProcessPanel();
        drawTablePanel();
        add(processPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JButton add, delete, deleteLast;
    private JTextField textField;
    private void drawProcessPanel() {
        processPanel = new JPanel();
        processPanel.setLayout(new BorderLayout());
        textField = new JTextField();
        processPanel.add(textField, BorderLayout.CENTER);

        add = new JButton("Add");
        add.addActionListener(handler);
        delete = new JButton("Delete");
        delete.addActionListener(handler);
        deleteLast = new JButton("Delete Last Item");
        deleteLast.addActionListener(handler);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3));
        buttons.add(add);
        buttons.add(delete);
        buttons.add(deleteLast);
        processPanel.add(buttons, BorderLayout.SOUTH);
    }

    JTextArea area;
    private void drawTablePanel() {
        area = new JTextArea("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n1");
        area.setBackground(Color.black);
        area.setForeground(Color.WHITE);
        tablePanel = new JScrollPane(area);
    }

    private class buttonHandler implements ActionListener {
        @Override
        // todo
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == add) {

            } else if (source == delete) {

            } else if (source == deleteLast) {

            }
        }
    }
}
