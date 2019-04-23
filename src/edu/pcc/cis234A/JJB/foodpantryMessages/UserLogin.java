package pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

/**
 * @author Jeff
 * @version 2018.04.23
 */

public class UserLogin {
    /***
     * Creates an instance of the UserLoginGUI class.
     * Creates a JFrame window, sets the content of it to the panel of the instance of the UserLoginGUI class.
     */
    public static void main(String[] args){

        UserLoginGUI main1 = new UserLoginGUI();

        JFrame jf1 = new JFrame();

        jf1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf1.setTitle("Sign In");
        jf1.setSize(800, 600);
        jf1.setContentPane(new UserLoginGUI().getRootPanel());
        jf1.setVisible(true);

    }

    /***
     * Takes email value from email textbox in the sign up half of the page.
     * Checks whether it contains an '@' symbol and a '.' .
     * A valid email can have multiple '.'s (like our PCC emails), but only one '@'.
     * The method iterates through all characters in a string so if it comes across an
     * '@' and we know one has already been found because our boolean has been set to true
     * then we just return false immediately. The final if statement checks that the email address
     * contains a '.' and '@', and that the index at which the '.' occurs is greater than the index
     * at which the '@' occurs. It then checks that the '@' character is not the 0th index, and that the '.'
     * character is not at the last index. Finally it insures that the difference between their indexes is greater
     * than 1 to avoid something like name@.com which isn't valid at all.
     */
    public static boolean verifyEmail(String email){
        boolean containsAT = false;
        boolean containsDOT = false;
        boolean inputOK = false;
        int charIndex = 0;
        int atIndex = -1;
        int dotIndex = -1;
        for(char c : email.toCharArray()){
            if(c == '@'){
                if(containsAT){
                    return false;
                }
                containsAT = true;
                atIndex = charIndex;
            }
            if(c == '.'){
                containsDOT = true;
                dotIndex = charIndex;
            }
            charIndex += 1;
        }
        if(((containsAT && containsDOT) && (dotIndex > atIndex)) && ((atIndex != 0) && (dotIndex != email.length() - 1))){
            if(dotIndex - atIndex > 1){
                inputOK = true;
            }
        }
        return inputOK;
    }

    /***
     * Takes both password entries from the sign up and verifies that they are the same.
     */
    public static boolean verifySignUpPassword(String password, String password2){
        if(password.equals(password2)){
            return true;
        }
        else{
            return false;
        }
    }


}
