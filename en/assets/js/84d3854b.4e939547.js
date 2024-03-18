"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[3024],{4137:(t,e,r)=>{r.d(e,{Zo:()=>c,kt:()=>g});var n=r(7294);function a(t,e,r){return e in t?Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}):t[e]=r,t}function o(t,e){var r=Object.keys(t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),r.push.apply(r,n)}return r}function l(t){for(var e=1;e<arguments.length;e++){var r=null!=arguments[e]?arguments[e]:{};e%2?o(Object(r),!0).forEach((function(e){a(t,e,r[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(r,e))}))}return t}function i(t,e){if(null==t)return{};var r,n,a=function(t,e){if(null==t)return{};var r,n,a={},o=Object.keys(t);for(n=0;n<o.length;n++)r=o[n],e.indexOf(r)>=0||(a[r]=t[r]);return a}(t,e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);for(n=0;n<o.length;n++)r=o[n],e.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(t,r)&&(a[r]=t[r])}return a}var m=n.createContext({}),p=function(t){var e=n.useContext(m),r=e;return t&&(r="function"==typeof t?t(e):l(l({},e),t)),r},c=function(t){var e=p(t.components);return n.createElement(m.Provider,{value:e},t.children)},d="mdxType",u={inlineCode:"code",wrapper:function(t){var e=t.children;return n.createElement(n.Fragment,{},e)}},s=n.forwardRef((function(t,e){var r=t.components,a=t.mdxType,o=t.originalType,m=t.parentName,c=i(t,["components","mdxType","originalType","parentName"]),d=p(r),s=a,g=d["".concat(m,".").concat(s)]||d[s]||u[s]||o;return r?n.createElement(g,l(l({ref:e},c),{},{components:r})):n.createElement(g,l({ref:e},c))}));function g(t,e){var r=arguments,a=e&&e.mdxType;if("string"==typeof t||a){var o=r.length,l=new Array(o);l[0]=s;var i={};for(var m in e)hasOwnProperty.call(e,m)&&(i[m]=e[m]);i.originalType=t,i[d]="string"==typeof t?t:a,l[1]=i;for(var p=2;p<o;p++)l[p]=r[p];return n.createElement.apply(null,l)}return n.createElement.apply(null,r)}s.displayName="MDXCreateElement"},1746:(t,e,r)=>{r.r(e),r.d(e,{assets:()=>m,contentTitle:()=>l,default:()=>u,frontMatter:()=>o,metadata:()=>i,toc:()=>p});var n=r(7462),a=(r(7294),r(4137));const o={id:"ntp",title:"Monitoring NTP",sidebar_label:"NTP Monitor",keywords:["open source monitoring tool","open source NTP monitoring tool","monitoring NTP metrics"]},l=void 0,i={unversionedId:"help/ntp",id:"help/ntp",title:"Monitoring NTP",description:"Collect and monitor the general performance Metrics of NTP.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/ntp.md",sourceDirName:"help",slug:"/help/ntp",permalink:"/en/docs/help/ntp",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/ntp.md",tags:[],version:"current",frontMatter:{id:"ntp",title:"Monitoring NTP",sidebar_label:"NTP Monitor",keywords:["open source monitoring tool","open source NTP monitoring tool","monitoring NTP metrics"]}},m={},p=[{value:"Configuration parameter",id:"configuration-parameter",level:3},{value:"Collection Metrics",id:"collection-metrics",level:3},{value:"Metrics Set\uff1asummary",id:"metrics-setsummary",level:4}],c={toc:p},d="wrapper";function u(t){let{components:e,...r}=t;return(0,a.kt)(d,(0,n.Z)({},c,r,{components:e,mdxType:"MDXLayout"}),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"Collect and monitor the general performance Metrics of NTP.")),(0,a.kt)("p",null,(0,a.kt)("strong",{parentName:"p"},"Protocol Use\uff1aNTP")),(0,a.kt)("h3",{id:"configuration-parameter"},"Configuration parameter"),(0,a.kt)("table",null,(0,a.kt)("thead",{parentName:"table"},(0,a.kt)("tr",{parentName:"thead"},(0,a.kt)("th",{parentName:"tr",align:null},"Parameter name"),(0,a.kt)("th",{parentName:"tr",align:null},"Parameter help description"))),(0,a.kt)("tbody",{parentName:"table"},(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"Monitoring Host"),(0,a.kt)("td",{parentName:"tr",align:null},"Monitored IPV4, IPV6 or domain name. Note\u26a0\ufe0fWithout protocol header (eg: https://, http://)")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"Monitoring name"),(0,a.kt)("td",{parentName:"tr",align:null},"Identify the name of this monitoring. The name needs to be unique")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"Collection interval"),(0,a.kt)("td",{parentName:"tr",align:null},"Interval time of monitor periodic data collection, unit: second, and the minimum interval that can be set is 30 seconds")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"Whether to detect"),(0,a.kt)("td",{parentName:"tr",align:null},"Whether to detect and check the availability of monitoring before adding monitoring. Adding and modifying operations will continue only after the detection is successful")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"Description remarks"),(0,a.kt)("td",{parentName:"tr",align:null},"For more information about identifying and describing this monitoring, users can note information here")))),(0,a.kt)("h3",{id:"collection-metrics"},"Collection Metrics"),(0,a.kt)("h4",{id:"metrics-setsummary"},"Metrics Set\uff1asummary"),(0,a.kt)("table",null,(0,a.kt)("thead",{parentName:"table"},(0,a.kt)("tr",{parentName:"thead"},(0,a.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,a.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,a.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,a.kt)("tbody",{parentName:"table"},(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"responseTime"),(0,a.kt)("td",{parentName:"tr",align:null},"ms"),(0,a.kt)("td",{parentName:"tr",align:null},"The time it takes for the NTP server to respond to a request).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"time"),(0,a.kt)("td",{parentName:"tr",align:null},"ms"),(0,a.kt)("td",{parentName:"tr",align:null},"The current time reported by the NTP server).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"date"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"The date corresponding to the current time reported by the NTP server).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"offset"),(0,a.kt)("td",{parentName:"tr",align:null},"ms"),(0,a.kt)("td",{parentName:"tr",align:null},"The time difference between the NTP server's clock and the client's clock).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"delay"),(0,a.kt)("td",{parentName:"tr",align:null},"ms"),(0,a.kt)("td",{parentName:"tr",align:null},"The time it takes for a request to reach the NTP server and for the response to return).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"version"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"The version number of the NTP protocol used by the server).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"mode"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"The operating mode of the NTP server, such as client, server, or broadcast).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"stratum"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"The stratumevel of the NTP server, indicating its distance from a reference clock).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"referenceId"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"An identifier that indicates the reference clock or time source used by the NTP server).")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:null},"precision"),(0,a.kt)("td",{parentName:"tr",align:null}),(0,a.kt)("td",{parentName:"tr",align:null},"The precision of the NTP server's clock, indicating its accuracy).")))))}u.isMDXComponent=!0}}]);