package com.knowware.aw.spellfight1.UI;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.knowware.aw.spellfight1.R;


/**
 * Created by Aaron on 7/20/2017.
 */

public class MsgOkDlgFrag extends DialogFragment
{
    static final public int NONE=0;
    static final public int INTRO=1;
    static final public int VICTORY=2;
    static final public int DEATH=3;

    public interface MsgOkDlgFragmentListener
    {
        void onOk(int type);
    }

    private MsgOkDlgFragmentListener mListener;

    /**
     *
     * newInstance
     *
     * @param title
     * @param msg
     * @param type
     * @return
     */
    public static MsgOkDlgFrag newInstance(String title, String msg, int type)
    {
        MsgOkDlgFrag dlg = new MsgOkDlgFrag();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("Message", msg);
        args.putInt("type",type);

        dlg.setArguments(args);
        return dlg;

    }

    /**
     *
     * onCreateDialog
     *
     * @param savedInstanceState
     * @return
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String title = getArguments().getString("title");
        String message = getArguments().getString("Message");
        Dialog dlg;

        dlg= new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                if(mListener!=null)
                                {
                                    int i;

                                    i=getArguments().getInt("type");

                                    mListener.onOk(i);
                                }

                            }
                        }

                )
                .create();

        return(dlg);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        mListener=(MsgOkDlgFragmentListener) activity;

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        Activity activity;

        activity=(Activity) context;
        mListener=(MsgOkDlgFragmentListener) activity;

    }


}