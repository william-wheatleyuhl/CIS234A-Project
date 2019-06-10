package edu.pcc.cis234A.JJB.foodpantryMessages;

/**
 * Role Class
 * A class to represent a Role object from the Database.
 *
 * @author Syn
 * @version 2019.06.09
 */
public class Role {
    public int roleID;
    public String roleName;

    public Role(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public int getRoleID() {
        return roleID;
    }

    public String getRoleName() {
        return roleName;
    }
}
