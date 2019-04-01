package grintsys.com.vanshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import grintsys.com.vanshop.CONST;
import grintsys.com.vanshop.MyApplication;
import grintsys.com.vanshop.R;
import grintsys.com.vanshop.api.EndPoints;
import grintsys.com.vanshop.api.GsonRequest;
import grintsys.com.vanshop.entities.client.Client;
import grintsys.com.vanshop.entities.client.ClientListResponse;
import grintsys.com.vanshop.ux.adapters.ClientsRecyclerAdapter;

public class RCobrosDiarios extends Activity {
    private ProgressDialog progressDialog;
    private List<Client> clients;

    private RecyclerView clientsRecycler;
    private GridLayoutManager clientsRecyclerLayoutManager;
    private ClientsRecyclerAdapter clientsRecyclerAdapter;

    private WebView wvDespacho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcobros_diarios);

        WebView myWebView = (WebView) findViewById(R.id.wvDespachos);
        myWebView.loadUrl("http://181.199.177.162/SVKAD/RCobrosDiarios");

    }

}
