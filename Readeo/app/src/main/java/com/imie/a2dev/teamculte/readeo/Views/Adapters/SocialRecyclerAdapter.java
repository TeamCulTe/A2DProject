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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Custom adapter used to display the books from the library.
 */
public final class SocialRecyclerAdapter extends RecyclerView.Adapter<SocialRecyclerAdapter.SocialViewHolder> {
    /**
     * Defines the default user pagination limit.
     */
    public static final int DEFAULT_LIMIT = 100;

    /**
     * The list of users to display.
     */
    private List<PublicUser> users;

    /**
     * Stores the listener used to notify when a cell is selected.
     */
    private SocialAdapterListener listener;

    /**
     * LibraryRecyclerAdapter's default constructor.
     */
    public SocialRecyclerAdapter() {
        super();
    }

    /**
     * LibraryRecyclerAdapter's constructor.
     * @param users The list of users to set.
     */
    public SocialRecyclerAdapter(List<PublicUser> users) {
        super();

        this.users = users;
    }

    @Override
    public SocialRecyclerAdapter.SocialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_social, parent, false);

        return new SocialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialRecyclerAdapter.SocialViewHolder holder,
                                 int position) {
        PublicUser user = this.users.get(position);

        holder.bind(user);

        if (position >= this.users.size() / 2) {
            if (this.listener != null) {
                this.listener.halfReached();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.users.size();
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
     * @return The SocialAdapterListener value of listener attribute.
     */
    public SocialAdapterListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new LibraryAdapterListener value to set.
     */
    public void setListener(SocialAdapterListener newListener) {
        this.listener = newListener;
        this.users =
                ManagerHolderUtils.getInstance().getUserDBManager().queryAllPaginatedSQLite(DEFAULT_LIMIT, 0);
    }

    /**
     * Gets the users attribute.
     * @return the value of the attribute.
     */
    public List<PublicUser> getUsers() {
        return this.users;
    }

    /**
     * Sets the users and refresh the view.
     * @param users The types to set.
     */
    public void setUsers(List<PublicUser> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    /**
     * Updates the users from the list by getting them from the database.
     */
    public void updateUsers() {
        this.users =
                ManagerHolderUtils.getInstance().getUserDBManager().queryAllPaginatedSQLite(DEFAULT_LIMIT, 0);

        this.notifyDataSetChanged();
    }

    /**
     * ViewHolder class used to display the users.
     */
    protected final class SocialViewHolder extends RecyclerView.ViewHolder {
        /**
         * Stores the content view.
         */
        private View contentView;

        /**
         * Displays the book cover.
         */
        private ImageView imgAvatar;

        /**
         * Displays the user pseudo.
         */
        private TextView txtPseudo;

        /**
         * SocialViewHolder's constructor.
         * @param view The view to populate.
         */
        private SocialViewHolder(View view) {
            super(view);

            this.contentView = view;
            this.imgAvatar = view.findViewById(R.id.img_avatar);
            this.txtPseudo = view.findViewById(R.id.txt_pseudo);
        }

        /**
         * Binds the data to the view.
         * @param user The user used to bind.
         */
        private void bind(PublicUser user) {
            ImageLoader imageLoader = ImageLoader.getInstance();

            if (!user.getProfile().getAvatar().isEmpty()) {
                imageLoader.displayImage(user.getProfile().getAvatar(), this.imgAvatar);
            }
            
            this.txtPseudo.setText(user.getPseudo());

            this.contentView.setOnClickListener(view -> {
                if (SocialRecyclerAdapter.this.listener != null) {
                    SocialRecyclerAdapter.this.listener.userCellSelected(user);
                }
            });
        }
    }

    /**
     * Interface used to notify the listener when a type cell is selected.
     */
    public interface SocialAdapterListener {
        /**
         * Called when half of the users are reached (scrolled).
         */
        void halfReached();

        /**
         * Called when a cell is selected.
         * @param user The user selected.
         */
        void userCellSelected(PublicUser user);

    }
}
