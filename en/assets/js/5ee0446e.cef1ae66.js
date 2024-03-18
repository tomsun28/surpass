"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[6628],{4137:(t,e,a)=>{a.d(e,{Zo:()=>o,kt:()=>N});var n=a(7294);function r(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}function l(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,n)}return a}function i(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?l(Object(a),!0).forEach((function(e){r(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):l(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}function p(t,e){if(null==t)return{};var a,n,r=function(t,e){if(null==t)return{};var a,n,r={},l=Object.keys(t);for(n=0;n<l.length;n++)a=l[n],e.indexOf(a)>=0||(r[a]=t[a]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(n=0;n<l.length;n++)a=l[n],e.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(t,a)&&(r[a]=t[a])}return r}var u=n.createContext({}),m=function(t){var e=n.useContext(u),a=e;return t&&(a="function"==typeof t?t(e):i(i({},e),t)),a},o=function(t){var e=m(t.components);return n.createElement(u.Provider,{value:e},t.children)},k="mdxType",d={inlineCode:"code",wrapper:function(t){var e=t.children;return n.createElement(n.Fragment,{},e)}},s=n.forwardRef((function(t,e){var a=t.components,r=t.mdxType,l=t.originalType,u=t.parentName,o=p(t,["components","mdxType","originalType","parentName"]),k=m(a),s=r,N=k["".concat(u,".").concat(s)]||k[s]||d[s]||l;return a?n.createElement(N,i(i({ref:e},o),{},{components:a})):n.createElement(N,i({ref:e},o))}));function N(t,e){var a=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=a.length,i=new Array(l);i[0]=s;var p={};for(var u in e)hasOwnProperty.call(e,u)&&(p[u]=e[u]);p.originalType=t,p[k]="string"==typeof t?t:r,i[1]=p;for(var m=2;m<l;m++)i[m]=a[m];return n.createElement.apply(null,i)}return n.createElement.apply(null,a)}s.displayName="MDXCreateElement"},6900:(t,e,a)=>{a.r(e),a.d(e,{assets:()=>u,contentTitle:()=>i,default:()=>d,frontMatter:()=>l,metadata:()=>p,toc:()=>m});var n=a(7462),r=(a(7294),a(4137));const l={title:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7 HertzBeat \u76d1\u63a7 Linux \u64cd\u4f5c\u7cfb\u7edf",author:"tom",author_title:"tom",author_url:"https://github.com/tomsun28",author_image_url:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4",tags:["opensource","practice"],keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7","Linux\u76d1\u63a7"]},i=void 0,p={permalink:"/en/blog/2023/02/15/monitor-linux",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/blog/2023-02-15-monitor-linux.md",source:"@site/blog/2023-02-15-monitor-linux.md",title:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7 HertzBeat \u76d1\u63a7 Linux \u64cd\u4f5c\u7cfb\u7edf",description:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf HertzBeat \u5bf9 Linux \u64cd\u4f5c\u7cfb\u7edf\u7684\u76d1\u63a7\u544a\u8b66\u5b9e\u8df5\uff0c5\u5206\u949f\u641e\u5b9a\uff01",date:"2023-02-15T00:00:00.000Z",formattedDate:"February 15, 2023",tags:[{label:"opensource",permalink:"/en/blog/tags/opensource"},{label:"practice",permalink:"/en/blog/tags/practice"}],readingTime:9.79,hasTruncateMarker:!1,authors:[{name:"tom",title:"tom",url:"https://github.com/tomsun28",imageURL:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4"}],frontMatter:{title:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7 HertzBeat \u76d1\u63a7 Linux \u64cd\u4f5c\u7cfb\u7edf",author:"tom",author_title:"tom",author_url:"https://github.com/tomsun28",author_image_url:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4",tags:["opensource","practice"],keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7","Linux\u76d1\u63a7"]},nextItem:{title:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf HertzBeat \u5bf9 Mysql \u6570\u636e\u5e93\u76d1\u63a7\u544a\u8b66\u5b9e\u8df5",permalink:"/en/blog/2023/02/11/monitor-mysql"}},u={authorsImageUrls:[void 0]},m=[{value:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf HertzBeat \u5bf9 Linux \u64cd\u4f5c\u7cfb\u7edf\u7684\u76d1\u63a7\u544a\u8b66\u5b9e\u8df5\uff0c5\u5206\u949f\u641e\u5b9a\uff01",id:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf-hertzbeat-\u5bf9-linux-\u64cd\u4f5c\u7cfb\u7edf\u7684\u76d1\u63a7\u544a\u8b66\u5b9e\u8df55\u5206\u949f\u641e\u5b9a",level:2},{value:"HertzBeat \u4ecb\u7ecd",id:"hertzbeat-\u4ecb\u7ecd",level:3},{value:"\u5728 HertzBeat 5\u5206\u949f\u641e\u5b9a\u5bf9 Linux \u7684\u76d1\u63a7",id:"\u5728-hertzbeat-5\u5206\u949f\u641e\u5b9a\u5bf9-linux-\u7684\u76d1\u63a7",level:3},{value:"\u64cd\u4f5c\u524d\u63d0\uff0c\u60a8\u5df2\u62e5\u6709 Linux \u73af\u5883\u548c HertzBeat \u73af\u5883\u3002",id:"\u64cd\u4f5c\u524d\u63d0\u60a8\u5df2\u62e5\u6709-linux-\u73af\u5883\u548c-hertzbeat-\u73af\u5883",level:4},{value:"\u5728\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf HertzBeat \u76d1\u63a7\u9875\u9762\u6dfb\u52a0\u5bf9 Linux \u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7",id:"\u5728\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf-hertzbeat-\u76d1\u63a7\u9875\u9762\u6dfb\u52a0\u5bf9-linux-\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7",level:4},{value:"Linux \u91c7\u96c6\u6307\u6807",id:"linux-\u91c7\u96c6\u6307\u6807",level:3},{value:"\u6307\u6807\u96c6\u5408\uff1abasic",id:"\u6307\u6807\u96c6\u5408basic",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1acpu",id:"\u6307\u6807\u96c6\u5408cpu",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1amemory",id:"\u6307\u6807\u96c6\u5408memory",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adisk",id:"\u6307\u6807\u96c6\u5408disk",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1ainterface",id:"\u6307\u6807\u96c6\u5408interface",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adisk_free",id:"\u6307\u6807\u96c6\u5408disk_free",level:4},{value:"\u4e09. \u5728 HertzBeat \u7cfb\u7edf\u6dfb\u52a0 Linux \u6307\u6807\u9608\u503c\u544a\u8b66",id:"\u4e09-\u5728-hertzbeat-\u7cfb\u7edf\u6dfb\u52a0-linux-\u6307\u6807\u9608\u503c\u544a\u8b66",level:4},{value:"\u5b8c\u6bd5\uff0c\u73b0\u5728\u5750\u7b49\u544a\u8b66\u6d88\u606f\u8fc7\u6765\u5566\u3002\u53ee\u53ee\u53ee\u53ee",id:"\u5b8c\u6bd5\u73b0\u5728\u5750\u7b49\u544a\u8b66\u6d88\u606f\u8fc7\u6765\u5566\u53ee\u53ee\u53ee\u53ee",level:3},{value:"\u5c0f\u7ed3",id:"\u5c0f\u7ed3",level:2},{value:"What is HertzBeat?",id:"what-is-hertzbeat",level:2},{value:"\u26c4 Supported",id:"-supported",level:2}],o={toc:m},k="wrapper";function d(t){let{components:e,...l}=t;return(0,r.kt)(k,(0,n.Z)({},o,l,{components:e,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf-hertzbeat-\u5bf9-linux-\u64cd\u4f5c\u7cfb\u7edf\u7684\u76d1\u63a7\u544a\u8b66\u5b9e\u8df55\u5206\u949f\u641e\u5b9a"},"\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf HertzBeat \u5bf9 Linux \u64cd\u4f5c\u7cfb\u7edf\u7684\u76d1\u63a7\u544a\u8b66\u5b9e\u8df5\uff0c5\u5206\u949f\u641e\u5b9a\uff01"),(0,r.kt)("h3",{id:"hertzbeat-\u4ecb\u7ecd"},"HertzBeat \u4ecb\u7ecd"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"HertzBeat \u662f\u4e00\u6b3e\u5f00\u6e90\uff0c\u6613\u7528\u53cb\u597d\u7684\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf\uff0c\u65e0\u9700Agent\uff0c\u62e5\u6709\u5f3a\u5927\u81ea\u5b9a\u4e49\u76d1\u63a7\u80fd\u529b\u3002    ")),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"\u96c6",(0,r.kt)("strong",{parentName:"li"},"\u76d1\u63a7-\u544a\u8b66-\u901a\u77e5\u4e3a\u4e00\u4f53"),"\uff0c\u652f\u6301\u5bf9\u5e94\u7528\u670d\u52a1\uff0c\u6570\u636e\u5e93\uff0c\u64cd\u4f5c\u7cfb\u7edf\uff0c\u4e2d\u95f4\u4ef6\uff0c\u4e91\u539f\u751f\u7b49\u76d1\u63a7\uff0c\u9608\u503c\u544a\u8b66\uff0c\u544a\u8b66\u901a\u77e5(\u90ae\u4ef6\u5fae\u4fe1\u9489\u9489\u98de\u4e66\u77ed\u4fe1 Slack Discord Telegram)\u3002    "),(0,r.kt)("li",{parentName:"ul"},"\u5176\u5c06Http,Jmx,Ssh,Snmp,Jdbc\u7b49\u534f\u8bae\u89c4\u8303\u53ef\u914d\u7f6e\u5316\uff0c\u53ea\u9700\u914d\u7f6eYML\u5c31\u80fd\u4f7f\u7528\u8fd9\u4e9b\u534f\u8bae\u53bb\u81ea\u5b9a\u4e49\u91c7\u96c6\u4efb\u4f55\u60a8\u60f3\u8981\u91c7\u96c6\u7684\u6307\u6807\u3002\u60a8\u76f8\u4fe1\u53ea\u9700\u914d\u7f6eYML\u5c31\u80fd\u7acb\u523b\u9002\u914d\u4e00\u4e2aK8s\u6216Docker\u7b49\u65b0\u7684\u76d1\u63a7\u7c7b\u578b\u5417\uff1f   "),(0,r.kt)("li",{parentName:"ul"},"HertzBeat \u7684\u5f3a\u5927\u81ea\u5b9a\u4e49\uff0c\u591a\u7c7b\u578b\u652f\u6301\uff0c\u6613\u6269\u5c55\uff0c\u4f4e\u8026\u5408\uff0c\u5e0c\u671b\u80fd\u5e2e\u52a9\u5f00\u53d1\u8005\u548c\u4e2d\u5c0f\u56e2\u961f\u5feb\u901f\u642d\u5efa\u81ea\u6709\u76d1\u63a7\u7cfb\u7edf\u3002  ")),(0,r.kt)("p",null,"Github: ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/dromara/hertzbeat"},"https://github.com/dromara/hertzbeat")," "),(0,r.kt)("h3",{id:"\u5728-hertzbeat-5\u5206\u949f\u641e\u5b9a\u5bf9-linux-\u7684\u76d1\u63a7"},"\u5728 HertzBeat 5\u5206\u949f\u641e\u5b9a\u5bf9 Linux \u7684\u76d1\u63a7"),(0,r.kt)("h4",{id:"\u64cd\u4f5c\u524d\u63d0\u60a8\u5df2\u62e5\u6709-linux-\u73af\u5883\u548c-hertzbeat-\u73af\u5883"},"\u64cd\u4f5c\u524d\u63d0\uff0c\u60a8\u5df2\u62e5\u6709 Linux \u73af\u5883\u548c HertzBeat \u73af\u5883\u3002"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"HertzBeat ",(0,r.kt)("a",{parentName:"li",href:"https://hertzbeat.com/docs/start/docker-deploy"},"\u5b89\u88c5\u90e8\u7f72\u6587\u6863"))),(0,r.kt)("h4",{id:"\u5728\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf-hertzbeat-\u76d1\u63a7\u9875\u9762\u6dfb\u52a0\u5bf9-linux-\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7"},"\u5728\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf HertzBeat \u76d1\u63a7\u9875\u9762\u6dfb\u52a0\u5bf9 Linux \u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"\u70b9\u51fb\u65b0\u589e Linux \u76d1\u63a7  ")),(0,r.kt)("p",null,"\u8def\u5f84\uff1a\u83dc\u5355 -> \u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7 -> Linux\u64cd\u4f5c\u7cfb\u7edf -> \u65b0\u589eLinux\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7  "),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(3862).Z,width:"4064",height:"2166"})),(0,r.kt)("ol",{start:2},(0,r.kt)("li",{parentName:"ol"},"\u914d\u7f6e\u65b0\u589e\u76d1\u63a7 Linux \u6240\u9700\u53c2\u6570   ")),(0,r.kt)("p",null,"\u5728\u76d1\u63a7\u9875\u9762\u586b\u5199 Linux ",(0,r.kt)("strong",{parentName:"p"},"\u5bf9\u7aefIP"),"\uff0c",(0,r.kt)("strong",{parentName:"p"},"SSH\u7aef\u53e3"),"(\u9ed8\u8ba422)\uff0c",(0,r.kt)("strong",{parentName:"p"},"\u8d26\u6237\u5bc6\u7801\u7b49"),"\uff0c\u6700\u540e\u70b9\u51fb\u786e\u5b9a\u6dfb\u52a0\u5373\u53ef\u3002",(0,r.kt)("br",{parentName:"p"}),"\n","\u5176\u4ed6\u53c2\u6570\u5982",(0,r.kt)("strong",{parentName:"p"},"\u91c7\u96c6\u95f4\u9694"),"\uff0c",(0,r.kt)("strong",{parentName:"p"},"\u8d85\u65f6\u65f6\u95f4"),"\u7b49\u53ef\u4ee5\u53c2\u8003\u5e2e\u52a9\u6587\u6863 ",(0,r.kt)("a",{parentName:"p",href:"https://hertzbeat.com/docs/help/mysql/"},"https://hertzbeat.com/docs/help/mysql/"),"   "),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(8986).Z,width:"4064",height:"2166"}),"    "),(0,r.kt)("ol",{start:3},(0,r.kt)("li",{parentName:"ol"},"\u5b8c\u6210\u2705,\u73b0\u5728\u6211\u4eec\u5df2\u7ecf\u6dfb\u52a0\u597d\u5bf9 Linux \u7684\u76d1\u63a7\u4e86\uff0c\u67e5\u770b\u76d1\u63a7\u5217\u8868\u5373\u53ef\u770b\u5230\u6211\u4eec\u7684\u6dfb\u52a0\u9879\u3002  ")),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(8108).Z,width:"4064",height:"2166"}),"  "),(0,r.kt)("ol",{start:4},(0,r.kt)("li",{parentName:"ol"},"\u70b9\u51fb\u76d1\u63a7\u5217\u8868\u9879\u7684",(0,r.kt)("strong",{parentName:"li"},"\u64cd\u4f5c"),"->",(0,r.kt)("strong",{parentName:"li"},"\u76d1\u63a7\u8be6\u60c5\u56fe\u6807")," \u5373\u53ef\u6d4f\u89c8 Linux \u7684\u5b9e\u65f6\u76d1\u63a7\u6307\u6807\u6570\u636e\u3002  ")),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(6706).Z,width:"4064",height:"2166"}),"  "),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(4275).Z,width:"4064",height:"2166"})),(0,r.kt)("ol",{start:5},(0,r.kt)("li",{parentName:"ol"},"\u70b9\u51fb",(0,r.kt)("strong",{parentName:"li"},"\u76d1\u63a7\u5386\u53f2\u8be6\u60c5TAB")," \u5373\u53ef\u6d4f\u89c8 Linux \u7684\u5386\u53f2\u76d1\u63a7\u6307\u6807\u6570\u636e\u56fe\u8868\ud83d\udcc8\u3002  ")),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(7597).Z,width:"4064",height:"2166"})),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(2678).Z,width:"4064",height:"2166"})),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"DONE\uff01\u5b8c\u6210\u5566\uff01\u4e0d\u9700\u8981\u6211\u4eec\u53bb\u90e8\u7f72agent\u6216\u8005\u5404\u79cd\u7e41\u7410\u64cd\u4f5c\uff0c\u662f\u4e0d\u662f\u5f88\u7b80\u5355")),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("strong",{parentName:"li"},"\u53ea\u9700\u4e00\u6b65\u5728 HertzBeat \u76d1\u63a7\u9875\u9762\u914d\u7f6eIP\u7aef\u53e3\u8d26\u6237\u5bc6\u7801\u6dfb\u52a0 Linux \u76d1\u63a7\u5373\u53ef"),"         ")),(0,r.kt)("h3",{id:"linux-\u91c7\u96c6\u6307\u6807"},"Linux \u91c7\u96c6\u6307\u6807"),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408basic"},"\u6307\u6807\u96c6\u5408\uff1abasic"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"hostname"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u4e3b\u673a\u540d\u79f0")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"version"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u64cd\u4f5c\u7cfb\u7edf\u7248\u672c")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"uptime"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7cfb\u7edf\u8fd0\u884c\u65f6\u95f4")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408cpu"},"\u6307\u6807\u96c6\u5408\uff1acpu"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"info"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"CPU\u578b\u53f7")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"cores"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6838\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"CPU\u5185\u6838\u6570\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"interrupt"),(0,r.kt)("td",{parentName:"tr",align:null},"\u4e2a\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"CPU\u4e2d\u65ad\u6570\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"load"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"CPU\u6700\u8fd11/5/15\u5206\u949f\u7684\u5e73\u5747\u8d1f\u8f7d")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"context_switch"),(0,r.kt)("td",{parentName:"tr",align:null},"\u4e2a\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u4e0a\u4e0b\u6587\u5207\u6362\u6570\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"usage"),(0,r.kt)("td",{parentName:"tr",align:null},"%"),(0,r.kt)("td",{parentName:"tr",align:null},"CPU\u4f7f\u7528\u7387")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408memory"},"\u6307\u6807\u96c6\u5408\uff1amemory"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"total"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u603b\u5185\u5b58\u5bb9\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"used"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7528\u6237\u7a0b\u5e8f\u5185\u5b58\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"free"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7a7a\u95f2\u5185\u5b58\u5bb9\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"buff_cache"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7f13\u5b58\u5360\u7528\u5185\u5b58")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"available"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5269\u4f59\u53ef\u7528\u5185\u5b58\u5bb9\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"usage"),(0,r.kt)("td",{parentName:"tr",align:null},"%"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5185\u5b58\u4f7f\u7528\u7387")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408disk"},"\u6307\u6807\u96c6\u5408\uff1adisk"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"disk_num"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5757\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"\u78c1\u76d8\u603b\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"partition_num"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u533a\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u533a\u603b\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"block_write"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5757\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5199\u5165\u78c1\u76d8\u7684\u603b\u5757\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"block_read"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5757\u6570"),(0,r.kt)("td",{parentName:"tr",align:null},"\u4ece\u78c1\u76d8\u8bfb\u51fa\u7684\u5757\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"write_rate"),(0,r.kt)("td",{parentName:"tr",align:null},"iops"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6bcf\u79d2\u5199\u78c1\u76d8\u5757\u7684\u901f\u7387")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408interface"},"\u6307\u6807\u96c6\u5408\uff1ainterface"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"interface_name"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7f51\u5361\u540d\u79f0")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"receive_bytes"),(0,r.kt)("td",{parentName:"tr",align:null},"byte"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5165\u7ad9\u6570\u636e\u6d41\u91cf(bytes)")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"transmit_bytes"),(0,r.kt)("td",{parentName:"tr",align:null},"byte"),(0,r.kt)("td",{parentName:"tr",align:null},"\u51fa\u7ad9\u6570\u636e\u6d41\u91cf(bytes)")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6\u5408disk_free"},"\u6307\u6807\u96c6\u5408\uff1adisk_free"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"filesystem"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6587\u4ef6\u7cfb\u7edf\u7684\u540d\u79f0")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"used"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5df2\u4f7f\u7528\u78c1\u76d8\u5927\u5c0f")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"available"),(0,r.kt)("td",{parentName:"tr",align:null},"Mb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u53ef\u7528\u78c1\u76d8\u5927\u5c0f")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"usage"),(0,r.kt)("td",{parentName:"tr",align:null},"%"),(0,r.kt)("td",{parentName:"tr",align:null},"\u4f7f\u7528\u7387")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"mounted"),(0,r.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6302\u8f7d\u70b9\u76ee\u5f55")))),(0,r.kt)("admonition",{type:"tip"},(0,r.kt)("p",{parentName:"admonition"},"\u901a\u8fc7\u4e0a\u9762\u6211\u4eec\u5c31\u5b8c\u6210\u4e86\u5bf9 Linux \u7684\u76d1\u63a7\uff0c\u6211\u4eec\u53ef\u4ee5\u5728 HertzBeat \u968f\u65f6\u67e5\u770bLinux\u7684\u5404\u79cd\u6307\u6807\u72b6\u6001\u548c\u53ef\u7528\u6027\u3002",(0,r.kt)("br",{parentName:"p"}),"\n","\u5f53\u7136\u4e0d\u53ef\u80fd\u4eba\u5de5\u4e00\u76f4\u5b9e\u65f6\u67e5\u770b\u6307\u6807\uff0c\u76d1\u63a7\u5f80\u5f80\u4f34\u968f\u7740\u544a\u8b66\u9608\u503c\uff0c\u5f53 Linux \u7684\u6027\u80fd\u6307\u6807\u8d85\u51fa\u6211\u4eec\u7684\u9608\u503c\u6216Linux\u672c\u8eab\u5f02\u5e38\u65f6\uff0c\u80fd\u53ca\u65f6\u7684\u901a\u77e5\u5230\u6211\u4eec\u5bf9\u5e94\u7684\u8d1f\u8d23\u4eba\uff0c\u8d1f\u8d23\u4eba\u6536\u5230\u901a\u77e5\u5904\u7406\uff0c\u8fd9\u6837\u624d\u662f\u4e00\u4e2a\u5b8c\u6574\u7684\u76d1\u63a7\u544a\u8b66\u6d41\u7a0b\u3002")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"\u63a5\u4e0b\u6765\u6211\u4eec\u5c31\u6765\u4e00\u6b65\u4e00\u6b65\u6f14\u793a\u5982\u4f55\u914d\u7f6e HertzBeat \u7cfb\u7edf\u91cc\u7684\u9608\u503c\u544a\u8b66\u901a\u77e5\uff0c\u5f53 Linux \u7684\u6307\u6807\u5f02\u5e38\u65f6\uff0c\u53ca\u65f6\u901a\u77e5\u7ed9\u6211\u4eec"),"     "),(0,r.kt)("h4",{id:"\u4e09-\u5728-hertzbeat-\u7cfb\u7edf\u6dfb\u52a0-linux-\u6307\u6807\u9608\u503c\u544a\u8b66"},"\u4e09. \u5728 HertzBeat \u7cfb\u7edf\u6dfb\u52a0 Linux \u6307\u6807\u9608\u503c\u544a\u8b66"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"\u5bf9\u67d0\u4e2a\u91cd\u8981\u6307\u6807\u914d\u7f6e\u544a\u8b66\u9608\u503c     ")),(0,r.kt)("p",null,"\u8def\u5f84\uff1a\u83dc\u5355 -> \u9608\u503c\u89c4\u5219 -> \u65b0\u589e\u9608\u503c  "),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"\u9009\u62e9\u914d\u7f6e\u7684\u6307\u6807\u5bf9\u8c61\uff0cLinux \u76d1\u63a7\u4e3b\u8981\u662fcpu \u5185\u5b58 \u78c1\u76d8 \u7f51\u7edc\u6027\u80fd\u7b49\u76f8\u5173\u6307\u6807\uff0c\u6211\u4eec\u4e3e\u4f8b\u5bf9 ",(0,r.kt)("inlineCode",{parentName:"li"},"CPU\u5229\u7528\u7387")," ",(0,r.kt)("inlineCode",{parentName:"li"},"cpu")," -> ",(0,r.kt)("inlineCode",{parentName:"li"},"usage")," \u8fd9\u4e2a\u6307\u6807\u8fdb\u884c\u9608\u503c\u8bbe\u7f6e\uff0c \u5f53Linux cpu\u5229\u7528\u7387\u5927\u4e8e90%\u65f6\u53d1\u51fa\u544a\u8b66\u3002       "),(0,r.kt)("li",{parentName:"ul"},"\u8fd9\u91cc\u6211\u4eec\u5c31\u914d\u7f6e\u5f53\u6b64\u6307\u6807",(0,r.kt)("inlineCode",{parentName:"li"},"cpu")," \u7684 ",(0,r.kt)("inlineCode",{parentName:"li"},"usage>90")," \u65f6\u53d1\u51fa\u544a\u8b66\uff0c\u544a\u8b66\u7ea7\u522b\u4e3a",(0,r.kt)("strong",{parentName:"li"},"\u8b66\u544a\u544a\u8b66"),"\uff0c\u4e09\u6b21\u5373\u89e6\u53d1\uff0c\u5177\u4f53\u5982\u4e0b\u56fe\u3002  ")),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(7972).Z,width:"4064",height:"2166"}),"     "),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(9996).Z,width:"4064",height:"2166"})),(0,r.kt)("ol",{start:2},(0,r.kt)("li",{parentName:"ol"},"\u65b0\u589e\u6d88\u606f\u901a\u77e5\u63a5\u6536\u4eba")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"\u914d\u7f6e\u63a5\u6536\u4eba\uff0c\u8ba9\u544a\u8b66\u6d88\u606f\u77e5\u9053\u8981\u53d1\u7ed9\u8c01\uff0c\u7528\u4ec0\u4e48\u65b9\u5f0f\u53d1\u3002  ")),(0,r.kt)("p",null,"\u8def\u5f84\uff1a\u83dc\u5355 -> \u544a\u8b66\u901a\u77e5 -> \u544a\u8b66\u63a5\u6536\u4eba -> \u65b0\u589e\u63a5\u6536\u4eba  "),(0,r.kt)("p",null,"\u6d88\u606f\u901a\u77e5\u65b9\u5f0f\u652f\u6301 ",(0,r.kt)("strong",{parentName:"p"},"\u90ae\u4ef6\uff0c\u9489\u9489\uff0c\u4f01\u4e1a\u5fae\u4fe1\uff0c\u98de\u4e66\uff0cWebHook\uff0c\u77ed\u4fe1"),"\u7b49\uff0c\u6211\u4eec\u8fd9\u91cc\u4ee5\u5e38\u7528\u7684\u9489\u9489\u4e3a\u4f8b\u3002  "),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"\u53c2\u7167\u6b64",(0,r.kt)("a",{parentName:"li",href:"https://hertzbeat.com/docs/help/alert_dingtalk"},"\u5e2e\u52a9\u6587\u6863")," ",(0,r.kt)("a",{parentName:"li",href:"https://hertzbeat.com/docs/help/alert_dingtalk"},"https://hertzbeat.com/docs/help/alert_dingtalk")," \u5728\u9489\u9489\u7aef\u914d\u7f6e\u673a\u5668\u4eba\uff0c\u8bbe\u7f6e\u5b89\u5168\u81ea\u5b9a\u4e49\u5173\u952e\u8bcd",(0,r.kt)("inlineCode",{parentName:"li"},"HertzBeat"),"\uff0c\u83b7\u53d6\u5bf9\u5e94",(0,r.kt)("inlineCode",{parentName:"li"},"access_token"),"\u503c\u3002 "),(0,r.kt)("li",{parentName:"ul"},"\u5728 HertzBeat \u914d\u7f6e\u63a5\u6536\u4eba\u53c2\u6570\u5982\u4e0b\u3002  ")),(0,r.kt)("p",null,"\u3010\u544a\u8b66\u901a\u77e5\u3011->\u3010\u65b0\u589e\u63a5\u6536\u4eba\u3011 ->\u3010\u9009\u62e9\u9489\u9489\u673a\u5668\u4eba\u901a\u77e5\u65b9\u5f0f\u3011->\u3010\u8bbe\u7f6e\u9489\u9489\u673a\u5668\u4ebaACCESS_TOKEN\u3011-> \u3010\u786e\u5b9a\u3011"),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(2862).Z,width:"3436",height:"890"}),"    "),(0,r.kt)("ol",{start:3},(0,r.kt)("li",{parentName:"ol"},"\u914d\u7f6e\u5173\u8054\u7684\u544a\u8b66\u901a\u77e5\u7b56\u7565\u26a0\ufe0f \u3010\u65b0\u589e\u901a\u77e5\u7b56\u7565\u3011-> \u3010\u5c06\u521a\u8bbe\u7f6e\u7684\u63a5\u6536\u4eba\u5173\u8054\u3011-> \u3010\u786e\u5b9a\u3011 ")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"\u914d\u7f6e\u544a\u8b66\u901a\u77e5\u7b56\u7565\uff0c\u8ba9\u544a\u8b66\u6d88\u606f\u4e0e\u63a5\u6536\u4eba\u7ed1\u5b9a\uff0c\u8fd9\u6837\u5c31\u80fd\u51b3\u5b9a\u54ea\u4e9b\u544a\u8b66\u53d1\u7ed9\u54ea\u4e2a\u4eba\u3002")),(0,r.kt)("p",null,(0,r.kt)("img",{alt:"hertzbeat",src:a(813).Z,width:"3436",height:"1088"}),"    "),(0,r.kt)("h3",{id:"\u5b8c\u6bd5\u73b0\u5728\u5750\u7b49\u544a\u8b66\u6d88\u606f\u8fc7\u6765\u5566\u53ee\u53ee\u53ee\u53ee"},"\u5b8c\u6bd5\uff0c\u73b0\u5728\u5750\u7b49\u544a\u8b66\u6d88\u606f\u8fc7\u6765\u5566\u3002\u53ee\u53ee\u53ee\u53ee"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre"},"[HertzBeat\u544a\u8b66\u901a\u77e5]\n\u544a\u8b66\u76ee\u6807\u5bf9\u8c61 : linux.cpu.usage\n\u6240\u5c5e\u76d1\u63a7ID : 483783444839382\n\u6240\u5c5e\u76d1\u63a7\u540d\u79f0 : Linux_182.33.34.2\n\u544a\u8b66\u7ea7\u522b : \u8b66\u544a\u544a\u8b66\n\u544a\u8b66\u89e6\u53d1\u65f6\u95f4 : 2023-02-15 21:13:44\n\u5185\u5bb9\u8be6\u60c5 : The linux cpu usage is too high. now is 95.\n")),(0,r.kt)("h2",{id:"\u5c0f\u7ed3"},"\u5c0f\u7ed3"),(0,r.kt)("admonition",{type:"tip"},(0,r.kt)("p",{parentName:"admonition"},"\u8fd9\u7bc7\u5b9e\u8df5\u6587\u7ae0\u5e26\u6211\u4eec\u4f53\u9a8c\u4e86\u5982\u4f55\u4f7f\u7528\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf HertzBeat \u6765\u76d1\u63a7 Linux \u6307\u6807\u6570\u636e\uff0c\u53ef\u4ee5\u53d1\u73b0\u96c6 ",(0,r.kt)("inlineCode",{parentName:"p"},"\u76d1\u63a7-\u544a\u8b66-\u901a\u77e5")," \u7684 HertzBeat \u5728\u64cd\u4f5c\u4e0e\u4f7f\u7528\u65b9\u9762\u66f4\u52a0\u7684\u4fbf\u6377\uff0c\u53ea\u9700\u9875\u9762\u4e0a\u7b80\u5355\u70b9\u4e00\u70b9\u5c31\u80fd\u628a Linux \u7eb3\u5165\u76d1\u63a7\u5e76\u544a\u8b66\u901a\u77e5\uff0c\u518d\u4e5f\u4e0d\u9700\u8981\u90e8\u7f72\u591a\u4e2a\u7ec4\u4ef6\u7f16\u5199\u914d\u7f6e\u6587\u4ef6\u90a3\u4e9b\u7e41\u7410\u64cd\u4f5c\u4e86\u3002  ")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"\u53ea\u9700\u8981\u4e00\u6761docker\u547d\u4ee4\u5373\u53ef\u5b89\u88c5\u4f53\u9a8cheartbeat:   ")),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat")),(0,r.kt)("h2",{id:"what-is-hertzbeat"},"What is HertzBeat?"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},(0,r.kt)("a",{parentName:"p",href:"https://github.com/dromara/hertzbeat"},"HertzBeat\u8d6b\u5179\u8df3\u52a8")," \u662f\u4e00\u4e2a\u62e5\u6709\u5f3a\u5927\u81ea\u5b9a\u4e49\u76d1\u63a7\u80fd\u529b\uff0c\u65e0\u9700Agent\u7684\u5b9e\u65f6\u76d1\u63a7\u544a\u8b66\u7cfb\u7edf\u3002\u5e94\u7528\u670d\u52a1\uff0c\u6570\u636e\u5e93\uff0c\u64cd\u4f5c\u7cfb\u7edf\uff0c\u4e2d\u95f4\u4ef6\uff0c\u4e91\u539f\u751f\u7b49\u76d1\u63a7\uff0c\u9608\u503c\u544a\u8b66\uff0c\u544a\u8b66\u901a\u77e5(\u90ae\u4ef6\u5fae\u4fe1\u9489\u9489\u98de\u4e66\u77ed\u4fe1 Discord Slack Telegram)\u3002")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"\u6211\u4eec\u5c06",(0,r.kt)("inlineCode",{parentName:"p"},"Http,Jmx,Ssh,Snmp,Jdbc"),"\u7b49\u534f\u8bae\u89c4\u8303\u53ef\u914d\u7f6e\u5316\uff0c\u53ea\u9700\u914d\u7f6eYML\u5c31\u80fd\u4f7f\u7528\u8fd9\u4e9b\u534f\u8bae\u53bb\u81ea\u5b9a\u4e49\u91c7\u96c6\u4efb\u4f55\u60a8\u60f3\u8981\u91c7\u96c6\u7684\u6307\u6807\u3002",(0,r.kt)("br",{parentName:"p"}),"\n","\u60a8\u76f8\u4fe1\u53ea\u9700\u914d\u7f6eYML\u5c31\u80fd\u7acb\u523b\u9002\u914d\u4e00\u4e2aK8s\u6216Docker\u7b49\u65b0\u7684\u76d1\u63a7\u7c7b\u578b\u5417\uff1f")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},(0,r.kt)("inlineCode",{parentName:"p"},"HertzBeat"),"\u7684\u5f3a\u5927\u81ea\u5b9a\u4e49\uff0c\u591a\u7c7b\u578b\u652f\u6301\uff0c\u6613\u6269\u5c55\uff0c\u4f4e\u8026\u5408\uff0c\u5e0c\u671b\u80fd\u5e2e\u52a9\u5f00\u53d1\u8005\u548c\u4e2d\u5c0f\u56e2\u961f\u5feb\u901f\u642d\u5efa\u81ea\u6709\u76d1\u63a7\u7cfb\u7edf\u3002")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"Github: ",(0,r.kt)("a",{parentName:"strong",href:"https://github.com/dromara/hertzbeat"},"https://github.com/dromara/hertzbeat")),(0,r.kt)("br",{parentName:"p"}),"\n",(0,r.kt)("strong",{parentName:"p"},"Gitee: ",(0,r.kt)("a",{parentName:"strong",href:"https://gitee.com/dromara/hertzbeat"},"https://gitee.com/dromara/hertzbeat"))),(0,r.kt)("h2",{id:"-supported"},"\u26c4 Supported"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"\u7f51\u7ad9\u76d1\u63a7,\xa0\u7aef\u53e3\u53ef\u7528\u6027,\xa0Http Api,\xa0Ping\u8fde\u901a\u6027,\xa0Jvm,\xa0SiteMap\u5168\u7ad9,\xa0Ssl\u8bc1\u4e66,\xa0SpringBoot, FTP\u670d\u52a1\u5668"),(0,r.kt)("li",{parentName:"ul"},"Mysql,\xa0PostgreSQL,\xa0MariaDB,\xa0Redis,\xa0ElasticSearch,\xa0SqlServer,\xa0Oracle,\xa0MongoDB,\xa0\u8fbe\u68a6,\xa0OpenGauss,\xa0ClickHouse,\xa0IoTDB"),(0,r.kt)("li",{parentName:"ul"},"Linux,\xa0Ubuntu,\xa0CentOS,\xa0Windows"),(0,r.kt)("li",{parentName:"ul"},"Tomcat,\xa0Nacos,\xa0Zookeeper,\xa0RabbitMQ,\xa0Flink,\xa0Kafka,\xa0ShenYu,\xa0DynamicTp,\xa0Jetty,\xa0ActiveMQ"),(0,r.kt)("li",{parentName:"ul"},"Kubernetes,\xa0Docker"),(0,r.kt)("li",{parentName:"ul"},"\u548c\u66f4\u591a\u60a8\u7684\u81ea\u5b9a\u4e49\u76d1\u63a7\u3002"),(0,r.kt)("li",{parentName:"ul"},"\u901a\u77e5\u652f\u6301\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"Discord"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"Slack"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"Telegram"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"\u90ae\u4ef6"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"\u9489\u9489"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"\u5fae\u4fe1"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"\u98de\u4e66"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"\u77ed\u4fe1"),"\xa0",(0,r.kt)("inlineCode",{parentName:"li"},"Webhook"),"\u3002")))}d.isMDXComponent=!0},2862:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/alert-notice-1-3bad6e06e2c870849bc656a2092d59ac.png"},813:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/alert-notice-2-8025b90624873463fe0a3f75bd7efafa.png"},3862:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-1-f5f2bd10b67c861ed02b71d6360f21e6.png"},8986:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-2-07f1b6a0dec9524c6bb1c705ddfb90f3.png"},8108:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-3-d7bf8ceaee68a94f7e436a0dcaf4c811.png"},6706:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-4-121d61bc7497e10352fe764f094a9398.png"},7597:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-5-4eb4e6eaec6a5d72b162d64ff7dd5456.png"},2678:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-6-758282e06b02fc2d96dca7521e42ccb2.png"},4275:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-7-436797db9ab725d6fd6c6f92878a64f2.png"},7972:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-8-6aaf2f53362be98bcf58b187d70e2532.png"},9996:(t,e,a)=>{a.d(e,{Z:()=>n});const n=a.p+"assets/images/monitor-linux-9-1f7d0e58a752559ce186757607f289b8.png"}}]);