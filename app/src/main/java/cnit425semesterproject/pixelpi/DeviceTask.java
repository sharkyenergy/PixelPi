package cnit425semesterproject.pixelpi;


import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jackb on 4/15/2018.
 */

//tasks that devices can do
public class DeviceTask {
    String mode; //type of devicetask
    String name;
    int id; //generated by database
    boolean activated;

    //getter and setters
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    //other methods
    public JSONObject toJSON()
    {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }


    //constructors
    public DeviceTask(String mode, String name, int id, boolean activated) {
        this.mode = mode;
        this.name = name;
        this.id = id;
        this.activated = activated;
    }

    public DeviceTask(JSONObject jsonObject)  //build devicetask from json
    {
        String mode = "";
        String name = "";
        int id;
        try {
            mode = jsonObject.getString("mode");
            name = jsonObject.getString("name");
            id = jsonObject.getInt("id");
            this.mode = mode;
            this.name = name;
            this.id = id;
            this.activated = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
