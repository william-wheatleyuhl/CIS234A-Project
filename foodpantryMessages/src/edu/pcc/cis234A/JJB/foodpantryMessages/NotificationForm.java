package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

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
    SubscriberDB subs = new SubscriberDB();
    ArrayList<Template> templates = subs.readTemplates();

    public NotificationForm() {

        populateComboBox();
        allRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(allRadio.isSelected()) {
                    recipientField.setEnabled(false);
                    specificRadio.setSelected(false);
                }
            }
        });

        specificRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(specificRadio.isSelected()) {
                    recipientField.setEnabled(true);
                    allRadio.setSelected(false);
                }
            }
        });
        chooseTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch(chooseTemplate.getSelectedIndex() ) {
                    case 0:     notificationTextArea.setText("");
                                break;
                    case 1:     notificationTextArea.setText(templates.get(0).messageText);
                                break;
                    case 2:     notificationTextArea.setText(templates.get(1).messageText);
                                break;
                    case 3:     notificationTextArea.setText(templates.get(2).messageText);
                                break;
                }
            }
        });
    }

    public void populateComboBox() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) chooseTemplate.getModel();
        model.removeAllElements();
        model.addElement("No Template");
        for(Template temp : templates) {
            model.addElement(temp.templateName);
        }
        chooseTemplate.setModel(model);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}
