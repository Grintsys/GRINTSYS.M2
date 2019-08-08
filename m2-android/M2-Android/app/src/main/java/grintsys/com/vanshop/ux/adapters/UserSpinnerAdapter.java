package grintsys.com.vanshop.ux.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grintsys.com.vanshop.R;
import grintsys.com.vanshop.entities.User.User;
import timber.log.Timber;

/**
 * Created by alienware on 2/23/2017.
 */

public class UserSpinnerAdapter extends ArrayAdapter<User> {

    private static final int layoutID = R.layout.spinner_item_sales_person;
    private final LayoutInflater layoutInflater;

    private List<User> userList;

    public UserSpinnerAdapter(Context context, List<User> users) {
        super(context, layoutID, users);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userList = users;
    }

    public UserSpinnerAdapter(Context context) {
        super(context, layoutID);
        this.userList = new ArrayList<>();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UserSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        this.userList = new ArrayList<>();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        if (userList != null) {
            this.userList.addAll(userList);
            notifyDataSetChanged();
        } else {
            Timber.e("Trying set null user list in %s", this.getClass().getSimpleName());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        UserSpinnerAdapter.ListItemHolder holder;

        if (v == null) {
            v = layoutInflater.inflate(layoutID, parent, false);
            holder = new UserSpinnerAdapter.ListItemHolder();
            holder.userText = (TextView) v.findViewById(R.id.sales_person_text);
            v.setTag(holder);
        } else {
            holder = (UserSpinnerAdapter.ListItemHolder) v.getTag();
        }

        if (userList.get(position) != null && userList.get(position).getName() != null) {
            holder.userText.setText(userList.get(position).getName());
        } else {
            Timber.e("Received null productSize in %s", this.getClass().getSimpleName());
        }
        return v;
    }

    static class ListItemHolder {
        TextView userText;
    }
}
