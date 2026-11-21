package com.viloria.studentinfosystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Gold success medallion with a navy check, drawn with Canvas shapes.
 */
public class SuccessBadgeView extends View {

    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint check = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

    private final int navy;
    private final int gold;

    public SuccessBadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        navy = ContextCompat.getColor(context, R.color.nu_navy);
        gold = ContextCompat.getColor(context, R.color.nu_gold);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float dp = getResources().getDisplayMetrics().density;
        float cx = w / 2f;
        float cy = h / 2f;
        float r = Math.min(w, h) / 2f - 6 * dp;

        fill.setStyle(Paint.Style.STROKE);
        fill.setStrokeWidth(2 * dp);
        fill.setColor(withAlpha(gold, 140));
        canvas.drawCircle(cx, cy, r + 3 * dp, fill);

        fill.setStyle(Paint.Style.FILL);
        fill.setColor(gold);
        canvas.drawCircle(cx, cy, r, fill);

        check.setStyle(Paint.Style.STROKE);
        check.setStrokeWidth(6 * dp);
        check.setStrokeCap(Paint.Cap.ROUND);
        check.setStrokeJoin(Paint.Join.ROUND);
        check.setColor(navy);
        path.reset();
        path.moveTo(cx - r * 0.34f, cy);
        path.lineTo(cx - r * 0.08f, cy + r * 0.26f);
        path.lineTo(cx + r * 0.36f, cy - r * 0.24f);
        canvas.drawPath(path, check);
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
