package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.Enums.BookFilterKey;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.ReadingListRecyclerAdapter;

import static com.imie.a2dev.teamculte.readeo.Utils.Enums.BookFilterKey.NONE;

/**
 * Fragment displaying the list of books from a user reading list.
 */
public final class ReadingListFragment extends Fragment
        implements ReadingListRecyclerAdapter.ReadingListAdapterListener {
    /**
     * Stores the current book list.
     */
    private BookList bookList;

    /**
     * Stores the recycler's adapter.
     */
    private ReadingListRecyclerAdapter adapter;

    /**
     * Stores the recycler containing the books.
     */
    private RecyclerView recyclerBooks;

    /**
     * Stores the associated manager.
     */
    private BookListDBManager manager;

    /**
     * ReadingListFragment's default constructor.
     */
    public ReadingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_list, container, false);

        this.bookList = ((ReadingListActivity) this.getActivity()).getBookList();
        this.manager = new BookListDBManager(this.getContext());

        this.init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.reload();
    }

    @Override
    public void bookCellSelected(Book book) {
        BookDialogFragment fragment = new BookDialogFragment();

        fragment.setBook(book);

        fragment.show(this.getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void editReviewButtonClick(Book book) {
        EditReviewDialogFragment fragment = new EditReviewDialogFragment();

        fragment.setBook(book);

        fragment.show(this.getActivity().getSupportFragmentManager(), "");
    }

    @Override public void editQuoteButtonClick(Book book) {
        EditQuoteDialogFragment fragment = new EditQuoteDialogFragment();

        fragment.setBook(book);

        fragment.show(this.getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void deleteButtonClicked(Book book) {
        BookListDBManager manager = new BookListDBManager(this.getContext());

        this.bookList.remove(book);
        this.adapter.getBooks().remove(book);

        PreferencesUtils.updateUserBookList(this.bookList);
        manager.updateSQLite(this.bookList);

        this.adapter.notifyDataSetChanged();
        // TODO: See update for MySQL.
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        /**
         * Stores the fragment's title.
         */
        TextView txtTitle = view.findViewById(R.id.txt_title);

        this.recyclerBooks = view.findViewById(R.id.recycler_books);
        this.adapter = new ReadingListRecyclerAdapter(this.bookList.getBooks());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                                                          R.layout.row_simple_spinner_item,
                                                          this.getResources().getStringArray(
                                                                  R.array.book_filters));

        this.adapter.setListener(this);
        this.recyclerBooks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.recyclerBooks.setAdapter(this.adapter);
        txtTitle.setText(this.getString(R.string.reading_list_title_replacement,
                                        this.bookList.getType().getName()));
    }

    /**
     * Reload the data from the book list.
     */
    private void reload() {
        this.bookList = this.manager.loadUserSQLite(this.bookList.getId())
                                    .get(this.bookList.getType().getName());

        this.adapter.notifyDataSetChanged();
    }
}
