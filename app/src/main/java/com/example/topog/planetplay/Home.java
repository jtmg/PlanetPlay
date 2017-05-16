package com.example.topog.planetplay;
import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.gesture.Gesture;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import java.util.ArrayList;
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Songs> arrayList;
    private ProfilePictureView profilePictureView;
    private TextView userName,email;
    private MediaPlayer mediaPlayer;
    private int pos;
    private  int isPaused;
    SharedPreferences sp;
    private int currentPos;
    private GestureDetector detector ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        arrayList = new ArrayList<Songs>();
        pos = 0;
        ImageView play = (ImageView) findViewById(R.id.playerStart);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying())
                {
                    currentPos = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }else
                {
                    mediaPlayer.seekTo(currentPos);
                    mediaPlayer.start();
                }
            }
        });
        Button next = (Button) findViewById(R.id.forwards);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == arrayList.size())
                {
                    pos = 0;
                }
                if (pos == 0)
                {
                    mediaPlayer = new MediaPlayer ();
                    Uri uri = Uri.parse(arrayList.get(pos).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                }else{
                    mediaPlayer.stop();
                    Uri uri = Uri.parse(arrayList.get(pos).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                }


                pos += 1;

            }
        });

        Button back = (Button) findViewById(R.id.backwards);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 0)
                {
                    pos = arrayList.size() -1;
                    mediaPlayer = new MediaPlayer ();
                    Uri uri = Uri.parse(arrayList.get(pos).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                }else{
                    mediaPlayer.stop();
                    Uri uri = Uri.parse(arrayList.get(pos).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                }

                pos -= 1;

            }
        });
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        return super.onTouchEvent(event);
        // Return true if you have consumed the event, false if you haven't.
        // The default implementation always returns false.
    }

    class Gestures_android implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener
    {


        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("Gesture ", " onDown");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("Gesture ", " onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("Gesture ", " onSingleTapUp");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("Gesture ", " onShowPress");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("Gesture ", " onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("Gesture ", " onDoubleTapEvent");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Gesture ", " onLongPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            //mediaPlayer = new MediaPlayer();
            Log.d("Gesture ", " onScroll");
            if (e1.getY() < e2.getY()){
                Log.d("Gesture ", " Scroll Down");

            }
            if(e1.getY() > e2.getY()){
                Log.d("Gesture ", " Scroll Up");
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() < e2.getX()) {
                Log.d("Gesture ", "Left to Right swipe: "+ e1.getX() + " - " + e2.getX());
                Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
                if (mediaPlayer != null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        int fastForward = mediaPlayer.getCurrentPosition();
                        fastForward += 5000;
                        mediaPlayer.seekTo(fastForward);
                    }
                }



            }
            if (e1.getX() > e2.getX()) {
                Log.d("Gesture ", "Right to Left swipe: "+ e1.getX() + " - " + e2.getX());
                Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
                if( mediaPlayer != null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        int backwards = mediaPlayer.getCurrentPosition();
                        backwards -= 5000;
                        mediaPlayer.seekTo(backwards);
                    }

                }


            }

            return true;

        }
    }
    public void SetMusics() {
        final ListView lista = (ListView) findViewById(R.id.MusicList);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Adapter adapter= new Adapter(this,arrayList);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    Uri aux = Uri.parse(arrayList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), aux);
                    mediaPlayer.start();
                    pos = position;
                }else
                {
                    if (position == pos && mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        isPaused = mediaPlayer.getCurrentPosition();
                    }else if(position == pos && !mediaPlayer.isPlaying())
                    {
                        mediaPlayer.seekTo(isPaused);
                        mediaPlayer.start();
                    }else
                    {
                        mediaPlayer.pause();
                        Uri aux = Uri.parse(arrayList.get(position).getPath());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), aux);
                        mediaPlayer.start();
                        pos = position;
                    }
                }

            }
        });
        final GestureDetector gd = new GestureDetector(getApplication(),new Gestures_android());
        View.OnTouchListener gl = new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        };
        lista.setOnTouchListener(gl);
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
        int id = item.getItemId();
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
            int path = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String sonArtist = songCursor.getString(songArtist);
                String path1 = songCursor.getString(path);
                arrayList.add(new Songs(currentId, currentTitle,sonArtist,path1));
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
