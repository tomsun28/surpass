"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[2577],{4137:(t,e,n)=>{n.d(e,{Zo:()=>p,kt:()=>k});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function i(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function o(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var m=a.createContext({}),c=function(t){var e=a.useContext(m),n=e;return t&&(n="function"==typeof t?t(e):i(i({},e),t)),n},p=function(t){var e=c(t.components);return a.createElement(m.Provider,{value:e},t.children)},d="mdxType",u={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},s=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,m=t.parentName,p=o(t,["components","mdxType","originalType","parentName"]),d=c(n),s=r,k=d["".concat(m,".").concat(s)]||d[s]||u[s]||l;return n?a.createElement(k,i(i({ref:e},p),{},{components:n})):a.createElement(k,i({ref:e},p))}));function k(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,i=new Array(l);i[0]=s;var o={};for(var m in e)hasOwnProperty.call(e,m)&&(o[m]=e[m]);o.originalType=t,o[d]="string"==typeof t?t:r,i[1]=o;for(var c=2;c<l;c++)i[c]=n[c];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}s.displayName="MDXCreateElement"},7144:(t,e,n)=>{n.r(e),n.d(e,{assets:()=>m,contentTitle:()=>i,default:()=>u,frontMatter:()=>l,metadata:()=>o,toc:()=>c});var a=n(7462),r=(n(7294),n(4137));const l={id:"iotdb",title:"Monitoring Apache IoTDB Database",sidebar_label:"IoTDB Database",keywords:["open source monitoring tool","open source database monitoring tool","monitoring IotDB database metrics"]},i=void 0,o={unversionedId:"help/iotdb",id:"help/iotdb",title:"Monitoring Apache IoTDB Database",description:"Monitor the running status of the Apache IoTDB Internet of Things time series database (JVM-related), memory task clusters and other related Metrics.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/iotdb.md",sourceDirName:"help",slug:"/help/iotdb",permalink:"/en/docs/help/iotdb",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/iotdb.md",tags:[],version:"current",frontMatter:{id:"iotdb",title:"Monitoring Apache IoTDB Database",sidebar_label:"IoTDB Database",keywords:["open source monitoring tool","open source database monitoring tool","monitoring IotDB database metrics"]},sidebar:"docs",previous:{title:"Hadoop",permalink:"/en/docs/help/hadoop"},next:{title:"Apache Hive",permalink:"/en/docs/help/hive"}},m={},c=[{value:"Operation before monitoring",id:"operation-before-monitoring",level:2},{value:"Configuration parameters",id:"configuration-parameters",level:3},{value:"Collect metrics",id:"collect-metrics",level:3},{value:"Metric collection: cluster_node_status",id:"metric-collection-cluster_node_status",level:4},{value:"Metric collection: jvm_memory_committed_bytes",id:"metric-collection-jvm_memory_committed_bytes",level:4},{value:"Metric collection: jvm_memory_used_bytes",id:"metric-collection-jvm_memory_used_bytes",level:4},{value:"Metric collection: jvm_threads_states_threads",id:"metric-collection-jvm_threads_states_threads",level:4},{value:"Index collection: quantity business data",id:"index-collection-quantity-business-data",level:4},{value:"Metric collection: cache_hit cache",id:"metric-collection-cache_hit-cache",level:4},{value:"Metric collection: queue task queue",id:"metric-collection-queue-task-queue",level:4},{value:"Metric collection: thrift_connections",id:"metric-collection-thrift_connections",level:4}],p={toc:c},d="wrapper";function u(t){let{components:e,...n}=t;return(0,r.kt)(d,(0,a.Z)({},p,n,{components:e,mdxType:"MDXLayout"}),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"Monitor the running status of the Apache IoTDB Internet of Things time series database (JVM-related), memory task clusters and other related Metrics.")),(0,r.kt)("h2",{id:"operation-before-monitoring"},"Operation before monitoring"),(0,r.kt)("p",null,"You need to enable the ",(0,r.kt)("inlineCode",{parentName:"p"},"metrics")," function in IoTDB, which will provide interface data in the form of prometheus metrics."),(0,r.kt)("p",null,"To enable the ",(0,r.kt)("inlineCode",{parentName:"p"},"metrics")," function, refer to ",(0,r.kt)("a",{parentName:"p",href:"https://iotdb.apache.org/UserGuide/V0.13.x/Maintenance-Tools/Metric-Tool.html"},"Official Documentation")),(0,r.kt)("p",null,"The main steps are as follows:"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"The metric collection is disabled by default, you need to modify the parameters in ",(0,r.kt)("inlineCode",{parentName:"li"},"conf/iotdb-metric.yml")," first, then restart the server")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre"},"# Whether to start the monitoring module, the default is false\nenableMetric: true\n\n# Whether to enable operation delay statistics\nenablePerformanceStat: false\n\n# Data provision method, externally provide metrics data through jmx and prometheus protocol, optional parameters: [JMX, PROMETHEUS, IOTDB], IOTDB is closed by default.\nmetricReporterList:\n   - JMX\n   - PROMETHEUS\n\n# The metric architecture used at the bottom layer, optional parameters: [MICROMETER, DROPWIZARD]\nmonitorType: MICROMETER\n\n# Initialize the level of the metric, optional parameters: [CORE, IMPORTANT, NORMAL, ALL]\nmetricLevel: IMPORTANT\n\n# Predefined metrics set, optional parameters: [JVM, LOGBACK, FILE, PROCESS, SYSTEM]\npredefinedMetrics:\n   - JVM\n   - FILE\n")),(0,r.kt)("ol",{start:2},(0,r.kt)("li",{parentName:"ol"},(0,r.kt)("p",{parentName:"li"},"Restart IoTDB, open a browser or use curl to access http://servier_ip:9091/metrics, and you can see the metric data.")),(0,r.kt)("li",{parentName:"ol"},(0,r.kt)("p",{parentName:"li"},"Add the corresponding IoTDB monitoring in HertzBeat."))),(0,r.kt)("h3",{id:"configuration-parameters"},"Configuration parameters"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Parameter name"),(0,r.kt)("th",{parentName:"tr",align:null},"Parameter help description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Monitoring Host"),(0,r.kt)("td",{parentName:"tr",align:null},"The peer IPV4, IPV6 or domain name to be monitored. Note \u26a0\ufe0fWithout protocol header (eg: https://, http://).")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Monitoring name"),(0,r.kt)("td",{parentName:"tr",align:null},"The name that identifies this monitoring, and the name needs to be unique.")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Port"),(0,r.kt)("td",{parentName:"tr",align:null},"The port provided by the IoTDB Metric interface, which is 9091 by default.")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Timeout"),(0,r.kt)("td",{parentName:"tr",align:null},"HTTP request query timeout")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Acquisition Interval"),(0,r.kt)("td",{parentName:"tr",align:null},"Interval time for monitoring periodic data collection, in seconds, the minimum interval that can be set is 30 seconds")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Whether to detect"),(0,r.kt)("td",{parentName:"tr",align:null},"Whether to detect and check the availability of monitoring before adding monitoring, and the operation of adding and modifying will continue after the detection is successful")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"Description Remarks"),(0,r.kt)("td",{parentName:"tr",align:null},"More remark information to identify and describe this monitoring, users can remark information here")))),(0,r.kt)("h3",{id:"collect-metrics"},"Collect metrics"),(0,r.kt)("h4",{id:"metric-collection-cluster_node_status"},"Metric collection: cluster_node_status"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"name"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Node name IP")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"status"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Node status, 1=online 2=offline")))),(0,r.kt)("h4",{id:"metric-collection-jvm_memory_committed_bytes"},"Metric collection: jvm_memory_committed_bytes"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"area"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"heap memory or nonheap memory")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"id"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"memory block")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"value"),(0,r.kt)("td",{parentName:"tr",align:null},"MB"),(0,r.kt)("td",{parentName:"tr",align:null},"The memory size currently requested by the JVM")))),(0,r.kt)("h4",{id:"metric-collection-jvm_memory_used_bytes"},"Metric collection: jvm_memory_used_bytes"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"area"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"heap memory or nonheap memory")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"id"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"memory block")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"value"),(0,r.kt)("td",{parentName:"tr",align:null},"MB"),(0,r.kt)("td",{parentName:"tr",align:null},"JVM used memory size")))),(0,r.kt)("h4",{id:"metric-collection-jvm_threads_states_threads"},"Metric collection: jvm_threads_states_threads"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"state"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"thread state")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"count"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"The number of threads corresponding to the thread state")))),(0,r.kt)("h4",{id:"index-collection-quantity-business-data"},"Index collection: quantity business data"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"name"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Business name timeSeries/storageGroup/device/deviceUsingTemplate")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"type"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"type total/normal/template/template")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"value"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"The current timeSeries/storageGroup/device/The number of devices that have activated the template")))),(0,r.kt)("h4",{id:"metric-collection-cache_hit-cache"},"Metric collection: cache_hit cache"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"name"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Cache name chunk/timeSeriesMeta/bloomFilter")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"value"),(0,r.kt)("td",{parentName:"tr",align:null},"%"),(0,r.kt)("td",{parentName:"tr",align:null},"chunk/timeSeriesMeta cache hit rate, bloomFilter interception rate")))),(0,r.kt)("h4",{id:"metric-collection-queue-task-queue"},"Metric collection: queue task queue"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"name"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Queue name compaction_inner/compaction_cross/flush")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"status"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"status running/waiting")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"value"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"Number of tasks at current time")))),(0,r.kt)("h4",{id:"metric-collection-thrift_connections"},"Metric collection: thrift_connections"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Metric Name"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Unit"),(0,r.kt)("th",{parentName:"tr",align:null},"Metric Help Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"name"),(0,r.kt)("td",{parentName:"tr",align:null},"None"),(0,r.kt)("td",{parentName:"tr",align:null},"name")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"connection"),(0,r.kt)("td",{parentName:"tr",align:null},"none"),(0,r.kt)("td",{parentName:"tr",align:null},"thrift current connection number")))))}u.isMDXComponent=!0}}]);