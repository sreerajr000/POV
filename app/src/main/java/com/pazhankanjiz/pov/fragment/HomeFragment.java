package com.pazhankanjiz.pov.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pazhankanjiz.pov.activity.MainActivity;
import com.pazhankanjiz.pov.activity.UserCreationActivity;
import com.pazhankanjiz.pov.adapter.CardStackAdapter;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.adapter.HomeAnswerAdapter;
import com.pazhankanjiz.pov.constant.DatabaseConstants;
import com.pazhankanjiz.pov.custom.DatabaseHandler;
import com.pazhankanjiz.pov.model.AnswerModel;
import com.pazhankanjiz.pov.model.HomeCardModel;
import com.pazhankanjiz.pov.util.ParseUtils;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.pazhankanjiz.pov.constant.DatabaseConstants.CLASS_QUESTION;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.CREATED_AT;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.DISLIKE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.HASHTAG;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.LIKE;
import static java.util.Collections.addAll;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements CardStackListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "HomeFragment";

    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseHandler databaseHandler;

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private OnFragmentInteractionListener mListener;

    private RecyclerView answersView;

    private View homeHeader, buttonContainer;

    private TextView following, forYou;

    private boolean flipped = false;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;

    private static int selected = 0;

    private static final int FOR_YOU = 0;
    private static final int FOLLOWING = 1;

    private boolean share = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardStackView = Objects.requireNonNull(getView()).findViewById(R.id.card_stack_view);


        forYou = view.findViewById(R.id.for_you_textview);
        following = view.findViewById(R.id.following_textview);

        homeHeader = view.findViewById(R.id.home_header);
        buttonContainer = view.findViewById(R.id.button_container);

        setupButton(view);

        updateSelected(false);
        if (null != progressDialog)
            progressDialog.dismiss();


        cardStackView.setLayoutManager(manager);

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(15.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        manager = new CardStackLayoutManager(getActivity(), this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private HomeCardModel createSpot() {
        return new HomeCardModel(
                "Yasaka Shrine",
                "Kyoto",
                "https://source.unsplash.com/Xq1ntWruZQI/600x800"
        );
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        Log.d(TAG, "onCardDragging: ");
        if (direction.equals(Direction.Top) && ratio > 0.6f) {
            this.share = true;
            CardShareHandler cardShareHandler = new CardShareHandler();
            cardShareHandler.execute();
        }
    }


    @Override
    public void onCardSwiped(Direction direction) {
        int position = ((CardStackLayoutManager) Objects.requireNonNull(cardStackView.getLayoutManager())).getTopPosition() - 1;
        HomeCardModel model = ((CardStackAdapter) cardStackView.getAdapter()).getHomeCardModels().get(position);

        CardSwipeHandler cardSwipeHandler = new CardSwipeHandler();
        cardSwipeHandler.execute(direction, position, model);
    }

    @Override
    public void onCardRewound() {
        Log.d(TAG, "onCardRewound: ");
    }

    @Override
    public void onCardCanceled() {
        Log.d(TAG, "onCardCanceled: ");
        if (this.share) {

        }
    }

    @Override
    public void onCardAppeared(View view, int position) {
        flipped = false;
        if (position == 0 || (position + 1) % 10 != 0) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_QUESTION);
        query.setSkip(currentPage++ * PAGE_SIZE);
        query.setLimit(PAGE_SIZE);
        if (selected == FOLLOWING) {
            query.whereContainedIn(HASHTAG, Arrays.asList("3dart"));
        }
        query.orderByDescending(CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (null != e) {
                    e.printStackTrace();
                    return;
                }
                List<HomeCardModel> cardModels = new ArrayList<>();
                for (ParseObject object : objects) {
                    cardModels.add(ParseUtils.cardModelFromParseObject(object));
                }
                progressDialog.dismiss();
                addLast(1, cardModels);
            }
        });

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        Log.d(TAG, "onCardDisappeared: ");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void setupButton(View view) {

        final FloatingActionButton info = view.findViewById(R.id.card_view_info);

        View.OnClickListener flippedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View w = manager.getTopView();
                final View flippedView = w.findViewById(R.id.item_spot_flipped);
                final View unFlippedView = w.findViewById(R.id.item_spot_unflipped);
                final float cameraDistance = w.getCameraDistance();
                w.animate().withLayer()
                        .rotationY(90)
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                float scale = getActivity().getResources().getDisplayMetrics().density;
                                float distance = w.getCameraDistance() * (scale + scale / 3);
                                w.setCameraDistance(distance);

                                if (flipped) {
                                    info.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_info_outline_primary_24dp));

                                    manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                                    homeHeader.setVisibility(View.VISIBLE);
                                    buttonContainer.setVisibility(View.VISIBLE);
                                    flippedView.setVisibility(View.GONE);
                                    unFlippedView.setVisibility(View.VISIBLE);
                                    flipped = false;
                                } else {
                                    //disable scrolling
                                    info.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
                                    manager.setSwipeableMethod(SwipeableMethod.None);
                                    buttonContainer.setVisibility(View.GONE);
                                    homeHeader.setVisibility(View.GONE);
                                    flippedView.setVisibility(View.VISIBLE);
                                    unFlippedView.setVisibility(View.GONE);
                                    flipped = true;
                                }

                                w.setRotationY(-90);
                                w.animate().withLayer()
                                        .rotationY(0)
                                        .setDuration(300)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                w.setCameraDistance(cameraDistance);
                                            }
                                        })
                                        .start();
                            }
                        });


            }
        };

        info.setOnClickListener(flippedListener);


        FloatingActionButton skip = view.findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });


        FloatingActionButton rewind = view.findViewById(R.id.rewind_button);
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setRewindAnimationSetting(setting);
                cardStackView.rewind();
            }
        });

        FloatingActionButton like = view.findViewById(R.id.like_button);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });


        forYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == FOR_YOU) return;
                selected = FOR_YOU;
                currentPage = 0;
                updateSelected(true);

            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == FOLLOWING) return;
                selected = FOLLOWING;
                currentPage = 0;
                updateSelected(true);
            }
        });

    }


    private void updateSelected(boolean forceUpdate) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_QUESTION);
        if (selected == FOR_YOU) {
            forYou.setTextColor(getResources().getColor(R.color.colorAccentLight));
            following.setTextColor(getResources().getColor(android.R.color.white));

        } else {
            following.setTextColor(getResources().getColor(R.color.colorAccentLight));
            forYou.setTextColor(getResources().getColor(android.R.color.white));
            query.whereContainedIn(HASHTAG, Arrays.asList("3dart"));
        }
        if (null == adapter || forceUpdate) {
            final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Please Wait ...");
            query.setSkip(currentPage++ * PAGE_SIZE);
            query.setLimit(PAGE_SIZE);
            query.orderByDescending(CREATED_AT);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (null != e) {
                        e.printStackTrace();
                        return;
                    }
                    List<HomeCardModel> cardModels = new ArrayList<>();
                    for (ParseObject object : objects) {
                        cardModels.add(ParseUtils.cardModelFromParseObject(object));
                    }
                    progressDialog.dismiss();
                    if (adapter == null)
                        adapter = new CardStackAdapter(getActivity(), cardModels, homeHeader, buttonContainer);
                    else
                        adapter.setHomeCardModels(cardModels);
                    cardStackView.setAdapter(adapter);
                    cardStackView.getLayoutManager().scrollToPosition(0);
                }
            });
        } else {
            cardStackView.setAdapter(adapter);
        }

    }

    public void addFirst(int size, HomeCardModel item) {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        for (int i = 0; i < size; ++i) {
            newList.add(manager.getTopPosition(), item);
        }
        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }


    public void paginate(List secondList) {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        newList.addAll(secondList);
        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }

    public void reload(List newList) {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        DiffCallback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }

    public void addLast(int size, List<HomeCardModel> cardModels) {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        newList.addAll(cardModels);

        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }

    public void removeFirst(int size) {
        if (adapter.getHomeCardModels().isEmpty()) {
            return;
        }

        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        for (int i = 0; i < size; i++) {
            newList.remove(manager.getTopPosition());
        }
        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }

    public void removeLast(int size) {
        if (adapter.getHomeCardModels().isEmpty()) {
            return;
        }

        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        for (int i = 0; i < size; i++) {
            newList.remove(newList.size() - 1);
        }
        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }

    public void replace() {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        newList.remove(manager.getTopPosition());
        newList.add(manager.getTopPosition(), createSpot());
        adapter.setHomeCardModels(newList);
        adapter.notifyItemChanged(manager.getTopPosition());
    }

    public void swap() {
        List<HomeCardModel> old = adapter.getHomeCardModels();
        List<HomeCardModel> newList = new ArrayList<>(old);
        HomeCardModel first = old.remove(manager.getTopPosition());
        HomeCardModel last = old.remove(newList.size() - 1);
        newList.add(manager.getTopPosition(), last);
        newList.add(first);
        DiffUtil.Callback callback = new DiffCallback(old, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setHomeCardModels(newList);
        result.dispatchUpdatesTo(adapter);
    }


    private class DiffCallback extends DiffUtil.Callback {

        private List<HomeCardModel> old;
        private List<HomeCardModel> newList;

        public DiffCallback(List<HomeCardModel> old, List<HomeCardModel> newList) {
            this.old = old;
            this.newList = newList;
        }


        @Override
        public int getOldListSize() {
            return old.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return Objects.equals(old.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return old.get(oldItemPosition) == newList.get(newItemPosition);
        }
    }

    private class CardSwipeHandler extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            Direction direction = (Direction) objects[0];
            int position = (int) objects[1];
            HomeCardModel model = (HomeCardModel) objects[2];

            ParseObject object;
            ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_QUESTION);
            try {
                object = query.get(model.getId());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            boolean saveObject = false;
            if (direction.equals(Direction.Left)) {
                Log.d(TAG, "onCardSwiped: Left");
                if (databaseHandler.getLikeDislikeStatus(model.getId()) == DatabaseConstants.NOT_FOUND) {
                    databaseHandler.addLikeDislike(model.getId(), DatabaseConstants.STATUS_DISLIKE);
                    object.increment(DISLIKE);
                    saveObject = true;
                }

            } else if (direction.equals(Direction.Right)) {
                Log.d(TAG, "onCardSwiped: Right");
                if (databaseHandler.getLikeDislikeStatus(model.getId()) == DatabaseConstants.NOT_FOUND) {
                    databaseHandler.addLikeDislike(model.getId(), DatabaseConstants.STATUS_LIKE);
                    object.increment(LIKE);
                    saveObject = true;
                }
            }
            if (saveObject) {
                Log.d(TAG, "onCardSwiped: saving " + object);
                object.saveInBackground();
            }
            return null;
        }
    }

    private class CardShareHandler extends AsyncTask {

        private View topView;
        private Drawable background;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            this.topView = ((CardStackLayoutManager) cardStackView.getLayoutManager()).getTopView();
            this.background = cardStackView.getBackground();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Bitmap bitmap = Bitmap.createBitmap(topView.getWidth(), topView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            if (null != background) {
                background.draw(canvas);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            topView.draw(canvas);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
            Uri imageUri = Uri.parse(path);


            Intent shareIntent = new Intent();
            shareIntent.setType("image/jpeg");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            share = false;
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
            return null;
        }

    }
}
