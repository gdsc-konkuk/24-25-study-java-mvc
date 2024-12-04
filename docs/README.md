# Reflection API
## Java.lang.Class

코드 상에서 호출 로직을 통해 클래스 정보를 얻어와야 할 때 사용하는 객체

자신이 속한 클래스의 모든 멤버 정보를 담아 런타임 환경에서 동적으로 지정된 클래스나 인터페이스 정보를 가져온다.

이는 자바의 실행 방식과 연관이 있는데 자바의 모든 클래스와 인터페이스는 실행 시 바이트코드(.class)로 컴파일된다. java.lang.Class는 전역 생성자가 존재하지 않는 대신에 클래스가 로드되거나 defineClass 메서드가 클래스 로더에 의해 호출될 때 JVM에 의해 자동적으로 생성된다.

```java
void printClassName(Object obj){
	System.out.println("The class of " + obj + " is " + obj.getClass().getName());
}
```

### Class 객체를 얻는 방법

1. Object.getClass()

   모든 클래스의 최상위 클래스인 Object 클래스의 getClass() 메서드

   해당 클래스가 인스턴스화 된 상태여야 한다.

2. .class 리터럴

   인스턴스가 존재하지 않을 경우 사용한다.

3. Class.forName()

   동적 로딩

   JVM에서 실행에 필요한 모든 클래스 파일을 메모리에 올려놓지 않고, 필요한 시점에 동적으로 메모리에 올리는 기술이다.

   컴파일 타임에 체크 할 수 없기 때문에 클래스 유무가 확인되지 않아 예외 처리가 반드시 필요하다.


### Class의 메서드

- 클래스 정보
    - getSimpleName
    - getPackage
    - getName
    - toString
    - toGenricString
- 클래스 구성
    - getFields
    - getMethods
    - getInterfaces
    - getSuperclass

## Reflection API

구체적인 클래스 타입을 알지 못한 상태로 그 클래스의 정보에 접근하게 해주는 자바 기법

클래스의 파일의 위치나 이름만 있다면 해당 클래스의 정보를 얻어내고, 객체를 생성하는 것이 가능해진다.

### Reflection 사용법

- 동적으로 생성자 가져와 초기화하기

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forNmae("Person");
    	
    	Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
    	
    	Person person1 = constructor.newInstance("홍길동", 55);
    	person1.getField();
    ```

    - getConstructor: 인자로 생성자의 매개변수 타입을 받아(바인딩) 해당하는 생성자를 리턴한다.
- 동적으로 메서드 가져와 실행하기

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forName("Person");
    	
    	Method sum = personClass.getMethod("sum", int.class, int.class);
    	int result = (int)sum.invoker(new Person(), 10, 20);
    	System.out.println("result = "+result);
    	
    	Method staticSum = personClass.getMethod("staticSum", int.class, int.class);
    	int staticResult = (int)staticSum.invoke(null, 100, 200);
    	
    	Method privateSum = personClass.getDeclaredMethod("privateSum", int.class, int.class);
    	privateSum.setAccesible(true);
    	int privateResult = (int) privateSum.invoke(new Person(), 1000, 2000);
    	System.out.println("privateResult = "+privateResult);
    }
    ```

    - getMethod: 메서드 이름과 매개변수 타입을 받아 해당 메서드를 리턴한다. (public only)
    - getDeclareMethod: private 메서드를 가져온다.
        - setAccessible: private이기 때문에 외부에서 access 할 수 있도록 허용해주어야한다.
    - invoke: 가져온 메서드를 동적으로 실행시켜주는 메서드

      첫번째 파라미터로 메서드를 실행할 객체를 받고, 두번째 파라미터부터는 해당 메서드의 파라미터 값을 전달받는다.

