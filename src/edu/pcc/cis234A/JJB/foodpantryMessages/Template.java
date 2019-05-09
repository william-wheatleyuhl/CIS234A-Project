package edu.pcc.cis234A.JJB.foodpantryMessages;

/**
 * Template Class.
 * A class to represent a Template object from the Database.
 * Created by Will Wheatley-Uhl on 4/23/19
 * @author Will Wheatley-Uhl
 * @version 2019.04.23
 */
public class Template {
    public int templateID;
    public String templateName;
    public String messageText;

    public Template(int templateID, String templateName, String messageText) {
        this.templateID = templateID;
        this.templateName = templateName;
        this.messageText = messageText;
    }

    public int getTemplateID() {
        return templateID;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getMessageText() {
        return messageText;
    }

}
