package com.viloria.studentinfosystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Deep navy portal backdrop with soft gold rings and small gold points,
 * drawn entirely with Canvas shapes.
 */
public class NuBackdropView extends View {

    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int navy;
    private final int navyDark;
    private final int gold;

    public NuBackdropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        navy = ContextCompat.getColor(context, R.color.nu_navy);
        navyDark = ContextCompat.getColor(context, R.color.nu_navy_dark);
        gold = ContextCompat.getColor(context, R.color.nu_gold);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float dp = getResources().getDisplayMetrics().density;

        fill.setStyle(Paint.Style.FILL);
        fill.setShader(new LinearGradient(0, 0, w, h, navy, navyDark, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, w, h, fill);
        fill.setShader(null);

        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(2 * dp);
        line.setColor(withAlpha(gold, 60));
        canvas.drawCircle(w * 0.86f, h * 0.10f, w * 0.46f, line);

        line.setStrokeWidth(10 * dp);
        line.setColor(withAlpha(gold, 26));
        canvas.drawCircle(w * 0.86f, h * 0.10f, w * 0.30f, line);

        line.setStrokeWidth(2 * dp);
        line.setColor(withAlpha(gold, 45));
        canvas.drawCircle(w * 0.08f, h * 0.92f, w * 0.22f, line);

        fill.setStyle(Paint.Style.FILL);
        fill.setColor(withAlpha(gold, 130));
        canvas.drawCircle(w * 0.14f, h * 0.20f, 2 * dp, fill);
        canvas.drawCircle(w * 0.22f, h * 0.12f, 1.6f * dp, fill);
        canvas.drawCircle(w * 0.92f, h * 0.62f, 2 * dp, fill);
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
