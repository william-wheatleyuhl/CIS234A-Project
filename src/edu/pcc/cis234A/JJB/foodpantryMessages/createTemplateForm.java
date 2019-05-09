package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Syn
 * @version 2019.04.23
 */
public class createTemplateForm {
    private JPanel rootPanel;
    private JLabel labelPageName;
    private JLabel labelLoggedInAs;
    private JRadioButton radioCreateNew;
    private JRadioButton radioEditExisting;
    private JLabel labelTemplateName;
    private JTextField fieldTemplateName;
    private JLabel labelTemplateText;
    private JTextArea areaTemplateText;
    private JLabel labelTip;
    private JButton buttonSave;
    private JComboBox comboTemplates;
    templateDB temps = new templateDB();
    ArrayList<Template> templates = temps.readTemplates();
    private String newTemplateName;
    private String newTemplateText;
    private int existingTemplateID;

    public createTemplateForm() {
        populateComboBox();
        areaTemplateText.setEnabled(false);
        fieldTemplateName.setEnabled(false);
        comboTemplates.setEnabled(false);

        rootPanel.setPreferredSize(new Dimension(800, 600));

        /**
         * Action listener for the "Create New" radio button.
         * Deselects the "Edit Existing" radio button.
         * Disables the existing templates list field.
         */
        radioCreateNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radioCreateNew.isSelected()) {
                    radioEditExisting.setSelected(false);
                    comboTemplates.setEnabled(false);
                    areaTemplateText.setText("");
                    fieldTemplateName.setText("");
                    areaTemplateText.setEnabled(true);
                    fieldTemplateName.setEnabled(true);
                }
            }
        });

        /**
         * Action listener for the "Edit Existing" radio button.
         * Deselects the "Create New" radio button.
         */
        radioEditExisting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radioEditExisting.isSelected()) {
                    radioCreateNew.setSelected(false);
                    comboTemplates.setEnabled(true);
                    areaTemplateText.setEnabled(true);
                    fieldTemplateName.setEnabled(true);
                }
            }
        });

        /**
         * Action listener for the Edit Existing template comboBox.
         * Selecting a template from the list clears the Template Name and Template Text fields
         * and replaces them with name and text from DB for selected template
         */
        comboTemplates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(comboTemplates.getSelectedIndex()) {
                    case 0: areaTemplateText.setText("");
                        break;
                    case 1: areaTemplateText.setText(templates.get(0).messageText);
                        break;
                    case 2: areaTemplateText.setText(templates.get(1).messageText);
                        break;
                    case 3: areaTemplateText.setText(templates.get(2).messageText);
                        break;
                    case 4: areaTemplateText.setText(templates.get(3).messageText);
                        break;
                }
                switch(comboTemplates.getSelectedIndex()) {
                    case 0: fieldTemplateName.setText("");
                        break;
                    case 1: fieldTemplateName.setText(templates.get(0).templateName);
                        break;
                    case 2: fieldTemplateName.setText(templates.get(1).templateName);
                        break;
                    case 3: fieldTemplateName.setText(templates.get(2).templateName);
                        break;
                    case 4: fieldTemplateName.setText(templates.get(3).templateName);
                        break;
                }
            }
        });

        /**
         * Action listener for the "Save" button.
         * Checks that form fields are completed.
         */
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkTemplateName = false;
                boolean checkTemplateText = false;

                // Check that the Template Name Field has something in it
                if(fieldTemplateName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a Template Name");
                }
                else {
                    newTemplateName = fieldTemplateName.getText();
                    checkTemplateName = true;
                }

                // Check that the Template Text Area has something in it
                if(areaTemplateText.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter text for the template");
                }
                else {
                    newTemplateText = areaTemplateText.getText();
                    checkTemplateText = true;
                }

                if (checkTemplateName && checkTemplateText) {
                    if(radioCreateNew.isSelected()) {
                        // Placeholder - In the future the save button will commit information to
                        // DB, inform user it has been saved, and refresh the page
                        JOptionPane.showMessageDialog(null, "New template saved saved as \"" + newTemplateName + "\"");
                        System.out.println("Template name: " + newTemplateName); //testing
                        System.out.println("Template text: " + newTemplateText); //testing
                        temps.logNewTemplate(newTemplateName, newTemplateText);
                    }
                    else if(radioEditExisting.isSelected()) {
                        existingTemplateID = comboTemplates.getSelectedIndex();
                        // Placeholder - In the future the save button will commit information to
                        // DB, inform user it has been saved, and refresh the page
                        JOptionPane.showMessageDialog(null, "Changes to \"" + newTemplateName + "\" saved");
                        System.out.println("Template name: " + newTemplateName); //testing
                        System.out.println("Template text: " + newTemplateText); //testing
                        System.out.println("TemplateID: " + existingTemplateID); //testing
                        temps.updateExistingTemplate(newTemplateName, newTemplateText, existingTemplateID);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Choose \"Create New\" or select an existing template to save");
                    }
                }
            }
        });
    }

    /**
     * Model for the drop down menu to select existing templates found in DB.
     */
    public void populateComboBox() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) comboTemplates.getModel();
        model.removeAllElements();
        model.addElement("Choose one...");
        for(Template temp : templates) {
            model.addElement(temp.templateName);
        }
        comboTemplates.setModel(model);
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
