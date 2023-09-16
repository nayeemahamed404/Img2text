package com.img2text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Set<String> scanHistorySet = ScanHistoryManager.loadScanHistory(this);
        List<String> scanHistoryList = new ArrayList<>(scanHistorySet);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scanHistoryList);

        ListView listView = findViewById(R.id.historyListView);
        listView.setAdapter(adapter);

        // Add a click listener to copy each scan history item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = scanHistoryList.get(position);
                copyToClipboard(selectedItem);
            }
        });
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Data", text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }
}
