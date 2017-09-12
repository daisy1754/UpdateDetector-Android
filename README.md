AndroidUpdateDetector [![Build Status](https://travis-ci.org/daisy1754/UpdateDetector-Android.svg?branch=master)](https://travis-ci.org/daisy1754/UpdateDetector-Android)
=======
Check version information of your Android app and notify when update is available.

*Note this project is still version 0.1 and work-in-progress.*

### Usage
#### Server-side setup
You'll need a remote endpoint that your client app can request JSON file. It can be a part of your API server, or static file hosting like Amazon S3.

This library expects JSON in following format
```
{
  "version": "1.1.0"
}
```

#### Client-side setup
```java
UpdateDetector
  .withRemoteVersionFileUrl(YOUR_REMOTE_ENDPOINT)
  .register(context, new UpdateDetectedCallback() {
     @Override
     public void onUpdateFound(
         @UpdateType int updateType, String latestVersion) {
       // Handle version update here.
    }
});
```


License
--------
[MIT](LICENSE.txt)
