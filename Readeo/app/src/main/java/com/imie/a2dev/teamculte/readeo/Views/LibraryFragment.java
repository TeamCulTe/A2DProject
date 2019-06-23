package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.LibraryRecyclerAdapter;

import static com.imie.a2dev.teamculte.readeo.Views.Adapters.LibraryRecyclerAdapter.DEFAULT_LIMIT;

/**
 * Fragment displaying the list of books from the app.
 */
public final class LibraryFragment extends Fragment implements View.OnClickListener, 
        LibraryRecyclerAdapter.LibraryAdapterListener {
    /**
     * Stores the search edit text.
     */
    private EditText editSearch;

    /**
     * Stores the library recycler.
     */
    private RecyclerView recyclerLibrary;

    /**
     * Stores the library recycler's adapter.
     */
    private LibraryRecyclerAdapter adapter;
    
    /**
     * LibraryFragment's default constructor.
     */
    public LibraryFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_library, container, false);
        
       this.init(view);
       
       return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_btn_search) {
            
        }
    }

    @Override
    public void halfReached() {
        this.adapter.getBooks().addAll(new BookDBManager(this.getContext()).queryAllPaginatedSQLite(DEFAULT_LIMIT,
                this.adapter.getItemCount()));
        this.recyclerLibrary.post(() -> LibraryFragment.this.adapter.notifyDataSetChanged());
    }

    @Override
    public void bookCellSelected(Book book) {
        BookDialogFragment fragment = new BookDialogFragment();
        
        fragment.setBook(book);
        
        fragment.show(this.getActivity().getSupportFragmentManager(), "");
    }
    
    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.editSearch = view.findViewById(R.id.edit_search);
        this.recyclerLibrary = view.findViewById(R.id.recycler_library);
        this.adapter = new LibraryRecyclerAdapter();
        
        this.adapter.setListener(this);
        this.recyclerLibrary.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.recyclerLibrary.setAdapter(this.adapter);
    }
}
