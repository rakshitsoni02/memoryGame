package in.co.accedo.colormemory.data.model;

import in.co.accedo.colormemory.views.TileView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by lookup on 19/02/17.
 */

@Getter
@Setter
@NoArgsConstructor
public class BoardTile {

    Integer id;
    String colourResourceId;
    TileView tileView;
    int position;

}
