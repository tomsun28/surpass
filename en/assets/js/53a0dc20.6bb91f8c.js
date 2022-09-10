"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[4654],{4137:function(t,e,n){n.d(e,{Zo:function(){return p},kt:function(){return s}});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function i(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function o(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var m=a.createContext({}),d=function(t){var e=a.useContext(m),n=e;return t&&(n="function"==typeof t?t(e):i(i({},e),t)),n},p=function(t){var e=d(t.components);return a.createElement(m.Provider,{value:e},t.children)},u={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},c=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,m=t.parentName,p=o(t,["components","mdxType","originalType","parentName"]),c=d(n),s=r,k=c["".concat(m,".").concat(s)]||c[s]||u[s]||l;return n?a.createElement(k,i(i({ref:e},p),{},{components:n})):a.createElement(k,i({ref:e},p))}));function s(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,i=new Array(l);i[0]=c;var o={};for(var m in e)hasOwnProperty.call(e,m)&&(o[m]=e[m]);o.originalType=t,o.mdxType="string"==typeof t?t:r,i[1]=o;for(var d=2;d<l;d++)i[d]=n[d];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}c.displayName="MDXCreateElement"},2930:function(t,e,n){n.r(e),n.d(e,{frontMatter:function(){return o},contentTitle:function(){return m},metadata:function(){return d},toc:function(){return p},default:function(){return c}});var a=n(7462),r=n(3366),l=(n(7294),n(4137)),i=["components"],o={id:"jvm",title:"Monitor\uff1aJVM",sidebar_label:"JVM"},m=void 0,d={unversionedId:"help/jvm",id:"help/jvm",title:"Monitor\uff1aJVM",description:"Collect and monitor the general performance Metrics of JVM.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/jvm.md",sourceDirName:"help",slug:"/help/jvm",permalink:"/en/docs/help/jvm",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/jvm.md",tags:[],version:"current",frontMatter:{id:"jvm",title:"Monitor\uff1aJVM",sidebar_label:"JVM"},sidebar:"docs",previous:{title:"Full site monitoring",permalink:"/en/docs/help/fullsite"},next:{title:"MySQL database",permalink:"/en/docs/help/mysql"}},p=[{value:"Configuration parameter",id:"configuration-parameter",children:[],level:3},{value:"Collection Metrics",id:"collection-metrics",children:[{value:"Metrics Set\uff1amemory_pool",id:"metrics-setmemory_pool",children:[],level:4},{value:"Metrics Set\uff1acode_cache",id:"metrics-setcode_cache",children:[],level:4},{value:"Metrics Set\uff1aclass_loading",id:"metrics-setclass_loading",children:[],level:4},{value:"Metrics Set\uff1athread",id:"metrics-setthread",children:[],level:4}],level:3},{value:"JVM App Enable JMX Protocol",id:"jvm-app-enable-jmx-protocol",children:[],level:3}],u={toc:p};function c(t){var e=t.components,n=(0,r.Z)(t,i);return(0,l.kt)("wrapper",(0,a.Z)({},u,n,{components:e,mdxType:"MDXLayout"}),(0,l.kt)("blockquote",null,(0,l.kt)("p",{parentName:"blockquote"},"Collect and monitor the general performance Metrics of JVM.")),(0,l.kt)("p",null,(0,l.kt)("strong",{parentName:"p"},"Protocol Use\uff1aJMX")),(0,l.kt)("h3",{id:"configuration-parameter"},"Configuration parameter"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Parameter name"),(0,l.kt)("th",{parentName:"tr",align:null},"Parameter help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring Host"),(0,l.kt)("td",{parentName:"tr",align:null},"Monitored IPV4, IPV6 or domain name. Note\u26a0\ufe0fWithout protocol header (eg: https://, http://)")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring name"),(0,l.kt)("td",{parentName:"tr",align:null},"Identify the name of this monitoring. The name needs to be unique")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Port"),(0,l.kt)("td",{parentName:"tr",align:null},"Port provided by JMX")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Username"),(0,l.kt)("td",{parentName:"tr",align:null},"JMX connection user name, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Password"),(0,l.kt)("td",{parentName:"tr",align:null},"JMX connection password, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Collection interval"),(0,l.kt)("td",{parentName:"tr",align:null},"Interval time of monitor periodic data collection, unit: second, and the minimum interval that can be set is 10 seconds")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect"),(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect and check the availability of monitoring before adding monitoring. Adding and modifying operations will continue only after the detection is successful")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Description remarks"),(0,l.kt)("td",{parentName:"tr",align:null},"For more information about identifying and describing this monitoring, users can note information here")))),(0,l.kt)("h3",{id:"collection-metrics"},"Collection Metrics"),(0,l.kt)("h4",{id:"metrics-setmemory_pool"},"Metrics Set\uff1amemory_pool"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"name"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"metrics name")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"committed"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"total size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"init"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"init size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"max"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"max size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"used"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"used size")))),(0,l.kt)("h4",{id:"metrics-setcode_cache"},"Metrics Set\uff1acode_cache"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"committed"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"total size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"init"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"init size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"max"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"max size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"used"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"used size")))),(0,l.kt)("h4",{id:"metrics-setclass_loading"},"Metrics Set\uff1aclass_loading"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"LoadedClassCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Loaded Class Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"TotalLoadedClassCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Total Loaded Class Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"UnloadedClassCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Unloaded Class Count")))),(0,l.kt)("h4",{id:"metrics-setthread"},"Metrics Set\uff1athread"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"TotalStartedThreadCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Total Started Thread Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"ThreadCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Thread Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"PeakThreadCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Peak Thread Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"DaemonThreadCount"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Daemon Thread Count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"CurrentThreadUserTime"),(0,l.kt)("td",{parentName:"tr",align:null},"ms"),(0,l.kt)("td",{parentName:"tr",align:null},"Current Thread User Time")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"CurrentThreadCpuTime"),(0,l.kt)("td",{parentName:"tr",align:null},"ms"),(0,l.kt)("td",{parentName:"tr",align:null},"Current Thread Cpu Time")))),(0,l.kt)("h3",{id:"jvm-app-enable-jmx-protocol"},"JVM App Enable JMX Protocol"),(0,l.kt)("ol",null,(0,l.kt)("li",{parentName:"ol"},"Add JVM ",(0,l.kt)("inlineCode",{parentName:"li"},"VM options")," When Start Server \u26a0\ufe0f customIP")),(0,l.kt)("p",null,"Refer: ",(0,l.kt)("a",{parentName:"p",href:"https://docs.oracle.com/javase/1.5.0/docs/guide/management/agent.html#remote"},"https://docs.oracle.com/javase/1.5.0/docs/guide/management/agent.html#remote")),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-shell"},"-Djava.rmi.server.hostname=yourIP  \n-Dcom.sun.management.jmxremote.port=9999\n-Dcom.sun.management.jmxremote.ssl=false\n-Dcom.sun.management.jmxremote.authenticate=false \n")))}c.isMDXComponent=!0}}]);