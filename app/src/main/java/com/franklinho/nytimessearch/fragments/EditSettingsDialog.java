package com.franklinho.nytimessearch.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.franklinho.nytimessearch.R;
import com.franklinho.nytimessearch.activities.SearchActivity;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by franklinho on 2/9/16.
 */

//This dialog shows the filters available
public class EditSettingsDialog extends DialogFragment{
    @Bind(R.id.etBeginDate) EditText etBeginDate;
    @Bind(R.id.etEndDate) EditText etEndDate;
    @Bind(R.id.spnSortOrder) Spinner spnSortOrder;
    @Bind(R.id.cbArts) CheckBox cbArts;
    @Bind(R.id.cbFashion) CheckBox cbFashion;
    @Bind(R.id.cbSports) CheckBox cbSports;
    @Bind(R.id.btnSave) Button btnSave;

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

        //Text validation for begin date editText field
        TextWatcher twBeginDate = new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + mmddyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etBeginDate.setText(current);
                    etBeginDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //Text validation for end date editText field
        TextWatcher twEndDate = new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + mmddyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etEndDate.setText(current);
                    etEndDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etBeginDate.addTextChangedListener(twBeginDate);
        etEndDate.addTextChangedListener(twEndDate);


        //Initialize shared preferences
        SharedPreferences preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        etBeginDate.setText(preferences.getString("beginDate","MM/DD/YYYY"));
        etEndDate.setText(preferences.getString("endDate","MM/DD/YYYY"));
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

        return view;
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

}
