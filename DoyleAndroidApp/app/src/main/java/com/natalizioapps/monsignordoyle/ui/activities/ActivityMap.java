package com.natalizioapps.monsignordoyle.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.utils.singletons.HotspotDataSingleton;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;
import com.qozix.tileview.TileView;
import com.qozix.tileview.hotspots.HotSpot;
import com.qozix.tileview.hotspots.HotSpotEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reno on 14-09-08.
 */
public class ActivityMap extends BaseActivity implements HotSpotEventListener {

    private TileView tileView;

    private String currentMap = "blank";

    private ImageView currentMarker;
    private List<ImageView> currentClasses;

    private Drawable pin;

    private PrefSingleton prefs;

    private List<int[]> hotspots = null;

    private List<String> roomNames = null;

    private RelativeLayout roomDetailView;
    private TextView roomDetailText;

    private List<ImageView> washrooms;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_map));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        prefs = PrefSingleton.getInstance();

        // Initialize the arrays
        hotspots = HotspotDataSingleton.getInstance().getHotspots();
        roomNames = HotspotDataSingleton.getInstance().getRoomNames();
        currentClasses = new ArrayList<>();
        washrooms = new ArrayList<>();

        // Set up the tileview
        tileView = new TileView(this);
        tileView.setSize(3000, 1880);

        refreshMapDetailLevels();

        tileView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tileView.setScale(0);

        frameTo(0.5, 0.5);

        FrameLayout fl = (FrameLayout) findViewById(R.id.container);
        fl.addView(tileView);

        // The view at the bottom with class details
        roomDetailView = (RelativeLayout) findViewById(R.id.room_detail_view);
        roomDetailText = (TextView) findViewById(R.id.room_detail_text);

        // Set up the pin drawable
        pin = getResources().getDrawable(R.drawable.ic_pin);
        pin.setColorFilter(getResources().getColor(R.color.material_red_500),
                PorterDuff.Mode.MULTIPLY);

        // Add the room hotspots to the map
        if (hotspots != null && roomNames != null) {
            for (int i = 0; i < hotspots.size(); i++) {
                int[] coords = hotspots.get(i);
                HotSpot h = new HotSpot(coords[0], coords[1], coords[2], coords[3]);
                int[] tag = new int[3];
                tag[0] = i;
                tag[1] = (coords[2] - coords[0]) / 2 + coords[0];
                tag[2] = (coords[3] - coords[1]) / 2 + coords[1];
                h.setTag(tag);
                h.setHotSpotEventListener(this);
                tileView.addHotSpot(h);
            }
        }

        // Refresh the current classes and washrooms
        refreshClasses();
        refreshWashrooms();

        // If we have previously saved the state of the marker, set it as the marker
        if (savedInstanceState != null) {
            refreshMarker(savedInstanceState.getIntArray("currentMarker"));
        }
    }

    private void refreshWashrooms() {
        if (prefs.getShowWashrooms()) {
            // Load the washroom images if we have not alreayd done so
            if (washrooms.isEmpty()) {
                // Add the single handicap washroom
                Drawable handicap = getResources().getDrawable(R.drawable.ic_wheelchair);
                ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#ff2196f3"),
                        PorterDuff.Mode.MULTIPLY);
                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(handicap);
                imageView.setTag(new int[]{837, 988});
                imageView.setColorFilter(cf);
                washrooms.add(imageView);

                // Add teh coordinates for the normal washrooms
                List<int[]> washroomData = new ArrayList<int[]>();
                washroomData.add(new int[]{1004, 942});
                washroomData.add(new int[]{471, 263});
                washroomData.add(new int[]{1660, 929});
                washroomData.add(new int[]{2556, 973});
                washroomData.add(new int[]{2546, 1622});

                // Add the five normal washrooms
                for (int[] data : washroomData) {
                    Drawable washroom = getResources().getDrawable(R.drawable.ic_toilet);
                    imageView = new ImageView(this);
                    imageView.setImageDrawable(washroom);
                    imageView.setTag(data);
                    imageView.setColorFilter(cf);
                    washrooms.add(imageView);
                }
            }
            // We now have the current washrooms, so add them as Markers to the TileView
            for (ImageView i : washrooms) {
                int[] data = (int[]) i.getTag();
                tileView.addMarker(i, data[0], data[1], -0.5f, -0.5f);
            }
        } else {
            // Washrooms are not to be shown, so remove each marker from the tileview
            for (ImageView i : washrooms) {
                tileView.removeMarker(i);
            }
        }
    }

    private void refreshClasses() {
        if (hotspots != null && roomNames != null && prefs.getShowClasses()) {
            // Only execute if classes are to be shown
            if (currentClasses.isEmpty()) {
                // Only execute if we have not already found the current classes
                for (int i = 1; i < 5; i++) {
                    // For each of our 4 classes
                    SchoolClass c = prefs.getClass(i);
                    for (int j = 0; j < roomNames.size(); j++) {
                        // Check if the room matches against our list of rooms
                        String room = roomNames.get(j);
                        if (!c.getRoom().isEmpty() &&
                                room.toLowerCase().contains(c.getRoom().toLowerCase())) {
                            // Create the ImageView marker and add it to the classes array
                            Drawable place = getResources().getDrawable(R.drawable.ic_place);
                            ColorFilter cf = new PorterDuffColorFilter(c.getColor(),
                                    PorterDuff.Mode.MULTIPLY);
                            int[] coords = hotspots.get(j);
                            int x = (coords[2] - coords[0]) / 2 + coords[0];
                            int y = (coords[3] - coords[1]) / 2 + coords[1];
                            int[] data = new int[]{x, y};
                            ImageView imageView = new ImageView(this);
                            imageView.setImageDrawable(place);
                            imageView.setTag(data);
                            imageView.setColorFilter(cf);
                            currentClasses.add(imageView);
                            j = roomNames.size();
                        }
                    }
                }
            }
            // We now have the current classes, so add them as Markers to the TileView
            for (ImageView i : currentClasses) {
                int[] data = (int[]) i.getTag();
                tileView.addMarker(i, data[0], data[1], -0.5f, -1.0f);
            }
        } else {
            // Classes are nto to be shown, so remove each marker from the tileview
            for (ImageView i : currentClasses) {
                tileView.removeMarker(i);
            }
        }
    }

    private void refreshMapDetailLevels() {
        // Change the tileset based on the preferences
        if (prefs.getShowRooms() && prefs.getShowLockers()) {
            currentMap = "roomsandlockers";
        } else if (prefs.getShowRooms()) {
            currentMap = "rooms";
        } else if (prefs.getShowLockers()) {
            currentMap = "lockers";
        } else {
            currentMap = "blank";
        }

        // Remove the current detail levels, then re-add them with the new tileset
        tileView.resetDetailLevels();

        tileView.addDetailLevel(1.00f, "tiles/" + currentMap + "/1000/%col%-%row%.jpg",
                "tiles/" + currentMap + "/1000/downsampled", 250, 250);
        tileView.addDetailLevel(0.50f, "tiles/" + currentMap + "/500/%col%-%row%.jpg",
                "tiles/" + currentMap + "/500/downsampled", 250, 250);

        tileView.refresh();
    }

    private void refreshMarker(int[] tag) {
        if (currentMarker != null) {
            tileView.removeMarker(currentMarker);
        }
        if (tag != null) {
            currentMarker = new ImageView(this);

            currentMarker.setTag(tag);

            currentMarker.setImageDrawable(pin);
            tileView.addMarker(currentMarker, tag[1], tag[2], -0.5f, -1.0f);

            roomDetailText.setText(roomNames.get(tag[0]));
            roomDetailView.setVisibility(View.VISIBLE);

            frameTo(tag[1], tag[2]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_map, menu);

        // Set the preferences to be initially checked based on the preferences
        menu.findItem(R.id.action_rooms).setChecked(prefs.getShowRooms());
        menu.findItem(R.id.action_lockers).setChecked(prefs.getShowLockers());
        menu.findItem(R.id.action_classes).setChecked(prefs.getShowClasses());
        menu.findItem(R.id.action_washrooms).setChecked(prefs.getShowWashrooms());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Class root = ActivityAbout.class;
            startActivity(new Intent(ActivityMap.this, root).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        } else if (id == R.id.action_rooms) {
            item.setChecked(!item.isChecked());
            prefs.setShowRooms(item.isChecked());
        } else if (id == R.id.action_lockers) {
            item.setChecked(!item.isChecked());
            prefs.setShowLockers(item.isChecked());
        } else if (id == R.id.action_classes) {
            item.setChecked(!item.isChecked());
            prefs.setShowClasses(item.isChecked());
            refreshClasses();
        } else if (id == R.id.action_washrooms) {
            item.setChecked(!item.isChecked());
            prefs.setShowWashrooms(item.isChecked());
            refreshWashrooms();
        }

        refreshMapDetailLevels();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Class root = ActivityAbout.class;
        startActivity(new Intent(ActivityMap.this, root).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }

    /**
     * This is a convenience method to moveToAndCenter after layout (which won't happen if called
     * directly in onCreate
     * see https://github.com/moagrius/TileView/wiki/FAQ
     */
    public void frameTo(final double x, final double y) {
        tileView.post(new Runnable() {
            @Override
            public void run() {
                tileView.moveToAndCenter(x, y);
            }
        });
    }

    @Override
    public void onHotSpotTap(HotSpot hotSpot, int x, int y) {
        refreshMarker((int[]) hotSpot.getTag());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentMarker != null) {
            outState.putIntArray("currentMarker", (int[]) currentMarker.getTag());
        }
        super.onSaveInstanceState(outState);
    }
}
