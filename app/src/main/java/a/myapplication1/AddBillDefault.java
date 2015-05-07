package a.myapplication1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.ContactsContract;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddBillDefault extends ActionBarActivity {

    private ArrayList<String> friendsAdded;
    private String billName;
    private double amount;
    private double tip;
    private double tax;
    private double total;
    private String participants;
    private double amtPer;
    private Date dateOrig;
    private String dateString;
    private SQLiteHelperBills sqLiteHelperBills;
    private SQLiteHelperFriends friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = new SQLiteHelperFriends(this);
        sqLiteHelperBills = new SQLiteHelperBills(this);
        friendsAdded = new ArrayList<String>();
        billName = "";
        dateOrig = null;
        dateString = "";
        amount = 0.0;
        tip = 0.0;
        tax = 0.0;
        total = 0.0;
        participants = "";
        amtPer = 0.0;
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

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void showFriendsPopUp(View view) {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_listview, (ViewGroup)findViewById(R.id.dropDownListView));

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
        pw.showAsDropDown((TextView)findViewById(R.id.addedFriendsTextView), 10, 0);

        final ListView list = (ListView)layout.findViewById(R.id.dropDownListView);
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

    public void saveBill(View view) {
        billName = ((EditText)findViewById(R.id.billNameText)).getText().toString();

        if (billName.isEmpty()){
            Toast.makeText(this, "Bill Name cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }


        Toast.makeText(this, "Bill saving...", Toast.LENGTH_SHORT).show();

        amount = Double.parseDouble(((EditText)findViewById(R.id.amountText)).getText().toString());
        total = amount * (1 + tax);
        total = total * (1 + tip);
        participants = friendsAdded.toString();
        amtPer = total / friendsAdded.size();

        SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yy");
        dateOrig = new Date();
        dateString = date_format.format(dateOrig);

        boolean result = sqLiteHelperBills.saveBill(billName, dateString, amount, tip, tax, total, participants, amtPer);

        if (result){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddBillDefault.this, "Bill saved!", Toast.LENGTH_SHORT).show();
                    ((EditText) findViewById(R.id.billNameText)).setText("");
                    ((EditText) findViewById(R.id.amountText)).setText("");
                    ((RadioButton) findViewById(R.id.tipNoneRadioButton)).setSelected(true);
                    ((EditText) findViewById(R.id.custTipText)).setText("");
                    ((RadioButton) findViewById(R.id.taxNoneRadioButton)).setSelected(true);
                    ((EditText) findViewById(R.id.custTaxText)).setText("");
                    ((TextView)findViewById(R.id.addedFriendsTextView)).setText("");
                    friendsAdded.clear();
                }
            }, 2500);
        } else {
            Toast.makeText(this, "Failed to save!", Toast.LENGTH_LONG).show();
        }


    }

    public void tipRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String customTip = new String();

        switch (view.getId()) {
            case R.id.tipNoneRadioButton:
                break;
            case R.id.tip15RadioButton:
                if(checked) tip = 0.15;
                break;
            case R.id.tip18RadioButton:
                if(checked) tip = 0.18;
                break;
            case R.id.tip20RadioButton:
                if(checked) tip = 0.20;
                break;
            case R.id.tipCustRadioButton:
                customTip = ((EditText)findViewById(R.id.custTipText)).getText().toString();
                if(checked && !customTip.isEmpty()) tip = Double.parseDouble(customTip)/100;
                break;
            default: break;
        }
    }

    public void taxRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String customTax = new String();

        switch (view.getId()) {
            case R.id.taxNoneRadioButton:
                break;
            case R.id.taxCustRadioButton:
                customTax = ((EditText)findViewById(R.id.custTaxText)).getText().toString();
                if(checked && !customTax.isEmpty()) tax = Double.parseDouble(customTax)/100;
                break;
            default: break;
        }
    }
}
