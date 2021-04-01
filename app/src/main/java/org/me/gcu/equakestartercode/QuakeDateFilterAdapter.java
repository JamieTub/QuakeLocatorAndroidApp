package org.me.gcu.equakestartercode;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

//inspiration from https://gist.github.com/ishitcno1/9545088

public class QuakeDateFilterAdapter extends DialogFragment {

    // init flag values
    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;
    private int flag = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle bundle){
        //new calendar instance
        Calendar calendar = Calendar.getInstance();
        //set calendar values
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getTargetFragment(),
                year,
                month,
                day
        );

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
        return dpd;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    public void setFlag(int i){
        flag = i;
    }

    public int getFlag(){
        return flag;
    }
}
