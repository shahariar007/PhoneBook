package com.example.n33r.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    CustomUser user = new CustomUser();
    ArrayList<CustomUser> list = new ArrayList<CustomUser>();
    CustomAdapter<CustomUser> adapter;


    SwipeList detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.contactlogo);
        listView = (ListView) findViewById(R.id.listView);
       new DoBack().execute();
       //ContactRetrive();

        detector = new SwipeList();
        listView.setOnTouchListener(detector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*CustomUser user = (CustomUser) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "" + user.getPhoneNumber(), Toast.LENGTH_SHORT).show();*/

                if (detector.swipeDetected()) {
                    if (detector.getAction() == SwipeList.Action.LR) {
                        String positionn = list.get(position).getPhoneNumber();
                        Intent call = new Intent(Intent.ACTION_CALL);
                        call.setData(Uri.parse("tel:" + positionn));
                        startActivity(call);

                    } else if (detector.getAction() == SwipeList.Action.RL) {
                        String positionn = list.get(position).getPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("smsto:" + positionn));
                        startActivity(intent);
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CharSequence[] item = {"BlueTooth", "NFC", "WIFI"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Send Contact Via");
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemposition) {
                        switch (itemposition) {
                            case 0:
                                Toast.makeText(getApplicationContext(), item[itemposition], Toast.LENGTH_SHORT).show();

                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), item[itemposition], Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), item[itemposition], Toast.LENGTH_SHORT).show();
                                break;
                        }


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

    }

    public Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {

            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }

            }
        } finally {
            cursor.close();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contact_image, null);
        return bitmap;
    }

    public void ContactRetrive() {
        try {

            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            while (phones.moveToNext()) {
                String phoneName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String contactid = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                list.add(new CustomUser(phoneName, phoneNumber, openPhoto(Long.parseLong(contactid))));
                Collections.sort(list, new Comparator<CustomUser>() {
                    @Override
                    public int compare(CustomUser lhs, CustomUser rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

            }
            phones.close();
            adapter = new CustomAdapter<CustomUser>(MainActivity.this, list);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(adapter);
                }
            });


        } catch (Exception e) {

        }


    }

    public class DoBack extends AsyncTask<Void, Integer, Void> {

        ProgressDialog dialog;
        @Override
        protected Void doInBackground(Void... params) {
            ContactRetrive();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

