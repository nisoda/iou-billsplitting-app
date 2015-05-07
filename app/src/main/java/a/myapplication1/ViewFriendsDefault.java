package a.myapplication1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.database.Cursor;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;



public class ViewFriendsDefault extends ActionBarActivity {

    private SQLiteHelperFriends friends;
    ListView lv;
    static String friend_first_name, friend_last_name;
    TextView friend_name_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new SQLiteHelperFriends(this);

        String[] columns = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID
        };

        Cursor contactsCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, columns, null,null,null);

        int nameColumn = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        if(contactsCursor.moveToFirst()){
            do{
                //add contacts to friends table
                String name = contactsCursor.getString(nameColumn);
                friends.saveFriend(name,"","");
            }while(contactsCursor.moveToNext());
        }

        setContentView(R.layout.activity_view_friends_default);
        Cursor cursor = friends.getAllFriends();
        String[]from = new String[]{SQLiteHelperFriends.COLUMN_NAME, SQLiteHelperFriends.COLUMN_PHONE, SQLiteHelperFriends.COLUMN_EMAIL};
        int[]to = new int[]{R.id.friend_name, R.id.friend_phone, R.id.friend_email};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_friend_entry,cursor,from, to);
        adapter.notifyDataSetChanged();
        lv = (ListView)findViewById(R.id.friendListView);
        lv.setAdapter(adapter);
        // OnCLickListiner For List Items
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                friend_name_tv = (TextView) view.findViewById(R.id.friend_name);

                friend_first_name = friend_name_tv.getText().toString();

                showButtonPopUp(view);
//                Intent modify_intent = new Intent(getApplicationContext(),
//                        ViewFriendsDefault.class);
//                modify_intent.putExtra("memberName", memberName_val);
//                modify_intent.putExtra("memberID", memberID_val);
//                startActivity(modify_intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_friends_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void showButtonPopUp(View view) {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_buttons_friends, (ViewGroup)findViewById(R.id.popUpButtonsFriendsView));

        final PopupWindow pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, 300, true);

        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));
        pw.setTouchable(true);

        pw.setOutsideTouchable(true);

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
        pw.showAsDropDown((TextView) view.findViewById(R.id.friend_name), 0, 0);

        final ListView list = (ListView)layout.findViewById(R.id.popUpButtonsFriendsView);

        //Load {"View More", "Delete"} into ListView
        String[]values = new String[]{"View More", "Delete"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.pop_up_entry, R.id.popUpEntry, values);
        spinnerArrayAdapter.notifyDataSetChanged();
        list.setAdapter(spinnerArrayAdapter);

        // OnCLickListiner For List Items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView buttonSelectedTextView;

                buttonSelectedTextView = (TextView) view.findViewById(R.id.popUpEntry);

                String buttonSelectedString = buttonSelectedTextView.getText().toString();

                if(buttonSelectedString.equals("View More")) {
                    viewMoreFriend(view);
                }
                else if(buttonSelectedString.equals("Delete"))
                    buttonSelectedString = "";
                    //Dialog for deletion
            }
        });
    }

    public void viewMoreFriend(View view) {
        Intent intent = new Intent(this, ViewMoreFriend.class);
        startActivity(intent);

    }

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendDefault.class);
        startActivity(intent);
    }
}
