package com.pivotallabs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.clickOn;
import static org.robolectric.Robolectric.shadowOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pivotallabs.injected.InjectedActivity;
import com.pivotallabs.tracker.RecentActivityActivity;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {
	private HomeActivity	activity;
	private Button			pressMeButton;
	private Button			trackerRecentActivityButton;
	private Button			injectedActivityButton;
	private ImageView		pivotalLogo;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(HomeActivity.class).create().get();
		pressMeButton = (Button) activity.findViewById(R.id.press_me_button);
		trackerRecentActivityButton = (Button) activity.findViewById(R.id.tracker_recent_activity);
		injectedActivityButton = (Button) activity.findViewById(R.id.injected_activity_button);
		pivotalLogo = (ImageView) activity.findViewById(R.id.pivotal_logo);
	}

	@Test
	public void shouldHaveAButtonThatSaysPressMe() throws Exception {
		assertThat((String) pressMeButton.getText(), equalTo("Tests Rock!"));
	}

	@Test
	public void pressingTheButtonShouldStartTheListActivity() throws Exception {
		pressMeButton.performClick();

		final ShadowActivity shadowActivity = shadowOf(activity);
		final Intent startedIntent = shadowActivity.getNextStartedActivity();
		final ShadowIntent shadowIntent = shadowOf(startedIntent);
		assertThat(shadowIntent.getComponent().getClassName(), equalTo(NamesActivity.class.getName()));
	}

	@Test
	public void pressingTheButtonShouldStartTheSignInActivity() throws Exception {
		trackerRecentActivityButton.performClick();

		final ShadowActivity shadowActivity = shadowOf(activity);
		final Intent startedIntent = shadowActivity.getNextStartedActivity();
		final ShadowIntent shadowIntent = shadowOf(startedIntent);

		assertThat(shadowIntent.getComponent().getClassName(), equalTo(RecentActivityActivity.class.getName()));
	}

	@Test
	public void shouldHaveALogo() throws Exception {
		assertThat(pivotalLogo.getVisibility(), equalTo(View.VISIBLE));
		assertThat(shadowOf(pivotalLogo.getDrawable()).getLoadedFromResourceId(),
				equalTo(R.drawable.pivotallabs_logo));
	}

	@Test
	public void shouldLaunchInjectedActivity() throws Exception {
		clickOn(injectedActivityButton);

		final ShadowActivity shadowActivity = shadowOf(activity);
		final Intent startedIntent = shadowActivity.getNextStartedActivity();
		assertNotNull(startedIntent);
		final ShadowIntent shadowIntent = shadowOf(startedIntent);
		assertThat(shadowIntent.getComponent().getClassName(), equalTo(InjectedActivity.class.getName()));
	}
}
