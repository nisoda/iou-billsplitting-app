package a.myapplication1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ViewFriendsDefault extends ActionBarActivity {

    private SQLiteHelperFriends friends;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new SQLiteHelperFriends(this);
        setContentView(R.layout.activity_view_friends_default);
        Cursor cursor = friends.getAllFriends();
        String[]from = new String[]{SQLiteHelperFriends.COLUMN_NAME, SQLiteHelperFriends.COLUMN_PHONE, SQLiteHelperFriends.COLUMN_EMAIL};
        int[]to = new int[]{R.id.friend_name, R.id.friend_phone, R.id.friend_email};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_friend_entry,cursor,from, to);
        adapter.notifyDataSetChanged();
        lv = (ListView)findViewById(R.id.friendListView);
        lv.setAdapter(adapter);
        // OnCLickListiner For List Items
//        lv.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                memName_tv = (TextView) view.findViewById(R.id.member_name);
//
//                String memberName_val = memName_tv.getText().toString();
//
//                Intent modify_intent = new Intent(getApplicationContext(),
//                        Modify_member.class);
//                modify_intent.putExtra("memberName", memberName_val);
//                modify_intent.putExtra("memberID", memberID_val);
//                startActivity(modify_intent);
//            }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendDefault.class);
        startActivity(intent);
    }
}
