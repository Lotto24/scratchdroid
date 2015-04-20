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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.esailors.android.widget.scratch.CustomScratchPath;
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
            .setCustomScratchPath(getCustomScratchPath());

    content.findViewById(R.id.scratch_completed_toggle_debug).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        boolean on = ((ToggleButton) v).isChecked();

        // make custom scratch region visible if true
        scratchViewWithCustomScratchRegion.setDebug(on);
        scratchViewWithDefaultScratchRegion.setDebug(on);
      }
    });

    return content;
  }

  private CustomScratchPath getCustomScratchPath() {

    CustomScratchPath p = new CustomScratchPath(293f, 108f);

    float appleWidth = 30,
            appleHeight = 35;

    // first apple;
    float left = 134,
            top = 53;

    p.addRect(left, top,
            left + appleWidth,
            top + appleHeight, Path.Direction.CW);

    // second apple
    left = 241;
    top = 57f;

    p.addRect(left, top,
            left + appleWidth,
            top + appleHeight, Path.Direction.CW);

    return p;
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
