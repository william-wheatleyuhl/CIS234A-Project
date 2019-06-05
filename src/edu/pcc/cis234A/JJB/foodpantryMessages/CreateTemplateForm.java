package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Syn Calvo
 * @version 2019.06.04
 *
 * 20190521 SC - Fixed layout of combobox
 * 20190521 SC - Reconstructed combobox listener to populate template fields dynamically
 * 20190604 SC - Added loggedInUserID to template submissions
 * 20190604 SC - Added labelLastEdit to combobox listener
 */
public class CreateTemplateForm {
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
    private JLabel labelLastEdit;
    private JLabel labelCharCount;
    TemplateDB temps = new TemplateDB();
    ArrayList<Template> templates = temps.readTemplates();
    UserLoginDB session = new UserLoginDB();
    UserLogin login = new UserLogin();
    private String newTemplateName;
    private String newTemplateText;
    private int existingTemplateID;
    private int loggedInUserID = session.getUsernameUserID(login.getLoggedInUser());

    public CreateTemplateForm() {
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
                    labelLastEdit.setText("");
                }
            }
        });

        /**
         * Action listener for the "Edit Existing" radio button.
         * Deselects the "Create New" radio button and enables
         * template text and name fields
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
         * Also sets the label at the bottom of the frame for the user who last edited that template
         */
        comboTemplates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboTemplates.getSelectedIndex() -1;
                areaTemplateText.setText(templates.get(selectedIndex).messageText);
                fieldTemplateName.setText(templates.get(selectedIndex).templateName);
                labelLastEdit.setText("Last edit by " + temps.getLastEditUser(templates.get(selectedIndex).getUserID()));
            }
        });

        /**
         * Action listener for the "Save" button.
         * Checks that form fields are completed and calls methods to submit changes to DB.
         * TODO: Refresh ComboBox dropdown with new changes
         */
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkTemplateName = false;
                boolean checkTemplateText = false;
                boolean checkTemplateChars = false;

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

                // Check that the Template Text Area is under 500 characters
                if(areaTemplateText.getText().length() > 500) {
                    JOptionPane.showMessageDialog(null, "Please limit messages to 500 characters or less");
                }
                else {
                    checkTemplateChars = true;
                }

                // Make sure the template has both fields filled in
                if (checkTemplateName && checkTemplateText && checkTemplateChars) {
                    if(radioCreateNew.isSelected()) {
                        JOptionPane.showMessageDialog(null, "New template saved saved as \"" + newTemplateName + "\"");
                        temps.logNewTemplate(newTemplateName, newTemplateText, loggedInUserID);
                    }
                    else if(radioEditExisting.isSelected()) {
                        existingTemplateID = comboTemplates.getSelectedIndex();
                        JOptionPane.showMessageDialog(null, "Changes to \"" + newTemplateName + "\" saved");
                        temps.updateExistingTemplate(newTemplateName, newTemplateText, loggedInUserID, existingTemplateID);
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
