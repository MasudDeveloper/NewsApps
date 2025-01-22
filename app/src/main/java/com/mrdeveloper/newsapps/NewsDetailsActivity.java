package com.mrdeveloper.newsapps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView tvTitle, tvDescription, tvTime;

    FloatingActionButton floatingActionButton;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.newsImage);
        tvTitle = findViewById(R.id.newsTitle);
        tvDescription = findViewById(R.id.newsDescription);
        tvTime = findViewById(R.id.newsDate);
        floatingActionButton = findViewById(R.id.fab);

        textToSpeech = new TextToSpeech(this, null);



        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String time = intent.getStringExtra("publishTime");

        byte[] bytes = intent.getByteArrayExtra("image");

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

        imageView.setImageBitmap(bitmap);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvTime.setText(time);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(description,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}