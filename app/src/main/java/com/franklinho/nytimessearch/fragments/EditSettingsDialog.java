package com.franklinho.nytimessearch.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.franklinho.nytimessearch.R;
import com.franklinho.nytimessearch.activities.SearchActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by franklinho on 2/9/16.
 */

//This dialog shows the filters available

public class EditSettingsDialog extends DialogFragment {
    @Bind(R.id.etBeginDate) EditText etBeginDate;
    @Bind(R.id.etEndDate) EditText etEndDate;
    @Bind(R.id.spnSortOrder) Spinner spnSortOrder;
    @Bind(R.id.cbArts) CheckBox cbArts;
    @Bind(R.id.cbFashion) CheckBox cbFashion;
    @Bind(R.id.cbSports) CheckBox cbSports;
    @Bind(R.id.btnSave) Button btnSave;
    @Bind(R.id.btnBeginDateClear) ImageButton btnBeginDateClear;
    @Bind(R.id.btnEndDateClear) ImageButton btnEndDateClear;

    public EditSettingsDialog() {

    }

    public static EditSettingsDialog newInstance() {

        Bundle args = new Bundle();

        EditSettingsDialog fragment = new EditSettingsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Settings");
        View view = inflater.inflate(R.layout.fragment_edit_settings,container, false);
        ButterKnife.bind(this, view);






        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("beginDate");


            }
        });


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker("endDate");

            }
        });

        //Initialize shared preferences
        SharedPreferences preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        etBeginDate.setText(preferences.getString("beginDate","MM/DD/YYYY"));
        etEndDate.setText(preferences.getString("endDate", "MM/DD/YYYY"));
        spnSortOrder.setSelection(preferences.getInt("newest", 0));
        cbArts.setChecked(preferences.getBoolean("arts", false));
        cbFashion.setChecked(preferences.getBoolean("fashion", false));
        cbSports.setChecked(preferences.getBoolean("sports", false));


        //save filter settings to shared preferences
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("beginDate", etBeginDate.getText().toString());
                editor.putString("endDate", etEndDate.getText().toString());
                editor.putInt("newest", spnSortOrder.getSelectedItemPosition());
                editor.putBoolean("arts", cbArts.isChecked());
                editor.putBoolean("fashion", cbFashion.isChecked());
                editor.putBoolean("sports", cbSports.isChecked());
                editor.commit();

                dismiss();
            }
        });

        etBeginDate.clearFocus();
        return view;
    }

    public void showDatePicker(String dateType) {
        DatePickerFragment newFragment = new DatePickerFragment().newInstance(dateType);
        newFragment.show(getChildFragmentManager(),"datePicker");
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SearchActivity searchActivity = (SearchActivity) getActivity();
        searchActivity.requestArticles(0, searchActivity.sharedQuery);
    }

    public void setBeginDate(String string) {
        etBeginDate.setText(string);
        etBeginDate.clearFocus();

    }

    public void setEndDate(String string) {
        etEndDate.setText(string);
        etEndDate.clearFocus();

    }


}
