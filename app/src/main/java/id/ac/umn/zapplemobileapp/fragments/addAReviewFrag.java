package id.ac.umn.zapplemobileapp.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.ac.umn.zapplemobileapp.ContentActivity;
import id.ac.umn.zapplemobileapp.R;

public class addAReviewFrag extends Fragment {
    ConstraintLayout clUploadImage;
    TextView tvAddPicture;

//    private AddAReviewViewModel mViewModel;

    public static addAReviewFrag newInstance() {
        return new addAReviewFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_a_review, container, false);

        ContentActivity contentActivity = (ContentActivity) getActivity();
        contentActivity.pageTitle.setText("Add a Review");

//        Bundle restaurantData = new Bundle();
//        Integer restoId = getArguments().getInt("restaurantId");

//        System.out.println("RESTAURANT ID"+restoId);

        tvAddPicture = view.findViewById(R.id.tvAddPict);
//        tvAddPicture.setText(restoId);


        clUploadImage = view.findViewById(R.id.reviewImage);
        clUploadImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Upload Image
//                clUploadImage.setBackgroundColor(-NEW PICTURE-);
                tvAddPicture.setVisibility(View.GONE);
            }
        });


        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(AddAReviewViewModel.class);
//        // TODO: Use the ViewModel
//    }

}