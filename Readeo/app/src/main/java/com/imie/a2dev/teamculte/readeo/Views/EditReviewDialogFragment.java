package com.imie.a2dev.teamculte.readeo.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;

/**
 * Fragment displaying a review edit form.
 */
public final class EditReviewDialogFragment extends DialogFragment implements View.OnClickListener {
    /**
     * Stores the associated book.
     */
    private Book book;

    /**
     * Stores the edited review.
     */
    private Review review;

    /**
     * Stores the review db manager.
     */
    private ReviewDBManager manager;

    /**
     * Stores the review text.
     */
    private EditText editTxtReview;

    /**
     * Stores the checkbox used to define if the review is shared or not.
     */
    private CheckBox checkShare;

    /**
     * Defines if the review is a new one or not.
     */
    private boolean isNewReview = false;

    /**
     * EditReviewDialogFragment's default constructor.
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
        View view = inflater.inflate(R.layout.dialog_fragment_edit_review, container, false);

        this.initReview();
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
        this.review.setReview(this.editTxtReview.getText().toString());
        this.review.setShared(this.checkShare.isChecked());

        if (!this.isNewReview) {
            this.manager.updateMySQL(this.review);
            this.manager.updateSQLite(this.review);
        } else {
            this.manager.createMySQL(this.review);
            this.manager.createSQLite(this.review);
            this.book.getReviews().add(this.review);
        }

        this.dismiss();
    }

    /**
     * Initializes the review (get it in order to update if exists or create it).
     */
    private void initReview() {
        this.manager = new ReviewDBManager(this.getContext());

        PrivateUser user = PreferencesUtils.loadUser();
        Review review = this.manager.loadSQLite(user.getId(), this.book.getId());

        if (review != null) {
            this.review = review;
        } else {
            this.review = new Review(this.book.getId(), user.getId());
            this.isNewReview = true;
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        ((TextView) view.findViewById(R.id.txt_book_title)).setText(this.book.getTitle());

        this.editTxtReview = view.findViewById(R.id.edit_txt_review);
        this.checkShare = view.findViewById(R.id.check_box_share);

        this.checkShare.setChecked(this.review.isShared());
        this.editTxtReview.setText(this.review.getReview());
        view.findViewById(R.id.btn_publish).setOnClickListener(this);
    }
}
