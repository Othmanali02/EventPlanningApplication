package com.example.eventplanningapp;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_activity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 5700; // 6 seconds
    private static final int ZOOM_IN_DURATION = 2000; // Duration for zooming in
    private static final int MOVE_LEFT_DURATION = 1000; // Duration for moving the image to the left
    private static final int SLIDE_IN_TEXT_DURATION = 1000; // Duration for sliding in the text
    private static final int TEXT_APPEAR_DURATION = 500; // Duration for the text to appear

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoImageView = findViewById(R.id.logoSplash);
        TextView textView = findViewById(R.id.textSplash);

        // Create a zoom-in effect for the logo
        ObjectAnimator zoomInAnimatorX = ObjectAnimator.ofFloat(logoImageView, "scaleX", 1.0f, 1.5f);
        ObjectAnimator zoomInAnimatorY = ObjectAnimator.ofFloat(logoImageView, "scaleY", 1.0f, 1.5f);

        zoomInAnimatorX.setDuration(ZOOM_IN_DURATION);
        zoomInAnimatorY.setDuration(ZOOM_IN_DURATION);

        zoomInAnimatorX.setInterpolator(new DecelerateInterpolator());
        zoomInAnimatorY.setInterpolator(new DecelerateInterpolator());

        // Combine both X and Y animations
        AnimatorSet zoomInSet = new AnimatorSet();
        zoomInSet.playTogether(zoomInAnimatorX, zoomInAnimatorY);

        // Slide in the text from the left after the layout has been drawn
        textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                textView.getViewTreeObserver().removeOnPreDrawListener(this);

                // Make the text initially invisible
                textView.setAlpha(0f);

                // Slide in the text from the right (initial position)
                textView.setTranslationX(textView.getWidth());

                // Combine animations for moving the image to the left and sliding in the text
                ObjectAnimator moveLeftAnimator = ObjectAnimator.ofFloat(logoImageView, "translationX", 0, -logoImageView.getWidth());
                moveLeftAnimator.setDuration(MOVE_LEFT_DURATION);

                ObjectAnimator slideInTextAnimator = ObjectAnimator.ofFloat(textView, "translationX", textView.getWidth(), 0);
                slideInTextAnimator.setDuration(SLIDE_IN_TEXT_DURATION);

                // Play the zoom-in animation first, then move the image to the left, and finally slide in the text
                AnimatorSet finalSet = new AnimatorSet();
                finalSet.playSequentially(zoomInSet, moveLeftAnimator, slideInTextAnimator);

                // Make the text appear after the image has moved and text has slid in
                finalSet.addListener(new AnimatorSet.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animator) {
                        // Do nothing
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animator) {
                        // Make the text appear
                        ObjectAnimator textAppearAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
                        textAppearAnimator.setDuration(TEXT_APPEAR_DURATION);
                        textAppearAnimator.start();
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animator) {
                        // Do nothing
                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animator) {
                        // Do nothing
                    }
                });

                finalSet.start();
                return true;
            }
        });

        // Delay for SPLASH_TIMEOUT milliseconds and then launch the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_activity.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
