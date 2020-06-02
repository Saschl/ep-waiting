package com.example.sascha.myapplication.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sascha.myapplication.R;
import com.example.sascha.myapplication.ui.main.dummy.WaitingTimeContent;
import com.example.sascha.myapplication.ui.main.dummy.WaitingTimeContent.WaitingTimeItem;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private TextView lastUpdate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        View recycler = view.findViewById(R.id.list);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        refreshData();
        SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) view;
        pullToRefresh.setOnRefreshListener(() -> {
            refreshData(); // your code
            pullToRefresh.setRefreshing(false);
        });


        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(WaitingTimeContent.ITEMS, mListener);
        // Set the adapter
        if (recycler instanceof RecyclerView) {
            Context context = view.getContext();

            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                    1);

            RecyclerView recyclerView = (RecyclerView) recycler;
            mDividerItemDecoration.setDrawable(Objects.requireNonNull(context.getDrawable(R.drawable.separator)));
            recyclerView.addItemDecoration(mDividerItemDecoration);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(WaitingTimeItem item);
    }

    private MainViewModel mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void refreshData() {

        //TODO fetch ride Data and compare instead of hardcoded
        mViewModel.getWaitTimes().observe(this, times -> {

            //LocalDateTime lastUpdate = LocalDateTime.now();
            List<WaitingTimeItem> textTimes = new ArrayList<>();
            for (WaitingTime time : times) {
                WaitingTimeItem none = null;
                switch (time.getCode()) {

                    case "500":
                        //textTimes.add("EuroMir: ").append(time.getTime()).append("m \n");
                        none = new WaitingTimeItem(time.getCode(), "Euromir " + time.getTime() + "m", "none");
                        break;
                    case "200":
                        none = new WaitingTimeItem(time.getCode(), "Eurosat " + time.getTime() + "m", "none");
                        break;
                    case "850":
                        none = new WaitingTimeItem(time.getCode(), "Blue Fire " + time.getTime() + "m", "none");
                        break;
                    case "250":
                        none = new WaitingTimeItem(time.getCode(), "Silver Star " + time.getTime() + "m", "none");
                        break;
                    case "853":
                        none = new WaitingTimeItem(time.getCode(), "Wodan " + time.getTime() + "m", "none");
                        break;
                    case "900":
                        none = new WaitingTimeItem(time.getCode(), "Arthur " + time.getTime() + "m", "none");
                        break;
                    case "400":
                        none = new WaitingTimeItem(time.getCode(), "Poseidon " + time.getTime() + "m", "none");
                        break;
                    default:
                        break;

                }
                if(none != null && !WaitingTimeContent.ITEMS.contains(none)) {
                    WaitingTimeContent.ITEMS.add(none);
                    myItemRecyclerViewAdapter.notifyItemInserted(textTimes.size()-1);
                } else if(WaitingTimeContent.ITEMS.contains(none)) {
                    WaitingTimeContent.ITEMS.remove(none);
                    WaitingTimeContent.ITEMS.add(none);
                    myItemRecyclerViewAdapter.notifyDataSetChanged();

                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime lastUpdate = LocalDateTime.now();
                    ((TextView)getActivity().findViewById(R.id.lastUpdate)).setText("Last update: "+ lastUpdate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMANY)));

                } else {
                    String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.UK).format(Calendar.getInstance().getTime());
                    ((TextView)getActivity().findViewById(R.id.lastUpdate)).setText("Last update: "+ date);

                }

                Log.i("Rest", "Calling Rest Service");

            }
        });
    }
}
