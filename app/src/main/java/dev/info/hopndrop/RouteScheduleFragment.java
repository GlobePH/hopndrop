package dev.info.hopndrop;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.Map;
import android.widget.AdapterView.OnItemClickListener;

public class RouteScheduleFragment extends ListFragment implements OnItemClickListener {

    public RouteScheduleFragment() {
    }

    ArrayList<HashMap<String, String>> busList;
    Intent intent;
    private SessionManager session;
    private SQLiteHandler db;
    ProgressDialog pDialog;
    String token;
    private static final String TAG_ID = "id";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_BUS_NO = "bus_no";
    private static final String TAG_DEPARTURE_TIME = "departure_time";
    private static final String TAG_START = "start_name";
    private static final String TAG_END= "end_name";



    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_route_schedule, container, false);
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");
        busList = new ArrayList<HashMap<String, String>>();
        busList.clear();
        getBus();


        return rootView;
    }

    private void getBus() {
        // Tag used to cancel the request
        String tag_string_req = "get_bus";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Buss ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_BUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Bus Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String result = jObj.getString("buses");

                    if (!result.equals("")) {
                        JSONArray bus = jObj.getJSONArray("buses");

                        for (int i = 0; i < bus.length(); i++) {
                            JSONObject c = bus.getJSONObject(i);

                            // Storing each json item in variable



                            String user_id = c.getString("user_id");
                            String bus_no = c.getString("bus_no");
                            String departure_time = c.getString("departure_time");
                            String id = c.getString("id");
                            JSONObject routes = c.getJSONObject("routes");
                            String start= routes.getString("start_name");
                            String end= routes.getString("end_name");
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID, id);
                            map.put(TAG_START, start);
                            map.put(TAG_END, end);
                            map.put(TAG_USER_ID, user_id);
                            map.put(TAG_BUS_NO, bus_no);
                            map.put(TAG_DEPARTURE_TIME, departure_time);

                            busList.add(map);
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
                headers.put("Authorization", "Bearer " + token);
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

    public void initializeList() {



        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), busList,
                        R.layout.list_bus,
                        new String[]{TAG_ID, TAG_START+"-"+TAG_END, TAG_USER_ID, TAG_BUS_NO, TAG_DEPARTURE_TIME},
                        new    int[]{R.id.pid, R.id.route, R.id.user_id, R.id.bus_no, R.id.departure_time});
                setListAdapter(adapter);
            }

        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView txt_aid= ((TextView) view.findViewById(R.id.pid));
        String api_id = txt_aid.getText().toString();
        session.setApi_id(api_id);
        Intent intent = new Intent(getActivity(),ViewBusRoutes.class);
        //intent.putExtras("id", api_id);
        startActivity(intent);

      //  Toast.makeText(getActivity().getApplicationContext(), "id: " + api_id, Toast.LENGTH_LONG).show();
    }
}
