package com.example.test2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button retryButton;
    boolean gameStatus = true;
    int activePlayer = 0; // 0 - O, 1 - X

    // 2 means unplayed
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
    };

    public void PlayerTap(View view) {
        if (!gameStatus) {
            retryButton.setVisibility(View.VISIBLE);
            return; // Block further moves if game ended
        }

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        TextView status = findViewById(R.id.Bottom_Text);

        if (gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;

            if (activePlayer == 0) {
                img.setImageResource(R.drawable.o);
                activePlayer = 1;
                status.setText("X Player Turn");
            } else {
                img.setImageResource(R.drawable.x);
                activePlayer = 0;
                status.setText("O Player Turn");
            }

            // Check for win
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 2) {

                    gameStatus = false;
                    String winnerStr = (gameState[winPosition[0]] == 0) ? "O has won!" : "X has won!";
                    status.setText(winnerStr);
                    retryButton.setVisibility(View.VISIBLE);
                }
            }
        } else {
            status.setText("Select a valid place!");
        }
    }

    public void retry(View view) {
        gameStatus = true;
        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView box = (ImageView) gridLayout.getChildAt(i);
            box.setImageDrawable(null);
        }

        TextView status = findViewById(R.id.Bottom_Text);
        status.setText("O Player Turn");

        retryButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        retryButton = findViewById(R.id.retry); // Connect retry button

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
