package edu.neu.madcourse.numad22sp_qingfu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class LinkCollectorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyLinkAdapter myLinkAdapter;
    private final ArrayList<Item> items = new ArrayList<>();
    private AlertDialog alertDialog;
    private EditText nameEditTextView;
    private EditText urlEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_collector);

        loadInitialData(savedInstanceState);
        buildRecyclerView();
        createLinkInputDialog();

        FloatingActionButton floatingActionButton = findViewById(R.id.insert_link_button);
        floatingActionButton.setOnClickListener(v -> insertLink());

        myLinkAdapter.setOnLinkClickListener(position -> items.get(position).onClick(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                items.remove(position);
                myLinkAdapter.notifyItemRemoved(position);
                Snackbar.make(recyclerView, "URL removed", Snackbar.LENGTH_LONG).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void createLinkInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View insertLinkInput = layoutInflater.inflate(R.layout.add_link, null);

        nameEditTextView = insertLinkInput.findViewById(R.id.link_name_insert);
        urlEditTextView = insertLinkInput.findViewById(R.id.link_url_insert);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(insertLinkInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Insert",
                        (dialog, id) -> {
                            String linkName = nameEditTextView.getText().toString();
                            String linkURL = urlEditTextView.getText().toString();
                            if (linkName.isEmpty()) {
                                Snackbar.make(recyclerView, "Please insert a name", Snackbar.LENGTH_LONG).show();
                            } else {
                                Item linkCard = new Item(linkName, linkURL);
                                if (linkCard.isValid()) {
                                    items.add(0, linkCard);
                                    myLinkAdapter.notifyItemInserted(0);
                                    Snackbar.make(recyclerView, "Added a link", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(recyclerView, "Your URL is not valid", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());
        alertDialog = alertDialogBuilder.create();
    }

    private void loadInitialData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("SIZE")) {
            if (items == null || items.size() == 0) {
                int size = savedInstanceState.getInt("SIZE");

                for (int i = 0; i < size; i++) {
                    String linkName = savedInstanceState.getString("ITEM" + i + "0");
                    String linkURL = savedInstanceState.getString("ITEM" + i + "1");
                    Item linkCard = new Item(linkName, linkURL);
                    assert items != null;
                    items.add(linkCard);
                }
            }
        }
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        myLinkAdapter = new MyLinkAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myLinkAdapter);
    }


    public void insertLink() {
        nameEditTextView.getText().clear();
        urlEditTextView.getText().clear();
        nameEditTextView.requestFocus();
        alertDialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = items == null ? 0 : items.size();
        outState.putInt("SIZE", size);

        for (int i = 0; i < size; i++) {
            outState.putString("ITEM" + i + "0", items.get(i).getName());
            outState.putString("ITEM" + i + "1", items.get(i).getURL());
        }
        super.onSaveInstanceState(outState);
    }
}