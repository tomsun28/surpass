"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[2834],{4137:function(e,t,n){n.d(t,{Zo:function(){return c},kt:function(){return m}});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var l=a.createContext({}),p=function(e){var t=a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},c=function(e){var t=p(e.components);return a.createElement(l.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},u=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,o=e.originalType,l=e.parentName,c=s(e,["components","mdxType","originalType","parentName"]),u=p(n),m=r,h=u["".concat(l,".").concat(m)]||u[m]||d[m]||o;return n?a.createElement(h,i(i({ref:t},c),{},{components:n})):a.createElement(h,i({ref:t},c))}));function m(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var o=n.length,i=new Array(o);i[0]=u;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s.mdxType="string"==typeof e?e:r,i[1]=s;for(var p=2;p<o;p++)i[p]=n[p];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},7944:function(e,t,n){n.r(t),n.d(t,{frontMatter:function(){return s},contentTitle:function(){return l},metadata:function(){return p},toc:function(){return c},default:function(){return u}});var a=n(7462),r=n(3366),o=(n(7294),n(4137)),i=["components"],s={id:"docker-deploy",title:"Install HertzBeat via Docker",sidebar_label:"Install via Docker"},l=void 0,p={unversionedId:"start/docker-deploy",id:"start/docker-deploy",title:"Install HertzBeat via Docker",description:"Recommend to use docker deploy HertzBeat",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/start/docker-deploy.md",sourceDirName:"start",slug:"/start/docker-deploy",permalink:"/en/docs/start/docker-deploy",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/start/docker-deploy.md",tags:[],version:"current",frontMatter:{id:"docker-deploy",title:"Install HertzBeat via Docker",sidebar_label:"Install via Docker"},sidebar:"docs",previous:{title:"TDengine Init(optional)",permalink:"/en/docs/start/tdengine-init"},next:{title:"Install via Package",permalink:"/en/docs/start/package-deploy"}},c=[{value:"Docker Deployment common issues",id:"docker-deployment-common-issues",children:[],level:3}],d={toc:c};function u(e){var t=e.components,n=(0,r.Z)(e,i);return(0,o.kt)("wrapper",(0,a.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},"Recommend to use docker deploy HertzBeat")),(0,o.kt)("p",null,"video tutorial of installation and deployment: ",(0,o.kt)("a",{parentName:"p",href:"https://www.bilibili.com/video/BV1GY41177YL"},"HertzBeat installation and deployment-BiliBili"),"  "),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Download and install the Docker environment",(0,o.kt)("br",{parentName:"p"}),"\n","Docker tools download refer to ",(0,o.kt)("a",{parentName:"p",href:"https://docs.docker.com/get-docker/"},"Docker official document"),"\u3002\nAfter the installation you can check if the Docker version normally output at the terminal."),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker -v\nDocker version 20.10.12, build e91ed57\n"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"pull HertzBeat Docker mirror",(0,o.kt)("br",{parentName:"p"}),"\n","you can look up the mirror version TAG in ",(0,o.kt)("a",{parentName:"p",href:"https://hub.docker.com/r/tancloud/hertzbeat/tags"},"official mirror repository"),"     "),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker pull tancloud/hertzbeat   \n"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Configure HertzBeat's configuration file(optional)",(0,o.kt)("br",{parentName:"p"}),"\n","Create application.yml in the host directory\uff0ceg:/opt/application.yml",(0,o.kt)("br",{parentName:"p"}),"\n","The configuration file content refer to project repository",(0,o.kt)("a",{parentName:"p",href:"https://gitee.com/dromara/hertzbeat/raw/master/script/application.yml"},"/script/application.yml"),"\uff0cmodify service parameters, IP port account password in ",(0,o.kt)("inlineCode",{parentName:"p"},"td-engine"),(0,o.kt)("br",{parentName:"p"}),"\n","Note\u26a0\ufe0f\uff08If use email to alert, please replace the mail server parameter. If use MYSQL data source, replace the datasource parameters inside  refer to",(0,o.kt)("a",{parentName:"p",href:"mysql-init"},"H2 database switch to MYSQL"),"\uff09",(0,o.kt)("br",{parentName:"p"}),"\n","Specific replacement parameters is as follows:     "))),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"   warehouse.store.td-engine.enable\n   warehouse.store.td-engine.url\n   warehouse.store.td-engine.username\n   warehouse.store.td-engine.password\n   \n   spring.mail.host\n   spring.mail.port\n   spring.mail.username\n   spring.mail.password\n")),(0,o.kt)("ol",{start:4},(0,o.kt)("li",{parentName:"ol"},"Configure the user configuration file(optional, user-defined user password)",(0,o.kt)("br",{parentName:"li"}),"HertzBeat default built-in three user accounts, respectively admin/hertzbeat tom/hertzbeat guest/hertzbeat",(0,o.kt)("br",{parentName:"li"}),"If you need add, delete or modify account or password, configure ",(0,o.kt)("inlineCode",{parentName:"li"},"sureness.yml"),". Ignore this step without this demand.",(0,o.kt)("br",{parentName:"li"}),"Create sureness.yml in the host directory\uff0ceg:/opt/sureness.yml",(0,o.kt)("br",{parentName:"li"}),"The configuration file content refer to project repository",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/dromara/hertzbeat/blob/master/script/sureness.yml"},"/script/sureness.yml"),"    ")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-yaml"},"\nresourceRole:\n   - /api/account/auth/refresh===post===[admin,user,guest]\n   - /api/apps/**===get===[admin,user,guest]\n   - /api/monitor/**===get===[admin,user,guest]\n   - /api/monitor/**===post===[admin,user]\n   - /api/monitor/**===put===[admin,user]\n   - /api/monitor/**===delete==[admin]\n   - /api/monitors/**===get===[admin,user,guest]\n   - /api/monitors/**===post===[admin,user]\n   - /api/monitors/**===put===[admin,user]\n   - /api/monitors/**===delete===[admin]\n   - /api/alert/**===get===[admin,user,guest]\n   - /api/alert/**===post===[admin,user]\n   - /api/alert/**===put===[admin,user]\n   - /api/alert/**===delete===[admin]\n   - /api/alerts/**===get===[admin,user,guest]\n   - /api/alerts/**===post===[admin,user]\n   - /api/alerts/**===put===[admin,user]\n   - /api/alerts/**===delete===[admin]\n   - /api/notice/**===get===[admin,user,guest]\n   - /api/notice/**===post===[admin,user]\n   - /api/notice/**===put===[admin,user]\n   - /api/notice/**===delete===[admin]\n   - /api/tag/**===get===[admin,user,guest]\n   - /api/tag/**===post===[admin,user]\n   - /api/tag/**===put===[admin,user]\n   - /api/tag/**===delete===[admin]\n   - /api/summary/**===get===[admin,user,guest]\n   - /api/summary/**===post===[admin,user]\n   - /api/summary/**===put===[admin,user]\n   - /api/summary/**===delete===[admin]\n\n# Resources that need to be filtered and protected can be accessed directly without authentication\n# /api/v1/source3===get means /api/v1/source3===get it can be accessed by anyone. Don't need to authentication\nexcludedResource:\n   - /api/account/auth/**===*\n   - /api/i18n/**===get\n   - /api/apps/hierarchy===get\n   # web ui  the front-end static resource\n   - /===get\n   - /dashboard/**===get\n   - /monitors/**===get\n   - /alert/**===get\n   - /account/**===get\n   - /setting/**===get\n   - /passport/**===get\n   - /**/*.html===get\n   - /**/*.js===get\n   - /**/*.css===get\n   - /**/*.ico===get\n   - /**/*.ttf===get\n   - /**/*.png===get\n   - /**/*.gif===get\n   - /**/*.jpg===get\n   - /**/*.svg===get\n   - /**/*.json===get\n   # swagger ui resource\n   - /swagger-resources/**===get\n   - /v2/api-docs===get\n   - /v3/api-docs===get\n\n# user account information\n# Here is admin tom lili three accounts\n# eg: admin includes[admin,user]roles, password is hertzbeat \n# eg: tom includes[user], password is hertzbeat\n# eg: lili includes[guest],text password is lili, salt password is 1A676730B0C7F54654B0E09184448289\naccount:\n   - appId: admin\n     credential: hertzbeat\n     role: [admin,user]\n   - appId: tom\n     credential: hertzbeat\n     role: [user]\n   - appId: guest\n     credential: hertzbeat\n     role: [guest]\n")),(0,o.kt)("p",null,"   Modify the following ",(0,o.kt)("strong",{parentName:"p"},"part parameters")," in sureness.yml ",(0,o.kt)("strong",{parentName:"p"},"[Note\u26a0\ufe0fOther default sureness configuration parameters should be retained]"),"\uff1a  "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-yaml"},"\n# user account information\n# Here is admin tom lili three accounts\n# eg: admin includes[admin,user]roles, password is hertzbeat \n# eg: tom includes[user], password is hertzbeat\n# eg: lili includes[guest], text password is lili, salt password is 1A676730B0C7F54654B0E09184448289\naccount:\n   - appId: admin\n     credential: hertzbeat\n     role: [admin,user]\n   - appId: tom\n     credential: hertzbeat\n     role: [user]\n   - appId: guest\n     credential: hertzbeat\n     role: [guest]\n")),(0,o.kt)("ol",{start:6},(0,o.kt)("li",{parentName:"ol"},"Start the HertzBeat Docker container    ")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-shell"},"$ docker run -d -p 1157:1157 \\\n    -e LANG=zh_CN.UTF-8 \\\n    -e TZ=Asia/Shanghai \\\n    -v /opt/data:/opt/hertzbeat/data \\\n    -v /opt/logs:/opt/hertzbeat/logs \\\n    -v /opt/application.yml:/opt/hertzbeat/config/application.yml \\\n    -v /opt/sureness.yml:/opt/hertzbeat/config/sureness.yml \\\n    --name hertzbeat tancloud/hertzbeat\n")),(0,o.kt)("p",null,"   This command starts a running HertzBeat Docker container, and the container port 1157 is mapped to the host machine 1157. If existing processes on the host use the port, please modify host mapped port.  "),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"docker run -d")," : Run a container in the background via Docker"),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-p 1157:1157"),"  : Mapping container ports to the host"),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-e LANG=zh_CN.UTF-8"),"  : (optional) set the LANG  "),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-e TZ=Asia/Shanghai")," : (optional) set the TimeZone  "),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-v /opt/data:/opt/hertzbeat/data")," : (optional\uff0cdata persistence) Important\u26a0\ufe0f Mount the H2 database file to the local host, to ensure that the data is not lost because of creating or deleting container.  "),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-v /opt/logs:/opt/hertzbeat/logs")," : (optional\uff0cif you don't have a need,just delete it) Mount the log file to the local host, to guarantee the log will not be lost because of creating or deleting container."),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-v /opt/application.yml:/opt/hertzbeat/config/application.yml"),"  : (optional\uff0cif you don't have a need,just delete it) Mount the local configuration file into the container which has been modified in the previous step, namely using the local configuration file to cover container configuration file. We need to modify MYSQL, TDengine configuration information in the configuration file to connect to an external service."),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"-v /opt/sureness.yml:/opt/hertzbeat/config/sureness.yml"),"  : (optional\uff0cif you don't have a need,just delete it) Mount account configuration file modified in the previous step into the container. Delete this command parameters if have no modify account needs."),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"--name hertzbeat")," : Naming container name hertzbeat "),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("inlineCode",{parentName:"li"},"tancloud/hertzbeat")," : Use the pulled latest HertzBeat official application mirror to start the container. version can be looked up in ",(0,o.kt)("a",{parentName:"li",href:"https://hub.docker.com/r/tancloud/hertzbeat/tags"},"official mirror repository"),"   ")),(0,o.kt)("ol",{start:7},(0,o.kt)("li",{parentName:"ol"},"Begin to explore HertzBeat",(0,o.kt)("br",{parentName:"li"}),"visit http://ip:1157/ on the browser. You can use HertzBeat monitoring alarm, default account and password are admin/hertzbeat.  ")),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"HAVE FUN"),"   "),(0,o.kt)("h3",{id:"docker-deployment-common-issues"},"Docker Deployment common issues"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},(0,o.kt)("strong",{parentName:"p"},"MYSQL, TDENGINE and HertzBeat are deployed on the same host by Docker,HertzBeat use localhost or 127.0.0.1 connect to the database but fail"),(0,o.kt)("br",{parentName:"p"}),"\n","The problems lies in Docker container failed to visit and connect localhost port. Beacuse the docker default network mode is Bridge mode which can't access loacl machine through localhost."),(0,o.kt)("blockquote",{parentName:"li"},(0,o.kt)("p",{parentName:"blockquote"},"Solution A\uff1aConfigure application.yml. Change database connection address from localhost to external IP of the host machine.",(0,o.kt)("br",{parentName:"p"}),"\n","Solution B\uff1aUse the Host network mode to start Docker, namely making Docker container and hosting share network. ",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d --network host ....."),"   "))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},(0,o.kt)("strong",{parentName:"p"},"According to the process deploy\uff0cvisit http://ip:1157/ no interface"),(0,o.kt)("br",{parentName:"p"}),"\n","Please refer to the following points to troubleshoot issues\uff1a  "),(0,o.kt)("blockquote",{parentName:"li"},(0,o.kt)("p",{parentName:"blockquote"},"one\uff1aIf you switch to dependency service MYSQL database\uff0ccheck whether the database is created and started successfully.\ntwo\uff1aCheck whether dependent services, IP account and password configuration is correct in HertzBeat's configuration file ",(0,o.kt)("inlineCode",{parentName:"p"},"application.yml"),".\nthree\uff1a",(0,o.kt)("inlineCode",{parentName:"p"},"docker logs hertzbeat")," Check whether the container log has errors. If you haven't solved the issue, report it to the communication group or community."))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},(0,o.kt)("strong",{parentName:"p"},"Log an error TDengine connection or insert SQL failed"),"  "),(0,o.kt)("blockquote",{parentName:"li"},(0,o.kt)("p",{parentName:"blockquote"},"one\uff1aCheck whether database account and password configured is correct, the database is created.",(0,o.kt)("br",{parentName:"p"}),"\n","two\uff1aIf you install TDengine2.3+ version, you must execute ",(0,o.kt)("inlineCode",{parentName:"p"},"systemctl start taosadapter")," to start adapter in addition to start the server.  "))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},(0,o.kt)("strong",{parentName:"p"},"Historical monitoring charts have been missing data for a long time"),"  "),(0,o.kt)("blockquote",{parentName:"li"},(0,o.kt)("p",{parentName:"blockquote"},"one\uff1aCheck whether you configure Tdengine. No configuration means no historical chart data.",(0,o.kt)("br",{parentName:"p"}),"\n","two\uff1aCheck whether Tdengine database ",(0,o.kt)("inlineCode",{parentName:"p"},"hertzbeat")," is created.\nthree: Check whether IP account and password configuration is correct in HertzBeat's configuration file ",(0,o.kt)("inlineCode",{parentName:"p"},"application.yml"),"."))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"If the history chart on the monitoring page is not displayed\uff0cpopup ","[please configure dependency service on TDengine time series database]"),(0,o.kt)("blockquote",{parentName:"li"},(0,o.kt)("p",{parentName:"blockquote"},"As shown in the popup window\uff0cthe premise of history chart display is that you need install and configure hertzbeat's dependency service - TDengine database.\nInstallation and initialization this database refer to ",(0,o.kt)("a",{parentName:"p",href:"tdengine-init"},"TDengine Installation and Initialization"),".")))))}u.isMDXComponent=!0}}]);