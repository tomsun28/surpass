"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[7417],{4137:(t,e,a)=>{a.d(e,{Zo:()=>u,kt:()=>b});var r=a(7294);function o(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}function n(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);e&&(r=r.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,r)}return a}function l(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?n(Object(a),!0).forEach((function(e){o(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):n(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}function i(t,e){if(null==t)return{};var a,r,o=function(t,e){if(null==t)return{};var a,r,o={},n=Object.keys(t);for(r=0;r<n.length;r++)a=n[r],e.indexOf(a)>=0||(o[a]=t[a]);return o}(t,e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(t);for(r=0;r<n.length;r++)a=n[r],e.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(t,a)&&(o[a]=t[a])}return o}var p=r.createContext({}),m=function(t){var e=r.useContext(p),a=e;return t&&(a="function"==typeof t?t(e):l(l({},e),t)),a},u=function(t){var e=m(t.components);return r.createElement(p.Provider,{value:e},t.children)},s="mdxType",h={inlineCode:"code",wrapper:function(t){var e=t.children;return r.createElement(r.Fragment,{},e)}},c=r.forwardRef((function(t,e){var a=t.components,o=t.mdxType,n=t.originalType,p=t.parentName,u=i(t,["components","mdxType","originalType","parentName"]),s=m(a),c=o,b=s["".concat(p,".").concat(c)]||s[c]||h[c]||n;return a?r.createElement(b,l(l({ref:e},u),{},{components:a})):r.createElement(b,l({ref:e},u))}));function b(t,e){var a=arguments,o=e&&e.mdxType;if("string"==typeof t||o){var n=a.length,l=new Array(n);l[0]=c;var i={};for(var p in e)hasOwnProperty.call(e,p)&&(i[p]=e[p]);i.originalType=t,i[s]="string"==typeof t?t:o,l[1]=i;for(var m=2;m<n;m++)l[m]=a[m];return r.createElement.apply(null,l)}return r.createElement.apply(null,a)}c.displayName="MDXCreateElement"},8769:(t,e,a)=>{a.r(e),a.d(e,{assets:()=>p,contentTitle:()=>l,default:()=>h,frontMatter:()=>n,metadata:()=>i,toc:()=>m});var r=a(7462),o=(a(7294),a(4137));const n={title:"HertzBeat v1.2.2\uff01Support K8S Monitor And More.",author:"tom",author_title:"tom",author_url:"https://github.com/tomsun28",author_image_url:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4",tags:["opensource"]},l=void 0,i={permalink:"/en/blog/2022/11/28/hertzbeat-v1.2.2",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-blog/2022-11-28-hertzbeat-v1.2.2.md",source:"@site/i18n/en/docusaurus-plugin-content-blog/2022-11-28-hertzbeat-v1.2.2.md",title:"HertzBeat v1.2.2\uff01Support K8S Monitor And More.",description:"v1.2.2",date:"2022-11-28T00:00:00.000Z",formattedDate:"November 28, 2022",tags:[{label:"opensource",permalink:"/en/blog/tags/opensource"}],readingTime:4.5,hasTruncateMarker:!1,authors:[{name:"tom",title:"tom",url:"https://github.com/tomsun28",imageURL:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4"}],frontMatter:{title:"HertzBeat v1.2.2\uff01Support K8S Monitor And More.",author:"tom",author_title:"tom",author_url:"https://github.com/tomsun28",author_image_url:"https://avatars.githubusercontent.com/u/24788200?s=400&v=4",tags:["opensource"]},prevItem:{title:"HertzBeat v1.2.3\uff01Support Prometheus,ShenYu and IotDb",permalink:"/en/blog/2022/12/28/hertzbeat-v1.2.3"},nextItem:{title:"HertzBeat v1.2.0 \u53d1\u5e03\uff01\u6613\u7528\u53cb\u597d\u7684\u5f00\u6e90\u5b9e\u65f6\u76d1\u63a7\u7cfb\u7edf",permalink:"/en/blog/2022/10/08/hertzbeat-v1.2.0"}},p={authorsImageUrls:[void 0]},m=[{value:"v1.2.2",id:"v122",level:2},{value:"V1.2.2",id:"v122-1",level:2}],u={toc:m},s="wrapper";function h(t){let{components:e,...a}=t;return(0,o.kt)(s,(0,r.Z)({},u,a,{components:e,mdxType:"MDXLayout"}),(0,o.kt)("h2",{id:"v122"},"v1.2.2"),(0,o.kt)("p",null,"Home: hertzbeat.com | tancloud.cn"),(0,o.kt)("p",null,"Hi guys! HertzBeat v1.2.2 is coming. This release brings significant features. This version we support monitor kubernets, docker, springboot, nacos and database dm, opengauss and more. Also we bring a experimental feature, users can custom define metrics collect from promethues with promql. Fixed several bugs and improved the overall stable usability. And more, linux monitor we support top10 cpu usage metrics, top10 memory usage mertics.",(0,o.kt)("br",{parentName:"p"}),"\n","Let's Try It Now!"),(0,o.kt)("p",null,"Only one docker command is needed to install and experience heartbeat\uff1a\n",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat")),(0,o.kt)("p",null,"Thanks to the contributors! \ud83d\udc4d\ud83d\udc4d"),(0,o.kt)("p",null,"We urgently need contributors to test cases, new application monitoring, documentation, etc., and very welcome you to join. Come on! HertzBeat is so easy!"),(0,o.kt)("p",null,"Feature\uff1a"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/410"},"[manager,collector] support dm database monitor #410")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/411"},"[home] add DM db document supplement #411")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/416"},"[home] support algolia search #416")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/418"},"[collector] support trigger and grading multiple subtasks through -_- placeholder expression #418")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/421"},"WIP:feature support k8s monitor, http monitor nacos, service&http_micro monitor msa #421")," @cuipiheqiuqiu"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/422"},"[manager] support opengauss database monitor #422")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/426"},"[#406][warehose] Add unit test MetricsDataControllerTest.java #426")," @haibo-duan"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/427"},"[#358][manager] Add unit test manager/service/NoticeConfigServiceTest.java #427")," @haibo-duan"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/432"},"[#356][manager] unit test case of manager/service/MailServiceTest.java #432")," @csyshu"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/438"},"[manager,collector] support docker metrics monitor #438")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/448"},"[alerter] implement AlertDefineControllerTest unit case #448")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/449"},"[collector] support spi load AbstractCollect Impl instance #449")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/451"},"[manager] support linux process top10 cpu_usage mem_usage #451")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/453"},"[hertzbeat] support springboot2.0 metrics monitor #453")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/455"},"[manager-monitors]\uff08\u589e\u5f3a\uff09\u5e94\u7528\u670d\u52a1\u68c0\u6d4b-\u7f51\u7ad9\u68c0\u6d4b-\u5206\u9875\uff1a\u6dfb\u52a0\u9ed8\u8ba4name\u5347\u5e8f \uff08enhancement\uff09manager-\u2026 #455")," @luxx-lq"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/456"},"[hertzbeat] update use PromQL to collect metrics from promethues server #456")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/460"},"[manager] support custom monitor api response data code #460"))),(0,o.kt)("p",null,"Bugfix."),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/409"},"\u3010bugfix#408\u3011if logs dir not exist, create logs dir #409")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/413"},"[warehouse] bugfix RealTimeRedisDataStorage wrong extend from #413")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/414"},"end The query closed the dataSet #414")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/415"},"[alerter] bugfix monitor status not change when alert #415")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/424"},"[OS Monitor]bugfix:Fix cpu cores and interrupt acquisition under Orac\u2026 #424")," @assassinfym"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/459"},"[manager] bugfix the gmtUpdate not change when update monitor param #459")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/464"},"[home] fix typo in springboot2.md #464")," @eltociear")),(0,o.kt)("p",null,"Online ",(0,o.kt)("a",{parentName:"p",href:"https://console.tancloud.cn"},"https://console.tancloud.cn"),"."),(0,o.kt)("p",null,"Have Fun!"),(0,o.kt)("hr",null),(0,o.kt)("h2",{id:"v122-1"},"V1.2.2"),(0,o.kt)("p",null,"\u5b98\u7f51: hertzbeat.com | tancloud.cn"),(0,o.kt)("p",null,"\u5927\u5bb6\u597d\uff0cHertzBeat v1.2.2\u53d1\u5e03\u5566\uff01\u8fd9\u4e2a\u7248\u672c\u5e26\u6765\u4e2a\u8d85\u591a\u91cd\u5927\u66f4\u65b0\uff0c\u6211\u4eec\u652f\u6301\u4e86\u5bf9\u4e91\u539f\u751fkubernets, docker\u7684\u76d1\u63a7\uff0c\u652f\u6301\u4e86\u5bf9springboot\u5e94\u7528, nacos\u6ce8\u518c\u53d1\u73b0\u4e2d\u5fc3\uff0c\u8fbe\u68a6\u6570\u636e\u5e93\uff0copengauss\u6570\u636e\u5e93\u7b49\u7684\u6307\u6807\u76d1\u63a7\u3002\u6211\u4eec\u4e5f\u5f15\u5165\u4e86\u4e00\u4e2a\u5b9e\u9a8c\u6027\u7279\u6027\uff0c\u7528\u6237\u53ef\u4ee5\u4f7f\u7528promethues promql \u4ecepromethues server\u62ff\u53d6\u6307\u6807\u6570\u636e\u4f5c\u4e3ahertzbeat\u81ea\u5b9a\u4e49\u76d1\u63a7\u6307\u6807\u6570\u636e\u3002\u5f53\u7136\u6211\u4eec\u4e5f\u65b0\u589e\u4e86\u591a\u4e2a\u6d4b\u8bd5\u7528\u6237\u8986\u76d6\uff0c\u4fee\u590d\u4e86\u591a\u4e2aBUG\u3002\u8fd8\u6709\u4e2a\u5f88\u591a\u7528\u6237\u60f3\u8981\u7684\u66f4\u65b0\uff0c\u6211\u4eec\u65b0\u589e\u4e86\u5bf9linux\u76d1\u63a7\u7684top10 cpu \u5185\u5b58\u5229\u7528\u7387\u7684\u8fdb\u7a0b\u76d1\u63a7\u6307\u6807\u3002\u6709\u4e2a\u8fd9\u4e2a\u6307\u6807\uff0c\u6211\u4eec\u5c31\u53ef\u4ee5\u5e72\u5f88\u591a\u4e8b\u60c5\u3002\u6bd4\u5982\u76d1\u63a7\u67d0\u4e2a\u8fdb\u7a0bCPU\u5f02\u5e38\uff0c\u5185\u5b58\u7206\u6ee1\u5565\u7684\u3002\u5feb\u6765\u8bd5\u8bd5\u5427\uff01"),(0,o.kt)("p",null,"\u53ea\u9700\u8981\u4e00\u6761docker\u547d\u4ee4\u5373\u53ef\u5b89\u88c5\u4f53\u9a8cheartbeat \uff1a\n",(0,o.kt)("inlineCode",{parentName:"p"},"docker run -d -p 1157:1157 --name hertzbeat tancloud/hertzbeat")),(0,o.kt)("p",null,"\u611f\u8c22hertzbeat\u8d21\u732e\u8005\u4eec\u7684\u8d21\u732e\uff01\ud83d\udc4d\ud83d\udc4d"),(0,o.kt)("p",null,"\u6211\u4eec\u6025\u9700\u5bf9\u6d4b\u8bd5\u7528\u4f8b\uff0c\u65b0\u589e\u5e94\u7528\u76d1\u63a7\uff0c\u6587\u6863\u7b49\u5404\u65b9\u9762\u7684\u8d21\u732e\u8005\uff0c\u975e\u5e38\u6b22\u8fce\u60a8\u7684\u52a0\u5165\u3002\u5feb\u6765\u5427\uff0cHertzBeat\u4e0a\u624b\u975e\u5e38\u7b80\u5355\uff01"),(0,o.kt)("p",null,"Feature\uff1a"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/410"},"[manager,collector] support dm database monitor #410")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/411"},"[home] add DM db document supplement #411")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/416"},"[home] support algolia search #416")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/418"},"[collector] support trigger and grading multiple subtasks through -_- placeholder expression #418")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/421"},"WIP:feature support k8s monitor, http monitor nacos, service&http_micro monitor msa #421")," @cuipiheqiuqiu"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/422"},"[manager] support opengauss database monitor #422")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/426"},"[#406][warehose] Add unit test MetricsDataControllerTest.java #426")," @haibo-duan"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/427"},"[#358][manager] Add unit test manager/service/NoticeConfigServiceTest.java #427")," @haibo-duan"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/432"},"[#356][manager] unit test case of manager/service/MailServiceTest.java #432")," @csyshu"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/438"},"[manager,collector] support docker metrics monitor #438")," @TJxiaobao"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/448"},"[alerter] implement AlertDefineControllerTest unit case #448")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/449"},"[collector] support spi load AbstractCollect Impl instance #449")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/451"},"[manager] support linux process top10 cpu_usage mem_usage #451")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/453"},"[hertzbeat] support springboot2.0 metrics monitor #453")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/455"},"[manager-monitors]\uff08\u589e\u5f3a\uff09\u5e94\u7528\u670d\u52a1\u68c0\u6d4b-\u7f51\u7ad9\u68c0\u6d4b-\u5206\u9875\uff1a\u6dfb\u52a0\u9ed8\u8ba4name\u5347\u5e8f \uff08enhancement\uff09manager-\u2026 #455")," @luxx-lq"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/456"},"[hertzbeat] update use PromQL to collect metrics from promethues server #456")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/460"},"[manager] support custom monitor api response data code #460"))),(0,o.kt)("p",null,"Bugfix."),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/409"},"\u3010bugfix#408\u3011if logs dir not exist, create logs dir #409")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/413"},"[warehouse] bugfix RealTimeRedisDataStorage wrong extend from #413")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/414"},"end The query closed the dataSet #414")," @Ceilzcx"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/415"},"[alerter] bugfix monitor status not change when alert #415")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/424"},"[OS Monitor]bugfix:Fix cpu cores and interrupt acquisition under Orac\u2026 #424")," @assassinfym"),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/459"},"[manager] bugfix the gmtUpdate not change when update monitor param #459")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/dromara/hertzbeat/pull/464"},"[home] fix typo in springboot2.md #464")," @eltociear")),(0,o.kt)("hr",null))}h.isMDXComponent=!0}}]);