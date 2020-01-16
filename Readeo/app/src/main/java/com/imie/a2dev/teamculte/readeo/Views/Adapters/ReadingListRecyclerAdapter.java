package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Custom adapter used to display the books from the reading list.
 */
public final class ReadingListRecyclerAdapter
        extends RecyclerView.Adapter<ReadingListRecyclerAdapter.ReadingListViewHolder> {
    /**
     * The list of types to display.
     */
    private List<Book> books;

    /**
     * Stores the listener used to notify when a cell is selected.
     */
    private ReadingListAdapterListener listener;

    /**
     * public ReadingListRecyclerAdapter(List<Book> books) {'s constructor.
     * @param books The list of books to set.
     */
    public ReadingListRecyclerAdapter(List<Book> books) {
        super();

        this.books = books;
    }

    /**
     * Gets the listener attribute.
     * @return The ReadingListAdapterListener value of listener attribute.
     */
    public ReadingListAdapterListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new ReadingListAdapterListener value to set.
     */
    public void setListener(ReadingListAdapterListener newListener) {
        this.listener = newListener;
    }

    /**
     * Gets the books attribute.
     * @return the value of the attribute.
     */
    public List<Book> getBooks() {
        return this.books;
    }

    /**
     * Sets the books, insert the header messages and refresh the view.
     * @param books The types to set.
     */
    public void setBooks(List<Book> books) {
        this.books = books;

        this.notifyDataSetChanged();
    }

    @Override
    public ReadingListRecyclerAdapter.ReadingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reading_list, parent, false);

        return new ReadingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingListRecyclerAdapter.ReadingListViewHolder holder, int position) {
        holder.bind(this.books.get(position));
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    /**
     * Interface used to notify the listener when a type cell is selected.
     */
    public interface ReadingListAdapterListener {
        /**
         * Called when a cell is selected.
         * @param book The book list type selected.
         */
        void bookCellSelected(Book book);

        /**
         * Called when a delete button is clicked.
         * @param book The associated book.
         */
        void deleteButtonClicked(Book book);

        /**
         * Called when the edit review button is clicked.
         * @param book The associated book.
         */
        void editReviewButtonClick(Book book);

        /**
         * Called when the edit quote button is clicked.
         * @param book The associated book.
         */
        void editQuoteButtonClick(Book book);
    }

    /**
     * ViewHolder class used to display the book list types.
     */
    protected final class ReadingListViewHolder extends RecyclerView.ViewHolder {
        /**
         * Stores the content view.
         */
        private View contentView;

        /**
         * Displays the book cover.
         */
        private ImageView imgCover;

        /**
         * Displays the book title.
         */
        private TextView txtTitle;

        /**
         * Displays the book author.
         */
        private TextView txtAuthor;

        /**
         * Displays the book's written date.
         */
        private TextView txtDate;

        /**
         * Stores the txt that when clicked, gives the ability to edit a review.
         */
        private TextView txtEditReview;

        /**
         * Stores the txt that when clicked, gives the ability to edit a quote.
         */
        private TextView txtEditQuote;

        /**
         * Stores the delete button.
         */
        private ImageButton imgBtnDelete;

        /**
         * LibraryViewHolder's constructor.
         * @param view The view to populate.
         */
        private ReadingListViewHolder(View view) {
            super(view);

            this.contentView = view;
            this.imgCover = view.findViewById(R.id.img_cover);
            this.txtTitle = view.findViewById(R.id.txt_title);
            this.txtAuthor = view.findViewById(R.id.txt_author);
            this.txtDate = view.findViewById(R.id.txt_date);
            this.txtEditReview = view.findViewById(R.id.txt_edit_review);
            this.txtEditQuote = view.findViewById(R.id.txt_edit_quote);
            this.imgBtnDelete = view.findViewById(R.id.img_btn_delete);
        }

        /**
         * Binds the data to the view.
         * @param book The book list type used to bind.
         */
        private void bind(Book book) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            StringBuilder authors = new StringBuilder();


            // TODO: See if "if" statement is needed here.
            imageLoader.displayImage(book.getCover(), this.imgCover);
            this.txtTitle.setText(book.getTitle());

            for (Author author : book.getAuthors()) {
                authors.append(author.getName());

                if (book.getAuthors().indexOf(author) != book.getAuthors().size() - 1) {
                    authors.append("\n");
                }
            }

            if (authors.toString().equals("")) {
                this.txtAuthor.setText(R.string.not_communicated);
            } else {
                this.txtAuthor.setText(authors.toString());
            }

            if (book.getDatePublished() == 0) {
                this.txtDate.setText(R.string.not_communicated);
            } else {
                this.txtDate.setText(String.valueOf(book.getDatePublished()));
            }

            this.contentView.setOnClickListener(view -> {
                if (ReadingListRecyclerAdapter.this.listener != null) {
                    ReadingListRecyclerAdapter.this.listener.bookCellSelected(book);
                }
            });

            this.txtEditReview.setOnClickListener(view -> {
                if (ReadingListRecyclerAdapter.this.listener != null) {
                    ReadingListRecyclerAdapter.this.listener.editReviewButtonClick(book);
                }
            });

            this.txtEditQuote.setOnClickListener(view -> {
                if (ReadingListRecyclerAdapter.this.listener != null) {
                    ReadingListRecyclerAdapter.this.listener.editQuoteButtonClick(book);
                }
            });

            this.imgBtnDelete.setOnClickListener(view -> {
                if (ReadingListRecyclerAdapter.this.listener != null) {
                    ReadingListRecyclerAdapter.this.listener.deleteButtonClicked(book);
                }
            });
        }
    }
}
