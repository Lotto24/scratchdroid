# Scratchdroid
ScratchView for Android.

[![Build Status](https://travis-ci.org/eSailors/scratchdroid.svg)](https://travis-ci.org/eSailors/scratchdroid)

![ScratchView demo video](https://raw.githubusercontent.com/josketres/scratchdroid/master/art/video-scratch-listener.gif)

Usage
----
Define in xml:

```xml
<de.esailors.android.widget.scratch.ScratchView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/scratch_completed_scratch_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    app:sv_scratch_background_drawable="@drawable/scratch_card_bg"
    app:sv_scratch_foreground_drawable="@drawable/repeatable_texture"
    app:sv_scratch_foreground_repeat="true"
    app:sv_scratch_radius="30dp"
    />
```

Or in code:

```java
ScratchView sv = new ScratchView(context)
.setScratchBackground(backgroundDrawable)
.setScratchForegroundRepeat(true)
.setScratchRadius((float) 30)
.setOnScratchCompletedListener(new ScratchView.OnScratchCompletedListener() {
    @Override
    public void onScratchCompleted(ScratchView view) {
    // do something
    }
});
```

License
-------

    Copyright 2015 eSailors IT Solutions GmbH

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
