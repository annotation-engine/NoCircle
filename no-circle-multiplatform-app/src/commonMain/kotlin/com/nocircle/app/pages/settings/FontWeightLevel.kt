package com.nocircle.app.pages.settings

enum class FontWeightLevel(
    val progression: IntProgression
) {
    UltraThin(100..340 step 30),
    ExtraThin3(100..420 step 40),
    ExtraThin2(100..500 step 50),
    ExtraThin1(100..580 step 60),
    Thin3(100..660 step 70),
    Thin2(100..740 step 80),
    Thin1(100..820 step 90),
    Medium(100..900 step 100),
    Bold1(180..900 step 90),
    Bold2(260..900 step 80),
    Bold3(340..900 step 70),
    ExtraBold1(420..900 step 60),
    ExtraBold2(500..900 step 50),
    ExtraBold3(580..900 step 40),
    UltraBold(660..900 step 30)
}