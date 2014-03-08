package com.example.drivetestapp;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends ListFragment {

	ArrayList<String> countries = new ArrayList<String>();;

	ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
				"OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
				"Android", "iPhone", "WindowsMobile" };

		for (int i = 0; i < values.length; ++i) {
			countries.add(values[i]);
		}

		/** Creating an array adapter to store the list of countries **/
		adapter = new ArrayAdapter<String>(inflater.getContext(),
				android.R.layout.simple_list_item_1, countries);

		/** Setting the list adapter for the ListFragment */
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void updateArray(ArrayList array) {
		// TODO Auto-generated method stub
		adapter.clear();
		adapter.addAll(array);
		setListAdapter(adapter);
	}
}
