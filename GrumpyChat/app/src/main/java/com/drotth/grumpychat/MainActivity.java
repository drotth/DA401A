package com.drotth.grumpychat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

public class MainActivity extends Activity {

    FragmentManager fragmentManager;
    ActionBar actionBar;
    ListFragment groupsPage;
    AboutFragment aboutPage;

    //Placeholder group list names
    //String[] group_names = getResources().getStringArray(R.array.groups_array);
    String[] group_test = {"Project P2", "Exam work", "DA401A team", "Family"};

    private AdapterView.OnItemClickListener onGroupClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // TODO: do something
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();

        // Force the Overflow Menu to show even on phones with dedicated menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception exc) {}

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        groupsPage = new ListFragment();
        fragmentTransaction.add(R.id.fragmentViewMain, groupsPage);
        fragmentTransaction.commit();

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, group_test);

        ListView groupsList = (ListView) findViewById(R.id.groupsListView);
        groupsList.setAdapter(listAdapter); // TODO: Why nullpointerexception?
        groupsList.setOnItemClickListener(onGroupClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            if (aboutPage == null || !aboutPage.isAdded()){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                aboutPage = new AboutFragment();
                fragmentTransaction.replace(R.id.fragmentViewMain, aboutPage);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
