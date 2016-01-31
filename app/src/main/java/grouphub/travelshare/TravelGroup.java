package grouphub.travelshare;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kenme_000 on 1/30/2016.
 */
public class TravelGroup {
    private static final String TAG = "TravelGroup";
    private String groupName;
    private ParseUser leader;
    private List<ParseObject> photos;
    private List<String> users;
    private String description;
    private ParseObject currentGroup;

    public TravelGroup(ParseUser leader, String groupName) {
        this.leader = leader;
        this.groupName = groupName;
        users = new LinkedList<String>();
        users.add(leader.getUsername());
        currentGroup = new ParseObject(TAG);
        currentGroup.put("leader", leader);
        currentGroup.put("groupName", groupName);
        currentGroup.addAll("users", users);
        LinkedList<ParseObject> groups = (LinkedList) this.leader.get("groups");
        groups.addFirst(currentGroup);
        currentGroup.saveInBackground();
        this.leader.put("groups", groups);
        this.leader.saveInBackground();
    }

    public void addUser(ParseUser user) {
        users.add(user.getUsername());
        currentGroup.addAll("users", users);
        LinkedList<ParseObject> groups = (LinkedList) user.get("groups");
        groups.addFirst(currentGroup);
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

    public List<String> getUsers() {
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
