package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

/**
 * @author Syn
 * @version 2019.04.23
 */
public class templateEditor {
    public static void createAndShowTemplate() {
        JFrame frame = new JFrame("Food Pantry Template Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new createTemplateForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowTemplate());
    }
}
