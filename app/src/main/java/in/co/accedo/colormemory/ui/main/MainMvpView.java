package in.co.accedo.colormemory.ui.main;

import java.util.List;

import in.co.accedo.colormemory.data.model.BoardTile;
import in.co.accedo.colormemory.data.model.Ribot;
import in.co.accedo.colormemory.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showGameBoard(List<BoardTile> boardTiles);

    void showRibotsEmpty();

    void showError();

}
