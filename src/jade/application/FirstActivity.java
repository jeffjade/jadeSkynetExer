package jade.application;

import com.example.jadeskynetexer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class FirstActivity extends Activity{
	private float cMoveSpace = 100;
	private int viewFilperPageNum = 0;
 	private ViewFlipper viewFlipper = null;
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	
	private Animation moveInLeftToRight = null;
	private Animation moveOutLeftToRight = null;
	
	private Animation moveInRightToLeft = null;
	private Animation moveOutRightToLeft = null;
	private float startX = 0;
	private float startY = 0;
	private float endX , endY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_activity);
		viewFlipper = (ViewFlipper)findViewById(R.id.FirstViewFlipper);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		imageView2 = (ImageView)findViewById(R.id.imageView2);
		
		moveInLeftToRight = AnimationUtils.loadAnimation(this, R.anim.move_in_left_to_right);
		moveOutLeftToRight = AnimationUtils.loadAnimation(this, R.anim.move_out_left_to_right);;
	
		moveInRightToLeft = AnimationUtils.loadAnimation(this, R.anim.move_in_right_toleft);
		moveOutRightToLeft = AnimationUtils.loadAnimation(this, R.anim.move_out_right_to_left);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void onEnterAppClick(View v){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.move_in_left_to_right, R.anim.move_out_right_to_left);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		if(event.getAction() == MotionEvent.ACTION_DOWN) { 
			Log.v("jade","ACTION_DOWN x = "+startX + " y = "+startY); 
			startX = event.getX();
			startY = event.getY();
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) { 
			Log.v("jade","ACTION_UP x = "+startX + " y = "+startY);
			endX = event.getX();
			endY = event.getY();
			if(startX - endX >= cMoveSpace){
				if(viewFilperPageNum < 1){
					viewFlipper.setInAnimation(moveInRightToLeft);
					viewFlipper.setOutAnimation(moveOutRightToLeft);
					
					viewFlipper.showNext();
					viewFilperPageNum = 1;
				}
			}else if(endX - startX >= cMoveSpace){
				if(viewFilperPageNum >= 1){
					viewFlipper.setInAnimation(moveInLeftToRight);
					viewFlipper.setOutAnimation(moveOutLeftToRight);
					
					viewFlipper.showPrevious();
					viewFilperPageNum = 0;
				}
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
}