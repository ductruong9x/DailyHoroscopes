package com.appfree.dailyhoroscopes;

import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;

public class DialogShare extends Dialog {
	private Button btnCancel, btnShare;
	private EditText edShare;
	private ProgressDialog dialog;

	public DialogShare(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_dialog);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnShare = (Button) findViewById(R.id.btnShare);
		edShare = (EditText) findViewById(R.id.tvShare);
		dialog = new ProgressDialog(getContext());
		dialog.setMessage("Sharing...");
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btnShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edShare.getText().toString())) {
					Toast.makeText(getContext(), "No content",
							Toast.LENGTH_SHORT).show();
				} else {

					try {
						if (!Session.getActiveSession().getPermissions()
								.contains("publish_actions")) {
							NewPermissionsRequest request = new NewPermissionsRequest(
									(Activity) getContext(), Arrays
											.asList("publish_actions"));

							Session.getActiveSession()
									.requestNewPublishPermissions(request);
							return;
						}
					} catch (Exception e) {

					}
					dialog.show();
					Bundle postParams = new Bundle();
					postParams.putString("name", "Tử vi hàng ngày");
					postParams.putString("message", edShare.getText()
							.toString() + "\n" + MyApplication.getContent());
					postParams
							.putString("description",
									"Ứng dụng xem tử vi hàng ngày 12 cung hoàng đạo cho Android");
					postParams
							.putString("link",
									"https://play.google.com/store/apps/details?id=com.truongtvd.tuvi");

					Request.Callback callback = new Request.Callback() {
						public void onCompleted(Response response) {
							dialog.dismiss();
							dismiss();
							Toast.makeText(getContext(), "Share successfuly	",
									Toast.LENGTH_SHORT).show();
						}
					};

					Request request = new Request(Session.getActiveSession(),
							"me/feed", postParams, HttpMethod.POST, callback);

					RequestAsyncTask task = new RequestAsyncTask(request);
					task.execute();

				}
			}
		});
	}

	// private class ShareFacebook extends AsyncTask<Void, Void, Void> {
	//
	// private ProgressDialog dialog;
	//
	// public ShareFacebook() {
	// dialog = new ProgressDialog(getContext());
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// dialog.setMessage("Sharing...");
	// dialog.show();
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	//
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// }

}
