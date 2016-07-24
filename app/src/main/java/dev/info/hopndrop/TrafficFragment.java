package dev.info.hopndrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.info.hopndrop.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This is an example usage of the AnimatedExpandableListView class.
 *
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class TrafficFragment extends Fragment{
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
  
    private SessionManager session;
    private SQLiteHandler db;
    ProgressDialog pDialog;
    GroupItem item;
    ArrayList<HashMap<String, String>> areaList;
    List<GroupItem> items;
    public TrafficFragment(){

    }
    String token;
    View rootView;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_traffic, container, false);

        items = new ArrayList<GroupItem>();
        db = new SQLiteHandler(getActivity().getApplicationContext());
        areaList = new ArrayList<HashMap<String, String>>();
        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");
        // Populate our list with groups and it's children

        //getAreas();
        for(int i = 1; i < 11; i++) {
            getlines(i+"");
        }



        return rootView;
    }


    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String updated;
        String northStatus;
        String southStatus;
    }

    private static class ChildHolder {
        TextView title;
        TextView updated;
        TextView northStatus;
        TextView southStatus;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.updated = (TextView) convertView.findViewById(R.id.textUpdated);
                holder.northStatus = (TextView) convertView.findViewById(R.id.northBound);
                holder.southStatus = (TextView) convertView.findViewById(R.id.southBound);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.updated.setText(item.updated);
            holder.northStatus.setText(item.northStatus);

            if(item.northStatus.equals("LIGHT TRAFFIC")){
                holder.northStatus.setBackgroundColor(Color.parseColor("#00ff99"));
            }
            else if(item.northStatus.equals("MODERATE TRAFFIC")){
                holder.northStatus.setBackgroundColor(Color.parseColor("#ffff66"));
            }
            else if(item.northStatus.equals("HEAVY TRAFFIC")){
                holder.northStatus.setBackgroundColor(Color.parseColor("#ff5050"));
            }

            holder.southStatus.setText(item.southStatus);
            if(item.southStatus.equals("LIGHT TRAFFIC")){
                holder.southStatus.setBackgroundColor(Color.parseColor("#00ff99"));
            }
            else if(item.southStatus.equals("MODERATE TRAFFIC")){
                holder.southStatus.setBackgroundColor(Color.parseColor("#ffff66"));
            }
            else if(item.southStatus.equals("HEAVY TRAFFIC")){
                holder.southStatus.setBackgroundColor(Color.parseColor("#ff5050"));
            }



            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }




    private void getlines(String area_id) {
        // Tag used to cancel the request
        String tag_string_req = "get_alerts";


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_TRAFFIC+area_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Areas Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String  result = jObj.getString("lines");

                    if (!result.equals("")) {
                        JSONObject area = jObj.getJSONObject("area");
                        JSONArray lines = jObj.getJSONArray("lines");


                        String name = area.getString("name");
                        item = new GroupItem();
                        item.title = name;
                        for (int i = 0; i < lines.length(); i++) {
                            JSONObject c = lines.getJSONObject(i);


                            //getlines(area_id);


                           // items.add(item);
                            // Storing each json item in variable
                            JSONObject status = c.getJSONObject("status");
                            JSONObject northBound = status.getJSONObject("north_bound");
                            JSONObject southBound = status.getJSONObject("south_bound");
                            String northStatus = northBound.getString("status");
                            String southStatus = southBound.getString("status");
                            String northServiceRoadStatus = northBound.getString("service_road_status");





                            String newName = c.getString("name");
                            String last_update = c.getString("last_updated");

                            ChildItem child = new ChildItem();
                            child.title = newName;
                            child.updated = "Updated: "+last_update;
                            child.northStatus = northStatus;
                            child.southStatus = southStatus;

                            item.items.add(child);






                         /*   HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_AREA_ID, area_id);
                            map.put(TAG_AREA_NAME, name);

                            areaList.add(map);*/
                        }

                        items.add(item);

                        adapter = new ExampleAdapter(getActivity());
                        adapter.setData(items);

                        listView = (AnimatedExpandableListView) rootView.findViewById(R.id.list);
                        listView.setAdapter(adapter);

                        // In order to show animations, we need to use a custom click handler
                        // for our ExpandableListView.
                        listView.setOnGroupClickListener(new OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                // We call collapseGroupWithAnimation(int) and
                                // expandGroupWithAnimation(int) to animate group
                                // expansion/collapse.
                                if (listView.isGroupExpanded(groupPosition)) {
                                    listView.collapseGroupWithAnimation(groupPosition);
                                } else {
                                    listView.expandGroupWithAnimation(groupPosition);
                                }
                                return true;
                            }

                        });



                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Login Failed!", Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                // Posting parameters to login url
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+ token);
                //params.put("password", password);

                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

  private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
