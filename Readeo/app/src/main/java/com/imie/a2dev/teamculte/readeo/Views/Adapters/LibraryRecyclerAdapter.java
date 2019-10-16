package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.R;

import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Custom adapter used to display the books from the library.
 */
public final class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.LibraryViewHolder> {
    /**
     * Defines the default book pagination limit.
     */
    public static final int DEFAULT_LIMIT = 100;

    /**
     * The list of books to display.
     */
    private List<Book> books;

    /**
     * Stores the listener used to notify when a cell is selected.
     */
    private LibraryAdapterListener listener;

    /**
     * LibraryRecyclerAdapter's default constructor.
     */
    public LibraryRecyclerAdapter() {
        super();
    }

    /**
     * LibraryRecyclerAdapter's constructor.
     * @param books The list of books to set.
     */
    public LibraryRecyclerAdapter(List<Book> books) {
        super();

        this.books = books;
    }

    @Override
    public LibraryRecyclerAdapter.LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_library, parent, false);

        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryRecyclerAdapter.LibraryViewHolder holder, int position) {
        Book book = this.books.get(position);

        holder.bind(book);

        if (position >= this.books.size() / 2) {
            if (this.listener != null) {
                this.listener.halfReached();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Gets the listener attribute.
     * @return The LibraryAdapterListener value of listener attribute.
     */
    public LibraryAdapterListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new LibraryAdapterListener value to set.
     */
    public void setListener(LibraryAdapterListener newListener) {
        this.listener = newListener;
        this.books = ManagerHolderUtils.getInstance().getBookDBManager().queryAllPaginatedSQLite(DEFAULT_LIMIT, 0);
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

    /**
     * Updates the books from the list by getting them from the database.
     */
    public void updateBooks() {
        this.books = ManagerHolderUtils.getInstance().getBookDBManager().queryAllPaginatedSQLite(DEFAULT_LIMIT, 0);

        this.notifyDataSetChanged();
    }

    /**
     * ViewHolder class used to display the book.
     */
    protected final class LibraryViewHolder extends RecyclerView.ViewHolder {
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
         * LibraryViewHolder's constructor.
         * @param view The view to populate.
         */
        private LibraryViewHolder(View view) {
            super(view);

            this.contentView = view;
            this.imgCover = view.findViewById(R.id.img_cover);
            this.txtTitle = view.findViewById(R.id.txt_title);
            this.txtAuthor = view.findViewById(R.id.txt_author);
            this.txtDate = view.findViewById(R.id.txt_date);
        }

        /**
         * Binds the data to the view.
         * @param book The book used to bind.
         */
        private void bind(Book book) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            StringBuilder authors = new StringBuilder();


            if (!book.getCover().isEmpty()) {
                imageLoader.displayImage(book.getCover(), this.imgCover);
            }

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
                if (LibraryRecyclerAdapter.this.listener != null) {
                    LibraryRecyclerAdapter.this.listener.bookCellSelected(book);
                }
            });
        }
    }

    /**
     * Interface used to notify the listener when a type cell is selected.
     */
    public interface LibraryAdapterListener {
        /**
         * Called when half of the books are reached (scrolled).
         */
        void halfReached();

        /**
         * Called when a cell is selected.
         * @param book The book selected.
         */
        void bookCellSelected(Book book);

    }
}
