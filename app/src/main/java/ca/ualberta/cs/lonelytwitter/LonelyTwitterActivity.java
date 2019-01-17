package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LonelyTwitterActivity extends Activity {

	private static final String FILENAME = "file.sav";
	private EditText bodyText;
	private ListView oldTweetsList;
	private ArrayList<Tweet> tweetList;
	private ArrayAdapter<Tweet> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		Button saveButton = (Button) findViewById(R.id.save);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
		tweetList = new ArrayList<Tweet>();

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_OK);
				String text = bodyText.getText().toString();

				try {
					Tweet tweet = new NormalTweet();
					tweet.setMessage(text);

					tweetList.add(tweet);

					saveInFile();
					finish();

				} catch (TweetTooLongException e) {
					e.printStackTrace();
				}


			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
//		String[] tweets = loadFromFile();
		adapter = new ArrayAdapter<Tweet>(this,
				R.layout.list_item, tweetList);
		oldTweetsList.setAdapter(adapter);
	}

	private void loadFromFile() {
//		ArrayList<String> tweets = new ArrayList<String>();

		try {
//			FileInputStream fis = openFileInput(FILENAME);
//			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			FileReader in = new FileReader(new File(getFilesDir(), FILENAME));

			Gson gson = new Gson();

			// taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt on Jan 16
			Type type = new TypeToken<ArrayList<Tweet>>(){}.getType();
			tweetList = gson.fromJson(in, type);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveInFile(String text, Date date) {
		try {

		    NormalTweet myTweet = new NormalTweet("");
		    myTweet.setMessage("I am looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong message");

		    FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_APPEND);
			fos.write(new String(date.toString() + " | " + text)
					.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TweetTooLongException e) {
            e.printStackTrace();
        }
	}

	private void saveInFile() {
		try {

			FileWriter out = new FileWriter(new File(getFilesDir(), FILENAME));

			Gson gson = new Gson();

			gson.toJson(tweetList, out);

		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}