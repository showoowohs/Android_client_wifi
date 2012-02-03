package Android_client_wifi.pkpkpkppp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Android_client_wifiActivity extends Activity {
	private TextView Show;
	private EditText ed;
	private String Remote_IP = "";
	public static final int SERVERPORT = 1234;
	private String line;
	public Thread desktopServerThread;
	private Handler handler = new Handler();
	private ServerSocket serverSocket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findID();
        Intent Progress_loading = new Intent(this, get_ip.class);  
        startActivityForResult(Progress_loading, 1);
        desktopServerThread = new Thread(socket_server);
        desktopServerThread.start();
    }
    private void findID(){
    	Show = (TextView) this.findViewById(R.id.Show);
    	ed = (EditText) this.findViewById(R.id.ed);
    }
    public void Button_Send(View view){
		InetAddress serverAddr = null;

		SocketAddress sc_add = null;

		Socket socket = null;

		// 要傳送的字串
		String Send = ed.getText().toString();
//		String message = "Hello Socket";

		try {

			// 設定Server IP位置

			serverAddr = InetAddress.getByName(Remote_IP.toString());

			// 設定port:1234

			sc_add = new InetSocketAddress(serverAddr, 1234);

			socket = new Socket();

			// 與Server連線，timeout時間2秒

			socket.connect(sc_add, 2000);

			// 傳送資料

			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());

			out.writeUTF(Send);
			Show.setText(Send);
			// 關閉socket

			socket.close();

		} catch (UnknownHostException e) {

			Show.setText("InetAddress物件建立失敗");

		} catch (SocketException e) {

			Show.setText("socket建立失敗");

		} catch (IOException e) {

			Show.setText("傳送失敗");

		}

    }
    private Runnable socket_server = new Runnable() {

		public void run() {


			try {

				// 建立serverSocket
				System.out.println("建立serverSocket");
				serverSocket = new ServerSocket(SERVERPORT);

				// 等待連線

				while (true) {

					// 接收連線

					Socket client = serverSocket.accept();

					handler.post(new Runnable() {

						public void run() {
							System.out.println("Connected.");
						}

					});

					try {

						// 接收資料

						DataInputStream in = new DataInputStream(
								client.getInputStream());

						line = in.readUTF();

						if (line != null || line != ""  && line.endsWith("opengps")) {

							handler.post(new Runnable() {

								public void run() {
									System.out.println("line. "+line);
									Show.setText(line);
									

								}

							});

						}

//						break;

					} catch (Exception e) {

						handler.post(new Runnable() {

							public void run() {
								System.out.println("傳送失敗");
								

							}

						});

					}

				}

			} catch (IOException e) {

				handler.post(new Runnable() {

					public void run() {
						System.out.println("建立socket失敗");
						

					}

				});

			}

		}

	};
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1){
            	Remote_IP = (String) data.getExtras().get("Remote_IP");
            	Show.setText("Remote IP is "+Remote_IP);
            }
    }
    
            
}