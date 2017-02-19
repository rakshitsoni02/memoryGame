package in.co.accedo.colormemory.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ToolbarTitleFontView extends TextView {

    public ToolbarTitleFontView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ToolbarTitleFontView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ToolbarTitleFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/grobold.ttf", context);
        setTypeface(customFont);
    }
}