package com.emertonalex.whiskynotes04;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity{

    // TODO: 03-Oct-17 add navigation drawer
    // TODO: 03-Oct-17 add personalisation
    // TODO: 04-Oct-17 add validation for all fields
    // TODO: 15-Oct-17 10 star ratings  bar or allow half measures (!)
    // TODO: 15-Oct-17 icons for buttons instead of spinners
    // TODO: 15-Oct-17 edit from a list


    EditText editTextNoteId, editTextExtra;
    RatingBar ratingBar;
    Spinner spinnerName, spinnerNose, spinnerPalate, spinnerFinish;
    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;
    List<note> noteList;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private ArrayList<String> spin_notes_name;
    private ArrayList<String> spin_notes_nose;
    private ArrayList<String> spin_notes_palate;
    private ArrayList<String> spin_notes_finish;

    private ArrayList<String> all_nose = new ArrayList<>();
    private String toDBNose;
    private ArrayList<String> all_palate = new ArrayList<>();
    private String toDBPalate;
    private ArrayList<String> all_finish = new ArrayList<>();
    private String toDBFinish;

    private JSONArray result;
    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNoteId =  findViewById(R.id.editTextNoteId);
        editTextExtra = findViewById(R.id.editTextExtra);
        ratingBar = findViewById(R.id.ratingBar);
        spinnerName = findViewById(R.id.spinnerName);
        spinnerNose = findViewById(R.id.spinnerNose);
        spinnerPalate = findViewById(R.id.spinnerPalate);
        spinnerFinish = findViewById(R.id.spinnerFinish);

        final TextView selectedNose = findViewById(R.id.selectedNose);
        final TextView selectedPalate = findViewById(R.id.selectedPalate);
        final TextView selectedFinish = findViewById(R.id.selectedFinish);

        buttonAddUpdate = findViewById(R.id.buttonAddUpdate);

        /*progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewNotes);*/

        spin_notes_name = new ArrayList<>();
        spin_notes_nose = new ArrayList<>();
        spin_notes_palate = new ArrayList<>();
        spin_notes_finish = new ArrayList<>();

        noteList = new ArrayList<>();

        getData("getName");
        getData("getNose");
        getData("getPalate");
        getData("getFinish");

        //*****************COLLECTING MULTIPLE VALUES FROM SPINNERS*********************************

        spinnerNose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                all_nose.add(parent.getItemAtPosition(position).toString());

                StringBuilder builderNose = new StringBuilder();
                for (String item : all_nose){
                    if (item.equals(all_nose.get(all_nose.size() - 1))) {
                        builderNose.append(item);
                    }
                    else {
                        builderNose.append(item).append(", ");
                    }
                }

                //selectedNose.setText(R.string.hint_selectedNose);

                selectedNose.setText(builderNose.toString());

                /*selectedNose.setText(String.format("%s%s", selectedNose.getText(),
                        parent.getItemAtPosition(position).toString()));*/
                toDBNose = builderNose.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        spinnerPalate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                all_palate.add(parent.getItemAtPosition(position).toString());

                StringBuilder builderPalate = new StringBuilder();
                for (String item : all_palate){
                    if (item.equals(all_palate.get(all_palate.size() - 1))) {
                        builderPalate.append(item);
                    }
                    else {
                        builderPalate.append(item).append(", ");
                    }
                }

                //selectedPalate.setText(R.string.hint_selectedNose);

                selectedPalate.setText(builderPalate.toString());

                /*selectedNose.setText(String.format("%s%s", selectedNose.getText(),
                        parent.getItemAtPosition(position).toString()));*/
                toDBPalate = builderPalate.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        spinnerFinish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                all_finish.add(parent.getItemAtPosition(position).toString());

                StringBuilder builderFinish = new StringBuilder();
                for (String item : all_finish){
                    if (item.equals(all_finish.get(all_finish.size() - 1))) {
                        builderFinish.append(item);
                    }
                    else {
                        builderFinish.append(item).append(", ");
                    }
                }

                //selectedPalate.setText(R.string.hint_selectedNose);

                selectedFinish.setText(builderFinish.toString());

                /*selectedNose.setText(String.format("%s%s", selectedNose.getText(),
                        parent.getItemAtPosition(position).toString()));*/
                toDBFinish = builderFinish.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    updateNote();
                } else {
                    createNote();
                }
            }
        });
        //readNotes();

    }

    //*********************GET DATA FROM DB -> TAKE JSON OUT*************************
    private void getData(String option){
        switch (option) {
            case "getNose": {

                StringRequest stringRequest = new StringRequest(api.URL_CATEGORIES_NOSE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray j;
                                try {
                                    //Parsing the fetched Json String to JSON Object
                                    j = new JSONArray(response);

                                    //Storing the Array of JSON String to our JSON Array
                                    result = j;

                                    //Calling method getNotes to get the notes from the JSON Array
                                    getNotes(result, "getNose");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });


                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);
                break;
            }
            case "getName": {
                StringRequest stringRequest = new StringRequest(api.URL_CATEGORIES_NAME,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray j;
                                try {
                                    //Parsing the fetched Json String to JSON Object
                                    j = new JSONArray(response);

                                    //Storing the Array of JSON String to our JSON Array
                                    result = j;

                                    //Calling method getNotes to get the notes from the JSON Array
                                    getNotes(result, "getName");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });


                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);

                break;
            }
            case "getPalate": {
                StringRequest stringRequest = new StringRequest(api.URL_CATEGORIES_PALATE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray j;
                                try {
                                    //Parsing the fetched Json String to JSON Object
                                    j = new JSONArray(response);

                                    //Storing the Array of JSON String to our JSON Array
                                    result = j;

                                    //Calling method getNotes to get the notes from the JSON Array
                                    getNotes(result, "getPalate");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });


                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);

                break;
            }
            case "getFinish": {
                StringRequest stringRequest = new StringRequest(api.URL_CATEGORIES_FINISH,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray j;
                                try {
                                    //Parsing the fetched Json String to JSON Array
                                    j = new JSONArray(response);

                                    //Storing the Array of JSON String to our JSON Array
                                    result = j;

                                    //Calling method getNotes to get the notes from the JSON Array
                                    getNotes(result, "getFinish");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });


                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);

                break;
            }
        }
    }

    //*********************PROCESS DATA FROM JSON*************************
    private void getNotes(JSONArray j, String option){
        switch (option) {
            case "getNose":
                for (int i = 0; i < j.length(); i++) {
                    try {
                        //Getting json object
                        JSONObject json = j.getJSONObject(i);
                        //Adding the name of the student to array list
                        spin_notes_nose.add(json.getString(config.TAG_NOSE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Setting adapter to show the items in the spinner
                spinnerNose.setAdapter(new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spin_notes_nose));
                break;
            case "getName":
                for (int i = 0; i < j.length(); i++) {
                    try {
                        //Getting json object
                        JSONObject json = j.getJSONObject(i);

                        //Adding the name of the student to array list
                        spin_notes_name.add(json.getString(config.TAG_NAME));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Setting adapter to show the items in the spinner
                spinnerName.setAdapter(new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spin_notes_name));
                break;
            case "getPalate":
                for (int i = 0; i < j.length(); i++) {
                    try {
                        //Getting json object
                        JSONObject json = j.getJSONObject(i);

                        //Adding the name of the student to array list
                        spin_notes_palate.add(json.getString(config.TAG_PALATE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Setting adapter to show the items in the spinner
                spinnerPalate.setAdapter(new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spin_notes_palate));
                break;
            case "getFinish":
                for (int i = 0; i < j.length(); i++) {
                    try {
                        //Getting json object
                        JSONObject json = j.getJSONObject(i);

                        //Adding the name of the student to array list
                        spin_notes_finish.add(json.getString(config.TAG_FINISH));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Setting adapter to show the items in the spinner
                spinnerFinish.setAdapter(new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spin_notes_finish));
                break;
        }
    }

    //*********************CREATE NOTE INSTANCE*************************************
    private void createNote() {

        int rating = (int) ratingBar.getRating();
        String name = spinnerName.getSelectedItem().toString();
        String nose = toDBNose;
        String palate = toDBPalate;
        String finish = toDBFinish;
        String extra = editTextExtra.getText().toString().trim();

        if (TextUtils.isEmpty(extra)) {
            editTextExtra.setError("Please don't leave this field empty");
            editTextExtra.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("rating", String.valueOf(rating));
        params.put("name", name);
        params.put("nose", nose);
        params.put("palate", palate);
        params.put("finish", finish);
        params.put("extra", extra);

        PerformNetworkRequest request = new PerformNetworkRequest(api.URL_CREATE_NOTE,
                params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readNotes() {
        PerformNetworkRequest request = new PerformNetworkRequest(api.URL_READ_NOTES,
                null, CODE_GET_REQUEST);
        request.execute();
    }

    //*********************NETWORK REQUEST*************************************
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;

        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
//            try {
//                JSONObject object = new JSONObject(s);
////                if (!object.getBoolean("error")) {
////                    Toast.makeText(getApplicationContext(), object.getString("message"),
////                            Toast.LENGTH_SHORT).show();
////
////                    //refreshNoteList(object.getJSONArray("notes"));
////                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }

    }

    class NoteAdapter extends ArrayAdapter<note> {
        //our note list
        List<note> noteList;

        //constructor to get the list
        public NoteAdapter(List<note> noteList) {
            super(MainActivity.this, R.layout.layount_note_list, noteList);
            this.noteList = noteList;
        }

        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem =
                    inflater.inflate(R.layout.layount_note_list, null, true);

            //getting the textview for displaying name
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final note note = noteList.get(position);

            textViewName.setText(note.getName());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //so when it is updating we will
                    //make the isUpdating as true
                    isUpdating = true;

                    //we will set the selected note to the UI elements
                    editTextNoteId.setText(String.valueOf(note.getID()));
                    editTextExtra.setText(note.getName());

                    ratingBar.setRating(note.getRating());
                    spinnerName.setSelection(((ArrayAdapter<String>)
                            spinnerName.getAdapter()).getPosition(note.getName()));

                    spinnerNose.setSelection(((ArrayAdapter<String>)
                            spinnerNose.getAdapter()).getPosition(note.getNose()));

                    spinnerPalate.setSelection(((ArrayAdapter<String>)
                            spinnerPalate.getAdapter()).getPosition(note.getPalate()));

                    spinnerFinish.setSelection(((ArrayAdapter<String>)
                            spinnerFinish.getAdapter()).getPosition(note.getFinish()));

                    //we will also make the button text to Update
                    buttonAddUpdate.setText(R.string.button_update);
                }
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Delete " + note.getName())
                            .setMessage("Are you sure you want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the note

                                    deleteNote(note.getID());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }

    }

    /*private void refreshNoteList(JSONArray notes) throws JSONException {
        noteList.clear();

        for (int i = 0; i < notes.length(); i++) {
            JSONObject obj = notes.getJSONObject(i);

            noteList.add(new note(
                    obj.getInt("id"),
                    obj.getInt("rating"),
                    obj.getString("name"),
                    obj.getString("nose"),
                    obj.getString("palate"),
                    obj.getString("finish"),
                    obj.getString("extra")
            ));
        }

        NoteAdapter adapter = new NoteAdapter(noteList);
        listView.setAdapter(adapter);
    }*/

    private void updateNote() {
        String id = editTextNoteId.getText().toString();
        String extra = editTextExtra.getText().toString().trim();

        int rating = (int) ratingBar.getRating();

        String name = spinnerName.getSelectedItem().toString();
        String nose = spinnerNose.getSelectedItem().toString();
        String palate = spinnerPalate.getSelectedItem().toString();
        String finish = spinnerFinish.getSelectedItem().toString();


        if (TextUtils.isEmpty(extra)) {
            editTextExtra.setError("Please don't leave this field empty");
            editTextExtra.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("rating", String.valueOf(rating));
        params.put("name", name);
        params.put("nose", nose);
        params.put("palate", palate);
        params.put("finish", finish);
        params.put("extra", extra);


        PerformNetworkRequest request = new PerformNetworkRequest(api.URL_UPDATE_NOTE,
                params, CODE_POST_REQUEST);
        request.execute();

        editTextExtra.setText("");
        buttonAddUpdate.setText(R.string.hint_add);

        ratingBar.setRating(0);
        spinnerName.setSelection(0);
        spinnerNose.setSelection(0);
        spinnerPalate.setSelection(0);
        spinnerFinish.setSelection(0);

        isUpdating = false;
    }

    private void deleteNote(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(api.URL_DELETE_NOTE
                + id, null, CODE_GET_REQUEST);
        request.execute();
    }

}



