# rookie-hair-stylist

이 프로젝트를 checkout하고 소스를 빌드하려면 : 
<pre><code>git clone https://github.com/mokdonjr/rookie-hair-stylist.git
cd rookie-hair-stylist
mvn clean install</code></pre>

application.properties :
<pre><code># =============================================
# - DATA SOURCES
# =============================================
spring.datasource.url=jdbc:mysql://localhost:3306/데이터베이스이름
spring.datasource.username=사용자이름
spring.datasource.password=사용자비밀번호
</pre></code>

## 프로젝트 소개

rookie-hair-style-service는 예비 헤어 디자이너 플랫폼 서비스입니다. 
경험을 원하는 헤어 디자이너와 저렴한 가격을 원하는 고객을 매칭시켜 드립니다.

1. 헤어 스타일링(커트, 펌, 염색 등)을 할 수 있지만, 실제 고객을 만날 기회가 없는 예비 헤어 디자이너 (대학생, 인턴, 수료생)가 
자신의 작업실(실습실, 미용실) 위치와 자신있는 포트폴리오를 게시한 온라인Shop을 등록할 수 있습니다.

2. 고도의 기술을 요구하지 않는 헤어 스타일을 원하는 고객은 위치와 포트폴리오를 고려하여 
원하는 예비 헤어 디자이너를 선택한 후 저렴한 가격(+보증금, 상한가)으로 선결제 예약을 합니다. 

3. 스타일링이 끝난 후, 고객은 후기를 작성해 보증금을 환급받고, 후기는 예비 헤어 디자이너의 포트폴리오로 기록됩니다.

## 개발자환경

rookie-hair-stylist 서버는 Kakao&Facebook OAuth Client로 아래 3개 외 Domain에 서비스하지 않습니다.

|  	| Domain 	| OS/System version 	| jdk version 	| mvn version 	| 실행 	| OAuth status 	|
|---------------	|------------------------------	|--------------------------	|-------------	|-------------	|-------------------------	|-------	|
| Local 	| http://localhost:8080/ 	| - 	| - 	| - 	| STS IDE 	| O 	|
| Deploy Server 	| http://13.124.59.137:8080/ 	| Ubuntu 16.04.2 LTS/64bit 	| JDK-1.8 	| ./mvnw 	| package 빌드/ nohub java 	| O 	|
| Goorm IDE 	| http://rookies.run.goorm.io/ 	| Ubuntu 14.04 LTS/64bit 	| OpenJDK-1.8 	| 3.3.9 	| mvn spring-boot:run 	| O 	|

Goorm IDE 에서 프로젝트 실행시, IDE상단의 '실행' 버튼을 누르지말고 IDE터미널에 'mvn spring-boot:run' 으로 실행하십시오.

## 테스트

위 개발환경에 맞게 실행하기 전에 DDL설정을 확인해 주세요. (권고 : update)

application.properties :
<pre><code># =============================================
# - JPA / HIBERNATE
# =============================================
spring.jpa.show-sql=false
# ddl-auto should be 'validate' in deploying time
spring.jpa.hibernate.ddl-auto=update
</pre></code>

| rookies 계정(Facebook) | rookies.yapp@gmail.com |
|:----------------------:|:----------------------:|
|        권한 관리       | rookies.yapp@gmail.com |
