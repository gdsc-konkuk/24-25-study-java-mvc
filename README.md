# Part 2. :leaves: 만들면서 배우는 스프링 :leaves:

## @MVC 구현하기

### :mag_right: 학습목표

- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### :rocket: 필요 요구사항

- build.gradle을 참고하여 환경 설정은 스스로 찾아서 한다.
- 미션을 시작할 때, `해당 기수(앞년도만) + 자신의 영문 이름`으로 브랜치를 파고, 작업 후 각자 브랜치로 커밋, 푸시한다.(예: `24YoonByungWook`)
- `main` 브랜치는 절대 건들지 말아주세요!!
- 예시

```text
git status
- On branch main
git branch 24YoonByungWook
git checkout 24YoonByungWook
```

# 0단계 - Reflection, DI 학습 미션

+ [x] study 디렉토리 안의 test를 아래 순서대로 모두 수행한다.
+ [x] study/src/test/java/reflection 디렉토리 내부의 테스트를 수행한다.
    + [x] [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    + [x] [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    + [x] [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    + [x] [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
+ [x] study/src/test/java/servlet 디렉토리 내부의 테스트를 수행한다.

# 🚀 1단계 - @MVC 프레임워크 구현하기

## 기능 요구사항

어노테이션 기반의 MVC 프레임워크를 구현한다.

- [x] `AnnotationHandlerMappingTest`가 정상 동작한다.
- [x] `DispatcherServlet`에서 `HandlerMapping` 인터페이스를 활용하여 `AnnotationHandlerMapping`과 `ManualHandlerMapping` 둘다 처리할 수 있다.

### AnnotationHandlerMapping 구현

- [x] 특정 package 내에서 `@Controller` annotation이 달린 class를 찾는다.
- [x] controller class 내에서 `@RequestMapping` annotation이 달린 method를 찾는다.
- [x] `@RequestMapping`에서 지정한 url과 http method에 대해 `HandlerExecution`을 mapping한다.

### DispatcherServlet 구현

`ManualHandlerMapping`과 `AnnotationHandlerMapping` 둘 다 사용할 수 있어야 한다.

- [x] `Controller`와 `HandlerExecution` 둘 다를 실행할 수 있다.
- [x] `ModelAndView`를 적절하게 rendering 할 수 있다.

## 🚀 2단계 - 점진적인 리팩터링

### 기능 요구사항

> Legacy MVC와 @MVC 통합하기

interface 기반의 컨트롤러와 annotation 기반의 컨트롤러가 공존하는 상태로 정상 동작하도록 구현한다.

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 한다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 한다.

## 🚀 3단계 - JSON View 구현하기

### 기능 요구사항

화면에 대한 책임을 View가 가지게 하고, `JsonView`를 구현하여 REST API를 지원할 수 있도록 한다.

- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

#### JspView 구현

- [x] Jsp 반환을 JspView에서 처리한다.

#### JsonView 구현

- [x] model의 객체를 json으로 변환하여 response body로 응답한다.

### Legacy MVC 제거

- [ ] app module의 모든 controller를 annotation 기반으로 변경한다.
- [ ] asis 패키지의 레거시 코드를 삭제해도 정상 동작하도록 리팩터링한다.

### Advanced

- [ ] `ArgumentResolver` 구현
- [ ] `Bean` 등록에 파라미터 지원
- [ ] `@Component`를 통한 Meta Annotation Scan 확장
