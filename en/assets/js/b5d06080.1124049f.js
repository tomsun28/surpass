"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[2101],{4137:function(e,t,r){r.d(t,{Zo:function(){return u},kt:function(){return d}});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function c(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var l=n.createContext({}),s=function(e){var t=n.useContext(l),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},u=function(e){var t=s(e.components);return n.createElement(l.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,l=e.parentName,u=c(e,["components","mdxType","originalType","parentName"]),m=s(r),d=a,f=m["".concat(l,".").concat(d)]||m[d]||p[d]||o;return r?n.createElement(f,i(i({ref:t},u),{},{components:r})):n.createElement(f,i({ref:t},u))}));function d(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,i=new Array(o);i[0]=m;var c={};for(var l in t)hasOwnProperty.call(t,l)&&(c[l]=t[l]);c.originalType=e,c.mdxType="string"==typeof e?e:a,i[1]=c;for(var s=2;s<o;s++)i[s]=r[s];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}m.displayName="MDXCreateElement"},3710:function(e,t,r){r.r(t),r.d(t,{frontMatter:function(){return c},contentTitle:function(){return l},metadata:function(){return s},toc:function(){return u},default:function(){return m}});var n=r(3117),a=r(102),o=(r(7294),r(4137)),i=["components"],c={id:"mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",sidebar_label:"MYSQL\u5b89\u88c5\u521d\u59cb\u5316"},l=void 0,s={unversionedId:"start/mysql-init",id:"start/mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",description:"MYSQL\u662f\u4e00\u6b3e\u503c\u5f97\u4fe1\u8d56\u7684\u5173\u7cfb\u578b\u6570\u636e\u5e93\uff0cHertzBeat\u4f7f\u7528\u5176\u5b58\u50a8\u76d1\u63a7\u4fe1\u606f\uff0c\u544a\u8b66\u4fe1\u606f\uff0c\u914d\u7f6e\u4fe1\u606f\u7b49\u7ed3\u6784\u5316\u5173\u7cfb\u6570\u636e\u3002",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/start/mysql-init.md",sourceDirName:"start",slug:"/start/mysql-init",permalink:"/en/docs/next/start/mysql-init",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/start/mysql-init.md",tags:[],version:"current",frontMatter:{id:"mysql-init",title:"\u4f9d\u8d56\u670d\u52a1MYSQL\u5b89\u88c5\u521d\u59cb\u5316",sidebar_label:"MYSQL\u5b89\u88c5\u521d\u59cb\u5316"},sidebar:"docs",previous:{title:"\u5feb\u901f\u5f00\u59cb",permalink:"/en/docs/next/start/quickstart"},next:{title:"TDengine\u5b89\u88c5\u521d\u59cb\u5316",permalink:"/en/docs/next/start/tdengine-init"}},u=[{value:"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5MYSQL",id:"\u901a\u8fc7docker\u65b9\u5f0f\u5b89\u88c5mysql",children:[],level:3},{value:"SQL\u811a\u672c\u6267\u884c",id:"sql\u811a\u672c\u6267\u884c",children:[],level:3}],p={toc:u};function m(e){var t=e.components,r=(0,a.Z)(e,i);return(0,o.kt)("wrapper",(0,n.Z)({},p,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"MYSQL\u662f\u4e00\u6b3e\u503c\u5f97\u4fe1\u8d56\u7684\u5173\u7cfb\u578b\u6570\u636e\u5e93\uff0cHertzBeat\u4f7f\u7528\u5176\u5b58\u50a8\u76d1\u63a7\u4fe1\u606f\uff0c\u544a\u8b66\u4fe1\u606f\uff0c\u914d\u7f6e\u4fe1\u606f\u7b49\u7ed3\u6784\u5316\u5173\u7cfb\u6570\u636e\u3002  "),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},"\u5982\u679c\u60a8\u5df2\u6709MYSQL\u73af\u5883\uff0c\u53ef\u76f4\u63a5\u8df3\u5230SQL\u811a\u672c\u6267\u884c\u90a3\u4e00\u6b65\u3002  ")),(0,o.kt)("h3",{id:"\u901a\u8fc7docker\u65b9\u5f0f\u5b89\u88c5mysql"},"\u901a\u8fc7Docker\u65b9\u5f0f\u5b89\u88c5MYSQL"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"\u4e0b\u8f7d\u5b89\u88c5Docker\u73af\u5883",(0,o.kt)("br",{parentName:"li"}),"Docker \u5de5\u5177\u81ea\u8eab\u7684\u4e0b\u8f7d\u8bf7\u53c2\u8003 ",(0,o.kt)("a",{parentName:"li",href:"https://docs.docker.com/get-docker/"},"Docker\u5b98\u7f51\u6587\u6863"),"\u3002\n\u5b89\u88c5\u5b8c\u6bd5\u540e\u7ec8\u7aef\u67e5\u770bDocker\u7248\u672c\u662f\u5426\u6b63\u5e38\u8f93\u51fa\u3002  ",(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker -v\nDocker version 20.10.12, build e91ed57\n"))),(0,o.kt)("li",{parentName:"ol"},"Docker\u5b89\u88c5MYSQl  ",(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre"},"$ docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest\n526aa188da767ae94b244226a2b2eec2b5f17dd8eff594533d9ec0cd0f3a1ccd\n")),"\u4f7f\u7528",(0,o.kt)("inlineCode",{parentName:"li"},"$ docker ps"),"\u67e5\u770b\u6570\u636e\u5e93\u662f\u5426\u542f\u52a8\u6210\u529f")),(0,o.kt)("h3",{id:"sql\u811a\u672c\u6267\u884c"},"SQL\u811a\u672c\u6267\u884c"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"\u8fdb\u5165MYSQL\u6216\u4f7f\u7528\u5ba2\u6237\u7aef\u8fde\u63a5MYSQL\u670d\u52a1"),(0,o.kt)("li",{parentName:"ol"},"\u521b\u5efa\u540d\u79f0\u4e3ahertzbeat\u7684\u6570\u636e\u5e93"),(0,o.kt)("li",{parentName:"ol"},"\u6267\u884c\u4f4d\u4e8e\u9879\u76ee\u4ed3\u5e93/script/sql/\u76ee\u5f55\u4e0b\u7684\u6570\u636e\u5e93\u5efa\u8868\u521d\u59cb\u5316\u811a\u672c ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/dromara/hertzbeat/raw/master/script/sql/schema.sql"},"schema.sql"),"  "),(0,o.kt)("li",{parentName:"ol"},"\u67e5\u770bhertzbeat\u6570\u636e\u5e93\u662f\u5426\u6210\u529f\u5efa\u8868")))}m.isMDXComponent=!0}}]);