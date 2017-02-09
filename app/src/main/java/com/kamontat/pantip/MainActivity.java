package com.kamontat.pantip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
	private ArrayList<Topic> topicList;
	private ArrayAdapter<Topic> topicAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initListView();
	}

	public void initListView() {
		topicList = new ArrayList<>();
		topicAdapter = new ArrayAdapter<Topic>(this, android.R.layout.simple_list_item_1, topicList);

		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(topicAdapter);

		new TrendTopicTask().execute();
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
				URL pantipAPI = new URL("https://pantip.com/home/ajax_pantip_trend?p=1");
				HttpURLConnection urlConnection = (HttpURLConnection) pantipAPI.openConnection();
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
					int id = jsonObject.getInt("topic_id");
					String title = jsonObject.getString("disp_topic");
					result.add(new Topic(id, title));
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

		@Override
		protected void onPostExecute(ArrayList<Topic> topics) {
			if (topics != null) {
				topicAdapter.clear();
				topicAdapter.addAll(topics);
			}
		}
	}
}
