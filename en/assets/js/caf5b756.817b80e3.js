"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[9196],{4137:function(t,e,r){r.d(e,{Zo:function(){return m},kt:function(){return s}});var a=r(7294);function n(t,e,r){return e in t?Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}):t[e]=r,t}function o(t,e){var r=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),r.push.apply(r,a)}return r}function l(t){for(var e=1;e<arguments.length;e++){var r=null!=arguments[e]?arguments[e]:{};e%2?o(Object(r),!0).forEach((function(e){n(t,e,r[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(r,e))}))}return t}function p(t,e){if(null==t)return{};var r,a,n=function(t,e){if(null==t)return{};var r,a,n={},o=Object.keys(t);for(a=0;a<o.length;a++)r=o[a],e.indexOf(r)>=0||(n[r]=t[r]);return n}(t,e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);for(a=0;a<o.length;a++)r=o[a],e.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(t,r)&&(n[r]=t[r])}return n}var i=a.createContext({}),u=function(t){var e=a.useContext(i),r=e;return t&&(r="function"==typeof t?t(e):l(l({},e),t)),r},m=function(t){var e=u(t.components);return a.createElement(i.Provider,{value:e},t.children)},h={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},c=a.forwardRef((function(t,e){var r=t.components,n=t.mdxType,o=t.originalType,i=t.parentName,m=p(t,["components","mdxType","originalType","parentName"]),c=u(r),s=n,b=c["".concat(i,".").concat(s)]||c[s]||h[s]||o;return r?a.createElement(b,l(l({ref:e},m),{},{components:r})):a.createElement(b,l({ref:e},m))}));function s(t,e){var r=arguments,n=e&&e.mdxType;if("string"==typeof t||n){var o=r.length,l=new Array(o);l[0]=c;var p={};for(var i in e)hasOwnProperty.call(e,i)&&(p[i]=e[i]);p.originalType=t,p.mdxType="string"==typeof t?t:n,l[1]=p;for(var u=2;u<o;u++)l[u]=r[u];return a.createElement.apply(null,l)}return a.createElement.apply(null,r)}c.displayName="MDXCreateElement"},4184:function(t,e,r){r.r(e),r.d(e,{frontMatter:function(){return p},contentTitle:function(){return i},metadata:function(){return u},assets:function(){return m},toc:function(){return h},default:function(){return s}});var a=r(7462),n=r(3366),o=(r(7294),r(4137)),l=["components"],p={title:"\u4e91\u76d1\u63a7\u7cfb\u7edf HertzBeat v1.1.3 \u53d1\u5e03\uff01",author:"tom",author_title:"tom",author_url:"https://github.com/tomsun28",author_image_url:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4",tags:["opensource"]},i=void 0,u={permalink:"/en/blog/2022/09/04/hertzbeat-v1.1.3",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/blog/2022-09-04-hertzbeat-v1.1.3.md",source:"@site/blog/2022-09-04-hertzbeat-v1.1.3.md",title:"\u4e91\u76d1\u63a7\u7cfb\u7edf HertzBeat v1.1.3 \u53d1\u5e03\uff01",description:"Home: hertzbeat.com | tancloud.cn",date:"2022-09-04T00:00:00.000Z",formattedDate:"September 4, 2022",tags:[{label:"opensource",permalink:"/en/blog/tags/opensource"}],readingTime:2.15,truncated:!1,authors:[{name:"tom",title:"tom",url:"https://github.com/tomsun28",imageURL:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4"}],prevItem:{title:"SSL\u8bc1\u4e66\u8fc7\u671f\u76d1\u63a7\u6700\u4f73\u5b9e\u8df5",permalink:"/en/blog/2022/09/10/ssl-practice"},nextItem:{title:"HertzBeat v1.1.1 is Publish\uff01",permalink:"/en/blog/2022/07/10/hertzbeat-v1.1.1"}},m={authorsImageUrls:[void 0]},h=[{value:"V1.1.3",id:"v113",children:[],level:2},{value:"Have Fun!",id:"have-fun",children:[],level:2}],c={toc:h};function s(t){var e=t.components,r=(0,n.Z)(t,l);return(0,o.kt)("wrapper",(0,a.Z)({},c,r,{components:e,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"Home: hertzbeat.com | tancloud.cn"),(0,o.kt)("p",null,"Hi gays! HertzBeat v1.1.3 is coming. This version supports kafka monitor, ssl certificate expired monitor and more. Fixed several bugs and improved the overall stable usability."),(0,o.kt)("p",null,"Only one docker command is needed to install and experience hertzbeat\uff1a\n",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat")),(0,o.kt)("p",null,"Thanks to the contributors! \ud83d\udc4d\ud83d\udc4d"),(0,o.kt)("p",null,"Feature\uff1a"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/260"},"[web-app]feature:update monitors layout, support host copy to clipboard #260")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/263"},"[monitor] feature: support apache kafka monitor #263")," contribute by @wang1027-wqh"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/265"},"[webapp] support history chart query 3 mouth time range #265")," issue by @ericfrol"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/266"},"[monitor] support ssl certificate expired monitor #266")," suggest by @noear"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/268"},"[web-app] update default interval 600s to 120s #268")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/272"},"[web-app] update layout ui - help button, nav menu #272")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/273"},"[alert,webapp] support delete all alerts at once. #273")," issue by @ericfrol"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/276"},"[web-app] update home background image #276"))),(0,o.kt)("p",null,"Bugfix."),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/262"},"[docs] fix extend-http-jsonpath.md parseScript error #262")," contribute by @woshiniusange    ."),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/264"},"[monitor] update help docs, refactor redis metrics name #264")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/270"},"[manager] bugfix alert tags is null when tags map key normal value null. #270")," issue by ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/hello_brother_niu"},"https://gitee.com/hello_brother_niu")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/275"},"[alert] bugfix: the alert global preset config do not take effect #275")," issue by ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/hello_brother_niu"},"https://gitee.com/hello_brother_niu"))),(0,o.kt)("p",null,"Online ",(0,o.kt)("a",{parentName:"p",href:"https://console.tancloud.cn"},"https://console.tancloud.cn"),"."),(0,o.kt)("p",null,"Have Fun!"),(0,o.kt)("hr",null),(0,o.kt)("h2",{id:"v113"},"V1.1.3"),(0,o.kt)("p",null,"\u5b98\u7f51: hertzbeat.com | tancloud.cn"),(0,o.kt)("p",null,"\u5927\u5bb6\u597d\uff0cHertzBeat v1.1.3 \u53d1\u5e03\u5566\uff01\u8fd9\u4e2a\u7248\u672c\u652f\u6301\u4e86apache kafka\u76d1\u63a7\uff0cSSL\u8bc1\u4e66\u8fc7\u671f\u76d1\u63a7\u7b49\u3002\u4fee\u590d\u4e86\u82e5\u5e72bug\uff0c\u63d0\u5347\u6574\u4f53\u7a33\u5b9a\u6027\u3002"),(0,o.kt)("p",null,"\u53ea\u9700\u8981\u4e00\u6761docker\u547d\u4ee4\u5373\u53ef\u5b89\u88c5\u4f53\u9a8chertzbeat \uff1a\n",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat")),(0,o.kt)("p",null,"\u611f\u8c22hertzbeat\u8d21\u732e\u8005\u4eec\u7684\u8d21\u732e\uff01\ud83d\udc4d\ud83d\udc4d"),(0,o.kt)("p",null,"Feature\uff1a"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/260"},"[web-app]feature:update monitors layout, support host copy to clipboard #260")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/263"},"[monitor] feature: support apache kafka monitor #263")," contribute by @wang1027-wqh"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/265"},"[webapp] support history chart query 3 mouth time range #265")," issue by @ericfrol"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/266"},"[monitor] support ssl certificate expired monitor #266")," suggest by @noear"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/268"},"[web-app] update default interval 600s to 120s #268")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/272"},"[web-app] update layout ui - help button, nav menu #272")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/273"},"[alert,webapp] support delete all alerts at once. #273")," issue by @ericfrol"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/276"},"[web-app] update home background image #276"))),(0,o.kt)("p",null,"Bugfix."),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/262"},"[docs] fix extend-http-jsonpath.md parseScript error #262")," contribute by @woshiniusange    ."),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/264"},"[monitor] update help docs, refactor redis metrics name #264")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/270"},"[manager] bugfix alert tags is null when tags map key normal value null. #270")," issue by ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/hello_brother_niu"},"https://gitee.com/hello_brother_niu")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/275"},"[alert] bugfix: the alert global preset config do not take effect #275")," issue by ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/hello_brother_niu"},"https://gitee.com/hello_brother_niu"))),(0,o.kt)("p",null,"Online ",(0,o.kt)("a",{parentName:"p",href:"https://console.tancloud.cn"},"https://console.tancloud.cn"),"."),(0,o.kt)("h2",{id:"have-fun"},"Have Fun!"))}s.isMDXComponent=!0}}]);