package com.franklinho.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.franklinho.nytimessearch.activities.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public String dateType;

    public static DatePickerFragment newInstance(String dateType) {

        Bundle args = new Bundle();

        DatePickerFragment fragment = new DatePickerFragment();
        args.putString("dateType", dateType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        EditSettingsDialog editSettingsDialog = (EditSettingsDialog) getTargetFragment();

        if (dateType.equals("beginDate")){
            editSettingsDialog.setBeginDate(format.format(c.getTime()));
        } else if (dateType.equals("endDate")) {
            editSettingsDialog.setEndDate(format.format(c.getTime()));
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        SearchActivity searchActivity = (SearchActivity) getActivity();
        searchActivity.dateType = getArguments().getString("dateType");
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }


}
