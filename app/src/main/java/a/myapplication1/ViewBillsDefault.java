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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ViewBillsDefault extends ActionBarActivity {
    private SQLiteHelperBills bills;
    ListView lv;
    private String selectedBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bills = new SQLiteHelperBills(this);
        selectedBill = "";
        setContentView(R.layout.activity_view_bills_default);
        Cursor cursor = bills.getAllBills();
        String[]from = new String[]{SQLiteHelperBills.COLUMN_BILL_NAME, SQLiteHelperBills.COLUMN_PARTICIPANTS, SQLiteHelperBills.COLUMN_AMT_PER};
        int[]to = new int[]{R.id.bill_name, R.id.participants, R.id.amount};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_bill_entry,cursor,from, to);
        adapter.notifyDataSetChanged();
        lv = (ListView)findViewById(R.id.billListView);
        lv.setAdapter(adapter);

        // OnCLickListiner For List Items
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView billSelectedTextView;

                billSelectedTextView = (TextView) view.findViewById(R.id.bill_name);
                selectedBill = billSelectedTextView.getText().toString();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_bills_default, menu);
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

    public void addBill(View view) {
        Intent intent = new Intent(this, AddBillDefault.class);
        startActivity(intent);
    }

    //Show more info in a popup on ListView click
//    public void showMoreInfo(View view) {
//        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_more_info, (ViewGroup)findViewById(R.id.dropDownMoreInfo));
//
//        final PopupWindow pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, 300, true);
//
//        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6CA2BFF4")));
//        pw.setTouchable(true);
//
//        pw.setOutsideTouchable(true);
//
//        pw.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    pw.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        pw.setContentView(layout);
//        pw.showAsDropDown((Button)findViewById(R.id.btn_add_bill), 10, 0);
//
//        Cursor cursor = bills.getBill(selectedBill);
//
//        String billName = cursor.getString(0);
//        double amount = cursor.getDouble(1);
//        double tip = cursor.getDouble(2);
//        double tax = cursor.getDouble(3);
//        double total = cursor.getDouble(4);
//        String participants = cursor.getString(5);
//        double amtPer = cursor.getDouble(6);
//
//        String moreInfo = new String(String.format("Bill name: %s %n" +
//                "Amount: $%.2f %n" +
//                "Tip: %f%% %n" +
//                "Tax: %f%% %n" +
//                "Total: $%.2f %n" +
//                "Participants: %s %n" +
//                "Amount per: $%.2f %n", billName, amount, (tip * 100), (tax * 100), total, participants, amtPer));
//
//        ((TextView)findViewById(R.id.moreInfoTextView)).setText(moreInfo);
//
//    }
}
