package fcm.svikas.com.sv_fcm_notify_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //private static  final String URL_TOKEN_STORE = "http://192.168.0.100:3000/fcmtoken";
    private static  final String URL_TOKEN_STORE = "http://10.140.56.22:3000/fcmtoken";
    private TextView textView;
    private EditText editCuid;
    private EditText editEmail;
    private Button registerDeviceButton;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "----------------------onCreate----------------------");
        textView = (TextView) findViewById(R.id.informationTextView);
        editCuid = (EditText) findViewById(R.id.cuid);
        editEmail = (EditText) findViewById(R.id.emailid);
        registerDeviceButton = (Button) findViewById(R.id.registerToken);

        registerDeviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                sendTokenToServer();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG, "----------------------onReceive--------SharedPref--------------"+MySharedPrefManager.getInstance(MainActivity.this).toString());
                textView.setText("New MyToken = "+ MySharedPrefManager.getInstance(MainActivity.this).getToken());

            }
        };

        if(MySharedPrefManager.getInstance(this).getToken() != null) {
            textView.setText("Unchanged MyToken = " + MySharedPrefManager.getInstance(this).getToken());
        }

        Log.d(TAG, "fcmtoken = "+ MySharedPrefManager.getInstance(this).getToken());

        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_BROADCAST));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
    }

    private void sendTokenToServer() {
        String cuid = editCuid.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email id", Toast.LENGTH_LONG).show();
        } else {
            if (MySharedPrefManager.getInstance(this).getToken() != null) {
                callStorageEndPoint(cuid, email);
            } else {
                Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callStorageEndPoint(final String cuid, final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_TOKEN_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cuid", cuid);
                params.put("email", email);
                params.put("token", MySharedPrefManager.getInstance(MainActivity.this).getToken());

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