- 동적으로 필드 가져와 조작하기

  필드 값을 원하는대로 변경할 수 있고 필드는 클래스가 인스턴스가 되어야 Heap 메모리에 적재되므로 인스턴스가 필요하다(static의 경우 이미 적재되어 있으므로 인스턴스가 필요하지 않다.).

    ```java
    public static void main(String[] args) throws Exception{
    	Class<Person> personClass = (Class<Person>) Class.forName("Person");
    	
    	Field height_field = personClass.getField("height");
    	height_field.set(null, 200);
    	System.out.println(height_field.get(null));
    }
    ```

    ```java
    public static void main(String[] args) throws Exception{
    	Person person = new Person("홍길동", 55);
    	
    	Class<Person> personClass = (Class<Person>)Class.forName("Person");
    	
    	Field name_field = personClass.getField("name");
    	
    	Field age_field = personClass.getDeclaredField("age");
    	age_field.setAccessible(true);
    	
    	name_field.set(person, "임꺽정");
    	age_field.set(person, 88);
    	
    	System.out.println(name_field.get(person));
    	System.out.println(age_field.get(person));
    }
    ```


### Reflection의 문제점

런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없어 모든 예외 처리를 해주어야한다는 단점이 있다.

# Test

## Junit3TestRunner, Junit4TestRunner

- Class.getConstructor를 사용해 클래스의 생성자를 가져옴
- Constructor.newInstance: 해당 클래스의 생성자를 사용해 객체를 생성
    - class.newInstance: 똑같이 객체를 생성

      매개변수가 없는 생성자만 호출 가능

      생성자가 던지는 모든 예외를 던짐

      public 생성자만 사용 가능


    Constructor.newInstance의 경우
    
    매개변수에 상관없이 생성자를 호출가능
    
    생성자가 던지는 예외를 InvocationTargetException으로 래핑
    
    private 생성자도 사용 가능 
    
    하기 때문에 Constructor의 newInstance를 주로 더 사용한다.  

- Class.getMethods를 사용해 메서드를 가져와 “test”로 시작하는 메서드를 invoke를 통해 생성된 객체에 대해 실행
- Method의 isAnnotationPresent를 통해 특정 어노테이션을 사용한 메서드를 실행

## ReflectionTest, ReflectionsTest

### Java.lang.Class

### 메서드

- getSimpleName: 클래스의 이름만 반환
- getName: 패키지명을 포함한 클래스의 이름을 반환
- GenCanonicalName: import문에 사용하는 이름을 반환

### Java.reflections.Reflections

- Reflection 객체를 생성하면서 매개변수로 패키지명을 넣어 해당 패키지에 존재하는 모든 클래스를 탐색해 읽어들일 수 있다.
    - getTypesAnnotatedWith: 특정 어노테이션을 가진 모든 클래스 탐색 가능

## AnnotationHandlerMapping

### Mockito

실제 객체를 모방한 Mock 객체 생성 가능

- when().then()

  when 안에 정해진 mock 객체의 메서드가 실행될 때 리턴하는 값을 지정해 줄 수 있다.


### AnnotationHandlerMapping initialize

- Reflections 객체를 사용해 basePackage에 Controller 어노테이션이 붙은  클래스를 읽어오기
- 클래스의 메서드에 RequestMapping 어노테이션이 붙은 메서드를 가져와 handler를 매핑해주어야 함

  클래스별로 메서드를 모두 읽어와 확인

- 해당하는 메서드에 대해 requestMapping의 값과 메서드를 가져와 handlerkey를 생성하고 새로운 HandlerExecution을 생성
- getHandler 메서드에서는 해당하는 Handlerkey에 대한 handler가 등록되어 있는지 확인하고 해당하는 handler 객체를 반환

# 스프링 MVC 프레임워크 동작 방식

예제 코드를 실행하기 위해 스프링 MVC 설정을 하고 컨트롤러와 뷰 생성을 위한 JSP 코드를 작성하기

## 1. 스프링 MVC 핵심 구성 요소

DispatcherServlet은 모든 연결을 담당하며 웹 브라우저로부터 들어온 요청을 처리하기 위한 컨트롤러 객체를 검색하는 역할을 수행하는데 직접 컨트롤러를 검색하지 않고 HandlerMapping이라는 빈 객체에서 컨트롤러 검색을 요청

