package com.example.techsic.firebase;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PushNotification {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization:key=AAAAZt-hwAc:APA91bG6cZljPCt2ge2XFAEuvxTnVScrraa7kmcevbjITm6YzjbBqS7uFfWjvba4doLrPjWc5NdZ6YjpRJfwKnmHJ5QvqzxeDhecSYrc6w-DD0kHHOTBY4Fh1TYpBf0qmkVhUgP2Kv_K"
            }
    )
    @POST("fcm/send")
    @NonNull
    Observable<NotificationResponse> sendNotification(@Body NotificationSendData data);
}
