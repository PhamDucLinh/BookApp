package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookapp.adapters.AdapterCategory;
import com.example.bookapp.databinding.ActivityDashboardAdminBinding;
import com.example.bookapp.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardAdminBinding binding;

    //xac thuc firebase
    private FirebaseAuth firebaseAuth;

    //tao mang de luu category
    private ArrayList<ModelCategory> categoryArrayList;
    private AdapterCategory adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        chekUser();
        loadCategories();
        // xu li click log out
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                chekUser();


            }

            private void loadCategories() {

                categoryArrayList = new ArrayList<>();

                // lay all data tu firebase den categories
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Categories");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        //clear data trc khi add
                        categoryArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            //add data
                            ModelCategory model = ds.getValue(ModelCategory.class);
                            //add vo mang
                            categoryArrayList.add(model);
                        }
                        //setup adapter
                        adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categoryArrayList);
                        //set adapter vo recycleview
                        binding.categoriesRv.setAdapter(adapterCategory);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        //xu li click add category
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
            }
        });

        //xu li click  add pdf
        binding.addPdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdminActivity.this, PdfAddActivity.class));
            }
        });
    }

    private void loadCategories() {

        categoryArrayList = new ArrayList<>();

        // lay all data tu firebase den categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                //clear data trc khi add
                categoryArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    //add data
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    //add vo mang
                    categoryArrayList.add(model);
                }
                //setup adapter
                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categoryArrayList);
                //set adapter vo recycleview
                binding.categoriesRv.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void chekUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            //login thanh cong lay thong tin user
            String email = firebaseUser.getEmail();
            //set in text view  of toolbar
            binding.subTitleTv.setText(email);
        }
    }
}