HandlerMapping은 클라이언트의 요청 경로를 이용해 이를 처리할 컨트롤러 빈 객체를 DispatcherServlet에 전달

DispatcherServlet은 HandlerMapping이 찾아준 컨트롤러 객체를 처리할 수 있는 HandlerAdapter 빈에게 요청 처리를 위임하고 HandlerAdapter는 컨트롤러의 알맞은 메서드를 호출해 요청을 처리하고 그 결과를 다시 DispatcherServlet에 리턴

HandlerAdater로부터 컨트롤러의 요청 처리 결과를 ModelAndView로 받으면 DispatcherServlet은 결과를 보여줄 뷰를 찾기 위해 ViewResolver 빈 객체를 사용

ModelAndView는 컨트롤러가 리턴할 뷰 이름을 담고 있고, ViewResolver는 이 뷰 이름에 해당하는 View 객체를 찾거나 생성해 리턴

ViewResolver는 응답을 생성하기 위해 JSP를 사용하고 매번 새로운 View 객체를 생성해서 DispatherServlet에 리턴

DispatcherServlet은 ViewResolver가 리턴한 View 객체에게 응답 결과 생성을 요청하고 View 객체는 JSP를 실행해 웹 브라우저에 전송할 응답 결과 생성

### 컨트롤러와 핸들러

DispatcherServlet 입장에서 클라이언트 요청을 처리하는 객체의 타입이 반드시 @Controller를 적용한 클래스일 필요는 없음

핸들러 객체의 실제 타입에 상관없이 실행 결과를 ModelAndView라는 타입으로 받기만 한다면 상광 없음

## 2. DispatcherServlet과 스프링 컨테이너

DispatcherServlet이 사용하는 설정 파일에 반드시 HandlerMapping, HandlerAdapter, 컨트롤러 빈, ViewResolver에 대한 정의가 포함되어 있어야 함

## 3. @Controller를 위한 HandlerMapping과 HandlerAdapter

RequestMappingHandlerAdapter는 컨트롤러 메서드 결과 값이 String 타입이면 값을 뷰 이름으로 갖는 ModelAndView 객체를 생성해서 리턴

## 4. WebMvcConfigurer 인터페이스와 설정

@EnableWebMvc 애노테이션은 @Contoroller 애노테이션을 붙인 컨트롤러를 위한 설정을 생성하고 WebMvcConfigurer의 빈을 이용해 MVC 설정을 추가로 생산

```java
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer{
	
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer){
		configurer.enable();
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry){
		registry.jsp("/WEB-INF/view/", ".jsp");
	}
}
```

## 5. JSP를 위한 ViewResolver

WebMvcConfigurer 인터페이스에 저의된 configureViewResolver() 메서드는 ViewResolverRegistry 타입의 registry 파라미터를 갖는데 ViewResolverRegistry#jsp() 메서드를 통해 JSP를 위한 ViewResolver를 설정 가능

```java
@Bean
public ViewResolver viewResolver(){
	InternalResourceViewResolver vr = new InternalResourceViewResolver();
	vr.setPerfix("/WEB-INF/view/");
	vr.setSuffix(".jsp");
	return vr;
}
```

위에서 본 것처럼 컨트롤러의 실행 결과를 받은 DispatcherServlet은 ViewResolver에게 뷰 이름에 해당하는 View 객체를 요청

InternalResourceViewResolver는 “perfix + 뷰 이름 + suffix”에 해당하는 경로를 뷰 코드로 사용하는 InternalResourceView 타입의 View 객체를 리턴

DispatcherServlet은 컨트롤러의 실행 결과를 HandlerAdapter를 통해 ModelAndView 형태로 받는데 Model에 담긴 값은 View 객체에 Map 형식으로 전달

InternalResourceView는 Map 객체에 담겨 있는 키 값을 request.stAttribute()를 이용해 request의 속성에 저장하고 해당 경로의 JSP를 실행

→ 컨트롤러에 저장한 Model 속성은 request 객체 속성으로 JSP에 전달되기 때문에 JSP는 다음과 같이 모델에 지정한 속성 이름을 사용해 값을 사용 가능

