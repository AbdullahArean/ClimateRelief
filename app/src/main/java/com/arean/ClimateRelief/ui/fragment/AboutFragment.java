package com.arean.ClimateRelief.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.databinding.FragmentAboutBinding;
import com.arean.ClimateRelief.ui.activity.MainActivityweather;
import com.arean.ClimateRelief.utils.AppUtil;
import com.arean.ClimateRelief.utils.LocaleManager;
import com.arean.ClimateRelief.utils.MyApplication;
import com.arean.ClimateRelief.utils.SharedPreferencesUtil;
import com.arean.ClimateRelief.utils.ViewAnimation;


public class AboutFragment extends DialogFragment {

  private Activity activity;
  private String currentLanguage;
  private FragmentAboutBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentAboutBinding.inflate(inflater, container, false);
    View view = binding.getRoot();
    initVariables(view);
    return view;
  }

  private void initVariables(View view) {
    currentLanguage = MyApplication.localeManager.getLanguage();
    activity = getActivity();
    if (activity != null) {
      Drawable drawable = activity.getResources().getDrawable(R.drawable.ic_done_black_24dp);
      String versionName = "";
      try {
        versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
      } catch (PackageManager.NameNotFoundException e) {
        // do nothing
      }
//      setTextWithLinks(view.findViewById(R.id.text_application_info), getString(R.string.application_info_text, versionName));
//      setTextWithLinks(view.findViewById(R.id.text_developer_info), getString(R.string.developer_info_text));
//      setTextWithLinks(view.findViewById(R.id.text_design_api), getString(R.string.design_api_text));
//      setTextWithLinks(view.findViewById(R.id.text_libraries), getString(R.string.libraries_text));
//      setTextWithLinks(view.findViewById(R.id.text_license), getString(R.string.license_text));
      if (currentLanguage.equals(LocaleManager.LANGUAGE_ENGLISH)) {
        binding.englishButton.setIcon(drawable);
      } else {
        binding.persianButton.setIcon(drawable);
      }
    }
    binding.nightModeSwitch.setChecked(SharedPreferencesUtil.getInstance(activity).isDarkThemeEnabled());
    binding.nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferencesUtil.getInstance(activity).setDarkThemeEnabled(isChecked);
        if (isChecked) {
          AppCompatDelegate.setDefaultNightMode(
              AppCompatDelegate.MODE_NIGHT_YES);
        } else {
          AppCompatDelegate.setDefaultNightMode(
              AppCompatDelegate.MODE_NIGHT_NO);
        }
        activity.recreate();
      }
    });
    binding.closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
        if (getFragmentManager() != null) {
          getFragmentManager().popBackStack();
        }
      }
    });
    binding.englishButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (currentLanguage.equals(LocaleManager.LANGUAGE_PERSIAN)) {
          MyApplication.localeManager.setNewLocale(activity, LocaleManager.LANGUAGE_ENGLISH);
          restartActivity();
        }
      }
    });
    binding.persianButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (currentLanguage.equals(LocaleManager.LANGUAGE_ENGLISH)) {
          MyApplication.localeManager.setNewLocale(activity, LocaleManager.LANGUAGE_PERSIAN);
          restartActivity();
        }
      }
    });
    binding.toggleInfoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toggleView();
      }
    });
    binding.toggleInfoLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toggleView();
      }
    });
  }

  private void toggleView() {
    boolean show = toggleArrow(binding.toggleInfoButton);
    if (show) {
      ViewAnimation.expand(binding.expandLayout, new ViewAnimation.AnimListener() {
        @Override
        public void onFinish() {
        }
      });
    } else {
      ViewAnimation.collapse(binding.expandLayout);
    }
  }

  private void setTextWithLinks(TextView textView, String htmlText) {
    AppUtil.setTextWithLinks(textView, AppUtil.fromHtml(htmlText));
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);
    return dialog;
  }

  private void restartActivity() {
    Intent intent = new Intent(activity, MainActivityweather.class);
    activity.startActivity(intent);
    activity.finish();
  }

  private boolean toggleArrow(View view) {
    if (view.getRotation() == 0) {
      view.animate().setDuration(200).rotation(180);
      return true;
    } else {
      view.animate().setDuration(200).rotation(0);
      return false;
    }
  }
}
