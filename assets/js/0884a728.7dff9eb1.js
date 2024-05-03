"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[6717],{5680:(e,n,r)=>{r.d(n,{xA:()=>m,yg:()=>s});var t=r(6540);function a(e,n,r){return n in e?Object.defineProperty(e,n,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[n]=r,e}function i(e,n){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var t=Object.getOwnPropertySymbols(e);n&&(t=t.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),r.push.apply(r,t)}return r}function o(e){for(var n=1;n<arguments.length;n++){var r=null!=arguments[n]?arguments[n]:{};n%2?i(Object(r),!0).forEach((function(n){a(e,n,r[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(r,n))}))}return e}function l(e,n){if(null==e)return{};var r,t,a=function(e,n){if(null==e)return{};var r,t,a={},i=Object.keys(e);for(t=0;t<i.length;t++)r=i[t],n.indexOf(r)>=0||(a[r]=e[r]);return a}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(t=0;t<i.length;t++)r=i[t],n.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var g=t.createContext({}),p=function(e){var n=t.useContext(g),r=n;return e&&(r="function"==typeof e?e(n):o(o({},n),e)),r},m=function(e){var n=p(e.components);return t.createElement(g.Provider,{value:n},e.children)},u={inlineCode:"code",wrapper:function(e){var n=e.children;return t.createElement(t.Fragment,{},n)}},c=t.forwardRef((function(e,n){var r=e.components,a=e.mdxType,i=e.originalType,g=e.parentName,m=l(e,["components","mdxType","originalType","parentName"]),c=p(r),s=a,y=c["".concat(g,".").concat(s)]||c[s]||u[s]||i;return r?t.createElement(y,o(o({ref:n},m),{},{components:r})):t.createElement(y,o({ref:n},m))}));function s(e,n){var r=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var i=r.length,o=new Array(i);o[0]=c;var l={};for(var g in n)hasOwnProperty.call(n,g)&&(l[g]=n[g]);l.originalType=e,l.mdxType="string"==typeof e?e:a,o[1]=l;for(var p=2;p<i;p++)o[p]=r[p];return t.createElement.apply(null,o)}return t.createElement.apply(null,r)}c.displayName="MDXCreateElement"},5673:(e,n,r)=>{r.r(n),r.d(n,{assets:()=>g,contentTitle:()=>o,default:()=>u,frontMatter:()=>i,metadata:()=>l,toc:()=>p});var t=r(8168),a=(r(6540),r(5680));const i={id:"guide",title:"Help Center",sidebar_label:"Help Center"},o=void 0,l={unversionedId:"help/guide",id:"help/guide",title:"Help Center",description:"Hertzbeat - An open source, real-time monitoring tool with custom-monitor and agentLess.",source:"@site/docs/help/guide.md",sourceDirName:"help",slug:"/help/guide",permalink:"/docs/help/guide",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/docs/help/guide.md",tags:[],version:"current",frontMatter:{id:"guide",title:"Help Center",sidebar_label:"Help Center"},sidebar:"docs",previous:{title:"Introduce",permalink:"/docs/"},next:{title:"Prometheus Task Monitor",permalink:"/docs/help/prometheus"}},g={},p=[{value:"\ud83d\udd2c Monitoring services",id:"-monitoring-services",level:2},{value:"Application service monitoring",id:"application-service-monitoring",level:3},{value:"Database monitoring",id:"database-monitoring",level:3},{value:"Operating system monitoring",id:"operating-system-monitoring",level:3},{value:"Middleware monitoring",id:"middleware-monitoring",level:3},{value:"CloudNative monitoring",id:"cloudnative-monitoring",level:3},{value:"Bigdata monitoring",id:"bigdata-monitoring",level:3},{value:"Network monitoring",id:"network-monitoring",level:3},{value:"\ud83d\udca1 Alarm service",id:"-alarm-service",level:2},{value:"Alarm center",id:"alarm-center",level:3},{value:"Alarm configuration",id:"alarm-configuration",level:3},{value:"Alarm notification",id:"alarm-notification",level:3}],m={toc:p};function u(e){let{components:n,...r}=e;return(0,a.yg)("wrapper",(0,t.A)({},m,r,{components:n,mdxType:"MDXLayout"}),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"Hertzbeat - An open source, real-time monitoring tool with custom-monitor and agentLess.",(0,a.yg)("br",{parentName:"p"}),"\n","Help documents and auxiliary information during use ")),(0,a.yg)("h2",{id:"-monitoring-services"},"\ud83d\udd2c Monitoring services"),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"Regularly collect and monitor the performance Metrics exposed by end-to-end services, provide visual interfaces, and process data for alarm and other service scheduling.",(0,a.yg)("br",{parentName:"p"}),"\n","Planned monitoring type\uff1aapplication service, database, operating system, cloud native, open source middleware.")),(0,a.yg)("h3",{id:"application-service-monitoring"},"Application service monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"website"},"Website monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"api"},"HTTP API")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"ping"},"PING Connectivity")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"port"},"Port availability")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"fullsite"},"Full site monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"ssl_cert"},"SSL Cert monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"jvm"},"JVM monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"springboot2"},"SpringBoot2.0")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"dns"},"DNS monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"ftp"},"FTP monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"websocket"},"Websocket monitoring")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"database-monitoring"},"Database monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"mysql"},"MYSQL database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"mariadb"},"MariaDB database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"postgresql"},"PostgreSQL database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"sqlserver"},"SqlServer database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"oracle"},"Oracle database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"dm"},"DM database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"opengauss"},"OpenGauss database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"iotdb"},"IoTDB database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"tidb"},"TiDB database monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"mongodb"},"MongoDB database monitoring")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"operating-system-monitoring"},"Operating system monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"linux"},"Linux operating system monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"windows"},"Windows operating system monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"ubuntu"},"Ubuntu operating system monitoring")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"centos"},"Centos operating system monitoring")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"middleware-monitoring"},"Middleware monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"zookeeper"},"Zookeeper")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"kafka"},"Kafka")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"tomcat"},"Tomcat")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"shenyu"},"ShenYu")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"dynamic_tp"},"DynamicTp")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"rabbitmq"},"RabbitMQ")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"activemq"},"ActiveMQ")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"jetty"},"Jetty")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"nacos"},"Nacos")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"cloudnative-monitoring"},"CloudNative monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"docker"},"Docker")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"kubernetes"},"Kubernetes")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"bigdata-monitoring"},"Bigdata monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"clickhouse"},"Clickhouse")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"elasticsearch"},"ElasticSearch")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"flink"},"Flink")," ",(0,a.yg)("br",null)),(0,a.yg)("h3",{id:"network-monitoring"},"Network monitoring"),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"huawei_switch"},"Huawei-switch")," ",(0,a.yg)("br",null)," "),(0,a.yg)("hr",null),(0,a.yg)("h2",{id:"-alarm-service"},"\ud83d\udca1 Alarm service"),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"More liberal threshold alarm configuration (calculation expression), supports email, SMS, WebHook, DingDing, WeChat and FeiShu for alarm notification.\nThe positioning of alarm service is to trigger the threshold accurately and timely, and the alarm notification can be reached in time.")),(0,a.yg)("h3",{id:"alarm-center"},"Alarm center"),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"The triggered alarm information center provides query and filtering of alarm deletion, alarm processing, mark unprocessed, alarm level status, etc.")),(0,a.yg)("h3",{id:"alarm-configuration"},"Alarm configuration"),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"The Metric threshold configuration provides the Metric threshold configuration in the form of expression, which can set the alarm level, trigger times, alarm notification template and whether it is enabled, correlation monitoring and other functions.")),(0,a.yg)("p",null,"More details see","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_threshold"},"Threshold alarm")," ",(0,a.yg)("br",null),"\n","\u2003","\u2003","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_threshold_expr"},"Threshold expression"),"   "),(0,a.yg)("h3",{id:"alarm-notification"},"Alarm notification"),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"After triggering the alarm information, in addition to being displayed in the alarm center list, it can also be notified to the designated recipient in a specified way (e-mail, wechat and FeiShu etc.)",(0,a.yg)("br",{parentName:"p"}),"\n","Alarm notification provides different types of notification methods, such as email recipient, enterprise wechat robot notification, DingDing robot notification, and FeiShu robot notification.",(0,a.yg)("br",{parentName:"p"}),"\n","After setting the receiver, you need to set the associated alarm notification strategy to configure which alarm information is sent to which receiver.   ")),(0,a.yg)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_email"},"Configure Email Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_webhook"},"Configure Discord Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_webhook"},"Configure Slack Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_webhook"},"Configure Telegram Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_webhook"},"Configure WebHook Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_wework"},"Configure enterprise WeChat Robot Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_dingtalk"},"Configure DingDing Robot Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_feishu"},"Configure FeiShu Robot Notification")," ",(0,a.yg)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.yg)("a",{parentName:"p",href:"alert_smn"},"Configure Huawei Cloud SMN Notification")," ",(0,a.yg)("br",null)))}u.isMDXComponent=!0}}]);