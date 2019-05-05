package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Notification Log form class
 *
 * @author Liana Schweitzer
 * @version 2019.04.21
 */
public class NotificationLogForm {
    private JPanel rootPanel;
    private JLabel pageNameLabel;
    private JLabel userLoggedInLabel;
    private JLabel messageNumberLabel;
    private JLabel timestampLabel;
    private JLabel sentByLabel;
    private JLabel recipientCountLabel;
    private JLabel messagePreviewLabel;
    private JTable notificationDataTable;
    private JScrollPane tableScrollPane;
    private JLabel minDateLabel;
    private JPanel minDatePanel;
    private JLabel maxDateLabel;
    private JPanel maxDatePanel;
    private JLabel fullMessageLabel;
    private JScrollPane fullMessageScrollPane;
    private static Timestamp minDate;
    private static Timestamp maxDate;
    private static ArrayList<Notification> notifications = new ArrayList<Notification>();
    private static DateFormat minDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static DateFormat maxDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    /*private static DefaultTableModel model = new DefaultTableModel(new String[]{"#", "Timestamp", "Sent By",
            "Recipient Count", "Message"}, 0);*/
    private static String[] columnNames = {"#", "Timestamp", "Recipient Count", "Message"};

    JDateChooser jMinDateChooser = new JDateChooser();
    JDateChooser jMaxDateChooser = new JDateChooser();
    DefaultTableModel dtm = new DefaultTableModel();

    /**
     * Creates the Food Pantry form, adds the min and max date pickers, and creates listeners for the date pickers.
     */
    public NotificationLogForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));
        //rootPanel.add(notificationDataTable);
        minDatePanel.add(jMinDateChooser);
        maxDatePanel.add(jMaxDateChooser);


        jMinDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("date".equals(evt.getPropertyName())) {
                    Date dtMinDate = (Date) evt.getNewValue();
                    String minTmst = minDateFormat.format(dtMinDate);
                    minDate = Timestamp.valueOf(minTmst);
                    runQuery();
                    //notificationDataTable.setModel(model);
                }
            }
        });

        jMaxDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("date".equals(evt.getPropertyName())) {
                    Date dtMaxDate = (Date) evt.getNewValue();
                    String maxTmst = maxDateFormat.format(dtMaxDate);
                    maxDate = Timestamp.valueOf(maxTmst);
                    runQuery();
                }
            }
        });
    }

    /**
     * Returns the root panel associated with the form.
     * @return the root panel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public static void runQuery() {
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        //jjb.setMinDate(Timestamp.valueOf("2000-01-01 00:00:00.0"));
        notifications = jjb.readNotifications(minDate, maxDate);
        //displayData(notifications);
        int i = 1;

        for (Notification not: notifications) {
            /*Timestamp t = not.getDateTime();
            String s = not.getUsername();
            int r = not.getRecipientCount();
            String m = not.getMessage();

            model.addRow(new Object[]{i, t, s, r, m});*/
            String str = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(not.getDateTime());
            System.out.println("#: " + i +
                    " || Timestamp: " + str +
                    " || Sent By: " + not.getUsername() +
                    " || Recipient Count: " + not.getRecipientCount() +
                    " || Message: " + not.getMessage());
            i++;
        }
    }

    /**
     * Sets the min date, which will be used as the first parameter for NOTIFICATION_SQL.
     */
    public void setMinDate(Timestamp minDate) {
        this.minDate = minDate;
        System.out.println("Main Min Date: " + this.minDate);
    }

    /**
     * Sets the max date, which will be used as the second parameter for NOTIFICATION_SQL.
     */
    public void setMaxDate(Timestamp maxDate) {
        this.maxDate = maxDate;
        System.out.println("Main Max Date: " + this.maxDate);
    }


    /*public static void displayData(ArrayList<Notification> notifications) {
        TableModel model = notificationLogForm.getTableModel();
        Object[] row = new Object[5];
        for (int i = 0; i < notifications.size(); i++) {
            row[0] = notifications.get(i);
            row[1] = notifications.get(i).getDateTime();
            row[2] = notifications.get(i).getUsername();
            row[3] = notifications.get(i).getRecipientCount();
            row[4] = notifications.get(i).getMessage();
        }
    }*/


    public void setTableModel(DefaultTableModel model) {
        System.out.println("Form row count: " + model.getRowCount());
        notificationDataTable.setModel(model);
        //rootPanel.add(notificationDataTable);
    }

    /*public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }*/

    public JScrollPane addJTable(JTable table) {
        tableScrollPane.add(table);
        return tableScrollPane;
    }
}
