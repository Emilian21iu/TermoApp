package com.example.starnet.termoapp.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.starnet.termoapp.MainActivity;
import com.example.starnet.termoapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationMessage {

    private String thicker;
    private String title;
    private String text;
    private int id;
    private NotificationCompat.Builder notification;
    private final String CHANEL_ID = "personal_notification";

    public void SendNotification(Context context){
        notification =  new NotificationCompat.Builder(context,CHANEL_ID);
        notification.setAutoCancel(true);
        notification.setTicker(thicker);
        notification.setContentText(text);
        notification.setContentTitle(title);
        notification.setWhen(System.currentTimeMillis());
        notification.setSmallIcon(R.drawable.ic_notification_icon);

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification.build());
    }

    public NotificationMessage(int status) {

        switch (status){
            case ParametersAlertStatus.LOW_BATTERY:
                setId(NotificationIDsClass.LOW_BATTERY_ID);
                setText("Battery is under 15%, please connect the charger.");
                setTitle("Low battery");
                setThicker("Battery is 15% or lower");
                break;
            case ParametersAlertStatus.LOW_PRESSURE:
                setId(NotificationIDsClass.LOW_PRESSURE_ID);
                setText("Presiunea a scazut sub limita normala!");
                setTitle("Presiune scazuta");
                setThicker("Alerta de presiune");
                break;

            case ParametersAlertStatus.HIGH_PRESSURE:
                setId(NotificationIDsClass.HIGH_PRESSURE_ID);
                setText("Presiunea a depasit limita normala!");
                setTitle("Presiune mare");
                setThicker("Alerta de presiune");
                break;
            case ParametersAlertStatus.LOW_TEMPERATURE:
                setId(NotificationIDsClass.LOW_TEMPERATURE_ID);
                setText("Temperatura a scazut sub limita normala!");
                setTitle("Temperatura scazuta");
                setThicker("Alerta de temperatura");
                break;
            case ParametersAlertStatus.HIGH_TEMPERATURE:
                setId(NotificationIDsClass.HIGH_TEMPERATURE_ID);
                setText("Temperatura a depasit limita normala!");
                setTitle("Temperatura mare");
                setThicker("Alerta de temperatura");
                break;
            case ParametersAlertStatus.LOW_WATER:
                setId(NotificationIDsClass.LOW_WATER_ID);
                setText("Temperatura apei a scazut sub 5 °C!");
                setTitle("Temperatura apei scazuta");
                setThicker("Alerta de temperatura");
                break;
            case ParametersAlertStatus.HIGH_WATER:
                setId(NotificationIDsClass.HIGH_WATER_ID);
                setText("Temperatura apei a depasit limita normala!");
                setTitle("Temperatura apei  este prea mare");
                setThicker("Alerta de temperatura");
                break;
            case ParametersAlertStatus.LOW_HEATING:
                setId(NotificationIDsClass.LOW_HEATING_ID);
                setText("Temperatura apei a scazut sub °C!");
                setTitle("Temperatura Incalziri este ridicata");
                setThicker("Alerta de temperatura");
                break;
            case ParametersAlertStatus.HIGH_HEATING:
                setId(NotificationIDsClass.HIGH_HEATING_ID);
                setText("Temperatura apei a depasit limita normala!");
                setTitle("Temperatura incalzirei este mica");
                setThicker("Alerta de temperatura");
                break;
        }
    }

    public void setThicker(String thicker) {
        this.thicker = thicker;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThicker() {
        return thicker;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
