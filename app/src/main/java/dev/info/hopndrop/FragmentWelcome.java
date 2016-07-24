package dev.info.hopndrop;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;


public class FragmentWelcome extends Fragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    View rootView;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    TextView dateButton, timeButton;
    EditText start, end;
    public FragmentWelcome(){}
String filter;

    private SessionManager session;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        //db = new SQLiteHandler(this.getApplicationContext());

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());
         start =(EditText) rootView.findViewById(R.id.start);
         end =(EditText) rootView.findViewById(R.id.end);
         //dateButton =(TextView)rootView.findViewById(R.id.dateButton);
         timeButton =(TextView)rootView.findViewById(R.id.timeButton);
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        rootView.findViewById(R.id.checklist).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Sort Result by:")
                        .items(R.array.items)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                 /* If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 */

                                return true;
                            }
                        })
                        .positiveText("choose")
                        .show();
            }
        });
   /*     rootView.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                datePickerDialog.setYearRange(2000, 2028);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
*/
        rootView.findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }


        rootView.findViewById(R.id.btnViewSearch).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String origin = start.getText().toString();
                String destination = end.getText().toString();
                String time = timeButton.getText().toString();



                if (origin.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter origin address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (destination.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter destination address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                session.setOrigin(origin);
                session.setDestination(destination);
                session.setTime(time);
                Intent intent = new Intent(getActivity(), ViewSearched.class);
                startActivity(intent);

            }
        });


        return rootView;
    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //Toast.makeText(FragmentWelcome.this.getActivity(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();


        dateButton.setText(day+"-"+month+"-"+year);


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

        timeButton.setText(hourOfDay+":"+minute);

        //Toast.makeText(FragmentWelcome.this.getActivity(), "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }

}
