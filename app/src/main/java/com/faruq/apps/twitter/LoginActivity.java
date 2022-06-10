package com.faruq.apps.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.faruq.apps.twitter.databinding.ActivityLoginBinding;
import com.faruq.apps.twitter.utils.FormatNumbers;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println(FormatNumbers.ShortenNumber(19250));
		System.out.println(FormatNumbers.ShortenNumber(627101317));
		System.out.println(FormatNumbers.ShortenNumber(20231939));
		super.onCreate(savedInstanceState);
		ActivityLoginBinding binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
		setContentView(binding.getRoot());

		Toolbar toolbar = binding.toolbar;

		setSupportActionBar(toolbar);

	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
