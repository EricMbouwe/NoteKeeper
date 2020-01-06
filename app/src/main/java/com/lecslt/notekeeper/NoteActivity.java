package com.lecslt.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private int mNotePosition;
    public static final String NOTE_INFO = "com.lecslt.notekeeper.NOTE_INFO";
    public static final String NOTE_POSITION = "com.lecslt.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.lecslt.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.lecslt.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.lecslt.notekeeper.ORIGINAL_NOTE_TEXT";
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;
     private boolean mIsCancelling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nous permet d'utililiser la composante spinner_course de notre layout dans notre code java. c'est un referencement.
        mSpinnerCourses = findViewById(R.id.spinner_courses);

        // Comment remplir notre Spinner ? How to populate our Spinner ?
        List<CourseInfo> courses = DataManager.getInstance().getCourses(); // on initialise notre liste de cours
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses); // on cree un adapter pour associer la liste de cours a notre spinner en utilisant les parametres (context,layout,ressources a deployer).
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // on associe la liste de ressources a utiliser a la methode setDropDownViewRessource de notre adapter.
        mSpinnerCourses.setAdapter(adapterCourses); // on associe l'adapter a notre Spinner.

        readDisplayStateValues();
        if(savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if(!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);

    }

    // methode qui nou permet de sauvegarder l'etat de nos donnees quand on change de tache par exemple un retour en arriere (back button)
    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){ // si l'utilisateur annule la saisie en cliquant sur le bouton cancel
            if(mIsNewNote){  // si c'est une nouvelle note en cours de creation alors...
                DataManager.getInstance().removeNote(mNotePosition);  // retirer cette note.
            }
        } else {
            saveNote();
        }

    }

    private void saveNote() {
       mNote.setCourse((CourseInfo)mSpinnerCourses.getSelectedItem());
       mNote.setTitle(mTextNoteTitle.getText().toString());
       mNote.setText(mTextNoteText.getText().toString());
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValues() {
        if(mIsNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    private  void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textnoteText){
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textnoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNote = intent.getParcelableExtra(NOTE_INFO);
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if(mIsNewNote) {
            createNewNote();
        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
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
        if (id == R.id.anction_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel){
            mIsCancelling = true;
            finish();
        } else if (id == R.id.action_next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

        // aller a la note suivante
    private void moveNext() {

    }

    //envoyer un email a partir de notre application avec le contenu actuel
    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();    // de la ligne 1 a 3 on rassemble les elements qui constituront notre mail.
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what i learned in the Pluralsight course \"" +
                course.getTitle() + "\"\n" + mTextNoteText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND); // on defini l"action explicite que doit effectuer notre intent
        intent.setType("message/rfc2822"); // on defini le type de notre intent

        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // on defini la cible a remplir des different elements de notre intent
        intent.putExtra(Intent.EXTRA_TEXT, text); // on defini la cible a remplir des different elements de notre intent

        startActivity(intent);


    }
}
