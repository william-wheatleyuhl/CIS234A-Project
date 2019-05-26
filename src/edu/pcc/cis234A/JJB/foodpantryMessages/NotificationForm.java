package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * NotificationForm Class
 * This class defines the functionality of the GUI for sending Foot Pantry based Messages.
 * The user is able to select what subscribers the message is sent to, type a message, select a message from a template,
 * and send the message. The templates are pulled from the TEMPLATE table in the Database, and all sent messages will
 * be stored in the MESSAGES table and given a unique messageID.
 * @authors Syn Calvo and Will Wheatley-Uhl
 * @version 2019.05.07
 */

public class NotificationForm {
    private JPanel rootPanel;
    private JTextArea notificationTextArea;
    private JLabel editTextLabel;
    private JButton sendNotificationButton;
    private JLabel sendToLabel;
    private JRadioButton allRadio;
    private JRadioButton specificRadio;
    private JComboBox groupSelect;
    private JComboBox chooseTemplate;
    private String currentUser;
    private int groupID;
    private SubscriberDB subs = new SubscriberDB();
    private ArrayList<Template> templates = subs.readTemplates();
    private ArrayList<Recipient> recipients = subs.readSubscriberData();


    public NotificationForm(String currUser) {
        this.currentUser = currUser;
        populateTemplateMenu();
        poulateRecipientMenu();
        getCurrentUserID();

        /**
        * Action Listener for the "All Recipient" radio button. Selecting this populates the recipient text field
        * with the usernames from the Database. Deselects the "Specific Recipients" radio, and disables the Recipients
        * field from editing.
        */
        allRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(allRadio.isSelected()) {
                    groupID = 0;
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
                    groupID = groupSelect.getSelectedIndex();
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
                int msgRecipientCount = 0;
                if(checkMessageContent()) {
                    String parsedMessage = parser.returnParsedMessage(); // Added here in case the next lines are commented out.
                    for(Recipient recipient: recipients) {
                        if(groupID == 0) {
                            msgRecipientCount++;
                        } else if(recipient.getSubscriberRole() == groupID) {
//                            The following lines are disabled to prevent spam.
                            MessageBuilder msg = new MessageBuilder(recipient, parser.returnParsedMessage());
//                            msg.sendMessage();
                            recipientIDs.add(recipient.getUserID());
                            msgRecipientCount++;
                        }
                    }
                    System.out.println(parsedMessage);
//                    subs.logMessage(getMessageText(), msgRecipientCount, getCurrentUserID());
//                    for(int recipID : recipientIDs) {
//                        subs.logRecipients(recipID);
//                    }
                }
            }
        });
        /**
         * Change the selected Group of Recipients.
         */
        groupSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setGroupID(groupSelect.getSelectedIndex());
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
     * Create a model for the ComboBox pulldown menu for the message recipients and populate it with group
     * name and number of members of each group.
     */
    public void poulateRecipientMenu() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) groupSelect.getModel();
        model.removeAllElements();
        int[] groupCounts = getGroupCounts();
        model.addElement("Select Recipients...");
        model.addElement("Managers: " + groupCounts[0]);
        model.addElement("Staff: " + groupCounts[1]);
        model.addElement("Subscribers: " + groupCounts[2]);
        groupSelect.setModel(model);
    }

    /**
     *
     * @return
     */
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
     * Sets the
     * @param groupID
     */
    private void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    private int getCurrentUserID() {
        int currUserID = 0;
        for(Recipient recipient : recipients) {
            if(recipient.getUserName().equals(currentUser)) {
                currUserID =  recipient.getUserID();
            }
        }
        return currUserID;
    }
}
