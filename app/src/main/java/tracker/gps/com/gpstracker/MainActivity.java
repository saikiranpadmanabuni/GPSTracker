package tracker.gps.com.gpstracker;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

	private CustomGraphView graphView;
	private Button trackButton;
	private boolean isTracking;
	private MyLocationListener myLocListener;
	private LocationManager locationManager;
	public static Queue<Integer> speed  = new LinkedList<Integer>();
	private TextView cureentSpeedTextView;
	private TextView AverageTextView;
	private TextView timeTextView;
	public static int averageSpeed = 0;
	private int count = 0;
	private int totalSpeed = 0;
	private int sec = 0;
	Timer t ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		graphView = (CustomGraphView)findViewById(R.id.graphview);
		trackButton = (Button) findViewById(R.id.button);
		cureentSpeedTextView = (TextView) findViewById(R.id.textView1);
		AverageTextView = (TextView) findViewById(R.id.textView2);
		timeTextView = (TextView) findViewById(R.id.textView3);
		trackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// If tracking is not started
				if(!isTracking){
					trackButton.setText(getString(R.string.stop_tracking));
					isTracking = true;
					startLocationListening();
					speed  = new LinkedList<Integer>();
					startTimer();
				}
				// Stops the tracker, if it is started.
				else {
					trackButton.setText(getString(R.string.start_tracking));
					stopLocationListening();
					isTracking = false;
					stoptimer();
					totalSpeed = 0;
					count = 0;
					sec = 0;
					averageSpeed = 0;
				}
			}
		});

	}

	/**
	 * Stops the timer
	 */
	private void stoptimer() {
		t.cancel();
	}

	/**
	 * Stops to listen location change
	 * @throws SecurityException
	 */
	private void stopLocationListening() throws SecurityException{
		locationManager.removeUpdates(myLocListener);
		locationManager = null;
		graphView.invalidate();
	}

	/**
	 * Starts to listen location change
	 * @throws SecurityException
	 */
	private void startLocationListening() throws SecurityException {
		int minTime = 1000;
		float minDistance = 5;
		myLocListener = new MyLocationListener(MainActivity.this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(true);
		String bestProvider = locationManager.getBestProvider(criteria, false);
		locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, myLocListener);
	}

	/**
	 * Starts the timer and update UI on every second
	 */
	private void startTimer() {
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				sec++;
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						timeTextView.setText(getString(R.string.overall_time)+" "+sec+" s");
					}
				});
			}

		}, 0, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		WindowManager wm = (WindowManager) this
				.getSystemService(this.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
				graphView .getLayoutParams();
		params.height = display.getWidth();
		graphView .setLayoutParams(params);
	}

	/**
	 * Sets the current speed and average speed, based on location listener
	 * @param speedPerHour
	 */
	public void setSpeed(int speedPerHour) {
		count++;
		totalSpeed = totalSpeed + speedPerHour;
		cureentSpeedTextView.setText(getString(R.string.current_speed) + " " + speedPerHour + " " + getString(R.string.kmh));
		averageSpeed = totalSpeed / count;
		AverageTextView.setText(getString(R.string.average_speed) + " " + averageSpeed + " " + getString(R.string.kmh));
		graphView.invalidate();
		speed.add(speedPerHour);
		if (speed.size() > 100) {
			speed.poll();
		}
	}

}
