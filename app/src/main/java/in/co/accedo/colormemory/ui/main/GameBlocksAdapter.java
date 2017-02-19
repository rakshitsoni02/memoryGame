package in.co.accedo.colormemory.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.accedo.colormemory.R;
import in.co.accedo.colormemory.data.model.BoardTile;
import in.co.accedo.colormemory.util.ScoreUpdateListener;
import in.co.accedo.colormemory.views.TileView;

public class GameBlocksAdapter extends RecyclerView.Adapter<GameBlocksAdapter.BoardViewHolder> {

    private List<BoardTile> boardTiles;
    private Context context;
    private boolean mLocked = false;
    private ScoreUpdateListener scoreUpdateListener;
    private int flippedUp = 0;
    private BoardViewHolder mFlippedTile;
    private int previousPosition = -1;
    int noPairs = 0;

    @Inject
    public GameBlocksAdapter() {
        boardTiles = new ArrayList<>();
    }

    public void setRibots(List<BoardTile> ribots, Context context, ScoreUpdateListener scoreUpdateListener) {
        boardTiles = ribots;
        this.scoreUpdateListener = scoreUpdateListener;
        this.context = context;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tile_view, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BoardViewHolder holder, final int position) {
        final BoardTile tile = boardTiles.get(position);
        final String colourResourceId = tile.getColourResourceId();

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return getBitmap(colourResourceId);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                holder.tileView.setTileImage(result);
            }
        }.execute();

        holder.tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLocked && holder.tileView.isFlippedDown()) {
                    holder.tileView.flipUp();


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (previousPosition == -1) {
                                mFlippedTile = holder;
                                previousPosition = position;
                            }
                            flippedUp++;
                            if (flippedUp == 2) {
                                mLocked = true;
                                if (isPair(previousPosition, position)) {
                                    ++noPairs;
                                    hideCards(mFlippedTile.tileView, holder.tileView);
                                    scoreUpdateListener.addPoint();
                                    if (noPairs == 8) {
                                        scoreUpdateListener.gameOver();
                                    }
                                } else {
                                    scoreUpdateListener.removePoint();
                                    holder.tileView.flipDown();
                                    flipDownAll();
                                }
                                previousPosition = -1;
                            }
                        }
                    }, 700);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return boardTiles.size();
    }

    class BoardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tile_root)
        TileView tileView;

        public BoardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private Bitmap getBitmap(String name) {
        String drawableResourceName = "colour" + name;
        int resourceId = context.getResources().getIdentifier(drawableResourceName, "drawable", context.getPackageName());
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public void flipDownAll() {
        mFlippedTile.tileView.flipDown();
        flippedUp = 0;
        mFlippedTile = null;
        mLocked = false;

    }

    public void hideCards(TileView id1, TileView id2) {
        animateHide(id1);
        animateHide(id2);
        flippedUp = 0;
        mFlippedTile = null;
        mLocked = false;
    }

    protected void animateHide(final TileView v) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", 0f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setLayerType(View.LAYER_TYPE_NONE, null);
                v.setVisibility(View.INVISIBLE);
            }
        });
        animator.setDuration(100);
        v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animator.start();
    }

    public boolean isPair(int id1, int id2) {
        if (boardTiles.get(id1).getColourResourceId().equalsIgnoreCase
                (boardTiles.get(id2).getColourResourceId())) {
            return true;

        }
        return false;
    }
}
