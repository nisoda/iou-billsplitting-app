package a.myapplication1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class AddFriendDefault extends ActionBarActivity {

    private String name;
    private long phone;
    private String email;
    SQLiteHelperFriends sqLiteHelperFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelperFriends = new SQLiteHelperFriends(this);
        name = "";
        phone = 0;
        email = "";
        setContentView(R.layout.activity_add_friend_default);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend_default, menu);
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

    public void saveFriend(View view) {
        name = ((EditText)findViewById(R.id.friendNameText)).getText().toString();

        if (name.isEmpty()){
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Friend saving...", Toast.LENGTH_SHORT).show();

        boolean result = sqLiteHelperFriends.saveFriend(name, phone, email);

        if (result){
            Toast.makeText(getApplicationContext(), "Friend saved!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to save!", Toast.LENGTH_LONG).show();
        }
    }
}
