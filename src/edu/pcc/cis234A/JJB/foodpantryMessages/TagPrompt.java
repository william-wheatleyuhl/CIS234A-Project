package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TagPrompt {
    private JPanel panel = new JPanel();
    private GridBagConstraints left = new GridBagConstraints();
    private GridBagConstraints right = new GridBagConstraints();
    private HashMap<String, JTextField> tagValues = new HashMap<String, JTextField>();

    public TagPrompt() {
        panel.setLayout(new GridBagLayout());
        left.anchor = GridBagConstraints.EAST;
        left.insets = new Insets(5,5,5,5);
        right.insets = new Insets(5,5,5,5);
        right.weightx = 2.0;
        right.gridwidth = GridBagConstraints.REMAINDER;
    }

    public void populateTagFrame(String tag) {
        JLabel label = new JLabel("What should the value of " + tag + " be?");
        tagValues.put(tag, new JTextField(15));
        panel.add(label, left);
        panel.add(tagValues.get(tag), right);
    }

    public JPanel getPanel(){
        return panel;
    }

    public int getTagValuesSize() {
        return tagValues.size();
    }

    public Boolean checkTagFilled(String tag) {
        if(tagValues.get(tag).getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String getTagValue(String tag) {
        return tagValues.get(tag).getText();
    }
}
