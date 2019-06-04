package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * NotificationForm Class
 * This class defines the functionality of the GUI for sending Foot Pantry based Messages.
 * The user is able to select what subscribers the message is sent to, type a message, select a message from a template,
 * and send the message. The templates are pulled from the TEMPLATE table in the Database, and all sent messages will
 * be stored in the MESSAGES table and given a unique messageID.
 * @authors Syn Calvo and Will Wheatley-Uhl
 * @version 2019.05.25
 */

public class NotificationForm {
    private JPanel rootPanel;
    private JTextArea notificationTextArea;
    private JLabel editTextLabel;
    private JButton sendNotificationButton;
    private JLabel sendToLabel;
    private JList groupSelect;
    private JComboBox chooseTemplate;
    private String currentUser;
    private ArrayList<Integer> selectedGroups = new ArrayList<>();
    private SubscriberDB subs = new SubscriberDB();
    private ArrayList<Template> templates = subs.readTemplates();
    private ArrayList<Recipient> subscribers = subs.readSubscriberData();
    private HashMap<Integer, ArrayList<Integer>> groups = subs.getGroupMakeup();


    public NotificationForm(String currUser) {
        this.currentUser = currUser;
        populateTemplateMenu();
        populateRecipientMenu();
        getCurrentUserID();
        System.out.println(groups.toString());

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
                    notificationTextArea.setEnabled(true);
                } else {
                notificationTextArea.setText(templates.get(selectedIndex).messageText);
                notificationTextArea.setEnabled(false);
                }
            }
        });

        /**
         * Action Listener for the "Send" button. Checks if message is empty, parses for tags, sends message, logs
         * messages.
         */
        sendNotificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Integer> recipientIDs = new ArrayList<>();
                TagParser parser = new TagParser(getMessageText());
                if(checkMessageContent()) {
                    String parsedMessage = parser.returnParsedMessage();
                    ArrayList<Recipient> recipients = buildRecipientList();
                    for(Recipient recipient : recipients) {
                        MessageBuilder msg = new MessageBuilder(recipient, parsedMessage);
                        msg.sendMessage();
                        System.out.println(recipient.getFullName());
                        recipientIDs.add(recipient.getUserID());
                    }
                    System.out.println(parsedMessage);
                    subs.logMessage(parsedMessage, recipients.size(), getCurrentUserID());
                    for(int recipID : recipientIDs) {
                        subs.logRecipients(recipID);
                    }
                    // Added here in case the next lines are commented out.
//                    for(Recipient recipient: subscribers) {
//                        if(selectedGroups.contains(0)) {
//                            msgRecipientCount++;
//                        } else if(groups.get(groupID).contains(recipient.getUserID())) {
////                            The following lines are disabled to prevent spam.
////                            SMSBuilder smsMsg = new SMSBuilder(parser.returnParsedMessage()); //SMS Message Builder
////                            MessageBuilder msg = new MessageBuilder(recipient, parser.returnParsedMessage());
////                            smsMsg.sendSMS();
////                            msg.sendMessage();
//                            msgRecipientCount++;
//                        }
//                    }
                }
            }
        });
        /**
         * Change the selected Group of Recipients.
         */
        groupSelect.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                selectedGroups.clear();
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    int[] groups = groupSelect.getSelectedIndices();
                    for (int group : groups) {
                        selectedGroups.add(group);
                    }
                }
//                for(int group: selectedGroups) {
//                    System.out.println(group);
//                }
                ArrayList<Recipient> recipients = buildRecipientList();
                for(Recipient recipient: recipients) {
                    System.out.println(recipient.getUserName());
                }
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

    /**
     * Create a model for the ComboBox pulldown menu for the message subscribers and populate it with group
     * name and number of members of each group.
     */
    public void populateRecipientMenu() {
//        ListModel model = (ListModel) groupSelect.getModel();
        DefaultListModel model = new DefaultListModel();
        model.removeAllElements();
        model.addElement("All Recipients");
        for(Integer key : groups.keySet()) {
            model.addElement( key + ": " +  groups.get(key).size());
        }
        groupSelect.setModel(model);
    }

    /**
     * Return the Root Panel of the NotificationForm GUI.
     * @return
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }


    /**
     *  Return the String Value of the Message in the Notification Text Area
     * @return String of the Message Text
     */
    public String getMessageText() {
        return notificationTextArea.getText();
    }

    /**
     * Check to make sure that there is actually text in the Message box, do not sent until
     * something has been entered.
     * @return A Boolean flag declaring if the message is valid or not.
     */
    private Boolean checkMessageContent() {
        Boolean valid = false;
        if(getMessageText().equals("")) {
            JOptionPane.showMessageDialog(null, "Message Text may not be Blank");
        } else {
            valid = true;
        }
        return valid;
    }

    /**
     * Obtains the current user's ID number from the DB, and adds this value to the notification log.
     * @return Currently logged in userID.
     */
    private int getCurrentUserID() {
        int currUserID = 0;
        for(Recipient recipient : subscribers) {
            if(recipient.getUserName().equals(currentUser)) {
                currUserID =  recipient.getUserID();
            }
        }
        return currUserID;
    }

    private ArrayList buildRecipientList() {
        ArrayList<Recipient> recipients = new ArrayList<>();
            if(selectedGroups.contains(0)) {
                recipients = subscribers;
            } else {
                for (Recipient recipient : subscribers) {
                    for(Integer groupID : selectedGroups) {
                        if (groups.get(groupID).contains(recipient.getUserID()) && !recipients.contains(recipient)) {
                            recipients.add(recipient);
                        }
                    }
                }
            }
        return recipients;
    }
}
