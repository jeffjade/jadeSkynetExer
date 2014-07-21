package jade.application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.jadeskynetexer.R;
import com.example.jadeskynetexer.R.id;
import com.jade.helper.TabHostActivity;

import android.app.Activity;
import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class CommunicateActivity extends Activity {
	private Button chatButton = null;
	private Button findButton = null;
	private Button contactsButton = null;
	
	private EditText editChatText = null;
	private TextView chatTextView = null;
	private Button sendChatBtn = null;
	
	private ViewFlipper viewFlipper = null;
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	private ImageView imageView3 = null;

	private float cMoveSpace = 100;
	private int currentPageNum = 1;
	public int viewFilperCount = 3;

	private float startX = 0;
	private float startY = 0;
	private float endX, endY;
	
	private Animation moveInLeftToRight = null;
	private Animation moveOutLeftToRight = null;

	private Animation moveInRightToLeft = null;
	private Animation moveOutRightToLeft = null;
	
	private Socket mSocket;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				String text = (String) msg.obj;
				chatTextView.setText(text);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicate_activity);

		chatButton = (Button) findViewById(R.id.chatButton);
		findButton = (Button) findViewById(R.id.findButton);
		contactsButton = (Button) findViewById(R.id.contactsButton);
		
		sendChatBtn = (Button)findViewById(R.id.sendChatBtn);
		editChatText = (EditText)findViewById(R.id.editChatText);
		chatTextView = (TextView)findViewById(R.id.chatTextView);
		
		viewFlipper = (ViewFlipper) findViewById(R.id.OneViewFlipper);

		moveInLeftToRight = AnimationUtils.loadAnimation(this,
				R.anim.move_in_left_to_right);
		moveOutLeftToRight = AnimationUtils.loadAnimation(this,
				R.anim.move_out_left_to_right);
		moveInRightToLeft = AnimationUtils.loadAnimation(this,
				R.anim.move_in_right_toleft);
		moveOutRightToLeft = AnimationUtils.loadAnimation(this,
				R.anim.move_out_right_to_left);

		chatButton.setOnClickListener(new ButtonListener());
		findButton.setOnClickListener(new ButtonListener());
		contactsButton.setOnClickListener(new ButtonListener());
		
		/* ·¢ËÍÁÄÌìButton 2014.07.20 */
		sendChatBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if(mSocket != null){
						PrintWriter printWriter = new PrintWriter(mSocket.getOutputStream());	
						String text = editChatText.getText().toString();
						editChatText.setText("");
						
						if (!text.equals("")) {
							printWriter.println(text);
							printWriter.flush();
						}
					}else{
						Log.v("jade:", "jade mSocket is null !!!");
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mSocket = new Socket("192.168.200.86", 8888);
					Log.v("jade","jade start connect socket 192.168.200.86:8888");
					BufferedReader buff = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
					while (true) {
						String text = buff.readLine();
						Log.i("jade", "jade chat meaage-text = " + text);
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = text;				
						mHandler.sendMessage(msg);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int viewId = view.getId();
			switch (viewId) {
			case R.id.chatButton:
				showPageByNum(1);
				break;
			case R.id.findButton:
				showPageByNum(2);
				break;
			case R.id.contactsButton:
				showPageByNum(3);
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.v("jade", "ACTION_DOWN x = " + startX + " y = " + startY);
			startX = event.getX();
			startY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.v("jade", "ACTION_UP x = " + startX + " y = " + startY);
			endX = event.getX();
			endY = event.getY();
			if (startX - endX >= cMoveSpace) {
				if (currentPageNum != viewFilperCount) {
					showNextPage();
				}
			} else if (endX - startX >= cMoveSpace) {
				if (currentPageNum != 1 ) {
					showPreviousPage();
				}
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	private void showPageByNum(int num) {
		int diff = num - currentPageNum;
		if (diff < 0) {
			diff = Math.abs(diff);
			for (int i = 1; i <= diff; i++) {
				showPreviousPage();
			}
		} else if (diff > 0) {
			for (int i = 1; i <= diff; i++) {
				showNextPage();
			}
		}
	}

	/* viewFilper show Next page 2014.07.20 */
	private void showNextPage() {
		viewFlipper.setInAnimation(moveInRightToLeft);
		viewFlipper.setOutAnimation(moveOutRightToLeft);

		viewFlipper.showNext();
		currentPageNum += 1;
	}

	/* viewFilper show previous page 2014.07.20 */
	private void showPreviousPage() {
		viewFlipper.setInAnimation(moveInLeftToRight);
		viewFlipper.setOutAnimation(moveOutLeftToRight);

		viewFlipper.showPrevious();
		currentPageNum = currentPageNum - 1;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.communicate_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		Intent intent = null;
		switch(itemId){
			case R.id.itemMore: 
				intent = new Intent(this,MainActivity.class);
				break;
			case R.id.itemAbout:
				intent = new Intent(this,TabHostActivity.class);
				break;
		}
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
}
