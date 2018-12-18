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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;

public class ExampleActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

  private int selectedFragment = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    getSupportActionBar().setListNavigationCallbacks(
            ArrayAdapter.createFromResource(
                    getSupportActionBar().getThemedContext(),
                    R.array.action_list,
                    android.R.layout.simple_spinner_dropdown_item),
            this);

    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .replace(android.R.id.content, new FillForegroundFragment())
              .commit();
    }
  }

  @Override
  public boolean onNavigationItemSelected(int itemPosition, long itemId) {

    if (itemPosition == selectedFragment) {
      return true;
    }

    selectedFragment = itemPosition;
    Fragment newFragment;
    switch (itemPosition) {
      default:
      case 0:
        newFragment = new FillForegroundFragment();
        break;
      case 1:
        newFragment = new DrawableForegroundFragment();
        break;
      case 2:
        newFragment = new ScratchCompletedListenerFragment();
        break;
      case 3:
        newFragment = new ScratchCustomFragment();
        break;
    }

    getSupportFragmentManager().beginTransaction()
            .replace(android.R.id.content, newFragment)
            .commit();

    return true;
  }
}
