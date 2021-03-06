package edu.wit.mobileapp.instagredients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        // drawable layout
        dl = (DrawerLayout) findViewById(R.id.activity_base);
        // action bar drawer toggle
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);

        // navigation bar toggle
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id)
                {
                    case R.id.home:
                        Toast.makeText(BaseActivity.this, "Home",Toast.LENGTH_SHORT).show();
                        intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.all:
                        intent = new Intent(BaseActivity.this, AllRecipesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.saved:
                        Toast.makeText(BaseActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                        intent = new Intent(BaseActivity.this, ViewSavedActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(BaseActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}