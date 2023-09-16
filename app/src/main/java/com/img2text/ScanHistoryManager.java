package com.img2text;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class ScanHistoryManager {

    public static void saveScanResult(Context context, String scanResult) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ScanHistory", Context.MODE_PRIVATE);
        Set<String> historySet = sharedPreferences.getStringSet("history", new HashSet<>());
        historySet.add(scanResult);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("history", historySet);
        editor.apply();
    }

    public static Set<String> loadScanHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ScanHistory", Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet("history", new HashSet<>());
    }
}
