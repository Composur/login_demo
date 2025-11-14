package com.example.web.controller;

import com.example.common.Response;
import com.example.service.MonitorQuartzJobService;
import com.example.service.dto.QuartzJobDTO;
import com.example.web.mapper.QuartzJobTransfer;
import com.example.web.req.QuartzJobQueryPageReq;
import com.example.web.req.QuartzJobSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.QuartzJobQueryPageResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/monitor/quartz/job")
@RequiredArgsConstructor
public class MonitorQuartzJobController {

    private final MonitorQuartzJobService monitorQuartzJobService;

    /**
     * 保存定时任务
     *
     * @param req 定时任务保存请求
     * @return 保存结果
     */
    @PostMapping("/save")
    public Response<String> saveQuartzJob(@RequestBody @Valid QuartzJobSaveReq req) {
        log.info("保存定时任务请求: {}", req);

        try {
            // 新增时验证：id 必须为空（新增时ID由系统自动生成）
            if (req.getId() != null && !req.getId().trim().isEmpty()) {
                return Response.error("新增定时任务时不能传入ID，ID将由系统自动生成");
            }

            // 验证cron表达式
            CronExpression cronExpression = new CronExpression(req.getCronExpression());

            // 保存定时任务
            String jobId = monitorQuartzJobService.save(req);

            log.info("定时任务保存成功，ID: {}", jobId);
            return Response.success(jobId);

        } catch (ParseException e) {
            log.error("cron表达式格式错误: {}, 错误信息: {}", req.getCronExpression(), e.getMessage());
            return Response.error("cron表达式格式错误: " + e.getMessage());
        } catch (Exception e) {
            log.error("保存定时任务异常: {}", e.getMessage(), e);
            return Response.error("保存定时任务失败: " + e.getMessage());
        }
    }

    /**
     * 更新定时任务
     *
     * @param req 定时任务保存请求（需包含id）
     * @return 更新结果
     */
    @PutMapping("/update/{id}")
    public Response<String> updateQuartzJob(@PathVariable String id, @RequestBody @Valid QuartzJobSaveReq req) {
        log.info("更新定时任务请求: {}", req);

        try {
            // 验证ID是否存在
            if (id == null || id.trim().isEmpty()) {
                return Response.error("更新定时任务时ID不能为空");
            }

            // 验证cron表达式
            CronExpression cronExpression = new CronExpression(req.getCronExpression());

            // 更新定时任务
            String jobId = monitorQuartzJobService.update(req);

            log.info("定时任务更新成功，ID: {}", jobId);
            return Response.success(jobId);

        } catch (ParseException e) {
            log.error("cron表达式格式错误: {}, 错误信息: {}", req.getCronExpression(), e.getMessage());
            return Response.error("cron表达式格式错误: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("更新定时任务参数错误: {}", e.getMessage());
            return Response.error(e.getMessage());
        } catch (Exception e) {
            log.error("更新定时任务异常: {}", e.getMessage(), e);
            return Response.error("更新定时任务失败: " + e.getMessage());
        }
    }

    /**
     * 立即执行任务
     */
    @PostMapping("/execute/{id}")
    public Response<String> executeQuartzJob(@PathVariable String id) {
        log.info("立即执行定时任务: {}", id);
        // 验证ID是否存在
        if (id == null || id.trim().isEmpty()) {
            return Response.error("立即执行定时任务时ID不能为空");
        }

        // 立即执行定时任务
        String jobId = monitorQuartzJobService.execute(id);

        log.info("定时任务立即执行成功，ID: {}", jobId);
        return Response.success(jobId);
    }

    // 列表查询
    @GetMapping("/query/page")
    public Response<PageResult<QuartzJobQueryPageResp>> queryPage(QuartzJobQueryPageReq req) {
        log.info("分页查询定时任务: {}", req);
        PageResult<QuartzJobDTO> dtoPage = monitorQuartzJobService.queryPage(req);
        PageResult<QuartzJobQueryPageResp> respPage = QuartzJobTransfer.INSTANCE.toRespPage(dtoPage);
        return Response.success(respPage);
    }

    /**
     * 检查cron表达式是否有效
     *
     * @param cron cron表达式
     * @return 验证结果
     */
    @GetMapping("/check/cron")
    public Response<String> checkCronExpression(@RequestParam("cron") String cron) {
        log.info("检查cron表达式: {}", cron);

        Map<String, Object> result = new HashMap<>();

        try {
            // 验证cron表达式
            CronExpression cronExpression = new CronExpression(cron);

            // 检查表达式是否有效 - 如果能成功创建CronExpression对象，说明表达式有效
            result.put("valid", true);
            result.put("message", "cron表达式有效");

            // 获取下一个执行时间
            java.util.Date nextFireTime = cronExpression.getNextValidTimeAfter(new java.util.Date());
            if (nextFireTime != null) {
                result.put("nextFireTime", nextFireTime);
            }

            log.info("cron表达式验证成功: {}", cron);
            return Response.success("cron表达式验证成功");

        } catch (ParseException e) {
            result.put("valid", false);
            result.put("message", "cron表达式格式错误: " + e.getMessage());
            log.error("cron表达式解析异常: {}, 错误信息: {}", cron, e.getMessage());
            return Response.error("cron表达式格式错误: " + e.getMessage());
        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "cron表达式验证异常: " + e.getMessage());
            log.error("cron表达式验证异常: {}, 错误信息: {}", cron, e.getMessage());
            return Response.error("cron表达式验证异常: " + e.getMessage());
        }
    }