## 6. 디폴트 핸들러와 HandlerMapping의 우선순위

web.xml 설정에 준 DispatchServlet에 대한 매핑 경로가 ‘/’일 경우 .jsp로 끝나는 요청을 제외한 모든 요청을 DispatchServlet이 처리

HandlerMapping은 @Controller 애노테이션을 적용한 빈 객체가 처리할 수 있는 요청 경로만 대응 가능

→ 다른 경로의 요청을 처리하기 위해 WebMvcConfigurer의 configureDefaultServletHandling() 메서드를 사용

- configureDefaultServletHandling()

  DefaultServletHandlerConfigurer#enable()은 두개의 빈 객체를 추가

    - DefaultServletHttpRequestHandler: 클라이언트의 모든 요청을 WAS가 제공하는 디폴트 서블릿에 전달
    - SimplerUrlHandlerMapping: 모든 경로를 DefaultServletHttp RequestHandler를 이용해서 처리하도록 설정

  RequestMappingHandlerMapping의 적용 우선순위가 더 높기 때문에 먼저 처리할 핸들러를 검색하고 해당 컨트롤러가 존재하지 않을 경우 DispatcherServlet은 DefaultServletHttpRequestHandler에 처리를 요청


## 7. 직접 설정 예

@EnableWebMvc 애노테이션을 사용하지 않고 스프링 MVC를 사용하는 것도 가능

# MVC 1: 요청 매핑, 커맨드 객체, 리다이렉트, 폼 태그, 모델

## 1. 프로젝트 준비

## 2. 요청 매핑 애노테이션을 이용한 경로 매핑

웹 어플리케이션의 개발

- 특정 요청 URL을 처리할 코드 → @Controller의 컨트롤러 클래스를 이용해 구현
- 처리 결과를 HTML과 같은 형식으로 응답하는 코드

요청 매핑 애노테이션을 사용해 메서드가 처리할 요청 경로를 지정

@RequestMapping, @GetMapping, @PostMapping

요청 매핑 애노테이션을 적용한 메서드를 두 개 이상 정의 가능

```java
@Controller 
public class RegistController{
	
	@RequestMapping("/register/step1")
	public String handleStep1(){
		return "register/step";
	}
	
	@RequestMapping("/register/step02")
	public String handlerStep2(){
		...
	}
	
	@RequestMapping("/register/step03")
	public String handleStep3(){
		...
	}
}
```

대신에

```java
@Controller 
@RequestMapping("/register")
public class RegistController{
	
	@RequestMapping("/step1")
	public String handleStep1(){
		return "register/step";
	}
	
	@RequestMapping("/step02")
	public String handlerStep2(){
		...
	}
	
	@RequestMapping("/step03")
	public String handleStep3(){
		...
	}
}
```

## 3. GET과 POST 구분: @GetMapping, @PostMapping

스프링 MVC는 별도 설정이 없으면 GET과 POST 방식에 상관없이 @RequestMapping에 지정한 경로와 일치하는 요청을 처리

POST/GET 방식의 요청만 처리하고 싶을 경우 @PostMapping/@GetMapping 사용 가능

@PutMapping, @DeleteMapping, @PatchMapping 등도 존재

## 4. 요청 파라미터 접근

약관 동의 화면을 생성하는 코드를 볼 때 약관에 동의할 경우 값이 true인 요청 파라미터를 POST 방식으로 지정

- 컨트롤러 메서드에서 요청 파라미터를 사용하는 방법
    - HttpServletRequest를 직접 이용하는 방법

      메서드의 파라미터로 HttpServletRequest 타입을 사용하고 HttpServletRequest의 getParameter() 메서드를 사용해 파라미터의 값을 구하기

    - @RequestParam 애노테이션 사용
        - String value: HTTP 요청 파라미터의 이름 지정
        - boolean required: 필수 여부 지정, default true
        - String defaultValue: 요청 파라미터가 없을 때 사용할 문자열 값

