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
모든 마이크로 서비스는 Spring Cloud Netflix Eureka에 등록한다.
유레카가 해주는 역할 == Service Discovery: 외부에서 마이크로서비스를 검색하는데 사용한다.
1. 유레카에 마이크로서비스를 등록한다.
2. 클라이언트는 필요한 요청정보를 Load Balancer(API Gateway)에 요청하고
3. 이 정보가 서비스 디스커버리에 전달되어 필요한 마이크로서비스에 전달해준다.