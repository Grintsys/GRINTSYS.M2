package grintsys.com.vanshop.interfaces;

import grintsys.com.vanshop.entities.User.User;

/**
 * Interface declaring methods for login dialog.
 */
public interface LoginDialogInterface {

    void successfulLoginOrRegistration(User user);

}
