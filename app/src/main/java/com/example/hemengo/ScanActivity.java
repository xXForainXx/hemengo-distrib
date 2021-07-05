package com.example.hemengo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

// Implements onClickListener for the onclick behaviour of scan button
public class ScanActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView textViewDistributorId;
    TextView textViewDistributorLat;
    TextView textViewDistributorLng;
    TextView textViewDistributorLastScan;
    TextView textViewLabel;
    Distributor distributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Referencing and initializing the button, and the text views (for content and format)
        scanBtn = findViewById(R.id.scanBtn);

        textViewLabel = findViewById(R.id.textViewLabel);
        textViewDistributorId = findViewById(R.id.textViewDistributorId);
        textViewDistributorLat = findViewById(R.id.textViewDistributorLat);
        textViewDistributorLng = findViewById(R.id.textViewDistributorLng);
        textViewDistributorLastScan = findViewById(R.id.textViewDistributorLastScan);

        // Adding listener to the scan button
        scanBtn.setOnClickListener(this);

        this.distributor = new Distributor(10, 43.46196412157843, -1.5377605279714546);
        try {
            this.distributor.setQrCodeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function will use Intent and launch the scanner.
     * Scanner requires “onActivityResult” function defined in the calling Activity.
     */
    @Override
    public void onClick(View v) {
        // We need to create the object of IntentIntegrator class which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        // We only allow QR Codes
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        // Display what to do at the bottom
        intentIntegrator.setPrompt("Scan a QR Code");
        // To have vertical camera layout
        intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        // Locking the orientation (no horizontal layout)
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            // If the intentResult content is null then toast a message as "cancelled"
            if (intentResult.getContents() != null) {

                try {
                    // We transform the scan content to JSON object to use its fields conveniently
                    JSONObject scannedQrCodeData = new JSONObject(intentResult.getContents());

                    this.distributor.scan();

                    // If what we just scanned equals the qr code data of the distributor then we set the
                    // appropriate information, if not we display a toast and empty all text fields
                    if (this.distributor.getQrCodeData().toString().equals(scannedQrCodeData.toString())) {

                        textViewLabel.setText("Merci !");
                        textViewDistributorId.setText(String.format("Identifiant: %s", scannedQrCodeData.get("id").toString()));
                        textViewDistributorLat.setText(String.format("Latitude: %s", scannedQrCodeData.get("lat").toString()));
                        textViewDistributorLng.setText(String.format("Longitude: %s", scannedQrCodeData.get("lng").toString()));
                        textViewDistributorLastScan.setText(String.format("Dernier scan: %s", this.distributor.getLastScanDate()));

                    } else {
                        Toast.makeText(getBaseContext(), "Not matching distributor data", Toast.LENGTH_SHORT).show();

                        textViewLabel.setText("Merci de scanner le distributeur");
                        textViewDistributorId.setText("Identifiant:");
                        textViewDistributorLat.setText("Latitude:");
                        textViewDistributorLng.setText("Longitude:");
                        textViewDistributorLastScan.setText("Dernier scan:");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
