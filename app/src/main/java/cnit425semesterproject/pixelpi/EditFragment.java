/*
MIT License

        Copyright (c) 2018 Jack Francis Bartolone, Robert Benjamin Osborne
        Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
        to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
        and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
        The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
        IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package cnit425semesterproject.pixelpi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jackb on 4/16/2018.
 */

//Fragment used to edit each individual device
// TODO: 4/28/2018 consider changing name
public class EditFragment extends Fragment implements DeviceTaskCallback {
    //ui
    private View rootview;
    private View footerview;
    private TextView txtdevicenameedit;
    private ListView lvdevicetasks;
    private ImageView imgplus;
    private DeviceTaskAdapter deviceTaskAdapter;
    private ArrayList<DeviceTask> deviceTasks;

    //other variables
    private Device selecteddevice;
    private String mode;
    private EditFragmentListener listener; // TODO: 4/28/2018 Use functions

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceTasks = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootview == null) {
            rootview = inflater.inflate(R.layout.edit_layout, container, false);
            footerview = inflater.inflate(R.layout.listview_plus_footer, null, false); //set to llplusfooter linear layout

            txtdevicenameedit = rootview.findViewById(R.id.txtdevicenameedit);
            imgplus = footerview.findViewById(R.id.imgplus);
            lvdevicetasks = rootview.findViewById(R.id.lvdevicetasks);

            deviceTaskAdapter = new DeviceTaskAdapter(getActivity(), deviceTasks);
            lvdevicetasks.addFooterView(footerview); //adding footer before setting adapter
            lvdevicetasks.setAdapter(deviceTaskAdapter);

            // adding new device task
            imgplus.setOnClickListener(new View.OnClickListener() { 
                @Override
                public void onClick(View view) {
                    //build dialog to get task mode/type
                    final AlertDialog.Builder selecttypedialog = new AlertDialog.Builder(getActivity());
                    selecttypedialog.setTitle("Select type of new task");
                    //selecting item
                    selecttypedialog.setSingleChoiceItems(R.array.devicemodes, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //once chosen
                            String[] list = getResources().getStringArray(R.array.devicemodes);
                            //type of device task
                            mode = list[i];
                        }
                    });
                    //cancelling dialog
                    selecttypedialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                        }
                    });
                    //moving to next dialog
                    selecttypedialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(mode != null) {
                                switch (mode) {
                                    case "simple": SimpleDialog simpledialog= SimpleDialog.newInstance();
                                        simpledialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_Fullscreen);
                                        simpledialog.show(getFragmentManager(), getString(R.string.SIMPLE_DIALOG));
                                        dialogInterface.dismiss();
                                        break;
                                    case "timer": AlertDialog.Builder timerdialog = new AlertDialog.Builder(getActivity());
                                        // TODO: 4/21/2018 Decide on whether to use Android provided timer, AlertDialog.Builder, or custom dialog class
                                        break;
                                }
                            }
                            else
                            {
                                dialogInterface.cancel();
                            }
                        }
                    });
                    //set onCancel event
                    selecttypedialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(getActivity(), "Please select a task mode", Toast.LENGTH_LONG).show();
                        }
                    });
                    //selecttypedialog.cancellable(bool) default is true
                    selecttypedialog.create();
                    //show dialog
                    selecttypedialog.show();
                }
            });

        }
        return rootview;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditFragmentListener) context;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroyView() {
        if(rootview.getParent() != null) {
            ((ViewGroup)rootview.getParent()).removeView(rootview);
        }
        super.onDestroyView();
    }

    //callback from dialog fragments
    //update device tasks, send back to main activity
    @Override
    public void sendnewdevicetask(DeviceTask deviceTask) {
        ArrayList<DeviceTask> deviceTasks = selecteddevice.getDeviceTasks();
        deviceTasks.add(deviceTask);
        selecteddevice.setDeviceTasks(deviceTasks);
        deviceTaskAdapter.notifyDataSetChanged();
        listener.updatedevice(selecteddevice);
        listener.activatedevicetask(deviceTask);
    }

    //getters and setters
    public Device getSelecteddevice() {
        return selecteddevice;
    }

    //sets up Fragment
    public void setSelecteddevice(Device selecteddevice) {
        this.selecteddevice = selecteddevice;
        String devicename = getString(R.string.devicedisplaytext) + selecteddevice.getDevicename();
        txtdevicenameedit.setText(devicename);
        this.deviceTasks = selecteddevice.getDeviceTasks();
        if(!(selecteddevice.getDeviceTasks().isEmpty())) //if device has tasks, update adapter
        {
            deviceTasks.clear();
            deviceTasks.addAll(selecteddevice.getDeviceTasks());
            deviceTaskAdapter.notifyDataSetChanged();
        }

    }

}
