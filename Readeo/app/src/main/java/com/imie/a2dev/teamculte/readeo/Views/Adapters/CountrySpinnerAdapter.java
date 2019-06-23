package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Country;

import java.util.List;

/**
 * Custom adapter used to feed the country spinners.
 */
public final class CountrySpinnerAdapter extends ArrayAdapter {
    /**
     * Defines the default spinner items layout.
     */
    private static final int DEFAULT_LAYOUT = android.R.layout.simple_spinner_dropdown_item;

    /**
     * Stores the list of country to display.
     */
    private List<Country> items;

    /**
     * Stores the associated context.
     */
    private Context context;

    /**
     * CountrySpinnerAdapter's constructor.
     * @param context The associated context.
     * @param items The list of country to display.
     */
    public CountrySpinnerAdapter(Context context, List<Country> items) {
        super(context, DEFAULT_LAYOUT);
        
        this.items = items;
        this.context = context;
    }
    
    @NonNull
    @Override
    public TextView getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(this.context, DEFAULT_LAYOUT, null);
        }
        
        TextView text = convertView.findViewById(android.R.id.text1);
        
        text.setText(this.items.get(position).getName());
        
        return text;
    }

    @NonNull
    @Override
    public TextView getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(this.context, DEFAULT_LAYOUT, null);
        }

        TextView text = convertView.findViewById(android.R.id.text1);

        text.setText(this.items.get(position).getName());

        return text;
    }
    
    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Country getItem(int i) {
        return this.items.get(i);
    }

    public int getPosition(Country item) {
        for (int i = 0; i < this.items.size(); i++) {
            if ((item.getName().equals(this.items.get(i).getName()))) {
                return i;
            }
        }
        
        return -1;
    }
}
