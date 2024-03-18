"use strict";(self.webpackChunktancloud=self.webpackChunktancloud||[]).push([[6118],{4137:(t,e,a)=>{a.d(e,{Zo:()=>p,kt:()=>b});var l=a(7294);function n(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}function r(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(t);e&&(l=l.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,l)}return a}function i(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?r(Object(a),!0).forEach((function(e){n(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):r(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}function d(t,e){if(null==t)return{};var a,l,n=function(t,e){if(null==t)return{};var a,l,n={},r=Object.keys(t);for(l=0;l<r.length;l++)a=r[l],e.indexOf(a)>=0||(n[a]=t[a]);return n}(t,e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);for(l=0;l<r.length;l++)a=r[l],e.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(t,a)&&(n[a]=t[a])}return n}var _=l.createContext({}),o=function(t){var e=l.useContext(_),a=e;return t&&(a="function"==typeof t?t(e):i(i({},e),t)),a},p=function(t){var e=o(t.components);return l.createElement(_.Provider,{value:e},t.children)},m="mdxType",k={inlineCode:"code",wrapper:function(t){var e=t.children;return l.createElement(l.Fragment,{},e)}},u=l.forwardRef((function(t,e){var a=t.components,n=t.mdxType,r=t.originalType,_=t.parentName,p=d(t,["components","mdxType","originalType","parentName"]),m=o(a),u=n,b=m["".concat(_,".").concat(u)]||m[u]||k[u]||r;return a?l.createElement(b,i(i({ref:e},p),{},{components:a})):l.createElement(b,i({ref:e},p))}));function b(t,e){var a=arguments,n=e&&e.mdxType;if("string"==typeof t||n){var r=a.length,i=new Array(r);i[0]=u;var d={};for(var _ in e)hasOwnProperty.call(e,_)&&(d[_]=e[_]);d.originalType=t,d[m]="string"==typeof t?t:n,i[1]=d;for(var o=2;o<r;o++)i[o]=a[o];return l.createElement.apply(null,i)}return l.createElement.apply(null,a)}u.displayName="MDXCreateElement"},7459:(t,e,a)=>{a.r(e),a.d(e,{assets:()=>_,contentTitle:()=>i,default:()=>k,frontMatter:()=>r,metadata:()=>d,toc:()=>o});var l=a(7462),n=(a(7294),a(4137));const r={id:"doris_be",title:"\u76d1\u63a7\uff1aDORIS\u6570\u636e\u5e93BE\u76d1\u63a7",sidebar_label:"DORIS\u6570\u636e\u5e93BE",keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u5f00\u6e90\u6570\u636e\u5e93\u76d1\u63a7","DORIS\u6570\u636e\u5e93BE\u76d1\u63a7"]},i=void 0,d={unversionedId:"help/doris_be",id:"help/doris_be",title:"\u76d1\u63a7\uff1aDORIS\u6570\u636e\u5e93BE\u76d1\u63a7",description:"\u5bf9DORIS\u6570\u636e\u5e93FE\u7684\u901a\u7528\u6027\u80fd\u6307\u6807\u8fdb\u884c\u91c7\u96c6\u76d1\u63a7\u3002\u652f\u6301DORIS2.0.0\u3002",source:"@site/i18n/en/docusaurus-plugin-content-docs/current/help/doris_be.md",sourceDirName:"help",slug:"/help/doris_be",permalink:"/en/docs/help/doris_be",draft:!1,editUrl:"https://github.com/dromara/hertzbeat/edit/master/home/i18n/en/docusaurus-plugin-content-docs/current/help/doris_be.md",tags:[],version:"current",frontMatter:{id:"doris_be",title:"\u76d1\u63a7\uff1aDORIS\u6570\u636e\u5e93BE\u76d1\u63a7",sidebar_label:"DORIS\u6570\u636e\u5e93BE",keywords:["\u5f00\u6e90\u76d1\u63a7\u7cfb\u7edf","\u5f00\u6e90\u6570\u636e\u5e93\u76d1\u63a7","DORIS\u6570\u636e\u5e93BE\u76d1\u63a7"]},sidebar:"docs",previous:{title:"Spark Monitor",permalink:"/en/docs/help/spark"},next:{title:"DORIS\u6570\u636e\u5e93FE",permalink:"/en/docs/help/doris_fe"}},_={},o=[{value:"\u914d\u7f6e\u53c2\u6570",id:"\u914d\u7f6e\u53c2\u6570",level:3},{value:"\u91c7\u96c6\u6307\u6807",id:"\u91c7\u96c6\u6307\u6807",level:3},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_load_channel_count",id:"\u6307\u6807\u96c6\u5408doris_be_load_channel_count",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_memtable_flush_total",id:"\u6307\u6807\u96c6\u5408doris_be_memtable_flush_total",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_plan_fragment_count",id:"\u6307\u6807\u96c6\u5408doris_be_plan_fragment_count",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_process_thread_num",id:"\u6307\u6807\u96c6\u5408doris_be_process_thread_num",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_query_scan_rows",id:"\u6307\u6807\u96c6\u5408doris_be_query_scan_rows",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_result_buffer_block_count",id:"\u6307\u6807\u96c6\u5408doris_be_result_buffer_block_count",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_send_batch_thread_pool_queue_size",id:"\u6307\u6807\u96c6\u5408doris_be_send_batch_thread_pool_queue_size",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_tablet_base_max_compaction_score",id:"\u6307\u6807\u96c6\u5408doris_be_tablet_base_max_compaction_score",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_timeout_canceled_fragment_count",id:"\u6307\u6807\u96c6\u5408doris_be_timeout_canceled_fragment_count",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_load_rows",id:"\u6307\u6807\u96c6\u5408doris_be_load_rows",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_all_rowsets_num",id:"\u6307\u6807\u96c6\u5408doris_be_all_rowsets_num",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_all_segments_num",id:"\u6307\u6807\u96c6\u5408doris_be_all_segments_num",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_max_threads",id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_max_threads",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_max_threads",id:"\u6307\u6807\u96c6\u5408doris_be_light_work_max_threads",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_pool_queue_size",id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_pool_queue_size",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_pool_queue_size",id:"\u6307\u6807\u96c6\u5408doris_be_light_work_pool_queue_size",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_active_threads",id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_active_threads",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_active_threads",id:"\u6307\u6807\u96c6\u5408doris_be_light_work_active_threads",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_compaction_bytes_total",id:"\u6307\u6807\u96c6\u5408doris_be_compaction_bytes_total",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_disks_avail_capacity",id:"\u6307\u6807\u96c6\u5408doris_be_disks_avail_capacity",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_disks_total_capacity",id:"\u6307\u6807\u96c6\u5408doris_be_disks_total_capacity",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_local_bytes_read_total",id:"\u6307\u6807\u96c6\u5408doris_be_local_bytes_read_total",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_local_bytes_written_total",id:"\u6307\u6807\u96c6\u5408doris_be_local_bytes_written_total",level:4},{value:"\u6307\u6807\u96c6\u5408\uff1adoris_be_memory_allocated_bytes",id:"\u6307\u6807\u96c6\u5408doris_be_memory_allocated_bytes",level:4}],p={toc:o},m="wrapper";function k(t){let{components:e,...a}=t;return(0,n.kt)(m,(0,l.Z)({},p,a,{components:e,mdxType:"MDXLayout"}),(0,n.kt)("blockquote",null,(0,n.kt)("p",{parentName:"blockquote"},"\u5bf9DORIS\u6570\u636e\u5e93FE\u7684\u901a\u7528\u6027\u80fd\u6307\u6807\u8fdb\u884c\u91c7\u96c6\u76d1\u63a7\u3002\u652f\u6301DORIS2.0.0\u3002")),(0,n.kt)("h3",{id:"\u914d\u7f6e\u53c2\u6570"},"\u914d\u7f6e\u53c2\u6570"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u53c2\u6570\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u53c2\u6570\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u76d1\u63a7Host"),(0,n.kt)("td",{parentName:"tr",align:null},"\u88ab\u76d1\u63a7\u7684\u5bf9\u7aefIPV4\uff0cIPV6\u6216\u57df\u540d\u3002\u6ce8\u610f\u26a0\ufe0f\u4e0d\u5e26\u534f\u8bae\u5934(eg: https://, http://)")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u4efb\u52a1\u540d\u79f0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u6807\u8bc6\u6b64\u76d1\u63a7\u7684\u540d\u79f0\uff0c\u540d\u79f0\u9700\u8981\u4fdd\u8bc1\u552f\u4e00\u6027")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u7aef\u53e3"),(0,n.kt)("td",{parentName:"tr",align:null},"\u6570\u636e\u5e93\u5bf9\u5916\u63d0\u4f9b\u7684\u7aef\u53e3\uff0c\u9ed8\u8ba4\u4e3a8040")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u67e5\u8be2\u8d85\u65f6\u65f6\u95f4"),(0,n.kt)("td",{parentName:"tr",align:null},"\u8bbe\u7f6e\u8fde\u63a5\u672a\u54cd\u5e94\u7684\u8d85\u65f6\u65f6\u95f4\uff0c\u5355\u4f4dms\u6beb\u79d2\uff0c\u9ed8\u8ba43000\u6beb\u79d2")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u6570\u636e\u5e93\u540d\u79f0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u6570\u636e\u5e93\u5b9e\u4f8b\u540d\u79f0\uff0c\u53ef\u9009")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u91c7\u96c6\u95f4\u9694"),(0,n.kt)("td",{parentName:"tr",align:null},"\u76d1\u63a7\u5468\u671f\u6027\u91c7\u96c6\u6570\u636e\u95f4\u9694\u65f6\u95f4\uff0c\u5355\u4f4d\u79d2\uff0c\u53ef\u8bbe\u7f6e\u7684\u6700\u5c0f\u95f4\u9694\u4e3a30\u79d2")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u662f\u5426\u63a2\u6d4b"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65b0\u589e\u76d1\u63a7\u524d\u662f\u5426\u5148\u63a2\u6d4b\u68c0\u67e5\u76d1\u63a7\u53ef\u7528\u6027\uff0c\u63a2\u6d4b\u6210\u529f\u624d\u4f1a\u7ee7\u7eed\u65b0\u589e\u4fee\u6539\u64cd\u4f5c")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"\u63cf\u8ff0\u5907\u6ce8"),(0,n.kt)("td",{parentName:"tr",align:null},"\u66f4\u591a\u6807\u8bc6\u548c\u63cf\u8ff0\u6b64\u76d1\u63a7\u7684\u5907\u6ce8\u4fe1\u606f\uff0c\u7528\u6237\u53ef\u4ee5\u5728\u8fd9\u91cc\u5907\u6ce8\u4fe1\u606f")))),(0,n.kt)("h3",{id:"\u91c7\u96c6\u6307\u6807"},"\u91c7\u96c6\u6307\u6807"),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_load_channel_count"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_load_channel_count"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6253\u5f00\u7684 load channel \u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_memtable_flush_total"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_memtable_flush_total"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"memtable\u5199\u5165\u78c1\u76d8\u7684\u4e2a\u6570\u7d2f\u8ba1\u503c")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_plan_fragment_count"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_plan_fragment_count"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u5df2\u63a5\u6536\u7684 fragment instance \u7684\u6570\u91cf")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_process_thread_num"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_process_thread_num"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"BE \u8fdb\u7a0b\u7ebf\u7a0b\u6570\u3002\u901a\u8fc7 ",(0,n.kt)("inlineCode",{parentName:"td"},"/proc/pid/task")," \u91c7\u96c6")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_query_scan_rows"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_query_scan_rows"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u8bfb\u53d6\u884c\u6570\u7684\u7d2f\u8ba1\u503c\u3002\u8fd9\u91cc\u53ea\u7edf\u8ba1\u8bfb\u53d6 Olap \u8868\u7684\u6570\u636e\u91cf\u3002\u5e76\u4e14\u662f RawRowsRead\uff08\u90e8\u5206\u6570\u636e\u884c\u53ef\u80fd\u88ab\u7d22\u5f15\u8df3\u8fc7\uff0c\u5e76\u6ca1\u6709\u771f\u6b63\u8bfb\u53d6\uff0c\u4f46\u4ecd\u4f1a\u8bb0\u5f55\u5230\u8fd9\u4e2a\u503c\u4e2d\uff09")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_result_buffer_block_count"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_result_buffer_block_count"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u67e5\u8be2\u7ed3\u679c\u7f13\u5b58\u4e2d\u7684 query \u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_send_batch_thread_pool_queue_size"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_send_batch_thread_pool_queue_size"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5bfc\u5165\u65f6\u7528\u4e8e\u53d1\u9001\u6570\u636e\u5305\u7684\u7ebf\u7a0b\u6c60\u7684\u6392\u961f\u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_tablet_base_max_compaction_score"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_tablet_base_max_compaction_score"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6700\u5927\u7684 Base Compaction Score")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_timeout_canceled_fragment_count"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_timeout_canceled_fragment_count"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u56e0\u8d85\u65f6\u800c\u88ab\u53d6\u6d88\u7684 fragment instance \u6570\u91cf\u7d2f\u8ba1\u503c")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_load_rows"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_load_rows"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u901a\u8fc7 tablet sink \u53d1\u9001\u7684\u884c\u6570\u7d2f\u8ba1")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_all_rowsets_num"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_all_rowsets_num"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6240\u6709 rowset \u7684\u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_all_segments_num"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_all_segments_num"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5f53\u524d\u6240\u6709 segment \u7684\u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_max_threads"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_max_threads"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc heavy\u7ebf\u7a0b\u6c60\u7ebf\u7a0b\u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_light_work_max_threads"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_max_threads"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc light\u7ebf\u7a0b\u6c60\u7ebf\u7a0b\u4e2a\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_pool_queue_size"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_pool_queue_size"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc heavy\u7ebf\u7a0b\u6c60\u961f\u5217\u6700\u5927\u957f\u5ea6,\u8d85\u8fc7\u5219\u963b\u585e\u63d0\u4ea4work")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_light_work_pool_queue_size"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_pool_queue_size"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc light\u7ebf\u7a0b\u6c60\u961f\u5217\u6700\u5927\u957f\u5ea6,\u8d85\u8fc7\u5219\u963b\u585e\u63d0\u4ea4work")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_heavy_work_active_threads"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_heavy_work_active_threads"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc heavy\u7ebf\u7a0b\u6c60\u6d3b\u8dc3\u7ebf\u7a0b\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_light_work_active_threads"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_light_work_active_threads"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"brpc light\u7ebf\u7a0b\u6c60\u6d3b\u8dc3\u7ebf\u7a0b\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_compaction_bytes_total"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_compaction_bytes_total"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"base"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},"Base Compaction \u7684\u6570\u636e\u91cf\u7d2f\u8ba1")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"cumulative"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},"Cumulative Compaction \u7684\u6570\u636e\u91cf\u7d2f\u8ba1")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_disks_avail_capacity"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_disks_avail_capacity"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"path"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u6307\u5b9a\u6570\u636e\u76ee\u5f55")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},(0,n.kt)("inlineCode",{parentName:"td"},'{path="/path1/"}')," \u8868\u793a ",(0,n.kt)("inlineCode",{parentName:"td"},"/path1")," \u76ee\u5f55\u6240\u5728\u78c1\u76d8\u7684\u5269\u4f59\u7a7a\u95f4")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_disks_total_capacity"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_disks_total_capacity"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"path"),(0,n.kt)("td",{parentName:"tr",align:null},"\u65e0"),(0,n.kt)("td",{parentName:"tr",align:null},"\u6307\u5b9a\u6570\u636e\u76ee\u5f55")),(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},(0,n.kt)("inlineCode",{parentName:"td"},'{path="/path1/"}')," \u8868\u793a ",(0,n.kt)("inlineCode",{parentName:"td"},"/path1")," \u76ee\u5f55\u6240\u5728\u78c1\u76d8\u7684\u5168\u90e8\u7a7a\u95f4")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_local_bytes_read_total"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_local_bytes_read_total"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},"\u7531 ",(0,n.kt)("inlineCode",{parentName:"td"},"LocalFileReader")," \u8bfb\u53d6\u7684\u5b57\u8282\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_local_bytes_written_total"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_local_bytes_written_total"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},"\u7531 ",(0,n.kt)("inlineCode",{parentName:"td"},"LocalFileWriter")," \u5199\u5165\u7684\u5b57\u8282\u6570")))),(0,n.kt)("h4",{id:"\u6307\u6807\u96c6\u5408doris_be_memory_allocated_bytes"},"\u6307\u6807\u96c6\u5408\uff1adoris_be_memory_allocated_bytes"),(0,n.kt)("table",null,(0,n.kt)("thead",{parentName:"table"},(0,n.kt)("tr",{parentName:"thead"},(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u540d\u79f0"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5355\u4f4d"),(0,n.kt)("th",{parentName:"tr",align:null},"\u6307\u6807\u5e2e\u52a9\u63cf\u8ff0"))),(0,n.kt)("tbody",{parentName:"table"},(0,n.kt)("tr",{parentName:"tbody"},(0,n.kt)("td",{parentName:"tr",align:null},"value"),(0,n.kt)("td",{parentName:"tr",align:null},"\u5b57\u8282"),(0,n.kt)("td",{parentName:"tr",align:null},"BE \u8fdb\u7a0b\u7269\u7406\u5185\u5b58\u5927\u5c0f\uff0c\u53d6\u81ea ",(0,n.kt)("inlineCode",{parentName:"td"},"/proc/self/status/VmRSS"))))))}k.isMDXComponent=!0}}]);