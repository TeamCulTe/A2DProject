package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import com.imie.a2dev.teamculte.readeo.R;

import java.util.List;

/**
 * Custom adapter used to feed the quote lists.
 */
public final class QuoteListRecyclerAdapter extends RecyclerView.Adapter<QuoteListRecyclerAdapter.QuoteListViewHolder> {
    /**
     * Stores the list of quote to display.
     */
    private List<Quote> items;

    /**
     * Stores the associated listener.
     */
    private QuoteListRecyclerListener listener;

    /**
     * Stores the associated context.
     */
    private Context context;

    /**
     * QuoteListAdapter's constructor.
     * @param context The associated context.
     * @param items The list of quote to display.
     */
    public QuoteListRecyclerAdapter(Context context, List<Quote> items) {
        this.items = items;
        this.context = context;
    }

    /**
     * Gets the listener attribute.
     * @return The QuoteListRecyclerListener value of listener attribute.
     */
    public QuoteListRecyclerListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new QuoteListRecyclerListener value to set.
     */
    public void setListener(QuoteListRecyclerListener newListener) {
        this.listener = newListener;
    }

    /**
     * Gets the items attribute.
     * @return The List<Quote> value of items attribute.
     */
    public List<Quote> getItems() {
        return this.items;
    }

    /**
     * Sets the items attribute.
     * @param newItems The new List<Quote> value to set.
     */
    public void setItems(List<Quote> newItems) {
        this.items = newItems;
    }

    /**
     * Gets the index of the quote given in parameter.
     * @param item The quote to get the position.
     * @return The index of the quote if in items else -1.
     */
    public int getItemPos(Quote item) {
        for (Quote quote : this.items) {
            if (quote.getId() == item.getId()) {
                return this.items.indexOf(quote);
            }
        }

        return -1;
    }

    @NonNull @Override public QuoteListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.row_single_quote, viewGroup, false);

        return new QuoteListViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull QuoteListViewHolder quoteListViewHolder, int pos) {
        quoteListViewHolder.bind(this.items.get(pos));
    }

    @Override public int getItemCount() {
        return this.items.size();
    }

    /**
     * ViewHolder pattern used to feed the view elements.
     */
    protected final class QuoteListViewHolder  extends RecyclerView.ViewHolder {
        /**
         * Stores the content view.
         */
        private ConstraintLayout content;

        /**
         * Stores the quote text.
         */
        private TextView txtQuote;

        /**
         * QuoteListViewHolder's constructor.
         * @param view The view to populate.
         */
        private QuoteListViewHolder(View view) {
            super(view);

            this.content = view.findViewById(R.id.constraint_content);
            this.txtQuote = view.findViewById(R.id.txt_quote);
        }

        /**
         * Populate the view elements with the info from the given quote.
         * @param quote The quote to display.
         */
        private void bind(Quote quote) {
            this.txtQuote.setText(quote.getQuote());
            this.content.setOnClickListener((view) -> {
                if (QuoteListRecyclerAdapter.this.listener != null) {
                    QuoteListRecyclerAdapter.this.listener.quoteSelected(quote);
                }
            });
        }
    }

    /**
     * Listener interface used to notify when a quote is selected.
     */
    public interface QuoteListRecyclerListener {
        /**
         * Called when a quote is selected.
         * @param quote The selected quote.
         */
        void quoteSelected(Quote quote);
    }
}
