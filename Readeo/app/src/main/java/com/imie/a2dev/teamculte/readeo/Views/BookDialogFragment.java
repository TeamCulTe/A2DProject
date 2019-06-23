package com.imie.a2dev.teamculte.readeo.Views;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.BookListTypeRecyclerAdapter;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.QuoteListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying a specific book info and giving the ability to add it into the user book lists.
 */
public final class BookDialogFragment extends DialogFragment implements BookListTypeRecyclerAdapter.BookListTypeAdapterListener, 
        View.OnClickListener {
    /**
     * Stores the displayed book.
     */
    private Book book;

    /**
     * Stores the book's image cover.
     */
    private ImageView imgCover;
    
    /**
     * Stores the quote text view.
     */
    private TextView txtQuotes;
    
    /**
     * Stores the review text view.
     */
    private TextView txtReview;

    /**
     * Stores the recycler's adapter.
     */
    private BookListTypeRecyclerAdapter recyclerAdapter;

    /**
     * Stores the book list types recycler view.
     */
    private RecyclerView recyclerBookLists;

    /**
     * Stores the quote list.
     */
    private LinearLayout quoteList;
    
    /**
     * BookFragment's default constructor.
     */
    public BookDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Gets the book attribute.
     * @return The Book value of book attribute.
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * Sets the book attribute.
     * @param newBook The new Book value to set.
     */
    public void setBook(Book newBook) {
        this.book = newBook;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_book, container, false);
        
        this.init(view);
        
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        Dialog dialog = this.getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View v) {
        int visibility = (BookDialogFragment.this.quoteList.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        
        BookDialogFragment.this.quoteList.setVisibility(visibility);
    }

    @Override
    public void bookListCellSelected(BookListType type, boolean selected) {
        BookListDBManager bookListDBManager = new BookListDBManager(this.getContext());
        PrivateUser user = PreferencesUtils.loadUser();
        BookList bookList = user.getBookLists().get(type.getName());
        
        if (selected) {
            if (!bookList.contains(book)) {
                bookList.getBooks().add(this.book);
            }
        } else {
            bookList.remove(this.book);
        }
        
        PreferencesUtils.saveUser(user);
        bookListDBManager.updateSQLite(bookList);
        // TODO : Update the book list from distant db.
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        
        this.imgCover = view.findViewById(R.id.img_cover);

        imageLoader.displayImage(book.getCover(), this.imgCover);
        
        this.initTextElements(view);
        this.initQuoteList(view);
        this.initRecycler(view);
    }

    /**
     * Initializes the fragment's text elements.
     * @param view The fragment's view.
     */
    private void initTextElements(View view) {
        StringBuilder authors = new StringBuilder();
        TextView txtAuthor = view.findViewById(R.id.txt_author);
        TextView txtDate = view.findViewById(R.id.txt_date);

        this.txtReview = view.findViewById(R.id.txt_review_text);
        this.txtQuotes = view.findViewById(R.id.txt_quotes);

        for (Author author : this.book.getAuthors()) {
            authors.append(author.getName());

            if (this.book.getAuthors().indexOf(author) != this.book.getAuthors().size() - 1) {
                authors.append("\n");
            }
        }

        ((TextView) view.findViewById(R.id.txt_title)).setText(this.book.getTitle());
        ((TextView) view.findViewById(R.id.txt_summary)).setText(this.book.getSummary());

        if (authors.toString().equals("")) {
            txtAuthor.setText(R.string.not_communicated);
        } else {
            txtAuthor.setText(authors.toString());
        }

        if (this.book.getDatePublished() == 0) {
            txtDate.setText(R.string.not_communicated);
        } else {
            txtDate.setText(String.valueOf(this.book.getDatePublished()));
        }

        if (this.book.getReviews() != null && this.book.getReviews().size() > 0) {
            // TODO : Update here if enabling multiple reviews per book.
            view.findViewById(R.id.constraint_review_block).setVisibility(View.VISIBLE);
            this.txtReview.setText(this.book.getReviews().get(0).getReview());
        }

        this.txtQuotes.setText(String.format(this.getString(R.string.quotes_number_replacement),
                this.book.getQuotes().size()));
        this.txtQuotes.setOnClickListener(this);
    }

    /**
     * Initializes the quote list.
     * @param view The fragment's view.
     */
    private void initQuoteList(View view) {
        if (this.book.getQuotes().size() > 0) {
            this.quoteList = view.findViewById(R.id.linear_quote_list);
            QuoteListAdapter adapter = new QuoteListAdapter(this.getContext(), this.book.getQuotes());

            adapter.initList(this.quoteList);
        }
    }

    /**
     * Initializes the book lists type recycler view.
     * @param view The fragment's view.
     */
    private void initRecycler(View view) {
        PrivateUser user = PreferencesUtils.loadUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        List<Boolean> selected = new ArrayList<>();
        List<BookListType> types = new BookListTypeDBManager(this.getContext()).queryAllSQLite();
        
        for (BookListType type : types) {
            if (user.getBookLists().get(type.getName()) != null 
                    && user.getBookLists().get(type.getName()).getBooks() != null 
                    && user.getBookLists().get(type.getName()).contains(this.book)) {
                selected.add(true);
            } else {
                selected.add(false);
            }
        }
        
        this.recyclerBookLists = view.findViewById(R.id.recycler_book_list_types);
        this.recyclerAdapter = new BookListTypeRecyclerAdapter(types, selected);

        this.recyclerAdapter.setListener(this);
        this.recyclerBookLists.setAdapter(recyclerAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.recyclerBookLists.setLayoutManager(linearLayoutManager);
    }
}
