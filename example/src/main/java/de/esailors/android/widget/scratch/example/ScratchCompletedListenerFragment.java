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

package de.esailors.android.widget.scratch.example;

import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.esailors.android.widget.scratch.ScratchView;

public class ScratchCompletedListenerFragment extends Fragment implements ScratchView.OnScratchCompletedListener {

  private static final String TAG = "ScratchCompleted";

  private ScratchView scratchViewWithDefaultScratchRegion;
  private ScratchView scratchViewWithCustomScratchRegion;
  private TextView label;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View content = inflater.inflate(R.layout.fragment_scratch_completed, container, false);
    scratchViewWithDefaultScratchRegion = (ScratchView) content.findViewById(R.id.scratch_completed_scratch_view);
    scratchViewWithCustomScratchRegion = (ScratchView) content.findViewById(R.id.scratch_completed_custom_scratch_region_scratch_view);

    label = (TextView) content.findViewById(R.id.scratch_completed_label);
    scratchViewWithDefaultScratchRegion.setOnScratchCompletedListener(this);

    scratchViewWithCustomScratchRegion.setOnScratchCompletedListener(this)
            .setScratchPathCalculator(new ScratchView.ScratchPathCalculator() {
              @Override
              public Path calculateScratchPath(Rect surfaceFrame) {

                // TODO: set paths and let the view resize them instead of using this ugly calculator
                // see http://stackoverflow.com/questions/18637817/resize-a-path-in-android-canvas

                // custom scratch region for the apples
                Path p = new Path();

                float width = 293,
                        height = 108,
                        appleWidth = 30,
                        appleHeight = 35,
                        horizontalScale = surfaceFrame.width() / width,
                        verticalScale = surfaceFrame.height() / height;

                // first apple;
                float left = 134,
                        top = 53;
                p.addRect(left * horizontalScale, top * verticalScale,
                        (left + appleWidth) * horizontalScale,
                        (top + appleHeight) * verticalScale, Path.Direction.CW);

                // second apple
                left = 241;
                top = 57f;
                p.addRect(left * horizontalScale, top * verticalScale,
                        (left + appleWidth) * horizontalScale,
                        (top + appleHeight) * verticalScale, Path.Direction.CW);

                return p;
              }
            })
            .setDebug(true); // make custom scratch region visible

    return content;
  }

  @Override
  public void onScratchCompleted(ScratchView scratchView) {

    if (scratchView == ScratchCompletedListenerFragment.this.scratchViewWithDefaultScratchRegion) {
      Log.d(TAG, "FULL SCRATCH COMPLETED!");
      label.setText("FULL SCRATCH COMPLETED!");
    } else if (scratchView == scratchViewWithCustomScratchRegion) {
      Log.d("ScratchCompleted", "APPLES SCRATCH COMPLETED!");
      label.setText("APPLES SCRATCH COMPLETED!");
      scratchViewWithCustomScratchRegion.scratchAll();
    }
  }
}
