package com.appquanly.japanfigure.Retrofit;


import com.appquanly.japanfigure.Model.*;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAA3AaIvBk:APA91bHutJsJWsP04NbaAZD6sm6AT93JMQCB533GHGuTKc3jIZ7G6GwUJYuadC3ufjAulx-VdfMhR8SA1cfESNOyvT2D0UeL39JK_uA3MEiylCpE_j2A0gxTpVPPW66ULdwl1JBF3B0N"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNofication(@Body NotiSendData notiSendData);
}
