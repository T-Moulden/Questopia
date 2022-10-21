package com.qsp.player.view.settings;

import static com.qsp.player.utils.LanguageUtil.setLocale;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.qsp.player.R;
import com.qsp.player.viewModel.viewModels.ActivitySettings;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private String currentLanguage = Locale.getDefault().getLanguage();
    private SettingsController settingsController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActivitySettings activitySettings = new ViewModelProvider(this)
                .get(ActivitySettings.class);
        activitySettings.settingsActivityObservableField.set(this);
        settingsController = SettingsController.newInstance().loadSettings(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment
                        .newInstance(activitySettings.formationAboutDesc(settingsController , this)))
                .commit();

        loadLocale();
    }

    private void loadLocale() {
        setLocale(this, settingsController.language);
        currentLanguage = settingsController.language;
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingsController = SettingsController.newInstance().loadSettings(this);
        updateLocale();
    }

    private void updateLocale() {
        if (currentLanguage.equals(settingsController.language)) return;
        setLocale(this, settingsController.language);
        currentLanguage = settingsController.language;
    }
}
