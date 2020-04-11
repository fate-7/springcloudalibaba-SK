package com.chengshare.contentcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.TestBaiduFeignClient;
import com.chengshare.contentcenter.feignclient.TestFeignClient;
import com.chengshare.contentcenter.service.TestService;
import com.chengshare.sentineltest.TestControllerBlockHandlerClass;
import com.chengshare.sentineltest.TestControllerFallbackHandlerClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fate7
 * @Date 2020/4/10 3:44 下午
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final TestFeignClient testFeignClient;

    private final TestBaiduFeignClient testBaiduFeignClient;

    @GetMapping("test-get")
    public UserDTO query(UserDTO userDTO) {
        return testFeignClient.query(userDTO);
    }

    @GetMapping("baidu")
    public String baidu() {
        return testBaiduFeignClient.index();
    }


    @Autowired
    private TestService testService;

    @GetMapping("test-a")
    public String testA() {
        this.testService.common();
        return "test-a";
    }

    @GetMapping("test-b")
    public String testB() {
        this.testService.common();
        return "test-b";
    }

    //配置热点规则：针对参数的流控规则
    @GetMapping("test-hot")
    @SentinelResource("hot")
    public String testHot(
            @RequestParam(required = false) String a,
            @RequestParam(required = false) String b
    ) {

        return a+ " " + b;
    }


    @GetMapping("test-add-flow-rule")
    public String testAddFlowRule() {
        this.initFlowQpsRule();
        return "success";
    }

    //代码配置流控规则
    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/shares/1");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * Sentinel的核心API
     * SphU 定义资源，让资源收到监控，保护资源
     * Tracer 对想要的异常实现统计
     * ContextUtil 针对来源，标记调用
     * @param a
     * @return
     */
    @GetMapping("/test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a) {

        String resourceName = "test-sentinel-api";

        //指定流控规则的针对来源
        ContextUtil.enter(resourceName, "test-wfw");
        //定义一个Sentinel保护的资源，名称是test-sentinel-api
        Entry entry =null;
        try {
            entry = SphU.entry(resourceName);
            //被保护的业务逻辑
            if(StringUtils.isBlank(a)) {
                //降级处理默认只处理BlockException
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        }
        //如果被保护的资源被限流或降级了，就会抛出BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了",e);
            return "限流，或者降级了";
        }
        catch (IllegalArgumentException e2) {
            //统计IllegalArgumentException发生的次数发生的占比
            Tracer.trace(e2);
            return "a不能为空,发生降级事件";
        }
        finally {
            if (entry != null) {
                //推出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-resource")
    //降级处理方法,必须和保护的方法有相同类型的返回值以及相同类型的参数
    @SentinelResource(
            value = "test-sentinel-resource",
            blockHandler = "block",
            blockHandlerClass = TestControllerBlockHandlerClass.class,
            fallback = "fallback")
    public String testSentinelAPITo(@RequestParam(required = false) String a) {

        //被保护的业务逻辑
        if(StringUtils.isBlank(a)) {
            //降级处理默认只处理BlockException
            throw new IllegalArgumentException("a cannot be block.");
        }

        return a;

    }



    /**
     * 处理降级
     *  - sentinel1.6处理throwable?
     * @param a
     * @param e
     * @return
     */
    public String fallback(String a){
        log.warn("降级 fallback");
        return "降级 fallback";
    }
}
