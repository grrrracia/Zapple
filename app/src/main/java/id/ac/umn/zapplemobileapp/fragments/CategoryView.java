package id.ac.umn.zapplemobileapp.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.ac.umn.zapplemobileapp.R;

public class CategoryView extends Fragment {
    TextView tvCategory;

    private CategoryViewViewModel mViewModel;

    public static CategoryView newInstance() {
        return new CategoryView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_view_fragment, container, false);

        Intent intent;

//        getCategoryIntent();

//        tvCategory = view.findViewById(R.id.tvCategory);
//        tvCategory.setText();
        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(CategoryViewViewModel.class);
//        // TODO: Use the ViewModel
//    }

}