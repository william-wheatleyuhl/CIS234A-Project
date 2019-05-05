package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Syn, Jeff
 * @version 2019.04.23
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

    public static String usernameDB;
    public static String lastNameDB;
    public static String firstNameDB;
    public static String emailDB;
    public static String[] firstLast;

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
                    if(firstLast[0].length() <= 25 && firstLast[1].length() <= 25){
                        nameOK = true;
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Invalid Name. Too Long.");
                    }
                }

                if(suUsernameField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Username");
                }
                else{
                    if(suUsernameField.getText().length() <= 50){
                        usernameOK = true;
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Invalid Username. Too Long.");
                    }
                }

                if(!emailOK){
                    JOptionPane.showMessageDialog(null, "Invalid Email");
                }

                boolean passwordOK = UserLogin.verifySignUpPassword(suPasswordField1.getText(), suPasswordField2.getText());
                if(!passwordOK){
                    JOptionPane.showMessageDialog(null, "Invalid Password");
                }

                /**
                 * assuming java works like C and && operator only compares two values
                 */
                if((nameOK && usernameOK) && (emailOK && passwordOK)){
                    usernameDB = suUsernameField.getText();
                    lastNameDB = firstLast[1];
                    firstNameDB = firstLast[0];
                    emailDB = suEmailField.getText();

                    database1.insertUser(highestUserID);

                    JOptionPane.showMessageDialog(null, "Sucessfully signed up!");
                }
            }
        });

        /**
         * This code executes when you press the sign in button.
         * At present it only checks that both the fields contain *some* data.
         * In the future when we are connected to the database it will verify that the username
         * exists and that the generated password hash matches the one in the database.
         */
        siButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(siUsernameField.getText().equals("") || siPasswordField.getPassword().toString().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Login");
                }
                else{
                    JOptionPane.showMessageDialog(null, "You Have Signed In");
                }
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
