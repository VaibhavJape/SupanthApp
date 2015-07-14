package com.supanth;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class Application extends android.app.Application {
    public static final Bus rssFeedFetchParseBus = new Bus(ThreadEnforcer.ANY);
}