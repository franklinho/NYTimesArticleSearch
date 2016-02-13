package com.franklinho.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.franklinho.nytimessearch.activities.SearchActivity;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {

    public static DatePickerFragment newInstance(String dateType) {

        Bundle args = new Bundle();

        DatePickerFragment fragment = new DatePickerFragment();
        args.putString("dateType", dateType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 1;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        SearchActivity searchActivity = (SearchActivity) getActivity();
        searchActivity.dateType = getArguments().getString("dateType");
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }


}
