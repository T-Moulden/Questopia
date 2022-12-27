package org.qp.android.view.game.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.qp.android.R;
import org.qp.android.databinding.DialogImageBinding;

import java.util.ArrayList;

public class GameDialogFrags extends GamePatternDialogFrags {
    private ArrayList<String> items;
    private GameDialogType dialogType;
    private DialogImageBinding imageBinding;
    private String processedMsg;
    private String message;

    public ObservableField<String> pathToImage = new ObservableField<>();

    public void setDialogType(GameDialogType dialogType) {
        this.dialogType = dialogType;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public void setProcessedMsg(String processedMsg) {
        this.processedMsg = processedMsg;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("selectedDialogType")) {
                dialogType = GameDialogType.valueOf(savedInstanceState.getString("selectedDialogType"));
            }
            if (savedInstanceState.containsKey("items")) {
                items = savedInstanceState.getStringArrayList("items");
            }
            if (savedInstanceState.containsKey("message")) {
                message = savedInstanceState.getString("message");
            }
            if (savedInstanceState.containsKey("pathToImage")) {
                pathToImage.set(savedInstanceState.getString("pathToImage"));
            }
            if (savedInstanceState.containsKey("processedMsg")) {
                processedMsg = savedInstanceState.getString("processedMsg");
            }
        }
        switch (dialogType) {
            case ERROR_DIALOG:
                builder.setTitle(R.string.error);
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {});
                return builder.create();
            case CLOSE_DIALOG:
                builder.setMessage(requireContext().getString(R.string.promptCloseGame));
                builder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                        listener.onDialogPositiveClick(this));
                builder.setNegativeButton(android.R.string.no, (dialog, which) -> { });
                return builder.create();
            case IMAGE_DIALOG:
                imageBinding = DialogImageBinding.inflate(getLayoutInflater());
                imageBinding.setDialogFragment(this);
                builder.setView(imageBinding.getRoot());
                return builder.create();
            case INPUT_DIALOG:
                final View view =
                        getLayoutInflater().inflate(R.layout.dialog_input, null);
                builder.setView(view);
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                        listener.onDialogPositiveClick(this));
                return builder.create();
            case LOAD_DIALOG:
                builder.setMessage(requireContext().getString(R.string.loadGamePopup));
                builder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                        listener.onDialogPositiveClick(this));
                builder.setNegativeButton(android.R.string.no, (dialog, which) -> { });
                return builder.create();
            case MENU_DIALOG:
                builder.setItems(items.toArray(new CharSequence[0]), (dialog, which) ->
                        listener.onDialogListClick(this, which));
                builder.setOnCancelListener(dialog -> listener.onDialogNegativeClick(this));
                return builder.create();
            case MESSAGE_DIALOG:
                builder.setMessage(processedMsg);
                builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                        listener.onDialogPositiveClick(this));
                return builder.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedDialogType", dialogType.toString());
        if (items != null) {
            outState.putStringArrayList("items", items);
        }
        if (message != null) {
            outState.putString("message" , message);
        }
        if (pathToImage != null) {
            outState.putString("pathToImage" , pathToImage.get());
        }
        if (processedMsg != null) {
            outState.putString("processedMsg", processedMsg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (imageBinding != null) {
            imageBinding = null;
        }
    }
}