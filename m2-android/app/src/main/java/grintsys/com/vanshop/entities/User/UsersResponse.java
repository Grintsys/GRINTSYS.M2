package grintsys.com.vanshop.entities.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alienware on 2/22/2017.
 */

public class UsersResponse {

    @SerializedName("records")
    private List<User> userList;

    public UsersResponse(){

    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