## 5. 리다이렉트 처리

handleStep2() 메서드는 직접 주소를 입력해 접근하는 GET 방식의 요청은 처리하지 않음

잘못된 전송 방식으로 요청이 왔을 경우 에러 화면보다 알맞은 경로로 리다이렉트가 필요

- “redirect:경로”를 뷰 이름으로 리턴

  @RequestMapping, @GetMapping 등 요청 매핑 관련 애노테이션을 적용한 메서드가 “redirect:”로 시작하는 경로를 리턴할 경우 나머지 경로를 이용해 리다이렉트할 경로를 구함

  뒤 문자열이 “/”로 시작할 경우 웹 어플리케이션을 기준으로 이동 경로 생성

  “/”로 시작하지 않을 경우 현재 경로를 기준으로 상대 경로를 사용

  완전한 url로 사용도 가능


## 6. 커맨드 객체를 이용해서 요청 파라미터 사용하기

폼 전송 요청을 처리하는 컨트롤러 코드는 각 파라미터의 값을 구하기 위해 HttpServletRequest#getParameter 메서드를 사용

파라미터가 매우 많을 경우를 위해 스프링은 요청 파라미터의 값을 커맨드(command) 객체에 담아주는 기능을 제공

→ 요청 파라미터의 값을 전달받을 수 있는 세터 메서드를 포함하는 객체를 커맨드 객체로 사용

커맨드 객체는 다음과 같이 요청 매핑 애노테이션이 적용된 메서드의 파라미터에 위치

RegisterRequest 클래스에 모든 세터 메서드가 존재하고 스프링은 이 메서드를 사용해 요청 파라미터의 값을 커맨드 객체에 복사한 뒤 RegisterRequest 클래스 타입의 객체로 전달

## 7. 뷰 JSP 코드에서 커맨드 객체 사용하기

커맨드 객체를 사용해 정보를 표시하기

- 스프링 MVC는 커맨드 객체의 (첫 글자를 소문자로 바꾼) 클래스 이름과 동일한 속성 이름을 사용해 커맨드 객체를 뷰에 전달

## 8. @ModelAttribute 애노테이션으로 커맨드 객체 속성 이름 변경

```java
import org.springframework.web.bind.annotation.ModelAttribute;

@PostMapping("/register/step3")
public String handleStep3(@ModelAttribute("formData") RegisterRequest regReq){
	...
}
```

@ModelAttribute 어노테이션을 사용해 모델에서 사용할 속성 이름을 값으로 대체 가능

## 9. 커맨드 객체와 스프링 폼 연동

스프링 MVC의 커스텀 태그를 사용해 좀 더 간단한 커맨드 객체의 값을 출력 가능

- <form:form> 태그의 속성
    - action: <form> 태그의 action 속성과 동일한 값을 사용
    - modelAttribute: 커맨드 객체의 속성 이름을 지정, default command
- <form:input>: <input> 태그 생성

  path로 지정한 커맨드 객체의 프로퍼티를 <input> 태그의 value 속성값으로 사용

  태그를 사용하려면 커맨드 객체가 존재해야 함

  이 예제에선 step2.jsp에서 <form:form> 태그를 사용하기 때문에 step1.jsp에서 step2.jsp로 넘어오는 단계에서 이름이 registerRequest인 객체를 모델에 넣어줌

- <form:password>

  password 타입의 <input> 태그를 생성하므로 value의 속성값을 빈 문자열로 설정


## 10. 컨트롤러 구현 없는 경로 매핑

WebMvcConfigurer 인터페이스의 addViewControllers() 메서드를 사용하면 컨트롤러 구현없이 간단한 코드로 요청 경로와 뷰 이름을 연결 가능

```java
@Override
public void addViewControllers(ViewControllerRegistry registry){
	registry.addViewController("/main").setViewName("main");
}
```

## 11. 주요 에러 발생 상황

### 요청 매핑 애노테이션과 관련된 주요 익셉션

