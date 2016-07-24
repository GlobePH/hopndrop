package dev.info.hopndrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;


public class SettingsFragment extends Fragment {
    Intent intent;
    private SessionManager session;
    private SQLiteHandler db;
    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

       TextView name = (TextView) rootView.findViewById(R.id.name);
       TextView email = (TextView) rootView.findViewById(R.id.email);
       TextView contact_number = (TextView) rootView.findViewById(R.id.contact_number);
       TextView address = (TextView) rootView.findViewById(R.id.address);
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        name.setText(user.get("firstname")+" " +user.get("lastname"));
        email.setText(user.get("email"));
        contact_number.setText(user.get("contact_number"));
        address.setText(user.get("address"));

        rootView.findViewById(R.id.edit_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getActivity(), UpdateInfo.class);
                startActivity(intent);
                //finish();

            }
        });
        return rootView;
    }
}