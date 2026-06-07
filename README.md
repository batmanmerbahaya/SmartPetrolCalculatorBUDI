# Smart Petrol Calculator (BUDI MADANI)

A modern Android application designed to help Malaysian drivers estimate their fuel costs and calculate potential savings from the **BUDI MADANI** petrol subsidy program.

## 🚀 Features

- **Segmented UI Workflow**: Organized into four distinct functional cards:
  - **Fuel Details**: Select between RON 95, RON 97, or Diesel.
  - **Price & Usage**: Input current pricing and volume (with quick-select presets for 20L, 40L, and Full Tank).
  - **Eligibility**: Toggle BUDI MADANI eligibility status.
  - **Cost Summary**: View a detailed breakdown of total cost, rebates, and final savings.
- **Dynamic Animations**:
  - Interactive fuel type selection with horizontal expansion animations.
  - Smooth slide-up and fade-in transitions for the Result Summary card.
  - Visual feedback for button interactions.
- **Robust Validation**: Real-time error handling using `EditText.setError()` to prevent invalid or empty inputs.
- **State Management**: Full support for screen rotation; calculation results and UI states are preserved automatically.
- **Modern Styling**: High-fidelity UI with shadowed cards, brand-consistent coloring, and vector iconography.

## 🛠️ Tech Stack

- **Language**: Java
- **UI Framework**: XML (Android View System)
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34
- **Key Components**: `ValueAnimator`, `SpannableString`, `OvershootInterpolator`, `CardView`-style layouts.

## 📸 Functionality

1. **Input**: Enter the petrol price and the amount of fuel used.
2. **Selection**: Choose the fuel type. Note that BUDI MADANI rebates currently focus on eligible RON 95 usage.
3. **Calculation**: The app computes the total cost and applies a rebate of **RM 1.99/liter** if the criteria are met.
4. **Summary**: Clear breakdown of savings displayed with intuitive icons.

## 👨‍💻 Developer Information

- **Name**: Muhammad ZulZarif bin Zahir
- **Matric No**: 2025149671
- **Course**: CS266 - Information System Engineering
- **University**: Universiti Teknologi MARA (UiTM)

## 🔗 Repository
[https://github.com/batmanmerbahaya/SmartPetrolCalculatorBUDI](https://github.com/batmanmerbahaya/SmartPetrolCalculatorBUDI)

---
© 2026 Muhammad ZulZarif. All rights reserved.
