import javax.swing.*;
import java.awt.*;

/**
 * @author Syn
 * @version 2019.04.10
 */
public class notificationForm {
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
    private JButton sendNotificationButton;
    private JPanel rootPanel;
    private JLabel editTextLabel;

    public notificationForm() {
        rootPanel.setPreferredSize(new Dimension(500, 350));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
