package pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

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
     * Checks whether it contains an @ symbol and an . .
     * If it contains both then return true, otherwise false.
     */
    public static boolean verifyEmail(String email){
        boolean containsAT = false;
        boolean containsDOT = false;
        boolean inputOK = false;
        for(char c : email.toCharArray()){
            if(c == '@'){
                containsAT = true;
            }
            if(c == '.'){
                containsDOT = true;
            }
        }
        if(containsAT && containsDOT){
            inputOK = true;
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
