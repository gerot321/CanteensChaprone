package com.example.gerrys.canteen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerrys.canteen.Model.Voucher;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TopUpActivity extends AppCompatActivity {
    String userID=" ";
    Button scanbtn;
    TextView result;
    FirebaseDatabase database;
    DatabaseReference user;
    DatabaseReference voucher;
    Toolbar mToolbar;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        database = FirebaseDatabase.getInstance();
        voucher = database.getReference("Voucher");
        user = database.getReference("User");
        userID = getIntent().getStringExtra("userID");
        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Top up saldo");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

       mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                finish();
           }
       });

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopUpActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopUpActivity.this);
                        alertDialog.setTitle("TopUp Saldo Confirmation");

                        LayoutInflater layoutInflater =
                                (LayoutInflater)getBaseContext()
                                        .getSystemService(LAYOUT_INFLATER_SERVICE);

                        Context context = layoutInflater.getContext();
                        LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final TextView text = new TextView(TopUpActivity.this);

                        text.setGravity(Gravity.CENTER);
                        layout.addView(text);
                        voucher.child(barcode.displayValue.toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Voucher vocer = dataSnapshot.getValue(Voucher.class);
                                text.setText("Voucher Amount : "+vocer.getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                        alertDialog.setView(layout);


                        // Add edit text to alert dialog
                        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

                        alertDialog.setPositiveButton("Isi", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                voucher.child(barcode.displayValue.toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Voucher vocer = dataSnapshot.getValue(Voucher.class);
                                        if(vocer.getStatus().equals("valid")){
                                            Toast.makeText(TopUpActivity.this, "saldo telah ditambahkan"+vocer.getValue().toString(), Toast.LENGTH_SHORT).show();
                                            user.child(userID).child("saldo").setValue(vocer.getValue().toString());

                                            result.setText(vocer.getValue().toString());

                                        }
                                        else{
                                            Toast.makeText(TopUpActivity.this, "Kode voucher tidak sesuai atau sudah di topup", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }
                        });

                        alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });

                        alertDialog.show();


                    }
                });
            }
        }
    }
}
