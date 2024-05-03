"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[739],{5680:(t,e,n)=>{n.d(e,{xA:()=>d,yg:()=>m});var a=n(6540);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function g(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function y(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var i=a.createContext({}),p=function(t){var e=a.useContext(i),n=e;return t&&(n="function"==typeof t?t(e):g(g({},e),t)),n},d=function(t){var e=p(t.components);return a.createElement(i.Provider,{value:e},t.children)},o={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},u=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,i=t.parentName,d=y(t,["components","mdxType","originalType","parentName"]),u=p(n),m=r,N=u["".concat(i,".").concat(m)]||u[m]||o[m]||l;return n?a.createElement(N,g(g({ref:e},d),{},{components:n})):a.createElement(N,g({ref:e},d))}));function m(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,g=new Array(l);g[0]=u;var y={};for(var i in e)hasOwnProperty.call(e,i)&&(y[i]=e[i]);y.originalType=t,y.mdxType="string"==typeof t?t:r,g[1]=y;for(var p=2;p<l;p++)g[p]=n[p];return a.createElement.apply(null,g)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},9751:(t,e,n)=>{n.r(e),n.d(e,{assets:()=>i,contentTitle:()=>g,default:()=>o,frontMatter:()=>l,metadata:()=>y,toc:()=>p});var a=n(8168),r=(n(6540),n(5680));const l={id:"dns",title:"\u76d1\u63a7 DNS \u670d\u52a1\u5668",sidebar_label:"DNS\u670d\u52a1\u5668",keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u5f00\u6e90DNS\u76d1\u63a7\u5de5\u5177","\u76d1\u63a7DNS\u6307\u6807"]},g="\u76d1\u63a7\uff1aDNS\u670d\u52a1\u5668",y={unversionedId:"help/dns",id:"help/dns",title:"\u76d1\u63a7 DNS \u670d\u52a1\u5668",description:"\u6536\u96c6\u548c\u76d1\u63a7DNS\u7684\u5e38\u89c4\u6027\u80fd\u6307\u6807\u3002",source:"@site/i18n/zh-cn/docusaurus-plugin-content-docs/current/help/dns.md",sourceDirName:"help",slug:"/help/dns",permalink:"/zh-cn/docs/help/dns",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/zh-cn/docusaurus-plugin-content-docs/current/help/dns.md",tags:[],version:"current",frontMatter:{id:"dns",title:"\u76d1\u63a7 DNS \u670d\u52a1\u5668",sidebar_label:"DNS\u670d\u52a1\u5668",keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u5f00\u6e90DNS\u76d1\u63a7\u5de5\u5177","\u76d1\u63a7DNS\u6307\u6807"]},sidebar:"docs",previous:{title:"NTP \u670d\u52a1\u5668",permalink:"/zh-cn/docs/help/ntp"},next:{title:"FTP\u670d\u52a1\u5668",permalink:"/zh-cn/docs/help/ftp"}},i={},p=[{value:"\u914d\u7f6e\u53c2\u6570",id:"\u914d\u7f6e\u53c2\u6570",level:3},{value:"\u91c7\u96c6\u6307\u6807",id:"\u91c7\u96c6\u6307\u6807",level:3},{value:"\u6307\u6807\u96c6\uff1aHeader",id:"\u6307\u6807\u96c6header",level:4},{value:"\u6307\u6807\u96c6: Question",id:"\u6307\u6807\u96c6-question",level:3},{value:"\u6307\u6807\u96c6: Answer",id:"\u6307\u6807\u96c6-answer",level:3},{value:"\u6307\u6807\u96c6: Authority",id:"\u6307\u6807\u96c6-authority",level:3},{value:"\u6307\u6807\u96c6: Additional",id:"\u6307\u6807\u96c6-additional",level:3}],d={toc:p};function o(t){let{components:e,...n}=t;return(0,r.yg)("wrapper",(0,a.A)({},d,n,{components:e,mdxType:"MDXLayout"}),(0,r.yg)("h1",{id:"\u76d1\u63a7dns\u670d\u52a1\u5668"},"\u76d1\u63a7\uff1aDNS\u670d\u52a1\u5668"),(0,r.yg)("blockquote",null,(0,r.yg)("p",{parentName:"blockquote"},"\u6536\u96c6\u548c\u76d1\u63a7DNS\u7684\u5e38\u89c4\u6027\u80fd\u6307\u6807\u3002")),(0,r.yg)("p",null,(0,r.yg)("strong",{parentName:"p"},"\u534f\u8bae\u4f7f\u7528\uff1aDNS")),(0,r.yg)("h3",{id:"\u914d\u7f6e\u53c2\u6570"},"\u914d\u7f6e\u53c2\u6570"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u53c2\u6570\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u53c2\u6570\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"DNS\u670d\u52a1\u5668IP"),(0,r.yg)("td",{parentName:"tr",align:null},"\u88ab\u76d1\u63a7\u7684IPv4\u3001IPv6\u3002\u6ce8\u610f\u26a0\ufe0f\u4e0d\u5305\u542b\u534f\u8bae\u5934\uff08\u4f8b\u5982\uff1ahttps://\uff0chttp://\uff09\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u540d\u79f0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u6807\u8bc6\u6b64\u76d1\u63a7\u7684\u540d\u79f0\uff0c\u540d\u79f0\u9700\u8981\u662f\u552f\u4e00\u7684\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u7aef\u53e3"),(0,r.yg)("td",{parentName:"tr",align:null},"DNS\u670d\u52a1\u5bf9\u5916\u63d0\u4f9b\u7684\u7aef\u53e3\uff0c\u9ed8\u8ba4\u4e3a53\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u57df\u540d\u89e3\u6790\u7684\u5730\u5740"),(0,r.yg)("td",{parentName:"tr",align:null},"\u57df\u540d\u89e3\u6790\u7684\u5730\u5740\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u8fde\u63a5\u8d85\u65f6\u65f6\u95f4"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8bbe\u7f6e\u8fde\u63a5DNS\u670d\u52a1\u5668\u7684\u8d85\u65f6\u65f6\u95f4\uff0c\u5355\u4f4dms\u6beb\u79d2\uff0c\u9ed8\u8ba46000\u6beb\u79d2\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u67e5\u8be2\u7c7b\u522b"),(0,r.yg)("td",{parentName:"tr",align:null},"DNS\u67e5\u8be2\u7684\u7c7b\u522b. \u53ef\u9009\u7684\u503c\u5305\u62ec ",(0,r.yg)("inlineCode",{parentName:"td"},"IN"),"\uff0c ",(0,r.yg)("inlineCode",{parentName:"td"},"CHAOS"),"\uff0c ",(0,r.yg)("inlineCode",{parentName:"td"},"HESIOD"),"\uff0c ",(0,r.yg)("inlineCode",{parentName:"td"},"NONE"),"\uff0c \u548c ",(0,r.yg)("inlineCode",{parentName:"td"},"ANY"),"\u3002\u9ed8\u8ba4\u503c\uff1aIN")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u662f\u5426\u4f7f\u7528tcp\u534f\u8bae"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8bbe\u7f6eDNS\u67e5\u8be2\u662f\u5426\u4f7f\u7528tcp\u534f\u8bae\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u91c7\u96c6\u95f4\u9694"),(0,r.yg)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u5468\u671f\u6027\u6570\u636e\u91c7\u96c6\u7684\u65f6\u95f4\u95f4\u9694\uff0c\u5355\u4f4d\uff1a\u79d2\uff0c\u6700\u5c0f\u53ef\u8bbe\u7f6e\u4e3a30\u79d2\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u7ed1\u5b9a\u6807\u7b7e"),(0,r.yg)("td",{parentName:"tr",align:null},"\u7528\u4e8e\u5bf9\u76d1\u63a7\u8d44\u6e90\u8fdb\u884c\u5206\u7c7b\u7ba1\u7406\u3002")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u63cf\u8ff0\u5907\u6ce8"),(0,r.yg)("td",{parentName:"tr",align:null},"\u7528\u4e8e\u66f4\u591a\u5173\u4e8e\u6807\u8bc6\u548c\u63cf\u8ff0\u6b64\u76d1\u63a7\u7684\u4fe1\u606f\uff0c\u7528\u6237\u53ef\u4ee5\u5728\u6b64\u5904\u6dfb\u52a0\u5907\u6ce8\u4fe1\u606f\u3002")))),(0,r.yg)("h3",{id:"\u91c7\u96c6\u6307\u6807"},"\u91c7\u96c6\u6307\u6807"),(0,r.yg)("h4",{id:"\u6307\u6807\u96c6header"},"\u6307\u6807\u96c6\uff1aHeader"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u65f6\u95f4"),(0,r.yg)("td",{parentName:"tr",align:null},"\u6beb\u79d2"),(0,r.yg)("td",{parentName:"tr",align:null},"DNS\u670d\u52a1\u5668\u54cd\u5e94\u8bf7\u6c42\u6240\u9700\u7684\u65f6\u95f4")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u64cd\u4f5c\u7801"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6d88\u606f\u7684\u7c7b\u578b")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u72b6\u6001"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u7684\u72b6\u6001\u7801")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u6807\u5fd7"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u6807\u5fd7")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u8bf7\u6c42\u8bb0\u5f55\u6570"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8bf7\u6c42\u8bb0\u5f55\u7684\u6570\u91cf")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u8bb0\u5f55\u6570"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u54cd\u5e94\u8bb0\u5f55\u7684\u6570\u91cf")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u6388\u6743\u8bb0\u5f55\u6570"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8868\u793a\u6743\u5a01\u8d44\u6e90\u8bb0\u5f55\u7684\u6570\u91cf")),(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"\u9644\u52a0\u8bb0\u5f55\u6570"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8868\u793a\u9644\u52a0\u8d44\u6e90\u8bb0\u5f55\u7684\u6570\u91cf")))),(0,r.yg)("h3",{id:"\u6307\u6807\u96c6-question"},"\u6307\u6807\u96c6: Question"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"Section"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8bf7\u6c42\u8bb0\u5f55\u4fe1\u606f\uff0c\u5176\u4e2d\u5305\u542b\u67e5\u8be2\u7684\u57df\u540d\uff0c\u8d44\u6e90\u7c7b\u578b\uff0c\u8d44\u6e90\u8bb0\u5f55\u7c7b\uff0c\u9644\u52a0\u4fe1\u606f\u3002")))),(0,r.yg)("h3",{id:"\u6307\u6807\u96c6-answer"},"\u6307\u6807\u96c6: Answer"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"Section0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u8bf7\u6c42\u54cd\u5e94\u4fe1\u606f\uff0c\u5176\u4e2d\u5305\u542b\u67e5\u8be2\u7684\u57df\u540d\uff0cTTL\uff0c\u8d44\u6e90\u8bb0\u5f55\u7c7b\uff0c\u8d44\u6e90\u7c7b\u578b\uff0c\u67e5\u8be2\u7684\u7ed3\u679c\u3002")))),(0,r.yg)("blockquote",null,(0,r.yg)("p",{parentName:"blockquote"},"Answer \u6307\u6807\u96c6\u6700\u591a\u4f1a\u91c7\u96c610\u6761\u54cd\u5e94\u8bb0\u5f55\uff0c\u6307\u6807\u540d\u79f0\u4ece Section0 \u5230 Section9\u3002")),(0,r.yg)("h3",{id:"\u6307\u6807\u96c6-authority"},"\u6307\u6807\u96c6: Authority"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"Section0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u57df\u540d\u7684SOA\uff08Start of Authority\uff0c\u6743\u5a01\u533a\u57df\u8d77\u59cb\uff09\u8bb0\u5f55\uff0c\u5176\u4e2d\u5305\u542b\u67e5\u8be2\u7684\u57df\u540d\uff0cTTL\uff0c\u8d44\u6e90\u7c7b\u578b\uff0c\u8d44\u6e90\u8bb0\u5f55\u7c7b\u7b49\u4fe1\u606f\u3002")))),(0,r.yg)("blockquote",null,(0,r.yg)("p",{parentName:"blockquote"},"Authority \u6307\u6807\u96c6\u6700\u591a\u4f1a\u91c7\u96c610\u6761\u54cd\u5e94\u8bb0\u5f55\uff0c\u6307\u6807\u540d\u79f0\u4ece Section0 \u5230 Section9\u3002")),(0,r.yg)("h3",{id:"\u6307\u6807\u96c6-additional"},"\u6307\u6807\u96c6: Additional"),(0,r.yg)("table",null,(0,r.yg)("thead",{parentName:"table"},(0,r.yg)("tr",{parentName:"thead"},(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.yg)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,r.yg)("tbody",{parentName:"table"},(0,r.yg)("tr",{parentName:"tbody"},(0,r.yg)("td",{parentName:"tr",align:null},"Section0"),(0,r.yg)("td",{parentName:"tr",align:null},"\u65e0"),(0,r.yg)("td",{parentName:"tr",align:null},"DNS\u67e5\u8be2\u7684\u9644\u52a0\u4fe1\u606f\u3002")))),(0,r.yg)("blockquote",null,(0,r.yg)("p",{parentName:"blockquote"},"Additional \u6307\u6807\u96c6\u6700\u591a\u4f1a\u91c7\u96c610\u6761\u54cd\u5e94\u8bb0\u5f55\uff0c\u6307\u6807\u540d\u79f0\u4ece Section0 \u5230 Section9\u3002")))}o.isMDXComponent=!0}}]);