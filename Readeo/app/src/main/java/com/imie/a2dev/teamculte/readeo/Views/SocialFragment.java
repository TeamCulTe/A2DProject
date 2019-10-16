package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;

import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.SocialRecyclerAdapter;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.Views.Adapters.SocialRecyclerAdapter.DEFAULT_LIMIT;

/**
 * Fragment containing the social functionality.
 */
public final class SocialFragment extends Fragment implements
                                                   SocialRecyclerAdapter.SocialAdapterListener,
                                                   View.OnClickListener {
    /**
     * Stores the list of users.
     */
    private RecyclerView recyclerUsers;

    /**
     * Stores the search edit text.
     */
    private EditText editSearch;

    /**
     * Stores the recycler's adapter.
     */
    private SocialRecyclerAdapter adapter;

    /**
     * SocialFragment's default constructor.
     */
    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        this.init(view);

        return view;
    }

    @Override
    public void halfReached() {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override public void handleMessage(Message msg) {
                for (int i = SocialFragment.this.adapter.getItemCount();
                     i < SocialFragment.this.adapter.getItemCount() + DEFAULT_LIMIT; i++) {
                    SocialFragment.this.adapter.notifyItemInserted(i);
                }
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                List<PublicUser> users = ManagerHolderUtils.getInstance().getUserDBManager()
                                                           .queryAllPaginatedSQLite(DEFAULT_LIMIT,
                                                                                    SocialFragment.this.adapter
                                                                                            .getItemCount());

                SocialFragment.this.adapter.getUsers().addAll(users);

                Message message = handler.obtainMessage();

                message.sendToTarget();
            }
        };

        thread.start();
    }

    @Override
    public void userCellSelected(PublicUser user) {
        Intent intent = new Intent(this.getContext(), ProfileActivity.class);

        intent.putExtra(UserDBSchema.TABLE, user);

        this.startActivity(intent);
    }

    @Override public void onClick(View view) {
        if (view.getId() == R.id.img_btn_search) {
            this.adapter.setUsers(ManagerHolderUtils.getInstance().getUserDBManager()
                                                    .loadFilteredSQLite(UserDBSchema.PSEUDO,
                                                                        this.editSearch.getText()
                                                                                       .toString()));
            this.adapter.notifyDataSetChanged();
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.recyclerUsers = view.findViewById(R.id.recycler_users);
        this.editSearch = view.findViewById(R.id.edit_search);
        this.adapter = new SocialRecyclerAdapter();

        this.recyclerUsers.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        this.recyclerUsers.setAdapter(this.adapter);
        this.adapter.setListener(this);

        view.findViewById(R.id.img_btn_search).setOnClickListener(this);
    }

}
