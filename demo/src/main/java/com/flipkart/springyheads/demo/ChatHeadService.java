package com.flipkart.springyheads.demo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flipkart.chatheads.ui.ChatHead;
import com.flipkart.chatheads.ui.ChatHeadContainer;
import com.flipkart.chatheads.ui.ChatHeadViewAdapter;
import com.flipkart.chatheads.ui.MinimizedArrangement;
import com.flipkart.chatheads.ui.container.DefaultChatHeadManager;
import com.flipkart.chatheads.ui.container.WindowManagerContainer;
import com.flipkart.circularImageView.CircularDrawable;
import com.flipkart.circularImageView.OverlayArcDrawer;
import com.flipkart.circularImageView.TextDrawer;
import com.flipkart.circularImageView.notification.CircularNotificationDrawer;

import java.util.Random;

public class ChatHeadService extends Service {

    private DefaultChatHeadManager<String> chatContainer;
    private int chatHeadIdentifier = 0;


    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ChatHeadContainer chatHeadContainer = new WindowManagerContainer(this);
        chatContainer = new DefaultChatHeadManager<String>(this, chatHeadContainer);
        chatContainer.setViewAdapter(new ChatHeadViewAdapter<String>() {

            @Override
            public View createView(String key, ChatHead chatHead, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.fragment_test, parent, false);
                TextView identifier = (TextView) view.findViewById(R.id.identifier);
                identifier.setText(key);
                return view;
            }

            @Override
            public Drawable getChatHeadDrawable(String key) {
                return ChatHeadService.this.getChatHeadDrawable(key);
            }

            @Override
            public Drawable getPointerDrawable() {
                return getResources().getDrawable(R.drawable.circular_ring);
            }

            @Override
            public View getTitleView(String key, ChatHead chatHead) {
                return null;
            }
        });

        addChatHead();
        addChatHead();
        addChatHead();
        addChatHead();
        chatContainer.setArrangement(MinimizedArrangement.class, null);
        chatContainer.onMeasure();

        moveToForeground();

    }

    private Drawable getChatHeadDrawable(String key) {
        Random rnd = new Random();
        int randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        CircularDrawable circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrTextOrIcon(new TextDrawer().setText("C"+key).setBackgroundColor(randomColor));
        int badgeCount = (int) (Math.random() * 10f);
        circularDrawable.setNotificationDrawer(new CircularNotificationDrawer().setNotificationText(String.valueOf(badgeCount)).setNotificationAngle(135).setNotificationColor(Color.WHITE, Color.RED));
        circularDrawable.setBorder(Color.WHITE, 3);
        return circularDrawable;

    }

    private void moveToForeground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentText("Chat heads is active")
                .build();

        startForeground(1, notification);
    }

    private void addChatHead() {
        chatHeadIdentifier++;
        chatContainer.addChatHead(String.valueOf(chatHeadIdentifier), false, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}