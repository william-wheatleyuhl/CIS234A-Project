package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
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

    public TagParser(String msg) {
        this.stringToSearch = msg.replaceAll("><", "> <");
        System.out.println(stringToSearch);
        this.defaultTokenizer = new StringTokenizer(stringToSearch);
    }

    /**
     * While there are more tokens left to parse in the message, check to see if they are tags.
     * If the token is a tag, send it to the findAndReplace method to be filled.
     */
    public String parseTags() {
        while (defaultTokenizer.hasMoreTokens()) {
            String token = defaultTokenizer.nextToken();
            Matcher m = p.matcher(token);
            if(m.find()) {
                findAndReplace(m.group());
            }
        }
        System.out.println(stringToSearch);
        return stringToSearch;
    }

    /**
     * This method takes a token matching the Tag format, and prompts the user to provide a value for it. Once
     * the user has entered their value, the
     * @param token The current tag to be replaced by a user defined value
     */
    private void findAndReplace(String token) {
        String value = JOptionPane.showInputDialog( "What should be in the " + token + " field?");
        while(value.equals("")){
            JOptionPane.showMessageDialog(null, "Tags May Not Be Left Blank.");
            value = JOptionPane.showInputDialog("What should be in the " + token + " field?");
        }
        stringToSearch = stringToSearch.replaceFirst(token, value);
    }

}
