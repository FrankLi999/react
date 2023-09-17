"use strict";(self.webpackChunk_hawtio_console_assembly=self.webpackChunk_hawtio_console_assembly||[]).push([[421,557],{14421:function(f,s,e){e.r(s);var l=e(67557),i=e(8190),h=e(28416),m=e.n(h),p=e(24470),y=(t,o,r)=>new Promise((a,u)=>{var w=d=>{try{j(r.next(d))}catch(R){u(R)}},b=d=>{try{j(r.throw(d))}catch(R){u(R)}},j=d=>d.done?a(d.value):Promise.resolve(d.value).then(w,b);j((r=r.apply(t,o)).next())});const v=()=>y(void 0,null,function*(){const t=yield i.jolokiaService.readAttribute("hawtio:type=About","HawtioVersion");i.configManager.addProductInfo("Hawtio",t)}),c=t=>{t&&t instanceof Function&&e.e(493).then(e.t.bind(e,61493,23)).then(({getCLS:o,getFID:r,getFCP:a,getLCP:u,getTTFB:w})=>{o(t),r(t),a(t),u(t),w(t)})};(0,i.registerPlugins)(),i.hawtio.addUrl("plugin").bootstrap(),v(),p.createRoot(document.getElementById("root")).render((0,l.jsx)(m().StrictMode,{children:(0,l.jsx)(i.Hawtio,{})})),c()},24470:function(f,s,e){var l=e(31051);if(!0)s.createRoot=l.createRoot,s.hydrateRoot=l.hydrateRoot;else var i},52455:function(f,s,e){/**
 * @license React
 * react-jsx-runtime.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */var l=e(28416),i=Symbol.for("react.element"),h=Symbol.for("react.fragment"),m=Object.prototype.hasOwnProperty,p=l.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner,y={key:!0,ref:!0,__self:!0,__source:!0};function v(c,n,t){var o,r={},a=null,u=null;t!==void 0&&(a=""+t),n.key!==void 0&&(a=""+n.key),n.ref!==void 0&&(u=n.ref);for(o in n)m.call(n,o)&&!y.hasOwnProperty(o)&&(r[o]=n[o]);if(c&&c.defaultProps)for(o in n=c.defaultProps,n)r[o]===void 0&&(r[o]=n[o]);return{$$typeof:i,type:c,key:a,ref:u,props:r,_owner:p.current}}s.Fragment=h,s.jsx=v,s.jsxs=v},67557:function(f,s,e){f.exports=e(52455)}}]);

//# sourceMappingURL=421.a670ddf7.chunk.js.map