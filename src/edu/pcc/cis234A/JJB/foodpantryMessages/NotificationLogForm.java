package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * @version 2019.05.07
 *
 * Modifications:
 * - Added new fields in addition to those already created by the GUI form.
 * - Updated constructor to initialize fields, set up table, and include event listeners.
 * - Added a method that initializes that table when the application is first started.
 * - Added a method to call the DB class to run the query based on the min and max dates selected.
 * - Added a method to load the table with the query results.
 * - Added a method to format how the timestamp is displayed in the table.
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
    private JTextArea fullMessageTextArea;
    private JScrollPane fullMessageScrollPane;
    private static Timestamp minDate;
    private static Timestamp maxDate;
    private static ArrayList<Notification> notifications;
    private static DateFormat minDateFormat;
    private static DateFormat maxDateFormat;
    private static DateFormat dateFormat;
    private DefaultTableModel model;
    private JDateChooser jMinDateChooser;
    private JDateChooser jMaxDateChooser;
    private JavaneseJumpingBeansDB jjb;
    private static Boolean initialLoadInd;

    /**
     * Creates the Food Pantry form with date pickers and notification log table.  Includes a method call to
     * initialize the table with the 200 most recent logs.  Adds listeners for the date pickers and table.
     */
    public NotificationLogForm() {
        notifications = new ArrayList<Notification>();
        minDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        maxDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        model = (DefaultTableModel) notificationDataTable.getModel();
        jMinDateChooser = new JDateChooser();
        jMaxDateChooser = new JDateChooser();
        minDatePanel.add(jMinDateChooser);
        maxDatePanel.add(jMaxDateChooser);
        fullMessageTextArea.setMargin(new Insets(2,5,2,5));
        jjb = new JavaneseJumpingBeansDB();
        initialLoadInd = false;

        model.setColumnIdentifiers(new Object[] { "#", "Timestamp", "Sent By", "Recipient Count", "Message Preview" });
        notificationDataTable.setRowHeight(20);
        notificationDataTable.getColumnModel().getColumn(0).setPreferredWidth(1);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        notificationDataTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(1).setPreferredWidth(105);
        notificationDataTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        notificationDataTable.getColumnModel().getColumn(3).setPreferredWidth(65);
        notificationDataTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(4).setMinWidth(210);
        notificationDataTable.setIntercellSpacing(new Dimension(10, 4));
        initializeTable();

        /**
         * Event listener for the min date picker.
         */
        jMinDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date dtMinDate = (Date) evt.getNewValue();
                    String minTmst = minDateFormat.format(dtMinDate);
                    minDate = Timestamp.valueOf(minTmst);
                    runQuery();
                }
            }
        });

        /**
         * Event listener for the max date picker.
         */
        jMaxDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date dtMaxDate = (Date) evt.getNewValue();
                    String maxTmst = maxDateFormat.format(dtMaxDate);
                    maxDate = Timestamp.valueOf(maxTmst);
                    runQuery();
                }
            }
        });

        /**
         * Event listener for the table rows which populates the full message text area with the complete message
         * which was previewed in the table.
         */
        notificationDataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            int i = notificationDataTable.getSelectedRow();
            TableModel model = notificationDataTable.getModel();
            fullMessageTextArea.setText(model.getValueAt(i, 4).toString());
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
     * Initializes the table with the top 200 most recent logs
     */
    private void initializeTable() {
        initialLoadInd = true;
        notifications = jjb.readNotifications(Timestamp.valueOf("2000-01-01 00:00:00"),
                Timestamp.valueOf("2200-12-31 23:59:59"), initialLoadInd);
        populateTable();
        /*int i = 1;
        for (Notification not: notifications) {
            String str = dateFormat.format(not.getDateTime());
            System.out.println("#: " + i +
                    " || Timestamp: " + str +
                    " || Sent By: " + not.getUsername() +
                    " || Recipient Count: " + not.getRecipientCount() +
                    " || Message: " + not.getMessage());
            i++;
        }*/
        initialLoadInd = false;
    }

    /**
     * Makes the call to the database class, which runs the query.  Calls the method that will populate the table.
     */
    private void runQuery() {
        notifications = jjb.readNotifications(minDate, maxDate, initialLoadInd);
        populateTable();
        /*int i = 1;
        for (Notification not: notifications) {
            String str = dateFormat.format(not.getDateTime());
            System.out.println("#: " + i +
                    " || Timestamp: " + str +
                    " || Sent By: " + not.getUsername() +
                    " || Recipient Count: " + not.getRecipientCount() +
                    " || Message: " + not.getMessage());
            i++;
        }*/
    }

    /**
     * Populates the table with the data returned from the database query
     */
    private void populateTable() {
        model.setRowCount(0);
        int i = 1;
        for (Notification not : notifications) {
            String t = formatTimestamp(not.getDateTime());
            String s = not.getUsername();
            int r = not.getRecipientCount();
            String m = not.getMessage();
            model.addRow(new Object[]{i, t, s, r, m});
            i++;
        }
        notificationDataTable.setModel(model);
    }

    /**
     * Formats how the timestamp will be rendered in the table
     * @param t The timestamp retrieved for each notification log entry
     * @return A formatted timestamp for the table
     */
    private static String formatTimestamp(Timestamp t) {
        Date d = new Date(t.getTime());
        return dateFormat.format(d);
    }
}
