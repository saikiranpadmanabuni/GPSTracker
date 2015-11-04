package tracker.gps.com.gpstracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.Iterator;

public class CustomGraphView extends View {
	private Paint greenPaint = new Paint();
	private Paint redPaint = new Paint();
	private Paint whitePaint = new Paint();
	private Context mContext;
	private int screenWidth;

	public CustomGraphView(Context context) {
		super(context);
		init(context);
	}

	public CustomGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int minheight = screenWidth / 7;
		canvas.drawLine(0, minheight, screenWidth, minheight, whitePaint);
		canvas.drawLine(0, minheight * 2, screenWidth, minheight * 2, whitePaint);
		canvas.drawLine(0, minheight * 3, screenWidth, minheight * 3, whitePaint);
		canvas.drawLine(0, minheight * 4, screenWidth, minheight * 4, whitePaint);
		canvas.drawLine(0, minheight * 5, screenWidth, minheight * 5, whitePaint);
		canvas.drawLine(0, minheight * 6, screenWidth, minheight * 6, whitePaint);

		int xPosition = 0;
		int yPosition = screenWidth;
		int xStopPosition = screenWidth / 100;
		int yStopPosition;
		int conversion = screenWidth / 70;

		Iterator<Integer> iter = MainActivity.speed.iterator();
		while (iter.hasNext()) {
			int currentSpeed = iter.next();
			yStopPosition = screenWidth - currentSpeed * conversion;
			canvas.drawLine(xPosition, yPosition, xStopPosition, yStopPosition, greenPaint);
			xPosition = xStopPosition;
			yPosition = yStopPosition;
			xStopPosition = xStopPosition + (screenWidth / 100);
		}
		int averagepostion = screenWidth - MainActivity.averageSpeed * conversion;
		canvas.drawLine(0, averagepostion, screenWidth, averagepostion, redPaint);
		super.onDraw(canvas);
	}

	/**
	 * Initializing required members to draw graph
	 * @param context
	 */
	private void init(Context context) {
		mContext = context;
		WindowManager wm = (WindowManager) mContext
				.getSystemService(mContext.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStrokeWidth(2);
		whitePaint.setStyle(Paint.Style.STROKE);
		redPaint.setColor(Color.RED);
		redPaint.setStrokeWidth(2);
		greenPaint.setColor(Color.GREEN);
		greenPaint.setStrokeWidth(2);

	}

}
