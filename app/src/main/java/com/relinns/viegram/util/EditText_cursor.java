package com.relinns.viegram.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by win 7 on 6/7/2017.
 */
public class EditText_cursor extends EditText {

    private Context context;

    public EditText_cursor(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context=context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            KeyEvent.DispatcherState state=getKeyDispatcherState();
            if (state!=null)
            {
                if (event.getAction()==KeyEvent.ACTION_DOWN )
                {
                    InputMethodManager inputManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(this.getWindowToken(),0);


                }
            }
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
