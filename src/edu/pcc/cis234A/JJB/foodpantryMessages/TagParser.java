package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.*;

/**
 * With the Given Structure of message tags, parse the text of a given message string to find the tags, and identify
 * their values. Create TagPrompt to obtain tag values from user. Substitute tags for user provided values, return
 * modified string.
 * @author William Wheatley-Uhl
 * @version 2019.05.25
 */
class TagParser {
    private String messageString;
    private StringTokenizer defaultTokenizer;
    private String pattern = "<(.*?)>";
    private Pattern p = Pattern.compile(pattern);
    private ArrayList<String> tags = new ArrayList<>();
    private TagPrompt prompt;
    private String imageSrcPath;

    public TagParser(String msg) {
        this.messageString = msg.replaceAll("><", "> <");
        this.defaultTokenizer = new StringTokenizer(messageString);
        this.prompt = new TagPrompt();
    }

    /**
     * While there are more tokens left to parse in the message, check to see if they are tags.
     * If the token is a tag, only add it to the ArrayList if it is not already present.
     *
     */
    private void parseTags() {
        while (defaultTokenizer.hasMoreTokens()) {
            String token = defaultTokenizer.nextToken();
            Matcher m = p.matcher(token);
            if(m.find() && !tags.contains(token)) {
                tags.add(m.group());
            }
        }
    }
    /**
     * Given an array list of tags, populate the JOptionPane with fields to fill tags. While all tags fields are not
     * filled, present an error message and prompt for tags again. If cancel button is pressed, exit without
     * sending messages.
     */
    private void tagFillPrompt() {
       boolean tagsValid = false;
       if(tags.size() > 0) {
           for (String tag : tags) {
               prompt.populateTagFrame(tag);
           }
           int result;
           while (!tagsValid) {
               result = JOptionPane.showConfirmDialog(null, prompt.getPanel(), "Please Fill Tags", JOptionPane.OK_CANCEL_OPTION);
               if (result == JOptionPane.OK_OPTION) {
                   tagsValid = checkTagCount();
               } else if (result == JOptionPane.CANCEL_OPTION) {
                   return;
               }
           }
       } else {
           return;
       }
    }
    /**
     * For each given tag in the tags list, substitute the provided value from the
     * tag prompt window. Original tags are stored in the tags ArrayList, and are used to query
     * the values of the tagValues hashmap. Tags act as keys, the values are the text from the
     * tag prompt text fields.
     */
    private void replaceTags() {
        for(String tag : tags) {
            if(tag.contains("image")) {
                messageString = messageString.replaceAll(tag, "<img style=\"max-height:300px;\" src=\"cid:image\">");
            } else {
                String newValue = prompt.getTagValue(tag);
                messageString = messageString.replaceAll(tag, newValue);
            }
        }
        System.out.println(messageString);
    }

    /**
     * A method to validate that all tags have been provided with values in the TagPrompt.
     * If a value is present, increment the tagsFilled counter, this counter must match the number of tags to be filled.
     * @return Boolean value of whether tags were filled or not.
     */
    private Boolean checkTagCount() {
        int tagsFilled = 0;
        boolean filled;
            for (String tag : tags) {
                if (prompt.checkTagFilled(tag)) {
                    tagsFilled += 1;
                }
            }
            if (tagsFilled != prompt.getTagValuesSize()){
                filled = false;
                JOptionPane.showMessageDialog(null, "Tags May Not Be Left Blank.");
            }else{
                filled = true;
            }
        return filled;
    }

    /**
     *
     * @return
     */
    public boolean checkForImages() {
        boolean needsImage = false;
        for(String tag : tags) {
            if(tag.equals("image")) {
                needsImage = true;
            }
        }
        return needsImage;
    }

    /**
     * Parse tags out of message, prompt user to fill tags on message send, replace tags with provided values.
     * @return messageString The message with tags filled in.
     */
    public String returnParsedMessage() {
        parseTags();
        tagFillPrompt();
        replaceTags();
        if(prompt.getImageFileSrc() != null) {
            imageSrcPath = prompt.getImageFileSrc();
        }
        return messageString;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
    public String getImageSrcPath() {
        return imageSrcPath;
    }
}
