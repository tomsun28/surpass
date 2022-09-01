"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[7817],{4137:function(t,e,n){n.d(e,{Zo:function(){return p},kt:function(){return u}});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function i(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function o(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var d=a.createContext({}),m=function(t){var e=a.useContext(d),n=e;return t&&(n="function"==typeof t?t(e):i(i({},e),t)),n},p=function(t){var e=m(t.components);return a.createElement(d.Provider,{value:e},t.children)},s={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},c=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,d=t.parentName,p=o(t,["components","mdxType","originalType","parentName"]),c=m(n),u=r,k=c["".concat(d,".").concat(u)]||c[u]||s[u]||l;return n?a.createElement(k,i(i({ref:e},p),{},{components:n})):a.createElement(k,i({ref:e},p))}));function u(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,i=new Array(l);i[0]=c;var o={};for(var d in e)hasOwnProperty.call(e,d)&&(o[d]=e[d]);o.originalType=t,o.mdxType="string"==typeof t?t:r,i[1]=o;for(var m=2;m<l;m++)i[m]=n[m];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}c.displayName="MDXCreateElement"},6684:function(t,e,n){n.r(e),n.d(e,{frontMatter:function(){return o},contentTitle:function(){return d},metadata:function(){return m},toc:function(){return p},default:function(){return c}});var a=n(3117),r=n(102),l=(n(7294),n(4137)),i=["components"],o={id:"postgresql",title:"Monitoring\uff1aPostgreSQL database monitoring",sidebar_label:"PostgreSQL database"},d=void 0,m={unversionedId:"help/postgresql",id:"help/postgresql",title:"Monitoring\uff1aPostgreSQL database monitoring",description:"Collect and monitor the general performance Metrics of PostgreSQL database. Support PostgreSQL 10+.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/postgresql.md",sourceDirName:"help",slug:"/help/postgresql",permalink:"/en/docs/help/postgresql",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/postgresql.md",tags:[],version:"current",frontMatter:{id:"postgresql",title:"Monitoring\uff1aPostgreSQL database monitoring",sidebar_label:"PostgreSQL database"},sidebar:"docs",previous:{title:"MariaDB database",permalink:"/en/docs/help/mariadb"},next:{title:"SqlServer database",permalink:"/en/docs/help/sqlserver"}},p=[{value:"Configuration parameter",id:"configuration-parameter",children:[],level:3},{value:"Collection Metric",id:"collection-metric",children:[{value:"Metric set\uff1abasic",id:"metric-setbasic",children:[],level:4},{value:"Metric set\uff1astate",id:"metric-setstate",children:[],level:4},{value:"Metric set\uff1aactivity",id:"metric-setactivity",children:[],level:4}],level:3}],s={toc:p};function c(t){var e=t.components,n=(0,r.Z)(t,i);return(0,l.kt)("wrapper",(0,a.Z)({},s,n,{components:e,mdxType:"MDXLayout"}),(0,l.kt)("blockquote",null,(0,l.kt)("p",{parentName:"blockquote"},"Collect and monitor the general performance Metrics of PostgreSQL database. Support PostgreSQL 10+.")),(0,l.kt)("h3",{id:"configuration-parameter"},"Configuration parameter"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Parameter name"),(0,l.kt)("th",{parentName:"tr",align:null},"Parameter help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring Host"),(0,l.kt)("td",{parentName:"tr",align:null},"Monitored IPV4, IPV6 or domain name. Note\u26a0\ufe0fWithout protocol header (eg: https://, http://)")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Monitoring name"),(0,l.kt)("td",{parentName:"tr",align:null},"Identify the name of this monitoring. The name needs to be unique")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Port"),(0,l.kt)("td",{parentName:"tr",align:null},"Port provided by the database. The default is 5432")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Query timeout"),(0,l.kt)("td",{parentName:"tr",align:null},"Set the timeout time when SQL query does not respond to data, unit: ms, default: 3000ms")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Database name"),(0,l.kt)("td",{parentName:"tr",align:null},"Database instance name, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Username"),(0,l.kt)("td",{parentName:"tr",align:null},"Database connection user name, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Password"),(0,l.kt)("td",{parentName:"tr",align:null},"Database connection password, optional")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"URL"),(0,l.kt)("td",{parentName:"tr",align:null},"Database connection URL\uff0coptional\uff0cIf configured, the database name, user name, password and other parameters in the URL will overwrite the above configured parameters")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Collection interval"),(0,l.kt)("td",{parentName:"tr",align:null},"Interval time of monitor periodic data collection, unit: second, and the minimum interval that can be set is 10 seconds")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect"),(0,l.kt)("td",{parentName:"tr",align:null},"Whether to detect and check the availability of monitoring before adding monitoring. Adding and modifying operations will continue only after the detection is successful")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"Description remarks"),(0,l.kt)("td",{parentName:"tr",align:null},"For more information about identifying and describing this monitoring, users can note information here")))),(0,l.kt)("h3",{id:"collection-metric"},"Collection Metric"),(0,l.kt)("h4",{id:"metric-setbasic"},"Metric set\uff1abasic"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"server_version"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"Version number of the database server")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"port"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"Database server exposure service port")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"server_encoding"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"Character set encoding of database server")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"data_directory"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"Database storage data disk address")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"max_connections"),(0,l.kt)("td",{parentName:"tr",align:null},"connections"),(0,l.kt)("td",{parentName:"tr",align:null},"Database maximum connections")))),(0,l.kt)("h4",{id:"metric-setstate"},"Metric set\uff1astate"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"name"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"Database name, or share-object is a shared object")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"conflicts"),(0,l.kt)("td",{parentName:"tr",align:null},"times"),(0,l.kt)("td",{parentName:"tr",align:null},"The number of queries canceled in the database due to a conflict with recovery")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"deadlocks"),(0,l.kt)("td",{parentName:"tr",align:null},"number"),(0,l.kt)("td",{parentName:"tr",align:null},"Number of deadlocks detected in the database")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"blks_read"),(0,l.kt)("td",{parentName:"tr",align:null},"times"),(0,l.kt)("td",{parentName:"tr",align:null},"The number of disk blocks read in the database")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"blks_hit"),(0,l.kt)("td",{parentName:"tr",align:null},"times"),(0,l.kt)("td",{parentName:"tr",align:null},"Times the disk block has been found to be in the buffer, so there is no need to read it once (This only includes hits in the PostgreSQL buffer, not in the operating system file system buffer)")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"blk_read_time"),(0,l.kt)("td",{parentName:"tr",align:null},"ms"),(0,l.kt)("td",{parentName:"tr",align:null},"Time spent by the backend reading data file blocks in the database")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"blk_write_time"),(0,l.kt)("td",{parentName:"tr",align:null},"ms"),(0,l.kt)("td",{parentName:"tr",align:null},"Time spent by the backend writing data file blocks in the database")),(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"stats_reset"),(0,l.kt)("td",{parentName:"tr",align:null},"none"),(0,l.kt)("td",{parentName:"tr",align:null},"The last time these statistics were reset")))),(0,l.kt)("h4",{id:"metric-setactivity"},"Metric set\uff1aactivity"),(0,l.kt)("table",null,(0,l.kt)("thead",{parentName:"table"},(0,l.kt)("tr",{parentName:"thead"},(0,l.kt)("th",{parentName:"tr",align:null},"Metric name"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric unit"),(0,l.kt)("th",{parentName:"tr",align:null},"Metric help description"))),(0,l.kt)("tbody",{parentName:"table"},(0,l.kt)("tr",{parentName:"tbody"},(0,l.kt)("td",{parentName:"tr",align:null},"running"),(0,l.kt)("td",{parentName:"tr",align:null},"connections"),(0,l.kt)("td",{parentName:"tr",align:null},"Number of current client connections")))))}c.isMDXComponent=!0}}]);