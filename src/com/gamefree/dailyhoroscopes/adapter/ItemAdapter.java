package com.gamefree.dailyhoroscopes.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamefree.dailyhoroscopes.DetailActivity;
import com.gamefree.dailyhoroscopes.R;
import com.gamefree.dailyhoroscopes.model.ItemNewFeed;
import com.gamefree.dailyhoroscopes.network.MyVolley;
import com.gamefree.dailyhoroscopes.util.Util;
import com.gamefree.dailyhoroscopes.view.FadeInNetworkImageView;

public class ItemAdapter extends ArrayAdapter<ItemNewFeed> {

	private Context context;
	private ArrayList<ItemNewFeed> listItem = null;
	private SharedPreferences sharedPreferences;
	private boolean isLoad;

	public ItemAdapter(Context context, int resource,
			ArrayList<ItemNewFeed> listItem) {
		super(context, resource, listItem);
		this.context = context;
		this.listItem = listItem;
		sharedPreferences = context.getSharedPreferences("SETTING",
				Context.MODE_PRIVATE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		isLoad = sharedPreferences.getBoolean("LOAD", true);
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tvTime_item);
			viewHolder.imgItem = (FadeInNetworkImageView) convertView
					.findViewById(R.id.imgItem);
			viewHolder.btnShare = (RelativeLayout) convertView
					.findViewById(R.id.btnShare);
			viewHolder.btnView = (RelativeLayout) convertView
					.findViewById(R.id.btnView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ItemNewFeed item = getItem(position);
		viewHolder.tvContent.setText(item.getMessage());
		Linkify.addLinks(viewHolder.tvContent, Linkify.ALL);
		final String time = Util.convertDate(item.getTime()) + "";
		viewHolder.tvTime.setText(time);
		if (isLoad == true && !TextUtils.isEmpty(item.getImage())) {
			viewHolder.imgItem.setImageUrl(item.getImage(),
					MyVolley.getImageLoader());
		}
		viewHolder.btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("TIME", time);
				bundle.putString("CONTENT", item.getMessage());
				bundle.putString("POST_ID", item.getPost_id());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("TIME", time);
				bundle.putString("CONTENT", item.getMessage());
				bundle.putString("POST_ID", item.getPost_id());
				bundle.putInt("LIKE_COUNT", item.getLike_count());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		viewHolder.btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView tvTime, tvContent;
		private FadeInNetworkImageView imgItem;
		private RelativeLayout btnView, btnShare;
	}

}
