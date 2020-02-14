package com.aboukalil.phoneinsider1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aboukalil.phoneinsider1.R;

import java.util.ArrayList;

public class Report extends AppCompatActivity
{
    MySimpleArrayAdapter adapter;
    ArrayList<ReportNode> listItems;
    ArrayList<String> newData;
    public int deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // We're getting our listView by the id
        ListView listView = (ListView) findViewById(R.id.list);

       newData = displayReport();
        // We're initialising our custom adapter with all our data from the
        // database
        adapter = new MySimpleArrayAdapter(Report.this, newData);
        // Assigning the adapter to ListView
        listView.setAdapter(adapter);
        // Assigning an event to the listview
        // This event will be used to delete records
        listView.setOnItemLongClickListener(myClickListener);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<String> displayReport()
    {
        DatabaseHelper db = new DatabaseHelper(Report.this);
        listItems = db.getAllNotes();
        newData = new ArrayList<String>();
        for (ReportNode node : listItems)
        {
            newData.add("Date : "+node.getDate()+"\nTask : "+node.getTask()+"\nAccess by : "+node.getContact());
        }
        return newData;
    }

    //This adapter will create your list view row by row
    public class MySimpleArrayAdapter extends ArrayAdapter<String>
    {
        private final Context context;
        private final ArrayList<String> values;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.rowlayout, values);

            this.context = context;
            this.values = values;
        }

        /**
         * Here we go and get our rowlayout.xml file and set the textview text.
         * This happens for every row in your listview.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.label);

            // Setting the text to display
            textView.setText(values.get(position));

            return rowView;
        }
    }

    /**
     * On a long click delete the selected item
     */
    public AdapterView.OnItemLongClickListener myClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // Assigning the item position to our global variable
            // So we can access it within our AlertDialog below
            deleteItem = arg2;

            // Creating a new alert dialog to confirm the delete
            //AlertDialog alert = new AlertDialog.Builder(arg1.getContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(arg1.getContext());
            alert.setTitle("Delete Confirm");
            alert.setCancelable(true);
            alert.setMessage("Are you sure? ");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    // Retrieving the note from our listItems
                    // property, which contains all notes from
                    // our database
                    ReportNode node = listItems.get(deleteItem);

                    // Deleting it from the ArrayList<string>
                    // property which is linked to our adapter
                    newData.remove(deleteItem);

                    // Deleting the note from our database
                    DatabaseHelper db = new DatabaseHelper(Report.this);
                    db.deleteNode(node.getId());
                    // Tell the adapter to update the list view
                    // with the latest changes
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // When you press cancel, just close the
                                    // dialog
                                    dialog.cancel();
                                }
                            }).show();

            return false;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

