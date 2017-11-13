package com.example.takephoto;

import java.nio.ByteBuffer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

	
	FeatureStreamer fs = new FeatureStreamer();
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.enableDefaults();//habilitar acceso a base de datos
		
		
		 predict();
		 
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	static final int REQUEST_IMAGE_CAPTURE = 1;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        ImageView im = (ImageView)findViewById(R.id.imageView2);
	        im.setImageBitmap(imageBitmap);
	      //  data.putExtra("byteArr", touchView.data);
	        Toast.makeText(this, String.valueOf(imageBitmap.getByteCount()), Toast.LENGTH_SHORT).show();
	      
	        int size = imageBitmap.getRowBytes() * imageBitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            imageBitmap.copyPixelsToBuffer(byteBuffer);
            
            Toast.makeText(this, String.valueOf(imageBitmap.getWidth()), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, String.valueOf(imageBitmap.getHeight()), Toast.LENGTH_SHORT).show();
            for(int i = 0; i< byteBuffer.array().length ; i++)
            {
            	if(byteBuffer.array()[i] > 255)
            	{
            		 Toast.makeText(this, "Alerta: " +String.valueOf(byteBuffer.array()[i]), Toast.LENGTH_SHORT).show();
            	}
            }
		    fs.sendFeatures(byteBuffer.array(), this);
       
	    }
	}
	
	public void event_take_photo(View n)
	{
		dispatchTakePictureIntent();
    	
	}
	public void predict()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("Ingrese ip");
		final EditText input = new EditText(this);
		input.setText("192.168.1.3");
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		       LinearLayout.LayoutParams.MATCH_PARENT,
		       LinearLayout.LayoutParams.MATCH_PARENT);
	    input.setLayoutParams(lp);
	    alertDialogBuilder.setView(input);
	    alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface arg0, int arg1) 
	    {  	 
	    	fs.connect(input.getText().toString(), 6666);
	    	
		}
	    });
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
		
	}
	
	
	
}