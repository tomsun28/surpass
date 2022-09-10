"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[4418],{4137:function(t,e,n){n.d(e,{Zo:function(){return c},kt:function(){return k}});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function i(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function o(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var p=a.createContext({}),m=function(t){var e=a.useContext(p),n=e;return t&&(n="function"==typeof t?t(e):i(i({},e),t)),n},c=function(t){var e=m(t.components);return a.createElement(p.Provider,{value:e},t.children)},u={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},d=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,p=t.parentName,c=o(t,["components","mdxType","originalType","parentName"]),d=m(n),k=r,s=d["".concat(p,".").concat(k)]||d[k]||u[k]||l;return n?a.createElement(s,i(i({ref:e},c),{},{components:n})):a.createElement(s,i({ref:e},c))}));function k(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,i=new Array(l);i[0]=d;var o={};for(var p in e)hasOwnProperty.call(e,p)&&(o[p]=e[p]);o.originalType=t,o.mdxType="string"==typeof t?t:r,i[1]=o;for(var m=2;m<l;m++)i[m]=n[m];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}d.displayName="MDXCreateElement"},549:function(t,e,n){n.r(e),n.d(e,{frontMatter:function(){return o},contentTitle:function(){return p},metadata:function(){return m},toc:function(){return c},default:function(){return d}});var a=n(7462),r=n(3366),l=(n(7294),n(4137)),i=["components"],o={id:"kafka",title:"Monitor\uff1aApache Kafka",sidebar_label:"Apache Kafka"},p=void 0,m={unversionedId:"help/kafka",id:"help/kafka",title:"Monitor\uff1aApache Kafka",description:"Collect and monitor the general performance Metrics of Apache Kafka.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/kafka.md",sourceDirName:"help",slug:"/help/kafka",permalink:"/en/docs/help/kafka",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/kafka.md",tags:[],version:"current",frontMatter:{id:"kafka",title:"Monitor\uff1aApache Kafka",sidebar_label:"Apache Kafka"},sidebar:"docs",previous:{title:"Zookeeper monitoring",permalink:"/en/docs/help/zookeeper"},next:{title:"Apache Tomcat",permalink:"/en/docs/help/tomcat"}},c=[{value:"Configuration parameter",id:"configuration-parameter",children:[],level:3},{value:"Collection Metrics",id:"collection-metrics",children:[{value:"Metrics Set\uff1aserver_info",id:"metrics-setserver_info",children:[],level:4},{value:"Metrics Set\uff1amemory_pool",id:"metrics-setmemory_pool",children:[],level:4},{value:"Metrics Set\uff1aactive_controller_count",id:"metrics-setactive_controller_count",children:[],level:4},{value:"Metrics Set\uff1abroker_partition_count",id:"metrics-setbroker_partition_count",children:[],level:4},{value:"Metrics Set\uff1abroker_leader_count",id:"metrics-setbroker_leader_count",children:[],level:4},{value:"Metrics Set\uff1abroker_handler_avg_percent",id:"metrics-setbroker_handler_avg_percent",children:[],level:4}],level:3},{value:"Kafka Enable JMX Protocol",id:"kafka-enable-jmx-protocol",children:[],level:3}],u={toc:c};function d(t){var e=t.components,n=(0,r.Z)(t,i);return(0,l.kt)("wrapper",(0,a.Z)({},u,n,{components:e,mdxType:"MDXLayout"}),(0,l.kt)("blockquote",null,(0,l.kt)("p",{parentName:"blockquote"},"Collect and monitor the general performance Metrics of Apache Kafka.")),(0,l.kt)("p",null,(0,l.kt)("strong",{parentName:"p"},"Protocol Use\uff1aJMX")),(0,l.kt)("h3",{id:"configuration-parameter"},"Configuration parameter"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Parameter name"),(0,l.kt)("th",{parentName:"tr",align:null},"Parameter help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring Host"),(0,l.kt)("td",{parentName:"tr",align:null},"Monitored IPV4, IPV6 or domain name. Note\u26a0\ufe0fWithout protocol header (eg: https://, http://)")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring name"),(0,l.kt)("td",{parentName:"tr",align:null},"Identify the name of this monitoring. The name needs to be unique")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Port"),(0,l.kt)("td",{parentName:"tr",align:null},"Port provided by JMX")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Username"),(0,l.kt)("td",{parentName:"tr",align:null},"JMX connection user name, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Password"),(0,l.kt)("td",{parentName:"tr",align:null},"JMX connection password, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Collection interval"),(0,l.kt)("td",{parentName:"tr",align:null},"Interval time of monitor periodic data collection, unit: second, and the minimum interval that can be set is 10 seconds")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect"),(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect and check the availability of monitoring before adding monitoring. Adding and modifying operations will continue only after the detection is successful")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Description remarks"),(0,l.kt)("td",{parentName:"tr",align:null},"For more information about identifying and describing this monitoring, users can note information here")))),(0,l.kt)("h3",{id:"collection-metrics"},"Collection Metrics"),(0,l.kt)("h4",{id:"metrics-setserver_info"},"Metrics Set\uff1aserver_info"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Version"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Kafka Version")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"StartTimeMs"),(0,l.kt)("td",{parentName:"tr",align:null},"ms"),(0,l.kt)("td",{parentName:"tr",align:null},"Start Time")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"CommitId"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"Version Commit ID")))),(0,l.kt)("h4",{id:"metrics-setmemory_pool"},"Metrics Set\uff1amemory_pool"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"name"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"metrics name")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"committed"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"total size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"init"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"init size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"max"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"max size")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"used"),(0,l.kt)("td",{parentName:"tr",align:null},"kb"),(0,l.kt)("td",{parentName:"tr",align:null},"used size")))),(0,l.kt)("h4",{id:"metrics-setactive_controller_count"},"Metrics Set\uff1aactive_controller_count"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Value"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"server active controller count")))),(0,l.kt)("h4",{id:"metrics-setbroker_partition_count"},"Metrics Set\uff1abroker_partition_count"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Value"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"broker partition count")))),(0,l.kt)("h4",{id:"metrics-setbroker_leader_count"},"Metrics Set\uff1abroker_leader_count"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Value"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"broker leader count")))),(0,l.kt)("h4",{id:"metrics-setbroker_handler_avg_percent"},"Metrics Set\uff1abroker_handler_avg_percent"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"EventType"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"event type")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"RateUnit"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"rate unit")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Count"),(0,l.kt)("td",{parentName:"tr",align:null}),(0,l.kt)("td",{parentName:"tr",align:null},"percent count")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"OneMinuteRate"),(0,l.kt)("td",{parentName:"tr",align:null},"%"),(0,l.kt)("td",{parentName:"tr",align:null},"One Minute Rate")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"FiveMinuteRate"),(0,l.kt)("td",{parentName:"tr",align:null},"%"),(0,l.kt)("td",{parentName:"tr",align:null},"Five Minute Rate")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"MeanRate"),(0,l.kt)("td",{parentName:"tr",align:null},"%"),(0,l.kt)("td",{parentName:"tr",align:null},"Mean Rate")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"FifteenMinuteRate"),(0,l.kt)("td",{parentName:"tr",align:null},"%"),(0,l.kt)("td",{parentName:"tr",align:null},"Fifteen Minute Rate")))),(0,l.kt)("h3",{id:"kafka-enable-jmx-protocol"},"Kafka Enable JMX Protocol"),(0,l.kt)("ol",null,(0,l.kt)("li",{parentName:"ol"},(0,l.kt)("p",{parentName:"li"},"Install Kafka")),(0,l.kt)("li",{parentName:"ol"},(0,l.kt)("p",{parentName:"li"},"Modify ",(0,l.kt)("inlineCode",{parentName:"p"},"kafka-server-start.sh")," "))),(0,l.kt)("p",null,"Append content in kafka-server-start.sh, Attention Replace Port And IP."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-shell"},'export JMX_PORT=9999;\nexport KAFKA_JMX_OPTS="-Djava.rmi.server.hostname=ip\u5730\u5740 -Dcom.sun.management.jmxremote.rmi.port=9999 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false";\n\n# Already Has \nexec $base_dir/kafka-run-class.sh $EXTRA_ARGS kafka.Kafka "$@"\n')))}d.isMDXComponent=!0}}]);