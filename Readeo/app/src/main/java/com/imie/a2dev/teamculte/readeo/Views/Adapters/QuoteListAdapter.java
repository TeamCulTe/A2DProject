package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import com.imie.a2dev.teamculte.readeo.R;

import java.util.List;

/**
 * Custom adapter used to feed the quote lists.
 */
public final class QuoteListAdapter {
    /**
     * Stores the list of quote to display.
     */
    private List<Quote> items;

    /**
     * Stores the associated context.
     */
    private Context context;

    /**
     * CountrySpinnerAdapter's constructor.
     * @param context The associated context.
     * @param items The list of quote to display.
     */
    public QuoteListAdapter(Context context, List<Quote> items) {
        this.items = items;
        this.context = context;
    }

    /**
     * Initializes the container's view elements according to the list of quotes. 
     */
    public void initList(LinearLayout container) {
        for (Quote quote : this.items) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.row_quote, container, false);
            QuoteListViewHolder viewHolder = new QuoteListViewHolder(view);
            
            viewHolder.populate(quote);
            
            container.addView(view);
        }
    }

    /**
     * ViewHolder pattern used to feed the view elements.
     */
    protected final class QuoteListViewHolder {
        /**
         * Stores the pseudo of the author who wrote the quote.
         */
        private TextView txtUserPseudo;

        /**
         * Stores the quote text.
         */
        private TextView txtQuote;
        
        /**
         * QuoteListViewHolder's constructor.
         * @param view The view to populate.
         */
        private QuoteListViewHolder(View view) {
            this.txtUserPseudo = view.findViewById(R.id.txt_user_pseudo);
            this.txtQuote = view.findViewById(R.id.txt_quote);
        }

        /**
         * Populate the view elements with the info from the given quote.
         * @param quote The quote to display.
         */
        private void populate(Quote quote) {
            PublicUser user = new UserDBManager(QuoteListAdapter.this.context).loadSQLite(quote.getUserId());
            
            if (user != null) {
                this.txtUserPseudo.setText(user.getPseudo());
            } else {
                this.txtUserPseudo.setText(R.string.not_communicated);
            }
            
            this.txtQuote.setText(quote.getQuote());
        } 
    }
}
