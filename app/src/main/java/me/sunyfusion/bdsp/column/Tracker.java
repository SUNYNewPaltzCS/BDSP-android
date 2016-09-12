package me.sunyfusion.bdsp.column;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import me.sunyfusion.bdsp.db.BdspDB;
import me.sunyfusion.bdsp.hardware.GPS;
import me.sunyfusion.bdsp.state.Config;
import me.sunyfusion.bdsp.state.Global;

/**
 * Created by deisingj1 on 8/8/2016.
 */
public class Tracker {
    GPS gps;
    Context c;
    Config config;
    BdspDB db = Global.getDb();

    /**
     * Instantiates new Tracker object
     * @param c the context of the activity that is using the tracker
     * @param config the current configuration of the application
     */
    public Tracker(Context c, Config config) {
        this.gps = config.getGps();
        gps.bindTracker(this);
        this.config = config;
        db.addColumn("latTrack", "TEXT");
        db.addColumn("longTrack", "TEXT");
    }

    /**
     * Inserts into the database the location information contained in the passed
     * Location object
     * @param l the Location object to be inserted
     */
    public void insertPoint(Location l) {
        ContentValues cv = new ContentValues();
        cv.put("latTrack", l.getLatitude());
        cv.put("longTrack", l.getLongitude());
        config.getId().insertValue(cv);
        cv.put("date", getDateString());
        config.getRun().checkDate();
        config.getRun().insertValueNoInc(cv);
        db.insert(cv);
    }

    /**
     * Returns the current date and time in string form
     * @return a date string for "now" in the format "yyyy-MM-dd H:m:s"
     */
    private String getDateString() {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        d.setTimeZone(TimeZone.getDefault());
        String cdt = d.format(new java.util.Date());
        return cdt;
    }

}
