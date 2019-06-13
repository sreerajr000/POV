package com.pazhankanjiz.pov.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pazhankanjiz.pov.adapter.NotificationAdapter;
import com.pazhankanjiz.pov.model.NotificationViewModel;
import com.pazhankanjiz.pov.R;

import java.util.Arrays;
import java.util.List;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;

    private RecyclerView recyclerView;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.notification_recycler_view);
        List<NotificationViewModel> notificationViewModelList = Arrays.asList(new NotificationViewModel(R.drawable.like_green_24dp, "You've got a message"),
                new NotificationViewModel(R.drawable.ic_dashboard_black_24dp, "Your dog ran away"));
        NotificationAdapter adapter = new NotificationAdapter(getContext(), notificationViewModelList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        // TODO: Use the ViewModel
    }

}
