/**
 * Copyright (C) 2015 Damien Chazoule
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.doomy.padlock;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

	// Declaring your view and variables
    private ArrayList<String> mTitleSet;
	private ArrayList<Integer> mColorSet;
	private ArrayList<String> mInfoSet;
	private ArrayList<Integer> mLogoSet;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewAdapter(ArrayList<String> myTitleSet, ArrayList<Integer> myColorSet, ArrayList<String> myInfoSet, ArrayList<Integer> myLogoSet) {
        this.mTitleSet = myTitleSet;
        this.mColorSet = myColorSet;
		this.mInfoSet = myInfoSet;
        this.mLogoSet = myLogoSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview, null);

        // Create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - Get element from your dataset at this position
        // - Replace the contents of the view with that element
        viewHolder.setTitle.setText(mTitleSet.get(position).toString());
        viewHolder.setTitle.setTextColor(viewHolder.setTitle.getContext().getResources().getColor(mColorSet.get(position)));
		viewHolder.setInfo.setText(mInfoSet.get(position).toString());
        viewHolder.setLogo.setImageResource(mLogoSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTitleSet.size();
    }

    // Inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

		// Declaring your view and variables
        public TextView setTitle;
        public TextView setInfo;
        public ImageView setLogo;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
			
            setTitle = (TextView) itemLayoutView.findViewById(R.id.cardTitle);
			setInfo = (TextView) itemLayoutView.findViewById(R.id.cardInfo);
            setLogo = (ImageView) itemLayoutView.findViewById(R.id.cardLogo);
        }
    }
}
