package grouphub.travelshare;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Kenan Mesic and Christopher Chinowth on 1/30/2016.
 */
public class TravelGroup {
    private static final String TAG = "TravelGroup";
    private String groupName;
    private ParseUser leader;
    private ArrayList<ParseObject> photos;
    private ArrayList<String> users;
    private String description;
    private ParseObject currentGroup;

    public TravelGroup(ParseUser leader, String groupName) {
        this.leader = leader;
        this.groupName = groupName;
        users = new ArrayList<String>();
        users.add(leader.getUsername());
        currentGroup = new ParseObject(TAG);
        currentGroup.put("leader", leader);
        currentGroup.put("groupName", groupName);
        currentGroup.addAll("users", users);
        ArrayList <ParseObject> groups = (ArrayList) this.leader.get("groups");
        groups.add(0, currentGroup);
        currentGroup.saveInBackground();
        this.leader.put("groups", groups);
        this.leader.saveInBackground();
    }

    //TODO send a request to invite a user
    public void inviteUser(ParseUser user) {
        return;
    }

    //Add the current user logged in
    public void addUser(ParseUser user) {
        users.add(user.getUsername());
        ArrayList<String> newUser = new ArrayList<String>();
        newUser.add(user.getUsername());
        currentGroup.addAll("users", newUser );
        ArrayList<ParseObject> groups = (ArrayList) user.get("groups");
        groups.add(0, currentGroup);
        currentGroup.saveInBackground();
        user.put("groups", groups);
        user.saveInBackground();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        currentGroup.put("description", description);
        currentGroup.saveInBackground();
    }

    public ParseUser getLeader() {
        return leader;
    }

    public void setLeader(ParseUser leader) {
        this.leader = leader;
        currentGroup.put("leader", leader);
        currentGroup.saveInBackground();
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        currentGroup.put("groupName", groupName);
        currentGroup.saveInBackground();
    }
}
