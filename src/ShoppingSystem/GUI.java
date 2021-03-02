package ShoppingSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JPanel rootPanel;
    private JButton pressMeButton;
    private JLabel testLabel;

    public GUI() {
        pressMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testLabel.setText("Testing... You clicked!");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello World");
        frame.setContentPane(new GUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
