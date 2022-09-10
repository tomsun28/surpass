"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[88],{4137:function(e,t,r){r.d(t,{Zo:function(){return h},kt:function(){return d}});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function l(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var s=n.createContext({}),c=function(e){var t=n.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},h=function(e){var t=c(e.components);return n.createElement(s.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,s=e.parentName,h=l(e,["components","mdxType","originalType","parentName"]),m=c(r),d=a,u=m["".concat(s,".").concat(d)]||m[d]||p[d]||o;return r?n.createElement(u,i(i({ref:t},h),{},{components:r})):n.createElement(u,i({ref:t},h))}));function d(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,i=new Array(o);i[0]=m;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l.mdxType="string"==typeof e?e:a,i[1]=l;for(var c=2;c<o;c++)i[c]=r[c];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}m.displayName="MDXCreateElement"},273:function(e,t,r){r.r(t),r.d(t,{frontMatter:function(){return l},contentTitle:function(){return s},metadata:function(){return c},toc:function(){return h},default:function(){return m}});var n=r(7462),a=r(3366),o=(r(7294),r(4137)),i=["components"],l={id:"alert_threshold",title:"Threshold alarm configuration",sidebar_label:"Threshold alarm configuration"},s=void 0,c={unversionedId:"help/alert_threshold",id:"help/alert_threshold",title:"Threshold alarm configuration",description:"Configure the alarm threshold (warning alarm, critical alarm, emergency alarm) for the monitoring Metrics, and the system calculates and triggers the alarm according to the threshold configuration and the collected Metric data.",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/alert_threshold.md",sourceDirName:"help",slug:"/help/alert_threshold",permalink:"/en/docs/help/alert_threshold",editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/alert_threshold.md",tags:[],version:"current",frontMatter:{id:"alert_threshold",title:"Threshold alarm configuration",sidebar_label:"Threshold alarm configuration"},sidebar:"docs",previous:{title:"Apache Tomcat",permalink:"/en/docs/help/tomcat"},next:{title:"Threshold trigger expression",permalink:"/en/docs/help/alert_threshold_expr"}},h=[{value:"Operation steps",id:"operation-steps",children:[],level:3}],p={toc:h};function m(e){var t=e.components,l=(0,a.Z)(e,i);return(0,o.kt)("wrapper",(0,n.Z)({},p,l,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},"Configure the alarm threshold (warning alarm, critical alarm, emergency alarm) for the monitoring Metrics, and the system calculates and triggers the alarm according to the threshold configuration and the collected Metric data.         ")),(0,o.kt)("h3",{id:"operation-steps"},"Operation steps"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("strong",{parentName:"li"},"\u3010Alarm configuration\u3011->\u3010Add new threshold\u3011-> \u3010Confirm after configuration\u3011"),"  ")),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"threshold",src:r(6663).Z}),"  "),(0,o.kt)("p",null,"As shown above\uff1a     "),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"Metric object"),"\uff1aSelect the monitoring Metric object for which we need to configure the threshold. Eg\uff1awebsite monitoring type -> summary Metric set -> responseTime-response time Metric",(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Threshold trigger expression"),"\uff1aCalculate and judge whether to trigger the threshold according to this expression. See the page prompts for expression environment variables and operators. Eg\uff1aset the response time greater than 50 to trigger an alarm, and the expression is ",(0,o.kt)("inlineCode",{parentName:"p"},"responseTime > 50"),". For detailed help on threshold expression, see ",(0,o.kt)("a",{parentName:"p",href:"alert_threshold_expr"},"Threshold expression help"),(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Alarm level"),"\uff1aThe alarm level that triggers the threshold, from low to high: warning, critical, emergency.",(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Trigger times"),"\uff1aHow many times will the threshold be triggered before the alarm is really triggered.",(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Notification template"),"\uff1aNotification information Template sent after alarm triggering, See page prompts for template environment variables, eg\uff1a",(0,o.kt)("inlineCode",{parentName:"p"},"${app}.${metrics}.${metric} Metric's value is ${responseTime}, greater than 50 triggers an alarm"),(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Global default"),"\uff1a Set whether this threshold is valid for such global Metrics, and the default is No. After adding a new threshold, you need to associate the threshold with the monitoring object, so that the threshold will take effect for this monitoring.",(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"Enable alarm"),"\uff1aThis alarm threshold configuration is enabled or disabled.   "),(0,o.kt)("ol",{start:2},(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("strong",{parentName:"li"},"Threshold  association monitoring\u26a0\ufe0f \u3010Alarm configuration\u3011-> \u3010Threshold just set\u3011-> \u3010Configure associated monitoring\u3011-> \u3010Confirm after configuration\u3011"),"  ")),(0,o.kt)("blockquote",null,(0,o.kt)("p",{parentName:"blockquote"},(0,o.kt)("strong",{parentName:"p"},"Note\u26a0\ufe0f After adding a new threshold, you need to associate the threshold with the monitoring object(That is, to set this threshold for which monitoring is effective), so that the threshold will take effect for this monitoring."),"\u3002   ")),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"threshold",src:r(6414).Z}),"   "),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"threshold",src:r(2454).Z}),"   "),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"After the threshold alarm is configured, the alarm information that has been successfully triggered can be seen in \u3010alarm center\u3011."),(0,o.kt)("br",{parentName:"p"}),"\n",(0,o.kt)("strong",{parentName:"p"},"If you need to notify the relevant personnel of the alarm information by email, Wechat, DingDing and Feishu, it can be configured in \u3010alarm notification\u3011."),"     "),(0,o.kt)("p",null,"Other issues can be fed back through the communication group ISSUE!"))}m.isMDXComponent=!0},6663:function(e,t,r){t.Z=r.p+"assets/images/alert-threshold-1-d42690576e2d740cd262b0c83841ae91.png"},6414:function(e,t,r){t.Z=r.p+"assets/images/alert-threshold-2-c642d543791b5a6a326ec75f3900582a.png"},2454:function(e,t,r){t.Z=r.p+"assets/images/alert-threshold-3-d92f01483904b4c76a459e6e095a2292.png"}}]);