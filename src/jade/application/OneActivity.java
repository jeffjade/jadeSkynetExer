package jade.application;

import com.example.jadeskynetexer.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class OneActivity extends Activity {
	private Button chatButton = null;
	private Button findButton = null;
	private Button contactsButton = null;
	
	private ViewFlipper viewFlipper = null;
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	private ImageView imageView3 = null;
	
	private float cMoveSpace = 100;
	private int viewFilperPageNum = 0;
	private float startX = 0;
	private float startY = 0;
	private float endX , endY;
	
	private Animation moveInLeftToRight = null;
	private Animation moveOutLeftToRight = null;
	
	private Animation moveInRightToLeft = null;
	private Animation moveOutRightToLeft = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		
		chatButton = (Button)findViewById(R.id.chatButton);
		findButton = (Button)findViewById(R.id.findButton);
		contactsButton = (Button)findViewById(R.id.contactsButton);
		
		viewFlipper = (ViewFlipper)findViewById(R.id.OneViewFlipper);
		
		moveInLeftToRight = AnimationUtils.loadAnimation(this, R.anim.move_in_left_to_right);
		moveOutLeftToRight = AnimationUtils.loadAnimation(this, R.anim.move_out_left_to_right);;
	
		moveInRightToLeft = AnimationUtils.loadAnimation(this, R.anim.move_in_right_toleft);
		moveOutRightToLeft = AnimationUtils.loadAnimation(this, R.anim.move_out_right_to_left);
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
