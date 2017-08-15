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
