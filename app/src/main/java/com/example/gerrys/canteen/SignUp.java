package com.example.gerrys.canteen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gerrys.canteen.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    MaterialEditText etPhone, etName, etPassword,etDate,etAddress;
    RadioButton radio;
    RadioGroup groups;
    Button btnSignUp;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhone = (MaterialEditText)findViewById(R.id.etPhone);
        etName = (MaterialEditText)findViewById(R.id.etName);
        etPassword = (MaterialEditText)findViewById(R.id.etPassword);
        etDate= (MaterialEditText)findViewById(R.id.etTanngal);
        etAddress =(MaterialEditText)findViewById(R.id.etAddress);
        radio= (RadioButton)findViewById(R.id.radioButton);
        groups =(RadioGroup)findViewById(R.id.radioGroup);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        // I
        // nitialize firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDateDialog();
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("loading...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if already user phone
                        if(dataSnapshot.child(etPhone.getText().toString()).exists()){
                            mDialog.dismiss();

                            Toast.makeText(SignUp.this, "Account already exist!", Toast.LENGTH_SHORT).show();
                        }else {
                            mDialog.dismiss();
                            int select = groups.getCheckedRadioButtonId();
                            radio= (RadioButton)findViewById(select);
                            User user = new User(etName.getText().toString(), etPassword.getText().toString(), "Costumer","0",etAddress.getText().toString(),radio.getText().toString(),
                                    etDate.getText().toString()," "," "," ");
                            table_user.child(etPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Account successfully created!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
}
