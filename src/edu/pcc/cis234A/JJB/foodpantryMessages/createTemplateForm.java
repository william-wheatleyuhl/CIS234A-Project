package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Syn
 * @version 2019.04.23
 */
public class createTemplateForm {
    private JPanel rootPanel;
    private JRadioButton radioCreateNew;
    private JRadioButton radioEditExisting;
    private JLabel labelTemplateName;
    private JTextField fieldTemplateName;
    private JLabel labelTemplateText;
    private JTextArea areaTemplateText;
    private JLabel labelTip;
    private JButton buttonSave;
    private JList listTemplates;

    public createTemplateForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));
        buttonSave.addActionListener(new ActionListener() {
            /**
             * Action performed when "Save" button is clicked
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkTemplateName = false;
                boolean checkTemplateText = false;

                // Check that the Template Name Field has something in it
                if(fieldTemplateName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a Template Name");
                }
                else {
                    checkTemplateName = true;
                }

                // Check that the Template Text Area has something in it
                if(areaTemplateText.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter text for the template");
                }
                else {
                    checkTemplateText = true;
                }

                if (checkTemplateName && checkTemplateText) {
                    if(radioCreateNew.isSelected()) {
                        // Placeholder - In the future the save button will commit information to
                        // DB, inform user it has been saved, and refresh the page
                        JOptionPane.showMessageDialog(null, "New template saved");
                    }
                    else if(radioEditExisting.isSelected()) {
                        // Placeholder - In the future the save button will commit information to
                        // DB, inform user it has been saved, and refresh the page
                        JOptionPane.showMessageDialog(null, "Changes to <template name> saved");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Choose \"Create New\" or select an existing template to save");
                    }
                }
            }
        });
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}

