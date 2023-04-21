package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bookapp.adapters.AdapterPdfAdmin;
import com.example.bookapp.databinding.ActivityPdfAddBinding;
import com.example.bookapp.databinding.ActivityPdfListAdminBinding;
import com.example.bookapp.models.ModelPdf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfListAdminActivity extends AppCompatActivity {
    //viewbinding
    private ActivityPdfListAdminBinding binding;

    //arraylist to hold  list of data of type ModelPdf
    private ArrayList<ModelPdf> pdfArrayList;

    //adapter
    private AdapterPdfAdmin adapterPdfAdmin;

    private String categoryId, categoryTitle;
    private static final String TAG = "PDF_LIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get data form intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        //set pdf category
        binding.subTitleTv.setText(categoryTitle);

        loadPdfList();

        //handle click goto previous activity
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadPdfList() {
        //init list before adding data
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Book");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            // get data
                            ModelPdf model = ds.getValue(ModelPdf.class);
                            //add to list
                            if(model != null){
                                pdfArrayList.add(model);
                                Log.d(TAG, "onDataChange: " + model.getId() + " " + model.getTitle());
                            }else {
                                Log.d(TAG, "No product: ");
                            }
                        }
                        //setup adapter
                        adapterPdfAdmin = new AdapterPdfAdmin(PdfListAdminActivity.this, pdfArrayList);
                        binding.bookRv.setAdapter(adapterPdfAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}