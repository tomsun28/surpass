"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[8894],{4137:function(e,t,a){a.d(t,{Zo:function(){return s},kt:function(){return m}});var r=a(7294);function n(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function o(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,r)}return a}function l(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?o(Object(a),!0).forEach((function(t){n(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):o(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function i(e,t){if(null==e)return{};var a,r,n=function(e,t){if(null==e)return{};var a,r,n={},o=Object.keys(e);for(r=0;r<o.length;r++)a=o[r],t.indexOf(a)>=0||(n[a]=e[a]);return n}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)a=o[r],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(n[a]=e[a])}return n}var c=r.createContext({}),p=function(e){var t=r.useContext(c),a=t;return e&&(a="function"==typeof e?e(t):l(l({},t),e)),a},s=function(e){var t=p(e.components);return r.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var a=e.components,n=e.mdxType,o=e.originalType,c=e.parentName,s=i(e,["components","mdxType","originalType","parentName"]),d=p(a),m=n,k=d["".concat(c,".").concat(m)]||d[m]||u[m]||o;return a?r.createElement(k,l(l({ref:t},s),{},{components:a})):r.createElement(k,l({ref:t},s))}));function m(e,t){var a=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var o=a.length,l=new Array(o);l[0]=d;var i={};for(var c in t)hasOwnProperty.call(t,c)&&(i[c]=t[c]);i.originalType=e,i.mdxType="string"==typeof e?e:n,l[1]=i;for(var p=2;p<o;p++)l[p]=a[p];return r.createElement.apply(null,l)}return r.createElement.apply(null,a)}d.displayName="MDXCreateElement"},5642:function(e,t,a){a.r(t),a.d(t,{frontMatter:function(){return i},contentTitle:function(){return c},metadata:function(){return p},toc:function(){return s},default:function(){return d}});var r=a(7462),n=a(3366),o=(a(7294),a(4137)),l=["components"],i={id:"quickstart",title:"Quick Start",sidebar_label:"Quick Start"},c=void 0,p={unversionedId:"start/quickstart",id:"start/quickstart",title:"Quick Start",description:"\ud83d\udc15 Quick Start",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/start/quickstart.md",sourceDirName:"start",slug:"/start/quickstart",permalink:"/en/docs/start/quickstart",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/start/quickstart.md",tags:[],version:"current",frontMatter:{id:"quickstart",title:"Quick Start",sidebar_label:"Quick Start"},sidebar:"docs",previous:{title:"Introduce",permalink:"/en/docs/"},next:{title:"TDengine Init(optional)",permalink:"/en/docs/start/tdengine-init"}},s=[{value:"\ud83d\udc15 Quick Start",id:"-quick-start",children:[],level:3},{value:"\ud83c\udf5e Install HertzBeat",id:"-install-hertzbeat",children:[{value:"1\uff1aInstall quickly via docker",id:"1install-quickly-via-docker",children:[],level:5},{value:"2\uff1aInstall via package",id:"2install-via-package",children:[],level:5},{value:"3\uff1aStart via source code",id:"3start-via-source-code",children:[],level:5},{value:"4\uff1aInstall All(mysql+tdengine+hertzbeat) via Docker-compose",id:"4install-allmysqltdenginehertzbeat-via-docker-compose",children:[],level:5}],level:3}],u={toc:s};function d(e){var t=e.components,a=(0,n.Z)(e,l);return(0,o.kt)("wrapper",(0,r.Z)({},u,a,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h3",{id:"-quick-start"},"\ud83d\udc15 Quick Start"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"If you don\u2019t want to deploy but use it directly, we provide ",(0,o.kt)("a",{parentName:"li",href:"https://console.tancloud.cn"},"SAAS Monitoring Cloud-TanCloud"),", ",(0,o.kt)("strong",{parentName:"li"},(0,o.kt)("a",{parentName:"strong",href:"https://console.tancloud.cn"},"Log In And Register For Free")),"."),(0,o.kt)("li",{parentName:"ul"},"If you want to deploy HertzBeat local, please refer to the following Deployment Documentation for operation.")),(0,o.kt)("h3",{id:"-install-hertzbeat"},"\ud83c\udf5e Install HertzBeat"),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},"HertzBeat supports installation through source code, docker or package, cpu support X86/ARM64.")),(0,o.kt)("h5",{id:"1install-quickly-via-docker"},"1\uff1aInstall quickly via docker"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Just one command to get started: ",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat"))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Access ",(0,o.kt)("inlineCode",{parentName:"p"},"localhost:1157")," to start, default account: ",(0,o.kt)("inlineCode",{parentName:"p"},"admin/hertzbeat")))),(0,o.kt)("p",null,"Detailed config refer to ",(0,o.kt)("a",{parentName:"p",href:"https://hertzbeat.com/docs/start/docker-deploy"},"Install HertzBeat via Docker")),(0,o.kt)("h5",{id:"2install-via-package"},"2\uff1aInstall via package"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Download the installation package ",(0,o.kt)("a",{parentName:"li",href:"https://gitee.com/dromara/hertzbeat/releases"},"GITEE Release")," ",(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/releases"},"GITHUB Release")),(0,o.kt)("li",{parentName:"ol"},"Need Jdk Environment, ",(0,o.kt)("inlineCode",{parentName:"li"},"jdk11")),(0,o.kt)("li",{parentName:"ol"},"[optional]","Configure the HertzBeat configuration yml file ",(0,o.kt)("inlineCode",{parentName:"li"},"hertzbeat/config/application.yml")),(0,o.kt)("li",{parentName:"ol"},"Run shell ",(0,o.kt)("inlineCode",{parentName:"li"},"$ ./startup.sh ")),(0,o.kt)("li",{parentName:"ol"},"Access ",(0,o.kt)("inlineCode",{parentName:"li"},"localhost:1157")," to start, default account: ",(0,o.kt)("inlineCode",{parentName:"li"},"admin/hertzbeat"))),(0,o.kt)("p",null,"Detailed config refer to ",(0,o.kt)("a",{parentName:"p",href:"https://hertzbeat.com/docs/start/package-deploy"},"Install HertzBeat via Package")),(0,o.kt)("h5",{id:"3start-via-source-code"},"3\uff1aStart via source code"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Local source code debugging needs to start the back-end project manager and the front-end project web-app."),(0,o.kt)("li",{parentName:"ol"},"Backend\uff1aneed ",(0,o.kt)("inlineCode",{parentName:"li"},"maven3+"),", ",(0,o.kt)("inlineCode",{parentName:"li"},"java11"),", ",(0,o.kt)("inlineCode",{parentName:"li"},"lombok"),", start the manager service."),(0,o.kt)("li",{parentName:"ol"},"Web\uff1aneed ",(0,o.kt)("inlineCode",{parentName:"li"},"nodejs npm angular-cli")," environment, Run ",(0,o.kt)("inlineCode",{parentName:"li"},"ng serve --open")," in ",(0,o.kt)("inlineCode",{parentName:"li"},"web-app")," directory after backend startup."),(0,o.kt)("li",{parentName:"ol"},"Access ",(0,o.kt)("inlineCode",{parentName:"li"},"localhost:4200")," to start, default account: ",(0,o.kt)("inlineCode",{parentName:"li"},"admin/hertzbeat"))),(0,o.kt)("p",null,"Detailed steps refer to ",(0,o.kt)("a",{parentName:"p",href:"../others/contributing"},"CONTRIBUTING"),"   "),(0,o.kt)("h5",{id:"4install-allmysqltdenginehertzbeat-via-docker-compose"},"4\uff1aInstall All(mysql+tdengine+hertzbeat) via Docker-compose"),(0,o.kt)("p",null,"Install and deploy the mysql database, tdengine database and hertzbeat at one time through ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/dromara/hertzbeat/tree/master/script/docker-compose"},"docker-compose deployment script"),"."),(0,o.kt)("p",null,"Detailed steps refer to ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/dromara/hertzbeat/tree/master/script/docker-compose"},"Install via Docker-Compose")),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"HAVE FUN")))}d.isMDXComponent=!0}}]);