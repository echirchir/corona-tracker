package ke.co.simpledeveloper.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class CoronaTextView extends androidx.appcompat.widget.AppCompatTextView{

    public CoronaTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CoronaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CoronaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = TypeFaceSingleton.getInstance().getTypeface(context);
        setTypeface(customFont);
    }
}
