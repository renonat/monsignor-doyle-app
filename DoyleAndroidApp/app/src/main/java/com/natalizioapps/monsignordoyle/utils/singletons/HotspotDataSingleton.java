package com.natalizioapps.monsignordoyle.utils.singletons;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Reno on 15-01-04.
 */
public class HotspotDataSingleton {
    //TODO: COMMENT
    private static HotspotDataSingleton ourInstance = null;

    private List<int[]> hotspots = null;

    private List<String> roomNames = null;

    public static HotspotDataSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new HotspotDataSingleton();
        }
        return ourInstance;
    }

    public void Initialize(Context c) {
        if (hotspots == null || roomNames == null) {
            try {
                AssetManager am = c.getAssets();
                InputStream inputStream = am.open("roomdata.txt");
                Scanner scan = new Scanner(inputStream);

                roomNames = new ArrayList<>();
                hotspots = new ArrayList<>();

                while (scan.hasNextLine()) {
                    String[] line = scan.nextLine().split(",");
                    roomNames.add(line[0]);

                    int[] data = new int[]{
                            Integer.parseInt(line[1]),
                            Integer.parseInt(line[2]),
                            Integer.parseInt(line[3]),
                            Integer.parseInt(line[4])};
                    hotspots.add(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HotspotDataSingleton() {
    }

    public List<int[]> getHotspots() {
        return hotspots;
    }

    public List<String> getRoomNames() {
        return roomNames;
    }
}
