package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.BookListTypeRecyclerAdapter;

import java.util.Map;

/**
 * Fragment displaying a user profile.
 */
public final class ProfileFragment extends Fragment implements BookListTypeRecyclerAdapter.BookListTypeAdapterListener,
                                                               View.OnClickListener {
    /**
     * Stores the displayed user.
     */
    private PublicUser user;

    /**
     * ProfileFragment's default constructor.
     */
    public ProfileFragment() {
    }

    /**
     * Gets the user attribute.
     * @return The PublicUser value of user attribute.
     */
    public PublicUser getUser() {
        return this.user;
    }

    /**
     * Sets the user attribute.
     * @param newUser The new PublicUser value to set.
     */
    public void setUser(PublicUser newUser) {
        this.user = newUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.init(view);

        return view;
    }

    @Override public void onResume() {
        super.onResume();
        
        this.init(this.getView());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_account_settings) {
            Intent intent = new Intent(this.getContext(), AccountActivity.class);

            this.getActivity().startActivity(intent);
        }
    }

    @Override
    public void bookListCellSelected(BookListType type, boolean selected) {
        PrivateUser user = PreferencesUtils.loadUser();
        Map<String, BookList> bookLists =
                new BookListDBManager(this.getContext()).loadUserSQLite(user.getId());
        BookList bookList = bookLists.get(type.getName());

        if (bookList != null) {
            Intent intent = new Intent(this.getContext(), ReadingListActivity.class);

            intent.putExtra(BookListDBSchema.TABLE, bookList);
            this.startActivity(intent);
        } else {
            Toast.makeText(this.getContext(), R.string.no_books_in_book_list, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        PrivateUser user = PreferencesUtils.loadUser();
        RecyclerView recyclerBookListTypes = view.findViewById(R.id.recycler_reading_lists);

        if (this.user == null) {
            this.user = user;
        }

        if (this.user.getPseudo().equals(user.getPseudo())) {
            view.findViewById(R.id.btn_account_settings).setOnClickListener(this);

            BookListTypeRecyclerAdapter adapter = new BookListTypeRecyclerAdapter(
                    new BookListTypeDBManager(this.getContext()).queryAllSQLite());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());

            adapter.setSelectable(false);
            adapter.setListener(this);
            recyclerBookListTypes.setAdapter(adapter);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerBookListTypes.setLayoutManager(linearLayoutManager);
        } else {
            view.findViewById(R.id.btn_account_settings).setVisibility(View.INVISIBLE);
            recyclerBookListTypes.setVisibility(View.INVISIBLE);
        }

        // TODO : Checking if avatar path given else, apply default avatar image.
        //((ImageView) view.findViewById(R.id.img_avatar)).setImageResource();

        String description;
                
        if (this.user.getProfile() != null && !this.user.getProfile().getDescription().isEmpty()) {
            description = this.user.getProfile().getDescription();
        } else {
            description = this.getString(R.string.no_profile_description);
        }
        
        ((TextView) view.findViewById(R.id.txt_description)).setText(description);
    }
}
