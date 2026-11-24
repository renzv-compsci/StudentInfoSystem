package com.viloria.studentinfosystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;

/**
 * Weekly timetable drawn with Canvas: 7 days, 6 AM to 7 PM, dark blocks,
 * today highlight, and a live current-time line.
 */
public class TimetableGridView extends View {

    private static final int AXIS_W = 44;
    private static final int COL_W = 108;
    private static final int HEADER_H = 34;
    private static final int ROW_H = 36;
    private static final int ROWS = 26;
    private static final int START_MIN = 360;
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint headerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint nowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint codePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint namePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint metaPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint timePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private final float density;
    private final float scaledDensity;
    private final int navyDark;
    private final int gold;
    private final int cream;
    private final int muted;
    private final int mutedLight;
    private final int rowColor;
    private final int borderColor;
    private final int red;

    private float downX;
    private float downY;
    private boolean moved;

    private static final class Entry {
        final String code;
        final String name;
        final int day;
        final int start;
        final int end;
        final String room;
        final String teacher;

        Entry(String code, String name, int day, int start, int end, String room, String teacher) {
            this.code = code;
            this.name = name;
            this.day = day;
            this.start = start;
            this.end = end;
            this.room = room;
            this.teacher = teacher;
        }
    }

    private final Entry[] entries = {
            new Entry("CTINASSL", "Information Assurance and Security 1", 1, 660, 740, "COM243", "Florencio V. Contreras Jr."),
            new Entry("CCSFEN2L", "Software Engineering 2", 1, 740, 820, "COM242", "Mr. Eliseo Q. Ramirez"),
            new Entry("CCADMACL", "Advanced Machine Learning", 1, 820, 900, "COM242ML", "Ms. Angela Raven G. Uy"),
            new Entry("CTADEVL", "Applications Development and Emerging Technologies", 1, 900, 980, "COM243", "Mrs. Susan S."),

            new Entry("CCSFEN2L", "Software Engineering 2", 2, 660, 780, "COM242", "Mr. Eliseo Q. Ramirez"),
            new Entry("CCADMACL", "Advanced Machine Learning", 2, 780, 900, "COM242ML", "Ms. Angela Raven G. Uy"),
            new Entry("CTADEVL", "Applications Development and Emerging Technologies", 2, 900, 1020, "COM243", "Mrs. Susan S."),
            new Entry("CTINASSL", "Information Assurance and Security 1", 2, 1020, 1140, "COM243", "Florencio V. Contreras Jr."),

            new Entry("CTINASSL", "Information Assurance and Security 1", 4, 660, 740, "COM243", "Florencio V. Contreras Jr."),
            new Entry("CCSFEN2L", "Software Engineering 2", 4, 740, 820, "COM242", "Mr. Eliseo Q. Ramirez"),
            new Entry("CCADMACL", "Advanced Machine Learning", 4, 820, 900, "COM242ML", "Ms. Angela Raven G. Uy"),
            new Entry("CTADEVL", "Applications Development and Emerging Technologies", 4, 900, 980, "COM243", "Mrs. Susan S."),

            new Entry("CCSFEN2L", "Software Engineering 2", 5, 660, 780, "COM242", "Mr. Eliseo Q. Ramirez"),
            new Entry("CCADMACL", "Advanced Machine Learning", 5, 780, 900, "COM242ML", "Ms. Angela Raven G. Uy"),
            new Entry("CTADEVL", "Applications Development and Emerging Technologies", 5, 900, 1020, "COM243", "Mrs. Susan S."),
            new Entry("CTINASSL", "Information Assurance and Security 1", 5, 1020, 1140, "COM243", "Florencio V. Contreras Jr.")
    };

    public TimetableGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        navyDark = ContextCompat.getColor(context, R.color.nu_schedule_bg);
        gold = ContextCompat.getColor(context, R.color.nu_gold);
        cream = ContextCompat.getColor(context, R.color.nu_cream);
        muted = ContextCompat.getColor(context, R.color.nu_muted);
        mutedLight = ContextCompat.getColor(context, R.color.nu_muted_light);
        rowColor = ContextCompat.getColor(context, R.color.nu_row);
        borderColor = ContextCompat.getColor(context, R.color.nu_border);
        red = Color.parseColor("#EF4444");

        Typeface inter = ResourcesCompat.getFont(context, R.font.inter);
        if (inter == null) {
            inter = Typeface.SANS_SERIF;
        }
        Typeface interBold = Typeface.create(inter, Typeface.BOLD);

        codePaint.setTypeface(interBold);
        codePaint.setTextSize(sp(7f));
        codePaint.setColor(Color.parseColor("#CBD5E1"));

        namePaint.setTypeface(inter);
        namePaint.setTextSize(sp(8f));
        namePaint.setColor(cream);

        metaPaint.setTypeface(inter);
        metaPaint.setTextSize(sp(6f));
        metaPaint.setColor(mutedLight);

        timePaint.setTypeface(inter);
        timePaint.setTextSize(sp(6f));
        timePaint.setColor(mutedLight);

        headerPaint.setTypeface(interBold);
        headerPaint.setTextSize(sp(9f));
        headerPaint.setTextAlign(Paint.Align.CENTER);

        clockPaint.setTypeface(inter);
        clockPaint.setTextSize(sp(7f));
        clockPaint.setColor(muted);

        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(dp(1f));
        stroke.setColor(borderColor);

