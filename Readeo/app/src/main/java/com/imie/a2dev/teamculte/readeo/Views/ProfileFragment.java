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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.BookListTypeRecyclerAdapter;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public final class ProfileFragment extends Fragment implements BookListTypeRecyclerAdapter.BookListTypeAdapterListener,
                                                               View.OnClickListener {
    /**
     * Defines if the fragment is a public profile or not.
     */
    private boolean isPublic = false;

    /**
     * Stores the displayed user.
     */
    private PublicUser user;

    /**
     * Stores the recycler displaying the book list types.
     */
    private RecyclerView recyclerBookListTypes;

    /**
     * Stores the recycler's adapter.
     */
    private BookListTypeRecyclerAdapter adapter;

    /**
     * ProfileFragment's default constructor.
     */
    public ProfileFragment() {
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
        this.recyclerBookListTypes = view.findViewById(R.id.recycler_reading_lists);
        this.adapter = new BookListTypeRecyclerAdapter(new BookListTypeDBManager(this.getContext()).queryAllSQLite());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());

        this.adapter.setSelectable(false);
        this.adapter.setListener(this);
        this.recyclerBookListTypes.setAdapter(adapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.recyclerBookListTypes.setLayoutManager(linearLayoutManager);

        if (this.isPublic) {
            view.findViewById(R.id.btn_account_settings).setVisibility(View.INVISIBLE);
        } else {
            view.findViewById(R.id.btn_account_settings).setOnClickListener(this);

            this.user = PreferencesUtils.loadUser();
        }

        // TODO : Checking if avatar path given else, apply default avatar image.
        //((ImageView) view.findViewById(R.id.img_avatar)).setImageResource();

        if (this.user.getProfile() != null && this.user.getProfile().getDescription() != null) {
            ((TextView) view.findViewById(R.id.txt_description)).setText(this.user.getProfile().getDescription());
        }
    }
}
