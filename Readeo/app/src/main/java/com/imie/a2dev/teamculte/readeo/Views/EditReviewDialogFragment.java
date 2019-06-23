package com.imie.a2dev.teamculte.readeo.Views;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Fragment displaying a review edit form.
 */
public final class EditReviewDialogFragment extends DialogFragment implements View.OnClickListener {
    /**
     * Stores the associated book.
     */
    private Book book;
    
    /**
     * BookFragment's default constructor.
     */
    public EditReviewDialogFragment() {
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
        View view = inflater.inflate(R.layout.dialog_fragment_, container, false);
        
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
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
    }
}
