## xxl-job 源码解析 基于2.1.2版本

### 启动类 XxlJobSpringExecutor

#### XxlJobSpringExecutor 继承了 XxlJobExecutor 实现了 ApplicationContextAware, InitializingBean, DisposableBean
##### 启动加载方法
      ```
      	public void afterPropertiesSet() throws Exception {

      		// init JobHandler Repository 废弃的方法，加载注解 @JobHandler 的bean存入 jobHandlerRepository ConcurrentMap
      		initJobHandlerRepository(applicationContext);

      		// init JobHandler Repository (for method) 查询spring上下文中 @XxlJob注解的对象，并存入 jobHandlerRepository ConcurrentMap
      		initJobHandlerMethodRepository(applicationContext);

      		// refresh GlueFactory 初始化 Glue
      		GlueFactory.refreshInstance(1);

      		// super start
      		super.start();
      	}

      ```
##### XxlJobExecutor.start() 方法
      ```
      	public void start() throws Exception {

      		// init logpath 初始化日志目录
      		XxlJobFileAppender.initLogPath(logPath);

      		// init invoker, admin-client 初始化admin地址
      		initAdminBizList(adminAddresses, accessToken);


      		// init JobLogFileCleanThread 启动清理线程为守护线程，每日启动一次，执行完毕使用 TimeUnit.DAYS.sleep(1);
      		JobLogFileCleanThread.getInstance().start(logRetentionDays);

      		// init TriggerCallbackThread 初始化 TriggerCallbackThread
      		TriggerCallbackThread.getInstance().start();

      		// init executor-server
      		port = port>0?port: NetUtil.findAvailablePort(9999);
      		ip = (ip!=null&&ip.trim().length()>0)?ip: IpUtil.getIp();
      		// 初始化rpc
      		initRpcProvider(ip, port, appName, accessToken);
      	}

      ```
##### TriggerCallbackThread.start()方法
	1. 启动线程
	2. take LinkedBlockingQueue
	3. LinkedBlockingQueue.drainTo 到 callbackParamList
	4. 循环控制台admin，调用 api/callback 接口
##### admin api/callback 接口
	1. 查询 XxlJobLog表	
	2. 查询 xxlJobInfo表
	3. String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
	4. 子job循环调用  JobTriggerPoolHelper.trigger
##### JobTriggerPoolHelper
	1. .start() 启动两个线程池 fastTriggerPool和slowTriggerPool
	2. addTrigger() 
		1. 根据 jobTimeoutCountMap 的jobid判断和触发次数判断使用fastTriggerPool or slowTriggerPool	
		2. XxlJobTrigger.trigger();
		3. 执行时间大于500，重置 jobTimeoutCountMap中的jobid的计数器
##### XxlJobTrigger
	1. trigger();
		1. 根据jobId查询 XxlJobInfo表	
		2. 根据getJobGroup 查询 XxlJobGroup表
		3. 如果执行器路由策略等于SHARDING_BROADCAST，并且分片参数为空 
		3. 循环执行器执行 processTrigger();
	2. processTrigger()
		1. 确定丢弃策略
		2. 确定 执行器路由策略
		3. 若为分片拼接index和total
		4. save log-id
		4. 获取路由地址
		5. 注册job 并启动线程
	
	 