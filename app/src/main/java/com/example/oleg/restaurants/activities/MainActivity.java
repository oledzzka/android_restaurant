package com.example.oleg.restaurants.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.oleg.restaurants.Fragments.ChooseCityFragment;
import com.example.oleg.restaurants.Fragments.FragmentWithoutCityCategory;
import com.example.oleg.restaurants.Fragments.RestaurantListFragment;
import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.adapters.AdapterCities;
import com.example.oleg.restaurants.database.CategoryEntry;
import com.example.oleg.restaurants.database.CurrentCityEntry;
import com.example.oleg.restaurants.models.Category;
import com.example.oleg.restaurants.models.City;
import com.example.oleg.restaurants.network.CitiesDownloader;
import com.example.oleg.restaurants.stores.CurrentCityStore;
import com.example.oleg.restaurants.stores.StoreCategory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterCities.OnItemClickListener {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Category currentCategory;
    private CitiesDownloader downloader;
    private MenuItem searchMenuItem;
    private final static String CURRENT_CATEGORY_ID = "current category id";
    private final static int noCurrentCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(darkTheme ? R.style.AppDarkTheme : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState != null) {
            int currentCategoryId = savedInstanceState.getInt(CURRENT_CATEGORY_ID);
            if (currentCategoryId != noCurrentCategoryId) {
                for (Category category: StoreCategory.initInstance().getCategories()) {
                    if (category.getId() == currentCategoryId) {
                        currentCategory = category;
                        break;
                    }
                }
            }
        } else {
            initCurrentItemsFromDB();
            initCategoryFragment();
        }
        initToolbar();
        initNavigationView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 1 && downloader != null) {
                    downloader.stopDownload();
                    downloader.startDownload(newText.trim());
                }
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                ChooseCityFragment fragment = new ChooseCityFragment();
                fragment.initAdapter(MainActivity.this);
                downloader = new CitiesDownloader(fragment);
                changeFragment(fragment);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                initCategoryFragment();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu navigationMenu = navigationView.getMenu();

        // Search and populate the menu with acceptable offering applications.
        navigationView.setNavigationItemSelectedListener(this);
        for (Category category: StoreCategory.initInstance().getCategories()) {
            navigationMenu.add(R.id.category_group, 0,0,category.getName());
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        setCityTitle();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        return toolbar;
    }

    private void setCityTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        City currentCity = CurrentCityStore.initInstance().getCity();
        if (currentCity == null) toolbar.setTitle(R.string.choose_city);
        else toolbar.setTitle(getString(R.string.city) + ": " + currentCity.getName());
    }



    private void initCurrentItemsFromDB() {
        City city = CurrentCityEntry.getCurrentCity(this);
        if (city != null) {
            CurrentCityStore.initInstance().setCity(city);
        }
        currentCategory = CategoryEntry.getCurrentCategory(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_restaurant_list, fragment);
        fragmentTransaction.commit();
    }

    private void initCategoryFragment() {
        if (CurrentCityStore.initInstance().getCity() != null && currentCategory != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(RestaurantListFragment.CATEGORY_ID, currentCategory.getId());
            RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
            restaurantListFragment.setArguments(bundle);
            changeFragment(restaurantListFragment);
        } else changeFragment(new FragmentWithoutCityCategory());
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        for (Category category: StoreCategory.initInstance().getCategories()) {
            if (category.getName().equals(item.getTitle())) {
                currentCategory = category;
                break;
            }
        }
        initCategoryFragment();
        ((DrawerLayout) findViewById(R.id.drawer_layout_main)).closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }


    @Override
    public void onCityClick(City city) {
        CurrentCityStore.initInstance().setCity(city);
        setCityTitle();
        searchMenuItem.collapseActionView();
        initCategoryFragment();
    }

    @Override
    protected void onStop() {
        CurrentCityEntry.setCurrentCity(this, CurrentCityStore.initInstance().getCity());
        CategoryEntry.setListCategory(this, StoreCategory.initInstance().getCategories());
        if (currentCategory != null ) {
            CategoryEntry.setCurrentCategory(this, currentCategory);
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentCategory != null){
            outState.putInt(CURRENT_CATEGORY_ID, currentCategory.getId());
        } else {
            outState.putInt(CURRENT_CATEGORY_ID, noCurrentCategoryId);
        }
        super.onSaveInstanceState(outState);
    }
}
