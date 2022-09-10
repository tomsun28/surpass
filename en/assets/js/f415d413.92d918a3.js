"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[1533],{4137:function(e,t,r){r.d(t,{Zo:function(){return s},kt:function(){return d}});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function i(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function o(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?i(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function l(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},i=Object.keys(e);for(n=0;n<i.length;n++)r=i[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(n=0;n<i.length;n++)r=i[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var c=n.createContext({}),p=function(e){var t=n.useContext(c),r=t;return e&&(r="function"==typeof e?e(t):o(o({},t),e)),r},s=function(e){var t=p(e.components);return n.createElement(c.Provider,{value:t},e.children)},m={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},u=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,i=e.originalType,c=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),u=p(r),d=a,f=u["".concat(c,".").concat(d)]||u[d]||m[d]||i;return r?n.createElement(f,o(o({ref:t},s),{},{components:r})):n.createElement(f,o({ref:t},s))}));function d(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var i=r.length,o=new Array(i);o[0]=u;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:a,o[1]=l;for(var p=2;p<i;p++)o[p]=r[p];return n.createElement.apply(null,o)}return n.createElement.apply(null,r)}u.displayName="MDXCreateElement"},6996:function(e,t,r){r.r(t),r.d(t,{frontMatter:function(){return l},contentTitle:function(){return c},metadata:function(){return p},toc:function(){return s},default:function(){return u}});var n=r(7462),a=r(3366),i=(r(7294),r(4137)),o=["components"],l={id:"mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",sidebar_label:"MYSQL\u5b89\u88c5\u521d\u59cb\u5316"},c=void 0,p={unversionedId:"start/mysql-init",id:"version-v1.0.0/start/mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",description:"MYSQL\u662f\u4e00\u6b3e\u503c\u5f97\u4fe1\u8d56\u7684\u5173\u7cfb\u578b\u6570\u636e\u5e93\uff0cHertzBeat\u4f7f\u7528\u5176\u5b58\u50a8\u76d1\u63a7\u4fe1\u606f\uff0c\u544a\u8b66\u4fe1\u606f\uff0c\u914d\u7f6e\u4fe1\u606f\u7b49\u7ed3\u6784\u5316\u5173\u7cfb\u6570\u636e\u3002",source:"@site/versioned_docs/version-v1.0.0/start/mysql-init.md",sourceDirName:"start",slug:"/start/mysql-init",permalink:"/en/docs/v1.0.0/start/mysql-init",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/versioned_docs/version-v1.0.0/start/mysql-init.md",tags:[],version:"v1.0.0",frontMatter:{id:"mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",sidebar_label:"MYSQL\u5b89\u88c5\u521d\u59cb\u5316"},sidebar:"docs",previous:{title:"\u5feb\u901f\u5f00\u59cb",permalink:"/en/docs/v1.0.0/start/quickstart"},next:{title:"TDengine\u5b89\u88c5\u521d\u59cb\u5316",permalink:"/en/docs/v1.0.0/start/tdengine-init"}},s=[{value:"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5MYSQL",id:"\u901a\u8fc7docker\u65b9\u5f0f\u5b89\u88c5mysql",children:[],level:3},{value:"SQL\u811a\u672c\u6267\u884c",id:"sql\u811a\u672c\u6267\u884c",children:[],level:3}],m={toc:s};function u(e){var t=e.components,r=(0,a.Z)(e,o);return(0,i.kt)("wrapper",(0,n.Z)({},m,r,{components:t,mdxType:"MDXLayout"}),(0,i.kt)("p",null,"MYSQL\u662f\u4e00\u6b3e\u503c\u5f97\u4fe1\u8d56\u7684\u5173\u7cfb\u578b\u6570\u636e\u5e93\uff0cHertzBeat\u4f7f\u7528\u5176\u5b58\u50a8\u76d1\u63a7\u4fe1\u606f\uff0c\u544a\u8b66\u4fe1\u606f\uff0c\u914d\u7f6e\u4fe1\u606f\u7b49\u7ed3\u6784\u5316\u5173\u7cfb\u6570\u636e\u3002  "),(0,i.kt)("p",null,"\u5b89\u88c5\u90e8\u7f72\u89c6\u9891\u6559\u7a0b: ",(0,i.kt)("a",{parentName:"p",href:"https://www.bilibili.com/video/BV1GY41177YL"},"HertzBeat\u5b89\u88c5\u90e8\u7f72-BiliBili"),"  "),(0,i.kt)("blockquote",null,(0,i.kt)("p",{parentName:"blockquote"},"\u5982\u679c\u60a8\u5df2\u6709MYSQL\u73af\u5883\uff0c\u53ef\u76f4\u63a5\u8df3\u5230SQL\u811a\u672c\u6267\u884c\u90a3\u4e00\u6b65\u3002  ")),(0,i.kt)("h3",{id:"\u901a\u8fc7docker\u65b9\u5f0f\u5b89\u88c5mysql"},"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5MYSQL"),(0,i.kt)("ol",null,(0,i.kt)("li",{parentName:"ol"},"\u4e0b\u8f7d\u5b89\u88c5Docker\u73af\u5883",(0,i.kt)("br",{parentName:"li"}),"Docker \u5de5\u5177\u81ea\u8eab\u7684\u4e0b\u8f7d\u8bf7\u53c2\u8003 ",(0,i.kt)("a",{parentName:"li",href:"https://docs.docker.com/get-docker/"},"Docker\u5b98\u7f51\u6587\u6863"),"\u3002\n\u5b89\u88c5\u5b8c\u6bd5\u540e\u7ec8\u7aef\u67e5\u770bDocker\u7248\u672c\u662f\u5426\u6b63\u5e38\u8f93\u51fa\u3002  ",(0,i.kt)("pre",{parentName:"li"},(0,i.kt)("code",{parentName:"pre"},"$ docker -v\nDocker version 20.10.12, build e91ed57\n"))),(0,i.kt)("li",{parentName:"ol"},"Docker\u5b89\u88c5MYSQl  ",(0,i.kt)("pre",{parentName:"li"},(0,i.kt)("code",{parentName:"pre"},"$ docker run -d --name mysql -p 3306:3306 -v /opt/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7\n526aa188da767ae94b244226a2b2eec2b5f17dd8eff594533d9ec0cd0f3a1ccd\n")),(0,i.kt)("inlineCode",{parentName:"li"},"-v /opt/data:/var/lib/mysql")," \u4e3amysql\u6570\u636e\u76ee\u5f55\u672c\u5730\u6301\u4e45\u5316\u6302\u8f7d\uff0c\u9700\u5c06",(0,i.kt)("inlineCode",{parentName:"li"},"/opt/data"),"\u66ff\u6362\u4e3a\u5b9e\u9645\u672c\u5730\u5b58\u5728\u7684\u76ee\u5f55",(0,i.kt)("br",{parentName:"li"}),"\u4f7f\u7528",(0,i.kt)("inlineCode",{parentName:"li"},"$ docker ps"),"\u67e5\u770b\u6570\u636e\u5e93\u662f\u5426\u542f\u52a8\u6210\u529f")),(0,i.kt)("h3",{id:"sql\u811a\u672c\u6267\u884c"},"SQL\u811a\u672c\u6267\u884c"),(0,i.kt)("ol",null,(0,i.kt)("li",{parentName:"ol"},"\u8fdb\u5165MYSQL\u6216\u4f7f\u7528\u5ba2\u6237\u7aef\u8fde\u63a5MYSQL\u670d\u52a1",(0,i.kt)("br",{parentName:"li"}),(0,i.kt)("inlineCode",{parentName:"li"},"mysql -uroot -p123456"),"  "),(0,i.kt)("li",{parentName:"ol"},"\u521b\u5efa\u540d\u79f0\u4e3ahertzbeat\u7684\u6570\u636e\u5e93",(0,i.kt)("br",{parentName:"li"}),(0,i.kt)("inlineCode",{parentName:"li"},"create database hertzbeat;")),(0,i.kt)("li",{parentName:"ol"},"\u6267\u884c\u4f4d\u4e8e\u9879\u76ee\u4ed3\u5e93/script/sql/\u76ee\u5f55\u4e0b\u7684\u6570\u636e\u5e93\u5efa\u8868\u521d\u59cb\u5316\u811a\u672c ",(0,i.kt)("a",{parentName:"li",href:"https://gitee.com/dromara/hertzbeat/raw/master/script/sql/schema.sql"},"schema.sql"),(0,i.kt)("br",{parentName:"li"}),(0,i.kt)("inlineCode",{parentName:"li"},"mysql -uroot -p123456 < schema.sql"),"   "),(0,i.kt)("li",{parentName:"ol"},"\u67e5\u770bhertzbeat\u6570\u636e\u5e93\u662f\u5426\u6210\u529f\u5efa\u8868")))}u.isMDXComponent=!0}}]);