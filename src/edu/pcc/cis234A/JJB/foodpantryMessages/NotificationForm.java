package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * NotificationForm Class
 * This class defines the functionality of the GUI for sending Foot Pantry based Messages.
 * The user is able to select what subscribers the message is sent to, type a message, select a message from a template,
 * and send the message. The templates are pulled from the TEMPLATE table in the Database, and all sent messages will
 * be stored in the MESSAGES table and given a unique messageID.
 * @authors Syn Calvo and Will Wheatley-Uhl
 * @version 2019.06.03
 */

public class NotificationForm {
    private JPanel rootPanel;
    private JTextArea notificationTextArea;
    private JLabel editTextLabel;
    private JButton sendNotificationButton;
    private JLabel sendToLabel;
    private JList groupSelect;
    private JComboBox chooseTemplate;
    private JScrollPane scrollList;
    private String currentUser;
    private ArrayList<Integer> selectedGroups = new ArrayList<>();
    private SubscriberDB subs = new SubscriberDB();
    private ArrayList<Template> templates = subs.readTemplates();
    private ArrayList<Recipient> subscribers = subs.readSubscriberData();
    private HashMap<Integer, ArrayList<Object>> groups = subs.getGroupMakeup();
    private DefaultComboBoxModel model = (DefaultComboBoxModel) chooseTemplate.getModel();



    public NotificationForm(String currUser) {
        this.currentUser = currUser;
        populateTemplateMenu();
        populateRecipientMenu();
        getCurrentUserID();

        rootPanel.setPreferredSize(new Dimension(800, 600));
        for(Integer key : groups.keySet()) {
            System.out.println(groups.get(key).toString());
        }


        /**
          * Action Listener for the chooseTemplate comboBox. Selecting the first option in the pull-down
          * clears the message field.
          * TODO: Cache Message when switching between templates and new messages.
          */
        chooseTemplate.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (chooseTemplate.getModel().getSize() > 1) {
                    int selectedIndex = chooseTemplate.getSelectedIndex();
                    if (selectedIndex == 0) {
                        notificationTextArea.setText("");
                        notificationTextArea.setEnabled(true);
                    } else {
                        notificationTextArea.setText(templates.get(selectedIndex-1).messageText);
                        notificationTextArea.setEnabled(false);
                    }
                }
            }
        });

        //OLD CODE
//        chooseTemplate.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                int selectedIndex = chooseTemplate.getSelectedIndex() - 1;
//                if(chooseTemplate.getSelectedIndex() == 0) {
//                    notificationTextArea.setText("");
//                    notificationTextArea.setEnabled(true);
//                } else {
//                notificationTextArea.setText(templates.get(selectedIndex).messageText);
//                notificationTextArea.setEnabled(false);
//                }
//            }
//        });

//        /**
//         * On Mouse-Over, refresh the list of available templates.
//         */
//        chooseTemplate.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent mouseEvent) {
//                super.mouseEntered(mouseEvent);
//                refreshTemplates();
//                populateTemplateMenu();
//            }
//        });

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
                    if(getMessageText().contains("image")) {
                        for(Recipient recipient : recipients) {
                            MessageBuilder msg = new MessageBuilder(recipient, parsedMessage);
                            msg.sendMessageWithImage(parser.getImageSrcPath());
                        }
                    }
                    for(Recipient recipient : recipients) {
//                        MessageBuilder msg = new MessageBuilder(recipient, parsedMessage);
//                        msg.sendMessage();
//                        SMSBuilder smsMsg = new SMSBuilder(parser.returnParsedMessage()); //SMS Message Builder
//                        smsMsg.sendSMS();
                        System.out.println(recipient.getFullName());
                        recipientIDs.add(recipient.getUserID());
                    }
                    System.out.println(parsedMessage);
//                    subs.logMessage(parsedMessage, recipients.size(), getCurrentUserID());
//                    for(int recipID : recipientIDs) {
//                        subs.logRecipients(recipID);
//                    }
                    JOptionPane.showMessageDialog(null, "Message Sent!");
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
                //Output for debugging
//                ArrayList<Recipient> recipients = buildRecipientList();
//                for(Recipient recipient: recipients) {
//                    System.out.println("NAME: " + recipient.getUserName() + " " + recipient.getRoleID() + " " + recipient.getRoleTitle());
//                }
            }

        });
    }

    /**
     * Create a Model for the ComboBox pulldown menu for message templates and populate it from saved Templates
     * found in the database. Currently set with one default option and 3 template options.
     */
    public void populateTemplateMenu() {
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
    private void populateRecipientMenu() {
        this.groupSelect = new JList();
        DefaultListModel model = new DefaultListModel();
        model.removeAllElements();
        model.addElement("All Recipients");
        for(Integer key : groups.keySet()) {
            model.addElement( groups.get(key).get(0) + ":\t\t " +  (groups.get(key).size() - 2));
        }
        groupSelect.setModel(model);
        scrollList.setViewportView(groupSelect);
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
    private String getMessageText() {
        return notificationTextArea.getText();
    }

    /**
     * Check to make sure that there is actually text in the Message box, do not sent until
     * something has been entered.
     * @return A Boolean flag declaring if the message is valid or not.
     */
    private boolean checkMessageContent() {
        boolean valid = false;
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

    /**
     * Determine which subscribers will be receiving notifications based on the selected groups.
     * @return An ArrayList of Recipient objects of the subscribers to receive the notification.
     */
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

    /**
     * Clear the current ArrayList of Templates. Reload available templates from DB.
     */
    public void refreshTemplates() {
        templates.clear();
        templates = subs.readTemplates();
    }
}

