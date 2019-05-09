package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Syn, Jeff
 * @version 2019.05.08
 */
public class UserLoginGUI {
    private JPanel rootPanel;
    private JLabel pageName;
    private JLabel signIn;
    private JTextField siUsernameField;
    private JPasswordField siPasswordField;
    private JLabel siPasswordLabel;
    private JLabel siUsernameLabel;
    private JButton siButton;
    private JLabel signUp;
    private JLabel suNameLabel;
    private JTextField suNameField;
    private JLabel suEmailLabel;
    private JFormattedTextField suEmailField;
    private JLabel suUsernameLabel;
    private JTextField suUsernameField;
    private JLabel suPasswordLabel1;
    private JTextField suPasswordField1;
    private JLabel suPasswordLabel2;
    private JTextField suPasswordField2;
    private JButton suButton;

    public static String[] usernameDB;
    public static String lastNameDB;
    public static String firstNameDB;
    public static String emailDB;
    public static String[] firstLast;
    public static String passwordDB;

    private UserLoginDB database1 = new UserLoginDB();
    int highestUserID = database1.getHighestUserID();


    public UserLoginGUI() {
        rootPanel.setPreferredSize(new Dimension(400, 400));

        /**
         * This is the code that executes when you press the sign up button.
         * It checks that the name and username field contain any data. When our database
         * is standardized it will verify that they do not exceed the character limit we have set.
         * It checks that the email has an @ and . in it, and that the 2 entered passwords
         * match each other.
         */
        suButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean nameOK = false;
                boolean usernameOK = false;
                boolean emailOK = UserLogin.verifyEmail(suEmailField.getText());
                if(suNameField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Name");
                }
                else{
                    String fullName = suNameField.getText();
                    firstLast = fullName.split(" ");
                    if(firstLast.length > 1 && (firstLast[0].length() <= 25 && firstLast[1].length() <= 25)){
                        nameOK = true;
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Invalid Name");
                    }
                }

                if(!emailOK){
                    JOptionPane.showMessageDialog(null, "Invalid Email");
                }
                else{
                    usernameDB = suEmailField.getText().split("@");
                    suUsernameField.setText(usernameDB[0]);
                }

                boolean passwordOK = UserLogin.verifySignUpPassword(suPasswordField1.getText(), suPasswordField2.getText());
                if(!passwordOK){
                    JOptionPane.showMessageDialog(null, "Invalid Password");
                }

                boolean emailUnique = UserLogin.verifyEmailUnique(suEmailField.getText());

                if(!emailUnique){
                    JOptionPane.showMessageDialog(null, "Email Already Used");
                }

                /**
                 * assuming java works like C and && operator only compares two values
                 */
                if((nameOK && emailUnique) && (emailOK && passwordOK)){
                    lastNameDB = firstLast[1];
                    firstNameDB = firstLast[0];
                    emailDB = suEmailField.getText();
                    passwordDB = UserLogin.hashPassword(suPasswordField1.getText());

                    database1.insertUser(highestUserID);

                    JOptionPane.showMessageDialog(null, "Sucessfully signed up!");
                }
            }
        });

        /**
         * This code executes when you press the sign in button.
         * First it checks that the username you typed in exists.
         * Then it hashes the PW you typed in.
         * It then runs checkHashedPWUserMatch to verify that in the PASSWORD table there is a row
         * which contains the UserID associated with the username typed in and that the associated hash
         * is the same as the one generated from the password the user typed in.
         */
        siButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                /*if(siUsernameField.getText().equals("") || siPasswordField.getPassword().toString().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Login");
                }
                else{
                    JOptionPane.showMessageDialog(null, "You Have Signed In");
                }*/

                int signInUserID = 0;
                String signInPW;
                String signInPWHash;
                boolean signInSuccess = false;

                boolean userExists = database1.checkUserExists(siUsernameField.getText());

                if(userExists){
                    signInUserID = database1.getUsernameUserID(siUsernameField.getText());

                    signInPW = new String(siPasswordField.getPassword());
                    signInPWHash = UserLogin.hashPassword(signInPW);

                    //System.out.println(signInUserID);
                    //System.out.println(signInPWHash);

                    signInSuccess = database1.checkHashedPWUserMatch(signInPWHash, signInUserID);

                    if(!signInSuccess){
                        JOptionPane.showMessageDialog(null, "Invalid Sign-in");
                    }
                    else{
                        //JOptionPane.showMessageDialog(null, "Signed in!");
                        /**
                         * This is where the splash screen code will execute
                         */
                        UserLogin loginSuccess = new UserLogin();
                        loginSuccess.setLogin(true);
                        loginSuccess.setLoggedInUser(siUsernameField.getText());


                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Invalid Sign-in");
                }


            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
