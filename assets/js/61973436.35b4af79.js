"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[3071],{4137:(t,e,n)=>{n.d(e,{Zo:()=>d,kt:()=>k});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function p(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function i(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},l=Object.keys(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);for(a=0;a<l.length;a++)n=l[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var u=a.createContext({}),o=function(t){var e=a.useContext(u),n=e;return t&&(n="function"==typeof t?t(e):p(p({},e),t)),n},d=function(t){var e=o(t.components);return a.createElement(u.Provider,{value:e},t.children)},m="mdxType",s={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},g=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,l=t.originalType,u=t.parentName,d=i(t,["components","mdxType","originalType","parentName"]),m=o(n),g=r,k=m["".concat(u,".").concat(g)]||m[g]||s[g]||l;return n?a.createElement(k,p(p({ref:e},d),{},{components:n})):a.createElement(k,p({ref:e},d))}));function k(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var l=n.length,p=new Array(l);p[0]=g;var i={};for(var u in e)hasOwnProperty.call(e,u)&&(i[u]=e[u]);i.originalType=t,i[m]="string"==typeof t?t:r,p[1]=i;for(var o=2;o<l;o++)p[o]=n[o];return a.createElement.apply(null,p)}return a.createElement.apply(null,n)}g.displayName="MDXCreateElement"},4106:(t,e,n)=>{n.r(e),n.d(e,{assets:()=>u,contentTitle:()=>p,default:()=>s,frontMatter:()=>l,metadata:()=>i,toc:()=>o});var a=n(7462),r=(n(7294),n(4137));const l={id:"nginx",title:"\u76d1\u63a7 Nginx",sidebar_label:"Nginx \u76d1\u63a7",keywords:["\u5f00\u6e90\u76d1\u63a7\u5de5\u5177","\u5f00\u6e90Java\u76d1\u63a7\u5de5\u5177","\u76d1\u63a7Nginx\u6307\u6807"]},p=void 0,i={unversionedId:"help/nginx",id:"help/nginx",title:"\u76d1\u63a7 Nginx",description:"\u6536\u96c6\u548c\u76d1\u63a7 Nginx \u7684\u5e38\u89c4\u6027\u80fd\u6307\u6807\u3002",source:"@site/docs/help/nginx.md",sourceDirName:"help",slug:"/help/nginx",permalink:"/docs/help/nginx",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/docs/help/nginx.md",tags:[],version:"current",frontMatter:{id:"nginx",title:"\u76d1\u63a7 Nginx",sidebar_label:"Nginx \u76d1\u63a7",keywords:["\u5f00\u6e90\u76d1\u63a7\u5de5\u5177","\u5f00\u6e90Java\u76d1\u63a7\u5de5\u5177","\u76d1\u63a7Nginx\u6307\u6807"]},sidebar:"docs",previous:{title:"SSL\u8bc1\u4e66\u76d1\u63a7",permalink:"/docs/help/ssl_cert"},next:{title:"POP3\u76d1\u63a7",permalink:"/docs/help/pop3"}},u={},o=[{value:"\u542f\u7528 Nginx \u7684 <code>ngx_http_stub_status_module</code> \u548c <code>ngx_http_reqstat_module</code> \u914d\u7f6e",id:"\u542f\u7528-nginx-\u7684-ngx_http_stub_status_module-\u548c-ngx_http_reqstat_module-\u914d\u7f6e",level:3},{value:"\u914d\u7f6e\u53c2\u6570",id:"\u914d\u7f6e\u53c2\u6570",level:3},{value:"\u6536\u96c6\u6307\u6807",id:"\u6536\u96c6\u6307\u6807",level:3},{value:"\u6307\u6807\u6536\u96c6\uff1anginx_status",id:"\u6307\u6807\u6536\u96c6nginx_status",level:4},{value:"\u6307\u6807\u96c6\uff1areq_status",id:"\u6307\u6807\u96c6req_status",level:4}],d={toc:o},m="wrapper";function s(t){let{components:e,...n}=t;return(0,r.kt)(m,(0,a.Z)({},d,n,{components:e,mdxType:"MDXLayout"}),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"\u6536\u96c6\u548c\u76d1\u63a7 Nginx \u7684\u5e38\u89c4\u6027\u80fd\u6307\u6807\u3002")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"\u4f7f\u7528\u7684\u534f\u8bae\uff1aNginx")),(0,r.kt)("h3",{id:"\u542f\u7528-nginx-\u7684-ngx_http_stub_status_module-\u548c-ngx_http_reqstat_module-\u914d\u7f6e"},"\u542f\u7528 Nginx \u7684 ",(0,r.kt)("inlineCode",{parentName:"h3"},"ngx_http_stub_status_module")," \u548c ",(0,r.kt)("inlineCode",{parentName:"h3"},"ngx_http_reqstat_module")," \u914d\u7f6e"),(0,r.kt)("p",null,"\u5982\u679c\u4f60\u60f3\u4f7f\u7528\u8fd9\u79cd\u76d1\u63a7\u65b9\u5f0f\u76d1\u63a7 'Nginx' \u7684\u4fe1\u606f\uff0c\u4f60\u9700\u8981\u4fee\u6539\u4f60\u7684 Nginx \u914d\u7f6e\u6587\u4ef6\u4ee5\u542f\u7528\u76d1\u63a7\u6a21\u5757\u3002"),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"1\u3001\u6dfb\u52a0 ngx_http_stub_status_module \u914d\u7f6e\uff1a")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-shell"},"# modify nginx.conf\nserver {\n        listen 80; # port\n        server_name localhost;\n        location /nginx-status {\n                stub_status     on;\n                access_log      on;\n                #allow 127.0.0.1;   #only allow requests from localhost\n                #deny all;      #deny all other hosts\n        }\n}\n")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"2\u3001\u6dfb\u52a0 ngx_http_reqstat_module \u914d\u7f6e\uff1a")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-shell"},"# install `ngx_http_reqstat_module`\nwget https://github.com/zls0424/ngx_req_status/archive/master.zip -O ngx_req_status.zip\n\nunzip ngx_req_status.zip\n\npatch -p1 < ../ngx_req_status-master/write_filter.patch\n\n./configure --prefix=/usr/local/nginx-1.4.2 --add-module=../ngx_req_status-master\n\nmake -j2\n\nmake install\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-shell"},"# modify nginx.conf\nhttp {\n        req_status_zone server_name $server_name 256k;\n        req_status_zone server_addr $server_addr 256k;\n        req_status_zone server_url  $server_name$uri 256k;\n        req_status server_name server_addr server_url;\n        server {\n            server_name xxx; // server_name\n            location /req-status {\n              req_status_show on;\n            }\n        }\n}\n")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"\u26a0\ufe0f",(0,r.kt)("inlineCode",{parentName:"strong"},"ngx_http_reqstat_module")," \u9700\u8981\u81ea\u884c\u4e0b\u8f7d\u5b89\u88c5\uff0c\u5982\u679c\u4e0d\u9700\u8981\u76d1\u63a7\u8be5\u6a21\u5757\uff0c\u53ea\u9700\u6536\u96c6 ",(0,r.kt)("inlineCode",{parentName:"strong"},"ngx_http_stub_status_module")," \u6a21\u5757\u7684\u4fe1\u606f\u5373\u53ef")),(0,r.kt)("p",null,(0,r.kt)("strong",{parentName:"p"},"\u8fd9\u91cc\u6709\u5e2e\u52a9\u6587\u6863\uff1a ",(0,r.kt)("a",{parentName:"strong",href:"https://blog.csdn.net/weixin_55985097/article/details/116722309"},"https://blog.csdn.net/weixin_55985097/article/details/116722309"))),(0,r.kt)("h3",{id:"\u914d\u7f6e\u53c2\u6570"},"\u914d\u7f6e\u53c2\u6570"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u53c2\u6570\u540d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u53c2\u6570\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u4e3b\u673a"),(0,r.kt)("td",{parentName:"tr",align:null},"\u88ab\u76d1\u63a7\u7684 IPV4\u3001IPV6 \u6216\u57df\u540d\u3002\u6ce8\u610f\u26a0\ufe0f\u4e0d\u9700\u8981\u534f\u8bae\u5934\u90e8\uff08\u4f8b\u5982\uff1ahttps://\uff0chttp://\uff09")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u540d\u79f0"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6807\u8bc6\u6b64\u76d1\u63a7\u7684\u540d\u79f0\u3002\u540d\u79f0\u9700\u8981\u552f\u4e00")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u7aef\u53e3"),(0,r.kt)("td",{parentName:"tr",align:null},"Nginx \u63d0\u4f9b\u7684\u7aef\u53e3")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u8d85\u65f6\u65f6\u95f4"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5141\u8bb8\u6536\u96c6\u54cd\u5e94\u65f6\u95f4")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u6536\u96c6\u95f4\u9694\u65f6\u95f4"),(0,r.kt)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u5468\u671f\u6027\u6570\u636e\u6536\u96c6\u7684\u95f4\u9694\u65f6\u95f4\uff0c\u5355\u4f4d\u4e3a\u79d2\uff0c\u6700\u5c0f\u53ef\u8bbe\u7f6e\u7684\u95f4\u9694\u65f6\u95f4\u4e3a30\u79d2")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u662f\u5426\u68c0\u6d4b"),(0,r.kt)("td",{parentName:"tr",align:null},"\u662f\u5426\u5728\u6dfb\u52a0\u76d1\u63a7\u4e4b\u524d\u68c0\u6d4b\u548c\u786e\u8ba4\u76d1\u63a7\u7684\u53ef\u7528\u6027\u3002\u53ea\u6709\u5728\u68c0\u6d4b\u6210\u529f\u540e\uff0c\u6dfb\u52a0\u548c\u4fee\u6539\u64cd\u4f5c\u624d\u4f1a\u7ee7\u7eed\u8fdb\u884c")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u63cf\u8ff0\u5907\u6ce8"),(0,r.kt)("td",{parentName:"tr",align:null},"\u7528\u6237\u53ef\u4ee5\u5728\u6b64\u5904\u6ce8\u660e\u6709\u5173\u6807\u8bc6\u548c\u63cf\u8ff0\u6b64\u76d1\u63a7\u7684\u66f4\u591a\u4fe1\u606f")))),(0,r.kt)("h3",{id:"\u6536\u96c6\u6307\u6807"},"\u6536\u96c6\u6307\u6807"),(0,r.kt)("h4",{id:"\u6307\u6807\u6536\u96c6nginx_status"},"\u6307\u6807\u6536\u96c6\uff1anginx_status"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u63a5\u6536\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5df2\u63a5\u53d7\u7684\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5904\u7406\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u6210\u529f\u5904\u7406\u7684\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u6d3b\u52a8\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6d3b\u52a8\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u4e22\u5f03\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u4e22\u5f03\u7684\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u8bf7\u6c42\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5ba2\u6237\u7aef\u8bf7\u6c42")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u8bfb\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u6b63\u5728\u6267\u884c\u8bfb\u64cd\u4f5c\u7684\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5199\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u6b63\u5728\u6267\u884c\u5199\u64cd\u4f5c\u7684\u8fde\u63a5")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u7b49\u5f85\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u7b49\u5f85\u8fde\u63a5")))),(0,r.kt)("h4",{id:"\u6307\u6807\u96c6req_status"},"\u6307\u6807\u96c6\uff1areq_status"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,r.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u63cf\u8ff0"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u7ec4\u7c7b\u522b"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u7ec4\u7c7b\u522b")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u7ec4\u540d\u79f0"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5206\u7ec4\u540d\u79f0")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u6700\u5927\u5e76\u53d1\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u6700\u5927\u5e76\u53d1\u8fde\u63a5\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u6700\u5927\u5e26\u5bbd"),(0,r.kt)("td",{parentName:"tr",align:null},"kb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u6700\u5927\u5e26\u5bbd")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u603b\u6d41\u91cf"),(0,r.kt)("td",{parentName:"tr",align:null},"kb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u603b\u6d41\u91cf")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u603b\u8bf7\u6c42\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u603b\u8bf7\u6c42\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u5e76\u53d1\u8fde\u63a5\u6570"),(0,r.kt)("td",{parentName:"tr",align:null}),(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u5e76\u53d1\u8fde\u63a5\u6570")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u5e26\u5bbd"),(0,r.kt)("td",{parentName:"tr",align:null},"kb"),(0,r.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u5e26\u5bbd")))))}s.isMDXComponent=!0}}]);