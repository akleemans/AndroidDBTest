package com.dbtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DBTestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		Button showall = (Button) findViewById(R.id.showall);
		showall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), ShowResults.class);
				intent.putExtra("query", "%");
				
				startActivity(intent);
			}
		});
		
		Button showsome = (Button) findViewById(R.id.showsome);
		showsome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), ShowResults.class);
				intent.putExtra("query", "%e%");
				
				startActivity(intent);
			}
		});
        
    }
}