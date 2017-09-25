package com.example.android.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.Dimension;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;


public class Donate extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate);
        generateMaskedWalletRequest();
        mony = (TextView) findViewById(R.id.mon);
        plus = (Button) findViewById(R.id.pls);
        minus = (Button) findViewById(R.id.min);
        Total = 0;
        String WALLET_FRAGMENT_ID = "wallet_fragment";
        // Check if WalletFragment exists
        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);

        // It does not, add it
        if (mWalletFragment == null) {
            // Wallet fragment style
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(BuyButtonText.BUY_NOW)
                    .setBuyButtonWidth(Dimension.MATCH_PARENT);

            // Wallet fragment options
            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();


            // Instantiate the WalletFragment
            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

            // Initialize the WalletFragment
            WalletFragmentInitParams.Builder startParamsBuilder =
                    WalletFragmentInitParams.newBuilder()
                            .setMaskedWalletRequest(generateMaskedWalletRequest())
                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
                            .setAccountName("Google I/O Codelab");
            mWalletFragment.initialize(startParamsBuilder.build());
            // Add the fragment to the UI
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                    .commit();


        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();

    }

    TextView mony;
    Button plus;
    Button minus;
    double Total;
    private SupportWalletFragment mWalletFragment;
    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    private GoogleApiClient mGoogleApiClient;
    private MaskedWallet mMaskedWallet;
    public static final int FULL_WALLET_REQUEST_CODE = 889;
    private FullWallet mFullWallet;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Button button = (Button) findViewById(R.id.rtts);


        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Donate.this, MainActivity.class);
                startActivity(intent);

            }


        });

        return true;

    }


    public void inc(View view) {
        Total = Total + 1;
        mony.setText(String.valueOf(Total));
    }

    public void min(View view) {
        Total = Total - 1;
        check();
        mony.setText(String.valueOf(Total));

    }

    public double check() {
        if (Total < 0) {
            Total = 0;
        }
        return Total;
    }

    private MaskedWalletRequest generateMaskedWalletRequest() {
        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName("Donate Music APP")
                        .setPhoneNumberRequired(true)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode("USD")
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice(String.valueOf(Total))
                                .addLineItem(LineItem.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setDescription("Donation")
                                        .setQuantity("1")
                                        .setUnitPrice(String.valueOf(Total))
                                        .setTotalPrice(String.valueOf(Total))
                                        .build())
                                .build())
                        .setEstimatedTotalPrice(String.valueOf(Total))
                        .build();
        return maskedWalletRequest;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // GoogleApiClient is connected, we don't need to do anything here
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // GoogleApiClient is temporarily disconnected, no action needed
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // GoogleApiClient failed to connect, we should log the error and retry
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MASKED_WALLET_REQUEST_CODE:
                // ...
            case FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Update transaction status
                        Wallet.Payments.notifyTransactionStatus(mGoogleApiClient,
                                generateNotifyTransactionStatusRequest(
                                        mFullWallet.getGoogleTransactionId(),
                                        NotifyTransactionStatusRequest.Status.SUCCESS));
                        mFullWallet = data
                                .getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        // Show the credit card number
                        Toast.makeText(this,
                                mFullWallet.getProxyCard().getPan().toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice(String.valueOf(Total))
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("DONATEMUSICAPP")
                                .setQuantity("1")
                                .setUnitPrice(String.valueOf(Total))
                                .setTotalPrice(String.valueOf(Total))
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice("0")
                                .build())
                        .build())
                .build();
        mFullWallet.getProxyCard().getPan();
        return fullWalletRequest;
    }

    public void requestFullWallet(View view) {
        if (mMaskedWallet == null) {
            Toast.makeText(this, "No masked wallet, can't confirm", Toast.LENGTH_SHORT).show();
            return;
        }
        Wallet.Payments.loadFullWallet(mGoogleApiClient,
                generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                FULL_WALLET_REQUEST_CODE);
    }

    public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(
            String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }

    public void url() {
        Intent url = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/android-pay/get-started"));
        startActivity(url);
    }
}

