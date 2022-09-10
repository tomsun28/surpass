"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[1217],{4137:function(e,t,n){n.d(t,{Zo:function(){return s},kt:function(){return d}});var r=n(7294);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function a(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?a(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):a(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,i=function(e,t){if(null==e)return{};var n,r,i={},a=Object.keys(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var c=r.createContext({}),p=function(e){var t=r.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=p(e.components);return r.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},m=r.forwardRef((function(e,t){var n=e.components,i=e.mdxType,a=e.originalType,c=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),m=p(n),d=i,f=m["".concat(c,".").concat(d)]||m[d]||u[d]||a;return n?r.createElement(f,o(o({ref:t},s),{},{components:n})):r.createElement(f,o({ref:t},s))}));function d(e,t){var n=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var a=n.length,o=new Array(a);o[0]=m;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:i,o[1]=l;for(var p=2;p<a;p++)o[p]=n[p];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}m.displayName="MDXCreateElement"},1938:function(e,t,n){n.r(t),n.d(t,{frontMatter:function(){return l},contentTitle:function(){return c},metadata:function(){return p},toc:function(){return s},default:function(){return m}});var r=n(7462),i=n(3366),a=(n(7294),n(4137)),o=["components"],l={id:"guide",title:"Help center",sidebar_label:"Help center"},c=void 0,p={unversionedId:"help/guide",id:"help/guide",title:"Help center",description:"Hertzbeat - An open-source, real-time monitoring system with custom-monitor and agentLess.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/guide.md",sourceDirName:"help",slug:"/help/guide",permalink:"/en/docs/help/guide",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/guide.md",tags:[],version:"current",frontMatter:{id:"guide",title:"Help center",sidebar_label:"Help center"},sidebar:"docs",previous:{title:"SSH Protocol Custom Monitoring",permalink:"/en/docs/advanced/extend-ssh"},next:{title:"Website monitoring",permalink:"/en/docs/help/website"}},s=[{value:"\ud83d\udd2c Monitoring services",id:"-monitoring-services",children:[{value:"Application service monitoring",id:"application-service-monitoring",children:[],level:3},{value:"Database monitoring",id:"database-monitoring",children:[],level:3},{value:"Operating system monitoring",id:"operating-system-monitoring",children:[],level:3},{value:"Middleware monitoring",id:"middleware-monitoring",children:[],level:3}],level:2},{value:"\ud83d\udca1 Alarm service",id:"-alarm-service",children:[{value:"Alarm center",id:"alarm-center",children:[],level:3},{value:"Alarm configuration",id:"alarm-configuration",children:[],level:3},{value:"Alarm notification",id:"alarm-notification",children:[],level:3}],level:2}],u={toc:s};function m(e){var t=e.components,n=(0,i.Z)(e,o);return(0,a.kt)("wrapper",(0,r.Z)({},u,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"Hertzbeat - An open-source, real-time monitoring system with custom-monitor and agentLess.",(0,a.kt)("br",{parentName:"p"}),"\n","Help documents and auxiliary information during use ")),(0,a.kt)("h2",{id:"-monitoring-services"},"\ud83d\udd2c Monitoring services"),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"Regularly collect and monitor the performance Metrics exposed by end-to-end services, provide visual interfaces, and process data for alarm and other service scheduling.",(0,a.kt)("br",{parentName:"p"}),"\n","Planned monitoring type\uff1aapplication service, database, operating system, cloud native, open source middleware.")),(0,a.kt)("h3",{id:"application-service-monitoring"},"Application service monitoring"),(0,a.kt)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"website"},"Website monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"api"},"HTTP API")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"ping"},"PING Connectivity")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"port"},"Port availability")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"fullsite"},"Full site monitoring")," ",(0,a.kt)("br",null)),(0,a.kt)("h3",{id:"database-monitoring"},"Database monitoring"),(0,a.kt)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"mysql"},"MYSQL database monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"mariadb"},"MariaDB database monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"postgresql"},"PostgreSQL database monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"sqlserver"},"SqlServer database monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"oracle"},"Oracle database monitoring")," ",(0,a.kt)("br",null),"        "),(0,a.kt)("h3",{id:"operating-system-monitoring"},"Operating system monitoring"),(0,a.kt)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"linux"},"Linux operating system monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"windows"},"Windows operating system monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"ubuntu"},"Ubuntu operating system monitoring")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"centos"},"Centos operating system monitoring")," ",(0,a.kt)("br",null)),(0,a.kt)("h3",{id:"middleware-monitoring"},"Middleware monitoring"),(0,a.kt)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"zookeeper"},"Zookeeper")," ",(0,a.kt)("br",null)),(0,a.kt)("hr",null),(0,a.kt)("h2",{id:"-alarm-service"},"\ud83d\udca1 Alarm service"),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"More liberal threshold alarm configuration (calculation expression), supports email, SMS, WebHook, DingDing, WeChat and FeiShu for alarm notification.\nThe positioning of alarm service is to trigger the threshold accurately and timely, and the alarm notification can be reached in time.")),(0,a.kt)("h3",{id:"alarm-center"},"Alarm center"),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"The triggered alarm information center provides query and filtering of alarm deletion, alarm processing, mark unprocessed, alarm level status, etc.")),(0,a.kt)("h3",{id:"alarm-configuration"},"Alarm configuration"),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"The Metric threshold configuration provides the Metric threshold configuration in the form of expression, which can set the alarm level, trigger times, alarm notification template and whether it is enabled, correlation monitoring and other functions.")),(0,a.kt)("p",null,"More details see","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_threshold"},"threshold alarm")," ",(0,a.kt)("br",null),"\n","\u2003","\u2003","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_threshold_expr"},"Threshold expression"),"   "),(0,a.kt)("h3",{id:"alarm-notification"},"Alarm notification"),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"After triggering the alarm information, in addition to being displayed in the alarm center list, it can also be notified to the designated recipient in a specified way (e-mail, wechat and FeiShu etc.)",(0,a.kt)("br",{parentName:"p"}),"\n","Alarm notification provides different types of notification methods, such as email recipient, enterprise wechat robot notification, DingDing robot notification, and FeiShu robot notification.",(0,a.kt)("br",{parentName:"p"}),"\n","After setting the receiver, you need to set the associated alarm notification strategy to configure which alarm information is sent to which receiver.   ")),(0,a.kt)("p",null,"\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_email"},"Configure email notification")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_webhook"},"Configure WebHook notification")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_wework"},"Configure enterprise wechat robot notification")," ",(0,a.kt)("br",null),(0,a.kt)("br",{parentName:"p"}),"\n","","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_dingtalk"},"Configure DingDing robot notification")," ",(0,a.kt)("br",null),"\n","\u2003","\ud83d\udc49","\u2003",(0,a.kt)("a",{parentName:"p",href:"alert_feishu"},"Configure FeiShu robot notification")," ",(0,a.kt)("br",null)))}m.isMDXComponent=!0}}]);