package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Color;

/**
 * A class to define the functionality of the Template Editor GUI
 * Allows a user to create a new template or select a saved template from the DB
 * and make changes to it.
 *
 * @author Syn Calvo
 * @version 2019.06.04
 *
 * 20190521 SC - Fixed layout of combobox
 * 20190521 SC - Reconstructed combobox listener to populate template fields dynamically
 * 20190604 SC - Added loggedInUserID to template submissions
 * 20190604 SC - Added labelLastEdit to combobox listener
 * 20190608 SC - Added refreshTemplates() method
 * 20190608 SC - Restructured action listener to item listener for combo box w/ if statements
 * 20190608 SC - Moved comboBox model outside of the populateComboBox() method
 * 20190608 SC - Uncommented resetAllTheThings() method and added it to Save button listener
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
    DefaultComboBoxModel model = (DefaultComboBoxModel) comboTemplates.getModel();

    public CreateTemplateForm() {
        populateComboBox();
        // Set fields unusable until radio choice is selected
        areaTemplateText.setEnabled(false);
        fieldTemplateName.setEnabled(false);
        comboTemplates.setEnabled(false);

        rootPanel.setPreferredSize(new Dimension(800, 600));

        /**
         * Document Listener that counts characters in the "document" created
         * in the text area and displays as green if within 500 or red if over
         * Note: Char limit in DB is 500 for MessageText column
         */
        areaTemplateText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(areaTemplateText.getText().length() > 500) {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(Color.red);
                }
                else {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(new Color(0, 133, 66));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(areaTemplateText.getText().length() > 500) {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(Color.red);
                }
                else {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(new Color(0, 133, 66));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(areaTemplateText.getText().length() > 500) {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(Color.red);
                    //labelCharCount.setText(String.valueOf(areaTemplateText.getText().length()));
                }
                else {
                    labelCharCount.setText("Character count: " + areaTemplateText.getText().length());
                    labelCharCount.setForeground(new Color(0, 133, 66));
                }
            }
        });

        /**
         * Action listener for the "Create New" radio button.
         * Deselects the "Edit Existing" radio button.
         * Disables the existing templates comboBox.
         * Clears any text in text area and text fields.
         * Enables text area and text fields for editing.
         * Clears "Last edit by" label.
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
         * Deselects the "Create New" radio button.
         * Enables template text and name fields.
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
        comboTemplates.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboTemplates.getModel().getSize() > 1) {
                    int selectedIndex = comboTemplates.getSelectedIndex();
                    if (selectedIndex == 0) {
                        areaTemplateText.setText("");
                        fieldTemplateName.setText("");
                        labelLastEdit.setText("");
                    } else {
                        areaTemplateText.setText(templates.get(selectedIndex - 1).messageText);
                        fieldTemplateName.setText(templates.get(selectedIndex - 1).templateName);
                        labelLastEdit.setText("Last edit by " + temps.getLastEditUser(templates.get(selectedIndex).getUserID()));
                    }
                }
            }
        });

        /**
         * Action listener for the "Save" button.
         * Checks that form fields are completed and calls methods to submit changes to DB.
         * Calls refresh and populate methods to refresh ComboBox with updated templates from DB.
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

                // Make sure the template has both fields filled in and is under 500 characters
                if (checkTemplateName && checkTemplateText && checkTemplateChars) {
                    if(radioCreateNew.isSelected()) {
                        JOptionPane.showMessageDialog(null, "New template saved saved as \"" + newTemplateName + "\"");
                        temps.logNewTemplate(newTemplateName, newTemplateText, loggedInUserID);
                        // Reset the frame
                        resetAllTheThings();
                    }
                    else if(radioEditExisting.isSelected()) {
                        existingTemplateID = comboTemplates.getSelectedIndex();
                        JOptionPane.showMessageDialog(null, "Changes to \"" + newTemplateName + "\" saved");
                        temps.updateExistingTemplate(newTemplateName, newTemplateText, loggedInUserID, existingTemplateID);
                        // Reset the frame
                        resetAllTheThings();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Choose \"Create New\" or select an existing template to save");
                    }
                }
            }
        });
    }
    /**
     * Reset the Template Editor
     * For use in the Save button action listener
     * Clears text area, text field, last edit by, and character count
     * Calls methods to refresh the template array and repopulate the combo box
     */
    private void resetAllTheThings() {
        areaTemplateText.setText("");
        fieldTemplateName.setText("");
        labelLastEdit.setText("");
        labelCharCount.setText("");
        //radioCreateNew.setSelected(false);
        //radioEditExisting.setSelected(false);
        //comboTemplates.setEnabled(false);
        refreshTemplates();
        populateComboBox();
    }

    /**
     * Model for the drop down menu to select existing templates found in DB.
     */
    private void populateComboBox() {
        model.removeAllElements();
        model.addElement("Choose one...");
        for(Template temp : templates) {
            model.addElement(temp.templateName);
        }
        comboTemplates.setModel(model);
    }

    /**
     * Clear the ArrayList of templates and reload from the DB
     */
    private void refreshTemplates() {
        templates.clear();
        templates = temps.readTemplates();
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