    /**
     * 获取cron表达式说明
     *
     * @return cron表达式格式说明
     */
    @GetMapping("/cron/help")
    public Response<Map<String, Object>> getCronHelp() {
        Map<String, Object> help = new HashMap<>();
        help.put("format", "秒 分 时 日 月 周");
        help.put("examples", new String[]{
                "0 0 12 * * ? - 每天中午12点执行",
                "0 15 10 ? * * - 每天上午10:15执行",
                "0 15 10 * * ? - 每天上午10:15执行",
                "0 15 10 * * ? * - 每天上午10:15执行",
                "0 15 10 * * ? 2005 - 2005年每天上午10:15执行",
                "0 * 14 * * ? - 每天下午2点到2:59期间每分钟执行",
                "0 0/5 14 * * ? - 每天下午2点到2:55期间每5分钟执行",
                "0 0/5 14,18 * * ? - 每天下午2点到2:55期间和6点到6:55期间每5分钟执行",
                "0 0-5 14 * * ? - 每天下午2点到2:05期间每分钟执行",
                "0 10,44 14 ? 3 WED - 每年3月的星期三下午2:10和2:44执行",
                "0 15 10 ? * MON-FRI - 周一到周五上午10:15执行",
                "0 15 10 15 * ? - 每月15日上午10:15执行",
                "0 15 10 L * ? - 每月最后一天上午10:15执行",
                "0 15 10 ? * 6L - 每月最后一个星期五上午10:15执行",
                "0 15 10 ? * 6L 2002-2005 - 2002年到2005年每月最后一个星期五上午10:15执行",
                "0 15 10 ? * 6#3 - 每月第三个星期五上午10:15执行"
        });
        help.put("fields", new String[]{
                "秒 (0-59)",
                "分 (0-59)",
                "时 (0-23)",
                "日 (1-31)",
                "月 (1-12 或 JAN-DEC)",
                "周 (1-7 或 SUN-SAT)"
        });
        help.put("special", new String[]{
                "* - 表示所有值",
                "? - 表示不指定值，仅用于日和周字段",
                "- - 表示范围，如10-12表示10,11,12",
                ", - 表示多个值，如1,3,5表示1,3,5",
                "/ - 表示增量，如0/15表示从0开始每15个单位",
                "L - 表示最后，仅用于日和周字段",
                "W - 表示工作日，仅用于日字段",
                "# - 表示第几个星期几，仅用于周字段"
        });

        return Response.success(help);
    }

    /**
     * 删除定时任务
     *
     * @param id 定时任务ID
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Response<String> deleteQuartzJob(@RequestParam("id") String id) {
        log.info("删除定时任务请求，ID: {}", id);

        try {
            if (id == null || id.trim().isEmpty()) {
                return Response.error("删除定时任务时ID不能为空");
            }

            monitorQuartzJobService.delete(id);
            log.info("定时任务删除成功，ID: {}", id);
            return Response.success("删除定时任务成功");
        } catch (IllegalArgumentException e) {
            log.error("删除定时任务参数错误: {}", e.getMessage());
            return Response.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除定时任务异常: {}", e.getMessage(), e);
            return Response.error("删除定时任务失败: " + e.getMessage());
        }
    }
}
