package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

public class NotificationForm {
    private JPanel rootPanel;
    private JLabel userLoggedInLabel;
    private JTextArea notificationTextArea;
    private JLabel editTextLabel;
    private JButton sendNotificationButton;
    private JLabel sendToLabel;
    private JRadioButton allRadio;
    private JRadioButton specificRadio;
    private JTextField recipientField;
    private JComboBox chooseTemplate;

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
