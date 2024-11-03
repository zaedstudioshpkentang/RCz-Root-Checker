package com.my.chck.z;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
	
	private Toolbar toolbar;
	private Button checkButton;
	private TextView resultTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		checkButton = findViewById(R.id.checkButton);
		resultTextView = findViewById(R.id.resultTextView);
		
		checkButton.setOnClickListener(v -> checkRootAccess());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.about) {
			showAboutDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void checkRootAccess() {
		if (isDeviceRooted()) {
			resultTextView.setText("Device is rooted.");
			} else {
			resultTextView.setText("Device is not rooted.");
		}
	}
	
	private boolean isDeviceRooted() {
		return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
	}
	
	private boolean checkRootMethod1() {
		String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"};
		for (String path : paths) {
			if (new File(path).exists()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkRootMethod2() {
		Process process;
		try {
			process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			if (in.readLine() != null) {
				return true;
			}
			process.waitFor();
			} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkRootMethod3() {
		File file = new File("/system/app/Superuser.apk");
		return file.exists();
	}
	
	private void showAboutDialog() {
		String appVersion = BuildConfig.VERSION_NAME; // Mengambil versi aplikasi dari file build.gradle
		
		String description = "This is a sample app to check if your device is rooted.\n\n" +
		"Version: " + appVersion + "\n\n" +
		"Developed by: Zaed";
		
		new MaterialAlertDialogBuilder(this)
		.setTitle("About")
		.setMessage(description)
		.setPositiveButton("OK", null)
		.show();
	}
}