package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * NotificationForm Class
 * This class defines the functionality of the GUI for sending Foot Pantry based Messages.
 * The user is able to select what subscribers the message is sent to, type a message, select a message from a template,
 * and send the message. The templates are pulled from the TEMPLATE table in the Database, and all sent messages will
 * be stored in the MESSAGES table and given a unique messageID.
 * @authors Syn Calvo and Will Wheatley-Uhl
 * 2019.04.19
 */
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
    ArrayList<Recipient> recipUsers;
    Iterator<Recipient> recipients = subs.readSubscriberNames().iterator();

    public NotificationForm() {

        populateComboBox();
        allRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(allRadio.isSelected()) {
                    recipientField.setEnabled(false);
                    recipientField.setText(gatherRecipients());
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
/**
 * Action Listener for the chooseTemplate comboBox. Selecting the first option in the pull-down
*/
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

    public String gatherRecipients() {
        String toList = "";
        while(recipients.hasNext()) {
            toList += recipients.next().getUserName();
            if(recipients.hasNext()) {
                toList += ", ";
            }
        }
        return toList;
    }
}
