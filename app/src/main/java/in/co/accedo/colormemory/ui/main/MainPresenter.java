package in.co.accedo.colormemory.ui.main;

import java.util.List;

import javax.inject.Inject;

import in.co.accedo.colormemory.data.model.BoardTile;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import in.co.accedo.colormemory.data.DataManager;
import in.co.accedo.colormemory.data.model.Ribot;
import in.co.accedo.colormemory.injection.ConfigPersistent;
import in.co.accedo.colormemory.ui.base.BasePresenter;
import in.co.accedo.colormemory.util.RxUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRibots() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getBoardTiles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<BoardTile>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<BoardTile> ribots) {
                        if (ribots.isEmpty()) {
                            getMvpView().showRibotsEmpty();
                        } else {
                            getMvpView().showGameBoard(ribots);
                        }
                    }
                });
    }

    public void saveScoreInDb(Integer score) {
        mDataManager.getPreferencesHelper().setHighScore(score);
    }

    public Integer getHighScore() {
        return mDataManager.getPreferencesHelper().getHighScore();
    }


}
