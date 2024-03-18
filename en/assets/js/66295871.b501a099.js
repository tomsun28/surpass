"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[949],{4137:(e,t,r)=>{r.d(t,{Zo:()=>m,kt:()=>g});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function l(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var s=n.createContext({}),c=function(e){var t=n.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},m=function(e){var t=c(e.components);return n.createElement(s.Provider,{value:t},e.children)},p="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,s=e.parentName,m=l(e,["components","mdxType","originalType","parentName"]),p=c(r),d=a,g=p["".concat(s,".").concat(d)]||p[d]||u[d]||o;return r?n.createElement(g,i(i({ref:t},m),{},{components:r})):n.createElement(g,i({ref:t},m))}));function g(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,i=new Array(o);i[0]=d;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l[p]="string"==typeof e?e:a,i[1]=l;for(var c=2;c<o;c++)i[c]=r[c];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},9129:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>s,contentTitle:()=>i,default:()=>u,frontMatter:()=>o,metadata:()=>l,toc:()=>c});var n=r(7462),a=(r(7294),r(4137));const o={id:"introduce",title:"HertzBeat",sidebar_label:"Introduce",slug:"/"},i=void 0,l={unversionedId:"introduce",id:"introduce",title:"HertzBeat",description:"An open-source, real-time monitoring system with custom-monitor and agentLess.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/introduce.md",sourceDirName:".",slug:"/",permalink:"/en/docs/",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/introduce.md",tags:[],version:"current",frontMatter:{id:"introduce",title:"HertzBeat",sidebar_label:"Introduce",slug:"/"},sidebar:"docs",next:{title:"Help Center",permalink:"/en/docs/help/guide"}},s={},c=[{value:'\ud83c\udfa1 <font color="green">Introduction</font>',id:"-introduction",level:2},{value:"\ud83e\udd50 Architecture",id:"-architecture",level:2}],m={toc:c},p="wrapper";function u(e){let{components:t,...r}=e;return(0,a.kt)(p,(0,n.Z)({},m,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"An open-source, real-time monitoring system with custom-monitor and agentLess.  ")),(0,a.kt)("p",null,(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/web-monitor-4EB1BA",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/api-monitor-lightgrey",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/ping-connect-brightgreen",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/port-available-green",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/database-monitor-yellowgreen",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/os-monitor-yellow",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/custom-monitor-orange",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/threshold-red",alt:"tan-cloud"}),"\n",(0,a.kt)("img",{parentName:"p",src:"https://img.shields.io/badge/alert-notify-bule",alt:"tan-cloud"})),(0,a.kt)("h2",{id:"-introduction"},"\ud83c\udfa1 ",(0,a.kt)("font",{color:"green"},"Introduction")),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},(0,a.kt)("a",{parentName:"p",href:"https://github.com/dromara/hertzbeat"},"HertzBeat")," is an open-source, real-time monitoring system with custom-monitor and agentless. Support web service, database, os, middleware and more.",(0,a.kt)("br",{parentName:"p"}),"\n","We also provide ",(0,a.kt)("strong",{parentName:"p"},(0,a.kt)("a",{parentName:"strong",href:"https://console.tancloud.cn"},"Monitoring Cloud For Saas")),", people no longer need to deploy a cumbersome monitoring system in order to monitor their website resources. ",(0,a.kt)("strong",{parentName:"p"},(0,a.kt)("a",{parentName:"strong",href:"https://console.tancloud.cn"},"Sign in to get started for free")),".",(0,a.kt)("br",{parentName:"p"}),"\n","HertzBeat supports more liberal threshold alarm configuration (calculation expression), supports alarm notification, alarm template, email, dingDing, weChat, feiShu, webhook and more.",(0,a.kt)("br",{parentName:"p"}),"\n","Most important is HertzBeat supports ",(0,a.kt)("a",{parentName:"p",href:"https://hertzbeat.com/docs/advanced/extend-point"},"Custom Monitoring"),", just by configuring the YML file, we can customize the monitoring types and metrics what we need.",(0,a.kt)("br",{parentName:"p"}),"\n","HertzBeat is modular, ",(0,a.kt)("inlineCode",{parentName:"p"},"manager, collector, scheduler, warehouse, alerter")," modules are decoupled for easy understanding and custom development.",(0,a.kt)("br",{parentName:"p"}),"\n","Welcome to join us to build hertzbeat together.  ")),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},(0,a.kt)("inlineCode",{parentName:"p"},"HertzBeat"),"'s multi-type support, easy expansion, low coupling, hope to help developers and micro teams to quickly build their own monitoring system.")),(0,a.kt)("hr",null),(0,a.kt)("h2",{id:"-architecture"},"\ud83e\udd50 Architecture"),(0,a.kt)("ul",null,(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/manager"},"manager"))," Provide monitoring management, system management basic services.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Provides monitoring management, monitoring configuration management, system user management, etc."))),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/collector"},"collector"))," Provide metrics data collection services.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Use common protocols to remotely collect and obtain peer-to-peer metrics data."))),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/scheduler"},"scheduler"))," Provide monitoring task scheduling service.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Collection task management, scheduling and distribution of one-time tasks and periodic tasks."))),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/warehouse"},"warehouse"))," Provide monitoring data warehousing services.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Metrics data management, data query, calculation and statistics."))),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/alerter"},"alerter"))," Provide alert service.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Alarm calculation trigger, monitoring status linkage, alarm configuration, and alarm notification."))),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("strong",{parentName:"li"},(0,a.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat/tree/master/web-app"},"web-app"))," Provide web ui.",(0,a.kt)("blockquote",{parentName:"li"},(0,a.kt)("p",{parentName:"blockquote"},"Angular Web UI.   ")))),(0,a.kt)("p",null,(0,a.kt)("img",{parentName:"p",src:"https://cdn.jsdelivr.net/gh/dromara/hertzbeat/home/static/img/docs/hertzbeat-stru-en.svg",alt:"hertzBeat"})))}u.isMDXComponent=!0}}]);