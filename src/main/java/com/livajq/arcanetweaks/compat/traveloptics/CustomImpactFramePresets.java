package com.livajq.arcanetweaks.compat.traveloptics;

import com.livajq.arcanetweaks.Config;

public class CustomImpactFramePresets {
    public static int[] GOLD_SHADOWS = new int[]{0x0A0200, 0x1E1B01, 0x3D3B01};
    public static int[] GOLD_TONES = new int[]{0x1E1B01, 0x3D3801, 0x7E7602, 0xA79F02, 0xD6CB00, 0xFFFF1D, 0xFFF93C};
    
    public static int[] EXPERIMENTAL_SHADOWS = new int[]{0x0A0200, 0x1E1B01, 0x3D3B01};
    public static int[] EXPERIMENTAL_TONES = new int[]{0x1E1B01, 0x3D3801, 0x7E7602, 0xA79F02, 0xD6CB00, 0xFFFF1D, 0xFFF93C};
    
    
    public static void init() {
        EXPERIMENTAL_SHADOWS = Config.experimentalImpactShadows;
        EXPERIMENTAL_TONES = Config.experimentalImpactTones;
    }
}