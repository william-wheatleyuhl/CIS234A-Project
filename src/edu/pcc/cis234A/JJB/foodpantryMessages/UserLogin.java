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

        /*UserLoginDB database1 = new UserLoginDB();
        int highestUserID = database1.getHighestUserID();
        System.out.println(highestUserID);
        database1.insertUser(highestUserID);*/

    }

    /***
     * This method ensures that the email provided contains an arbitrary amount of '.'s before the one
     * '@', but only one (mandatory) '.' after the one '@'. No '@' or '.' can be either the first or last character
     * of the email string. If there is a '.' at the character directly before or directly after the '@' then the email
     * is invalid. Before the '@' any 2 '.'s with an index differential of 1 results in an invalid email address.
     */
    public static boolean verifyEmail(String email){
        boolean containsAT = false;
        boolean containsDOTAfterAT = false;
        boolean inputOK = false;
        int charIndex = 0;
        int atIndex = -1;
        int dotAfterAtIndex = -1;
        for(char c : email.toCharArray()){
            if(c == '@'){
                if(containsAT){
                    return false;
                }
                containsAT = true;
                atIndex = charIndex;

                if(charIndex == 0 || charIndex == email.length() - 1){
                    return false;
                }
            }

            if(c == '.'){
                if(charIndex == 0 || charIndex == email.length() - 1){
                    return false;
                }

                if(email.charAt(charIndex + 1) == '@'){
                    return false;
                }

                if(email.charAt(charIndex + 1) == '.' || email.charAt(charIndex - 1) == '.'){
                    return false;
                }

                if(containsAT){
                    if(containsDOTAfterAT){
                        return false;
                    }
                    containsDOTAfterAT = true;
                    dotAfterAtIndex = charIndex;
                }
            }

            charIndex += 1;
        }

        if((containsAT && containsDOTAfterAT) && dotAfterAtIndex - atIndex > 1){
            if(email.length() <= 50){
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
