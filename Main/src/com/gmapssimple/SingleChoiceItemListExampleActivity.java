package com.gmapssimple;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SingleChoiceItemListExampleActivity extends Activity implements android.view.View.OnClickListener{

	
	String[] period =new String[] {"byzantine","roman"};
	int selectedItem=0;
	Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        button =(Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
    }
   
	public void onClick(View v) {
		if(v.getId()==R.id.button1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Select a time period");	        
		        int selected = selectedItem;	        
		        builder.setSingleChoiceItems(
		        		period, 
		                selected, 
		                new DialogInterface.OnClickListener() {
		            
		            public void onClick(DialogInterface dialog,int which) {
		            	selectedItem=which;
		            	Toast.makeText(SingleChoiceItemListExampleActivity.this,"You Select Letter "+period[selectedItem],Toast.LENGTH_SHORT).show();
		                dialog.dismiss();
		            }
		        });
		        AlertDialog alert = builder.create();
		        alert.show();
		}
	}
	
}