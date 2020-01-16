package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter used to display the book list types from a room.
 */
public final class BookListTypeRecyclerAdapter
        extends RecyclerView.Adapter<BookListTypeRecyclerAdapter.BookListTypeViewHolder> {
    /**
     * The list of types to display.
     */
    private List<BookListType> types;

    /**
     * The associated list of boolean defining if the types are selected or not.
     */
    private List<Boolean> selected;

    /**
     * Stores the listener used to notify when a cell is selected.
     */
    private BookListTypeAdapterListener listener;

    /**
     * Defines if the book list types are selectable or not.
     */
    private boolean selectable = true;

    /**
     * BookListTypeRecyclerAdapter's constructor.
     * @param types The list of types to set.
     */
    public BookListTypeRecyclerAdapter(List<BookListType> types) {
        super();

        this.types = types;

        this.initSelectedList();
    }

    /**
     * BookListTypeRecyclerAdapter's constructor.
     * @param types The list of types to set.
     * @param selected The list of boolean to set.
     */
    public BookListTypeRecyclerAdapter(List<BookListType> types, List<Boolean> selected) {
        super();

        this.types = types;
        this.selected = selected;
    }

    @NonNull
    @Override
    public BookListTypeRecyclerAdapter.BookListTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book_list_type, parent, false);

        return new BookListTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListTypeRecyclerAdapter.BookListTypeViewHolder holder, int position) {
        BookListType type = this.types.get(position);
        boolean selected = this.selected.get(position);

        holder.bind(type, selected);
    }

    @Override
    public int getItemCount() {
        return this.types.size();
    }

    /**
     * Gets the listener attribute.
     * @return The BookListTypeAdapterListener value of listener attribute.
     */
    public BookListTypeAdapterListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new BookListTypeAdapterListener value to set.
     */
    public void setListener(BookListTypeAdapterListener newListener) {
        this.listener = newListener;
    }

    /**
     * Sets the types, insert the header messages and refresh the view.
     * @param types The types to set.
     */
    public void setTypes(List<BookListType> types) {
        this.types = types;

        this.notifyDataSetChanged();
    }

    /**
     * Gets the selectable attribute.
     * @return The boolean value of selectable attribute.
     */
    public boolean isSelectable() {
        return this.selectable;
    }

    /**
     * Sets the selectable attribute.
     * @param newSelectable The new boolean value to set.
     */
    public void setSelectable(boolean newSelectable) {
        this.selectable = newSelectable;
    }

    /**
     * Initializes the list of selected element with false value (default).
     */
    private void initSelectedList() {
        this.selected = new ArrayList<>();

        for (int i = 0; i < this.types.size(); i++) {
            this.selected.add(false);
        }
    }

    /**
     * Interface used to notify the listener when a type cell is selected.
     */
    public interface BookListTypeAdapterListener {
        /**
         * Called when a cell is selected.
         * @param type The book list type selected.
         * @param selected Defines if is selected or unselected.
         */
        void bookListCellSelected(BookListType type, boolean selected);
    }

    /**
     * ViewHolder class used to display the book list types.
     */
    protected final class BookListTypeViewHolder extends RecyclerView.ViewHolder {
        /**
         * Stores the content view.
         */
        private View contentView;

        /**
         * Displays the book list type image.
         */
        private ImageView imgType;

        /**
         * Displays the type name.
         */
        private TextView txtType;

        /**
         * BookListTypeViewHolder's constructor.
         * @param view The view to populate.
         */
        private BookListTypeViewHolder(View view) {
            super(view);

            this.contentView = view;
            this.imgType = view.findViewById(R.id.img_type);
            this.txtType = view.findViewById(R.id.txt_type);
        }

        /**
         * Binds the data to the view.
         * @param type The book list type used to bind.
         */
        private void bind(BookListType type, boolean selected) {
            // TODO : See how to get the image.
            //this.imgType.setImageResource();
            this.txtType.setText(type.getName());
            this.imgType.setSelected(selected);
            this.imgType.setOnClickListener(view -> this.contentView.performClick());
            this.contentView.setOnClickListener(view -> {
                if (BookListTypeRecyclerAdapter.this.selectable) {
                    this.imgType.setSelected(!this.imgType.isSelected());
                }

                if (BookListTypeRecyclerAdapter.this.listener != null) {
                    BookListTypeRecyclerAdapter.this.listener.bookListCellSelected(type, this.imgType.isSelected());
                }
            });
        }
    }
}
