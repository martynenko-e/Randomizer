package randomizer.coddingforfun.martynenko.ua.randomizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private Map<String, View> viewMap = new HashMap<>();
    private ArrayList<String> resultList = new ArrayList<>();
    private Button btnStart;
    private Button btnResult;
    private TextView txtResult;
    Task task;

    Button buttonAdd;
    LinearLayout container;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);
        btnStart = (Button) findViewById(R.id.start);
        btnResult = (Button) findViewById(R.id.result);
        txtResult = (TextView) findViewById(R.id.txtResult);

        Intent i = getIntent();
        if (i.hasExtra("names")){
            String value = i.getStringExtra("names");
            String[] nameArray = value.split(";");
            for (String name: nameArray) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View innerView = layoutInflater.inflate(R.layout.row, null);
                final TextView textOut = (TextView) innerView.findViewById(R.id.textout);

                textOut.setText(name);
                Button buttonRemove = (Button) innerView.findViewById(R.id.remove);
                buttonRemove.setBackground(getResources().getDrawable(R.drawable.button_shape));
                final ProgressBar progressBar = (ProgressBar) innerView.findViewById(R.id.progressBar);
                progressBar.setMax(1000);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) innerView.getParent()).removeView(innerView);
                        viewMap.remove(textOut.getText().toString());
                    }
                });
                if (!viewMap.containsKey(textOut.getText().toString())) {
                    if (!textOut.getText().toString().equals("")) {
                        viewMap.put(textOut.getText().toString(), progressBar);
                        container.addView(innerView);
                    }
                }
            }
        }

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int counter = 0;
                for (final Map.Entry p : viewMap.entrySet()) {
                    ResultListener progress = new ResultListener() {
                        @Override
                        public void updateView(int newValue) {
                            ((ProgressBar) p.getValue()).setProgress(newValue);
                        }
                    };

                    task = new Task(progress, (String) p.getKey());
                    Thread argueThread = new Thread(task);
                    argueThread.start();
                    if (task.isStopped()) {
                        txtResult.setText("Somebody finish");
                    }
                }
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i = 0; i < resultList.size(); i++) {
                    result += (i + 1);
                    for (int j = 0; j < 40 - resultList.get(i).length(); j++)
                        result += ".";
                    result += resultList.get(i);
                    result += "\n";
                }
                txtResult.setText(result);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void startActivity(View view){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    class Task implements Runnable {
        String name = "";
        ResultListener rl1;
        volatile boolean stop = false;

        public Task(ResultListener rl1, String n) {
            this.rl1 = rl1;
            this.name = n;
        }

        public void stopTask() {
            stop = true;
        }

        @Override
        public void run() {

            resultList.clear();
            int value = 0;

            while (value < 1000 && !stop) {
                int temp = (int) (Math.random() * 3);
                if (temp + value > 1000) {
                    value = 1000;
                } else {
                    value += temp;
                }
                if (value == 1000) {
                    resultList.add(name);
                }

                synchronized (this) {
                    final int finalValue = value;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rl1.updateView(finalValue);
                        }
                    });
                }


                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            stop = true;
        }

        boolean isStopped() {
            return stop;
        }
    }
}
