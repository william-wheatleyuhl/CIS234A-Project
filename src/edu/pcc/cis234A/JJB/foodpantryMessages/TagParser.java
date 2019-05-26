package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.*;

/**
 * With the Given Structure of message tags, parse the text of a given message string to find the tags, and identify
 * their values.
 * @author William Wheatley-Uhl
 * @version 2019.05.06
 */
class TagParser {
    private String stringToSearch;
    private StringTokenizer defaultTokenizer;
    private String pattern = "<(.*?)>";
    private Pattern p = Pattern.compile(pattern);

    private JFrame tagFrame = new JFrame("Fill Tags");
    private JPanel panel = new JPanel();
    private GridBagConstraints left = new GridBagConstraints();
    private GridBagConstraints right = new GridBagConstraints();
    private HashMap<String, JTextField> tagValues = new HashMap<String, JTextField>();

    public TagParser(String msg) {
        //Parsing Tags
        tagFrame.getContentPane().add(panel);
        this.stringToSearch = msg.replaceAll("><", "> <");
//        System.out.println(stringToSearch);
        this.defaultTokenizer = new StringTokenizer(stringToSearch);

        //JFrame For Filling Tags
        panel.setLayout(new GridBagLayout());
        left.anchor = GridBagConstraints.EAST;
        left.insets = new Insets(5,5,5,5);
        right.insets = new Insets(5,5,5,5);
        right.weightx = 2.0;
//        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;

    }

    /**
     * While there are more tokens left to parse in the message, check to see if they are tags.
     * If the token is a tag, send it to the tagFillPrompt method to be filled.
     */
    private String parseTags() {
        ArrayList<String> tags = new ArrayList<>();
        while (defaultTokenizer.hasMoreTokens()) {
            String token = defaultTokenizer.nextToken();
            Matcher m = p.matcher(token);
            if(m.find() && !tags.contains(token)) {
                tags.add(m.group());
            }
        }
        tagFillPrompt(tags);
        return stringToSearch;
    }

    /**
     * This method takes a token matching the Tag format, and prompts the user to provide a value for it. Once
     * the user has entered their value, the
     * @param tags
     */
    private void tagFillPrompt(ArrayList<String> tags) {
       boolean tagsValid = false;
        for(String tag : tags) {
            populateTagFrame(tag);
        }
        int result;
        while(!tagsValid) {
            result = JOptionPane.showConfirmDialog(null, panel, "Please Fill Tags", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                tagsValid = checkTagCount(tags);
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        replaceTags(tags);
//        System.out.println(stringToSearch);
    }

    private void replaceTags(ArrayList<String> tags) {
        for(String tag : tags) {
            String newValue = tagValues.get(tag).getText();
            stringToSearch = stringToSearch.replaceAll(tag, newValue);
        }
    }

    private Boolean checkTagCount(ArrayList<String> tags) {
        int tagsFilled = 0;
        boolean filled;
            for (String tag : tags) {
                if (!tagValues.get(tag).getText().equals("")) {
                    tagsFilled += 1;
                }
            }
            if (tagsFilled != tagValues.size()){
                filled = false;
                JOptionPane.showMessageDialog(null, "Tags May Not Be Left Blank.");
            }else{
                filled = true;
            }
        return filled;
    }

    private void populateTagFrame(String tag) {
        JLabel label = new JLabel("What should the value of " + tag + " be?");
        tagValues.put(tag, new JTextField(15));
        panel.add(label, left);
        panel.add(tagValues.get(tag), right);
    }

    public String returnParsedMessage() {
        return parseTags();
    }

}
