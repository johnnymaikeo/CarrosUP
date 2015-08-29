package br.com.up.carrosup.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import br.com.up.carrosup.R;
import br.com.up.carrosup.dialog.AboutDialog;
import br.com.up.carrosup.fragment.CarrosFragment;
import br.com.up.carrosup.fragment.CarrosTabFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        setupNavDrawer(this);

        if(savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            CarrosTabFragment frag = new CarrosTabFragment();
            fm.beginTransaction().add(R.id.CarrosFragment,frag,"CarrosTabFragment").commit();
        }
    }

    private void setupViewPagerTabs() {
        // ViewPager
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(adapter);

        // Tabs
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_carros:
                toast("carros");
                return true;
            case R.id.nav_item_config:
                toast("config");
                return true;
            case R.id.nav_item_sobre:
                startActivity(new Intent(this,SobreActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AboutDialog.showAbout(getSupportFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
