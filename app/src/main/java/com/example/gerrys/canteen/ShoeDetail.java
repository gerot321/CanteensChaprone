package com.example.gerrys.canteen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.gerrys.canteen.Database.Database;
import com.example.gerrys.canteen.Model.Order;
import com.example.gerrys.canteen.Model.Shoe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShoeDetail extends AppCompatActivity {

    TextView shoeName, shoePrice, shoeSize, shoeDescription;
    ImageView shoeImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String addr = " ";
    String productId = "";


    FirebaseDatabase database;
    DatabaseReference shoes,category;
    String merchantID;
    Shoe currentShoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_detail);

        // Firebase
        merchantID = getIntent().getStringExtra("Merchant");
        database = FirebaseDatabase.getInstance();
        shoes = database.getReference("Product");





        // Init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        category = database.getReference("Category").child(merchantID).child("origin");
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoeDetail.this);
                alertDialog.setTitle("One last step!");
                LayoutInflater layoutInflater =
                        (LayoutInflater)getBaseContext()
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                Context context = layoutInflater.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                TextView text = new TextView(ShoeDetail.this);
                text.setText("Destinasi Gedung");
                layout.addView(text);

                String[] Des = {"GedungA", "GedungB", "GedungC", "GedungD", "GedungE", "GedungF", "FJ", "DP"};

                ArrayAdapter < String > adapter = new ArrayAdapter<String>(ShoeDetail.this, android.R.layout.simple_spinner_item, Des);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner popupSpinner = new Spinner(ShoeDetail.this);
                popupSpinner.setAdapter(adapter);
                popupSpinner.setLayoutParams(lp);
                layout.addView(popupSpinner);

                TextView text2 = new TextView(ShoeDetail.this);
                text2.setText("Detail Alamat");
                layout.addView(text2);
                alertDialog.setMessage("Enter your shipping address: ");
                final EditText edtAddress = new EditText(ShoeDetail.this);

                edtAddress.setLayoutParams(lp);
                layout.addView(edtAddress);
                alertDialog.setView(layout);


                // Add edit text to alert dialog
                alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

                alertDialog.setPositiveButton("GO", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        category.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String origin = dataSnapshot.getValue().toString();
                                addr = popupSpinner.getSelectedItem().toString()+edtAddress.getText().toString();
                                String gedung = popupSpinner.getSelectedItem().toString();
                                int total=0;

                                if (origin.equals("FJ") && gedung.equals("GedungA")) {
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+2000;
                                }
                                else if (origin.equals("FJ") && gedung.equals("GedungB")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+2000;

                                } else if (origin.equals("FJ") && gedung.equals("GedungC")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+2000;

                                } else if (origin.equals("FJ") && gedung.equals("GedungD")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+4000;

                                } else if (origin.equals("FJ") && gedung.equals("GedungE")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+5000;

                                } else if (origin.equals("FJ") && gedung.equals("GedungF")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+5000;

                                } else if (origin.equals("FJ") && gedung.equals("DP")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+3000;

                                } else if (origin.equals("FJ") && gedung.equals("FJ")){
                                    total = (Integer.parseInt(currentShoe.getPrice())*Integer.parseInt(numberButton.getNumber()))+1000;

                                }
                                Log.d("asdasdasdasd",String.valueOf(currentShoe.getPrice()) );

                                new Database(getBaseContext()).addToCart(new Order(
                                        productId,
                                        currentShoe.getName(),
                                        numberButton.getNumber(),
                                        String.valueOf(total),
                                        addr
                                ));

                                //category.child("status").setValue("Waiting Admin Confirmation");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });

                        Toast.makeText(ShoeDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.show();




            }

        });

        shoeDescription = (TextView)findViewById(R.id.shoe_description);
        shoeName = (TextView)findViewById(R.id.shoe_name);
        shoePrice = (TextView)findViewById(R.id.shoe_price);
        shoeSize = (TextView)findViewById(R.id.shoe_size);

        shoeImage = (ImageView)findViewById(R.id.img_shoe);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            productId = getIntent().getStringExtra("ShoeId");


        if (!productId.isEmpty()){
            getDetailShoe(productId);
        }


    }

    private void getDetailShoe(String shoeId) {
        shoes.child(shoeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentShoe = dataSnapshot.getValue(Shoe.class);

                // Set image
                Picasso.with(getBaseContext()).load(currentShoe.getImage())
                        .into(shoeImage);

                collapsingToolbarLayout.setTitle(currentShoe.getName());

                shoeSize.setText(currentShoe.getStock());

                shoeName.setText(currentShoe.getName());

                shoePrice.setText(currentShoe.getPrice());

                shoeDescription.setText(currentShoe.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
    }
