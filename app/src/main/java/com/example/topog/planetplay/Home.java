package com.example.topog.planetplay;
import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContentResolverCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Songs> arrayList;
    private ProfilePictureView profilePictureView;
    private TextView userName,email;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        arrayList = new ArrayList<Songs>();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        permissions();
        SetMusics();

    }
    public void SetMusics() {
        ListView lista = (ListView) findViewById(R.id.MusicList);
        Adapter adapter= new Adapter(this,arrayList);
        lista.setAdapter(adapter);



    }
        public static int pxToDp(int px)
    {
        return ((int)(px/ Resources.getSystem().getDisplayMetrics().density));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        Bundle bundle = getIntent().getExtras();
        String status = bundle.getString("Fb");
        if (status != "no")
        {
            profilePictureView.setProfileId(bundle.getString("ID"));
            String name = sp.getString("name",null);
            String emailuser = sp.getString("email",null);
            Toast.makeText(this,name,Toast.LENGTH_LONG).show();
            userName = (TextView) findViewById(R.id.usernamehome);
            userName.setText(name);
            email = (TextView) findViewById(R.id.emailuserhome);
            email.setText(emailuser);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.logout) {
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token",null);
            editor.putString("name",null);
            editor.putString("email",null);
            editor.apply();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean permissions()
    {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            getAllMusic();
            return true;
        }

    }


    private  void getAllMusic()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            do {
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String sonArtist = songCursor.getString(songArtist);
                arrayList.add(new Songs(currentId, currentTitle,sonArtist));
            } while(songCursor.moveToNext());
        }else
        {
            Log.i("mira","não dá");
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
           getAllMusic();
            //resume tasks needing this permission
        }
    }
}
