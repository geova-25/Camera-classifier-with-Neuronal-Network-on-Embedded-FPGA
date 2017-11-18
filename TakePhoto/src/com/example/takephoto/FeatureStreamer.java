package com.example.takephoto;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

/**
 * Connects to a Driver app server via TCP and streams features over the network.
 */
class FeatureStreamer {
  private Socket sock;
  private DataOutputStream dos;
  private DataInputStream in; 
  
  FeatureStreamer() {
  }
  
  void connect(String addr, int port) {
    try {
      sock = new Socket(addr, port);
      dos = new DataOutputStream(sock.getOutputStream());
     
      
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void audioPlayer(String path, String fileName){
	    //set up MediaPlayer    
	    MediaPlayer mp = new MediaPlayer();

	    try {
	        mp.setDataSource(path + File.separator + fileName);
	        mp.prepare();
	        mp.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

  void receiveMp3(Activity act) {

		try {
			in = new DataInputStream(sock.getInputStream());
			int peso = in.readInt();
			Toast.makeText(act, "MP3 size : " + peso, Toast.LENGTH_SHORT).show();
			ByteBuffer byteArrayMp3 = ByteBuffer.allocate(peso);
			byte[] buf = byteArrayMp3.array();		
			in.readFully(buf, 0, peso);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/mymp3");
			Toast.makeText(act, "saved in: " + root + "/mymp3", Toast.LENGTH_SHORT).show();
			String fname = "result.mp3";
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			FileOutputStream out;
			out = new FileOutputStream(file);
			out.write(buf);
			out.flush();
			out.close();
			
			audioPlayer(root+ "/mymp3", "result.mp3");
			
			
		} catch (FileNotFoundException e1) 
		{
			Toast.makeText(act, e1.getMessage(), Toast.LENGTH_SHORT).show();
		}

		catch (IOException e) {
			Toast.makeText(act, "Error receiving", Toast.LENGTH_SHORT).show();
			Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	void sendFeatures(byte[] send, Activity act, int height, int width, int net) {

		try {
			if (dos != null) {
				
				dos.writeBytes(String.valueOf(net));
				Thread.sleep(400);
				dos.writeBytes(String.valueOf(send.length));
				Thread.sleep(400);
				dos.writeBytes(String.valueOf(height));
				Thread.sleep(400);
				dos.writeBytes(String.valueOf(width));
				Thread.sleep(400);
				dos.write(send, 0, send.length);
				dos.flush();
						
			
			}
		} catch (IOException e) {
			Toast.makeText(act, "Error sending", Toast.LENGTH_SHORT).show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void close() throws IOException {
		if (dos != null) {
			dos.close();
		}
		if (sock != null) {
			sock.close();
		}
	}
};
