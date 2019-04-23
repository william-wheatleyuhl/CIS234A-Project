package pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Syn
 * @version 2019.04.10
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
                boolean emailOK = UserLogin.verifyEmail(suEmailField.getText());
                if(suNameField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Name");
                }

                if(suUsernameField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Invalid Username");
                }

                if(!emailOK){
                    JOptionPane.showMessageDialog(null, "Invalid Email");
                }

                boolean passwordOK = UserLogin.verifySignUpPassword(suPasswordField1.getText(), suPasswordField2.getText());
                if(!passwordOK){
                    JOptionPane.showMessageDialog(null, "Invalid Password");
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
