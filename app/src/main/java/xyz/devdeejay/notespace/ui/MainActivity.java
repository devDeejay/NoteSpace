package xyz.devdeejay.notespace.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.devdeejay.notespace.R;
import xyz.devdeejay.notespace.database.NoteEntity;
import xyz.devdeejay.notespace.utilities.Constants;
import xyz.devdeejay.notespace.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    MainViewModel viewModel;
    private String TAG = "MainActivity_DJ";
    private List<NoteEntity> notesData = new ArrayList<>();
    private NotesAdapter adapter;
    private int layoutType;

    @OnClick(R.id.edit_note_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Order of Initialization is important
        readFromSharedPreferences();
        ButterKnife.bind(this);
        initRecyclerView(layoutType);
        initViewModel();

    }

    private void readFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(Constants.SharedPreference, MODE_PRIVATE);
        layoutType = prefs.getInt(Constants.LayoutTypeKey, 3);
        Log.d(TAG, "Read " + layoutType);
    }

    private void writeDataToSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SharedPreference, MODE_PRIVATE).edit();
        editor.putInt(Constants.LayoutTypeKey, layoutType);
        Log.d(TAG, "Wrote " + layoutType);
        editor.apply();
    }

    private void initViewModel() {

        final android.arch.lifecycle.Observer<List<NoteEntity>> notesObserver = new android.arch.lifecycle.Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                notesData.clear();
                notesData.addAll(noteEntities);

                if (adapter == null) {
                    adapter = new NotesAdapter(notesData, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        };

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);       // Initializing the view-model
        viewModel.notesData.observe(this, notesObserver);                       // Adding the observer to the view-model
    }

    private void initRecyclerView(int layoutType) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = null;
        // 1 - Simple Linear Layout
        // 2 - Grid With Equal Spaces
        // 3 - Staggered Grid

        switch (layoutType) {
            case 1:
                layoutManager = new GridLayoutManager(this, 2);
                break;

            case 2:
                layoutManager = new StaggeredGridLayoutManager(2, 1);
                break;

            default:
                layoutManager = new LinearLayoutManager(this);
                break;
        }

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete_sample_data) {
            Log.d(TAG, "Deleting Sample Data");
            deleteAllNotes();
            return true;
        } else if (id == R.id.action_change_layout) {
            Log.d(TAG, "Changing Layout");
            changeRecyclerViewLayout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeRecyclerViewLayout() {

        final CharSequence[] items = {"Simple List", "Equal Grid", "Unequal Grid"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        layoutType = 0;
                        initRecyclerView(layoutType);
                        writeDataToSharedPreferences();
                        break;
                    case 1:
                        layoutType = 1;
                        initRecyclerView(layoutType);
                        writeDataToSharedPreferences();
                        break;
                    case 2:
                        layoutType = 2;
                        initRecyclerView(layoutType);
                        writeDataToSharedPreferences();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        Log.d(TAG, "Changing Layout to type " + layoutType);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.github) {
            Log.d(TAG, "Open Github Repository");
            openGithubPage();
            return true;
        } else if (id == R.id.medium) {
            Log.d(TAG, "Open Medium Link");
            openMediumPage();
            return true;
        } else if (id == R.id.nav_share) {
            showSnackbarMessage("Thanks a lot for sharing, We appreciate it!");
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey! Check Out this simple yet beautiful app called 'Note Space'" + getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_send) {
            showSnackbarMessage("Opening Your Email App");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For NoteSpace");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey devDeejay!");
            emailIntent.setData(Uri.parse("mailto:delhidev10@gmail.com"));
            startActivity(emailIntent);

        } else if (id == R.id.rate) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Opening Google Play Store", Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            startActivity(rateIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openMediumPage() {
        OpenWebViewActivity("https://medium.com", "Medium");
    }

    private void openGithubPage() {
        OpenWebViewActivity("https://github.com/dhananjaytrivedi", "Github");
    }

    private void OpenWebViewActivity(String url, String title) {
        Intent i = new Intent(MainActivity.this, WebViewActivity.class);
        i.putExtra(Constants.URL, url);
        i.putExtra(Constants.TITLE, title);
        startActivity(i);
    }

    private void deleteAllNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("This operation will be irreversible");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do something
                viewModel.deleteAllNotes();
                showSnackbarMessage("All data deleted");
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showSnackbarMessage(String messageText) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), messageText, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void addSampleData() {
        viewModel.addSampleData();
    }
}
