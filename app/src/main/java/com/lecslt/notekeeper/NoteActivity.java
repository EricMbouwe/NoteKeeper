package com.lecslt.notekeeper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinnerCourses = findViewById(R.id.spinner_courses); // nous permet d'utililiser la composante spinner_course de notre layout dans notre code java. c'est un referencement.

        // Comment remplir notre Spinner ? How to populate our Spinner ?
        List<CourseInfo> courses = DataManager.getInstance().getCourses(); // on initialise notre liste de cours
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses); // on cree un adapter pour associer la liste de cours a notre spinner en utilisant les parametres (context,layout,ressources a deployer).
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // on associe la liste de ressources a utiliser a la methode setDropDownViewRessource de notre adapter.
        spinnerCourses.setAdapter(adapterCourses); // on associe l'adapter a notre Spinner.


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
