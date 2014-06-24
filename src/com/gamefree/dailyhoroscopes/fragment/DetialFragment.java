package com.gamefree.dailyhoroscopes.fragment;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.gamefree.dailyhoroscopes.MyApplication;
import com.gamefree.dailyhoroscopes.R;
import com.gamefree.dailyhoroscopes.network.NetworkOperator;
import com.gamefree.dailyhoroscopes.util.AnimationUtil;
import com.gamefree.dailyhoroscopes.view.OnShareListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DetialFragment extends Fragment {
	private View mParent;
	private String time = "", content = "";
	private String avatar = "";
	private TextView tvTimeDetail, tvContentAll;
	private ImageLoader imvLoader;
	private DisplayImageOptions options;
	private ImageView btnMenu, btnShareDetail, btnLikeDetail;
	private Handler handler = new Handler();
	private NetworkOperator operator;
	private OnShareListener onShare;
	private Typeface font_light;
	private AdView adView;
	private UiLifecycleHelper uiHelper;

	public OnShareListener getOnShare() {
		return onShare;
	}

	public void setOnShare(OnShareListener onShare) {
		this.onShare = onShare;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mParent = inflater.inflate(R.layout.activity_detail, null);
		adView = (AdView) mParent.findViewById(R.id.adFragment);
		adView.loadAd(new AdRequest.Builder().build());
		adView.setAdListener(new AdListener() {

			@Override
			public void onAdOpened() {
				// TODO Auto-generated method stub
				adView.destroy();
				super.onAdOpened();
			}
		});
		font_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Light.ttf");
		operator = NetworkOperator.getInstance().init(getActivity());
		tvContentAll = (TextView) mParent.findViewById(R.id.tvAllContent);
		tvContentAll.setTypeface(font_light);
		tvTimeDetail = (TextView) mParent.findViewById(R.id.tvTimeDetail);
		btnMenu = (ImageView) mParent.findViewById(R.id.btnMenu);
		btnShareDetail = (ImageView) mParent.findViewById(R.id.btnShareDetail);
		btnLikeDetail = (ImageView) mParent.findViewById(R.id.btnLike);

		initImageLoader();
		initClickAvatar();
		initClickLike();
		initClickShare();
		avatar = MyApplication.getAvater();
		tvContentAll.setText(MyApplication.getContent());
		tvTimeDetail.setText(MyApplication.getTime());
		imvLoader.displayImage(avatar, btnMenu, options, imageload);

		return mParent;
	}

	private void initClickLike() {
		btnLikeDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("POST_ID", MyApplication.getPost_id());
				new LikeFacebook().execute(MyApplication.getPost_id());

			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void initClickShare() {
		btnShareDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				DialogShare dialogShare = new DialogShare(getActivity());
//				dialogShare.show();
				shareWeb();
			}
		});

	}

	@SuppressLint("NewApi")
	private void initClickAvatar() {
		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int x = (int) v.getX();
				int y = (int) v.getY();
				btnMenu.setVisibility(View.GONE);
				int sharex = 0 - x;
				btnShareDetail.setAnimation(AnimationUtil.translateAnimation(
						sharex, 0, 0, 0));
				btnLikeDetail.setAnimation(AnimationUtil.translateAnimation(x,
						0, 0, 0));
				btnShareDetail.setVisibility(View.VISIBLE);
				btnLikeDetail.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int xmenu = (int) btnMenu.getX();
						int menu = 0 - xmenu;
						btnShareDetail.setAnimation(AnimationUtil
								.translateAnimation(0, menu + 50, 0, 0));
						btnLikeDetail.setAnimation(AnimationUtil
								.translateAnimation(0, xmenu - 50, 0, 0));
						btnShareDetail.setVisibility(View.GONE);
						btnLikeDetail.setVisibility(View.GONE);
						btnMenu.setVisibility(View.VISIBLE);
					}
				}, 2000);
			}
		});

	}

	public void init() {
	}

	private void initImageLoader() {
		imvLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_default)
				.showImageOnFail(R.drawable.ic_default).cacheInMemory(true)
				.cacheOnDisc(false).displayer(new RoundedBitmapDisplayer(50))
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

	private ImageLoadingListener imageload = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			Log.e("ERROR", "load eror");

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			// btnMenu.setImageBitmap(loadedImage);
			Log.e("COM", "load com");
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		imvLoader.destroy();
		super.onDestroy();
	}

	private class LikeFacebook extends AsyncTask<String, Void, Void> {
		private ProgressDialog dialog;

		public LikeFacebook() {
			dialog = new ProgressDialog(getActivity());

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			Toast.makeText(getActivity(), "Like done", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("Like...");
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

			Request likeRequest = new Request(Session.getActiveSession(),
					params[0] + "/likes", null, HttpMethod.POST,
					new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							Log.i("LIKE", response.toString());
						}
					});
			Request.executeBatchAndWait(likeRequest);
			return null;
		}

	}

	private void shareWeb() {
		Bundle params = new Bundle();
		params.putString("name", getActivity().getString(R.string.app_name));
		params.putString("caption", "Download");
		params.putString("description","View daily horoscope on Android");
		params.putString("link","https://play.google.com/store/apps/details?id="+getActivity().getPackageName());

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(),
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the
							// success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getActivity(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										getActivity().getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	private void shareIt() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				MyApplication.getContent());
		sharingIntent
				.putExtra(android.content.Intent.EXTRA_TEXT,
						"https://play.google.com/store/apps/details?id=com.truongtvd.tuvi");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				MyApplication.getContent());
		startActivity(Intent.createChooser(sharingIntent,
				MyApplication.getContent()));

	}

}
