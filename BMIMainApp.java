// Add to the showMenu() method:
private static void showMenu() {
    System.out.println("\n┌─────────────────────────────────────────────────┐");
    System.out.println("│ 1.  👤 Register New Person                        │");
    System.out.println("│ 2.  📊 Calculate BMI & Store Record               │");
    System.out.println("│ 3.  📜 View BMI History                           │");
    System.out.println("│ 4.  💡 Get Personalized Health Advice             │");
    System.out.println("│ 5.  🏥 Generate Clinical Report (for Doctor)      │");
    System.out.println("│ 6.  📈 Growth Potential (Age < 20)                │");
    System.out.println("│ 7.  🔮 Predict Adult Height (with parent data)    │");
    System.out.println("│ 8.  📏 Check Height Percentile                    │");
    System.out.println("│ 9.  🔥 Calculate BMR & Daily Calories             │");
    System.out.println("│ 10. 🎯 Ideal Weight Range Calculator              │");
    System.out.println("│ 11. 🚪 Exit                                       │");
    System.out.println("└─────────────────────────────────────────────────┘");
}

// Add these methods to BMIMainApp:

private static void predictAdultHeight() {
    if (users.isEmpty()) {
        System.out.println("\n❌ No users registered.");
        return;
    }
    
    System.out.println("\nSelect user:");
    for (int i = 0; i < users.size(); i++) {
        System.out.println((i+1) + ". " + users.get(i).getName());
    }
    int idx = InputValidator.getIntInput(scanner, "Choose: ", 1, users.size()) - 1;
    
    Person p = users.get(idx);
    
    if (p.getAge() >= 20) {
        System.out.println("\n⚠️ Height prediction is only meaningful for individuals under 20.");
        System.out.println("Your current height is likely your final adult height.");
        return;
    }
    
    System.out.println("\n📏 HEIGHT PREDICTION REQUIRES PARENTAL DATA");
    double motherHeight = InputValidator.getDoubleInput(scanner, "Mother's height (cm): ", 100, 200);
    double fatherHeight = InputValidator.getDoubleInput(scanner, "Father's height (cm): ", 100, 220);
    
    GrowthPredictor predictor = new GrowthPredictor();
    GrowthPredictor.GrowthPrediction prediction = predictor.predictAdultHeight(p, motherHeight, fatherHeight);
    
    System.out.println(prediction);
}

private static void checkHeightPercentile() {
    if (users.isEmpty()) {
        System.out.println("\n❌ No users registered.");
        return;
    }
    
    System.out.println("\nSelect user:");
    for (int i = 0; i < users.size(); i++) {
        System.out.println((i+1) + ". " + users.get(i).getName());
    }
    int idx = InputValidator.getIntInput(scanner, "Choose: ", 1, users.size()) - 1;
    
    Person p = users.get(idx);
    
    if (p.getAge() > 18) {
        System.out.println("\n📊 Height percentiles are most meaningful for children and adolescents.");
        System.out.println("For adults, focus on your overall health rather than height comparison.");
    }
    
    GrowthPredictor predictor = new GrowthPredictor();
    double heightCm = p.getHeightMeters() * 100;
    GrowthPredictor.HeightPercentile percentile = predictor.getHeightPercentile(heightCm, p.getAge(), p.getGender());
    
    System.out.println(percentile);
}

private static void calculateBMRAndCalories() {
    if (users.isEmpty()) {
        System.out.println("\n❌ No users registered.");
        return;
    }
    
    System.out.println("\nSelect user:");
    for (int i = 0; i < users.size(); i++) {
        System.out.println((i+1) + ". " + users.get(i).getName());
    }
    int idx = InputValidator.getIntInput(scanner, "Choose: ", 1, users.size()) - 1;
    
    Person p = users.get(idx);
    BMICalculatorService calculator = new BMICalculatorService();
    
    double bmr = calculator.calculateBMR(p);
    double dailyCalories = calculator.calculateDailyCalories(p);
    double bmi = calculator.calculateBMI(p);
    
    System.out.println("\n╔════════════════════════════════════════════╗");
    System.out.println("║         METABOLIC PROFILE                  ║");
    System.out.println("╠════════════════════════════════════════════╣");
    System.out.printf("║ BMR (Basal Metabolic Rate): %6.0f cal/day   ║\n", bmr);
    System.out.printf("║ Daily Calorie Need:          %6.0f cal/day   ║\n", dailyCalories);
    System.out.println("╠════════════════════════════════════════════╣");
    
    // Weight loss/gain calorie targets
    if (bmi >= 25) {
        double weightLossCalories = dailyCalories - 500;
        System.out.printf("║ For weight loss (0.5kg/week): %6.0f cal/day ║\n", weightLossCalories);
    } else if (bmi < 18.5) {
        double weightGainCalories = dailyCalories + 500;
        System.out.printf("║ For weight gain (0.3kg/week): %6.0f cal/day ║\n", weightGainCalories);
    } else {
        double maintenanceLow = dailyCalories - 200;
        double maintenanceHigh = dailyCalories + 200;
        System.out.printf("║ Maintenance range:     %6.0f - %6.0f cal/day ║\n", maintenanceLow, maintenanceHigh);
    }
    System.out.println("╚════════════════════════════════════════════╝");
}

private static void showIdealWeightRange() {
    if (users.isEmpty()) {
        System.out.println("\n❌ No users registered.");
        return;
    }
    
    System.out.println("\nSelect user:");
    for (int i = 0; i < users.size(); i++) {
        System.out.println((i+1) + ". " + users.get(i).getName());
    }
    int idx = InputValidator.getIntInput(scanner, "Choose: ", 1, users.size()) - 1;
    
    Person p = users.get(idx);
    BMICalculatorService calculator = new BMICalculatorService();
    
    BMICalculatorService.IdealWeightRange range = calculator.getIdealWeightRange(
        p.getHeightMeters(), p.getGender(), p.getAge()
    );
    
    System.out.println(range);
    
    double currentWeight = p.getWeightKg();
    if (currentWeight < range.bmiMinWeight) {
        System.out.printf("\n📌 You need to gain %.1f kg to reach minimum healthy weight.\n", 
                         range.bmiMinWeight - currentWeight);
    } else if (currentWeight > range.bmiMaxWeight) {
        System.out.printf("\n📌 You need to lose %.1f kg to reach maximum healthy weight.\n", 
                         currentWeight - range.bmiMaxWeight);
    } else {
        System.out.println("\n✅ Congratulations! Your weight is within the healthy range.");
    }
}

// Update the main switch statement:
// Add cases 7, 8, 9, 10 accordingly