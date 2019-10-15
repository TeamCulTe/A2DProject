package com.imie.a2dev.teamculte.readeo.Views;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.QuoteListRecyclerAdapter;

/**
 * Fragment displaying a quote edit form.
 */
public final class EditQuoteDialogFragment extends DialogFragment implements View.OnClickListener,
                                                                             QuoteListRecyclerAdapter.QuoteListRecyclerListener {
    /**
     * Stores the associated book.
     */
    private Book book;

    /**
     * Stores the edited review.
     */
    private Quote quote;

    /**
     * Stores the review db manager.
     */
    private QuoteDBManager manager;

    /**
     * Stores the review text.
     */
    private EditText editTxtQuote;

    /**
     * Stores the quote recycler view.
     */
    private RecyclerView recyclerQuoteList;

    /**
     * Stores the recycler's adapter.
     */
    private QuoteListRecyclerAdapter adapter;

    /**
     * Stores the associated user.
     */
    private PrivateUser user;

    /**
     * Defines if the quote is new.
     */
    private boolean newQuote = true;

    /**
     * Defines if the quote is saved.
     */
    private boolean quoteSaved = false;

    /**
     * EditQuoteDialogFragment's default constructor.
     */
    public EditQuoteDialogFragment() {
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
        View view = inflater.inflate(R.layout.dialog_fragment_edit_quote, container, false);

        this.manager = new QuoteDBManager(this.getContext());
        this.user = PreferencesUtils.loadUser();

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
    public void onClick(View view) {
        if (view.getId() == R.id.btn_publish) {
            this.save();
        }
        
        this.initNewQuote();
    }

    @Override public void quoteSelected(Quote quote) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());

        dialogBuilder.setMessage(R.string.would_you_like_to_save_the_current_quote);
        dialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
            this.save();
            this.quote = quote;

            this.editTxtQuote.setText(this.quote.getQuote());
            dialog.cancel();
        });
        dialogBuilder.setNegativeButton(R.string.no, ((dialog, which) -> {
            this.quote = quote;

            this.editTxtQuote.setText(this.quote.getQuote());
            dialog.cancel();
        }));

        this.newQuote = false;

        dialogBuilder.show();
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.quote = new Quote(this.user.getId(), this.book.getId(), "");

        this.adapter = new QuoteListRecyclerAdapter(this.getContext(),
                                                    this.manager.loadUserBookSQLite(this.user.getId(),
                                                                                    this.book.getId()));
        this.editTxtQuote = view.findViewById(R.id.edit_txt_quote);
        this.recyclerQuoteList = view.findViewById(R.id.recycler_quote_list);

        ((TextView) view.findViewById(R.id.txt_book_title)).setText(this.book.getTitle());
        this.recyclerQuoteList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.adapter.setListener(this);
        this.recyclerQuoteList.setAdapter(this.adapter);
        this.editTxtQuote.setText(this.quote.getQuote());
        view.findViewById(R.id.btn_publish).setOnClickListener(this);
        view.findViewById(R.id.img_btn_add).setOnClickListener(this);
    }

    /**
     * Saves the current quote.
     */
    private void save() {
        this.quote.setQuote(this.editTxtQuote.getText().toString());

        if (!this.newQuote) {
            this.manager.updateSQLite(this.quote);
        } else {
            this.manager.createSQLite(this.quote);
            this.book.getQuotes().add(this.quote);
            this.adapter.getItems().add(this.quote);
        }

        EditQuoteDialogFragment.this.adapter.notifyDataSetChanged();
    }

    /**
     * Initializes a new quote.
     */
    private void initNewQuote() {
        this.newQuote = true;
        this.quoteSelected(new Quote(this.user.getId(), this.book.getId(), ""));
        this.editTxtQuote.setText("");
    }
}
