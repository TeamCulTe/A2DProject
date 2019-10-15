package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.Enums.BookFilterKey;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.LibraryRecyclerAdapter;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.Utils.Enums.BookFilterKey.NONE;
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
     * Stores the filter selection spinner.
     */
    private Spinner spinnerFilter;

    /**
     * Stores the associated filter.
     */
    private BookFilterKey filter = NONE;

    /**
     * LibraryFragment's default constructor.
     */
    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        this.init(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_btn_search) {
            this.filter = BookFilterKey.fromName((String) this.spinnerFilter.getSelectedItem());

            if (this.filter != null && this.filter != NONE) {
                this.adapter.setBooks(
                        ManagerHolderUtils.getInstance().getBookDBManager()
                                          .loadFilteredPaginatedSQLite(this.filter.getFilterCol(),
                                                                       this.editSearch.getText().toString(),
                                                                       DEFAULT_LIMIT, 0));
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void halfReached() {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override public void handleMessage(Message msg) {
                for (int i = LibraryFragment.this.adapter.getItemCount();
                     i < LibraryFragment.this.adapter.getItemCount() + DEFAULT_LIMIT; i++) {
                    LibraryFragment.this.adapter.notifyItemInserted(i);
                }
            }
        };
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                String filterField;
                List<Book> books;

                switch (LibraryFragment.this.filter) {
                    case NONE:
                        books = ManagerHolderUtils.getInstance().getBookDBManager()
                                                  .queryAllPaginatedSQLite(DEFAULT_LIMIT,
                                                                           LibraryFragment.this.adapter
                                                                                   .getItemCount());
                        break;
                    case TITLE:
                    case AUTHOR:
                    case CATEGORY:
                        filterField = LibraryFragment.this.filter.getFilterCol();
                        books = ManagerHolderUtils.getInstance().getBookDBManager()
                                                  .loadFilteredPaginatedSQLite(filterField,
                                                                               LibraryFragment.this.editSearch
                                                                                       .getText()
                                                                                       .toString(),
                                                                               DEFAULT_LIMIT,
                                                                               LibraryFragment.this.adapter
                                                                                       .getItemCount());

                        break;
                    default:
                        return;
                }

                LibraryFragment.this.adapter.getBooks().addAll(books);
                
                Message message = handler.obtainMessage();
                
                message.sendToTarget();
            }
        };

        thread.start();
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
        this.spinnerFilter = view.findViewById(R.id.spinner_filter);
        this.editSearch = view.findViewById(R.id.edit_search);
        this.recyclerLibrary = view.findViewById(R.id.recycler_library);
        this.adapter = new LibraryRecyclerAdapter();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                                                          R.layout.row_simple_spinner_item,
                                                          this.getResources().getStringArray(R.array.book_filters));

        this.spinnerFilter.setAdapter(adapter);
        this.adapter.setListener(this);
        this.recyclerLibrary.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.recyclerLibrary.setAdapter(this.adapter);
        view.findViewById(R.id.img_btn_search).setOnClickListener(this);
    }
}
