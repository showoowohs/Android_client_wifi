package Android_client_wifi.pkpkpkppp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class get_ip extends Activity {
	private EditText ed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_ip);
        findID();
    }
    private void findID(){
    	ed = (EditText) this.findViewById(R.id.get_ip_ed);
    }
    public void get_ip_Button_Send(View view){
    	String Send_IP = ed.getText().toString();
    	Intent i = new Intent();
        Bundle b = new Bundle();
                b.putString("Remote_IP", Send_IP);             
                i.putExtras(b);         
        setResult(RESULT_OK,i);
		finish();

    }
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        System.out.println("onKeyUp  keyCode "+keyCode +"   KeyEvent  " +event);
        if(keyCode==4 ){
//        	finish();
        	Toast.makeText(this, "½Ð¥ý¿é¤JIP", Toast.LENGTH_SHORT).show();
        }
            
        return true;  
    }
    
}