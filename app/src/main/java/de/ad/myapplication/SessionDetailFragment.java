package de.ad.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.ad.myapplication.bl.Api;
import de.ad.myapplication.bl.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link SessionListActivity}
 * in two-pane mode (on tablets) or a {@link SessionDetailActivity}
 * on handsets.
 */
public class SessionDetailFragment extends Fragment {
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String ARG_ITEM_ID = "item_id";

  /**
   * The dummy content this fragment is presenting.
   */
  private Session mItem;
  private TextView tvDetails;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public SessionDetailFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ITEM_ID)) {
      String id = getArguments().getString(ARG_ITEM_ID);

      Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.SERVER)
          .addConverterFactory(GsonConverterFactory.create())
          .build();

      final Api api = retrofit.create(Api.class);

      api.loadSession(id).enqueue(new Callback<Session>() {
        @Override public void onResponse(Call<Session> call, Response<Session> response) {
          mItem = response.body();

          Activity activity = getActivity();
          CollapsingToolbarLayout appBarLayout =
              (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
          if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getTitle());
          }

          tvDetails.setText(mItem.getSpeaker() + "\n\n" + mItem.getDescription() +
              "\n\n" + mItem.getTags());
        }

        @Override public void onFailure(Call<Session> call, Throwable t) {
          Log.d("a", t.getMessage());
        }
      });
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);

    tvDetails = ((TextView) rootView.findViewById(R.id.item_detail));

    return rootView;
  }
}
