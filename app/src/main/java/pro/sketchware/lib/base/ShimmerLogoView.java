package pro.sketchware.lib.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * ImageView that draws a white light line sweeping across the logo.
 * The line is only visible on the opaque pixels of the image (i.e. on the logo text).
 */
public class ShimmerLogoView extends AppCompatImageView {
    private final Paint shimmerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueAnimator animator;
    private float progress = -1f;

    public ShimmerLogoView(Context context) {
        super(context);
        init();
    }

    public ShimmerLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShimmerLogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        shimmerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator = ValueAnimator.ofFloat(-0.4f, 1.4f);
        animator.setDuration(1800);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(a -> {
            progress = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null) animator.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        float bandWidth = w * 0.18f;
        float center = w * progress;
        // slanted white line: gradient along x, soft edges
        shimmerPaint.setShader(new LinearGradient(
                center - bandWidth, 0, center + bandWidth, h * 0.25f,
                new int[]{0x00FFFFFF, 0xFFFFFFFF, 0x00FFFFFF},
                new float[]{0f, 0.5f, 1f},
                Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, w, h, shimmerPaint);
    }
}
