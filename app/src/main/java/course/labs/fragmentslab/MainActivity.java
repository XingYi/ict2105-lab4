package course.labs.fragmentslab;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.app.FragmentTransaction;


public class MainActivity extends Activity implements
		FriendsFragment.SelectionListener {

	private static final String TAG = "Lab-Fragments";

	private FriendsFragment mFriendsFragment;
	private FeedFragment mFeedFragment;

    // Added an extra class scope variable to store the position into savedInstanceState
    // Assign -1 as a default value to indicate that no friend list has been selected
    private int mPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// If the layout is single-pane, create the FriendsFragment 
		// and add it to the Activity

		if (!isInTwoPaneMode()) {
			
			mFriendsFragment = new FriendsFragment();

			//TODO - add the FriendsFragment to the fragment_container

            // get fragment manager
            FragmentManager fm = getFragmentManager();

            // add transaction
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, mFriendsFragment);

            // Commit the transaction
            ft.commit();

		} else {

			// Otherwise, save a reference to the FeedFragment for later use

			mFeedFragment = (FeedFragment) getFragmentManager()
					.findFragmentById(R.id.feed_frag);

            Log.i(TAG, "isInTwoPaneMode");

            // Restore the FeedFragment display feed
            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mPosition = savedInstanceState.getInt("position");

                // If a selection has been in the Friend listView
                if(mPosition != -1) {
                    // Update Twitter feed display on FriendFragment
                    mFeedFragment.updateFeedDisplay(mPosition);
                }

                Log.i(TAG, "MainActivity - get savedInstanceState: " + mPosition);
            }
		}

	}



        //TODO - What else can be done when the screen is rotated?

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);

        outState.putInt("position", mPosition);

        Log.d(TAG, "MainActivity - onSaveInstanceState: " + mPosition);
    }


	// If there is no fragment_container ID, then the application is in
	// two-pane mode

	private boolean isInTwoPaneMode() {

		return findViewById(R.id.fragment_container) == null;
	
	}

	// Display selected Twitter feed

	public void onItemSelected(int position) {

		Log.i(TAG, "Entered onItemSelected(" + position + ")");

		// If there is no FeedFragment instance, then create one

		if (mFeedFragment == null)
			mFeedFragment = new FeedFragment();

		// If in single-pane mode, replace single visible Fragment

		if (!isInTwoPaneMode()) {

			//TODO - replace the fragment_container with the FeedFragment
            Log.i(TAG, "Not isInTwoPaneMode");
            // get fragment manager
            FragmentManager fm = getFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();

            // retain this fragment across configuration changes.
            mFeedFragment.setRetainInstance(true);

            // replace the fragment
            ft.replace(R.id.fragment_container, mFeedFragment);

            // add the transaction to the back stack so the user can navigate back
            ft.addToBackStack(null);

            // commit the transaction
            ft.commit();

			// execute transaction now
			getFragmentManager().executePendingTransactions();

		}

		// Update Twitter feed display on FriendFragment
		mFeedFragment.updateFeedDisplay(position);


        // store the position into the private class scope variable.
        mPosition = position;
	}

}
