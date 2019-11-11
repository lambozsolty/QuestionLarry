package com.example.questionlarry;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FormFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_form, container, false);

        setLocationSpinner(v);
        setDepartmentSpinner(v);

        return v;
    }

    public void setLocationSpinner(View v)
    {
        Spinner locationspinner=(Spinner) v.findViewById(R.id.location);

        List<String> locations = new ArrayList<String>();
        locations.add("Your city");
        locations.add("Marosvásárhely");
        locations.add("Szászrégen");
        locations.add("Székelyudvarhely");
        locations.add("Csíkszereda");
        locations.add("Sepsiszentgyörgy");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, locations)
        {
            @Override
            public boolean isEnabled(int position)
            {
                if(position == 0)
                {
                    return false;
                }

                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }

                return view;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        locationspinner.setAdapter(dataAdapter);
    }

    public void setDepartmentSpinner(View v)
    {
        Spinner departmentspinner=(Spinner) v.findViewById(R.id.department);

        List<String> departments = new ArrayList<String>();
        departments.add("Your department");
        departments.add("Agricultural Engineering");
        departments.add("Automation and applied informatics");
        departments.add("Communication and public relations");
        departments.add("Computer science");
        departments.add("Computer-aided operation planning");
        departments.add("Horticultural engineering");
        departments.add("Information science");
        departments.add("Landscape architecture");
        departments.add("Mechatronics");
        departments.add("Public Health Services and Policies");
        departments.add("Telecommunication");
        departments.add("Translation and interpreting studies");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, departments)
        {
            @Override
            public boolean isEnabled(int position)
            {
                if(position == 0)
                {
                    return false;
                }

                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }

                return view;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        departmentspinner.setAdapter(dataAdapter);
    }
}