- 404 에러: 요청 경로를 처리할 컨트롤러가 존재하지 않거나 MvcConfigurer를 이용한 설정이 없을 경우 발생
    - 요청 경로가 올바른지
    - 컨트롤러에 설정한 경로가 올바른지
    - 컨트롤러 클래스를 빈으로 등록했는지
    - 컨트롤러 클래스에 @Controller 애노테이션을 적용했는지

### @RequestParam이나 커맨드 객체와 관련된 주요 익셉션

- @RequestParam 애노테이션을 필수로 설정하고 기본값을 지정하지 않았을 경우

  파라미터가 존재하지 않는다는 익셉션 발생 가능

- 요청 파라미터의 값을 @RequestParam이 적용된 파라미터의 타입으로 변환할 수 없는 경우에도 에러 발생

> **Logback으로 자세한 에러 로그 출력하기**
>
>
> pom.xml에 Logback 관련 의존을 추가
>
> ```xml
> <dependency>
> 	<groupId>org.slf4j</groupId>
> 	<artifactId>slf4j-api</artifactId>
> 	<version>1.7.25</version>
> </dependency>
> 
> <dependency>
> 	<groupId>ch.qos.logback</groupId>
> 	<artifactId>logback-classic</artifactId>
> 	<version>1.2.3</version>
> </dependency>
> ```
>
> src/main/resourcs 폴더에 logback.xml 파일을 생성하고 src/main/resources 폴더에 logback.xml 파일 생성
>

## 12. 커맨드 객체: 중첩, 콜렉션 프로퍼티

- Respondent 클래스:응답자 정보를 담음
- AnsweredData 클래스:  설문 항목에 대한 답변과 응답자 정보를 함께 담음
    - 리스트 타입의 프로퍼티 존재
    - 중첩 프로퍼티를 가짐

스프링 MVC는 커맨드 객체가 리스트 타입의 프로퍼티를 가졌거나 중첩 프로퍼티를 가지더라도 요청 파라미터의 값을 알맞게 커맨드 객체에 설정

- “프로퍼티이름[인덱스]” 형식 → List 타입 프로퍼티의 값 목록으로 처리
- “프로퍼티이름.프로퍼티이름” → 중첩 프로퍼티 값을 처리

## 13. Model을 통해 컨트롤러에서 뷰에 데이터 전달하기

컨트롤러는 뷰가 응담 화면을 구성하는데 필요한 데이터를 생성해 전달 → Model을 사용

뷰에 데이터를 전달해야 하는 컨트롤러는 아래의 역할을 수행해야 함

- 요청 매핑 애노테이션이 적용된 메서드의 파라미터로 Model을 추가
- Model 파라미터의 addAttribute() 메서드로 뷰에서 사용할 데이터 전달

  속성이름을 파라미터로 가짐


### ModelAndView를 통한 뷰 선택과 모델 전달

ModelAndView를 사용하면 뷰에 전달할 데이터 설정과 결과를 보여줄 뷰 이름 리턴을 한번에 처리할 수 있음

뷰에 전달할 모델 데이터를 addObject 메서드로 추가하고 뷰 이름은 setViewName() 메서드를 통해 지정

### GET 방식과 POST 방식에 동일 이름 커맨드 객체 사용하기

<form:form> 태그를 위해선 커맨드 객체가 필요

최초에 폼을 보여주는 요청에 대해 <form:form> 태그 사용을 위해선 폼 표시 요펑이 왔을 때 커맨드 객체를 생성해 모델에 저장 필요

- Model에 직접 객체 추가
- 커맨드 객체를 파라미터로 추가

  이름을 명시적으로 지정하기 위해선 @ModelAttribute 애노테이션을 사용


## 14. 주요 폼 태그 설명

HTML 폼과 커맨드 객체를 연동하기 위한 JSP 태그 라이브러리

### <form> 태그를 위한 커스텀 태그: <form:form>

- Method와 Action 속성을 지정하지 않을 경우 method=”POST”, actions=현재요청URL로 설정
- id 속성값으론 입력 폼의 값을 저장하는 커맨드 객체의 이름을 사용

  기본값인 “command”가 아닐 경우 modelAttribute 속성값으로 커맨드 객체의 이름을 설정

