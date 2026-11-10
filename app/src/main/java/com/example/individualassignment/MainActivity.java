package com.example.individualassignment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {

    private RadioGroup rgPetrolType;
    private RadioGroup rgEligibility;
    private EditText edtPetrolPrice;
    private EditText edtFuelUsage;

    private TextView txtTotalCost;
    private TextView txtBudiRebate;
    private TextView txtTotalSaving;
    private TextView txtNote;
    private TextView txtGithubUrl;

    private LinearLayout homeSection;
    private LinearLayout aboutSection;
    private LinearLayout cardSummary;

    private TextView btnHome;
    private TextView btnAbout;
    private Button btnCalculate;
    private Button btnClear;
    private Button btnQuick20;
    private Button btnQuick40;
    private Button btnQuickFull;

    private RadioButton rbRon95;
    private RadioButton rbRon97;
    private RadioButton rbDiesel;

    private static final double BUDI_SUBSIDY_RATE = 1.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectViews();
        setupNavigation();
        setupButtons();
        setupRadioAnimations();
        setupFuelTypeStyles();
        setupGithubLink();

        if (savedInstanceState != null) {
            boolean isAboutVisible = savedInstanceState.getBoolean("isAboutVisible", false);
            if (isAboutVisible) {
                showAboutSection();
            } else {
                showHomeSection();
            }

            txtTotalCost.setText(savedInstanceState.getString("totalCost", getString(R.string.result_total_cost)));
            txtBudiRebate.setText(savedInstanceState.getString("budiRebate", getString(R.string.result_budi_rebate)));
            txtTotalSaving.setText(savedInstanceState.getString("totalSaving", getString(R.string.result_total_saving)));
            txtNote.setText(savedInstanceState.getString("note", ""));

            boolean isSummaryVisible = savedInstanceState.getBoolean("isSummaryVisible", false);
            if (isSummaryVisible) {
                cardSummary.setVisibility(View.VISIBLE);
                cardSummary.setAlpha(1f);
            }
        } else {
            // Initial state
            showHomeSection();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isAboutVisible", aboutSection.getVisibility() == View.VISIBLE);
        outState.putBoolean("isSummaryVisible", cardSummary.getVisibility() == View.VISIBLE);
        outState.putString("totalCost", txtTotalCost.getText().toString());
        outState.putString("budiRebate", txtBudiRebate.getText().toString());
        outState.putString("totalSaving", txtTotalSaving.getText().toString());
        outState.putString("note", txtNote.getText().toString());
    }

    private void connectViews() {
        rgPetrolType = findViewById(R.id.rgPetrolType);
        rgEligibility = findViewById(R.id.rgEligibility);

        edtPetrolPrice = findViewById(R.id.edtPetrolPrice);
        edtFuelUsage = findViewById(R.id.edtFuelUsage);

        txtTotalCost = findViewById(R.id.txtTotalCost);
        txtBudiRebate = findViewById(R.id.txtBudiRebate);
        txtTotalSaving = findViewById(R.id.txtTotalSaving);
        txtNote = findViewById(R.id.txtNote);
        txtGithubUrl = findViewById(R.id.txtGithubUrl);

        homeSection = findViewById(R.id.homeSection);
        aboutSection = findViewById(R.id.aboutSection);
        cardSummary = findViewById(R.id.cardSummary);

        btnHome = findViewById(R.id.btnHome);
        btnAbout = findViewById(R.id.btnAbout);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        btnQuick20 = findViewById(R.id.btnQuick20);
        btnQuick40 = findViewById(R.id.btnQuick40);
        btnQuickFull = findViewById(R.id.btnQuickFull);

        rbRon95 = findViewById(R.id.rbRon95);
        rbRon97 = findViewById(R.id.rbRon97);
        rbDiesel = findViewById(R.id.rbDiesel);
    }

    private void setupNavigation() {
        btnHome.setOnClickListener(v -> showHomeSection());
        btnAbout.setOnClickListener(v -> showAboutSection());
    }

    private void showHomeSection() {
        homeSection.setVisibility(View.VISIBLE);
        aboutSection.setVisibility(View.GONE);
        btnHome.setSelected(true);
        btnAbout.setSelected(false);
    }

    private void showAboutSection() {
        homeSection.setVisibility(View.GONE);
        aboutSection.setVisibility(View.VISIBLE);
        btnHome.setSelected(false);
        btnAbout.setSelected(true);
    }

    private void setupButtons() {
        btnCalculate.setOnClickListener(v -> calculatePetrolCost());
        btnClear.setOnClickListener(v -> clearFields());

        btnQuick20.setOnClickListener(v -> edtFuelUsage.setText("20"));
        btnQuick40.setOnClickListener(v -> edtFuelUsage.setText("40"));
        btnQuickFull.setOnClickListener(v -> edtFuelUsage.setText("50"));
    }

    private void setupRadioAnimations() {
        rgPetrolType.setOnCheckedChangeListener((group, checkedId) -> animateFuelSelection(checkedId));
        rgEligibility.setOnCheckedChangeListener((group, checkedId) -> animateSelection(group.findViewById(checkedId)));

        // Initial animation state
        animateFuelSelection(rgPetrolType.getCheckedRadioButtonId());
    }

    private void animateFuelSelection(int checkedId) {
        for (int i = 0; i < rgPetrolType.getChildCount(); i++) {
            View child = rgPetrolType.getChildAt(i);
            if (child instanceof RadioButton) {
                boolean isSelected = (child.getId() == checkedId);
                
                // Enlarge selected box horizontally (weight 1.5), shrink others (weight 0.75)
                float targetWeight = isSelected ? 1.5f : 0.75f;
                // Scale horizontally only: Enlarged vs slightly smaller
                float targetScaleX = isSelected ? 1.1f : 0.9f;

                // Animate layout weight for horizontal expansion/shrinkage
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                float startWeight = lp.weight;
                ValueAnimator weightAnim = ValueAnimator.ofFloat(startWeight, targetWeight);
                weightAnim.setDuration(400);
                weightAnim.addUpdateListener(animation -> {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params.weight = (float) animation.getAnimatedValue();
                    child.setLayoutParams(params);
                });
                weightAnim.start();

                // Animate scaleX only to keep top/bottom aligned
                child.animate()
                        .scaleX(targetScaleX)
                        .scaleY(1.0f)
                        .setDuration(400)
                        .setInterpolator(new OvershootInterpolator(1.2f))
                        .start();
            }
        }
    }

    private void animateSelection(View view) {
        if (view == null) return;
        view.setScaleX(0.85f);
        view.setScaleY(0.85f);
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator(2.0f))
                .start();
    }

    private void setupFuelTypeStyles() {
        rbRon95.setText(createFuelSpan("RON", "95"));
        rbRon97.setText(createFuelSpan("RON", "97"));
        rbDiesel.setText(createFuelSpan("EURO", "DIESEL"));

        // Apply shadow for depth to all fuel types
        applyTextShadow(rbRon95);
        applyTextShadow(rbRon97);
        applyTextShadow(rbDiesel);
    }

    private void applyTextShadow(RadioButton rb) {
        // Set shadow: radius=3, dx=2, dy=2, color=semi-transparent black
        rb.setShadowLayer(3f, 2f, 2f, 0x60000000);
    }

    private SpannableString createFuelSpan(String line1, String line2) {
        String combined = line1 + "\n" + line2;
        SpannableString ss = new SpannableString(combined);
        
        // Line 1: Label (Smaller)
        ss.setSpan(new RelativeSizeSpan(0.6f), 0, line1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        // Line 2: Value/Type (Larger)
        // If line2 is DIESEL, we make it slightly smaller than the 95/97 numbers to fit better
        float size2 = line2.equals("DIESEL") ? 1.1f : 1.5f;
        ss.setSpan(new RelativeSizeSpan(size2), line1.length() + 1, combined.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        // Both bold for emphasis
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, combined.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private void setupGithubLink() {
        txtGithubUrl.setOnClickListener(v -> {
            String url = txtGithubUrl.getText().toString().trim();

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Toast.makeText(this, getString(R.string.error_invalid_url), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private void calculatePetrolCost() {
        // Reset errors
        edtPetrolPrice.setError(null);
        edtFuelUsage.setError(null);

        String priceText = edtPetrolPrice.getText().toString().trim();
        String usageText = edtFuelUsage.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validate Petrol Price
        if (priceText.isEmpty()) {
            edtPetrolPrice.setError(getString(R.string.error_invalid_price));
            focusView = edtPetrolPrice;
            cancel = true;
        } else {
            try {
                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    edtPetrolPrice.setError(getString(R.string.error_zero_values));
                    focusView = edtPetrolPrice;
                    cancel = true;
                }
            } catch (NumberFormatException e) {
                edtPetrolPrice.setError(getString(R.string.error_numeric_only));
                focusView = edtPetrolPrice;
                cancel = true;
            }
        }

        // Validate Fuel Usage
        if (usageText.isEmpty()) {
            edtFuelUsage.setError(getString(R.string.error_invalid_usage));
            if (focusView == null) focusView = edtFuelUsage;
            cancel = true;
        } else {
            try {
                double usage = Double.parseDouble(usageText);
                if (usage <= 0) {
                    edtFuelUsage.setError(getString(R.string.error_zero_values));
                    if (focusView == null) focusView = edtFuelUsage;
                    cancel = true;
                }
            } catch (NumberFormatException e) {
                edtFuelUsage.setError(getString(R.string.error_numeric_only));
                if (focusView == null) focusView = edtFuelUsage;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
            return;
        }

        // Proceed with calculation
        double petrolPrice = Double.parseDouble(priceText);
        double fuelUsage = Double.parseDouble(usageText);

        int selectedPetrolId = rgPetrolType.getCheckedRadioButtonId();
        String petrolType = "";
        if (selectedPetrolId == R.id.rbRon95) petrolType = "Ron 95";
        else if (selectedPetrolId == R.id.rbRon97) petrolType = "Ron 97";
        else if (selectedPetrolId == R.id.rbDiesel) petrolType = "Diesel";

        int selectedEligibleId = rgEligibility.getCheckedRadioButtonId();
        String eligibility = (selectedEligibleId == R.id.rbEligibleYes) ? "Yes" : "No";

        // Cost at the normal pump price
        double totalPetrolCost = fuelUsage * petrolPrice;

        // By default the driver pays the normal price and saves nothing
        double amountToPay = totalPetrolCost;
        double totalSaving = 0.0;

        // Eligible RON95 drivers pay the BUDI95 price (RM1.99 per litre) instead
        if (petrolType.equalsIgnoreCase("Ron 95") && eligibility.equalsIgnoreCase("Yes")) {
            double budiPrice = fuelUsage * BUDI_SUBSIDY_RATE;
            amountToPay = Math.min(budiPrice, totalPetrolCost);
            totalSaving = totalPetrolCost - amountToPay;
        }

        txtTotalCost.setText(getString(R.string.result_total_cost_fmt, totalPetrolCost));
        txtBudiRebate.setText(getString(R.string.result_budi_rebate_fmt, amountToPay));
        txtTotalSaving.setText(getString(R.string.result_total_saving_fmt, totalSaving));

        if (!petrolType.equalsIgnoreCase("Ron 95")) {
            txtNote.setText(getString(R.string.note_ineligible_type));
        } else if (eligibility.equalsIgnoreCase("No")) {
            txtNote.setText(getString(R.string.note_ineligible_user));
        } else {
            txtNote.setText(getString(R.string.note_rebate_applied));
        }

        showSummaryCard();
    }

    private void showSummaryCard() {
        if (cardSummary.getVisibility() == View.VISIBLE) return;

        cardSummary.setVisibility(View.VISIBLE);
        cardSummary.setTranslationY(100f);
        cardSummary.setAlpha(0f);
        cardSummary.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator(1.0f))
                .start();
    }

    private void clearFields() {
        edtPetrolPrice.setError(null);
        edtFuelUsage.setError(null);
        edtPetrolPrice.setText("");
        edtFuelUsage.setText("");

        rgPetrolType.check(R.id.rbRon95);
        rgEligibility.check(R.id.rbEligibleYes);

        txtTotalCost.setText(getString(R.string.result_total_cost));
        txtBudiRebate.setText(getString(R.string.result_budi_rebate));
        txtTotalSaving.setText(getString(R.string.result_total_saving));
        txtNote.setText("");

        hideSummaryCard();
    }

    private void hideSummaryCard() {
        cardSummary.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> cardSummary.setVisibility(View.GONE))
                .start();
    }
}
