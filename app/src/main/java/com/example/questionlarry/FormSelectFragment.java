package com.example.questionlarry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FormSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    ArrayList<Form> forms=new ArrayList<>();
    Spinner namespinner;
    TextView name,location,birthday,gender,hobbies,department,yearofstudy,expectations;
    ImageView profilepicture;
    Bitmap bitprofilepicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_form_select_form, container, false);
        loadData();

        namespinner=v.findViewById(R.id.selectname);
        namespinner.setOnItemSelectedListener(this);

        name=v.findViewById(R.id.resname);
        location=v.findViewById(R.id.reslocation);
        profilepicture=v.findViewById(R.id.resprofilepicture);
        birthday=v.findViewById(R.id.resbirthday);
        gender=v.findViewById(R.id.resgender);
        hobbies=v.findViewById(R.id.reshobbies);
        department=v.findViewById(R.id.resdepartment);
        yearofstudy=v.findViewById(R.id.resyearofstudy);
        expectations=v.findViewById(R.id.resexpectations);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        String selected= namespinner.getSelectedItem().toString();

        for(int i=0;i<forms.size();i++)
        {
            if(selected==forms.get(i).getName())
            {
                name.setText(forms.get(i).getName());
                location.setText(forms.get(i).getLocation());

                bitprofilepicture=stringToBitmap(forms.get(i).getProfilepicture());
                profilepicture.setImageBitmap(bitprofilepicture);
                birthday.setText(forms.get(i).getBirthday());
                gender.setText(forms.get(i).getGender());

                String hobbiesstr=String.join(", ",forms.get(i).getHobbies());
                hobbies.setText(hobbiesstr);

                department.setText(forms.get(i).getDepartment());
                yearofstudy.setText(forms.get(i).getYearofstudy());
                expectations.setText(forms.get(i).getExpectations());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void loadData()
    {
        FirebaseDatabase.getInstance().getReference().child("Forms").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    forms.add(snapshot.getValue(Form.class));
                }

                setNamesSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setNamesSpinner()
    {
        ArrayList<String> names=new ArrayList<String>();

        names.add("Select a name");

        for(int i=0;i<forms.size();i++)
        {
            names.add(forms.get(i).getName());
        }

        Spinner namespinner=getActivity().findViewById(R.id.selectname);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, names)
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
        namespinner.setAdapter(dataAdapter);
    }

    public final static Bitmap stringToBitmap(String in)
    {
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
