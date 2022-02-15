"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[363],{4137:function(e,t,r){r.d(t,{Zo:function(){return u},kt:function(){return m}});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function p(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function c(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var i=n.createContext({}),l=function(e){var t=n.useContext(i),r=t;return e&&(r="function"==typeof e?e(t):p(p({},t),e)),r},u=function(e){var t=l(e.components);return n.createElement(i.Provider,{value:t},e.children)},s={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,i=e.parentName,u=c(e,["components","mdxType","originalType","parentName"]),d=l(r),m=a,k=d["".concat(i,".").concat(m)]||d[m]||s[m]||o;return r?n.createElement(k,p(p({ref:t},u),{},{components:r})):n.createElement(k,p({ref:t},u))}));function m(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,p=new Array(o);p[0]=d;var c={};for(var i in t)hasOwnProperty.call(t,i)&&(c[i]=t[i]);c.originalType=e,c.mdxType="string"==typeof e?e:a,p[1]=c;for(var l=2;l<o;l++)p[l]=r[l];return n.createElement.apply(null,p)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},7106:function(e,t,r){r.r(t),r.d(t,{frontMatter:function(){return c},contentTitle:function(){return i},metadata:function(){return l},toc:function(){return u},default:function(){return d}});var n=r(3117),a=r(102),o=(r(7294),r(4137)),p=["components"],c={id:"docker-deploy",title:"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5HertzBeat",sidebar_label:"Docker\u65b9\u5f0f\u90e8\u7f72"},i=void 0,l={unversionedId:"start/docker-deploy",id:"start/docker-deploy",title:"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5HertzBeat",description:"\u63a8\u8350\u4f7f\u7528docker\u90e8\u7f72HertzBeat",source:"@site/docs/start/docker-deploy.md",sourceDirName:"start",slug:"/start/docker-deploy",permalink:"/docs/start/docker-deploy",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/docs/start/docker-deploy.md",tags:[],version:"current",frontMatter:{id:"docker-deploy",title:"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5HertzBeat",sidebar_label:"Docker\u65b9\u5f0f\u90e8\u7f72"},sidebar:"docs",previous:{title:"TDengine\u5b89\u88c5\u521d\u59cb\u5316",permalink:"/docs/start/tdengine-init"},next:{title:"\u5b89\u88c5\u5305\u65b9\u5f0f\u90e8\u7f72",permalink:"/docs/start/package-deploy"}},u=[],s={toc:u};function d(e){var t=e.components,r=(0,a.Z)(e,p);return(0,o.kt)("wrapper",(0,n.Z)({},s,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},"\u63a8\u8350\u4f7f\u7528docker\u90e8\u7f72HertzBeat  ")),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"\u4e0b\u8f7d\u5b89\u88c5Docker\u73af\u5883",(0,o.kt)("br",{parentName:"p"}),"\n","Docker \u5de5\u5177\u81ea\u8eab\u7684\u4e0b\u8f7d\u8bf7\u53c2\u8003 ",(0,o.kt)("a",{parentName:"p",href:"https://docs.docker.com/get-docker/"},"Docker\u5b98\u7f51\u6587\u6863"),"\u3002\n\u5b89\u88c5\u5b8c\u6bd5\u540e\u7ec8\u7aef\u67e5\u770bDocker\u7248\u672c\u662f\u5426\u6b63\u5e38\u8f93\u51fa\u3002"),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker -v\nDocker version 20.10.12, build e91ed57\n"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"\u62c9\u53d6HertzBeat Docker\u955c\u50cf",(0,o.kt)("br",{parentName:"p"}),"\n","\u955c\u50cf\u7248\u672cTAG\u53ef\u67e5\u770b",(0,o.kt)("a",{parentName:"p",href:"https://hub.docker.com/r/tancloud/hertzbeat/tags"},"\u5b98\u65b9\u955c\u50cf\u4ed3\u5e93"),"     "),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker pull tancloud/hertzbeat:latest \n"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"\u914d\u7f6eHertzBeat\u7684\u914d\u7f6e\u6587\u4ef6",(0,o.kt)("br",{parentName:"p"}),"\n","\u5728\u4e3b\u673a\u76ee\u5f55\u4e0b\u521b\u5efaapplication.yml\uff0ceg:/opt/application.yml",(0,o.kt)("br",{parentName:"p"}),"\n","\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9\u53c2\u8003 \u9879\u76ee\u4ed3\u5e93",(0,o.kt)("a",{parentName:"p",href:"https://gitee.com/dromara/hertzbeat/raw/master/script/application.yml"},"/script/application.yml"),"\uff0c\u9700\u8981\u66ff\u6362\u91cc\u9762\u7684MYSQL\u670d\u52a1\u548cTDengine\u670d\u52a1\u53c2\u6570\uff0cIP\u7aef\u53e3\u8d26\u6237\u5bc6\u7801\uff08\u82e5\u4f7f\u7528\u90ae\u4ef6\u544a\u8b66\uff0c\u9700\u66ff\u6362\u91cc\u9762\u7684\u90ae\u4ef6\u670d\u52a1\u5668\u53c2\u6570\uff09\n\u5177\u4f53\u66ff\u6362\u53c2\u6570\u5982\u4e0b:"),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"spring.datasource.url\nspring.datasource.username\nspring.datasource.password\n\nwarehouse.store.td-engine.url\nwarehouse.store.td-engine.username\nwarehouse.store.td-engine.password\n\nspring.mail.host\nspring.mail.port\nspring.mail.username\nspring.mail.password\n\n"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"\u542f\u52a8HertzBeat Docker\u5bb9\u5668  "),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker run -d -p 1157:1157 -v /opt/application.yml:/opt/hertzbeat/config/application.yml --name hertzbeat tancloud/hertzbeat:latest\n526aa188da767ae94b244226a2b2eec2b5f17dd8eff592893d9ec0cd0f3a1ccd\n")),(0,o.kt)("p",{parentName:"li"},"\u8fd9\u6761\u547d\u4ee4\u542f\u52a8\u4e00\u4e2a\u8fd0\u884cHertzBeat\u7684Docker\u5bb9\u5668\uff0c\u5e76\u4e14\u5c06\u5bb9\u5668\u76841157\u7aef\u53e3\u6620\u5c04\u5230\u5bbf\u4e3b\u673a\u76841157\u7aef\u53e3\u4e0a\u3002\u82e5\u5bbf\u4e3b\u673a\u5df2\u6709\u8fdb\u7a0b\u5360\u7528\u8be5\u7aef\u53e3\uff0c\u5219\u9700\u8981\u4fee\u6539\u4e3b\u673a\u6620\u5c04\u7aef\u53e3\u3002"),(0,o.kt)("ul",{parentName:"li"},(0,o.kt)("li",{parentName:"ul"},"docker run -d : \u901a\u8fc7Docker\u8fd0\u884c\u4e00\u4e2a\u5bb9\u5668,\u4f7f\u5176\u5728\u540e\u53f0\u8fd0\u884c"),(0,o.kt)("li",{parentName:"ul"},"-p 1157:1157  : \u6620\u5c04\u5bb9\u5668\u7aef\u53e3\u5230\u4e3b\u673a\u7aef\u53e3"),(0,o.kt)("li",{parentName:"ul"},"-v /opt/application.yml:/opt/hertzbeat/config/application.yml  : \u6302\u8f7d\u4e0a\u4e00\u6b65\u4fee\u6539\u7684\u672c\u5730\u914d\u7f6e\u6587\u4ef6\u5230\u5bb9\u5668\u4e2d\uff0c\u5373\u4f7f\u7528\u672c\u5730\u914d\u7f6e\u6587\u4ef6\u8986\u76d6\u5bb9\u5668\u914d\u7f6e\u6587\u4ef6\u3002\u6211\u4eec\u9700\u8981\u4fee\u6539\u6b64\u914d\u7f6e\u6587\u4ef6\u7684MYSQL\uff0cTDengine\u914d\u7f6e\u4fe1\u606f\u6765\u8fde\u63a5\u5916\u90e8\u670d\u52a1\u3002"),(0,o.kt)("li",{parentName:"ul"},"--name hertzbeat : \u547d\u540d\u5bb9\u5668\u540d\u79f0 hertzbeat "),(0,o.kt)("li",{parentName:"ul"},"tancloud/hertzbeat:latest : \u4f7f\u7528\u62c9\u53d6\u7684HertzBeat\u5b98\u65b9\u53d1\u5e03\u7684\u5e94\u7528\u955c\u50cf\u6765\u542f\u52a8\u5bb9\u5668 "))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"\u5f00\u59cb\u63a2\u7d22HertzBeat",(0,o.kt)("br",{parentName:"p"}),"\n","\u6d4f\u89c8\u5668\u8bbf\u95ee http://ip:1157 \u5f00\u59cb\u4f7f\u7528HertzBeat\u8fdb\u884c\u76d1\u63a7\u544a\u8b66\u3002"))),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"HAVE FUN")))}d.isMDXComponent=!0}}]);