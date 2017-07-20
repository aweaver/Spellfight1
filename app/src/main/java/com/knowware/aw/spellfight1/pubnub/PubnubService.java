package com.knowware.aw.spellfight1.pubnub;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class PubnubService extends Service
{
    public static final String ACTION_PUBNUB_MSG_RESPONSE = "com.knowware.aw.spellfight1.action.PUBNUB_RESPONSE";

    private final IBinder mBinder = new LocalBinder();

    private String localUUID;


    private PubNubThread pubNubThread;

    public PubnubService()
    {
    }


    @Override
    public void onCreate()
    {
        pubNubThread= new PubNubThread();
        pubNubThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY;//must stay around even if the activity is gone
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder
    {
       public PubnubService getService()
        {
            // Return this instance of LocalService so clients can call public methods
            return PubnubService.this;
        }
    }

    public void sendMsgs(String msg)
    {
        pubNubThread.sendMsgs(msg);
    }

    public void sendMsgs(ObjectNode jsonObject)
    {
        pubNubThread.sendMsgs(jsonObject);
    }

    public String getLocalUUID()
    {
       return (pubNubThread.getLocalUUID());
    }


    @Override
    public void onDestroy ()
    {
        pubNubThread.shutDown();
        pubNubThread=null;
    }

    class PubNubThread extends Thread implements Runnable
    {
        private String localUUID;
        private PubNub pubnub;
        private PnManagerSubscribeCallback PnSubcallback;
        private String MSG_CHANNEL="Channel1";
        private boolean bFinished=false;


        public PubNubThread()
        {
            PNConfiguration pnConfiguration = new PNConfiguration();
            pnConfiguration.setSubscribeKey("");
            pnConfiguration.setPublishKey("");

            pubnub = new PubNub(pnConfiguration);

            localUUID=pnConfiguration.getUuid();
        }

        public void setupPubNub()
        {
            PnSubcallback= new PnManagerSubscribeCallback();
            pubnub.addListener(PnSubcallback);
            pubnub.subscribe().channels(Arrays.asList(MSG_CHANNEL)).execute();

            //notifyListenersUUID();
        }

        @Override
        public void run()
        {

            setupPubNub();

            while(bFinished==true)
            {

            }
        }

        public String getLocalUUID()
        {
            return(localUUID);
        }

        public void sendMsgs(String msg)
        {

            if(bFinished)
                return;

            pubnub.publish()
                    .message(msg)//Arrays.asList(msg)
                    .channel(MSG_CHANNEL)
                    .async(new PNCallback<PNPublishResult>()
                    {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status)
                        {
                            // handle publish result, status always present, result if successful
                            // status.isError to see if error happened

                            if(status.isError())
                            {
                                // notifyListeners(0,status.getErrorData().getInformation());
                                Log.d("PubNubThread/sendmsgs", status.getErrorData().getInformation());
                            }
                        }
                    });
        }


        public void sendMsgs(ObjectNode jsonObject)
        {


            if(bFinished)
                return;

            pubnub.publish()
                    .message(jsonObject)//Arrays.asList(msg)
                    .channel(MSG_CHANNEL)
                    .async(new PNCallback<PNPublishResult>()
                    {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status)
                        {
                            String stemp;

                            // handle publish result, status always present, result if successful
                            // status.isError to see if error happened

                            if(status.isError())
                            {

                                stemp=status.getErrorData().getInformation();
                                Log.d("PubNubThread/send obj",stemp );

                                //notifyListeners(0,status.getErrorData().getInformation());
                            }
                        }
                    });
        }

        public void shutDown()
        {
            bFinished=true;
            pubnub.unsubscribe().channels(Arrays.asList(MSG_CHANNEL)).execute();
            pubnub.removeListener(PnSubcallback);
        }

    }

    public class PnManagerSubscribeCallback extends SubscribeCallback
    {
        private boolean bConnected=false;

        @Override
        public void message(PubNub arg0, PNMessageResult arg1)
        {
            String sender;
            String stemp;
            int ltime;

            // sender = arg1.getMessage().get("src").asText();
            //if(sender.equals(localUUID))
            //   return;

            //JsonNode jsonMsg = arg1.getMessage();
            //stemp= jsonMsg.toString();

            //;how to filter out own  msgss
            //https://www.pubnub.com/docs/android/stream-filtering-tutorial-sdk-v4
            stemp=arg1.getMessage().toString();

            // Since msgs are broadcast to everyone even the sender
            // have to ignore self sent msgs or log them
            broadcastData(stemp);


            //notifyListeners(1,stemp);
        }

        void broadcastData(String sdata)
        {
            Intent intent = new Intent(ACTION_PUBNUB_MSG_RESPONSE);
            intent.putExtra("data", sdata);
            intent.putExtra("type", "d");
            sendBroadcast(intent);
        }

        void broadcastStatus(String status)
        {
            Intent intent = new Intent(ACTION_PUBNUB_MSG_RESPONSE);
            intent.putExtra("data",status );
            intent.putExtra("type", "s");
            sendBroadcast(intent);
        }

        @Override
        public void presence(PubNub arg0, PNPresenceEventResult arg1)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void status(PubNub arg0, PNStatus status)
        {
            if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory)
            {
                // This event happens when radio / connectivity is lost

                Log.d("status/send obj",status.getErrorData().getInformation());
                bConnected=false;
            }

            else if (status.getCategory() == PNStatusCategory.PNConnectedCategory)
            {

                // Connect event. You can do stuff like publish, and know you'll get it.
                // Or just use the connected event to confirm you are subscribed for
                // UI / internal notifications, etc

                if (status.getCategory() == PNStatusCategory.PNConnectedCategory)
                {
                    bConnected=true;

                }
            }
            else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                // Happens as part of our regular operation. This event happens when
                // radio / connectivity is lost, then regained.
                bConnected=true;
            }
            else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                // Handle messsage decryption error. Probably client configured to
                // encrypt messages and on live data feed it received plain text.
            }

        }

    }

}
