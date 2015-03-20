package a.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AddBillDefault extends ActionBarActivity {

    private ArrayList<String> friendsAdded;
    private SQLiteHelperFriends friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new SQLiteHelperFriends(this);
        friendsAdded = new ArrayList<String>();
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

    public void showFriendsPopUp(View view) {

        System.out.println("Attempting to add friends");
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.dropDownListView));

        final PopupWindow pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, 300, true);

        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6CA2BFF4")));
        pw.setTouchable(true);

        pw.setOutsideTouchable(true);
//        pw.setHeight(LayoutParams.WRAP_CONTENT);
//        pw.update(200, 100);

        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        pw.setContentView(layout);
        pw.showAsDropDown((TextView)findViewById(R.id.addedFriendsTextView), 10, 0);

        final ListView list = (ListView)layout.findViewById(R.id.dropDownListView);
        Cursor cursor = friends.getAllFriends();
        String[]from = new String[]{SQLiteHelperFriends.COLUMN_NAME, SQLiteHelperFriends.COLUMN_PHONE, SQLiteHelperFriends.COLUMN_EMAIL};
        int[]to = new int[]{R.id.friend_name};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_friend_entry, cursor, from, to);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        // OnCLickListiner For List Items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView friendSelectedTextView;

                friendSelectedTextView = (TextView) view.findViewById(R.id.friend_name);

                String friendSelectedString = friendSelectedTextView.getText().toString();

                if(!friendsAdded.contains(friendSelectedString))
                    friendsAdded.add(0, friendSelectedString);
                else
                    friendsAdded.remove(friendSelectedString);

                ((TextView)findViewById(R.id.addedFriendsTextView)).setText(friendsAdded.toString() + " added.");
            }
        });
    }
}
