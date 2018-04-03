package com.example.gerrys.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gerrys.canteen.Interface.ItemClickListener;
import com.example.gerrys.canteen.Model.Category;
import com.example.gerrys.canteen.Model.User;
import com.example.gerrys.canteen.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category,user;

    TextView txtFullName,saldo;
    String ID;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Brand");
        ID= getIntent().getStringExtra("phoneId");
        setSupportActionBar(toolbar);

        // Init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        user = database.getReference("User");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                intent.putExtra("userID", ID );
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set who is logged in
        View headerView = navigationView.getHeaderView(0);

        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,UserProfile.class);
                i.putExtra("phoneId",ID);
                startActivity(i);
            }
        });

        saldo = headerView.findViewById(R.id.txtSaldo);
        user.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                saldo.setText("Saldo " + users.getSaldo().toString());
                txtFullName.setText(users.getName().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Load menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
    }

    private void loadMenu() {

         adapter = new FirebaseRecyclerAdapter<Category,
                     MenuViewHolder>(Category.class,
             R.layout.menu_item,
             MenuViewHolder.class,
             category) {
                 @Override
                 protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                     viewHolder.txtMenuName.setText(model.getName());
                     Picasso.with(getBaseContext()).load(model.getImage())
                             .into(viewHolder.imageView);

                     final Category clickItem = model;

                     viewHolder.setItemClickListener(new ItemClickListener() {
                         @Override
                         public void onClick(View view, int position, boolean isLongCLick) {
                             // Get categoryId and send to the new activity
                             Intent intent = new Intent(Home.this, ShoeList.class);
                             // get the key of this item
                             intent.putExtra("CategoryId", adapter.getRef(position).getKey());

                             startActivity(intent);
                         }
                     });
                 }
        };

        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter = new FirebaseRecyclerAdapter<Category,
                        MenuViewHolder>(Category.class,
                        R.layout.menu_item,
                        MenuViewHolder.class,
                        category) {
                    @Override
                    protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                        viewHolder.txtMenuName.setText(model.getName());
                        Picasso.with(getBaseContext()).load(model.getImage())
                                .into(viewHolder.imageView);

                        final Category clickItem = model;

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongCLick) {
                                // Get categoryId and send to the new activity
                                Intent intent = new Intent(Home.this, ShoeList.class);
                                // get the key of this item
                                intent.putExtra("CategoryId", adapter.getRef(position).getKey());

                                startActivity(intent);
                            }
                        });
                    }
                };

                recycler_menu.setAdapter(adapter);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter = new FirebaseRecyclerAdapter<Category,
                        MenuViewHolder>(Category.class,
                        R.layout.menu_item,
                        MenuViewHolder.class,
                        category.orderByChild("name").startAt(query).endAt(query+"\uf8ff")) {
                    @Override
                    protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                        viewHolder.txtMenuName.setText(model.getName());
                        Picasso.with(getBaseContext()).load(model.getImage())
                                .into(viewHolder.imageView);

                        final Category clickItem = model;

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongCLick) {
                                // Get categoryId and send to the new activity
                                Intent intent = new Intent(Home.this, ShoeList.class);
                                // get the key of this item
                                intent.putExtra("CategoryId", adapter.getRef(position).getKey());

                                startActivity(intent);
                            }
                        });
                    }
                };

                recycler_menu.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter = new FirebaseRecyclerAdapter<Category,
                        MenuViewHolder>(Category.class,
                        R.layout.menu_item,
                        MenuViewHolder.class,
                        category.orderByChild("name").startAt(newText).endAt(newText+"\uf8ff")) {
                    @Override
                    protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                        viewHolder.txtMenuName.setText(model.getName());
                        Picasso.with(getBaseContext()).load(model.getImage())
                                .into(viewHolder.imageView);

                        final Category clickItem = model;

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongCLick) {
                                // Get categoryId and send to the new activity
                                Intent intent = new Intent(Home.this, ShoeList.class);
                                // get the key of this item
                                intent.putExtra("CategoryId", adapter.getRef(position).getKey());

                                startActivity(intent);
                            }
                        });
                    }
                };

                recycler_menu.setAdapter(adapter);
                return false;
            }
        });
       /* int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        int closeButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButtonImage = (ImageView) searchView.findViewById(closeButtonId);
        closeButtonImage.setImageResource(R.drawable.ic_clear_black_24dp);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor(Color.WHITE);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
                searchText.setTextColor(Color.BLACK);
                searchText.setHintTextColor(Color.BLACK);
            }
        }*/
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(Home.this, Cart.class);
            intent.putExtra("userID", ID );
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(Home.this, OrderStatus.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            // Log off
            Intent intent = new Intent(Home.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (id == R.id.nav_Confirmation) {
            Intent intent = new Intent(Home.this, ConfirmationSection.class);
            intent.putExtra("userID", ID );
            startActivity(intent);
        }
        else if (id == R.id.nav_top_up) {
            Intent intent = new Intent(Home.this, TopUpActivity.class);
            intent.putExtra("userID", ID );
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qr:
                Intent i = new Intent(this,ScanActivity.class);
                this.startActivity(i);
                return true;
            case R.id.cart:
                Intent ia = new Intent(this,Cart.class);
                ia.putExtra("userID", ID );
                this.startActivity(ia);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