        nowPaint.setStyle(Paint.Style.STROKE);
        nowPaint.setStrokeWidth(dp(1.5f));
        nowPaint.setColor(red);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) dp(AXIS_W + 7 * COL_W);
        int height = (int) dp(HEADER_H + ROWS * ROW_H);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(navyDark);

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        float axis = dp(AXIS_W);
        float colW = dp(COL_W);
        float headerH = dp(HEADER_H);
        float rowH = dp(ROW_H);

        fill.setStyle(Paint.Style.FILL);
        fill.setColor(0x12E9B44C);
        canvas.drawRect(axis + today * colW, headerH, axis + (today + 1) * colW, getHeight(), fill);

        for (int i = 0; i < DAYS.length; i++) {
            headerPaint.setColor(i == today ? gold : mutedLight);
            float cx = axis + i * colW + colW / 2f;
            canvas.drawText(DAYS[i], cx, dp(20f), headerPaint);
        }

        stroke.setColor(borderColor);
        canvas.drawLine(0, headerH, getWidth(), headerH, stroke);

        for (int h = 6; h <= 18; h++) {
            float y = headerH + (h - 6) * 2 * rowH + dp(10f);
            clockPaint.setColor(muted);
            canvas.drawText(hourLabel(h), dp(4f), y, clockPaint);
        }

        for (Entry entry : entries) {
            drawEntry(canvas, entry, axis, colW, headerH, rowH);
        }

        Calendar cal = Calendar.getInstance();
        int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        if (minutes >= START_MIN && minutes <= START_MIN + ROWS * 30) {
            float y = headerH + ((minutes - START_MIN) / 30f) * rowH;
            canvas.drawLine(axis, y, getWidth(), y, nowPaint);
        }
    }

    private void drawEntry(Canvas canvas, Entry entry, float axis, float colW, float headerH, float rowH) {
        float x = axis + entry.day * colW + dp(2f);
        float y = headerH + ((entry.start - START_MIN) / 30f) * rowH + dp(2f);
        float w = colW - dp(4f);
        float h = ((entry.end - entry.start) / 30f) * rowH - dp(4f);
        float radius = dp(7f);

        rect.set(x, y, x + w, y + h);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(rowColor);
        canvas.drawRoundRect(rect, radius, radius, fill);
        stroke.setColor(borderColor);
        canvas.drawRoundRect(rect, radius, radius, stroke);

        canvas.save();
        canvas.clipRect(rect);

        float pad = dp(5f);
        float top = y + dp(4f);
        canvas.drawText(entry.code, x + pad, top + codePaint.getTextSize(), codePaint);

        int textWidth = Math.max(1, (int) (w - pad * 2));
        float nameTop = top + codePaint.getTextSize() + dp(2f);
        StaticLayout nameLayout = layout(entry.name, namePaint, textWidth);
        float timeTop = y + h - dp(13f);

        canvas.save();
        canvas.clipRect(x, nameTop, x + w, timeTop);
        canvas.save();
        canvas.translate(x + pad, nameTop);
        nameLayout.draw(canvas);
        canvas.restore();

        float metaTop = nameTop + nameLayout.getHeight() + dp(2f);
        StaticLayout metaLayout = layout(entry.room + " · " + entry.teacher, metaPaint, textWidth);
        canvas.save();
        canvas.translate(x + pad, metaTop);
        metaLayout.draw(canvas);
        canvas.restore();
        canvas.restore();

        canvas.drawText(fmt(entry.start) + " – " + fmt(entry.end), x + pad, y + h - dp(5f), timePaint);
        canvas.restore();
    }

    private StaticLayout layout(String text, TextPaint paint, int width) {
        return StaticLayout.Builder.obtain(text, 0, text.length(), paint, width)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build();
    }

    private String hourLabel(int hour) {
        String suffix = hour >= 12 ? " PM" : " AM";
        int h = hour % 12;
        if (h == 0) h = 12;
        return h + suffix;
    }

    private String fmt(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        String suffix = h >= 12 ? "PM" : "AM";
        h = h % 12;
        if (h == 0) h = 12;
        return h + (m == 0 ? "" : String.format(":%02d", m)) + " " + suffix;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moved = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - downX);
                float dy = Math.abs(event.getY() - downY);
                if (dx > ViewConfiguration.get(getContext()).getScaledTouchSlop()
                        || dy > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    moved = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!moved) {
                    Entry entry = findEntry(event.getX(), event.getY());
                    if (entry != null) {
                        Toast.makeText(getContext(),
                                entry.code + " · " + entry.name + "\n" + entry.room + " · " + entry.teacher,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private Entry findEntry(float px, float py) {
        float axis = dp(AXIS_W);
        float colW = dp(COL_W);
        float headerH = dp(HEADER_H);
        float rowH = dp(ROW_H);
        for (Entry entry : entries) {
            float x = axis + entry.day * colW + dp(2f);
            float y = headerH + ((entry.start - START_MIN) / 30f) * rowH + dp(2f);
            float w = colW - dp(4f);
            float h = ((entry.end - entry.start) / 30f) * rowH - dp(4f);
            if (px >= x && px <= x + w && py >= y && py <= y + h) {
                return entry;
            }
        }
        return null;
    }

    private float dp(float value) {
        return value * density;
    }

    private float sp(float value) {
        return value * scaledDensity;
    }
}
