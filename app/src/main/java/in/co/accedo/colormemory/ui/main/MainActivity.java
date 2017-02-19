package in.co.accedo.colormemory.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;
import in.co.accedo.colormemory.R;
import in.co.accedo.colormemory.data.SyncService;
import in.co.accedo.colormemory.data.model.BoardTile;
import in.co.accedo.colormemory.data.model.Ribot;
import in.co.accedo.colormemory.ui.base.BaseActivity;
import in.co.accedo.colormemory.util.DialogFactory;
import in.co.accedo.colormemory.util.ScoreUpdateListener;
import in.co.accedo.colormemory.util.ViewUtil;
import in.co.accedo.colormemory.views.GridSpacingItemDecoration;
import in.co.accedo.colormemory.views.ToolbarTitleFontView;

public class MainActivity extends BaseActivity implements MainMvpView, ScoreUpdateListener {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "in.co.accedo.colormemory.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    MainPresenter mMainPresenter;
    @Inject
    GameBlocksAdapter mGameBlocksAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.high_score)
    ToolbarTitleFontView tvHighScore;

    @BindView(R.id.score)
    ToolbarTitleFontView tvScore;


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Integer highestScore = 0;
    private Integer currentScore = 0;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mGameBlocksAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.tedbottompicker_grid_layout_margin);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));
        mMainPresenter.attachView(this);
        mMainPresenter.loadRibots();
        highestScore = mMainPresenter.getHighScore();
        tvHighScore.setText("" + highestScore);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showRibots(List<Ribot> ribots) {
//        mGameBlocksAdapter.setRibots(ribots);
        mGameBlocksAdapter.notifyDataSetChanged();
    }

    @Override
    public void showGameBoard(List<BoardTile> boardTiles) {
        mGameBlocksAdapter.setRibots(boardTiles, MainActivity.this, this);
        mGameBlocksAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
                .show();
    }

    @Override
    public void showRibotsEmpty() {
        mGameBlocksAdapter.setRibots(Collections.<BoardTile>emptyList(), MainActivity.this, this);
        mGameBlocksAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_ribots, Toast.LENGTH_LONG).show();
    }


    @Override
    public void addPoint() {
        currentScore = currentScore + 2;
        tvScore.setText("" + currentScore);
        if (currentScore > highestScore) {
            highestScore = currentScore;
            tvHighScore.setText("" + highestScore);
        }

    }

    @Override
    public void removePoint() {
        currentScore = --currentScore;
        tvScore.setText("" + currentScore);
        if (currentScore > highestScore || highestScore <= 0) {
            highestScore = currentScore;
            tvHighScore.setText("" + highestScore);
        }

    }

    @Override
    public void gameOver() {
        Toast.makeText(this, R.string.game_finish, Toast.LENGTH_LONG).show();
        //TODO here take input name of user
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.saveScoreInDb(highestScore);
    }
}
