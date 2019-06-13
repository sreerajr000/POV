package com.pazhankanjiz.pov.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouattararomuald.slider.ImageSlider;
import com.ouattararomuald.slider.SliderAdapter;
import com.ouattararomuald.slider.loaders.glide.GlideImageLoaderFactory;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.adapter.SearchTrendingAdapter;
import com.pazhankanjiz.pov.model.SearchResultsModel;
import com.pazhankanjiz.pov.model.SearchTrendingItemModel;
import com.pazhankanjiz.pov.model.SearchTrendingModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchResultsModel mViewModel;

    private ImageSlider imageSlider;

    private RecyclerView searchRecyclerView;
    private RecyclerView searchTrendingRecyclerView;

    private List<SearchTrendingModel> searchTrendingModels;

    private List<SearchResultsModel> searchResultsModelList;
    private List<String> imageUrls = Arrays.asList("https://source.unsplash.com/Xq1ntWruZQI/600x800",
            "https://source.unsplash.com/NYyCqdBOKwc/600x800",
            "https://source.unsplash.com/buF62ewDLcQ/600x800");

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageSlider = view.findViewById(R.id.search_image_slider);
        imageSlider.setAdapter(new SliderAdapter(
                getContext(),
                new GlideImageLoaderFactory(),
                imageUrls,
                imageUrls
        ));

        searchTrendingRecyclerView = view.findViewById(R.id.search_trending_recycler_view);
        searchTrendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        searchTrendingModels = new ArrayList<>();

        List<SearchTrendingItemModel> modelList = Arrays.asList(new SearchTrendingItemModel("https://source.unsplash.com/Xq1ntWruZQI/600x800","Yasaka Shrine"),
                new SearchTrendingItemModel("https://source.unsplash.com/NYyCqdBOKwc/600x800", "Fushimi Inari Shrine"),
                new SearchTrendingItemModel("https://source.unsplash.com/buF62ewDLcQ/600x800", "Bamboo Forest"),
                new SearchTrendingItemModel("https://source.unsplash.com/THozNzxEP3g/600x800", "Brooklyn Bridge"));
        searchTrendingModels.add(new SearchTrendingModel("abc_tag", modelList));
        searchTrendingModels.add(new SearchTrendingModel("bcd_tag", modelList));

        SearchTrendingAdapter searchTrendingAdapter = new SearchTrendingAdapter(getContext(), searchTrendingModels);

        //setting adapter to recyclerview
        searchTrendingRecyclerView.setAdapter(searchTrendingAdapter);

    /*
        searchRecyclerView = view.findViewById(R.id.search_recycler_view);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        searchResultsModelList = new ArrayList<>();


        //adding some items to our list
        searchResultsModelList.add(
                new SearchResultsModel(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.like_green_24dp));

        searchResultsModelList.add(
                new SearchResultsModel(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        4.3,
                        60000,
                        R.drawable.like_green_24dp));

        searchResultsModelList.add(
                new SearchResultsModel(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.like_green_24dp));

        //creating recyclerview adapter
        SearchResultsAdapter adapter = new SearchResultsAdapter(getContext(), searchResultsModelList);

        //setting adapter to recyclerview
        searchRecyclerView.setAdapter(adapter);
        */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(SearchResultsModel.class);
        // TODO: Use the ViewModel
    }

}