- action - 폼 데이터를 전송할 URL
- enctype - 전송될 데이터의 인코딩 타입
- method - 전송 방식
- <form:form> 태그의 몸체

  <input>, <select> 등 입력 폼을 출력하는데 필요한 HTML 태그 사용 가능


### <input> 관련 커스텀 태그: <form:input>, <form:password>, <form:hidden>

- <form:input>: path 속성을 사용해 연결할 커맨드 객체의 프로퍼티를 지정

  id 속성과 name 속성값은 프로퍼티의 이름으로 설정, value는 <form:input> 커스텀 태그의 path 속성으로 지정한 커맨드 객체의 프로퍼티 타입 출력

- <form:password>: password 타입의 <input> 태그 생성
- <form:hidden>: hidden 타입의 <input> 태그 생성

### <select> 관련 커스텀 태그: <form:select>, <form:options>, <form:option>

- <select>: 선택 옵션 제공

  옵션 정보는 보통 컨트롤러에서 생성해 뷰에 전달

  뷰에 전달한 모델 객체를 갖고 간단하게 <selec>와 <option> 태그 생성 가능

- <form:options>: <form:select> 태그에 중첩해 사용

  item 속성에 값 목록으로 사용할 모델 이름을 설정

  주로 컬렉션에 없는 값을 <option> 태그로 추가할 때 사용

- <form:option>: <option> 태그를 직접 지정할 때 사용

  value 속성을 통해 <option> 태그의 valu 속성값을 지정하고 몸체 내용을 지정하지 않으면 value 속성에 지정한 값을 태그로 사용

  label 속성을 사용하면 그 값을 텍스트로 사용

  사용한 컬렉션 객체가 String이 아닐 경우, itemValue와 itemLabel 속성을 사용 → 사용할 객체의 프로퍼티 지정


→ 커맨드 객체의 프로퍼티 값과 일치하는 값을 갖는 <option>을 자동으로 선택

### 체크박스 관련 커스텀 태그: <form:checkboxes>, <form:checkbox>

한 개 이상의 값을 커맨드 객체의 특정 프로퍼티에 저장하고 싶을 때 List와 같은 타입을 사용하는데 HTML 입력 폼에서는 checkbox 타입의 <input> 태그를 사용

- <form:checkboxes>: items 속성을 이용해 값으로 사용할 콜렉션을 지정

  path 속성으로는 커맨드 객체의 프로퍼티 지정

    <intput> 태그의 value 속성에 사용한 값이 체크박스를 위한 텍스트로 사용 

  만약 String이 아닐 경우 itemValue 속성과 itemLabel 속성을 이용해 값과 텍스트로 사용할 객체의 프로퍼티 지정 가능

- <form:checkbox>: 한개의 checkbox 타입의 <input> 태그를 한 개 생성할 때 사용

  연결되는 프로퍼티 값이 true이면 “checked” 속성을 설정

  생성되는 <input> 태그의 value 속성값으론 “true”를 사용

  프로퍼티가 배열이나 Collection일 경우 해당 컬렉션 값이 포함되어 있다면 “checked”속성을 설정


### 라디오버튼 관련 커스텀 태그: <form:radiobuttons>, <form:radiobutton>

- <form:radiobuttons>: items 속성에 값으로 사용할 컬렉션을 전달하고 path 속성에 커맨드 객체의 프로퍼티 지정
- <form:radiobutton>: 한개의 radion 타입 <input> 태그를 생성할 때 사용

### <textarea> 태그를 위한 커스텀 태그: <form:textarea>

게시글 내용과 같이 여러 줄을 입력받아야 하는 경우 사용

커맨드 객체와 관련된 <testarea> 태그 생성 가능

### CSS 및 HTML 태그와 관련된 공통 속성

- CSS 관련
    - cssClass: HTML의 class
    - cssErrorClass: 폼 검증 에러 발생 시 HTML의 class
    - cssStyle: HTML의 style