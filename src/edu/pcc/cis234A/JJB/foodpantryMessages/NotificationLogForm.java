package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
    private static ArrayList<Notification> notifications;
    private static DateFormat minDateFormat;
    private static DateFormat maxDateFormat;
    private static DateFormat dateFormat;
    DefaultTableModel model;
    private static String[] columnNames = {"#", "Timestamp", "Sent By", "Recipient Count", "Message"};
    //private static DefaultTableModel model;
    /*private static DefaultTableModel model = new DefaultTableModel(new String[]{"#", "Timestamp", "Sent By",
            "Recipient Count", "Message"}, 0);*/

    JDateChooser jMinDateChooser = new JDateChooser();
    JDateChooser jMaxDateChooser = new JDateChooser();
    //DefaultTableModel dtm = new DefaultTableModel();

    /**
     * Creates the Food Pantry form, adds the min and max date pickers, and creates listeners for the date pickers.
     */
    public NotificationLogForm() {
        //rootPanel.setPreferredSize(new Dimension(800, 600));
        //rootPanel.add(notificationDataTable);

        notifications = new ArrayList<Notification>();
        minDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        maxDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        model = (DefaultTableModel) notificationDataTable.getModel();
        minDatePanel.add(jMinDateChooser);
        maxDatePanel.add(jMaxDateChooser);

        /*for(int i = 0; i < notificationDataTable.getColumnCount(); i++)
        {
            TableColumn column = notificationDataTable.getTableHeader().getColumnModel().getColumn(i);

            column.setHeaderValue(columnNames[i]);
        }*/
        model.setColumnIdentifiers(new Object[] {
                "#", "Timestamp", "Sent By", "Recipient Count", "Message Preview" });

        notificationDataTable.setRowHeight(20);
        notificationDataTable.getColumnModel().getColumn(0).setPreferredWidth(8);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        /*headerRenderer.setBackground(new Color(66, 209, 244));
        headerRenderer.setHorizontalAlignment( JLabel.CENTER);
        for (int i = 0; i < notificationDataTable.getModel().getColumnCount(); i++) {
            notificationDataTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }*/
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        notificationDataTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(1).setPreferredWidth(105);
        notificationDataTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        notificationDataTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        notificationDataTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(4).setMinWidth(248);

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

    /**
     * Returns the root panel associated with the form.
     * @return the root panel
     */
    /*public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }*/

    public void runQuery() {
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        //jjb.setMinDate(Timestamp.valueOf("2000-01-01 00:00:00.0"));
        notifications = jjb.readNotifications(minDate, maxDate);
        populateTable();
        //displayData(notifications);
        int i = 1;

        for (Notification not: notifications) {
            /*Timestamp t = not.getDateTime();
            String s = not.getUsername();
            int r = not.getRecipientCount();
            String m = not.getMessage();

            model.addRow(new Object[]{i, t, s, r, m});*/
            String str = dateFormat.format(not.getDateTime());
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

    public void populateTable() {
        model.setRowCount(0);
        int i = 1;
        System.out.println(notifications.size());
        for(Notification not : notifications) {
            String t = " " + formatTimestamp(not.getDateTime());
            String s = " " + not.getUsername();
            int r = not.getRecipientCount();
            String m = " " + not.getMessage();
            //String m = " " + formatMessagePreview(not.getMessage());

            model.addRow(new Object[]{i, t, s, r, m});
            i++;
        }

        notificationDataTable.setModel(model);
        //tableScrollPane.add(notificationDataTable);
    }

    public static String formatTimestamp(Timestamp t) {
        Date d = new Date(t.getTime());
        return dateFormat.format(d);
    }

    public static String formatMessagePreview(String message) {
        String s = message.substring(0, Math.min(message.length(), 45));
        if(message.length() > 45) {
            s = message.substring(0, Math.min(message.length(), 45)) + "...";
        }
        return s;
    }
}
