package com.pivotallabs.injected;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import roboguice.RoboGuice;
import android.content.Context;
import android.widget.TextView;

import com.google.inject.Inject;
import com.pivotallabs.R;

@RunWith(RobolectricTestRunner.class)
public class InjectedActivityTest {
	@Inject
	Context				context;

	InjectedActivity	injectedActivity;
	@Inject
	Counter				fieldCounter;
	@Inject
	FakeDateProvider	fakeDateProvider;

	@Before
	public void setUp() {
		injectedActivity = new InjectedActivity();
		fakeDateProvider.setDate("December 8, 2010");
	}

	@Test
	public void shouldAssignStringToTextView() throws Exception {
		injectedActivity.onCreate(null);
		final TextView injectedTextView = (TextView) injectedActivity.findViewById(R.id.injected_text_view);
		assertThat(injectedTextView.getText().toString(), equalTo("Roboguice Activity tested with Robolectric - December 8, 2010"));
	}

	@Test
	public void shouldInjectSingletons() throws Exception {
		final Counter instance = RoboGuice.getInjector(injectedActivity).getInstance(Counter.class);
		assertEquals(0, instance.count);

		instance.count++;

		final Counter instanceAgain = RoboGuice.getInjector(injectedActivity).getInstance(Counter.class);
		assertEquals(1, instanceAgain.count);

		assertSame(fieldCounter, instance);
	}

	@Test
	public void shouldBeAbleToInjectAContext() throws Exception {
		assertNotNull(context);
	}

}
