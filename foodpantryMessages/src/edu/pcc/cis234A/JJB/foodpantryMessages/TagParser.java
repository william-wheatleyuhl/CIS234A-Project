package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.util.StringTokenizer;
import java.util.regex.*;

/**
 * With the Given Structure of message tags, parse the text of a given message string to find the tags, and identify
 * their values.
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

    public void parseTags() {

        while (defaultTokenizer.hasMoreTokens()) {
            String token = defaultTokenizer.nextToken();
            Matcher m = p.matcher(token);
            if(m.find()) {
                findAndReplace(m.group());
            }
        }
        System.out.println(stringToSearch);
    }

    public void findAndReplace(String token) {
        String value = JOptionPane.showInputDialog("What should be in the " + token + " field?");
        System.out.println(value);
        System.out.println(token);
        stringToSearch = stringToSearch.replaceFirst(token, value);
    }

}
