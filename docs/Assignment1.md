### 1단계

``AnnotationHandlerMappingTest``

#### 테스트 케이스설명

```java
final var request = mock(HttpServletRequest.class);
final var response = mock(HttpServletResponse.class);
```

Servlet클래스를 가져옴

```java
when(request.getAttribute("id")).thenReturn("gugu");
when(request.getRequestURI()).thenReturn("/get-test");
when(request.getMethod()).thenReturn("GET");
```

`id`가 호출되면 `gugu`반환,  `URI`호출되면 `/get-test`반환, `Method`호출되면 `GET`반환 설정

```java
final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
final var modelAndView = handlerExecution.handle(request, response);
```

(기존에 이미 설정된) 핸들러를 호출해서 request에 맞는 `handlerExecution` 호출, 이를 통해 `modelAndView` 반환

```java
assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
```

테스트가 원활하게 동작하는지 확인

1. request 를 Servlet이 받고, 이를 HandlerMapping을 통해 적합한 HandlerController를 찾아 적용시킴
2. 여기서는 Key를 통해 찾고, 이를 Execution 을 통해 실행시키는 그림같음.
   `HandlerExecution`클래스 또한 수정. `ModelAndView`타입 반환임. View필드는 null로 초기화.
   id를 조회시 request에서 id에 해당하는 attribute를 찾아서, 이를 묶어 `ModelAndView`로 반환하도록수정.
3. 따라서 키를 생성하고, HandlerExecution을 생성해 Map 자료형 `handlerExecutions`에
   `handlerExecutions.put(new HandlerKey("/get-test", RequestMethod.GET), new HandlerExecution());`
4. 이후 `getHandler`에, 입력된 파라메터 토대로 HandlerKey를 생성, `return handlerExecutions.get(handlerKey);`

#### Post테스트도 비슷한 방법으로 해결

---
