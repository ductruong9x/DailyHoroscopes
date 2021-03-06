package com.appfree.dailyhoroscopes.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.appfree.dailyhoroscopes.MyApplication;
import com.appfree.dailyhoroscopes.R;
import com.appfree.dailyhoroscopes.adapter.ItemAdapter;
import com.appfree.dailyhoroscopes.model.ItemNewFeed;
import com.appfree.dailyhoroscopes.network.MyVolley;
import com.appfree.dailyhoroscopes.network.NetworkOperator;
import com.appfree.dailyhoroscopes.util.JsonUtils;
import com.appfree.dailyhoroscopes.view.FadeInNetworkImageView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class NewsFeedFragment extends Fragment {
	private View mParent;
	private NetworkOperator operator;
	private String COVER_URL = "";
	private Session session;
	private int limit = 300;
	private String avatar = "";
	private String nameUser = "";
	private String id = "";
	private ListView lvNewFeed;
	private View header;
	private FadeInNetworkImageView imgCover;
	private TextView tvTitle, tvMember;
	private String title;
	private int member;
	private ProgressBar loading;
	private ItemAdapter adapter;
	private ArrayList<ItemNewFeed> listItem = new ArrayList<ItemNewFeed>();
	private boolean check = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		operator = NetworkOperator.getInstance().init(getActivity());
		session = Session.getActiveSession();
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mParent = inflater.inflate(R.layout.fragment_main, null);
		lvNewFeed = (ListView) mParent.findViewById(R.id.lvNewsFeed);
		loading = (ProgressBar) mParent.findViewById(R.id.loading);
		header = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.layout_header, null);
		imgCover = (FadeInNetworkImageView) header.findViewById(R.id.imgBanner);
		tvTitle = (TextView) header.findViewById(R.id.tvTitle);
		tvMember = (TextView) header.findViewById(R.id.tvMember);
		return mParent;
	}

	public void init() {
		if (check) {

			return;
		}
		getFanpageInfo();
		getIDUser();

		check = true;
	}

	private void getIDUser() {
		Request request = Request.newMeRequest(session,
				new GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						try {
							id = user.getId();
							getUserInfo(id);
						} catch (Exception e) {

						}
					}
				});
		Request.executeBatchAsync(request);
	}

	private void getUserInfo(String id) {
		String fqlQuery = "SELECT name,pic FROM user WHERE uid='" + id + "'";
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		// session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						JSONObject jso = JsonUtils.parseResponToJson(response);
						try {
							JSONArray data = jso.getJSONArray("data");
							if (data.length() > 0) {
								JSONObject info = data.getJSONObject(0);
								avatar = info.getString("pic");
								nameUser = info.getString("name");
								MyApplication.setAvater(avatar);
								MyApplication.setName(nameUser);
								getNewFeed(limit);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Log.e("USER_INFO", jso.toString());

					}
				});
		Request.executeBatchAsync(request);

	}

	private void getNewFeed(int limit) {
		String fqlQuery = "SELECT post_id, message, attachment,created_time,like_info FROM stream WHERE source_id = '306103412789329' LIMIT "
				+ limit;
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		// session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						JSONObject jso = JsonUtils.parseResponToJson(response);
						// Util.writetoFile(jso.toString(), "TUVI");
						loading.setVisibility(View.GONE);
						listItem = JsonUtils.getListItem(jso, listItem);
						adapter = new ItemAdapter(getActivity(),
								R.layout.layout_item, listItem);
						lvNewFeed.addHeaderView(header);
						lvNewFeed.setAdapter(adapter);
						// Log.e("LIST_SIZE", listItem.size() + "");

						// Log.e("NEW", jso.toString());

					}
				});
		Request.executeBatchAsync(request);

	}

	private void getFanpageInfo() {
		operator.getFanpageInfo(getInfoSuccess(), getInfoError());
	}

	private Listener<JSONObject> getInfoSuccess() {
		// TODO Auto-generated method stub
		return new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0, String arg1) {
				// TODO Auto-generated method stub
				// Log.e("INFO", arg0.toString());
				try {
					title = arg0.getString("name");
					member = arg0.getInt("likes");
					JSONObject cover = arg0.getJSONObject("cover");
					COVER_URL = cover.getString("source");
					tvTitle.setText("" + title);
					tvMember.setText(getString(R.string.member, member));
					SharedPreferences sharedPreferences = getActivity()
							.getSharedPreferences("SETTING",
									Context.MODE_PRIVATE);
					boolean load = sharedPreferences.getBoolean("LOAD", true);
					if (load) {
						imgCover.setImageUrl(COVER_URL,
								MyVolley.getImageLoader());
					} else {
						imgCover.setImageResource(R.drawable.bg_item);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	private ErrorListener getInfoError() {
		// TODO Auto-generated method stub
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error, String arg1) {
				// TODO Auto-generated method stub
				error.printStackTrace();
			}
		};
	}

}
