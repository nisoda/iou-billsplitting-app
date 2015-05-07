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
import android.widget.ArrayAdapter;
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

    public static String bill_name;
    public static String bill_date;
    TextView bill_name_tv;
    TextView bill_date_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bills = new SQLiteHelperBills(this);
        selectedBill = "";
        setContentView(R.layout.activity_view_bills_default);
        Cursor cursor = bills.getAllBills();
        String[]from = new String[]{SQLiteHelperBills.COLUMN_BILL_NAME, SQLiteHelperBills.COLUMN_DATE, SQLiteHelperBills.COLUMN_AMT_PER};
        int[]to = new int[]{R.id.bill_name, R.id.bill_date_view, R.id.amount};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_bill_entry,cursor,from, to);
        adapter.notifyDataSetChanged();
        lv = (ListView)findViewById(R.id.billListView);
        lv.setAdapter(adapter);

        // OnCLickListiner For List Items
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                bill_name_tv = (TextView) view.findViewById(R.id.bill_name);
                bill_date_tv = (TextView) view.findViewById(R.id.bill_date_view);
                bill_name = bill_name_tv.getText().toString();
                bill_date = bill_date_tv.getText().toString();

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
        getMenuInflater().inflate(R.menu.menu_view_bills_default, menu);
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
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_buttons_bills, (ViewGroup)findViewById(R.id.popUpButtonsBillsView));

        final PopupWindow pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, 300, true);

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
        pw.showAsDropDown((TextView) view.findViewById(R.id.bill_name), 0, 0);

        final ListView list = (ListView)layout.findViewById(R.id.popUpButtonsBillsView);

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
                    viewMoreBill(view);
                }
                else if(buttonSelectedString.equals("Delete"))
                    buttonSelectedString = "";
                //Dialog for deletion
            }
        });
    }

    public void viewMoreBill(View view) {
        Intent intent = new Intent(this, ViewMoreBill.class);
        startActivity(intent);

    }

    public void addBill(View view) {
        Intent intent = new Intent(this, AddBillDefault.class);
        startActivity(intent);
    }

    public void deleteBill(View view) {

    }
}
