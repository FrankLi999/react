"use strict";(self.webpackChunkmy_camel_admin=self.webpackChunkmy_camel_admin||[]).push([[162],{1162:(e,t,a)=>{a.r(t),a.d(t,{default:()=>h});a(1356);var i=a(5848),o=a(9276),r=a(5293),n=a(6381),l=a(3391);const c={"ui:submitButtonOptions":{submitText:"Submit Configuration Data",norender:!1,props:{disabled:!1,className:"btn btn-info"}},application:{"ui:title":"Application","ui:autofocus":!0,"ui:emptyValue":"","ui:autocomplete":"my-camel-integrator"},profile:{"ui:title":"Profile","ui:emptyValue":"","ui:autocomplete":"default","ui:placeholder":"Spring profile"},label:{"ui:title":"Label","ui:emptyValue":"","ui:autocomplete":"master","ui:placeholder":"master"},props:{items:{propKey:{"ui:placeholder":"Property key"},propValue:{"ui:placeholder":"Property value"}}}},s={application:"my-camel-integrator",label:"master",profile:"default",props:[{id:1,propKey:"key",propValue:"value"}]};var u=a(9298),m=a(9065),p=a(9239),d=a(3875),f=a(9152);const h=function(){const[e]=(0,i.lT)(["XSRF-TOKEN"]);return(0,f.jsx)(f.Fragment,{children:(0,f.jsx)(u.A,{fluid:!0,children:(0,f.jsx)(m.A,{children:(0,f.jsx)(p.A,{md:"12",children:(0,f.jsxs)(d.A,{children:[(0,f.jsxs)(d.A.Header,{children:[(0,f.jsx)(d.A.Title,{as:"h4",children:"Camel Integrator Configurations"}),(0,f.jsx)("p",{className:"card-category",children:"Create configurations for an application profile"}),(0,f.jsx)("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:(0,f.jsx)(o.N_,{to:"/integrator/configuration-data",children:"Go to configurations page"})})]}),(0,f.jsx)(d.A.Body,{children:(0,f.jsx)(m.A,{children:(0,f.jsx)(p.A,{md:"12",children:(0,f.jsx)("div",{className:"form",children:(0,f.jsx)(n.Ay,{schema:l.A,uiSchema:c,formData:s,validator:r.Ay,onSubmit:t=>async function(t){console.log("submitted data",t),console.log("submitted for data",t.formData);try{const a={method:"POST",headers:{"Content-Type":"application/json",accept:"application/json","X-XSRF-TOKEN":e["XSRF-TOKEN"]},body:JSON.stringify([t.formData])};await fetch("/my-camel/admin/api/spring-config/configurations",a)}catch(a){console.log(a)}}(t)})})})})}),(0,f.jsx)(d.A.Footer,{className:"card-footer text-center",children:(0,f.jsx)("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:(0,f.jsx)(o.N_,{to:"/integrator/configuration-data",children:"Go to configurations page"})})})]})})})})})}}}]);
//# sourceMappingURL=162.d3a48f5a.chunk.js.map