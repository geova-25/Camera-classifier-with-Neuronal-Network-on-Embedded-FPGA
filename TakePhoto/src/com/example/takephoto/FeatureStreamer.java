package com.example.takephoto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.widget.Toast;

/**
 * Connects to a Driver app server via TCP and streams features over the network.
 */
class FeatureStreamer {
  private Socket sock;
  private DataOutputStream dos;
  
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
  
  void sendByteArray(byte[] send) throws IOException {
    if (dos != null) {
      dos.writeInt(send.length);
      dos.write(send, 0, send.length);
      dos.flush();
    }
  }
  
  void sendFeatures( byte[] send,Activity act) {
    try {
      if (dos != null) {
        //dos.writeInt(width);
        //dos.writeInt(height);
      //  dos.writeInt(accelerometerFeatures.length);
    	 Toast.makeText(act, String.valueOf(send.length), Toast.LENGTH_SHORT).show();
    	 dos.writeBytes( String.valueOf(send.length));
    	//dos.writeInt(send.length);
        dos.write(send, 0, send.length);
        //for (int i = 0; i < accelerometerFeatures.length; i++) {
         // dos.writeFloat(accelerometerFeatures[i]);
       // }
        dos.flush();
      }
    } catch (IOException e) {
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
