package grintsys.com.vanshop.entities.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alienware on 2/22/2017.
 */

public class UserListResult {

    private List<User> users;

    public UserListResult(){

    }

    public List<User> getUserList() {
        return users;
    }

    public void setUserList(List<User> userList) {
        this.users = userList;
    }
}
