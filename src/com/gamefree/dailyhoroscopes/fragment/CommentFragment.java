package com.gamefree.dailyhoroscopes.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.gamefree.dailyhoroscopes.MyApplication;
import com.gamefree.dailyhoroscopes.R;
import com.gamefree.dailyhoroscopes.adapter.CommentItemAdapter;
import com.gamefree.dailyhoroscopes.model.CommentInfo;
import com.gamefree.dailyhoroscopes.util.JsonUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CommentFragment extends Fragment {
	private View mParent;
	private String query = "SELECT text,time,fromid FROM comment WHERE post_id = '"
			+ MyApplication.getPost_id() + "' ORDER BY time DESC LIMIT " + 300;

	String query_3 = "{'comment_data':'SELECT text,fromid,time FROM comment WHERE post_id ='"
			+ MyApplication.getPost_id()
			+ "' LIMIT "
			+ 300
			+ "', 'user_data':'SELECT uid,name,pic FROM user WHERE uid IN (SELECT fromid FROM #comment_data)'}";

	private Session session;
	private boolean isLoad = false;
	private ArrayList<CommentInfo> listComment = new ArrayList<CommentInfo>();
	private CommentItemAdapter adapter;
	private ListView lvComment;
	private ProgressBar loading;
	private ImageView imgMyAvatar;
	private ImageLoader imvLoader;
	private DisplayImageOptions options;
	private EditText edComment;
	private Button btnComment;
	private Typeface font_light;
	private ProgressDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mParent = inflater.inflate(R.layout.layout_comment, null);

		font_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Light.ttf");
		loading = (ProgressBar) mParent.findViewById(R.id.loading);
		lvComment = (ListView) mParent.findViewById(R.id.lvComment);
		imgMyAvatar = (ImageView) mParent.findViewById(R.id.imgMyAvatar);
		edComment = (EditText) mParent.findViewById(R.id.edComment);
		btnComment = (Button) mParent.findViewById(R.id.btnComment);
		edComment.setTypeface(font_light);
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Posting...");
		session = Session.getActiveSession();
		initClickPostComment();
		initImageLoader();
		imvLoader.displayImage(MyApplication.getAvater(), imgMyAvatar, options,
				imageload);
		return mParent;
	}

	private void initClickPostComment() {
		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edComment.getText().toString())) {
					Toast.makeText(getActivity(), "Comment is emply",
							Toast.LENGTH_LONG).show();
				} else {

					new CommentFacebook(getActivity()).execute(MyApplication
							.getPost_id());
				}
			}
		});
	}

	private void initImageLoader() {
		imvLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_default)
				.showImageOnFail(R.drawable.ic_default).cacheInMemory(true)
				.cacheOnDisc(false).displayer(new RoundedBitmapDisplayer(30))
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		imvLoader.init(config);
	}

	public void init() {
		if (isLoad) {
			return;
		}
		getComment();
		isLoad = true;

	}

	private void getComment() {

		JSONObject json = new JSONObject();
		try {
			json.put("comment_data",
					"SELECT text,time,fromid FROM comment WHERE post_id = '"
							+ MyApplication.getPost_id() + "' LIMIT " + 300);

			json.put("user_data",
					"SELECT uid,name,pic FROM user WHERE uid IN (SELECT fromid FROM #comment_data)");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bundle params = new Bundle();
		params.putString("q", json.toString());
		// session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						JSONObject jso = JsonUtils.parseResponToJson(response);
						loading.setVisibility(View.GONE);
						listComment = JsonUtils
								.getCommentInfo(jso, listComment);
						adapter = new CommentItemAdapter(getActivity(),
								R.layout.item_comment, listComment);
						lvComment.setAdapter(adapter);
						Log.e("SIZE_COMMENT", listComment.size() + "");
						Log.e("COMMENT", jso.toString());

					}
				});
		Request.executeBatchAsync(request);

	}

	private ImageLoadingListener imageload = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			// Log.e("ERROR", "load eror");

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			// btnMenu.setImageBitmap(loadedImage);
			// Log.e("COM", "load com");
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}
	};

	private class CommentFacebook extends AsyncTask<String, Void, Void> {
		private Context context;

		public CommentFacebook(Context context) {
			this.context = context;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// dialog.dismiss();
			CommentInfo comment = new CommentInfo();
			int unixTime = (int) (System.currentTimeMillis() / 1000);
			comment.setAvatar(MyApplication.getAvater());
			comment.setComment(edComment.getText().toString() + "");
			comment.setTime(unixTime);
			comment.setUsername(MyApplication.getName());
			listComment.add(comment);
			adapter.notifyDataSetChanged();
			edComment.setText("");
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
			dialog.dismiss();
			lvComment.setSelection(listComment.size() - 1);
			Toast.makeText(getActivity(), "Comment done", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				if (!Session.getActiveSession().getPermissions()
						.contains("publish_actions")) {
					NewPermissionsRequest request = new NewPermissionsRequest(
							getActivity(), Arrays.asList("publish_actions"));

					Session.getActiveSession().requestNewPublishPermissions(
							request);
					return null;
				}
			} catch (Exception e) {

			}
			Bundle bundle = new Bundle();
			bundle.putString("message", edComment.getText().toString() + "");
			Request commentRequest = new Request(Session.getActiveSession(),
					params[0] + "/comments", bundle, HttpMethod.POST,
					new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							Log.i("Comment", response.toString());
						}
					});
			Request.executeBatchAndWait(commentRequest);

			return null;
		}

	}

}
