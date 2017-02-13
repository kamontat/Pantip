package com.kamontat.pantip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kamontat.model.Topic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MainActivity extends AppCompatActivity {
	private static final String pantipAPI = "https://pantip.com/home/ajax_pantip_trend?p=1";
	private static final String TAG = "Main";
	ProgressDialog progress;

	private ArrayList<Topic> topicList;
	private ArrayAdapter<Topic> topicAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);

		initListView();
	}

	public void initListView() {
		topicList = new ArrayList<>();
		topicAdapter = new ArrayAdapter<Topic>(this, android.R.layout.simple_list_item_1, topicList);

		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(topicAdapter);

		onClickInListView(listView);

		new TrendTopicTask().execute();
	}

	public void onClickInListView(ListView listView) {
		final Context self = this;
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Topic topic = (Topic) parent.getItemAtPosition(position);
				startActivity(new Intent(self, ShowActivity.class).putExtra("topic", topic));
			}
		});
	}

	public void refreshTopic(View view) {
		updateTopics();
	}

	public void updateTopics() {
		new TrendTopicTask().execute();
	}

	class TrendTopicTask extends AsyncTask<Void, Void, ArrayList<Topic>> {

		@Override
		protected ArrayList<Topic> doInBackground(Void... params) {
			ArrayList<Topic> result = new ArrayList<>();
			try {
				HttpURLConnection urlConnection = (HttpURLConnection) new URL(pantipAPI).openConnection();
				BufferedReader buffered = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder jsonResult = new StringBuilder();
				String line;

				while ((line = buffered.readLine()) != null) {
					jsonResult.append(line).append("\n");
				}
				buffered.close();
				JSONObject jsonObjects = new JSONObject(jsonResult.toString());
				JSONArray trends = jsonObjects.getJSONArray("trend");

				for (int i = 0; i < trends.length(); i++) {
					JSONObject jsonObject = trends.getJSONObject(i);
					int id = jsonObject.getInt(Topic.ID_JSON);
					String title = jsonObject.getString(Topic.TITLE_JSON);
					String body = jsonObject.getString(Topic.SHORT_BODY_JSON);
					result.add(new Topic(id, title, body));
				}

				return result;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		// before start
		@Override
		protected void onPreExecute() {
			progress.setTitle("Start loading..");
			progress.show();
		}

		// while running
		@Override
		protected void onProgressUpdate(Void... values) {
			progress.setTitle("Running...");
		}

		// after start
		@Override
		protected void onPostExecute(ArrayList<Topic> topics) {
			if (topics != null) {
				topicAdapter.clear();
				topicAdapter.addAll(topics);
				progress.setTitle("Complete loaded");
			}
			progress.dismiss();
		}

		// if cancel
		@Override
		protected void onCancelled(ArrayList<Topic> topics) {
			progress.setTitle("Cancel load");
			progress.dismiss();
		}
	}
}
