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
    private JComboBox groupSelect;
    private JComboBox chooseTemplate;
    SubscriberDB subs = new SubscriberDB();
    ArrayList<Template> templates = subs.readTemplates();
    ArrayList<Recipient> recipients = subs.readSubscriberData();

    public NotificationForm() {
        populateTemplateMenu();
        poulateRecipientMenu();

        /**
         * Action Listener for the "All Recipient" radio button. Selecting this populates the recipient text field
         * with the usernames from the Database. Deselects the "Specific Recipients" radio, and disables the Recipients
         * field from editing.
         */
        allRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(allRadio.isSelected()) {
                    groupSelect.setEnabled(false);
                    specificRadio.setSelected(false);
                }
            }
        });
/**
 * Action Listener for the "Specific Users" radio button. Disables the "All Users" radio, sets text of Recipient
 * List to an empty text field.
 * TODO: Parse Recipient field to detect usernames.
 */
        specificRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(specificRadio.isSelected()) {
                    groupSelect.setEnabled(true);
                    allRadio.setSelected(false);
                }
            }
        });
/**
 * Action Listener for the chooseTemplate comboBox. Selecting the first option in the pull-down
 * clears the message field.
 * TODO: Cache Message when switching between templates and new messages.
*/
        chooseTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = chooseTemplate.getSelectedIndex() - 1;
                if(chooseTemplate.getSelectedIndex() == 0) {
                    notificationTextArea.setText("");
                } else {
                notificationTextArea.setText(templates.get(selectedIndex).messageText);
                }
            }
        });

        sendNotificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                subs.logMessage(getMessageText());
                System.out.println(getMessageText());
            }
        });
    }

    /**
     * Create a Model for the ComboBox pulldown menu for message templates and populate it from saved Templates
     * found in the database. Currently set with one default option and 3 template options.
     */
    public void populateTemplateMenu() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) chooseTemplate.getModel();
        model.removeAllElements();
        model.addElement("No Template");
        for(Template temp : templates) {
            model.addElement(temp.templateName);
        }
        chooseTemplate.setModel(model);
    }

    public void poulateRecipientMenu() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) groupSelect.getModel();
        model.removeAllElements();
        int[] groupCounts = getGroupCounts();
        model.addElement("Select Recipients...");
        model.addElement("Managers: " + groupCounts[0]);
        model.addElement("Staff: " + groupCounts[1]);
        model.addElement("Subscribers: " + groupCounts[2]);
        chooseTemplate.setModel(model);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Converts ArrayList of recipients into an Iterator Class.Iterates through the list of usernames
     * in the Subscribers List. Returns a string of all Usernames in the Database.
     * @return toList A list of all subscriber's Usernames
     */
    public int[] getGroupCounts() {
//        Iterator<Recipient> recipientStrings = recipients.iterator();
        int[] groupCount = new int[3];
        for(Recipient recipient : recipients) {
            switch(recipient.getSubscriberRole()) {
                case 1: groupCount[0]++;
                        break;
                case 2: groupCount[1]++;
                        break;
                case 3: groupCount[2]++;
                        break;
            }
        }
        return groupCount;
    }

    public String getMessageText() {
        return notificationTextArea.getText();
    }
}
