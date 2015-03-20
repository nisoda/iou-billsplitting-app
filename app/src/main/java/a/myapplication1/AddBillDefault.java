package a.myapplication1;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class AddBillDefault extends ActionBarActivity {

    private ArrayList<String> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new ArrayList<String>();
        setContentView(R.layout.activity_add_bill_default);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_bill_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void test(View view) {
        Intent intent = new Intent(this, ViewFriendsDefault.class);
        startActivity(intent);
    }

    public void showFriendsPopUp(View view) {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.dropDownListView));

        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.dropDownRelLayout);
        final PopupWindow pw = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        pw.setOutsideTouchable(true);
        pw.setHeight(LayoutParams.WRAP_CONTENT);

        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        pw.dismiss();
                        return true;
                    }
                    return false;
                }
            }
        });

        pw.setContentView(layout);

        pw.showAsDropDown(layout1);

//        final ListView list = (ListView)layout.findViewById(R.)

    }
}
