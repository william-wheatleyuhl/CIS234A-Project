package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This class provides the structure for the GUI window that prompts the user to provide values for messages containing
 * tags. The interface has been consolidated so that only one tag prompt window is displayed, containing a field for
 * each tag in the message. This panel is then populated into a JOptionPane when called by the TagParser Class.
 * @author William Wheatley-Uhl
 * @version 2019.05.26
 */

public class TagPrompt {
    private JPanel panel = new JPanel();
    private JFileChooser chooser = new JFileChooser();
    private JButton imgButton = new JButton("Select File");
    private GridBagConstraints left = new GridBagConstraints();
    private GridBagConstraints right = new GridBagConstraints();
    private HashMap<String, JTextField> tagValues = new HashMap<String, JTextField>();
    private String imageFileSrc;

    public TagPrompt() {
        panel.setLayout(new GridBagLayout());
        left.anchor = GridBagConstraints.EAST;
        left.insets = new Insets(5,5,5,5);
        right.insets = new Insets(5,5,5,5);
        right.weightx = 2.0;
        right.gridwidth = GridBagConstraints.REMAINDER;

        imgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(panel);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    imageFileSrc = chooser.getSelectedFile().getAbsolutePath();
                    System.out.println("You Chose this file: " + imageFileSrc);
                }
            }
        });
    }

    /**
     * For each tag in the message, add a text field with label to the JPane so that they may be filled in
     * the tag prompt GUI.
     * @param tag String value of an individual tag to be filled.
     */
    public void populateTagFrame(String tag) {
        if(tag.contains("image") || tag.contains("<image>")){
            JLabel label = new JLabel("Choose Image File:");
//            tagValues.put(tag, imgButton);
            panel.add(label, left);
            panel.add(imgButton, right);
        } else {
            JLabel label = new JLabel("What should the value of " + tag + " be?");
            tagValues.put(tag, new JTextField(15));
            panel.add(label, left);
            panel.add(tagValues.get(tag), right);
        }
    }

    /**
     * Sends out the populated panel on request.
     * @return Jpanel panel, the "Root Panel" for the TagPrompt GUI
     */
    public JPanel getPanel(){
        return panel;
    }

    /**
     * Get the number of tags to be filled for validation in TagParser.
     * @return int Value of number of tags.
     */
    public int getTagValuesSize() {
        return tagValues.size();
    }

    /**
     * Check to see if tags has been filled
     * @param tag Use String tag value to check if that key has a matching value.
     * @return True or False, depending on if tag has been filled.
     */
    public Boolean checkTagFilled(String tag) {
        if(tagValues.get(tag).getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get text from textfields to fill tags with their value.
     * @param tag String value of tag to be filled
     * @return Value to fill tag in with.
     */
    public String getTagValue(String tag) {
        return tagValues.get(tag).getText();
    }
}
