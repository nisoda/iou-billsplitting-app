package a.myapplication1;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ViewMoreBill extends ActionBarActivity {
    SQLiteHelperBills bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<String> list = new ArrayList<String>();
        ArrayList<String> participantsArray = new ArrayList<String>();
        String participantsString = "";

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

        ((TextView)findViewById(R.id.billNameText)).setText(list.get(0));
        ((TextView)findViewById(R.id.billDateText)).setText("Date: " + list.get(1));
        ((TextView)findViewById(R.id.billAmountText)).setText("Subtotal: $ " + list.get(2));
        ((TextView)findViewById(R.id.billTipText)).setText("Tip: " + list.get(3) + "%");
        ((TextView)findViewById(R.id.billTaxText)).setText("Tax: " + list.get(4) + "%");
        ((TextView)findViewById(R.id.billTotalText)).setText("Total: $" + list.get(5));
        ((TextView)findViewById(R.id.billParticipantsText)).setText("Participants: " + list.get(6).substring(1,(list.get(6).length() - 1)));
        ((TextView)findViewById(R.id.billAmtPerText)).setText("Amount per participant: $ " + list.get(7));

        setTitle(getString(R.string.title_activity_view_more_friend) + " " + ViewBillsDefault.bill_name);
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

    private ArrayList<String> stringToArray(String arrayString){
        ArrayList<String> list = new ArrayList<String> ();
        int element = 0;
        char current = ' ';

        for(int i = 0 ; i < arrayString.length() ; i++){
            current = arrayString.charAt(i);
            System.out.println(current);
            if(current == ' ') {
                continue;
            }
            else if(current == ',') {
                element++;
                list.add(element, "");
            }
            else if(current == '[') {
                list.add(element, "");
            }
            else if(current == ']') {
                continue;
            }
            else {
                list.get(element).concat(String.valueOf(current));
            }
        }

        return list;
    }
}
