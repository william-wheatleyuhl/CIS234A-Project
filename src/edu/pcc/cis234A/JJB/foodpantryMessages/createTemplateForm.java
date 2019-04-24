package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;

/**
 * @author Syn
 * @version 2019.04.23
 */
public class createTemplateForm {
    private JPanel rootPanel;
    private JLabel labelPageName;
    private JLabel labelLoggedInAs;
    private JRadioButton radioCreateNew;
    private JRadioButton RadioEditExisting;
    private JLabel labelTemplateName;
    private JTextField fieldTemplateName;
    private JLabel labelTemplateText;
    private JTextArea areaTemplateText;
    private JLabel labelTip;
    private JButton buttonSave;
    private JList listTemplates;

    public createTemplateForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}

