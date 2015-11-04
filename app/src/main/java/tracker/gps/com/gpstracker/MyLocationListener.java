package tracker.gps.com.gpstracker;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener {

	private final Context mContext;

	public MyLocationListener(Context context) {
		mContext = context;
	}

	@Override
	public void onLocationChanged(Location loc) {
		if (loc != null) {
			setlocationSpeed(loc);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * Passes the current speed to registered Activity
	 *
	 * @param loc
	 */
	private void setlocationSpeed(Location loc) {
		int speedPerHour = (int) (loc.getSpeed() * 3.6);
		((MainActivity) mContext).setSpeed(speedPerHour);

	}

}
