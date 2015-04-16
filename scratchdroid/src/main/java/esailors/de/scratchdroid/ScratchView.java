/*
 * Copyright (c) 2015 eSailors IT Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package esailors.de.scratchdroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ScratchView extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "ScratchView";
  private static final int DEFAULT_SCRATCH_RADIUS_DIP = 30;

  private final Paint backgroundBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint backgroundSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Path scratchedPath = new Path();

  private DrawLoopThread drawLoopThread;
  private float scratchRadius;
  private int foregroundColor;

  private Drawable backgroundDrawable;
  private Bitmap backgroundBitmap;
  private BitmapShader backgroundBitmapShader;
  private Matrix backgroundTransformationMatrix;

  private Drawable foregroundDrawable;
  private Bitmap foregroundBitmap;
  private Matrix foregroundTransformationMatrix;

  {
    backgroundBitmapPaint.setStyle(Paint.Style.FILL);
    backgroundSolidPaint.setStyle(Paint.Style.FILL);
  }

  public ScratchView(Context context) {

    super(context);
  }

  public ScratchView(Context context, AttributeSet attrs) {

    this(context, attrs, 0);
  }

  public ScratchView(Context context, AttributeSet attrs, int defStyle) {

    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScratchView, defStyle, 0);
    try {
      backgroundDrawable = a.getDrawable(R.styleable.ScratchView_sv_background_drawable);
      foregroundDrawable = a.getDrawable(R.styleable.ScratchView_sv_foreground_drawable);
      scratchRadius = a.getDimension(R.styleable.ScratchView_sv_scratch_radius,
              TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SCRATCH_RADIUS_DIP, getResources().getDisplayMetrics()));
      foregroundColor = a.getColor(R.styleable.ScratchView_sv_foreground_color, Color.BLACK);
    } finally {
      if (a != null) {
        a.recycle();
      }
    }

    if (backgroundDrawable != null) {
      backgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
      backgroundBitmapShader = new BitmapShader(backgroundBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      backgroundBitmapPaint.setShader(backgroundBitmapShader);
    }

    if (foregroundDrawable != null) {
      foregroundBitmap = ((BitmapDrawable) foregroundDrawable).getBitmap();
    }

    getHolder().addCallback(this);
  }

  private void drawScratchView(Canvas c) {

    drawNotScratchedAreas(c);
    drawScratchedAreas(c);
  }

  private void drawNotScratchedAreas(Canvas c) {

    if (foregroundDrawable != null) {
      if (foregroundTransformationMatrix == null) {
        foregroundTransformationMatrix = createTransformationMatrix(c, foregroundBitmap);
      }
      c.drawBitmap(foregroundBitmap, foregroundTransformationMatrix, backgroundSolidPaint);
    } else {
      c.drawColor(foregroundColor);
    }
  }

  private void drawScratchedAreas(Canvas c) {

    if (backgroundBitmap != null) {
      if (backgroundTransformationMatrix == null) {
        backgroundTransformationMatrix = createTransformationMatrix(c, backgroundBitmap);
        backgroundBitmapShader.setLocalMatrix(backgroundTransformationMatrix);
      }
      c.drawPath(scratchedPath, backgroundBitmapPaint);
    } else {
      c.drawPath(scratchedPath, backgroundSolidPaint);
    }
  }

  private Matrix createTransformationMatrix(Canvas c, Bitmap bitmap) {

    float scaleWidth = (float) c.getWidth() / bitmap.getWidth();
    float scaleHeight = (float) c.getHeight() / bitmap.getHeight();
    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleHeight);
    return matrix;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        scratchedPath.addCircle(event.getX(), event.getY(), scratchRadius, Path.Direction.CW);
        break;
    }
    return true;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int desiredWidth = backgroundBitmap.getWidth();
    int desiredHeight = backgroundBitmap.getHeight();

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;

    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      width = Math.min(desiredWidth, widthSize);
    } else {
      width = desiredWidth;
    }

    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      height = Math.min(desiredHeight, heightSize);
    } else {
      height = desiredHeight;
    }

    setMeasuredDimension(width, height);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

    drawLoopThread = new DrawLoopThread();
    drawLoopThread.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

    drawLoopThread.paused = true;
    while (true) {
      try {
        drawLoopThread.join();
      } catch (InterruptedException e) {
        Log.e(TAG, "exception while waiting to join draw loop thread", e);
      }
      break;
    }
    drawLoopThread = null;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawScratchView(canvas);
  }

  private class DrawLoopThread extends Thread {

    boolean paused = false;

    @Override
    public void run() {

      Log.d(TAG, "Starting draw loop");
      while (!paused) {
        if (!getHolder().getSurface().isValid()) {
          continue;
        }
        Canvas c = null;
        try {
          c = getHolder().lockCanvas();
          synchronized (getHolder()) {
            if (c != null) {
              drawScratchView(c);
            }
          }
        } finally {
          if (c != null) {
            getHolder().unlockCanvasAndPost(c);
          }
        }
      }
      Log.d(TAG, "Stopping draw loop");
    }
  }

}