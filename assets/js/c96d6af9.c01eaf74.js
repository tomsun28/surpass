"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[397],{4137:function(e,n,t){t.d(n,{Zo:function(){return u},kt:function(){return d}});var a=t(7294);function r(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function p(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);n&&(a=a.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,a)}return t}function o(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?p(Object(t),!0).forEach((function(n){r(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):p(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function l(e,n){if(null==e)return{};var t,a,r=function(e,n){if(null==e)return{};var t,a,r={},p=Object.keys(e);for(a=0;a<p.length;a++)t=p[a],n.indexOf(t)>=0||(r[t]=e[t]);return r}(e,n);if(Object.getOwnPropertySymbols){var p=Object.getOwnPropertySymbols(e);for(a=0;a<p.length;a++)t=p[a],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(r[t]=e[t])}return r}var i=a.createContext({}),s=function(e){var n=a.useContext(i),t=n;return e&&(t="function"==typeof e?e(n):o(o({},n),e)),t},u=function(e){var n=s(e.components);return a.createElement(i.Provider,{value:n},e.children)},m={inlineCode:"code",wrapper:function(e){var n=e.children;return a.createElement(a.Fragment,{},n)}},c=a.forwardRef((function(e,n){var t=e.components,r=e.mdxType,p=e.originalType,i=e.parentName,u=l(e,["components","mdxType","originalType","parentName"]),c=s(t),d=r,h=c["".concat(i,".").concat(d)]||c[d]||m[d]||p;return t?a.createElement(h,o(o({ref:n},u),{},{components:t})):a.createElement(h,o({ref:n},u))}));function d(e,n){var t=arguments,r=n&&n.mdxType;if("string"==typeof e||r){var p=t.length,o=new Array(p);o[0]=c;var l={};for(var i in n)hasOwnProperty.call(n,i)&&(l[i]=n[i]);l.originalType=e,l.mdxType="string"==typeof e?e:r,o[1]=l;for(var s=2;s<p;s++)o[s]=t[s];return a.createElement.apply(null,o)}return a.createElement.apply(null,t)}c.displayName="MDXCreateElement"},3187:function(e,n,t){t.r(n),t.d(n,{frontMatter:function(){return l},contentTitle:function(){return i},metadata:function(){return s},toc:function(){return u},default:function(){return c}});var a=t(3117),r=t(102),p=(t(7294),t(4137)),o=["components"],l={id:"extend-http",title:"HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u76d1\u63a7",sidebar_label:"HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u76d1\u63a7"},i=void 0,s={unversionedId:"advanced/extend-http",id:"version-v1.0.0/advanced/extend-http",title:"HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u76d1\u63a7",description:"\u4ece\u81ea\u5b9a\u4e49\u76d1\u63a7\u4e86\u89e3\u719f\u6089\u4e86\u600e\u4e48\u81ea\u5b9a\u4e49\u7c7b\u578b\uff0c\u6307\u6807\uff0c\u534f\u8bae\u7b49\uff0c\u8fd9\u91cc\u6211\u4eec\u6765\u8be6\u7ec6\u4ecb\u7ecd\u4e0b\u7528HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u6307\u6807\u76d1\u63a7\u3002",source:"@site/versioned_docs/version-v1.0.0/advanced/extend-http.md",sourceDirName:"advanced",slug:"/advanced/extend-http",permalink:"/docs/v1.0.0/advanced/extend-http",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/versioned_docs/version-v1.0.0/advanced/extend-http.md",tags:[],version:"v1.0.0",frontMatter:{id:"extend-http",title:"HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u76d1\u63a7",sidebar_label:"HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u76d1\u63a7"},sidebar:"docs",previous:{title:"\u81ea\u5b9a\u4e49\u76d1\u63a7",permalink:"/docs/v1.0.0/advanced/extend-point"},next:{title:"\u7cfb\u7edf\u9ed8\u8ba4\u89e3\u6790\u65b9\u5f0f",permalink:"/docs/v1.0.0/advanced/extend-http-default"}},u=[{value:"HTTP\u534f\u8bae\u91c7\u96c6\u6d41\u7a0b",id:"http\u534f\u8bae\u91c7\u96c6\u6d41\u7a0b",children:[],level:3},{value:"\u81ea\u5b9a\u4e49\u6b65\u9aa4",id:"\u81ea\u5b9a\u4e49\u6b65\u9aa4",children:[],level:3},{value:"\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6",id:"\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6",children:[],level:3},{value:"\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6",id:"\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6",children:[],level:3}],m={toc:u};function c(e){var n=e.components,t=(0,r.Z)(e,o);return(0,p.kt)("wrapper",(0,a.Z)({},m,t,{components:n,mdxType:"MDXLayout"}),(0,p.kt)("blockquote",null,(0,p.kt)("p",{parentName:"blockquote"},"\u4ece",(0,p.kt)("a",{parentName:"p",href:"extend-point"},"\u81ea\u5b9a\u4e49\u76d1\u63a7"),"\u4e86\u89e3\u719f\u6089\u4e86\u600e\u4e48\u81ea\u5b9a\u4e49\u7c7b\u578b\uff0c\u6307\u6807\uff0c\u534f\u8bae\u7b49\uff0c\u8fd9\u91cc\u6211\u4eec\u6765\u8be6\u7ec6\u4ecb\u7ecd\u4e0b\u7528HTTP\u534f\u8bae\u81ea\u5b9a\u4e49\u6307\u6807\u76d1\u63a7\u3002   ")),(0,p.kt)("h3",{id:"http\u534f\u8bae\u91c7\u96c6\u6d41\u7a0b"},"HTTP\u534f\u8bae\u91c7\u96c6\u6d41\u7a0b"),(0,p.kt)("p",null,"\u3010",(0,p.kt)("strong",{parentName:"p"},"HTTP\u63a5\u53e3\u8c03\u7528"),"\u3011->\u3010",(0,p.kt)("strong",{parentName:"p"},"\u54cd\u5e94\u6821\u9a8c"),"\u3011->\u3010",(0,p.kt)("strong",{parentName:"p"},"\u54cd\u5e94\u6570\u636e\u89e3\u6790"),"\u3011->\u3010",(0,p.kt)("strong",{parentName:"p"},"\u9ed8\u8ba4\u65b9\u5f0f\u89e3\u6790\uff5cJsonPath\u811a\u672c\u89e3\u6790 | XmlPath\u89e3\u6790(todo) | Prometheus\u89e3\u6790(todo)"),"\u3011->\u3010",(0,p.kt)("strong",{parentName:"p"},"\u6307\u6807\u6570\u636e\u63d0\u53d6"),"\u3011"),(0,p.kt)("p",null,"\u7531\u6d41\u7a0b\u53ef\u89c1\uff0c\u6211\u4eec\u81ea\u5b9a\u4e49\u4e00\u4e2aHTTP\u534f\u8bae\u7684\u76d1\u63a7\u7c7b\u578b\uff0c\u9700\u8981\u914d\u7f6eHTTP\u8bf7\u6c42\u53c2\u6570\uff0c\u914d\u7f6e\u83b7\u53d6\u54ea\u4e9b\u6307\u6807\uff0c\u5bf9\u54cd\u5e94\u6570\u636e\u914d\u7f6e\u89e3\u6790\u65b9\u5f0f\u548c\u89e3\u6790\u811a\u672c\u3002",(0,p.kt)("br",{parentName:"p"}),"\n","HTTP\u534f\u8bae\u652f\u6301\u6211\u4eec\u81ea\u5b9a\u4e49HTTP\u8bf7\u6c42\u8def\u5f84\uff0c\u8bf7\u6c42header\uff0c\u8bf7\u6c42\u53c2\u6570\uff0c\u8bf7\u6c42\u65b9\u5f0f\uff0c\u8bf7\u6c42\u4f53\u7b49\u3002   "),(0,p.kt)("p",null,(0,p.kt)("strong",{parentName:"p"},"\u7cfb\u7edf\u9ed8\u8ba4\u89e3\u6790\u65b9\u5f0f"),"\uff1ahttp\u63a5\u53e3\u8fd4\u56dehertzbeat\u89c4\u5b9a\u7684json\u6570\u636e\u7ed3\u6784\uff0c\u5373\u53ef\u7528\u9ed8\u8ba4\u89e3\u6790\u65b9\u5f0f\u89e3\u6790\u6570\u636e\u63d0\u53d6\u5bf9\u5e94\u7684\u6307\u6807\u6570\u636e\uff0c\u8be6\u7ec6\u4ecb\u7ecd\u89c1 ",(0,p.kt)("a",{parentName:"p",href:"extend-http-default"},(0,p.kt)("strong",{parentName:"a"},"\u7cfb\u7edf\u9ed8\u8ba4\u89e3\u6790")),(0,p.kt)("br",{parentName:"p"}),"\n",(0,p.kt)("strong",{parentName:"p"},"JsonPath\u811a\u672c\u89e3\u6790\u65b9\u5f0f"),"\uff1a\u7528JsonPath\u811a\u672c\u5bf9\u54cd\u5e94\u7684json\u6570\u636e\u8fdb\u884c\u89e3\u6790\uff0c\u8fd4\u56de\u7cfb\u7edf\u6307\u5b9a\u7684\u6570\u636e\u7ed3\u6784\uff0c\u7136\u540e\u63d0\u4f9b\u5bf9\u5e94\u7684\u6307\u6807\u6570\u636e\uff0c\u8be6\u7ec6\u4ecb\u7ecd\u89c1 ",(0,p.kt)("a",{parentName:"p",href:"extend-http-jsonpath"},(0,p.kt)("strong",{parentName:"a"},"JsonPath\u811a\u672c\u89e3\u6790")),"    "),(0,p.kt)("h3",{id:"\u81ea\u5b9a\u4e49\u6b65\u9aa4"},"\u81ea\u5b9a\u4e49\u6b65\u9aa4"),(0,p.kt)("p",null,"\u914d\u7f6e\u81ea\u5b9a\u4e49\u76d1\u63a7\u7c7b\u578b\u9700\u65b0\u589e\u914d\u7f6e\u4e24\u4e2aYML\u6587\u4ef6"),(0,p.kt)("ol",null,(0,p.kt)("li",{parentName:"ol"},"\u7528\u76d1\u63a7\u7c7b\u578b\u547d\u540d\u7684\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6 - \u4f8b\u5982\uff1aexample.yml \u9700\u4f4d\u4e8e\u5b89\u88c5\u76ee\u5f55 /hertzbeat/define/app/ \u4e0b"),(0,p.kt)("li",{parentName:"ol"},"\u7528\u76d1\u63a7\u7c7b\u578b\u547d\u540d\u7684\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6 - \u4f8b\u5982\uff1aexample.yml \u9700\u4f4d\u4e8e\u5b89\u88c5\u76ee\u5f55 /hertzbeat/define/param/ \u4e0b"),(0,p.kt)("li",{parentName:"ol"},"\u91cd\u542fhertzbeat\u7cfb\u7edf\uff0c\u6211\u4eec\u5c31\u9002\u914d\u597d\u4e86\u4e00\u4e2a\u65b0\u7684\u81ea\u5b9a\u4e49\u76d1\u63a7\u7c7b\u578b\u3002")),(0,p.kt)("hr",null),(0,p.kt)("p",null,"\u4e0b\u9762\u8be6\u7ec6\u4ecb\u7ecd\u4e0b\u8fd9\u4fe9\u6587\u4ef6\u7684\u914d\u7f6e\u7528\u6cd5\uff0c\u8bf7\u6ce8\u610f\u770b\u4f7f\u7528\u6ce8\u91ca\u3002   "),(0,p.kt)("h3",{id:"\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6"},"\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6"),(0,p.kt)("blockquote",null,(0,p.kt)("p",{parentName:"blockquote"},"\u76d1\u63a7\u914d\u7f6e\u5b9a\u4e49\u6587\u4ef6\u7528\u4e8e\u5b9a\u4e49 ",(0,p.kt)("em",{parentName:"p"},"\u76d1\u63a7\u7c7b\u578b\u7684\u540d\u79f0(\u56fd\u9645\u5316), \u8bf7\u6c42\u53c2\u6570\u6620\u5c04, \u6307\u6807\u4fe1\u606f, \u91c7\u96c6\u534f\u8bae\u914d\u7f6e\u4fe1\u606f"),"\u7b49\u3002  ")),(0,p.kt)("p",null,"\u6837\u4f8b\uff1a\u81ea\u5b9a\u4e49\u4e00\u4e2a\u540d\u79f0\u4e3aexample_http\u7684\u81ea\u5b9a\u4e49\u76d1\u63a7\u7c7b\u578b\uff0c\u5176\u4f7f\u7528HTTP\u534f\u8bae\u91c7\u96c6\u6307\u6807\u6570\u636e\u3002",(0,p.kt)("br",{parentName:"p"}),"\n","\u6587\u4ef6\u540d\u79f0: example_http.yml \u4f4d\u4e8e /define/app/example_http.yml   "),(0,p.kt)("pre",null,(0,p.kt)("code",{parentName:"pre",className:"language-yaml"},"# \u6b64\u76d1\u63a7\u7c7b\u578b\u6240\u5c5e\u7c7b\u522b\uff1aservice-\u5e94\u7528\u670d\u52a1\u76d1\u63a7 db-\u6570\u636e\u5e93\u76d1\u63a7 custom-\u81ea\u5b9a\u4e49\u76d1\u63a7 os-\u64cd\u4f5c\u7cfb\u7edf\u76d1\u63a7\ncategory: custom\n# \u76d1\u63a7\u5e94\u7528\u7c7b\u578b(\u4e0e\u6587\u4ef6\u540d\u4fdd\u6301\u4e00\u81f4) eg: linux windows tomcat mysql aws...\napp: example_http\nname:\n  zh-CN: \u6a21\u62df\u5e94\u7528\u7c7b\u578b\n  en-US: EXAMPLE APP\n# \u53c2\u6570\u6620\u5c04map. \u8fd9\u4e9b\u4e3a\u8f93\u5165\u53c2\u6570\u53d8\u91cf\uff0c\u5373\u53ef\u4ee5\u7528^_^host^_^\u7684\u5f62\u5f0f\u5199\u5230\u540e\u9762\u7684\u914d\u7f6e\u4e2d\uff0c\u7cfb\u7edf\u81ea\u52a8\u53d8\u91cf\u503c\u66ff\u6362\n# type\u662f\u53c2\u6570\u7c7b\u578b: 0-number\u6570\u5b57, 1-string\u660e\u6587\u5b57\u7b26\u4e32, 2-secret\u52a0\u5bc6\u5b57\u7b26\u4e32\n# \u5f3a\u5236\u56fa\u5b9a\u5fc5\u987b\u53c2\u6570 - host\nconfigmap:\n  - key: host\n    type: 1\n  - key: port\n    type: 0\n  - key: username\n    type: 1\n  - key: password\n    type: 2\n# \u6307\u6807\u7ec4\u5217\u8868\nmetrics:\n# \u7b2c\u4e00\u4e2a\u76d1\u63a7\u6307\u6807\u7ec4 cpu\n# \u6ce8\u610f\uff1a\u5185\u7f6e\u76d1\u63a7\u6307\u6807\u6709 (responseTime - \u54cd\u5e94\u65f6\u95f4)\n  - name: cpu\n    # \u6307\u6807\u7ec4\u8c03\u5ea6\u4f18\u5148\u7ea7(0-127)\u8d8a\u5c0f\u4f18\u5148\u7ea7\u8d8a\u9ad8,\u4f18\u5148\u7ea7\u4f4e\u7684\u6307\u6807\u7ec4\u4f1a\u7b49\u4f18\u5148\u7ea7\u9ad8\u7684\u6307\u6807\u7ec4\u91c7\u96c6\u5b8c\u6210\u540e\u624d\u4f1a\u88ab\u8c03\u5ea6,\u76f8\u540c\u4f18\u5148\u7ea7\u7684\u6307\u6807\u7ec4\u4f1a\u5e76\u884c\u8c03\u5ea6\u91c7\u96c6\n    # \u4f18\u5148\u7ea7\u4e3a0\u7684\u6307\u6807\u7ec4\u4e3a\u53ef\u7528\u6027\u6307\u6807\u7ec4,\u5373\u5b83\u4f1a\u88ab\u9996\u5148\u8c03\u5ea6,\u91c7\u96c6\u6210\u529f\u624d\u4f1a\u7ee7\u7eed\u8c03\u5ea6\u5176\u5b83\u6307\u6807\u7ec4,\u91c7\u96c6\u5931\u8d25\u5219\u4e2d\u65ad\u8c03\u5ea6\n    priority: 0\n    # \u6307\u6807\u7ec4\u4e2d\u7684\u5177\u4f53\u76d1\u63a7\u6307\u6807\n    fields:\n      # \u6307\u6807\u4fe1\u606f \u5305\u62ec field\u540d\u79f0   type\u5b57\u6bb5\u7c7b\u578b:0-number\u6570\u5b57,1-string\u5b57\u7b26\u4e32   instance\u662f\u5426\u4e3a\u5b9e\u4f8b\u4e3b\u952e   unit:\u6307\u6807\u5355\u4f4d\n      - field: hostname\n        type: 1\n        instance: true\n      - field: usage\n        type: 0\n        unit: '%'\n      - field: cores\n        type: 0\n      - field: waitTime\n        type: 0\n        unit: s\n# (\u975e\u5fc5\u987b)\u76d1\u63a7\u6307\u6807\u522b\u540d\uff0c\u4e0e\u4e0a\u9762\u7684\u6307\u6807\u540d\u6620\u5c04\u3002\u7528\u4e8e\u91c7\u96c6\u63a5\u53e3\u6570\u636e\u5b57\u6bb5\u4e0d\u76f4\u63a5\u662f\u6700\u7ec8\u6307\u6807\u540d\u79f0,\u9700\u8981\u6b64\u522b\u540d\u505a\u6620\u5c04\u8f6c\u6362\n    aliasFields:\n      - hostname\n      - core1\n      - core2\n      - usage\n      - allTime\n      - runningTime\n# (\u975e\u5fc5\u987b)\u6307\u6807\u8ba1\u7b97\u8868\u8fbe\u5f0f,\u4e0e\u4e0a\u9762\u7684\u522b\u540d\u4e00\u8d77\u4f5c\u7528,\u8ba1\u7b97\u51fa\u6700\u7ec8\u9700\u8981\u7684\u6307\u6807\u503c\n# eg: cores=core1+core2, usage=usage, waitTime=allTime-runningTime\n    calculates:\n      - hostname=hostname\n      - cores=core1+core2\n      - usage=usage\n      - waitTime=allTime-runningTime\n# \u76d1\u63a7\u91c7\u96c6\u4f7f\u7528\u534f\u8bae eg: sql, ssh, http, telnet, wmi, snmp, sdk\n    protocol: http\n# \u5f53protocol\u4e3ahttp\u534f\u8bae\u65f6\u5177\u4f53\u7684\u91c7\u96c6\u914d\u7f6e\n    http:\n      # \u4e3b\u673ahost: ipv4 ipv6 \u57df\u540d\n      host: ^_^host^_^\n      # \u7aef\u53e3\n      port: ^_^port^_^\n      # url\u8bf7\u6c42\u63a5\u53e3\u8def\u5f84\n      url: /metrics/cpu\n      # \u8bf7\u6c42\u65b9\u5f0f GET POST PUT DELETE PATCH\n      method: GET\n      # \u662f\u5426\u542f\u7528ssl/tls,\u5373\u662fhttp\u8fd8\u662fhttps,\u9ed8\u8ba4false\n      ssl: false\n      # \u8bf7\u6c42\u5934\u5185\u5bb9\n      headers:\n        apiVersion: v1\n      # \u8bf7\u6c42\u53c2\u6570\u5185\u5bb9\n      params:\n        param1: param1\n        param2: param2\n      # \u8ba4\u8bc1\n      authorization:\n        # \u8ba4\u8bc1\u65b9\u5f0f: Basic Auth, Digest Auth, Bearer Token\n        type: Basic Auth\n        basicAuthUsername: ^_^username^_^\n        basicAuthPassword: ^_^password^_^\n      # \u54cd\u5e94\u6570\u636e\u89e3\u6790\u65b9\u5f0f: default-\u7cfb\u7edf\u89c4\u5219,jsonPath-jsonPath\u811a\u672c,website-\u7f51\u7ad9\u53ef\u7528\u6027\u6307\u6807\u76d1\u63a7\n      # todo xmlPath-xmlPath\u811a\u672c,prometheus-Prometheus\u6570\u636e\u89c4\u5219\n      parseType: jsonPath\n      parseScript: '$'\n\n  - name: memory\n    priority: 1\n    fields:\n      - field: hostname\n        type: 1\n        instance: true\n      - field: total\n        type: 0\n        unit: kb\n      - field: usage\n        type: 0\n        unit: '%'\n      - field: speed\n        type: 0\n    protocol: http\n    http:\n      host: ^_^host^_^\n      port: ^_^port^_^\n      url: /metrics/memory\n      method: GET\n      headers:\n        apiVersion: v1\n      params:\n        param1: param1\n        param2: param2\n      authorization:\n        type: Basic Auth\n        basicAuthUsername: ^_^username^_^\n        basicAuthPassword: ^_^password^_^\n      parseType: default\n")),(0,p.kt)("h3",{id:"\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6"},"\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6"),(0,p.kt)("blockquote",null,(0,p.kt)("p",{parentName:"blockquote"},"\u76d1\u63a7\u53c2\u6570\u5b9a\u4e49\u6587\u4ef6\u7528\u4e8e\u5b9a\u4e49 ",(0,p.kt)("em",{parentName:"p"},"\u9700\u8981\u7684\u8f93\u5165\u53c2\u6570\u5b57\u6bb5\u7ed3\u6784\u5b9a\u4e49(\u524d\u7aef\u9875\u9762\u6839\u636e\u7ed3\u6784\u6e32\u67d3\u8f93\u5165\u53c2\u6570\u6846)"),"\u3002   ")),(0,p.kt)("p",null,"\u6837\u4f8b\uff1a\u81ea\u5b9a\u4e49\u4e00\u4e2a\u540d\u79f0\u4e3aexample_http\u7684\u81ea\u5b9a\u4e49\u76d1\u63a7\u7c7b\u578b\uff0c\u5176\u4f7f\u7528HTTP\u534f\u8bae\u91c7\u96c6\u6307\u6807\u6570\u636e\u3002",(0,p.kt)("br",{parentName:"p"}),"\n","\u6587\u4ef6\u540d\u79f0: example_http.yml \u4f4d\u4e8e //define/param/example_http.yml   "),(0,p.kt)("pre",null,(0,p.kt)("code",{parentName:"pre",className:"language-yaml"},"# \u76d1\u63a7\u5e94\u7528\u7c7b\u578b\u540d\u79f0(\u4e0e\u6587\u4ef6\u540d\u4fdd\u6301\u4e00\u81f4) eg: linux windows tomcat mysql aws...\napp: example_http\n# \u5f3a\u5236\u56fa\u5b9a\u5fc5\u987b\u53c2\u6570 - host(ipv4,ipv6,\u57df\u540d)\nparam:\n    # field-\u5b57\u6bb5\u540d\u79f0\u6807\u8bc6\u7b26\n  - field: host\n    # name-\u53c2\u6570\u5b57\u6bb5\u663e\u793a\u540d\u79f0\n    name: \u4e3b\u673aHost\n    # type-\u5b57\u6bb5\u7c7b\u578b,\u6837\u5f0f(\u5927\u90e8\u5206\u6620\u5c04input\u6807\u7b7etype\u5c5e\u6027)\n    type: host\n    # \u662f\u5426\u662f\u5fc5\u8f93\u9879 true-\u5fc5\u586b false-\u53ef\u9009\n    required: true\n  - field: port\n    name: \u7aef\u53e3\n    type: number\n    # \u5f53type\u4e3anumber\u65f6,\u7528range\u8868\u793a\u8303\u56f4\n    range: '[0,65535]'\n    required: true\n    # \u7aef\u53e3\u9ed8\u8ba4\u503c\n    defaultValue: 80\n    # \u53c2\u6570\u8f93\u5165\u6846\u63d0\u793a\u4fe1\u606f\n    placeholder: '\u8bf7\u8f93\u5165\u7aef\u53e3'\n  - field: username\n    name: \u7528\u6237\u540d\n    type: text\n    # \u5f53type\u4e3atext\u65f6,\u7528limit\u8868\u793a\u5b57\u7b26\u4e32\u9650\u5236\u5927\u5c0f\n    limit: 20\n    required: false\n  - field: password\n    name: \u5bc6\u7801\n    type: password\n    required: false\n  - field: ssl\n    name: \u542f\u52a8SSL\n    # \u5f53type\u4e3aboolean\u65f6,\u524d\u7aef\u7528switch\u5c55\u793a\u5f00\u5173\n    type: boolean\n    required: false\n  - field: method\n    name: \u8bf7\u6c42\u65b9\u5f0f\n    type: radio\n    required: true\n    # \u5f53type\u4e3aradio\u5355\u9009\u6846,checkbox\u590d\u9009\u6846\u65f6,option\u8868\u793a\u53ef\u9009\u9879\u503c\u5217\u8868 {name1:value1,name2:value2}\n    options:\n      - label: GET\u8bf7\u6c42\n        value: GET\n      - label: POST\u8bf7\u6c42\n        value: POST\n      - label: PUT\u8bf7\u6c42\n        value: PUT\n      - label: DELETE\u8bf7\u6c42\n        value: DELETE\n")))}c.isMDXComponent=!0}}]);