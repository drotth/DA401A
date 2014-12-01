package com.drotth.grumpychat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.firebase.client.Firebase;

import java.lang.reflect.Field;

public class MainActivity extends Activity implements GroupsFragment.OnGroupsInteractionListener {

    private static final String FIREBASE_URL = "https://testda401a.firebaseio.com";
    private Firebase firebase;
    private FragmentManager fragmentManager;
    protected ActionBar actionBar;
    private GroupsFragment groupsPage;
    private AboutFragment aboutPage;
    private ChatFragment chatPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase = new Firebase(FIREBASE_URL);
        actionBar = getActionBar();
        fragmentManager = getFragmentManager();

        // Force the Overflow Menu to show even on phones with dedicated menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception exc) {}

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        groupsPage = new GroupsFragment();
        fragmentTransaction.add(R.id.fragmentViewMain, groupsPage);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                aboutPage = new AboutFragment();
                fragmentTransaction.replace(R.id.fragmentViewMain, aboutPage);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                break;

            case R.id.action_log_out:
                firebase.unauth();
                Intent startIntent = new Intent(this, StartActivity.class);
                this.startActivity(startIntent);
                this.finish();
                break;

            case R.id.action_new_group:
                //create new group
                // TODO: should the groupfragment or the mainactivity have the groups?
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGroupClick(Group group) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        chatPage = ChatFragment.newInstance(group);
        fragmentTransaction.replace(R.id.fragmentViewMain, chatPage);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}
