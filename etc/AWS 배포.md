​	

# AWS 배포 방법 정리

1. ssh 접속

   1. I3A609T.pem 가 있는 곳에서
   2. ssh -i I3A609T.pem ubuntu@i3a609.p.ssafy.io

2. 웹 서버 설치 (nginx) -> 엔진엑스
   1. sudo apt update
   2. sudo apt install nginx

3. 웹 서버 설치 확인 (팀별 dns)
   1. i3a609.p.ssafy.io 로 접속

      ​          	

### Gitlab 코드 clone

1. git clone

2. develop branch 확인

3. git 을 기준으로 배포해야하는 이유

   1. 마스터 브랜치 기준으로 서비스를 관리하기위해

      로컬로 파일 생성 후 배포하는 작업은 좋지 못하

node.js설치

1. node js 설치

   1. curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
   2. sudo apt install nodejs
      

2. npm 설치

   1. npm install
   2. npm run build
   3. ls dist

   > front assets 만들

   현재 경로  /home/ubuntu/s03p12a609/frontend/dist

   ###### url 초기화면  변경하기

   1. sudo vi /etc/nginx/sites-available/default
   2. root 를 ome/ubuntu/s03p12a609/frontend/dist 로 변경
   3. sudo systemctl restart nginx

3. node npm 설치 확인

   ​      	

### AWS 배포

```bash
#https://linuxize.com/post/how-to-install-node-js-on-ubuntu-18.04/
#위 사이트 들어가서 
$ curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
$ sudo apt install nodejs
$ node --version
$ npm install
$ npm run build
$ ls dist
$ cd dist
$ ls
$ pwd #본인이있는 dist폴더의 절대적인 경로가 나온다. 이를 저장해둬야 한다.
# Web Server 설정을 바꿀거다
$ sudo vi /etc/nginx/sites-available/default
# root까지 가 y를 두번 눌러 그다음 p를 눌러 그면 똑같은게 복사가 돼
# i를 누르면 뭘 쓸 수가 있어 #을 걸어서 스페이스를 쳐주고 아까 전에 복사해둔 절대 경로를 넣어줘 
# esc를 누르면 insert모드가 나가짐 그 후 :wq하면 밖으로 나와짐
# 확인을 하는 과정에서 cat 똑같은 경로 하면 설정 볼 수 있음 
# nginx를 재시작을 해야해
$ sudo systemctl restart nginx 
```

```bash
$ sudo apt update
$ sudo apt install nginx
```

### Backend 확인

```bash
$ cd ../../backend/
$ sudo apt install maven
$ mvn clean package #하나의 파일로 묶는 형태
$ ls
$ ls target #jar represented when build successed
$ java --version
$ java -jar target/webblog snapshot뭐시기 #에러 뜰걸 DB가 없거든
$ docker -v #도커 없대 저거 설치하래
$ sudo apt install docker.io
$ docker -v #도커 버전이 제대로 뜰 것이다
$ sudo docker run --name mariadb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mariadb mariadb #패스워드는 팀내에서 바꾸길 추천한다 mariadb를 쉬운걸로 해놓거나 다 동일한걸로 하면 외부에서 해킹해가니 조심하도록
$ sudo docker exec -it mariadb mysql -u root -p
$ cd main/
$ cd resources/
$ cat application.properties
#다시 jar실행

#마리아 db가서 주소 넣고 database새로 만들어
$ vi src/main/resources/application.properties
#코드 설정 바꾸면 다시 빌드 해야해
$ mvn clean package
$ java -jar target/SNAPSHOT.jar
#여기까지가 서버를 띄우는거!


```

```bash
$ sudo npm install pm2 -g
$ pm2 start app.js
#app.json을 만들어
{
  "apps": [{
    "name": "War",//이름 맘대로 바까
    "cwd": ".",
    "args": [
      "-jar",
      "/home/ubuntu/cicd/backend/target/webBlog-0.0.1-SNAPSHOT.jar" //여기에 war 파일 위치
    ],
    "env": {
    },
    "script": "java",
    "node_args": [],
    "log_date_format": "YYYY-MM-DD HH:mm Z",
    "exec_interpreter": "none",
    "exec_mode": "fork"
  }]
}
java -jar sdfsdf.jar
$ pwd #backend폴더에서 타겟 폴더가 생기는데 요 안에 jar가 있다.그면 나의 target파일 경로 복사해서 jar뒤에 붙여 "args"value로 "/home/ubuntu/cicd/backend/target/webGlog-0.0.1-SNAPSHOT.jar
$ pm2 start app.json
$ pm2 list
$ pm2 monit
$ pm2 logs backend #default로 15까지 보인다
```

### 8080이랑 api가 같은 위치를 보도록 한번 해보겠다!

```bash
#nginx 리버스 프록시
$ sudo vi /etc/nginx/sites-available/default
#들어가서 location에 루트가 아니라 location에 api
#5를 누르고 y를 눌러 shift p를 누르면 5줄을 복사해서 5줄을 붙여놓은 거다
#i를 누르면 insert 모드 
#두번째 location뒤에 /api/가 되도록
#esc 몇번 누르고 나서 first commit부분 가서 d3d를 하면 3개가 지워질겨
#server_name지정
server_name 우리주소.ssafy.io;
location /api/ {
                proxy_pass http://127.0.0.1:8080;
                proxy_set_header Host $http_host;
        }
#:wq해서 나가
#nginx 설정을 바꿨기 때문에 다시 시작을 해야 정상적으로 동작을 한다.
$ sudo systemctl restart nginx
```

### 수정사항을 가했을 때 어떻게 배포를 할 수 있는지

```bash
$ git branch
$ git fetch --all
$ git status
#너의 브랜치는 origin이랑 한개 뒤에 있어
$ git log
$ git pull 
$ cd frontend
$ npm run build #production용 빌드를 해야 배포를 할 수 있다는 것을 까먹지 말아라
```

### backend 타임

```bash
$ git branch feature/signup
---파일 수정---커밋하고 푸쉬해 원래 하던것처럼 merge도 원래 하던것처럼 해 일단 
$ git fetch --all
$ mvn clean package #에러가 뜰거야 clean를 할 수 없기때문에 
$ pm2 del backend
#pm2 list에 아무것도 없을때 mvn clean package를 해
$ pm2 start app.json
$ pm2 restart backend
# backend는 frontend랑 다르게 build만 한다고 될게 아니라 restart를 해줘야돼
```

