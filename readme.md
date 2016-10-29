## ClickToSpreadView

Ripple Spread view,Spread outside the widget rect!

![Markdown](./device-2016-10-22-205519.gif)

#### This is a module of Android Studio Project;

Feel free and help yourself!

## Usage

* compile 'com.halohoop:clicktospreadview:1.0.2'

    xmlns:halohoop="http://schemas.android.com/apk/res-auto"

		<com.halohoop.clicktospreadview.ClickToSpreadView
		    android:layout_width="wrap_content"
		    android:layout_height="wrap_content"
		    halohoop:end_radius="720"
		    halohoop:stroke_color="#00f"
		    halohoop:start_stroke_width="100"
		    halohoop:animation_duration="1000"
		    android:layout_alignParentRight="true"
		    android:layout_alignParentBottom="true">

        <TextView
            android:background="#8e8e8e"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="20dp"
            android:text="halohoop"
            android:id="@+id/textView" />

    	</com.halohoop.clicktospreadview.ClickToSpreadView>

## License

    Copyright 2016, Halohoop

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
