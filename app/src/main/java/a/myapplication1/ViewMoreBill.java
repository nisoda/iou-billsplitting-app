package a.myapplication1;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ViewMoreBill extends ActionBarActivity {
        SQLiteHelperBills bills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<String> list = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        bills = new SQLiteHelperBills(this);
        Cursor cursor = bills.getBill(ViewBillsDefault.bill_name, ViewBillsDefault.bill_date);
        if(cursor.moveToFirst()){
            do {
                for(int i = 1 ; i < 9 ; i++)
                    list.add(cursor.getString(i));
            }while(cursor.moveToNext());
        }
        setContentView(R.layout.activity_view_more_bill);

        ((TextView)findViewById(R.id.friendNameText)).setText(list.get(0));
        ((TextView)findViewById(R.id.friendPhoneText)).setText(list.get(1));
        ((TextView)findViewById(R.id.friendEmailText)).setText(list.get(2));

        setTitle(getString(R.string.title_activity_view_more_friend) + " " + ViewFriendsDefault.friend_first_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_more_bill, menu);
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
}
