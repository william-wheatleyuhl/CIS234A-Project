package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    JDateChooser jMinDateChooser = new JDateChooser();
    JDateChooser jMaxDateChooser = new JDateChooser();
    JavaneseJumpingBeansDB jjbdb = new JavaneseJumpingBeansDB();
    Main mn = new Main();

    /**
     * Creates the Food Pantry form, adds the min and max date pickers, and creates listeners for the date pickers.
     */
    public NotificationLogForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));

        minDatePanel.add(jMinDateChooser);
        maxDatePanel.add(jMaxDateChooser);

        jMinDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("date".equals(evt.getPropertyName())) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    Date dtMinDate = (Date) evt.getNewValue();
                    String minTmst = dateFormat.format(dtMinDate);
                    //System.out.println("String date: " + tmst);
                    Timestamp tmstMinDate = Timestamp.valueOf(minTmst);
                    mn.setMinDate(tmstMinDate);
                    //System.out.println("Min " + evt.getPropertyName() + ": " + tmstMinDate);
                    mn.runQuery();
                }
            }
        });

        jMaxDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("date".equals(evt.getPropertyName())) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                    Date dtMaxDate = (Date) evt.getNewValue();
                    String maxTmst = dateFormat.format(dtMaxDate);
                    //System.out.println("String date: " + tmst);
                    Timestamp tmstMaxDate = Timestamp.valueOf(maxTmst);
                    mn.setMaxDate(tmstMaxDate);
                    //System.out.println("Max " + evt.getPropertyName() + ": " + tmstMaxDate);
                    mn.runQuery();
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
}
