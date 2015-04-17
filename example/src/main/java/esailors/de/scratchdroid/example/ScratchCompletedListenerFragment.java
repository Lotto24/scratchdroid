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

package esailors.de.scratchdroid.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import esailors.de.scratchdroid.ScratchView;

public class ScratchCompletedListenerFragment extends Fragment implements ScratchView.OnScratchCompletedListener {

  private ScratchView scratchView;
  private TextView label;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View content = inflater.inflate(R.layout.fragment_scratch_completed, container, false);
    scratchView = (ScratchView) content.findViewById(R.id.scratch_completed_scratch_view);
    label = (TextView) content.findViewById(R.id.scratch_completed_label);
    scratchView.setOnScratchCompletedListener(this);
    return content;
  }

  @Override
  public void onScratchCompleted(ScratchView scratchView) {
    label.setText("SCRATCH COMPLETED!");
  }
}
