"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[6860],{5680:(e,t,n)=>{n.d(t,{xA:()=>m,yg:()=>u});var r=n(6540);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function l(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?l(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},l=Object.keys(e);for(r=0;r<l.length;r++)n=l[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);for(r=0;r<l.length;r++)n=l[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var p=r.createContext({}),g=function(e){var t=r.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},m=function(e){var t=g(e.components);return r.createElement(p.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},s=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,l=e.originalType,p=e.parentName,m=i(e,["components","mdxType","originalType","parentName"]),s=g(n),u=a,c=s["".concat(p,".").concat(u)]||s[u]||d[u]||l;return n?r.createElement(c,o(o({ref:t},m),{},{components:n})):r.createElement(c,o({ref:t},m))}));function u(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var l=n.length,o=new Array(l);o[0]=s;var i={};for(var p in t)hasOwnProperty.call(t,p)&&(i[p]=t[p]);i.originalType=e,i.mdxType="string"==typeof e?e:a,o[1]=i;for(var g=2;g<l;g++)o[g]=n[g];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}s.displayName="MDXCreateElement"},9447:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>o,default:()=>d,frontMatter:()=>l,metadata:()=>i,toc:()=>g});var r=n(8168),a=(n(6540),n(5680));const l={id:"flink",title:"Monitoring Flink",sidebar_label:"Flink",keywords:["open source monitoring tool","open source flink monitoring tool"]},o=void 0,i={unversionedId:"help/flink",id:"help/flink",title:"Monitoring Flink",description:"Collect and monitor the general performance Metrics of Flink.",source:"@site/docs/help/flink.md",sourceDirName:"help",slug:"/help/flink",permalink:"/docs/help/flink",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/docs/help/flink.md",tags:[],version:"current",frontMatter:{id:"flink",title:"Monitoring Flink",sidebar_label:"Flink",keywords:["open source monitoring tool","open source flink monitoring tool"]},sidebar:"docs",previous:{title:"Spark Monitor",permalink:"/docs/help/spark"},next:{title:"DORIS\u6570\u636e\u5e93BE",permalink:"/docs/help/doris_be"}},p={},g=[{value:"Configuration parameter",id:"configuration-parameter",level:3},{value:"Collection Metrics",id:"collection-metrics",level:3},{value:"Metrics Set\uff1aOverview",id:"metrics-setoverview",level:4}],m={toc:g};function d(e){let{components:t,...n}=e;return(0,a.yg)("wrapper",(0,r.A)({},m,n,{components:t,mdxType:"MDXLayout"}),(0,a.yg)("blockquote",null,(0,a.yg)("p",{parentName:"blockquote"},"Collect and monitor the general performance Metrics of Flink.")),(0,a.yg)("h3",{id:"configuration-parameter"},"Configuration parameter"),(0,a.yg)("table",null,(0,a.yg)("thead",{parentName:"table"},(0,a.yg)("tr",{parentName:"thead"},(0,a.yg)("th",{parentName:"tr",align:null},"Parameter Name"),(0,a.yg)("th",{parentName:"tr",align:null},"Parameter Help Description"))),(0,a.yg)("tbody",{parentName:"table"},(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Monitor Host"),(0,a.yg)("td",{parentName:"tr",align:null},"The monitored peer IPV4, IPV6, or domain name. Note: Do not include protocol headers (e.g., https://, http://).")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Task Name"),(0,a.yg)("td",{parentName:"tr",align:null},"Identifier for this monitoring task, name must be unique.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Port"),(0,a.yg)("td",{parentName:"tr",align:null},"Monitoring port.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Query Timeout"),(0,a.yg)("td",{parentName:"tr",align:null},"Sets the timeout for JVM connection in milliseconds, default is 3000 milliseconds.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"SSL"),(0,a.yg)("td",{parentName:"tr",align:null},"Whether to enable SSL (default is off).")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Username"),(0,a.yg)("td",{parentName:"tr",align:null},"Connection username.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Password"),(0,a.yg)("td",{parentName:"tr",align:null},"Connection password.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Collection Interval"),(0,a.yg)("td",{parentName:"tr",align:null},"Interval for periodic data collection during monitoring, in seconds. The minimum settable interval is 30 seconds.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Whether to detect"),(0,a.yg)("td",{parentName:"tr",align:null},"Whether to perform a probe check for monitoring availability before adding a new monitor; operations proceed if successful.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"Description Remarks"),(0,a.yg)("td",{parentName:"tr",align:null},"Additional identifiers and descriptions for this monitoring, where users can note information.")))),(0,a.yg)("h3",{id:"collection-metrics"},"Collection Metrics"),(0,a.yg)("h4",{id:"metrics-setoverview"},"Metrics Set\uff1aOverview"),(0,a.yg)("table",null,(0,a.yg)("thead",{parentName:"table"},(0,a.yg)("tr",{parentName:"thead"},(0,a.yg)("th",{parentName:"tr",align:null},"Metric Name"),(0,a.yg)("th",{parentName:"tr",align:null},"Metric Unit"),(0,a.yg)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,a.yg)("tbody",{parentName:"table"},(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"slots_total"),(0,a.yg)("td",{parentName:"tr",align:null},"Units"),(0,a.yg)("td",{parentName:"tr",align:null},"Total number of slots.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"slots_used"),(0,a.yg)("td",{parentName:"tr",align:null},"Units"),(0,a.yg)("td",{parentName:"tr",align:null},"Number of slots used.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"task_total"),(0,a.yg)("td",{parentName:"tr",align:null},"Units"),(0,a.yg)("td",{parentName:"tr",align:null},"Total number of tasks.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"jobs_running"),(0,a.yg)("td",{parentName:"tr",align:null},"Units"),(0,a.yg)("td",{parentName:"tr",align:null},"Number of jobs running.")),(0,a.yg)("tr",{parentName:"tbody"},(0,a.yg)("td",{parentName:"tr",align:null},"jobs_failed"),(0,a.yg)("td",{parentName:"tr",align:null},"Units"),(0,a.yg)("td",{parentName:"tr",align:null},"Number of jobs failed.")))))}d.isMDXComponent=!0}}]);