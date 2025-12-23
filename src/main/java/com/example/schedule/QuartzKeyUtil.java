package com.example.schedule;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

public final class QuartzKeyUtil {

    public static JobKey jobKey(String jobId) {
        return JobKey.jobKey("JOB_" + jobId, "DEFAULT");
    }

    public static TriggerKey triggerKey(String jobId) {
        return TriggerKey.triggerKey("TRIGGER_" + jobId, "DEFAULT");
    }
}

