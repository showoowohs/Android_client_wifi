package Android_client_wifi.pkpkpkppp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Android_client_wifiActivity extends Activity {
	private TextView Show;
	private EditText ed;
	private String Remote_IP = "";
	public static final int SERVERPORT = 1234;
	private String line;
	public Thread desktopServerThread;
	private Handler handler = new Handler();
	private ServerSocket serverSocket;
	private static String hostip;//IP
	public static String My_IP ;
	private String Send = "";
	
	private InetAddress serverAddr = null;
	private SocketAddress sc_add = null;
	private Socket socket = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findID();
        Intent Progress_loading = new Intent(this, get_ip.class);  
        startActivityForResult(Progress_loading, 1);
        hostip = getLocalIpAddress();//Get IP
        if (hostip != null){
        	Log.d("GetIPMAC", hostip);
        	My_IP = hostip;
        	Log.d("SERVERIP", My_IP);
//        	ShowIP.setText("IP is " + My_IP);
        }else{
        	Toast.makeText(getBaseContext(), "hostip is null", Toast.LENGTH_LONG).show();
        	Log.d("GetIPMAC", "null");   
        }
        
        desktopServerThread = new Thread(socket_server);
        desktopServerThread.start();
    }
    private void findID(){
    	Show = (TextView) this.findViewById(R.id.Show);
    	ed = (EditText) this.findViewById(R.id.ed);
    }
    public String getLocalIpAddress() {   
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface   
                    .getNetworkInterfaces(); en.hasMoreElements();) {   
                NetworkInterface intf = en.nextElement();   
                for (Enumeration<InetAddress> enumIpAddr = intf   
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {   
                    InetAddress inetAddress = enumIpAddr.nextElement();   
                    if (!inetAddress.isLoopbackAddress()) {   
                        return inetAddress.getHostAddress().toString();   
                    }   
                }   
            }   
        } catch (SocketException ex) {   
            Log.e("WifiPreference IpAddress", ex.toString());   
        }   
        return null;   
    }
    public void Button_Send(View view){
    	
		

		// �n�ǰe���r��
		String str = "";
		str += ed.getText().toString();
//		String message = "Hello Socket";
		Send_str(str);
		

    }
    private void Send_str(String str){
    	try {

			// �]�wServer IP��m

			serverAddr = InetAddress.getByName(Remote_IP.toString());

			// �]�wport:1234

			sc_add = new InetSocketAddress(serverAddr, 1234);

			socket = new Socket();

			// �PServer�s�u�Atimeout�ɶ�2��

			socket.connect(sc_add, 2000);

			// �ǰe���

			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());

			out.writeUTF(My_IP+": "+str);
			Show.setText(Send+=str+"\n");
			Show.setTextColor(Color.GREEN);
			// ����socket

			socket.close();

		} catch (UnknownHostException e) {

			Show.setText("InetAddress����إߥ���");

		} catch (SocketException e) {

			Show.setText("socket�إߥ���");

		} catch (IOException e) {

			Show.setText("�ǰe����");

		}
    }
    public void Button_Get_GPS(View view){
    	String LocationInfo = getLocationInfo();
    	mMakeTextToast(LocationInfo, true);
    	System.out.println("LocationInfo.." + LocationInfo.toString());
    }
   
	private void mMakeTextToast(String string, boolean b) {
		if(b == true){
			Toast.makeText(this, string, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		}
	}
	private String getLocationInfo() { 
	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
	    Location location = locationManager 
	            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
	    return location.getLatitude() + "," + location.getLongitude(); 
	}
	
    private Runnable socket_server = new Runnable() {

		public void run() {


			try {

				// �إ�serverSocket
				System.out.println("�إ�serverSocket..");
				serverSocket = new ServerSocket(SERVERPORT);

				// ���ݳs�u

				while (true) {

					// �����s�u

					Socket client = serverSocket.accept();

					handler.post(new Runnable() {

						public void run() {
							System.out.println("Connected.");
						}

					});

					try {

						// �������

						DataInputStream in = new DataInputStream(
								client.getInputStream());

						line = in.readUTF();

						if (line != null || line != "" ) {

							handler.post(new Runnable() {

								public void run() {
									System.out.println("line. "+line);
									Show.setText(line);
									Show.setTextColor(Color.RED);
									
									if(line.equals("opengps")){
										String LocationInfo = getLocationInfo();
										Send_str(LocationInfo);
//								    	mMakeTextToast(LocationInfo, true);
								    	System.out.println("LocationInfo.." + LocationInfo.toString());
										
									}else if(line.equals("openCamera")){
										toa(line);
									}
								}

							});

						}

//						break;

					} catch (Exception e) {

						handler.post(new Runnable() {

							public void run() {
								System.out.println("�ǰe����");
								

							}

						});

					}

				}

			} catch (IOException e) {

				handler.post(new Runnable() {

					public void run() {
						System.out.println("�إ�socket����");
						

					}

				});

			}

		}

	};
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + "  " );
            if(requestCode == 1){
            	Remote_IP = (String) data.getExtras().get("Remote_IP");
            	Show.setText("Remote IP is "+Remote_IP);
            }
    }
    public void toa(String line){
    	if(line.equals("opengps")){
    		Toast.makeText(this, "���b�}��GPS", Toast.LENGTH_SHORT).show();
    	}else if(line.equals("openCamera")){
    		Toast.makeText(this, "���b�}��Camera", Toast.LENGTH_SHORT).show();
    	}
    	

    }
    
            
}