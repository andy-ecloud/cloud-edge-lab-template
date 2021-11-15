# 實作步驟

1. 準備開發環境
    - IDE：可使用 Eclipse [https://www.eclipse.org/downloads/packages/release/mars/r/eclipse-ide-java-ee-developers](https://www.eclipse.org/downloads/packages/release/mars/r/eclipse-ide-java-ee-developers)
    - OpenJDK 11：可以到這邊下載 [https://developer.ibm.com/languages/java/semeru-runtimes/downloads](https://developer.ibm.com/languages/java/semeru-runtimes/downloads)，11.0.13.0 版本
    - 匯入預先準備好的範本專案

##

2. 撰寫 API 後端程式 (api-backend)
    - 該專案使用 spring boot 2.5.6，並已載入 spring-web 函式庫
    - 實作一個測試用端點，允許接受一個請求參數(delay)，來決定該 API 延遲多少秒後才回應
    - 回應資料可任意定義
    - 使用工具進行API端點測試，如 postman 

##

3. 撰寫 API 前端程式 (api-frontend)
    - 該專案使用 spring boot 2.5.6，並已載入 spring-web 函式庫
    - 實作一個測試用端點，允許接受一個請求參數(delay)，該端點轉呼叫 API 後端測試用端點
    - 使用 Apache HTTPClient 實作，設定將 Connection 數上限調高 (1000)

```
@GetMapping
public ResponseEntity<?> test(@RequestParam(required = false, defaultValue = "0") int delay) {
    logger.info("Test API is invoked. (Blocking)");

    HttpGet httpGet = new HttpGet(backendEndpoint + "?delay=" + delay);

    String content;

    try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
        content = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        EntityUtils.consume(httpResponse.getEntity());
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    } finally {
        if (httpGet != null) {
            httpGet.releaseConnection();
            httpGet = null;
        }
    }

    return ResponseEntity.ok(content);
}
```

```
@Bean
public CloseableHttpClient httpClient() {
    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory()).build();

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
            socketFactoryRegistry);
    connectionManager.setMaxTotal(1000);
    connectionManager.setDefaultMaxPerRoute(1000);

    return HttpClients.custom().setConnectionManager(connectionManager)
            .setConnectionReuseStrategy(new ConnectionReuseStrategy() {
                @Override
                public boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext) {
                    return false;
                }
            }).build();
}
```

##

4. 撰寫 API 前端程式-使用webflux (api-frontend-webflux)
    - 該專案使用 spring boot 2.5.6，並已載入 spring-webflux 函式庫
    - 實作一個測試用端點，允許接受一個請求參數(delay)，該端點轉呼叫 API 後端測試用端點
    - 使用 Spring WebClient 實作，設定將 Connection 數上限調高 (1000)

```
@GetMapping
public Mono<?> test(@RequestParam(required = false, defaultValue = "0") int delay) {
    logger.info("Test API is invoked. (Non-Blocking)");

    return webClient.get().uri(backendEndpoint + "?delay=" + delay).retrieve().bodyToMono(String.class)
            .onErrorResume(throwable -> {
                return Mono.error(throwable);
            });
}
```

```
@Bean
public WebClient webClient() {
    ConnectionProvider connectionProvider = ConnectionProvider.builder("http").maxConnections(1000)
            .pendingAcquireMaxCount(1000)
            .pendingAcquireTimeout(Duration.ofMillis(ConnectionProvider.DEFAULT_POOL_ACQUIRE_TIMEOUT))
            .maxIdleTime(Duration.ZERO).build();

    HttpClient httpClient = HttpClient.create(connectionProvider).keepAlive(false);

    WebClient.Builder builder = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));

    return builder.build();
}
```

##

5. 第一次使用 JMeter 工具進行測試
    - 兩個前端專案分別使用阻塞與非阻塞的 Web Container 作為伺服器，限制兩者 Maximum Thread 數量，以模擬其不同的運作方式(Threads vs Event Loop
    - 設定 Tomcat
    - 設定 Netty
    - 使用 Jmeter 腳本分別進行測試

```
// Tomcat
server:
  port: 8080
  tomcat:
    threads:
      max: 4
```

```
// Netty
// JVM arguments
-Dreactor.netty.ioWorkerCount=4
```

##

6. 第二次使用 JMeter 工具進行測試 (使用 JConsole 輔助)
    - 將 Thread 限制移除，調整腳本加大測試壓力，再次進行測試
    - 使用 JConsole 觀察 JVM 資源用量情況
