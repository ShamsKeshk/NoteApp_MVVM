package com.example.applicationmvvm;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

   private static final Object LOCK = new Object();
   private Executor diskIO;
   private Executor networkIO;
   private Executor mainThread;

   private static AppExecutors appExecutors;

   private AppExecutors(Executor diskIO,Executor networkIO,Executor mainThread){
       this.diskIO = diskIO;
       this.networkIO = networkIO;
       this.mainThread = mainThread;
   }

   public static AppExecutors getInstance(){
       if (appExecutors == null){
           appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                   Executors.newFixedThreadPool(3),
                   new MainThreadExecutors());
       }

       return appExecutors;
   }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutors implements Executor {

       private Handler handler = new Handler(Looper.getMainLooper());

       @Override
       public void execute(Runnable command) {
           handler.post(command);
       }
   }
}
