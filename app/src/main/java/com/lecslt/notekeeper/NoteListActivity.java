package com.lecslt.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeDisplayContent();
    }
    // Comment remplir notre ListView ? how to populate our Listview ?
    private void initializeDisplayContent() {
        final ListView listNotes = findViewById(R.id.list_notes); //ajouter la reference de notre listView

        List<NoteInfo> notes = DataManager.getInstance().getNotes();  //on initialise notre liste de notes.
        ArrayAdapter<NoteInfo> adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes); //on cree un adapter pour associer la liste de notes a notre ListView en utilisant les parametres (context,layout,ressources a deployer).
        listNotes.setAdapter(adapterNotes);  // on associe l'adapter a notre ListView..

        // Comment gerer le click d'un utilisateur sur un  element de notre Listview ? how to handle a user selection?
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class); // nous permet d'identifier l'activiter que nous voulons lancer avec en parametres ( le context cad la classe actuelle, l'activité a creer ou a lancer) // ctrl + P pour avoir les differents constructeurs.
                //NoteInfo note = listNotes.getItemAtPosition(position);
                //intent.putExtra(NoteActivity.NOTE_POSITION, position);
                startActivity(intent); // demarer l'activite
            }
        });
    }


}
