package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import com.imie.a2dev.teamculte.readeo.R;

import java.util.List;

/**
 * Custom adapter used to feed the quote lists.
 */
public final class ReviewListAdapter {
    /**
     * Stores the list of review to display.
     */
    private List<Review> items;

    /**
     * Stores the associated context.
     */
    private Context context;

    /**
     * ReviewListAdapter's constructor.
     * @param context The associated context.
     * @param items The list of review to display.
     */
    public ReviewListAdapter(Context context, List<Review> items) {
        this.items = items;
        this.context = context;
    }

    /**
     * Initializes the container's view elements according to the list of review. 
     */
    public void initList(LinearLayout container) {
        for (Review review : this.items) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.row_review, null);
            ReviewListViewHolder viewHolder = new ReviewListViewHolder(view);

            viewHolder.populate(review);
            container.addView(view);
        }
    }

    /**
     * ViewHolder pattern used to feed the view elements.
     */
    protected final class ReviewListViewHolder {
        /**
         * Stores the pseudo of the author who wrote the review.
         */
        private TextView txtUserPseudo;

        /**
         * Stores the review text.
         */
        private TextView txtReview;

        /**
         * ReviewListViewHolder's constructor.
         * @param view The view to populate.
         */
        private ReviewListViewHolder(View view) {
            this.txtUserPseudo = view.findViewById(R.id.txt_user_pseudo);
            this.txtReview = view.findViewById(R.id.txt_reviews);
        }

        /**
         * Populate the view elements with the info from the given review.
         * @param review The review to display.
         */
        private void populate(Review review) {
            PublicUser user = new UserDBManager(ReviewListAdapter.this.context).loadSQLite(review.getUserId());

            if (user != null) {
                this.txtUserPseudo.setText(user.getPseudo());
            } else {
                this.txtUserPseudo.setText(R.string.not_communicated);
            }

            this.txtReview.setText(review.getReview());
        }
    }
}
