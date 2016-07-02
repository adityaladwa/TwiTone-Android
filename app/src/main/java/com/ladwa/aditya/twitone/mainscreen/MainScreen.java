package com.ladwa.aditya.twitone.mainscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * This is The Launcher Activity of the App
 */
public class MainScreen extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawer();

    }

    private void setupDrawer() {

        final PrimaryDrawerItem timeline = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_timeline).withBadge("20");
        final PrimaryDrawerItem interaction = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_interaction);
        final PrimaryDrawerItem message = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_message);
        final PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_setting);
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        timeline,
                        interaction,
                        message,
                        new DividerDrawerItem(),
                        settings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(MainScreen.this, "Selected on " + position, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .build();
    }

}
