package in.co.accedo.colormemory.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.co.accedo.colormemory.data.model.BoardTile;
import rx.Observable;
import rx.functions.Func1;
import in.co.accedo.colormemory.data.local.DatabaseHelper;
import in.co.accedo.colormemory.data.local.PreferencesHelper;
import in.co.accedo.colormemory.data.model.Ribot;
import in.co.accedo.colormemory.data.remote.RibotsService;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mRibotsService.getRibots()
                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
                    @Override
                    public Observable<Ribot> call(List<Ribot> ribots) {
                        return mDatabaseHelper.setRibots(ribots);
                    }
                });
    }

    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }


    public Observable<List<BoardTile>> getBoardTiles() {

        List<BoardTile> boardTileList = new ArrayList<>();
        int drawableCount = 1;
        for (int i = 1; i <= 16; i++) {
            BoardTile boardTile = new BoardTile();
            boardTile.setId(i);
            boardTile.setColourResourceId("" + drawableCount);
            if (drawableCount == 8)
                drawableCount = 0;
            drawableCount++;
            boardTileList.add(boardTile);
        }

        Collections.shuffle(boardTileList);
        return Observable.just(boardTileList);
    }

}
