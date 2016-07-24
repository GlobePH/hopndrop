package dev.info.hopndrop;


import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlertsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public AlertsFragment(){}
    ArrayList<HashMap<String, String>> alertList;
    Intent intent;
    private SessionManager session;
    private SQLiteHandler db;
    ProgressDialog pDialog;

    private static final String TAG_AID ="aid";
    private static final String  TAG_ROUTE="route";
    private static final String TAG_ALTERNATE_ROUTE ="alternate_route";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_TYPE = "type";
    private static final String TAG_STATUS = "status";
    private static final String TAG_CREATED_AT = "created_at";

    String token;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alerts, container, false);
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");
        alertList = new ArrayList<HashMap<String, String>>();
        alertList.clear();
        getAlerts();


        return rootView;
    }



    private void getAlerts() {
        // Tag used to cancel the request
        String tag_string_req = "get_alerts";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Alerts ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_ALERTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Alert Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String  result = jObj.getString("alerts");

                    if (!result.equals("")) {
                        JSONArray alerts = jObj.getJSONArray("alerts");

                        for (int i = 0; i < alerts.length(); i++) {
                            JSONObject c = alerts.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString("id");
                            String route = c.getString("route");
                            String alternate_route = c.getString("alternative_route");
                            String description = c.getString("description");
                            String type = c.getString("type");
                            String status = c.getString("status");
                            String created_at = c.getString("created_at");

                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_AID, id);
                            map.put(TAG_ROUTE, route);
                            map.put(TAG_ALTERNATE_ROUTE, alternate_route);
                            map.put(TAG_DESCRIPTION, description);
                            map.put(TAG_TYPE, type);
                            map.put(TAG_STATUS, status);
                            map.put(TAG_CREATED_AT, created_at);
                            alertList.add(map);
                        }


                        initializeList();




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
                hideDialog();
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

    public void initializeList(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), alertList,
                        R.layout.list_alerts,
                        new String[] {TAG_AID,TAG_ROUTE,TAG_ALTERNATE_ROUTE,TAG_DESCRIPTION,TAG_TYPE,TAG_STATUS,TAG_CREATED_AT},
                        new int[]    { R.id.pid, R.id.route,  R.id.alternate_route,R.id.description,R.id.type,R.id.status,R.id.created_at});
                setListAdapter(adapter);
            }

        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}