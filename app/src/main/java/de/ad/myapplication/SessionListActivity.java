package de.ad.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.ad.myapplication.bl.Api;
import de.ad.myapplication.bl.Session;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SessionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SessionListActivity extends AppCompatActivity {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    View recyclerView = findViewById(R.id.item_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);

    if (findViewById(R.id.item_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }
  }

  private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.SERVER)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    final Api api = retrofit.create(Api.class);

    api.loadSessions().enqueue(new Callback<List<Session>>() {
      @Override public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(response.body()));
      }

      @Override public void onFailure(Call<List<Session>> call, Throwable t) {
        Log.d("a", t.getMessage());
      }
    });
  }

  public class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final List<Session> mValues;

    public SimpleItemRecyclerViewAdapter(List<Session> items) {
      mValues = items;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mItem = mValues.get(position);
      holder.title.setText(mValues.get(position).getTitle());
      holder.speaker.setText(mValues.get(position).getSpeaker());
      holder.tags.setText(mValues.get(position).getTags());

      holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(SessionDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
            SessionDetailFragment fragment = new SessionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
          } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, SessionDetailActivity.class);
            intent.putExtra(SessionDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

            context.startActivity(intent);
          }
        }
      });
    }

    @Override public int getItemCount() {
      return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public final View mView;
      public final TextView title;
      public final TextView speaker;
      public final TextView tags;
      public Session mItem;

      public ViewHolder(View view) {
        super(view);
        mView = view;
        title = (TextView) view.findViewById(R.id.tvTitle);
        speaker = (TextView) view.findViewById(R.id.tvSpeaker);
        tags = (TextView) view.findViewById(R.id.tvTags);
      }

      @Override public String toString() {
        return super.toString() + " '" + title.getText() + "'";
      }
    }
  }
}
