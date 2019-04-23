package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

public class NotificationForm {
    private JPanel rootPanel;
    private JLabel userLoggedInLabel;
    private JLabel templateLabel;
    private JRadioButton templateRadio1;
    private JRadioButton templateRadio2;
    private JRadioButton templateRadio3;
    private JRadioButton noneRadio;
    private JLabel sendToLabel;
    private JRadioButton allRadio;
    private JRadioButton specificRadio;
    private JTextField specificField;
    private JTextArea notificationTextArea;
    private JLabel editTextLabel;
    private JButton sendNotificationButton;

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
