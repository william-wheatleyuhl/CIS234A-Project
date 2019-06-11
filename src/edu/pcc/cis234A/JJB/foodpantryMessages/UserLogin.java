package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Jeff
 * @version 2019.04.23
 */

public class UserLogin {
    /***
     * Creates an instance of the UserLoginGUI class.
     * Creates a JFrame window, sets the content of it to the panel of the instance of the UserLoginGUI class.
     */

    private static boolean loginSuccess = false;
    private static String loggedInUser;

    public static byte[] salt = new byte[16];

    public void setLogin(boolean truefalse){
        loginSuccess = truefalse;
    }
    public void setLoggedInUser(String user){ loggedInUser = user; }
    public String getLoggedInUser(){ return loggedInUser; }

    public static void main(String[] args) {
        JFrame jf1 = new JFrame();
        jf1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf1.setTitle("Sign In");
        jf1.setSize(800, 600);
        jf1.setContentPane(new UserLoginGUI().getRootPanel());
        jf1.setVisible(true);

        while (!loginSuccess) {
            System.out.println("");
        }

        jf1.setVisible(false);

        JFrame jf2 = new JFrame();
        jf2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf2.setTitle("Food Pantry Notifications");
        jf2.setSize(900, 750);
        jf2.setContentPane(new Presentation().getRootPanel());
        jf2.setVisible(true);

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
                System.out.println("1");
                if(containsAT){
                    System.out.println("2");
                    return false;
                }
                containsAT = true;
                atIndex = charIndex;

                if(charIndex == 0 || charIndex == email.length() - 1){
                    System.out.println("3");
                    return false;
                }
            }

            if(c == '.'){
                System.out.println("4");
                if(charIndex == 0 || charIndex == email.length() - 1){
                    System.out.println("5");
                    return false;
                }

                if(email.charAt(charIndex + 1) == '@'){
                    System.out.println("6");
                    return false;
                }

                if(email.charAt(charIndex + 1) == '.' || email.charAt(charIndex - 1) == '.'){
                    System.out.println("7");
                    return false;
                }

                if(containsAT){
                    System.out.println("8");
                    if(containsDOTAfterAT){
                        System.out.println("9");
                        return false;
                    }
                    containsDOTAfterAT = true;
                    dotAfterAtIndex = charIndex;
                }
            }

            charIndex += 1;
        }

        if((containsAT && containsDOTAfterAT) && dotAfterAtIndex - atIndex > 1){
            System.out.println("10");
            if(email.length() <= 50){
                inputOK = true;
            }
        }

        if(email.contains("@pcc.edu")) {
            return inputOK;
        }
        System.out.println("11");
        return false;
    }


    /**
     * This method pulls all the emails from the database and insures that the email
     * in the paremeters does not match any emails already used.
     *
     */
    public static boolean verifyEmailUnique(String email){
        boolean unique = true;

        UserLoginDB database1 = new UserLoginDB();

        ArrayList<String> emailList = database1.getUserEmails();

        for(String s : emailList){
            if(email.equals(s)){
                unique = false;
            }
        }

        return unique;
    }

    /***
     * Takes both password entries from the sign up and verifies that they are the same.
     */
    public static boolean verifySignUpPassword(String password, String password2){
        if(password.equals(password2)){
            if(passwordStrong(password)) {
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * Method to check the strength of a password. Ensures that the password is at least 12 characters long and
     * contains at least one uppercase letter, and at least one number.
     */
    public static boolean passwordStrong(String password){
        boolean lengthOK = false;
        boolean containsUpperLetter = false;
        boolean containsNumber = false;

        if(password.length() < 12){
            lengthOK = false;
        }
        else{
            lengthOK = true;
        }

        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)){
                containsUpperLetter = true;
            }

            if(Character.isDigit(c)){
                containsNumber = true;
            }
        }

        if((lengthOK && containsUpperLetter) && containsNumber){
            return true;
        }
        return false;
    }


    public static void generateSalt(){
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
    }

    /**
     *
     * This method hashes the password fed to it with MD5.
     */
    public static String hashPassword(String password, String salt){
        String hashedPW = null;

        password += salt;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i ++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPW = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        //System.out.println(hashedPW);
        //System.out.println(salt);

        return hashedPW;
    }

    public static String genPasswordResetCode(){
        String code = "";
        int randNum = 0;
        String randNumString = "";

        Random rand = new Random();
        for(int i = 0; i < 4; i += 1){
            randNum = rand.nextInt(10);
            randNumString = Integer.toString(randNum);
            code = code.concat(randNumString);
        }
        return code;
    }

}
