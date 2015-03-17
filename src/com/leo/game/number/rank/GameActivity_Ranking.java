package com.leo.game.number.rank;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestQueueManager;
import com.leo.game.number.GameApplication;
import com.leo.game.number.R;

public class GameActivity_Ranking extends FragmentActivity {

	BasicTitle basicTitle;
	GameApplication mApp;
	BaseLoadingView mLoadingView;
	LinearLayout layoutNetError;
	LinearLayout layoutEmpty;
	ListView mListView;
	ArrayList<RankingListItem> m_vItems;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mApp = (GameApplication) getApplication();
		setContentView(R.layout.activity_ranking);
		basicTitle = (BasicTitle) findViewById(R.id.BasicTitle_root);
		basicTitle.setTitle("2048");
		basicTitle.setLBtn(R.drawable.icon_arrow_l_white, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mLoadingView = (BaseLoadingView) findViewById(R.id.GameRanking_LoadingView);
		mListView = (ListView) findViewById(R.id.GameRanking_List);
		layoutNetError = (LinearLayout) findViewById(R.id.GameRanking_NetError);
		layoutEmpty = (LinearLayout) findViewById(R.id.GameRanking_Eempty);
		findViewById(R.id.GameRanking_NetRefresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getRankingList();
			}
		});
		getRankingList();
	}

	private void setContentTip(String tip) {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.GONE);
			layoutNetError.setVisibility(View.VISIBLE);
		}
	}

	private void setContentEmpty() {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.GONE);
			layoutEmpty.setVisibility(View.VISIBLE);
		}
	}

	private void setContentLoading() {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
			layoutNetError.setVisibility(View.GONE);
		}
	}

	private void setContentList() {
		mLoadingView.setVisibility(View.GONE);
		RankingListAdapter adapter = new RankingListAdapter(this, R.layout.ranking_listitem, m_vItems);
		mListView.setAdapter(adapter);
	}

	private void setRankingListItems(JSONArray jsonArray) throws JSONException {
		if (m_vItems == null) {
			m_vItems = new ArrayList<RankingListItem>();
		}
		m_vItems.clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String display_name = jsonObject.getString("display_name");
			String score = jsonObject.getString("score");
			String username = jsonObject.getString("username");
			RankingListItem item = new RankingListItem();
			item.setDisplay_name(display_name);
			item.setScore(score);
			item.setUsername(username);
			m_vItems.add(item);
		}
	}

	private void getRankingList() {
		setContentLoading();
		HashMap<String, Object> vParams = new HashMap<String, Object>();
		vParams.put("imei", mApp.getGameInfo().getImei());
		RequestQueueManager.getInstance().postJsonRequest(this, GameUrl.getUrl_getRank(), new JSONObject(vParams), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					int code = response.getInt("code");
					if (code == 2401) {
						JSONArray jsonArray = response.getJSONArray("ext");
						setRankingListItems(jsonArray);
						setContentList();
					} else {
						if (code == 2904) {
							setContentEmpty();
						} else {
							String msg = response.getString("msg");
							setContentTip(msg);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.toString());
					setContentTip("网络不给力");
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				setContentTip("网络不给力");
			}
		});
	}

	class RankingListAdapter extends ArrayAdapter<RankingListItem> {
		LayoutInflater mLayoutInflater;
		int resId;

		public RankingListAdapter(Context context, int resource, ArrayList<RankingListItem> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			this.mLayoutInflater = LayoutInflater.from(context);
			this.resId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final RankingListItem item = getItem(position);
			if (null == item) {
				return convertView;
			}
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(resId, null);
			}
			TextView txtRank = (TextView) convertView.findViewById(R.id.RankingListitem_rank);
			TextView txtTitle = (TextView) convertView.findViewById(R.id.RankingListitem_displayName);
			TextView txtHighestScore = (TextView) convertView.findViewById(R.id.RankingListitem_highestScore);
			txtRank.setText(position + 1 + "");
			txtTitle.setText(item.getDisplay_name());
			txtHighestScore.setText(item.getScore());
			if (position < 3) {
				txtRank.setTextColor(getResources().getColor(R.color.orange));
			} else {
				txtRank.setTextColor(getResources().getColor(R.color.black));
			}
			TextView txtUserName = (TextView) convertView.findViewById(R.id.RankingListitem_userName);
			txtUserName.setText(item.getUsername());
			return convertView;
		}
	}
}
