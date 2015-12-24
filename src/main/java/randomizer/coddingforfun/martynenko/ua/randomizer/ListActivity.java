package randomizer.coddingforfun.martynenko.ua.randomizer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Martynenko on 15.12.2015.
 */
public class ListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Button btn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);
        Resources res = getResources();
        String[] s = res.getStringArray(R.array.string_array_name);
        final ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, s));
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                if (lv.getCheckItemIds().length <= 1) {
                    Toast.makeText(getApplicationContext(), "Please select more than 1 player", Toast.LENGTH_SHORT).show();
                }
                else if (lv.getCheckItemIds().length <= 6) {
                    for (int i = 0; i < lv.getCheckItemIds().length; i++) {
                        str += lv.getAdapter().getItem((int) lv.getCheckItemIds()[i]).toString() + ";";
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("names", str);
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select less than 6 players", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
