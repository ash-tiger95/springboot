# springboot
springboot with JPA, Querydsl, MSA

# Software Architecture
~2010s: Anti-Fragile (IT Culture: DevOps && IT Architecture: Cloud Native)

Anti-Fragile 특징
1. Auto Scaling: 자동 확장성
2. Microservices
3. Chaos engineering
4. Continuous deployments

# Cloud Native Architecture
1. 확장가능한 아키텍처
- 시스템의 수평적 확정에 유연
- 확장된 서버로 시스템의 부하 분산, 가용성 보장
- 시스템 또는 서비스 애플리케이션 단위의 패키지(컨테이너 기반 패키지)
- 모니터링

2. 탄력적 아키텍처
- 서비스 생성 - 통합 - 배포, 비즈니스 환경 변화에 대응 시간 단축
- 분활된 서비스 구종
- 무상태 통신 프로토콜
- 서비스의 추가와 삭제 자동으로 감지
- 변경된 서비스 요청에 따라 사용자 요청 처리(동적 처리)

3. 장애 격리(Fault isolation)
- 특정 서비스에 오류가 발생해도 다른 서비스에 영향 주지 않음

# Cloud Native Application
Cloud Native Architecture로 설계된 Application

1. Microservices
2. CI/CD
- 지속적인 통합(Continuouse Integration, CI)
  - 통합 서버, 소스 관리(SCM), 빌드 도구, 테스트 도구
  - ex) Jenkins, Team CI, Travis CI
- 지속적인 배포(CD)
  - Continuous Delivery, 지속적인 전달
  - Continuous Deployment, 지속적인 배포
  - Pipe line
- 카나리 배포와 블루그린 배포

3. DevOps
- Development + Operation + QA
- 고객의 요구사항, 변경사항을 필요할 때마다 수정할 수 있게 도와준다.

4. Container 가상화
---
# Spring Cloud Netflix Eureka
- 모든 마이크로 서비스는 Spring Cloud Netflix Eureka에 등록한다.
- 유레카가 해주는 역할 == Service Discovery: 외부에서 마이크로서비스를 검색하는데 사용한다.
1. 유레카에 마이크로서비스를 등록한다.
2. 클라이언트는 필요한 요청정보를 Load Balancer(API Gateway)에 요청하고
3. 이 정보가 서비스 디스커버리에 전달되어 필요한 마이크로서비스에 전달해준다.

# API Gateway Service
- 사용자가 설정한 라우팅 설정에 따라 엔드포인트로 클라인트 대신에 전달하는 proxy 역할
- 시스템 내부 구조를 숨기고 외부의 요청에 대해 적절한 가공을 통해 전달한다.

기능)
- 인증 및 권한 부여
- 서비스 검색 통합
- 응답 캐싱
- 정책, 회로 차단기 및 QoS 다시 시도
- 속도 제한
- 부하 분산
- 로깅, 추적, 상관관계
- 헤더, 쿼리 문자열 및 청구 변환
- IP 허용 목록에 추가

# Netflix Ribbon
- Spring Cloud에서의 MSA간 통신
- 방법1) RestTemplate
- 방법2) Feign Client
- 문제) 로드 밸런서를 어디서 구축해서 구현할지 고민


- Ribbon: Client side Load Balancer 역할
- 서비스 이름으로 호출
- Health Check
- 문제) Client side 측에 Ribbon을 장착해 사용했는데, 비동기가 안되서 잘 사용하지 않는다. (API Gateway 존재 x)
- 하지만 장점으로 외부의 서비스를 호출할 때, MSA 이름만을 가지고 호출이 가능하다.


- 최근에 Spring Cloud Ribbon은 Spring Boot 2.4에서 Maintenance 상태 (더이상 새로운 기능을 추가하지 않는다는 의미)

# Netflix Zuul
구성)
- First Service
- Second Service
- Netflix Zuul: Routing, API Gateway 역할
- Spring Cloud Zuul도 Spring Boot 2.4에서 Maintenance 상태

---
# Spring Cloud Gateway
- predicates로 요청을 하면 uri로 가겠다는 의미
- 쓰는 목적: 비동기가 가능, Zuul은 동기 방식, Spring Cloud Gateway가 Zuul을 대채한다.
1. application.yml에서 predicates, uri 설정
2. @RequestMapping 설정
---
# jar vs war
jar: springboot안에 포함된 내장 tomcat서버가 작동하면서 스프링부트를 기동
war: application 구조가 web형태로 바뀌어 내장 서버가 없고 외장 서버에 마이크로서비스를 배포해야 한다.
