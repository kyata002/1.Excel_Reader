package com.masterexcels.excelreader.view.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.documentmaster.documentscan.OnActionCallback;
import com.masterexcels.excelreader.R;

public class PasswordDialog extends DialogFragment {
    private EditText edtPassword;
    private OnActionCallback callback;
    private TextView btDone;

    public static PasswordDialog newInstance() {
        Bundle args = new Bundle();
        PasswordDialog fragment = new PasswordDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallback(OnActionCallback callback) {
        this.callback = callback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_enter_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().
                setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(false);
        edtPassword = getDialog().findViewById(R.id.edt_password);
        btDone = getDialog().findViewById(R.id.tv_done);
        btDone.setEnabled(false);
        btDone.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btDone.setEnabled(!editable.toString().isEmpty());
                if (!editable.toString().isEmpty()){
                    btDone.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B30B00")));
                }else {
                    btDone.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });

        new Handler().postDelayed(() -> {
            edtPassword.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtPassword, InputMethodManager.SHOW_IMPLICIT);
            edtPassword.setSelection(edtPassword.getText().length());
        }, 500);
        btDone.setOnClickListener(v -> {
            callback.callback(null, edtPassword.getText().toString());
        });
        getDialog().findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            callback.callback("cancel", edtPassword.getText().toString());
            dismiss();
        });
    }

}
