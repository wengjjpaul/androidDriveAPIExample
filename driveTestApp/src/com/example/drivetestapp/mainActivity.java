package com.example.drivetestapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;

public class mainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, ResultCallback<MetadataBufferResult> {
	GoogleApiClient mGoogleApiClient;
	private boolean mResolvingError = false;
	private ListView mResultsListView;
	private static final int REQUEST_CODE_CREATOR = 1005;
	// Request code to use when launching the resolution activity
	private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1001;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// FragmentManager fragmentManager = getFragmentManager();
		// FragmentTransaction fragmentTransaction = fragmentManager
		// .beginTransaction();
		// MyListFragment fragment = new MyListFragment();
		// fragmentTransaction.add(R.id.fragmentview, fragment);
		// fragmentTransaction.commit();

		// our adapter instance

		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		// TODO Auto-generated method stub
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();

	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (mResolvingError) {
			// Already attempting to resolve an error.
			return;
		} else if (connectionResult.hasResolution()) {
			try {
				mResolvingError = true;
				connectionResult.startResolutionForResult(this,
						RESOLVE_CONNECTION_REQUEST_CODE);
			} catch (SendIntentException e) {
				// There was an error with the resolution intent. Try again.
				mGoogleApiClient.connect();
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(), this, 0).show();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient,
				Drive.DriveApi.getRootFolder(mGoogleApiClient).getDriveId());
		folder.listChildren(mGoogleApiClient).setResultCallback(this);

		// Drive.DriveApi.newContents(mGoogleApiClient).setResultCallback(
		// contentsCallback);
	}

	final ResultCallback<ContentsResult> contentsCallback = new ResultCallback<ContentsResult>() {

		@Override
		public void onResult(ContentsResult result) {
			MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
					.setMimeType("text/html").build();
			IntentSender intentSender = Drive.DriveApi
					.newCreateFileActivityBuilder()
					.setInitialMetadata(metadataChangeSet)
					.setInitialContents(result.getContents())
					.build(mGoogleApiClient);
			try {
				startIntentSenderForResult(intentSender, REQUEST_CODE_CREATOR,
						null, 0, 0, 0);
			} catch (SendIntentException e) {

			}
		}
	};

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case RESOLVE_CONNECTION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			}
			if (resultCode == RESULT_CANCELED) {
				mResolvingError = false;
			}
			break;
		case REQUEST_CODE_CREATOR:
			if (resultCode == RESULT_OK) {
				DriveId driveId = (DriveId) data
						.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

			}
			break;
		}
	}

	@Override
	public void onResult(MetadataBufferResult result) {
		// TODO Auto-generated method stub
		if (!result.getStatus().isSuccess()) {
			return;
		}
		ArrayList array = new ArrayList<String>();
		MetadataBuffer abcd = result.getMetadataBuffer();
		for (int i = 0; i < abcd.getCount(); i++) {
			Metadata asdf = abcd.get(i);
			array.add(asdf.getTitle());

		}
		FragmentManager fragmentManager = getFragmentManager();
		// Get the book description fragment
		MyListFragment mMyListFragment = (MyListFragment) fragmentManager
				.findFragmentById(R.id.country_fragment);
		// Display the book title
		if (mMyListFragment != null)
			mMyListFragment.updateArray(array);
	}

}
