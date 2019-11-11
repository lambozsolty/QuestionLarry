package com.example.questionlarry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    public static final int PICK_IMAGE = 1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainactivity, new FormFragment()).commit();
        }
    }

    public void selectPhoto(View v)
    {
        Intent togallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(togallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            Uri image = data.getData();

            ImageView imageView = (ImageView) findViewById(R.id.profilepicture);
            imageView.setImageURI(image);
        }
    }

    public void onClickDate(View v)
    {
        showDatePickerDialog();
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        month++;
        String date=year+". "+month+". "+dayOfMonth+".";

        TextView bday=(TextView) findViewById(R.id.yourbirthday);
        bday.setText(date);
    }

    public  void sendForm(View v)
    {
        TextView name= findViewById(R.id.name);
        Spinner location=findViewById(R.id.location);
        ImageView profilepicture=findViewById(R.id.profilepicture);
        TextView birthday=findViewById(R.id.yourbirthday);
        RadioGroup gender= findViewById(R.id.gender);
        CheckBox cycling=findViewById(R.id.cycling);
        CheckBox gaming=findViewById(R.id.gaming);
        CheckBox traveling=findViewById(R.id.traveling);
        Spinner department=findViewById(R.id.department);
        RadioGroup yearofstudy=findViewById(R.id.yearsofstudy);
        TextView answer=findViewById(R.id.answer);

        if(name.getText().toString().matches(""))
        {
            Toast.makeText(this,"You should write your name!",Toast.LENGTH_SHORT).show();
        }
        else if(location.getSelectedItem().toString().matches("Your city"))
        {
            Toast.makeText(this,"You should select a location!",Toast.LENGTH_SHORT).show();
        }
        else if(profilepicture.getDrawable()==null)
        {
            Toast.makeText(this,"You should pick a profile picture!",Toast.LENGTH_SHORT).show();
        }
        else if(birthday.getText().toString().matches("Your birthday"))
        {
            Toast.makeText(this,"You should select your birthday!",Toast.LENGTH_SHORT).show();
        }
        else if(gender.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this,"You should select your gender!",Toast.LENGTH_SHORT).show();
        }
        else if(!cycling.isChecked() && !gaming.isChecked() && !traveling.isChecked())
        {
            Toast.makeText(this,"You should select at least one hobby!",Toast.LENGTH_SHORT).show();
        }
        else if(department.getSelectedItem().toString().matches("Your department"))
        {
            Toast.makeText(this,"You should select your department!",Toast.LENGTH_SHORT).show();
        }
        else if(yearofstudy.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this,"You should select your current year of study!",Toast.LENGTH_SHORT).show();
        }
        else if(answer.getText().toString().matches(""))
        {
            Toast.makeText(this,"You should write some expectations!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Form userform=new Form();

            userform.setName(name.getText().toString());
            userform.setLocation(location.getSelectedItem().toString());

            BitmapDrawable drawable=(BitmapDrawable) profilepicture.getDrawable();
            Bitmap uploadimage=drawable.getBitmap();
            userform.setProfilepicture(bitmapToString(uploadimage));

            userform.setBirthday(birthday.getText().toString());

            int selectedgenderid=gender.getCheckedRadioButtonId();
            RadioButton genderbutton=findViewById(selectedgenderid);
            userform.setGender(genderbutton.getText().toString());

            ArrayList<String> hobbies=new ArrayList<String>();

            if(cycling.isChecked())
            {
                hobbies.add(cycling.getText().toString());
            }

            if(gaming.isChecked())
            {
                hobbies.add(gaming.getText().toString());
            }

            if(traveling.isChecked())
            {
                hobbies.add(traveling.getText().toString());
            }

            userform.setHobbies(hobbies);
            userform.setDepartment(department.getSelectedItem().toString());

            int selectedyearofstudy=yearofstudy.getCheckedRadioButtonId();
            RadioButton yearofstudybutton=findViewById(selectedyearofstudy);
            userform.setYearofstudy(yearofstudybutton.getText().toString());

            userform.setExpectations(answer.getText().toString());

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Forms");
            mDatabase.push().setValue(userform);

            Button selectform=findViewById(R.id.selectform);
            selectform.setVisibility(View.VISIBLE);

            Toast.makeText(this,"Form saved!",Toast.LENGTH_SHORT).show();
        }
    }

    public final static String bitmapToString(Bitmap in)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }

    public void viewForms(View v)
    {
        Fragment formselectfragment = new FormSelectFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.mainactivity, formselectfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
