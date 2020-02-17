# RoboJyve
Application which spawns robots on the screen corresponding to user input. 

Tools used: Glide for image handling, androidx.test + mockK for testing, coroutines for concurrency. 

Code is written to be modular and scalable for future improvements. The network module is unit tested and the app module is UI/integration tested. In an ideal scenario the app module would be unit tested for full code coverage as well.
Manual dependency injection is used where possible to allow unit testability of all functions/classes.
Dagger is a tool that would be considered in a larger-scale app with much larger dependency trees. 
The network module contains an OKhttpclient as well for potential future code changes that might be needed. It isn't used for this project but does display some architecture patterns which I try to follow as well as good examples of unit testing tactics. 

If the debug variant is run, LeakCanary will run to check for memory leaks and StrictMode will be kill the app if any issues occur i.e. running IO operations on the main thread.

The app is developed to scale into a single-activity-multiple-fragment architecture as described here: https://medium.com/rosberryapps/a-single-activity-android-application-why-not-fa2a5458a099 This design allows for targetting of many different screens, including tablets and other large devices.

Some future improvements/notes about issues:

-Shared preferences is used to persist data through resets, however the data is stored as a set. On app relaunch, order of the robot pictures is not maintained.

-On network failure, the robot images will display a default picture instead. This is handled by Glide and currently there is no retry mechanism when network is resumed, therefore app must be restarted to reload images.

-Support dark mode which is a requirement for Android Q devices
