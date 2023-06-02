package com.alibaba.jvm.sandbox.module.debug;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@MetaInfServices(Module.class)
@Information(id = "change-resp")
public class ChangeRespModule implements Module {
    private static final Logger logger = LoggerFactory.getLogger(ChangeRespModule.class);

    private final static String CLASS_NAME = "com.chj.magic.api.rest.DemoController";
    private final static String METHOD_NAME = "selectCount";
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("changeResp")
    public void changeResp() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(CLASS_NAME)
                .onBehavior(METHOD_NAME)
                .onWatch(new AdviceListener() {
                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        logger.info("afterReturning");
                        ProcessController.returnImmediately("总条数=" + advice.getReturnObj());
                    }
                });
    }
}