package driverside.ojekkeren.com.ojekkerendriverside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashScreen extends Activity {

    private boolean retVal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final TextView loadingText = (TextView) findViewById(R.id.loadingText);
        /*Startup*/
        int secondsDelayed = 1;
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                loadingText.setText("Check Connection");
                getCheckConnection();

                Intent intent = new Intent(SplashScreen.this, ActivityLogin.class);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 3000);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {

                loadingText.setText("Rebuild Account Database");
                setAndResetAccount();
            }
        }, secondsDelayed * 5000);
    }

    public String getServerAPIServer(){
        Context context = getApplicationContext();
        String ServerAPI;
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ServerAPI = ai.metaData.get("ServerAPI").toString();
        return ServerAPI;
    }

    public boolean getCheckConnection(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getServerAPIServer())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APICheckConnection checkConnectionAPI = restAdapter.create(APICheckConnection.class);

        checkConnectionAPI.checkConn("checkConnection", new Callback<POJOCheckConnection>() {

            @Override
            public void success(POJOCheckConnection checkConnectionPojo, Response response) {
                retVal = true;
            }

            @Override
            public void failure(RetrofitError error) {
                retVal = false;
            }
        });
        return retVal;
    }

    public void setAndResetAccount(){
        DBDriverAccount dbAccount = new DBDriverAccount(this);

        POJODrivers memberAkun = new POJODrivers();
        memberAkun.setPhonenum("0");
        memberAkun.setIsLogged("0");
        memberAkun.setId(0);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() == null)
        {
            dbAccount.startUpMemberTable(memberAkun);
        }else{
            if(!dbAccount.getCurrentMemberDetails().getIsLogged().equals("1")){
                dbAccount.reCreateDB();
            }
        }

    }
}
