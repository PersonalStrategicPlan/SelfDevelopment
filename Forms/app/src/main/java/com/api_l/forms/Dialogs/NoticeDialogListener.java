package com.api_l.forms.Dialogs;

import android.app.DialogFragment;



public interface NoticeDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);
    public void onDialogNegativeClick(DialogFragment dialog);
    public void GoalSelected(String value);
}
