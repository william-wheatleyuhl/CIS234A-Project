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
import java.sql.Time;
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
     * Creates the Food Pantry form, adds the min and max date pickers, and creates listeners for the date pickers.
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
        //fullMessageTextArea.setLineWrap(true);
        //fullMessageTextArea.setWrapStyleWord(true);

        model.setColumnIdentifiers(new Object[] { "#", "Timestamp", "Sent By", "Recipient Count", "Message Preview" });
        notificationDataTable.setRowHeight(20);
        notificationDataTable.getColumnModel().getColumn(0).setPreferredWidth(1);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        notificationDataTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(1).setPreferredWidth(105);
        notificationDataTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        notificationDataTable.getColumnModel().getColumn(3).setPreferredWidth(55);
        notificationDataTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        notificationDataTable.getColumnModel().getColumn(4).setMinWidth(230);
        notificationDataTable.setIntercellSpacing(new Dimension(10, 4));
        initializeTable();

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

    public void initializeTable() {
        initialLoadInd = true;
        notifications = jjb.readNotifications(Timestamp.valueOf("2000-01-01 00:00:00"),
                Timestamp.valueOf("2200-12-31 23:59:59"), initialLoadInd);
        populateTable();
        int i = 1;

        for (Notification not: notifications) {
            String str = dateFormat.format(not.getDateTime());
            System.out.println("#: " + i +
                    " || Timestamp: " + str +
                    " || Sent By: " + not.getUsername() +
                    " || Recipient Count: " + not.getRecipientCount() +
                    " || Message: " + not.getMessage());
            i++;
        }
        /*if(initialLoadInd) {
            Timestamp maxTmst = jjb.getMaxTmst();
            Timestamp minTmst = jjb.getMinTmst();
            //Date maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(maxTmst);
            Date maxDt = new Date(maxTmst.getTime());
            Date minDt = new Date(minTmst.getTime());
            jMaxDateChooser.setDate(maxDt);
            jMinDateChooser.setDate(minDt);
            System.out.println("Set date picker min and dates.");
        }*/
        initialLoadInd = false;
        //runQuery();
    }

    public void runQuery() {
        notifications = jjb.readNotifications(minDate, maxDate, initialLoadInd);
        populateTable();
        int i = 1;

        for (Notification not: notifications) {
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
        for (Notification not : notifications) {
            String t = formatTimestamp(not.getDateTime());
            String s = not.getUsername();
            int r = not.getRecipientCount();
            String m = not.getMessage();
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
}
