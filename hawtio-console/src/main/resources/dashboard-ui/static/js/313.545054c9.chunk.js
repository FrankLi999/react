"use strict";(self.webpackChunkmy_camel_admin=self.webpackChunkmy_camel_admin||[]).push([[313],{3313:(e,t,i)=>{i.r(t),i.d(t,{default:()=>h});i(1356);var a=i(5848),o=i(1652),n=i(9276),l=i(5293),r=i(6381),s=i(3391);const c={"ui:submitButtonOptions":{submitText:"Submit Configuration Data",norender:!1,props:{disabled:!1,className:"btn btn-info"}},application:{"ui:title":"Application","ui:autofocus":!0,"ui:emptyValue":"","ui:autocomplete":"my-camel-integrator","ui:disabled":!0},profile:{"ui:title":"Profile","ui:emptyValue":"","ui:autocomplete":"default","ui:placeholder":"Spring profile","ui:disabled":!0},label:{"ui:title":"Label","ui:emptyValue":"","ui:autocomplete":"master","ui:placeholder":"master","ui:disabled":!0},props:{items:{propKey:{"ui:placeholder":"Property key"},propValue:{"ui:placeholder":"Property value"}}}};var d=i(9298),u=i(9065),m=i(9239),p=i(3875),f=i(9152);const h=function(){const[e]=(0,a.lT)(["XSRF-TOKEN"]),{state:t}=(0,o.zy)(),i={...t};return(0,f.jsx)(f.Fragment,{children:(0,f.jsx)(d.A,{fluid:!0,children:(0,f.jsx)(u.A,{children:(0,f.jsx)(m.A,{md:"12",children:(0,f.jsxs)(p.A,{children:[(0,f.jsxs)(p.A.Header,{children:[(0,f.jsx)(p.A.Title,{as:"h4",children:"Camel Integrator Configurations"}),(0,f.jsx)("p",{className:"card-category",children:"Edit configurations for an application profile"}),(0,f.jsx)("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:(0,f.jsx)(n.N_,{to:"/integrator/configuration-data",children:"Go to configurations page"})})]}),(0,f.jsx)(p.A.Body,{children:(0,f.jsx)(u.A,{children:(0,f.jsx)(m.A,{md:"12",children:(0,f.jsx)("div",{className:"form",children:(0,f.jsx)(r.Ay,{schema:s.A,uiSchema:c,formData:i,validator:l.Ay,onSubmit:t=>async function(t){console.log("submitted data",t),console.log("submitted for data",t.formData);try{const i={method:"PUT",headers:{"Content-Type":"application/json",accept:"application/json","X-XSRF-TOKEN":e["XSRF-TOKEN"]},body:JSON.stringify([t.formData])};await fetch("/my-camel/admin/api/spring-config/configurations",i)}catch(i){console.log(i)}}(t)})})})})}),(0,f.jsx)(p.A.Footer,{className:"card-footer text-center",children:(0,f.jsx)("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:(0,f.jsx)(n.N_,{to:"/integrator/configuration-data",children:"Go to configurations page"})})})]})})})})})}}}]);
//# sourceMappingURL=313.545054c9.chunk.js.map