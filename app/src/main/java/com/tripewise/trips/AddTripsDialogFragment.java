package com.tripewise.trips;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.tripewise.R;

import java.util.ArrayList;
import java.util.List;

public class AddTripsDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText etTripName;
    private EditText etMemberName;

    private TextView tvAddMember;

    private ChipGroup chipGroupMember;

    private List<String> memberName;

    private boolean createChip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        memberName = new ArrayList<>();

        setCancelable(false);

        etTripName = view.findViewById(R.id.et_trip_name);
        etMemberName = view.findViewById(R.id.et_name);

        chipGroupMember = view.findViewById(R.id.chip_member_group);

        Button btSave = view.findViewById(R.id.bt_save);
        Button btCancel = view.findViewById(R.id.bt_cancel);

        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        etMemberName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String tempText = etMemberName.getText().toString();
                if (!tempText.equals("")) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SPACE) {
                        chipGroupMember.addView(createChip(etMemberName.getText().toString()));
                        Log.d("TripDIalogFragment", " Chip Count : " + chipGroupMember.getChildCount());
                        memberName.add(etMemberName.getText().toString());
                        etMemberName.setText("");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(getActivity());
        chip.setText(text);
        chip.setTextColor(Color.WHITE);
        chip.setChipBackgroundColorResource(R.color.colorBlack);
        chip.setChipIcon(getResources().getDrawable(R.drawable.ic_people));
        chip.setCloseIconVisible(true);
        chip.setCloseIconTintResource(R.color.colorWhite);
        chip.setCloseIcon(getActivity().getResources().getDrawable(R.drawable.ic_close));
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chip tempChip = (Chip) v;
                memberName.remove(tempChip.getText().toString());
                chipGroupMember.removeView(v);
            }
        });
        return chip;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                break;
            case R.id.bt_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }
}
